package org.sapia.magnet.test;

import org.sapia.corus.interop.Param;
import org.sapia.corus.interop.Status;
import org.sapia.corus.interop.Context;
import org.sapia.corus.interop.client.InteropClient;
import org.sapia.corus.interop.api.ShutdownListener;
import org.sapia.corus.interop.api.StatusRequestListener;


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
  
  public DummyServer() {
    InteropClient.getInstance().addShutdownListener(this);
    InteropClient.getInstance().addStatusRequestListener(this);
  }
  
  public boolean isShutdown() {
    return _isShutdown;
  }
  
  /**
   * @see org.sapia.corus.interop.client.ShutdownListener#onShutdown()
   */
  public void onShutdown() {
    _isShutdown = true;
    System.out.println("Shutting down the dummy server...");
  }

  /**
   * @see org.sapia.corus.interop.client.StatusRequestListener#onStatus(org.sapia.corus.interop.Status)
   */
  public void onStatus(Status status) {
    Param anParam = new Param("isShutdown", (_isShutdown? "true": "false"));
    Context aContext = new Context("DummyServer");
    aContext.addParam(anParam);
    status.addContext(aContext); 
  }
  
}