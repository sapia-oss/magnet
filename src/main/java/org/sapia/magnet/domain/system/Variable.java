package org.sapia.magnet.domain.system;

import org.sapia.magnet.render.AbstractRenderable;
import org.sapia.magnet.render.MagnetContext;
import org.sapia.magnet.render.RenderingException;


/**
 *
 * @author Jean-Cedric Desrochers
 *
 * <dl>
 * <dt><b>Copyright:</b><dd>Copyright &#169; 2002-2003 <a href="http://www.sapia-oss.org">Sapia Open Source Software</a>. All Rights Reserved.</dd></dt>
 * <dt><b>License:</b><dd>Read the license.txt file of the jar or visit the
 *        <a href="http://www.sapia-oss.org/license.html" target="sapia-license">license page</a> at the Sapia OSS web site</dd></dt>
 * </dl>
 */
public class Variable extends AbstractRenderable {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The name of this variable. */
  private String _theName;

  /** The value of this variable. */
  private String _theValue;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Variable instance.
   */
  public Variable() {
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the name of this variable.
   *
   * @return The name of this variable.
   */
  public String getName() {
    return _theName;
  }

  /**
   * Returns the value of this variable.
   *
   * @return The value of this variable.
   */
  public String getValue() {
    return _theValue;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the name of this variable.
   *
   * @param aName The new name.
   */
  public void setName(String aName) {
    _theName = aName;
  }

  /**
   * Changes the value of this variable.
   *
   * @param aValue The new value.
   */
  public void setValue(String aValue) {
    _theValue = aValue;
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
    // Resolve the attributes
    try {
      _theName = resolveValue(aContext, _theName);
      _theValue = resolveValue(aContext, _theValue);
    } catch (RenderingException re) { 
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to resolve an attribute of the variable '").
              append(_theName).append("'");      
      
      throw new RenderingException(aBuffer.toString(), re);
    }
  }

  /**
   * Returns a string representation of this classpath.
   *
   * @return A string representation of this classpath.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[name=").append(_theName).
            append(" value=").append(_theValue).
            append("]");

    return aBuffer.toString();
  }
}
