package org.sapia.magnet;

import org.junit.Assert;
import org.junit.Test;
import org.sapia.corus.interop.api.message.ContextMessagePart;
import org.sapia.corus.interop.api.message.ParamMessagePart;
import org.sapia.corus.interop.api.message.StatusMessageCommand;
import org.sapia.corus.interop.client.CyclicIdGenerator;
import org.sapia.corus.interop.client.InteropClient;
import org.sapia.corus.interop.http.HttpProtocol;


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
public class InteropClientTest extends BaseMagnetTestCase {

  @Test
  public void testShutdown() throws Exception {
    DummyServer aServer = new DummyServer();
    safellySetInteropProtocol();
    Assert.assertTrue("The server is already shutdown", !aServer.isShutdown());
    
    InteropClient.getInstance().setExitSystemOnShutdown(false);
    InteropClient.getInstance().shutdown();
    Assert.assertTrue("The server is not shutdown", aServer.isShutdown());
  }
  
  @Test
  public void testStatusRequestAndShudown() throws Exception {
    DummyServer aServer = new DummyServer();
    safellySetInteropProtocol();
    Assert.assertTrue("The server is already shudown", !aServer.isShutdown());

    InteropClient client = InteropClient.getInstance();
    StatusMessageCommand.Builder statusBuilder = client.getProtocol().getMessageBuilderFactory().newStatusMessageBuilder();
    statusBuilder.commandId(CyclicIdGenerator.newCommandId());
    InteropClient.getInstance().processStatus(statusBuilder);
    StatusMessageCommand status = statusBuilder.build();
    Assert.assertEquals(1, status.getContexts().size());

    ContextMessagePart context = (ContextMessagePart) status.getContexts().get(0);
    Assert.assertEquals("DummyServer", context.getName());
    Assert.assertEquals(1, context.getParams().size());
    
    ParamMessagePart anParam = context.getParams().get(0);
    Assert.assertEquals("", "isShutdown", anParam.getName());
    Assert.assertEquals("", "false", anParam.getValue());
    
    InteropClient.getInstance().setExitSystemOnShutdown(false);
    InteropClient.getInstance().shutdown();
    Assert.assertTrue("The server is not shudown", aServer.isShutdown());
  }

  private void safellySetInteropProtocol() {
    try {
      InteropClient.getInstance().setProtocol(new HttpProtocol());
    } catch (Exception e) {
    }
  }
}
