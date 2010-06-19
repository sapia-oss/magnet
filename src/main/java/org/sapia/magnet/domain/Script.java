package org.sapia.magnet.domain;

import org.sapia.magnet.Log;
import org.sapia.magnet.MagnetException;
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
public class Script extends AbstractRenderable {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The type of this script. */
  private String _theType;

  /** The profile name associated with this script. */
  private String _theProfile;

  /** The code of this script. */
  private String _theCode;

  /** The abort on error indicator of this script. */
  private String _isAbortingOnError;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Script instance.
   */
  public Script() {
    _isAbortingOnError = "false";
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the type of this script.
   *
   * @return The type of this script.
   */
  public String getType() {
    return _theType;
  }

  /**
   * Returns the profile name associated with this script.
   *
   * @return The profile name associated with this script.
   */
  public String getProfile() {
    return _theProfile;
  }

  /**
   * Returns the code of this script.
   *
   * @return The code of this script.
   */
  public String getCode() {
    return _theCode;
  }

  /**
   * Returns the abort on error indicator of this script.
   *
   * @return The abort on error indicator of this script.
   */
  public boolean isAbortingOnError() {
    return _isAbortingOnError.equals("true");
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the type of this script.
   *
   * @param aType The new type.
   */
  public void setType(String aType) {
    _theType = aType;
  }

  /**
   * Changes the profile name of this script.
   *
   * @param aProfile The new profile name.
   */
  public void setProfile(String aProfile) {
    _theProfile = aProfile;
  }

  /**
   * Changes the code of this script.
   *
   * @param aCode The new code.
   */
  public void setCode(String aCode) {
    _theCode = aCode;
  }

  /**
   * Changes the text content of this script as the code of the script.
   *
   * @param aContent The new content.
   */
  public void setText(String aContent) {
    _theCode = aContent;
  }

  /**
   * Changes the abort on error indicator of this script.
   *
   * @param isAbortingOnError The new abort on error indicator value.
   */
  public void setIsAbortingOnError(String isAbortingOnError) {
    _isAbortingOnError = isAbortingOnError;
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
      _isAbortingOnError = resolveValue(aContext, _isAbortingOnError);
      _theCode = resolveValue(aContext, _theCode);
    } catch (RenderingException re) { 
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to resolve an attribute of the script of type ").
              append(_theType);
      if (_theProfile != null) {
        aBuffer.append(" for the profile ").append(_theProfile);
      } else {
        aBuffer.append(" for the default profile");
      }
      
      throw new RenderingException(aBuffer.toString(), re);
    }

    try {
      // Get a script handler and delegate the execution
      ScriptHandlerIF aHandler = HandlerFactory.getInstance().createScriptHandler(_theType);
      aHandler.execute(_theCode);
    } catch (ObjectCreationException oce) {
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to create a script handler of type ").
              append(_theType);
      if (_theProfile != null) {
        aBuffer.append(" for the profile ").append(_theProfile);
      } else {
        aBuffer.append(" for the default profile");
      }

      if (isAbortingOnError()) {
        throw new RenderingException(aBuffer.toString(), oce);
      } else {
        aBuffer.append(": ").append(Log.extactMessage(oce));
        Log.warn(aBuffer.toString());
      }
    } catch (MagnetException de) {
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to execute the script of type ").
              append(_theType);
      if (_theProfile != null) {
        aBuffer.append(" for the profile ").append(_theProfile);
      } else {
        aBuffer.append(" for the default profile");
      }

      if (isAbortingOnError()) {
        throw new RenderingException(aBuffer.toString(), de);
      } else {
        aBuffer.append(": ").append(Log.extactMessage(de));
        Log.warn(aBuffer.toString());
      }
    } catch (RuntimeException re) {
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("System error executing the script of type ").
              append(_theType);
      if (_theProfile != null) {
        aBuffer.append(" for the profile ").append(_theProfile);
      } else {
        aBuffer.append(" for the default profile");
      }

      if (isAbortingOnError()) {
        throw new RenderingException(aBuffer.toString(), re);
      } else {
        aBuffer.append(": ").append(Log.extactMessage(re));
        Log.warn(aBuffer.toString());
      }
    }
  }

  /**
   * Returns a string representation of this script.
   *
   * @return A string representation of this script.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[type=").append(_theType).
            append(" profile=").append(_theProfile).
            append(" abortOnError=").append(_isAbortingOnError).
            append(" code=").append(_theCode).
            append("]");

    return aBuffer.toString();
  }
}
