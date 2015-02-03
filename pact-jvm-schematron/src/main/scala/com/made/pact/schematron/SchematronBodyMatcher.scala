package com.made.pact.schematron

import au.com.dius.pact.matchers.BodyMatcher
import au.com.dius.pact.model.{HttpPart, DiffConfig, BodyMismatch}

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




