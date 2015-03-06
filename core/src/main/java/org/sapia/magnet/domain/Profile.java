package org.sapia.magnet.domain;

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
public class Profile extends AbstractObjectHandler {
	
	static Parameters EMPTY_PARAMETERS = new Parameters();

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The name of this profile. */
  private String _theName;

  /** The parameters of this profile. */
  private Parameters _theParameters = EMPTY_PARAMETERS;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Profile instance.
   */
  public Profile() {
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the name of this profile.
   *
   * @return The name of this profile.
   */
  public String getName() {
    return _theName;
  }

  /**
   * Returns the parameters of this profile.
   *
   * @return The parameters of this profile.
   */
  public Parameters getParameters() {
    return _theParameters;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the name of this profile to the one passed in.
   *
   * @param aName The new name of this profile.
   */
  public void setName(String aName) {
    _theName = aName;
  }

  /**
   * Changes the parameters of this profile to the one passed in.
   *
   * @param aParameters The new parameters of this profile.
   */
  public void setParameters(Parameters aParameters) {
    _theParameters = aParameters;
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
    MagnetContext aSubContext = new MagnetContext(aContext);

    // Render the parameters of this profile, if any
    try {
      if (_theParameters != null) {
        _theParameters.render(aSubContext);
      }
    } catch (RenderingException re) {
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to render the parameters of the profile ").
              append(_theName);
      throw new RenderingException(aBuffer.toString(), re);
    }

    // Render the handler definitions
    try {
      super.renderHandlerDefs(aContext);
    } catch (RenderingException re) {
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to render the handler defs of the profile ").
              append(_theName);
      throw new RenderingException(aBuffer.toString(), re);
    }

    // Render all the objects handled by this profile
    try {
      super.render(aSubContext);
    } catch (RenderingException re) {
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to render an object of the profile ").
              append(_theName);
      throw new RenderingException(aBuffer.toString(), re);
    }
  }

  /**
   * Returns a string representation of this profile.
   *
   * @return A string representation of this profile.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[name=").append(_theName).
            append(" parameters=").append(_theParameters).
            append("]");

    return aBuffer.toString();
  }
}
