package com.made.pact.schematron

import scala.xml.Elem

class Assertion(val test:String, val msg:String){}

class Pattern (val context:String, val name:String, val asserts:List[Assertion]){}

class Schema(val example:Elem, val patterns:List[Pattern]){}
                                                                                                    
