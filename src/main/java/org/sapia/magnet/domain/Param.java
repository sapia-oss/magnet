package org.sapia.magnet.domain;

// Import of Sapia's Corus classes
// --------------------------------
import org.sapia.magnet.render.AbstractRenderable;
import org.sapia.magnet.render.MagnetContext;
import org.sapia.magnet.render.RenderingException;


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
public class Param extends AbstractRenderable {

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  CLASS ATTRIBUTES  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the SYSTEM paramter scope. */
  public static final String SCOPE_SYSTEM = "system";

  /** Defines the MAGNET paramter scope. */
  public static final String SCOPE_MAGNET = "magnet";

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The name of this parameter. */
  private String _theName;

  /** The value of this parameter. */
  private String _theValue;

  /** The scope of this parameter. */
  private String _theScope;
  
  /** The required param name to use this parameter. */
  private String _theIfDefine;
  
  /** The inexistant param name to use this parameter. */
  private String _theUnlessDefine;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Param instance.
   */
  public Param() {
    _theScope = SCOPE_MAGNET;
  }

  /**
   * Creates a new Parameter instance with the arguments passed in.
   *
   * @param aName The name of this parameter.
   * @param aValue The value of this parameter.
   */
  public Param(String aName, String aValue) {
    this(aName, aValue, SCOPE_MAGNET);
  }

  /**
   * Creates a new Parameter instance with the arguments passed in.
   *
   * @param aName The name of this parameter.
   * @param aValue The value of this parameter.
   * @param aScope The scope of this parameter.
   */
  public Param(String aName, String aValue, String aScope) {
    _theName = aName;
    _theValue = aValue;
    _theScope = aScope;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the name of this parameter.
   *
   * @return The name of this parameter.
   */
  public String getName() {
    return _theName;
  }

  /**
   * Returns the value of this parameter.
   *
   * @return The value of this parameter.
   */
  public String getValue() {
    return _theValue;
  }

  /**
   * Returns the scope of this parameter.
   *
   * @return The scope of this parameter.
   */
  public String getScope() {
    return _theScope;
  }
  
  /**
   * Returns the name of the variable that MUST exist to
   * use this parameter.
   * 
   * @return The name of the variable that MUST exist to
   *         use this parameter.
   */
  public String getIfDefine() {
    return _theIfDefine;
  }
  
  public String getIf(){
    return getIfDefine();
  }
  
  /**
   * Returns the name of the variable that must NOT exist to
   * use this parameter.
   * 
   * @return The name of the variable that must NOT exist to
   *         use this parameter.
   */
  public String getUnlessDefine() {
    return _theUnlessDefine;
  }
  
  public String getUnless(){
    return getUnlessDefine();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the name of this parameter.
   *
   * @param aName The new name of this parameter.
   */
  public void setName(String aName) {
    _theName = aName;
  }

  /**
   * Changes the value of this parameter.
   *
   * @param aValue The new value of this parameter.
   */
  public void setValue(String aValue) {
    _theValue = aValue;
  }

  /**
   * Changes the scope of this parameter.
   *
   * @param aScope The scope of this parameter.
   */
  public void setScope(String aScope) {
    _theScope = aScope;
  }
  
  /**
   * Changes the name of the parameters that MUST exist to use
   * this parameter
   * 
   * @param aParamName The name of the parameter that need to exist. 
   */
  public void setIfDefine(String aParamName) {
    _theIfDefine = aParamName;
  }
  
  public void setIf(String aParamName){
    this.setIfDefine(aParamName);
  }
  
  /**
   * Changes the name of the parameters that must NOT exist to use
   * this parameter
   * 
   * @param aParamName The name of the parameter that need to be inexistant. 
   */
  public void setUnlessDefine(String aParamName) {
    _theUnlessDefine = aParamName;
  }
  
  public void setUnless(String aParamName){
    setUnlessDefine(aParamName);
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  OVERRIDEN METHODS  //////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Renders this objects to the magnet context passed in.
   *
   * @param aContext The magnet context to use.
   * @exception RenderingException If an error occurs while rendering this object.
   */
  public void render(MagnetContext aContext) throws RenderingException {
    try {
      _theValue = resolveValue(aContext, _theValue);
      _theScope = resolveValue(aContext, _theScope);
    } catch (RenderingException re) { 
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to resolve an attribute of the param '").
              append(_theName).append("'");      
      
      throw new RenderingException(aBuffer.toString(), re);
    }
  }

  /**
   * Returns a string representation of this parameter.
   *
   * @return A string representation of this parameter.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[name=").append(_theName).
            append(" value=").append(_theValue).
            append(" scope=").append(_theScope).
            append(" ifDefine=").append(_theIfDefine).
            append(" unlessDefine=").append(_theUnlessDefine).
            append("]");

    return aBuffer.toString();
  }
}
