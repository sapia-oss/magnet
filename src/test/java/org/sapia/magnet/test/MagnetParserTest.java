package org.sapia.magnet.test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Iterator;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;

import org.sapia.magnet.MagnetParser;
import org.sapia.magnet.MagnetException;
import org.sapia.magnet.domain.Exclude;
import org.sapia.magnet.domain.Launcher;
import org.sapia.magnet.domain.Include;
import org.sapia.magnet.domain.Magnet;
import org.sapia.magnet.domain.Param;
import org.sapia.magnet.domain.Parameters;
import org.sapia.magnet.domain.Path;
import org.sapia.magnet.domain.Profile;
import org.sapia.magnet.domain.Script;
import org.sapia.magnet.domain.system.SystemLauncher;
import org.sapia.magnet.domain.java.Classpath;
import org.sapia.magnet.domain.java.Codebase;
import org.sapia.magnet.domain.java.JavaLauncher;

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
public class MagnetParserTest extends TestCase {

  private static String XML_INVALID =
      "<magnet>";

  private static String XML_EMPTY_MAGNET =
      "<magnet xmlns=\"http://schemas.sapia-oss.org/magnet/core/\" />";

  private static String XML_SIMPLE_MAGNET =
      "<MAGNET:magnet xmlns:MAGNET=\"http://schemas.sapia-oss.org/magnet/core/\" name=\"testMagnet\" description=\"A test magnet\">" +
//      "    <MAGNET:extend magnetFile=\"abstractMagnet.xml\">" +
//      "        <MAGNET:path dir=\"../parent\"/>" +
//      "    </MAGNET:extend>" +
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


  public static void main(String[] args) {
    BasicConfigurator.configure();
    TestRunner.run(MagnetParserTest.class);
  }

  public MagnetParserTest(String aName) {
    super(aName);
  }

  public void testInvalidXml() throws Exception {
    Level aLevel = LogManager.getRootLogger().getLevel();
    LogManager.getRootLogger().setLevel(Level.OFF);
    try {
      ByteArrayInputStream anInputStream =
              new ByteArrayInputStream(XML_INVALID.getBytes());
      List someMagnets = new MagnetParser().parse(anInputStream);
      fail("Should not be able to parse an invalid xml");
    } catch (MagnetException de) {
    } finally {
      LogManager.getRootLogger().setLevel(aLevel);
    }
  }

  public void testEmptyMagnet() throws Exception {
    ByteArrayInputStream anInputStream =
            new ByteArrayInputStream(XML_EMPTY_MAGNET.getBytes());
    List someMagnets = new MagnetParser().parse(anInputStream);
    Magnet aMagnet = (Magnet) someMagnets.get(0);

    assertNotNull("The result magnet is null", aMagnet);
    assertNull("The magnet name is invalid", aMagnet.getName());
    assertNull("The magnet description is invalid", aMagnet.getDescription());
  }

  public void testSimpleMagnet() throws Exception {
    ByteArrayInputStream anInputStream =
            new ByteArrayInputStream(XML_SIMPLE_MAGNET.getBytes());
    List someMagnets = new MagnetParser().parse(anInputStream);
    Magnet aMagnet = (Magnet) someMagnets.get(0);

    assertNotNull("The result magnet is null", aMagnet);
    assertEquals("The magnet name is invalid", "testMagnet", aMagnet.getName());
    assertEquals("The magnet description is invalid", "A test magnet", aMagnet.getDescription());

//    assertNotNull("The extend of the magnet is invalid", aMagnet.getExtend());
//    assertEquals("The magnet file of the extend is invalid", "abstractMagnet.xml", aMagnet.getExtend().getMagnetFile());
//    assertNotNull("The path element of the extend is invalid", aMagnet.getExtend().getPath());
//    assertEquals("The protocol of the path is invalid", "file", aMagnet.getExtend().getPath().getProtocol());
//    assertEquals("The directory of the path is invalid", "../parent", aMagnet.getExtend().getPath().getDirectory());
//    assertEquals("The size of the exlude pattern list is invalid", 0, aMagnet.getExtend().getPath().getExcludes().size());
//    assertEquals("The size of the include pattern list is invalid", 0, aMagnet.getExtend().getPath().getIncludes().size());

    assertEquals("The size of the scripts list is invalid ", 1, aMagnet.getScripts().size());
    Iterator someScripts = aMagnet.getScripts().iterator();
    Script aScript = (Script) someScripts.next();
    assertEquals("The type of the script is invalid", "java", aScript.getType());
    assertEquals("The profile of the script is invalid", "prod", aScript.getProfile());
    assertEquals("The abort on error flag of the script is invalid", true, aScript.isAbortingOnError());
    assertEquals("The code of the script is invalid", "System.out.println(\"Production Mode\");", aScript.getCode());

    assertEquals("The size of the parameters list is invalid ", 3, aMagnet.getParameters().size());
    Iterator someParameters = aMagnet.getParameters().iterator();
    
    Parameters aParameters = (Parameters) someParameters.next();
   
    Iterator someParams = null;
    Param aParam = null;

    assertEquals("The profile of the parameters is invalid", "prod", aParameters.getProfile());
    assertEquals("The size of the params list is invalid ", 1, aParameters.getParams().size());
    someParams = aParameters.getParams().iterator();
    aParam = (Param) someParams.next();
    assertEquals("The name of the param is invalid", "param3", aParam.getName());
    assertEquals("The value of the param is invalid", "value_prod", aParam.getValue());
    
    aParameters = (Parameters) someParameters.next();
    assertNull("The profile of the parameters is invalid", aParameters.getProfile());
    assertEquals("The size of the params list is invalid ", 2, aParameters.getParams().size());
    someParams = aParameters.getParams().iterator();
    aParam = (Param) someParams.next();
    assertEquals("The name of the param is invalid", "param1", aParam.getName());
    assertEquals("The value of the param is invalid", "value1", aParam.getValue());
    aParam = (Param) someParams.next();
    assertEquals("The name of the param is invalid", "param2", aParam.getName());
    assertEquals("The value of the param is invalid", "value2", aParam.getValue());

    aParameters = (Parameters) someParameters.next();
    assertEquals("The profile of the parameters is invalid", "dev", aParameters.getProfile());
    assertEquals("The size of the params list is invalid ", 1, aParameters.getParams().size());
    someParams = aParameters.getParams().iterator();
    aParam = (Param) someParams.next();
    assertEquals("The name of the param is invalid", "param3", aParam.getName());
    assertEquals("The value of the param is invalid", "value_dev", aParam.getValue());
   
    assertEquals("The size of the launcher list is invalid ", 1, aMagnet.getLaunchers().size());
    Launcher aLauncher = (Launcher) aMagnet.getLaunchers().iterator().next();
    assertEquals("The type of the launcher is invalid", "system", aLauncher.getType());

    assertTrue("The class of the launch handler is invalid", aLauncher.getLaunchHandler() instanceof SystemLauncher);
    SystemLauncher aSystemLauncher = (SystemLauncher) aLauncher.getLaunchHandler();
    assertEquals("The type of the system launcher is invalid", "system", aSystemLauncher.getType());
    assertEquals("The name of the system launcher is invalid", "SapiaHomePage", aSystemLauncher.getName());
    assertEquals("The default of the launcher is invalid", "iexplore ${url}", aSystemLauncher.getCommand());

    assertEquals("The size of the profile list is invalid ", 1, aSystemLauncher.getProfiles().size());
    Profile aProfile = (Profile) aSystemLauncher.getProfiles().iterator().next();
    assertEquals("The name of the profile is invalid", "dev", aProfile.getName());

    aParameters = (Parameters) aProfile.getParameters();
    assertNull("The profile of the parameters is invalid", aParameters.getProfile());
    assertEquals("The size of the params list is invalid", 1, aParameters.getParams().size());
    someParams = aParameters.getParams().iterator();
    aParam = (Param) someParams.next();
    assertEquals("The name of the param is invalid", "url", aParam.getName());
    assertEquals("The value of the param is invalid", "http://dev.sapia.org", aParam.getValue());
  }

  public void testInvalidLauncher() throws Exception {
    Level aLevel = LogManager.getRootLogger().getLevel();
    LogManager.getRootLogger().setLevel(Level.OFF);
    try {
      ByteArrayInputStream anInputStream =
              new ByteArrayInputStream(XML_INVALID_LAUNCHER.getBytes());
      List aList = new MagnetParser().parse(anInputStream);
//      fail("Should not parse a launcher of type invalid");
    } catch (MagnetException de) {
    } finally {
      LogManager.getRootLogger().setLevel(aLevel);
    }
  }

  public void testJavaLauncher() throws Exception {
    ByteArrayInputStream anInputStream =
            new ByteArrayInputStream(XML_JAVA_LAUNCHER.getBytes());
    List someMagnets = new MagnetParser().parse(anInputStream);
    Magnet aMagnet = (Magnet) someMagnets.get(0);

    assertNotNull("The result magnet is null", aMagnet);
    assertEquals("The magnet name is invalid", "testMagnet", aMagnet.getName());
    assertEquals("The magnet description is invalid", "A test magnet", aMagnet.getDescription());
    assertNull("The extend of the magnet is invalid", aMagnet.getExtends());
    assertEquals("The size of the script list is invalid", 0, aMagnet.getScripts().size());
    assertEquals("The size of the parameters list is invalid", 0, aMagnet.getParameters().size());

    assertEquals("The size of the codebase list is invalid", 1, aMagnet.getObjectsFor("Codebase").size());
    Iterator someCodebases = aMagnet.getObjectsFor("Codebase").iterator();
    Codebase aCodebase = (Codebase) someCodebases.next();

    assertEquals("The id of the codebase is invalid", "remote", aCodebase.getId());
    assertNull("The parent of the codebase is invalid", aCodebase.getParent());
    assertEquals("The protocol of the path is invalid", "http", ((Path) aCodebase.getPaths().get(0)).getProtocol());
    assertEquals("The host of the path is invalid", "dev.sapia.org", ((Path) aCodebase.getPaths().get(0)).getHost());
    assertEquals("The directory of the path is invalid", "/codebase/lib", ((Path) aCodebase.getPaths().get(0)).getDirectory());
    assertEquals("The size of the exclude list is invalid", 0, ((Path) aCodebase.getPaths().get(0)).getExcludes().size());
    assertEquals("The size of the include list is invalid", 1, ((Path) aCodebase.getPaths().get(0)).getIncludes().size());
    Include anInclude = (Include) ((Path) aCodebase.getPaths().get(0)).getIncludes().iterator().next();
    assertEquals("The pattern of the include is invalid", "interfaces.jar", anInclude.getPattern());

    assertEquals("The size of the classpath list is invalid", 2, aMagnet.getObjectsFor("Classpath").size());
    Iterator someClasspaths = aMagnet.getObjectsFor("Classpath").iterator();
    Classpath aClasspath = (Classpath) someClasspaths.next();

    assertEquals("The id of the classpath is invalid", "main", aClasspath.getId());
    assertNull("The parent of the classpath is invalid", aClasspath.getParent());
    assertEquals("The protocol of the path is invalid", "file", ((Path) aClasspath.getPaths().get(0)).getProtocol());
    assertEquals("The host of the path is invalid", "localhost", ((Path) aClasspath.getPaths().get(0)).getHost());
    assertEquals("The directory of the path is invalid", "/lib/common", ((Path) aClasspath.getPaths().get(0)).getDirectory());
    assertEquals("The size of the exclude list is invalid", 0, ((Path) aClasspath.getPaths().get(0)).getExcludes().size());
    assertEquals("The size of the include list is invalid", 1, ((Path) aClasspath.getPaths().get(0)).getIncludes().size());
    anInclude = (Include) ((Path) aClasspath.getPaths().get(0)).getIncludes().iterator().next();
    assertEquals("The pattern of the include is invalid", "*.jar", anInclude.getPattern());

    aClasspath = (Classpath) someClasspaths.next();
    assertEquals("The id of the classpath is invalid", "interface", aClasspath.getId());
    assertEquals("The parent of the classpath is invalid", "main", aClasspath.getParent());
    assertEquals("The protocol of the path is invalid", "file", ((Path) aClasspath.getPaths().get(0)).getProtocol());
    assertEquals("The host of the path is invalid", "localhost", ((Path) aClasspath.getPaths().get(0)).getHost());
    assertEquals("The directory of the path is invalid", "/lib", ((Path) aClasspath.getPaths().get(0)).getDirectory());
    assertEquals("The size of the exclude list is invalid", 1, ((Path) aClasspath.getPaths().get(0)).getExcludes().size());
    Exclude anExclude = (Exclude) ((Path) aClasspath.getPaths().get(0)).getExcludes().iterator().next();
    assertEquals("The pattern of the exclude is invalid", "common/*.jar", anExclude.getPattern());
    assertEquals("The size of the include list is invalid", 1, ((Path) aClasspath.getPaths().get(0)).getIncludes().size());
    anInclude = (Include) ((Path) aClasspath.getPaths().get(0)).getIncludes().iterator().next();
    assertEquals("The pattern of the include is invalid", "*.jar", anInclude.getPattern());

    assertEquals("The size of the parameters list is invalid", 1, aMagnet.getLaunchers().size());
    Launcher aLauncher = (Launcher) aMagnet.getLaunchers().iterator().next();
    assertEquals("The type of the launcher is invalid", "java", aLauncher.getType());

    assertTrue("The class of the launch handler is invalid", aLauncher.getLaunchHandler() instanceof JavaLauncher);
    JavaLauncher aJavaLauncher = (JavaLauncher) aLauncher.getLaunchHandler();
    assertEquals("The type of the java launcher is invalid", "java", aJavaLauncher.getType());
    assertEquals("The name of the java launcher is invalid", "HelloWorld", aJavaLauncher.getName());
    assertEquals("The default of the java launcher is invalid", "dev", aJavaLauncher.getDefault());
    assertEquals("The main class of the java launcher is invalid", "org.sapia.magnet.testClass", aJavaLauncher.getMainClass());
    assertEquals("The args of the java launcher is invalid", "${app.args}", aJavaLauncher.getArgs());
    assertTrue("The daemon indicator of the java launcher is invalid", aJavaLauncher.isDaemon());

    assertEquals("The size of the profile list is invalid ", 1, aJavaLauncher.getProfiles().size());
    Profile aProfile = (Profile) aJavaLauncher.getProfiles().iterator().next();
    assertEquals("The name of the profile is invalid", "dev", aProfile.getName());

    Parameters aParameters = (Parameters) aProfile.getParameters();
    assertNull("The profile of the parameters is invalid", aParameters.getProfile());
    assertEquals("The size of the params list is invalid", 1, aParameters.getParams().size());
    Iterator someParams = aParameters.getParams().iterator();
    Param aParam = (Param) someParams.next();
    assertEquals("The name of the param is invalid", "app.args", aParam.getName());
    assertEquals("The value of the param is invalid", "HelloWorld", aParam.getValue());
 }

  public void testJavaProfile() throws Exception {
    ByteArrayInputStream anInputStream =
            new ByteArrayInputStream(XML_JAVA_PROFILE.getBytes());
    List someMagnets = new MagnetParser().parse(anInputStream);
    Magnet aMagnet = (Magnet) someMagnets.get(0);

    assertNotNull("The result magnet is null", aMagnet);
    assertEquals("The magnet name is invalid", "testMagnet", aMagnet.getName());
    assertEquals("The magnet description is invalid", "A test magnet", aMagnet.getDescription());
    assertNull("The extend of the magnet is invalid", aMagnet.getExtends());
    assertEquals("The size of the script list is invalid", 0, aMagnet.getScripts().size());
    assertEquals("The size of the parameters list is invalid", 0, aMagnet.getParameters().size());

    assertEquals("The size of the codebase list is invalid", 1, aMagnet.getObjectsFor("Codebase").size());
    Iterator someCodebases = aMagnet.getObjectsFor("Codebase").iterator();
    Codebase aCodebase = (Codebase) someCodebases.next();

    assertEquals("The id of the codebase is invalid", "remote", aCodebase.getId());
    assertNull("The parent of the codebase is invalid", aCodebase.getParent());
    assertEquals("The protocol of the path is invalid", "http", ((Path) aCodebase.getPaths().get(0)).getProtocol());
    assertEquals("The host of the path is invalid", "dev.sapia.org", ((Path) aCodebase.getPaths().get(0)).getHost());
    assertEquals("The directory of the path is invalid", "/codebase/lib", ((Path) aCodebase.getPaths().get(0)).getDirectory());
    assertEquals("The size of the exclude list is invalid", 0, ((Path) aCodebase.getPaths().get(0)).getExcludes().size());
    assertEquals("The size of the include list is invalid", 1, ((Path) aCodebase.getPaths().get(0)).getIncludes().size());
    Include anInclude = (Include) ((Path) aCodebase.getPaths().get(0)).getIncludes().iterator().next();
    assertEquals("The pattern of the include is invalid", "interfaces.jar", anInclude.getPattern());

    assertEquals("The size of the classpath list is invalid", 1, aMagnet.getObjectsFor("Classpath").size());
    Iterator someClasspaths = aMagnet.getObjectsFor("Classpath").iterator();
    Classpath aClasspath = (Classpath) someClasspaths.next();

    assertEquals("The id of the classpath is invalid", "main", aClasspath.getId());
    assertNull("The parent of the classpath is invalid", aClasspath.getParent());
    assertEquals("The protocol of the path is invalid", "file", ((Path) aClasspath.getPaths().get(0)).getProtocol());
    assertEquals("The host of the path is invalid", "localhost", ((Path) aClasspath.getPaths().get(0)).getHost());
    assertEquals("The directory of the path is invalid", "/lib/common", ((Path) aClasspath.getPaths().get(0)).getDirectory());
    assertEquals("The size of the exclude list is invalid", 0, ((Path) aClasspath.getPaths().get(0)).getExcludes().size());
    assertEquals("The size of the include list is invalid", 1, ((Path) aClasspath.getPaths().get(0)).getIncludes().size());
    anInclude = (Include) ((Path) aClasspath.getPaths().get(0)).getIncludes().iterator().next();
    assertEquals("The pattern of the include is invalid", "*.jar", anInclude.getPattern());

    assertEquals("The size of the launcher list is invalid", 1, aMagnet.getLaunchers().size());
    Launcher aLauncher = (Launcher) aMagnet.getLaunchers().iterator().next();
    assertEquals("The type of the launcher is invalid", "java", aLauncher.getType());

    assertTrue("The class of the launch handler is invalid", aLauncher.getLaunchHandler() instanceof JavaLauncher);
    JavaLauncher aJavaLauncher = (JavaLauncher) aLauncher.getLaunchHandler();
    assertEquals("The type of the java launcher is invalid", "java", aJavaLauncher.getType());
    assertEquals("The name of the java launcher is invalid", "HelloWorld", aJavaLauncher.getName());
    assertEquals("The default of the java launcher is invalid", "dev", aJavaLauncher.getDefault());
    assertEquals("The main class of the java launcher is invalid", "org.sapia.magnet.testClass", aJavaLauncher.getMainClass());
    assertEquals("The args of the java launcher is invalid", "${app.args}", aJavaLauncher.getArgs());
    assertTrue("The daemon indicator of the java launcher is invalid", aJavaLauncher.isDaemon());

    assertEquals("The size of the profile list is invalid ", 1, aJavaLauncher.getProfiles().size());
    Profile aProfile = (Profile) aJavaLauncher.getProfiles().iterator().next();
    assertEquals("The name of the profile is invalid", "dev", aProfile.getName());

    Parameters aParameters = (Parameters) aProfile.getParameters();
    assertNull("The profile of the parameters is invalid", aParameters.getProfile());
    assertEquals("The size of the params list is invalid", 1, aParameters.getParams().size());
    Iterator someParams = aParameters.getParams().iterator();
    Param aParam = (Param) someParams.next();
    assertEquals("The name of the param is invalid", "app.args", aParam.getName());
    assertEquals("The value of the param is invalid", "HelloWorld", aParam.getValue());

    assertEquals("The size of the codebase list is invalid", 1, aProfile.getObjectsFor("Codebase").size());
    someCodebases = aProfile.getObjectsFor("Codebase").iterator();
    aCodebase = (Codebase) someCodebases.next();

    assertNull("The id of the codebase is invalid", aCodebase.getId());
    assertEquals("The parent of the codebase is invalid", "remote", aCodebase.getParent());
    assertEquals("The protocol of the path is invalid", 0, aCodebase.getPaths().size());

    assertEquals("The size of the classpath list is invalid", 1, aProfile.getObjectsFor("Classpath").size());
    aClasspath = (Classpath) aProfile.getObjectsFor("Classpath").iterator().next();
    assertNull("The id of the classpath is invalid", aClasspath.getId());
    assertEquals("The parent of the classpath is invalid", "main", aClasspath.getParent());
    assertEquals("The protocol of the path is invalid", "file", ((Path) aClasspath.getPaths().get(0)).getProtocol());
    assertEquals("The host of the path is invalid", "localhost", ((Path) aClasspath.getPaths().get(0)).getHost());
    assertEquals("The directory of the path is invalid", "/lib", ((Path) aClasspath.getPaths().get(0)).getDirectory());
    assertEquals("The size of the exclude list is invalid", 1, ((Path) aClasspath.getPaths().get(0)).getExcludes().size());
    Exclude anExclude = (Exclude) ((Path) aClasspath.getPaths().get(0)).getExcludes().iterator().next();
    assertEquals("The pattern of the exclude is invalid", "common/*.jar", anExclude.getPattern());
    assertEquals("The size of the include list is invalid", 1, ((Path) aClasspath.getPaths().get(0)).getIncludes().size());
    anInclude = (Include) ((Path) aClasspath.getPaths().get(0)).getIncludes().iterator().next();
    assertEquals("The pattern of the include is invalid", "*.jar", anInclude.getPattern());

  }

}