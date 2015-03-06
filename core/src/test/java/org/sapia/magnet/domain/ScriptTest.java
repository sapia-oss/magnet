package org.sapia.magnet.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sapia.magnet.BaseMagnetTestCase;
import org.sapia.magnet.render.MagnetContext;
import org.sapia.magnet.render.RenderingException;

public class ScriptTest extends BaseMagnetTestCase {

  // Class fixtures
  public static final String VALID_BSH_SCRIPT_CODE = "System.out.println(\"BSH ==> Hello from script\")";
  public static final String INVALID_BSH_SCRIPT_CODE = "this is not a valid beanshell script";
  
  // Test fixtures
  private MagnetContext _context;
  
  @Before
  public void setUp() throws Exception {
    super.baseSetUp();
    _context = new MagnetContext((String) null);
  }

  @After
  public void tearDown() {
  }

  
  
  @Test(expected=RenderingException.class)
  public void testRender_invalidType() throws Exception {
    Script script = Script.createNew("snafoo", "", "junit", true);
    script.render(_context);
  }
  
  @Test
  public void testRender_basic() throws Exception {
    Script script = Script.createNew("bsh", "test", VALID_BSH_SCRIPT_CODE, true);
    script.render(_context);
    assertScript("bsh", "test", VALID_BSH_SCRIPT_CODE, true, script);
  }
  
  @Test
  public void testRender_interpolation_isAbortingOnError() throws Exception {
    Script script = Script.createNew("bsh", "test", VALID_BSH_SCRIPT_CODE, false);
    script.setIsAbortingOnError("${isAborting}");
    _context.addParameter(new Param("isAborting", "true"), true);
    
    script.render(_context);
    assertScript("bsh", "test", VALID_BSH_SCRIPT_CODE, true, script);
  }
  
  @Test
  public void testRender_interpolation_scriptCode() throws Exception {
    Script script = Script.createNew("bsh", "test", "System.out.println(\"BSH ==> Hello from ${scriptOwner}\")", true);
    _context.addParameter(new Param("scriptOwner", getClass().getName()), true);
    
    script.render(_context);
    assertScript("bsh", "test", "System.out.println(\"BSH ==> Hello from "+getClass().getName()+"\")", true, script);
  }
  
  @Test(expected=RenderingException.class)
  public void testRender_executionError_abortingOnError() throws Exception {
    Script script = Script.createNew("bsh", "test", INVALID_BSH_SCRIPT_CODE, true);
    script.render(_context);
    assertScript("bsh", "test", INVALID_BSH_SCRIPT_CODE, true, script);
  }
  
  @Test
  public void testRender_executionError_ignoringError() throws Exception {
    Script script = Script.createNew("bsh", "test", INVALID_BSH_SCRIPT_CODE, false);
    script.render(_context);
    assertScript("bsh", "test", INVALID_BSH_SCRIPT_CODE, false, script);
  }

  @Test
  public void testRender_customScriptHandler_successExec() throws Exception {
    HandlerFactory.getInstance().addScriptHandler("junit", TestableScriptHandler.class.getName());
    Script script = Script.createNew("junit", "test", "foo bar", true);
    script.render(_context);
    assertScript("junit", "test", "foo bar", true, script);
  }

  @Test(expected=RenderingException.class)
  public void testRender_customScriptHandler_errorExec() throws Exception {
    try {
      TestableScriptHandler.isThrowingExceptionOnExecution = true;
      HandlerFactory.getInstance().addScriptHandler("junit", TestableScriptHandler.class.getName());
      Script script = Script.createNew("junit", "test", "foo bar", true);
      script.render(_context);
      assertScript("junit", "test", "foo bar", true, script);
    } finally {
      TestableScriptHandler.isThrowingExceptionOnExecution = false;
    }
  }
  
  
}
