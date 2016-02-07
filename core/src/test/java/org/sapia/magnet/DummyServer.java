package org.sapia.magnet;

import org.sapia.corus.interop.api.ShutdownListener;
import org.sapia.corus.interop.api.StatusRequestListener;
import org.sapia.corus.interop.api.message.ContextMessagePart;
import org.sapia.corus.interop.api.message.InteropMessageBuilderFactory;
import org.sapia.corus.interop.api.message.StatusMessageCommand;
import org.sapia.corus.interop.client.InteropClient;


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
public class DummyServer implements ShutdownListener, StatusRequestListener {

  private boolean _isShutdown = false;
  
  /**
   * Creates a new {@link DummyServer} instance.
   */
  public DummyServer() {
    InteropClient.getInstance().addShutdownListener(this);
    InteropClient.getInstance().addStatusRequestListener(this);
  }
  
  public boolean isShutdown() {
    return _isShutdown;
  }

  @Override
  public void onShutdown() {
    _isShutdown = true;
    System.out.println("Shutting down the dummy server...");
  }

  
  @Override
  public void onStatus(StatusMessageCommand.Builder statusBuilder, InteropMessageBuilderFactory factory) {
    ContextMessagePart context = factory.newContextBuilder()
        .name("DummyServer")
        .param("isShutdown", Boolean.toString(_isShutdown? true : false))
        .build();
    statusBuilder.context(context); 
  }
  
}