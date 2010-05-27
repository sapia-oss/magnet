package org.sapia.magnet.test;

import org.sapia.corus.interop.Param;
import org.sapia.corus.interop.Status;
import org.sapia.corus.interop.Context;
import org.sapia.corus.interop.client.InteropClient;
import org.sapia.corus.interop.http.HttpProtocol;

import junit.framework.TestCase;


/**
 * Class documentation
 *
 * @author <a href="mailto:jc@sapia-oss.org">Jean-Cedric Desrochers</a>
 * <dl>
 * <dt><b>Copyright:</b><dd>Copyright &#169; 2002-2003 <a href="http://www.sapia-oss.org">
 *     Sapia Open Source Software</a>. All Rights Reserved.</dd></dt>
 * <dt><b>License:</b><dd>Read the license.txt file of the jar or visit the
 *     <a href="http://www.sapia-oss.org/license.html" target="sapia-license">license page</a>
 *     at the Sapia OSS web site</dd></dt>
 * </dl>
 */
public class InteropClientTest extends TestCase {

  /**
   * Creates a new InteropClientTest instance.
   */
  public InteropClientTest(String aName) {
    super(aName);
  }
  
  public void testShutdown() throws Exception {
    DummyServer aServer = new DummyServer();
    InteropClient.getInstance().setProtocol(new HttpProtocol());
    assertTrue("The server is already shutdown", !aServer.isShutdown());
    
    InteropClient.getInstance().setExitSystemOnShutdown(false);
    InteropClient.getInstance().shutdown();
    assertTrue("The server is not shutdown", aServer.isShutdown());
  }
  
  public void testStatusRequestAndShudown() throws Exception {
    
    DummyServer aServer = new DummyServer();
    InteropClient.getInstance().setProtocol(new HttpProtocol());
    assertTrue("The server is already shudown", !aServer.isShutdown());
    
    Status aStatus = new Status();
    InteropClient.getInstance().processStatus(aStatus);
    assertEquals("", 1, aStatus.getContexts().size());

    Context aContext = (Context) aStatus.getContexts().get(0);
    assertEquals("", "DummyServer", aContext.getName());
    assertEquals("", 1, aContext.getParams().size());
    
    Param anParam = (Param) aContext.getParams().get(0);
    assertEquals("", "isShutdown", anParam.getName());
    assertEquals("", "false", anParam.getValue());
    
    InteropClient.getInstance().setExitSystemOnShutdown(false);
    InteropClient.getInstance().shutdown();
    assertTrue("The server is not shudown", aServer.isShutdown());
  }
  
}
