package org.sapia.magnet;

import org.junit.Before;
import org.junit.Test;


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
public class SystemMagnetTest extends BaseMagnetTestCase {

  @Before
  public void setUp() throws Exception {
    super.baseSetUp();
  }

  @Test
  public void testSystemMagnet() throws Exception {
    if(System.getProperty("os.name").toLowerCase().indexOf("windows") < 0){
      System.out.println("Not launching System magnet - needs to run on Windows platform");
      return;
    }
    
    StringBuffer aName = new StringBuffer().
          append(System.getProperty("user.dir")).
          append(java.io.File.separator).append("etc").
          append(java.io.File.separator).append("systemMagnet.xml");

    for (int i = 0; i < 3; i++) {
      if (i % 3 == 0)
        MagnetRunner.main(new String[] { "-magnetfile", aName.toString(), "-info", "-p", "dev" } );

      else if (i % 3 == 1)
        MagnetRunner.main(new String[] { "-magnetfile", aName.toString(), "-debug", "-p", "test" } );

      else
        MagnetRunner.main(new String[] { "-magnetfile", aName.toString(), "-debug", "-p", "prod" } );
        
      Thread.sleep(1000);
    }
  }
  
}
