package com.made.pact.schematron

import scala.collection.mutable.MutableList
import scala.collection.JavaConversions._
import com.helger.schematron.pure.validation.PSValidationHandlerDefault
import au.com.dius.pact.model.BodyMismatch
import com.helger.schematron.pure.model.PSAssertReport
import org.w3c.dom.Node
import com.helger.commons.state.EContinue;

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

