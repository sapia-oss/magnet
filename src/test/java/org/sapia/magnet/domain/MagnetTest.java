package org.sapia.magnet.domain;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sapia.magnet.BaseMagnetTestCase;
import org.sapia.magnet.MagnetException;
import org.sapia.magnet.MagnetParser;
import org.sapia.magnet.MagnetRenderer;

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
public class MagnetTest extends BaseMagnetTestCase {

  // Test fixtures
  
  @Before
  public void setUp() throws Exception {
    super.baseSetUp();
  }

  @After
  public void tearDown() {
  }
  
  /**
   * 
   * @param aString
   * @return
   * @throws MagnetException
   */
  protected List<Magnet> doParseMagnets(String aString) throws MagnetException {
    MagnetParser aParser = new MagnetParser();
    ByteArrayInputStream anInput = new ByteArrayInputStream(aString.getBytes());
    return aParser.parse(anInput);
  }

  /**
   * 
   * @param someMagnets
   * @param aProfile
   * @throws MagnetException
   */
  protected void doRenderMagnets(List<Magnet> someMagnets, String aProfile) throws MagnetException {
    MagnetRenderer aRenderer = new MagnetRenderer();
    aRenderer.render(someMagnets, aProfile);
  }



  @Test(expected=MagnetException.class)
  public void testInvalidMagnetNamespace() throws Exception {
    StringBuffer aBuffer = new StringBuffer().
        append("<magnet />");
    
    doParseMagnets(aBuffer.toString());
  }


  @Test
  public void testEmptyMagnet() throws Exception {
    StringBuffer aBuffer = new StringBuffer().
        append("<magnet xmlns=\"http://schemas.sapia-oss.org/magnet/core/\" />");

    List<Magnet> someMagnets = doParseMagnets(aBuffer.toString());
    Assert.assertEquals(1, someMagnets.size());
    assertMagnet(0, 0, 0, 0, null, null, null, 0, 0, 0, 0, someMagnets.get(0));

    doRenderMagnets(someMagnets, null);
  }

  @Test
  public void testSimpleMagnet() throws Exception {
    StringBuffer aBuffer = new StringBuffer().
        append("<magnet xmlns=\"http://schemas.sapia-oss.org/magnet/core/\" ").
        append("name=\"testSimpleMagnet\" description=\"This is a simple description\" />");

    List<Magnet> someMagnets = doParseMagnets(aBuffer.toString());
    Assert.assertEquals(1, someMagnets.size());
    assertMagnet(0, 0, 0, 0, "testSimpleMagnet", "This is a simple description", null, 0, 0, 0, 0, someMagnets.get(0));

    doRenderMagnets(someMagnets, null);
  }
  
  
  
  
//  public void testRender_emptyMagnet


}

