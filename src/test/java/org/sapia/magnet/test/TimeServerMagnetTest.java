package org.sapia.magnet.test;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.sapia.magnet.MagnetRunner;
import org.sapia.ubik.rmi.naming.remote.Consts;
import org.sapia.ubik.rmi.naming.remote.EmbeddableJNDIServer;


/**
 *
 *
 * @author Jean-Cedric Desrochers
 *
 * <dl>
 * <dt><b>Copyright:</b><dd>Copyright &#169; 2002-2003 <a href="http://www.sapia-oss.org">Sapia Open Source Software</a>. All Rights Reserved.</dd></dt>
 * <dt><b>License:</b><dd>Read the license.txt file of the jar or visit the
 *        <a href="http://www.sapia-oss.org/license.html" target="sapia-license">license page</a> at the Sapia OSS web site</dd></dt>
 * </dl>
 */
public class TimeServerMagnetTest extends TestCase {

  EmbeddableJNDIServer jndi;

  public static void main(String[] args) {
    TestRunner.run(TimeServerMagnetTest.class);
  }

  public TimeServerMagnetTest(String aName) {
    super(aName);
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    jndi = new EmbeddableJNDIServer();
    jndi.start(true);
  }
  
  @Override
  protected void tearDown() throws Exception {
    jndi.stop();
  }

  public void testSystemMagnet() throws Exception {
    StringBuffer aName = new StringBuffer().
          append(System.getProperty("user.dir")).
          append(java.io.File.separator).append("etc").
          append(java.io.File.separator).append("TimeServerMagnet.xml");

    MagnetRunner.main(new String[] { "-debug", "-magnetfile", aName.toString(), "sapia"} );
  }
}