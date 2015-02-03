package com.made.pact.schematron

import au.com.dius.pact.model.{BodyMismatch, DiffConfig, Request}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.AllExpectations
import org.specs2.matcher.Expectable

@RunWith(classOf[JUnitRunner])
class SchematronBodyMatcherTest extends Specification with AllExpectations {
    isolated

    var expectedBody: Option[String] = None
    var actualBody: Option[String] = None
    var matchers: Option[Map[String, Map[String, String]]] = None
    val expected = () => Request("", "", None, None, expectedBody, matchers)
    val actual = () => Request("", "", None, None, actualBody, None)

    var diffconfig = DiffConfig(structural = true)

    val matcher = new SchematronBodyMatcher()

    def matchBody(schema:Schema, actualBody:String) = {
      matcher.matchSchema(schema, actualBody)
    }


    def containMessage(s: String) = (a: List[BodyMismatch]) => (
                a.exists((m: BodyMismatch) => m.mismatch.get == s),
                          s"$a does not contain '$s'"
                                  )

    val simpleFeed = <feed xmlns="http://www.w3.org/2005/Atom">
     
      <title>Example Feed</title>
      <subtitle>A subtitle.</subtitle>
      <link href="http://example.org/feed/" rel="self" />
      <link href="http://example.org/" />
      <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>
      <updated>2003-12-13T18:30:02Z</updated>
             
                 
      <entry>
        <title>Atom-Powered Robots Run Amok</title>
        <link rel="self" href="http://example.org/2003/12/13/atom03" />
        <link rel="alternate" type="text/html" href="http://example.org/2003/12/13/atom03.html"/>
        <link rel="edit" href="http://example.org/2003/12/13/atom03/edit"/>
        <id>urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a</id>
        <updated>2003-12-13T18:30:02Z</updated>
        <summary>Some text.</summary>
        <content type="xhtml">
          <div xmlns="http://www.w3.org/1999/xhtml">
            <p>This is the entry content.</p>
          </div>
        </content>
        <author>
          <name>John Doe</name>
          <email>johndoe@example.com</email>
        </author>
      </entry>
    </feed>


    "A valid schema" should {
     
      val schema = new Schema(simpleFeed, List( 
        new Pattern("entry", "Entry assertions", List(
          new Assertion("content[@type='xhtml']", "There must be a content element containing xhtml"),
          new Assertion("count(content) = 1", "There must be exactly one content element"),
          new Assertion("title", "There must be a title element"),
          new Assertion("link[@rel='self']", "There must be a self link")))))

      "return no errors for a valid request" in{
        matchBody(schema, simpleFeed.toString()) must beEmpty   
      }

      "return an error if an element is missing" in {
        val actual = <entry>
                      <link rel="self" />
                      <content type="xhtml" />
                    </entry>
        matchBody(schema, actual.toString()) must containMessage("There must be a title element")
      }

      "return an error if an attribute is missing" in {
        val actual = <entry>
                       <link href="foo" />
                       <title>Hooray</title>
                       <content type="xhtml">blah</content>
                     </entry>
        matchBody(schema, actual.toString()) must containMessage("There must be a self link")
      }

      "return an error if the input is not an xml document" in {
        
        matchBody(schema, "hello world") must containMessage("The actual value could not be parsed as xml")
      }
    }
}
