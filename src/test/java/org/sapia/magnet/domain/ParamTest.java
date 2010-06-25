package org.sapia.magnet.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sapia.magnet.BaseMagnetTestCase;
import org.sapia.magnet.render.MagnetContext;

public class ParamTest extends BaseMagnetTestCase {

  // Class fixtures

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

  
  
  @Test
  public void testCreation() throws Exception {
    Param created = new Param("param1", "value1", Param.SCOPE_MAGNET);
    created.setIf("conditionA");
    created.setUnless("conditionB");
    
    assertParam("param1", "value1", Param.SCOPE_MAGNET, "conditionA", "conditionB", created);
  }
  
  @Test
  public void testRender_noInterpolation() throws Exception {
    Param created = new Param("param2", "value2", Param.SCOPE_SYSTEM);
    created.setIf("conditionC");
    created.setUnless("conditionD");

    created.render(_context);
    assertParam("param2", "value2", Param.SCOPE_SYSTEM, "conditionC", "conditionD", created);
  }
  
  @Test
  public void testRender_withInterpolation() throws Exception {
    Param created = new Param("param3", "${param.value}", "${param.scope}");
    created.setIf("${param.if}");
    created.setUnless("${param.unless}");

    _context.addParameter(new Param("param.value", "value3"), true);
    _context.addParameter(new Param("param.scope", Param.SCOPE_MAGNET), true);
    _context.addParameter(new Param("param.if", "conditionE"), true);
    _context.addParameter(new Param("param.unless", "conditionF"), true);

    created.render(_context);
    assertParam("param3", "value3", Param.SCOPE_MAGNET, "conditionE", "conditionF", created);
  }
  
}
