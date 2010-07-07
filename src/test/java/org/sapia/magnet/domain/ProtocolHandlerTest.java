package org.sapia.magnet.domain;

import org.junit.Test;
import org.sapia.magnet.BaseMagnetTestCase;

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
public class ProtocolHandlerTest extends BaseMagnetTestCase {

  @Test
  public void testBasicPath() throws Exception {
    FileProtocolHandler aHandler = new FileProtocolHandler();
    Path aPath = new Path();
    aPath.setDirectory(System.getProperty("user.dir"));
    aPath.addInclude(new Include("**/*"));
    aPath.addExclude(new Exclude("**/bin/*"));

    System.out.println("INCLUDES: " + aHandler.resolveResources(aPath, SortingOrder.ASCENDING));
  }

  @Test
  public void testBasicHttpPath() throws Exception {
    HttpProtocolHandler aHandler = new HttpProtocolHandler();
    Path aPath = new Path();
    aPath.setProtocol(Path.PROTOCOL_HTTP);
    aPath.setHost("192.168.0.130:8200");
    aPath.setDirectory("/sapia/codebase");
    aPath.addInclude(new Include("interafces.jar"));
    aPath.addInclude(new Include("implAplha.jar"));
    aPath.addExclude(new Exclude("*.jar"));

    System.out.println("INCLUDES: " + aHandler.resolveResources(aPath, SortingOrder.DESCENDING));
  }
  
}
