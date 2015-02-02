package com.made.pact.schematron

import au.com.dius.pact.matchers.BodyMatcher
import au.com.dius.pact.model.{HttpPart, DiffConfig, BodyMismatch}

class SchematronBodyMatcher extends BodyMatcher {

  def matchBody(expected: HttpPart, actual: HttpPart, diffConfig: DiffConfig): List[BodyMismatch] = {
    List()
  }

  def matchSchema(schema:Schema, actual:String) : List[BodyMismatch] ={
    List()
  }
}
