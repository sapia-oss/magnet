package org.sapia.magnet;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.Assert;
import org.junit.Test;
import org.sapia.magnet.domain.Exclude;
import org.sapia.magnet.domain.Include;
import org.sapia.magnet.domain.Launcher;
import org.sapia.magnet.domain.Magnet;
import org.sapia.magnet.domain.Param;
import org.sapia.magnet.domain.Parameters;
import org.sapia.magnet.domain.Path;
import org.sapia.magnet.domain.Profile;
import org.sapia.magnet.domain.java.Classpath;
import org.sapia.magnet.domain.java.Codebase;
import org.sapia.magnet.domain.java.JavaLauncher;
import org.sapia.magnet.domain.system.SystemLauncher;

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
public class MagnetParserTest extends BaseMagnetTestCase {

  private static String XML_INVALID =
      "<magnet>";

  private static String XML_EMPTY_MAGNET =
      "<magnet xmlns=\"http://schemas.sapia-oss.org/magnet/core/\" />";

  private static String XML_SIMPLE_MAGNET =
      "<MAGNET:magnet xmlns:MAGNET=\"http://schemas.sapia-oss.org/magnet/core/\" name=\"testMagnet\" description=\"A test magnet\">" +
      "    <MAGNET:script type=\"java\" profile=\"prod\" isAbortingOnError=\"true\">" +
      "        System.out.println(\"Production Mode\");" +
      "    </MAGNET:script>" +
      "    <MAGNET:parameters profile=\"prod\">" +
      "        <MAGNET:param name=\"param3\" value=\"value_prod\" />" +
      "    </MAGNET:parameters>" +
      "    <MAGNET:parameters>" +
      "        <MAGNET:param name=\"param1\" value=\"value1\" />" +
      "        <MAGNET:param name=\"param2\" value=\"value2\" />" +
      "    </MAGNET:parameters>" +
      "    <MAGNET:parameters profile=\"dev\">" +
      "        <MAGNET:param name=\"param3\" value=\"value_dev\" />" +
      "    </MAGNET:parameters>" +
      "    <MAGNET:launcher type=\"system\" name=\"SapiaHomePage\" command=\"iexplore ${url}\">" +
      "        <MAGNET:profile name=\"dev\">" +
      "            <MAGNET:parameters>" +
      "                <MAGNET:param name=\"url\" value=\"http://dev.sapia.org\" />" +
      "            </MAGNET:parameters>" +
      "        </MAGNET:profile>" +
      "    </MAGNET:launcher>" +
      "</MAGNET:magnet>";

  private static String XML_INVALID_LAUNCHER =
      "<MAGNET:magnet xmlns:MAGNET=\"http://schemas.sapia-oss.org/magnet/core/\" name=\"testMagnet\" description=\"A test magnet\">" +
      "    <MAGNET:parameters>" +
      "        <MAGNET:param name=\"param1\" value=\"value1\" />" +
      "        <MAGNET:param name=\"param2\" value=\"value2\" />" +
      "    </MAGNET:parameters>" +
      "    <MAGNET:launcher type=\"invalid\" name=\"SapiaHomePage\">" +
      "    </MAGNET:launcher>" +
      "</MAGNET:magnet>";

  private static String XML_JAVA_LAUNCHER =
      "<MAGNET:magnet xmlns:MAGNET=\"http://schemas.sapia-oss.org/magnet/core/\" name=\"testMagnet\" description=\"A test magnet\">" +
      "    <MAGNET:codebase id=\"remote\">" +
      "        <MAGNET:path protocol=\"http\" host=\"dev.sapia.org\" directory=\"/codebase/lib\">" +
      "            <MAGNET:include pattern=\"interfaces.jar\" />" +
      "        </MAGNET:path>" +
      "    </MAGNET:codebase>" +
      "    <MAGNET:classpath id=\"main\">" +
      "        <MAGNET:path directory=\"/lib/common\">" +
      "            <MAGNET:include pattern=\"*.jar\" />" +
      "        </MAGNET:path>" +
      "    </MAGNET:classpath>" +
      "    <MAGNET:classpath id=\"interface\" parent=\"main\">" +
      "        <MAGNET:path directory=\"/lib\">" +
      "            <MAGNET:include pattern=\"*.jar\" />" +
      "            <MAGNET:exclude pattern=\"common/*.jar\" />" +
      "        </MAGNET:path>" +
      "    </MAGNET:classpath>" +
      "    <MAGNET:launcher type=\"java\" name=\"HelloWorld\" default=\"dev\" " +
      "                  mainClass=\"org.sapia.magnet.testClass\" args=\"${app.args}\" isDaemon=\"true\" >" +
      "        <MAGNET:profile name=\"dev\">" +
      "            <MAGNET:parameters>" +
      "                <MAGNET:param name=\"app.args\" value=\"HelloWorld\" />" +
      "            </MAGNET:parameters>" +
      "        </MAGNET:profile>" +
      "    </MAGNET:launcher>" +
      "</MAGNET:magnet>";

  private static String XML_JAVA_PROFILE =
      "<MAGNET:magnet xmlns:MAGNET=\"http://schemas.sapia-oss.org/magnet/core/\" name=\"testMagnet\" description=\"A test magnet\">" +
      "    <MAGNET:codebase id=\"remote\">" +
      "        <MAGNET:path protocol=\"http\" host=\"dev.sapia.org\" directory=\"/codebase/lib\">" +
      "            <MAGNET:include pattern=\"interfaces.jar\" />" +
      "        </MAGNET:path>" +
      "    </MAGNET:codebase>" +
      "    <MAGNET:classpath id=\"main\">" +
      "        <MAGNET:path directory=\"/lib/common\">" +
      "            <MAGNET:include pattern=\"*.jar\" />" +
      "        </MAGNET:path>" +
      "    </MAGNET:classpath>" +
      "    <MAGNET:launcher type=\"java\" name=\"HelloWorld\" default=\"dev\" " +
      "                  mainClass=\"org.sapia.magnet.testClass\" args=\"${app.args}\" isDaemon=\"true\" >" +
      "        <MAGNET:profile name=\"dev\">" +
      "            <MAGNET:parameters>" +
      "                <MAGNET:param name=\"app.args\" value=\"HelloWorld\" />" +
      "            </MAGNET:parameters>" +
      "            <MAGNET:codebase parent=\"remote\" />" +
      "            <MAGNET:classpath parent=\"main\">" +
      "                <MAGNET:path directory=\"/lib\">" +
      "                    <MAGNET:include pattern=\"*.jar\" />" +
      "                    <MAGNET:exclude pattern=\"common/*.jar\" />" +
      "                </MAGNET:path>" +
      "            </MAGNET:classpath>" +
      "        </MAGNET:profile>" +
      "    </MAGNET:launcher>" +
      "</MAGNET:magnet>";

  @Test
  public void testInvalidXml() throws Exception {
    Level aLevel = LogManager.getRootLogger().getLevel();
    LogManager.getRootLogger().setLevel(Level.OFF);
    try {
      ByteArrayInputStream anInputStream = new ByteArrayInputStream(XML_INVALID.getBytes());
      new MagnetParser().parse(anInputStream);
      Assert.fail("Should not be able to parse an invalid xml");
      
    } catch (MagnetException expected) {
    } finally {
      LogManager.getRootLogger().setLevel(aLevel);
    }
  }

  @Test
  public void testEmptyMagnet() throws Exception {
    ByteArrayInputStream anInputStream = new ByteArrayInputStream(XML_EMPTY_MAGNET.getBytes());
    List<Magnet> someMagnets = new MagnetParser().parse(anInputStream);
    
    Magnet aMagnet = someMagnets.get(0);
    Assert.assertNotNull("The result magnet is null", aMagnet);
    Assert.assertNull("The magnet name is invalid", aMagnet.getName());
    Assert.assertNull("The magnet description is invalid", aMagnet.getDescription());
  }

  @Test
  public void testSimpleMagnet() throws Exception {
    ByteArrayInputStream anInputStream = new ByteArrayInputStream(XML_SIMPLE_MAGNET.getBytes());
    List<Magnet> someMagnets = new MagnetParser().parse(anInputStream);
    Magnet magnet = someMagnets.get(0);

    assertMagnet(0, 0, 0, 0, "testMagnet", "A test magnet", null, 0, 1, 3, 1, magnet);

    assertScript("java", "prod", "System.out.println(\"Production Mode\");", true, magnet.getScripts().iterator().next());

    Iterator<Parameters> someParameters = magnet.getParameters().iterator();

    assertParameters("prod", new Param[] {Param.createNew("param3", "value_prod", Param.SCOPE_MAGNET, null, null)},
            someParameters.next());
    assertParameters(null, new Param[] {Param.createNew("param1", "value1", Param.SCOPE_MAGNET, null, null),
                                        Param.createNew("param2", "value2", Param.SCOPE_MAGNET, null, null)},
            someParameters.next());
    assertParameters("dev", new Param[] {Param.createNew("param3", "value_dev", Param.SCOPE_MAGNET, null, null)},
            someParameters.next());
    
    
    Launcher aLauncher = (Launcher) magnet.getLaunchers().iterator().next();
    Assert.assertTrue("The class of the launch handler is invalid", aLauncher.getLaunchHandler() instanceof SystemLauncher);
    SystemLauncher aSystemLauncher = (SystemLauncher) aLauncher.getLaunchHandler();
    Assert.assertEquals("The type of the system launcher is invalid", "system", aSystemLauncher.getType());
    Assert.assertEquals("The name of the system launcher is invalid", "SapiaHomePage", aSystemLauncher.getName());
    Assert.assertEquals("The default of the launcher is invalid", "iexplore ${url}", aSystemLauncher.getCommand());

    Assert.assertEquals("The size of the profile list is invalid ", 1, aSystemLauncher.getProfiles().size());
    Profile aProfile = (Profile) aSystemLauncher.getProfiles().iterator().next();
    Assert.assertEquals("The name of the profile is invalid", "dev", aProfile.getName());

    assertParameters(null, new Param[] {Param.createNew("url", "http://dev.sapia.org", Param.SCOPE_MAGNET, null, null)},
            aProfile.getParameters());
  }

  @Test
  public void testInvalidLauncher() throws Exception {
    Level aLevel = LogManager.getRootLogger().getLevel();
    LogManager.getRootLogger().setLevel(Level.OFF);
    try {
      ByteArrayInputStream anInputStream = new ByteArrayInputStream(XML_INVALID_LAUNCHER.getBytes());
      new MagnetParser().parse(anInputStream);
      Assert.fail("Should not parse a launcher of type invalid");
      
    } catch (MagnetException expected) {
      
    } finally {
      LogManager.getRootLogger().setLevel(aLevel);
    }
  }

  @Test
  public void testJavaLauncher() throws Exception {
    ByteArrayInputStream anInputStream = new ByteArrayInputStream(XML_JAVA_LAUNCHER.getBytes());
    List<Magnet> someMagnets = new MagnetParser().parse(anInputStream);
    Magnet magnet = someMagnets.get(0);

    assertMagnet(0, 0, 0, 2, "testMagnet", "A test magnet", null, 0, 0, 0, 1, magnet);

    Assert.assertEquals("The size of the codebase list is invalid", 1, magnet.getObjectsFor("Codebase").size());
    Codebase aCodebase = (Codebase) magnet.getObjectsFor("Codebase").iterator().next();
    Path ePath = Path.createNew("http", "dev.sapia.org", "/codebase/lib", null);
    ePath.addInclude(new Include("interfaces.jar"));
    assertCodebase("remote", null, new Path[] {ePath}, null, aCodebase);
    
    Assert.assertEquals("The size of the classpath list is invalid", 2, magnet.getObjectsFor("Classpath").size());
    Iterator<Object> someClasspaths = magnet.getObjectsFor("Classpath").iterator();

    ePath = Path.createNew("file", "localhost", "/lib/common", null);
    ePath.addInclude(new Include("*.jar"));
    assertClasspath("main", null, new Path[] {ePath}, (Classpath) someClasspaths.next());
    
    ePath = Path.createNew("file", "localhost", "/lib", null);
    ePath.addInclude(new Include("*.jar"));
    ePath.addExclude(new Exclude("common/*.jar"));
    assertClasspath("interface", "main", new Path[] {ePath}, (Classpath) someClasspaths.next());

    Assert.assertEquals("The size of the launcher list is invalid", 1, magnet.getLaunchers().size());
    Launcher aLauncher = (Launcher) magnet.getLaunchers().iterator().next();
    Assert.assertTrue("The class of the launch handler is invalid", aLauncher.getLaunchHandler() instanceof JavaLauncher);
    JavaLauncher aJavaLauncher = (JavaLauncher) aLauncher.getLaunchHandler();
    Assert.assertEquals("The type of the java launcher is invalid", "java", aJavaLauncher.getType());
    Assert.assertEquals("The name of the java launcher is invalid", "HelloWorld", aJavaLauncher.getName());
    Assert.assertEquals("The default of the java launcher is invalid", "dev", aJavaLauncher.getDefault());
    Assert.assertEquals("The main class of the java launcher is invalid", "org.sapia.magnet.testClass", aJavaLauncher.getMainClass());
    Assert.assertEquals("The args of the java launcher is invalid", "${app.args}", aJavaLauncher.getArgs());
    Assert.assertTrue("The daemon indicator of the java launcher is invalid", aJavaLauncher.isDaemon());

    Assert.assertEquals("The size of the profile list is invalid ", 1, aJavaLauncher.getProfiles().size());
    Profile aProfile = (Profile) aJavaLauncher.getProfiles().iterator().next();
    Assert.assertEquals("The name of the profile is invalid", "dev", aProfile.getName());

    assertParameters(null, new Param[] {new Param("app.args", "HelloWorld")} , aProfile.getParameters());
 }

  @Test
  public void testJavaProfile() throws Exception {
    ByteArrayInputStream anInputStream = new ByteArrayInputStream(XML_JAVA_PROFILE.getBytes());
    List<Magnet> someMagnets = new MagnetParser().parse(anInputStream);
    Magnet magnet = (Magnet) someMagnets.get(0);

    assertMagnet(0, 0, 0, 2, "testMagnet", "A test magnet", null, 0, 0, 0, 1, magnet);

    Assert.assertEquals("The size of the codebase list is invalid", 1, magnet.getObjectsFor("Codebase").size());
    Codebase aCodebase = (Codebase) magnet.getObjectsFor("Codebase").iterator().next();
    Path ePath = Path.createNew("http", "dev.sapia.org", "/codebase/lib", null);
    ePath.addInclude(new Include("interfaces.jar"));
    assertCodebase("remote", null, new Path[] {ePath}, null, aCodebase);

    Assert.assertEquals("The size of the classpath list is invalid", 1, magnet.getObjectsFor("Classpath").size());
    Iterator<Object> someClasspaths = magnet.getObjectsFor("Classpath").iterator();
    ePath = Path.createNew("file", "localhost", "/lib/common", null);
    ePath.addInclude(new Include("*.jar"));
    assertClasspath("main", null, new Path[] {ePath}, (Classpath) someClasspaths.next());

    Assert.assertEquals("The size of the launcher list is invalid", 1, magnet.getLaunchers().size());
    Launcher aLauncher = (Launcher) magnet.getLaunchers().iterator().next();
    Assert.assertTrue("The class of the launch handler is invalid", aLauncher.getLaunchHandler() instanceof JavaLauncher);
    JavaLauncher aJavaLauncher = (JavaLauncher) aLauncher.getLaunchHandler();
    Assert.assertEquals("The type of the java launcher is invalid", "java", aJavaLauncher.getType());
    Assert.assertEquals("The name of the java launcher is invalid", "HelloWorld", aJavaLauncher.getName());
    Assert.assertEquals("The default of the java launcher is invalid", "dev", aJavaLauncher.getDefault());
    Assert.assertEquals("The main class of the java launcher is invalid", "org.sapia.magnet.testClass", aJavaLauncher.getMainClass());
    Assert.assertEquals("The args of the java launcher is invalid", "${app.args}", aJavaLauncher.getArgs());
    Assert.assertTrue("The daemon indicator of the java launcher is invalid", aJavaLauncher.isDaemon());

    Assert.assertEquals("The size of the profile list is invalid ", 1, aJavaLauncher.getProfiles().size());
    Profile aProfile = (Profile) aJavaLauncher.getProfiles().iterator().next();
    Assert.assertEquals("The name of the profile is invalid", "dev", aProfile.getName());

    assertParameters(null, new Param[] {new Param("app.args", "HelloWorld")}, aProfile.getParameters());

    Assert.assertEquals("The size of the codebase list is invalid", 1, aProfile.getObjectsFor("Codebase").size());
    aCodebase = (Codebase) aProfile.getObjectsFor("Codebase").iterator().next();
    assertCodebase(null, "remote", new Path[0], null, aCodebase);

    Assert.assertEquals("The size of the classpath list is invalid", 1, aProfile.getObjectsFor("Classpath").size());
    ePath = Path.createNew("file", "localhost", "/lib", null);
    ePath.addInclude(new Include("*.jar"));
    ePath.addExclude(new Exclude("common/*.jar"));
    assertClasspath(null, "main", new Path[] {ePath}, (Classpath) aProfile.getObjectsFor("Classpath").iterator().next());
  }

}