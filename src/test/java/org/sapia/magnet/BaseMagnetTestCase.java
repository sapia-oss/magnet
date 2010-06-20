package org.sapia.magnet;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.sapia.magnet.domain.AbstractObjectHandler;
import org.sapia.magnet.domain.Magnet;
import org.sapia.magnet.domain.Script;

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
