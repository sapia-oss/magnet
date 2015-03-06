package org.sapia.magnet;

import java.util.Iterator;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.sapia.magnet.domain.AbstractObjectHandler;
import org.sapia.magnet.domain.Exclude;
import org.sapia.magnet.domain.Include;
import org.sapia.magnet.domain.Magnet;
import org.sapia.magnet.domain.Param;
import org.sapia.magnet.domain.Parameters;
import org.sapia.magnet.domain.Path;
import org.sapia.magnet.domain.Script;
import org.sapia.magnet.domain.SortingOrder;
import org.sapia.magnet.domain.java.Classpath;
import org.sapia.magnet.domain.java.Codebase;

public abstract class BaseMagnetTestCase {

  // Class Fixtures
  private static long _TEST_ID_SEQUENCE = 0l;
  
  // Test Fixtures
  private long _testId;
  private Logger _logger;
  
  /**
   * Creates a new {@link BaseMagnetTestCase} instance.
   */
  protected BaseMagnetTestCase() {
    _testId = (++_TEST_ID_SEQUENCE);
    _logger = Logger.getLogger(this.getClass().getSimpleName()+"#"+_testId);
  }

  /**
   * Returns the logger attribute.
   *
   * @return The logger value.
   */
  public Logger getLogger() {
    return _logger;
  }

  /**
   * Returns the testId attribute.
   *
   * @return The testId value.
   */
  public long getTestId() {
    return _testId;
  }

  /**
   * 
   */
  protected void baseSetUp() {
    getLogger().info("\n\t###### ---------------------------------------\n\t###### RUNNING TEST CASE #" +
            getTestId() + "\n\t###### ---------------------------------------\n");
  }
  
  
  
  /**
   * 
   * @param eScriptHandletDefCount
   * @param eProtocolHandlerDefCount
   * @param eLauncherDefCount
   * @param eAdditionalElementCount
   * @param actual
   */
  public static void assertAbstractObjectHandler(int eScriptHandlerDefCount,
          int eProtocolHandlerDefCount, int eLauncherDefCount, int eAdditionalElementCount, AbstractObjectHandler actual) {
    Assert.assertNotNull("The abstract object handler passed in should not be null", actual);
    Assert.assertEquals("The number of script handler defs of this object handler is invalid", eScriptHandlerDefCount, actual.getScriptHandlerDefs().size());
    Assert.assertEquals("The number of protocol handler defs of this object handler is invalid", eProtocolHandlerDefCount, actual.getProtocolHandlerDefs().size());
    Assert.assertEquals("The number of launcher handler defs of this object handler is invalid", eLauncherDefCount, actual.getLaunchHandlerDefs().size());
    Assert.assertEquals("The number of additionnal element of this object handler is invalid", eAdditionalElementCount, actual.getElementNames().size());
  }
  
  /**
   * 
   * @param eType
   * @param eProfile
   * @param eCode
   * @param eIsAbortingOnError
   * @param actual
   */
  public static void assertScript(String eType, String eProfile, String eCode, boolean eIsAbortingOnError, Script actual) {
    Assert.assertNotNull("The script passed in should not be null", actual);
    Assert.assertEquals("The type of this script is invalid", eType, actual.getType());
    Assert.assertEquals("The profile of this script is invalid", eProfile, actual.getProfile());
    Assert.assertEquals("The code of this script is invalid", eCode, actual.getCode());
    Assert.assertEquals("The is aborting on error flag of this script is invalid", eIsAbortingOnError, actual.isAbortingOnError());
    
  }
  
  
  /**
   * 
   * @param eName
   * @param eValue
   * @param eScope
   * @param eIf
   * @param eUnless
   * @param actual
   */
  public static void assertParam(String eName, String eValue, String eScope,
          String eIf, String eUnless, Param actual) {
    Assert.assertNotNull("The param passed in should not be null", actual);
    Assert.assertEquals("The name of the param is invalid", eName, actual.getName());
    Assert.assertEquals("The value of the param is invalid", eValue, actual.getValue());
    Assert.assertEquals("The scope of the param is invalid", eScope, actual.getScope());
    Assert.assertEquals("The if condition of the param is invalid", eIf, actual.getIf());
    Assert.assertEquals("The unless condition of the param is invalid", eUnless, actual.getUnless());
  }
  
  /**
   * 
   * @param eProfile
   * @param eParams
   * @param actual
   */
  public static void assertParameters(String eProfile, Param[] eParams, Parameters actual) {
    Assert.assertNotNull("The parameters passed in should not be null", actual);
    Assert.assertEquals("The profile of the parameters is invalid", eProfile, actual.getProfile());
    Assert.assertEquals("The number of param of the parameters is invalid", eParams.length, actual.getParams().size());
    Iterator<Param> actualParams = actual.getParams().iterator();
    for (int i = 0; i < eParams.length; i++) {
      assertParam(eParams[i].getName(), eParams[i].getValue(), eParams[i].getScope(), eParams[i].getIf(), eParams[i].getUnless(),
              actualParams.next());
    }
  }

  /**
   * 
   * @param ePattern
   * @param actual
   */
  public static void assertInclude(String ePattern, Include actual) {
    Assert.assertNotNull("The include passed in should not be null", actual);
    Assert.assertEquals("The pattern of the include is invalid", ePattern, actual.getPattern());
  }

  /**
   * 
   * @param ePattern
   * @param actual
   */
  public static void assertExclude(String ePattern, Exclude actual) {
    Assert.assertNotNull("The exclude passed in should not be null", actual);
    Assert.assertEquals("The pattern of the exclude is invalid", ePattern, actual.getPattern());
  }
  
  /**
   * 
   * @param eProtocol
   * @param eHost
   * @param eDirectory
   * @param eSorting
   * @param eIncludes
   * @param eExcludes
   * @param actual
   */
  public static void assertPath(String eProtocol, String eHost, String eDirectory, SortingOrder eSorting,
          Include[] eIncludes, Exclude[] eExcludes, Path actual) {
    Assert.assertNotNull("The path passed in should not be null", actual);
    Assert.assertEquals("The protocol of the path is invalid", eProtocol, actual.getProtocol());
    Assert.assertEquals("The host of the path is invalid", eHost, actual.getHost());
    Assert.assertEquals("The directory of the path is invalid", eDirectory, actual.getDirectory());
    Assert.assertEquals("The sorting of the path is invalid", eSorting, actual.getSorting());

    Assert.assertEquals("The size of the include list is invalid", eIncludes.length, actual.getIncludes().size());
    Iterator<Include> actualIncludes = actual.getIncludes().iterator();
    for (int i=0; i < eIncludes.length; i++) {
      assertInclude(eIncludes[i].getPattern(), actualIncludes.next());
    }

    Assert.assertEquals("The size of the exclude list is invalid", eExcludes.length, actual.getExcludes().size());
    Iterator<Exclude> actualExcludes = actual.getExcludes().iterator();
    for (int i=0; i < eExcludes.length; i++) {
      assertExclude(eExcludes[i].getPattern(), actualExcludes.next());
    }
  }
  
  /**
   * 
   * @param eId
   * @param eParent
   * @param ePaths
   * @param actual
   */
  public static void assertClasspath(String eId, String eParent, Path[] ePaths, Classpath actual) {
    Assert.assertNotNull("The classpath passed in should not be null", actual);
    Assert.assertEquals("The id of the classpath is invalid", eId, actual.getId());
    Assert.assertEquals("The parent of the classpath is invalid", eParent, actual.getParent());

    Assert.assertEquals("The path count of the classpath is invalid", ePaths.length, actual.getPaths().size());
    Iterator<Path> actualPaths = actual.getPaths().iterator();
    for (int i=0; i < ePaths.length; i++) {
      assertPath(ePaths[i].getProtocol(), ePaths[i].getHost(), ePaths[i].getDirectory(), ePaths[i].getSorting(),
              ePaths[i].getIncludes().toArray(new Include[ePaths[i].getIncludes().size()]),
              ePaths[i].getExcludes().toArray(new Exclude[ePaths[i].getExcludes().size()]),
              actualPaths.next());
    }
  }
  
  /**
   * 
   * @param eId
   * @param eParent
   * @param ePaths
   * @param eProfile
   * @param actual
   */
  public static void assertCodebase(String eId, String eParent, Path[] ePaths, String eProfile,
          Codebase actual) {
    Assert.assertNotNull("The codebase passed in should not be null", actual);
    Assert.assertEquals("The id of the codebase is invalid", eId, actual.getId());
    Assert.assertEquals("The parent of the codebase is invalid", eParent, actual.getParent());
    Assert.assertEquals("The profile of the codebase is invalid", eProfile, actual.getProfile());

    Assert.assertEquals("The path count of the codebase is invalid", ePaths.length, actual.getPaths().size());
    Iterator<Path> actualPaths = actual.getPaths().iterator();
    for (int i=0; i < ePaths.length; i++) {
      assertPath(ePaths[i].getProtocol(), ePaths[i].getHost(), ePaths[i].getDirectory(), ePaths[i].getSorting(),
              ePaths[i].getIncludes().toArray(new Include[ePaths[i].getIncludes().size()]),
              ePaths[i].getExcludes().toArray(new Exclude[ePaths[i].getExcludes().size()]),
              actualPaths.next());
    }
  }
  
  /**
   * 
   * @param eScriptHandlerDefCount
   * @param eProtocolHandlerDefCount
   * @param eLauncherDefCount
   * @param eAdditionalElementCount
   * @param eName
   * @param eDescription
   * @param eExtends
   * @param eParentMagnetCount
   * @param eScriptCount
   * @param eParametersCount
   * @param eLauncherCount
   * @param actual
   */
  public static void assertMagnet(int eScriptHandlerDefCount, int eProtocolHandlerDefCount,
          int eLauncherDefCount, int eAdditionalElementCount, String eName, String eDescription,
          String eExtends, int eParentMagnetCount, int eScriptCount, int eParametersCount,
          int eLauncherCount, Magnet actual) {
    assertAbstractObjectHandler(eScriptHandlerDefCount, eProtocolHandlerDefCount, eLauncherDefCount, eAdditionalElementCount, actual);
    Assert.assertEquals("The name of this magnet is invalid", eName, actual.getName());
    Assert.assertEquals("The description of this magnet is invalid", eDescription, actual.getDescription());
    Assert.assertEquals("The extends of this magnet is invalid", eExtends, actual.getExtends());
    Assert.assertEquals("The number of parent magnets of this magnet is invalid", eParentMagnetCount, actual.getParents().size());
    Assert.assertEquals("The number of scripts of this magnet is invalid", eScriptCount, actual.getScripts().size());
    Assert.assertEquals("The number of parameters of this magnet is invalid", eParametersCount, actual.getParameters().size());
    Assert.assertEquals("The number of launchers of this magnet is invalid", eLauncherCount, actual.getLaunchers().size());
  }
}
