package org.sapia.magnet.domain.test;

import java.io.ByteArrayInputStream;
import java.util.List;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.sapia.magnet.MagnetParser;
import org.sapia.magnet.MagnetRenderer;
import org.sapia.magnet.MagnetException;
import org.sapia.magnet.domain.Magnet;

/**
 *
 *
 * @author Jean-Cedric Desrochers
 * <dl>
 * <dt><b>Copyright:</b><dd>Copyright &#169; 2002-2003 <a href="http://www.sapia-oss.org">Sapia Open Source Software</a>. All Rights Reserved.</dd></dt>
 * <dt><b>License:</b><dd>Read the license.txt file of the jar or visit the
 *        <a href="http://www.sapia-oss.org/license.html" target="sapia-license">license page</a> at the Sapia OSS web site</dd></dt>
 * </dl>
 */
public class MagnetTest extends TestCase {

  static {
    org.apache.log4j.BasicConfigurator.configure();
  }

  public static void main(String[] args) {
    TestRunner.run(MagnetTest.class);
  }

  public MagnetTest(String aName) {
    super(aName);
  }

  /**
   *
   */
  public List parseMagnets(String aString) throws MagnetException {
    MagnetParser aParser = new MagnetParser();
    ByteArrayInputStream anInput = new ByteArrayInputStream(aString.getBytes());
    return aParser.parse(anInput);
  }

  /**
   *
   */
  public void renderMagnets(List someMagnets, String aProfile) throws MagnetException {
    MagnetRenderer aRenderer = new MagnetRenderer();
    aRenderer.render(someMagnets, aProfile);
  }


  /**
   *
   */
  public void testInvalidMagnetNamespace() throws Exception {
    org.apache.log4j.Level aLevel =
            org.apache.log4j.LogManager.getRootLogger().getLevel();
    StringBuffer aBuffer = new StringBuffer().
        append("<Magnet />");
    try {
      org.apache.log4j.LogManager.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
      List someMagnets = parseMagnets(aBuffer.toString());
      fail("Should not parse magnet without a namespace");
    } catch (MagnetException me) {
    } finally {
      org.apache.log4j.LogManager.getRootLogger().setLevel(aLevel);
    }
  }


  /**
   *
   */
  public void testEmptyMagnet() throws Exception {
    StringBuffer aBuffer = new StringBuffer().
        append("<Magnet xmlns=\"http://schemas.sapia-oss.org/magnet/core/\" />");

    List someMagnets = parseMagnets(aBuffer.toString());
    assertEquals("", 1, someMagnets.size());

    Magnet aMagnet = (Magnet) someMagnets.get(0);
    assertNull("", aMagnet.getDescription());
    assertEquals("", 0, aMagnet.getElementNames().size());
    assertNull("", aMagnet.getExtends());
    assertEquals("", 0, aMagnet.getLaunchers().size());
    assertEquals("", 0, aMagnet.getLaunchHandlerDefs().size());
    assertNull("", aMagnet.getName());
    assertEquals("", 0, aMagnet.getParameters().size());
    assertEquals("", 0, aMagnet.getParents().size());
    assertEquals("", 0, aMagnet.getProtocolHandlerDefs().size());
    assertEquals("", 0, aMagnet.getScriptHandlerDefs().size());
    assertEquals("", 0, aMagnet.getScripts().size());

    renderMagnets(someMagnets, null);
  }


  /**
   *
   */
  public void testSimpleMagnet() throws Exception {
    StringBuffer aBuffer = new StringBuffer().
        append("<Magnet xmlns=\"http://schemas.sapia-oss.org/magnet/core/\" ").
        append("name=\"testSimpleMagnet\" description=\"This is a simple description\" />");

    List someMagnets = parseMagnets(aBuffer.toString());
    assertEquals("", 1, someMagnets.size());

    Magnet aMagnet = (Magnet) someMagnets.get(0);
    assertEquals("", "This is a simple description", aMagnet.getDescription());
    assertEquals("", 0, aMagnet.getElementNames().size());
    assertNull("", aMagnet.getExtends());
    assertEquals("", 0, aMagnet.getLaunchers().size());
    assertEquals("", 0, aMagnet.getLaunchHandlerDefs().size());
    assertEquals("", "testSimpleMagnet", aMagnet.getName());
    assertEquals("", 0, aMagnet.getParameters().size());
    assertEquals("", 0, aMagnet.getParents().size());
    assertEquals("", 0, aMagnet.getProtocolHandlerDefs().size());
    assertEquals("", 0, aMagnet.getScriptHandlerDefs().size());
    assertEquals("", 0, aMagnet.getScripts().size());

    renderMagnets(someMagnets, null);
  }


}

