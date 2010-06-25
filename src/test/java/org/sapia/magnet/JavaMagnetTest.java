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
public class JavaMagnetTest extends BaseMagnetTestCase {

  @Before
  public void setup() throws Exception {
    super.baseSetUp();
  }
  
  @Test
  public void testSystemMagnet() throws Exception {
    StringBuffer aName = new StringBuffer().
          append(System.getProperty("user.dir")).
          append(java.io.File.separator).append("etc").
          append(java.io.File.separator).append("helloMagnet.xml");

    for (int i = 0; i < 3; i++) {
      if (i % 3 == 0)
        MagnetRunner.main(new String[] { "-magnetfile", aName.toString(), "-info", "-p", "english" } );
      else if (i % 3 == 1)
        MagnetRunner.main(new String[] { "-magnetfile", aName.toString(), "-debug", "-p", "spanish" } );
      else
        MagnetRunner.main(new String[] { "-magnetfile", aName.toString(), "-debug", "-p", "french" } );
    }
  }

  @Test
  public void testSystemMagnet_NoProfile() throws Exception {
    StringBuffer aName = new StringBuffer().
          append(System.getProperty("user.dir")).
          append(java.io.File.separator).append("etc").
          append(java.io.File.separator).append("helloMagnet.xml");

    MagnetRunner.main(new String[]
        { "-magnetfile", aName.toString() } );

  }
  
}
