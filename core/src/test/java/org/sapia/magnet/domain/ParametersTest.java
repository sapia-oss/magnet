package org.sapia.magnet.domain;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sapia.magnet.BaseMagnetTestCase;
import org.sapia.magnet.render.MagnetContext;

public class ParametersTest extends BaseMagnetTestCase {
  
  // Class fixtures

  // Test fixtures
  private MagnetContext _context;
  private Parameters _params;
  
  @Before
  public void setUp() throws Exception {
    super.baseSetUp();
    _context = new MagnetContext((String) null);
    _params = new Parameters();
  }

  @After
  public void tearDown() {
  }


  
  @Test(expected=IllegalArgumentException.class)
  public void testAddParam_null() throws Exception {
    _params.addParam(null);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testAddParam_nullName() throws Exception {
    _params.addParam(new Param(null, ""));
  }
  
  @Test
  public void testAddParam_valid() throws Exception {
    _params.addParam(new Param("name1", "value1", Param.SCOPE_MAGNET));
    assertParam("name1", "value1", Param.SCOPE_MAGNET, null, null, _params.getParam("name1"));
  }
  
  @Test
  public void testAddParam_overwrite() throws Exception {
    _params.addParam(new Param("name1", "value1", Param.SCOPE_MAGNET));
    assertParam("name1", "value1", Param.SCOPE_MAGNET, null, null, _params.getParam("name1"));

    _params.addParam(new Param("name1", "newValue", Param.SCOPE_SYSTEM));
    assertParam("name1", "newValue", Param.SCOPE_SYSTEM, null, null, _params.getParam("name1"));
  }

  
  
  @Test
  public void testRender_nullProfile_noParam() throws Exception {
    _params.setProfile(null);
    
    _params.render(_context);
    Assert.assertEquals("The count of parameters of the context is invalid", 0, _context.getParameters().size());
  }
  
  @Test
  public void testRender_nullProfile_singleParam() throws Exception {
    _params.setProfile(null);
    _params.addParam(new Param("param1", "value1", Param.SCOPE_MAGNET));
    
    _params.render(_context);
    Assert.assertEquals("The count of parameters of the context is invalid", 1, _context.getParameters().size());
    assertParam("param1", "value1", Param.SCOPE_MAGNET, null, null, _context.getParameterFor("param1"));
  }
  
  @Test
  public void testRender_nullProfile_multiParam_scopeMagnet() throws Exception {
    _params.setProfile(null);
    _params.addParam(new Param("param1", "value1", Param.SCOPE_MAGNET));
    _params.addParam(new Param("param2", "value2", Param.SCOPE_MAGNET));
    
    _params.render(_context);
    Assert.assertEquals("The count of parameters of the context is invalid", 2, _context.getParameters().size());
    assertParam("param1", "value1", Param.SCOPE_MAGNET, null, null, _context.getParameterFor("param1"));
    assertParam("param2", "value2", Param.SCOPE_MAGNET, null, null, _context.getParameterFor("param2"));
  }
  
  @Test
  public void testRender_nullProfile_multiParam_mixedScope() throws Exception {
    _params.setProfile(null);
    _params.addParam(new Param("param1", "value1", Param.SCOPE_MAGNET));
    _params.addParam(new Param("param2", "value2", Param.SCOPE_SYSTEM));
    
    _params.render(_context);
    Assert.assertEquals("The count of parameters of the context is invalid", 1, _context.getParameters().size());
    assertParam("param1", "value1", Param.SCOPE_MAGNET, null, null, _context.getParameterFor("param1"));
    Assert.assertEquals("The value of the system property is invalid", "value2", System.getProperty("param2"));
  }
  
  @Test
  public void testRender_nullProfile_multiParam_includeWithIf() throws Exception {
    _params.setProfile(null);
    Param p1 = new Param("param1", "value1", Param.SCOPE_MAGNET);
    Param p2 = new Param("param2", "value2", Param.SCOPE_MAGNET);
    p2.setIf("param1");
    _params.addParam(p1);
    _params.addParam(p2);
    
    _params.render(_context);
    Assert.assertEquals("The count of parameters of the context is invalid", 2, _context.getParameters().size());
    assertParam("param1", "value1", Param.SCOPE_MAGNET, null, null, _context.getParameterFor("param1"));
    assertParam("param2", "value2", Param.SCOPE_MAGNET, "param1", null, _context.getParameterFor("param2"));
  }
  
  @Test
  public void testRender_nullProfile_multiParam_excludeWithIf() throws Exception {
    _params.setProfile(null);
    Param p1 = new Param("param1", "value1", Param.SCOPE_MAGNET);
    Param p2 = new Param("param2", "value2", Param.SCOPE_MAGNET);
    p2.setIf("notfound");
    _params.addParam(p1);
    _params.addParam(p2);
    
    _params.render(_context);
    Assert.assertEquals("The count of parameters of the context is invalid", 1, _context.getParameters().size());
    assertParam("param1", "value1", Param.SCOPE_MAGNET, null, null, _context.getParameterFor("param1"));
  }
  
  @Test
  public void testRender_nullProfile_multiParam_includeWithUnless() throws Exception {
    _params.setProfile(null);
    Param p1 = new Param("param1", "value1", Param.SCOPE_MAGNET);
    Param p2 = new Param("param2", "value2", Param.SCOPE_MAGNET);
    p2.setUnless("notfound");
    _params.addParam(p1);
    _params.addParam(p2);
    
    _params.render(_context);
    Assert.assertEquals("The count of parameters of the context is invalid", 2, _context.getParameters().size());
    assertParam("param1", "value1", Param.SCOPE_MAGNET, null, null, _context.getParameterFor("param1"));
    assertParam("param2", "value2", Param.SCOPE_MAGNET, null, "notfound", _context.getParameterFor("param2"));
  }
  
  @Test
  public void testRender_nullProfile_multiParam_excludeWithUnless() throws Exception {
    _params.setProfile(null);
    Param p1 = new Param("param1", "value1", Param.SCOPE_MAGNET);
    Param p2 = new Param("param2", "value2", Param.SCOPE_MAGNET);
    p2.setUnless("param1");
    _params.addParam(p1);
    _params.addParam(p2);
    
    _params.render(_context);
    Assert.assertEquals("The count of parameters of the context is invalid", 1, _context.getParameters().size());
    assertParam("param1", "value1", Param.SCOPE_MAGNET, null, null, _context.getParameterFor("param1"));
  }
  
  @Test
  public void testRender_matchingProfileNames() throws Exception {
    _context.setProfile("matching");
    _params.setProfile("matching");
    _params.addParam(new Param("param1", "value1", Param.SCOPE_MAGNET));
    _params.addParam(new Param("param2", "value2", Param.SCOPE_MAGNET));
    
    _params.render(_context);
    Assert.assertEquals("The count of parameters of the context is invalid", 2, _context.getParameters().size());
    assertParam("param1", "value1", Param.SCOPE_MAGNET, null, null, _context.getParameterFor("param1"));
    assertParam("param2", "value2", Param.SCOPE_MAGNET, null, null, _context.getParameterFor("param2"));
  }
  
  @Test
  public void testRender_differentProfileNames() throws Exception {
    _context.setProfile("matching");
    _params.setProfile("diff");
    _params.addParam(new Param("param1", "value1", Param.SCOPE_MAGNET));
    _params.addParam(new Param("param2", "value2", Param.SCOPE_MAGNET));
    
    _params.render(_context);
    Assert.assertEquals("The count of parameters of the context is invalid", 0, _context.getParameters().size());
  }

}
