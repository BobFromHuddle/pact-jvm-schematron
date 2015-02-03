package com.made.pact.schematron

import com.helger.schematron.pure.model._
import com.helger.schematron.pure.binding.PSQueryBindingRegistry
import com.helger.schematron.pure.bound.IPSBoundSchema
import com.helger.schematron.pure.validation._
import com.helger.schematron.pure.preprocess.PSPreprocessor

class SchemaBuilder {

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

