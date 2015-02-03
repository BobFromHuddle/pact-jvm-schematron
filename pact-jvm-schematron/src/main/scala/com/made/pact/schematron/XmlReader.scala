package com.made.pact.schematron

import java.io.StringReader
import org.w3c.dom.Node
import org.xml.sax.InputSource
import javax.xml.parsers.DocumentBuilderFactory

class XmlReader {

  def Read(data:String) : Node = {
    val sr = new StringReader(data)
    val is = new InputSource();
    is.setCharacterStream(sr)
    DocumentBuilderFactory.newInstance()
      .newDocumentBuilder()
      .parse(is);
  }
}

