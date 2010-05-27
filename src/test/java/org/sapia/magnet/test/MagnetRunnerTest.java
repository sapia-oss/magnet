package org.sapia.magnet.test;

import junit.framework.TestCase;
import junit.textui.TestRunner;
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
public class MagnetRunnerTest extends TestCase {

  
  public static void main(String[] args) {
    TestRunner.run(MagnetRunnerTest.class);
  }

  public MagnetRunnerTest(String aName) {
    super(aName);
  }

  public void testVersion() throws Exception {
    MagnetRunner.main(new String[] {"-version"});
  }

  public void testHelp() throws Exception {
    MagnetRunner.main(new String[] {"-help"});
  }

  /*
  public void testMagnetFile() throws Exception {
    MagnetRunner.main(new String[] {"-magnetfile",  "Corus.xml",  "dev", "whatever"});
  }

  public void testDefaultMagnet() throws Exception {
    MagnetRunner.main(new String[] {"dev"});
  }

  public void testDefaultProfile() throws Exception {
    MagnetRunner.main(new String[] {"-file", "etc/myMagnet.xml"});
  }*/

  public void testUnknownOption() throws Exception {
    MagnetRunner.main(new String[] {"-unknown"});
  }

  public void testSystemLauncher() throws Exception {
    MagnetRunner.runFile("etc/systemMagnet.xml","dev");
  }
}