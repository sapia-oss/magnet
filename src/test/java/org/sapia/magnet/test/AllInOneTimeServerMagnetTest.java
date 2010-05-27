package org.sapia.magnet.test;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.sapia.magnet.MagnetRunner;


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
public class AllInOneTimeServerMagnetTest extends TestCase {

  public static void main(String[] args) {
    TestRunner.run(AllInOneTimeServerMagnetTest.class);
  }

  public AllInOneTimeServerMagnetTest(String aName) {
    super(aName);
  }

  public void testSystemMagnet() throws Exception {
    StringBuffer aName = new StringBuffer().
          append(System.getProperty("user.dir")).
          append(java.io.File.separator).append("etc").
          append(java.io.File.separator).append("AllInOneTimeServerMagnet.xml");

    MagnetRunner.main(new String[] { "-debug", "-magnetfile", aName.toString(), "colocated" } );
//    MagnetRunner.main(new String[] { "-debug", "-magnetfile", aName.toString(), "remote" } );
  }
}