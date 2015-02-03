package com.made.pact.schematron

import au.com.dius.pact.matchers.BodyMatcher
import au.com.dius.pact.model.{HttpPart, DiffConfig, BodyMismatch}
import com.helger.schematron.pure.model._
import com.helger.schematron.pure.binding.PSQueryBindingRegistry
import com.helger.schematron.pure.bound.IPSBoundSchema
import com.helger.schematron.pure.validation._
import com.helger.schematron.pure.preprocess.PSPreprocessor
import com.helger.commons.state.EContinue;
import scala.collection.mutable.MutableList
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.StringReader
import org.w3c.dom.Node
import org.xml.sax.InputSource
import javax.xml.parsers.DocumentBuilderFactory
import scala.collection.JavaConversions._
class SchematronBodyMatcher extends BodyMatcher {

  def matchSchema(schema:Schema, actual:String) : List[BodyMismatch] ={
   val handler = new ValidationHandler()
   val sch = new SchemaBuilder().BuildAndValidate(schema)
   val xml = new XmlReader().Read(actual)
   var result = sch.validate(xml, handler) 
   handler.result(schema.example, actual)
  }

  def matchBody(expected:HttpPart, actual:HttpPart, diffConfig:DiffConfig)
    :List[BodyMismatch] = {
      List()
  }
}

class XmlReader {

  val log = LoggerFactory.getLogger("schema-builder")
  
  def Read(data:String) : Node = {
    val sr = new StringReader(data)
    val is = new InputSource();
    is.setCharacterStream(sr)
    DocumentBuilderFactory.newInstance()
      .newDocumentBuilder()
      .parse(is);
  }
}

class SchemaBuilder {

  val log = LoggerFactory.getLogger("schema-builder")

  def BuildAndValidate(input:Schema) :IPSBoundSchema = {
    val sch = new PSSchema()
    for(p <- input.patterns) {  
      val pattern = new PSPattern()
      val rule = new PSRule()
      val title = new PSTitle()
      title.addText(p.name)
      pattern.setTitle(title)
      rule.setContext(p.context)

      for(a <- p.asserts) {  
        val assert = new PSAssertReport(true)
        assert.setTest(a.test)
        assert.addText(a.msg)
        rule.addAssertReport(assert)
      }
      pattern.addRule(rule)
      sch.addPattern(pattern)
    }

    val binding = PSQueryBindingRegistry.getQueryBindingOfNameOrThrow (sch.getQueryBinding ());
    val preprocessor = new PSPreprocessor(binding)
    val processed = preprocessor.getAsPreprocessedSchema(sch)
    binding.bind(processed, null, null)
 
  }

}

class ValidationHandler extends PSValidationHandlerDefault {

   val errors = new MutableList[PSAssertReport]()

    override def onFailedAssert(report:PSAssertReport, test:String, node:Node, 
      idx:Int, context:Any) = {

      errors += report
      EContinue.CONTINUE

    }

    def result (expected:Any, actual:Any) : List[BodyMismatch] = {
      errors.map{ (e:PSAssertReport) =>{ 
        val text = e.getAllTexts.toList mkString "\n"
        BodyMismatch(expected, actual, Some(text), e.getTest()) 
      }
    }.toList
  }
}

