package org.sapia.magnet.domain;

import org.junit.Before;
import org.junit.Test;
import org.sapia.magnet.BaseMagnetTestCase;
import org.sapia.magnet.MagnetRunner;

/**
 * @author Jean-Cedric Desrochers
 *
 * <dl>
 * <dt><b>Copyright:</b><dd>Copyright &#169; 2002-2003 <a href="http://www.sapia-oss.org">Sapia Open Source Software</a>. All Rights Reserved.</dd></dt>
 * <dt><b>License:</b><dd>Read the license.txt file of the jar or visit the
 *        <a href="http://www.sapia-oss.org/license.html" target="sapia-license">license page</a> at the Sapia OSS web site</dd></dt>
 * </dl>
 */
public class MagnetRunnerTest extends BaseMagnetTestCase {

  @Before
  public void setUp() throws Exception {
    super.baseSetUp();
  }
  
  @Test
  public void testVersion() throws Exception {
    MagnetRunner.main(new String[] {"-version"});
  }

  @Test
  public void testHelp() throws Exception {
    MagnetRunner.main(new String[] {"-help"});
  }

  @Test
  public void testUnknownOption() throws Exception {
    MagnetRunner.main(new String[] {"-unknown"});
  }

  @Test
  public void testSystemLauncher() throws Exception {
    MagnetRunner.runFile("etc/systemMagnet.xml","dev");
  }
  
}
