package com.made.pact.schematron

import au.com.dius.pact.matchers.BodyMatcher
import au.com.dius.pact.model.{HttpPart, DiffConfig, BodyMismatch}
import org.xml.sax.SAXParseException

class SchematronBodyMatcher extends BodyMatcher {

  def matchSchema(schema:Schema, actual:String) : List[BodyMismatch] ={
   val handler = new ValidationHandler()
   val sch = new SchemaBuilder().BuildAndValidate(schema)
   try {
    val xml = new XmlReader().Read(actual)
    var result = sch.validate(xml, handler) 
    handler.result(schema.example, actual)
   } catch {
     case e : SAXParseException => List( BodyMismatch(schema.example, actual, Some("The actual value could not be parsed as xml")))
   }

  }

  def matchBody(expected:HttpPart, actual:HttpPart, diffConfig:DiffConfig)
    :List[BodyMismatch] = {
      List()
  }
}




