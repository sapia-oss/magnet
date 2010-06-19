package org.sapia.magnet.domain.java;

import org.sapia.magnet.domain.Path;
import org.sapia.magnet.domain.Resource;
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
public class Codebase extends Classpath {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The name of the profile associated to this codebase. */
  private String _theProfileName;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Codebase instance.
   */
  public Codebase() {
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the name of the associated profile of this codebase.
   *
   * @return The name of the associated profile of this codebase.
   */
  public String getProfile() {
    return _theProfileName;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the profile name of this codebase.
   *
   * @param aProfileName The new profile name.
   */
  public void setProfile(String aProfileName) {
    _theProfileName = aProfileName;
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
    if (_theProfileName == null ||
        (aContext.getProfile() != null && _theProfileName.equals(aContext.getProfile()))) {
      try {
        super.render(aContext);
  
        StringBuffer aBuffer = new StringBuffer();
        if (System.getProperty("java.rmi.server.codebase") != null) {
          aBuffer.append(System.getProperty("java.rmi.server.codebase")).append(" ");
        }

        for (Path path: getPaths()) {
          for (Resource resource: path.getSelectedResources()) {
            aBuffer.append(resource.getURL()).append(" ");
          }
        }
  
        System.setProperty("java.rmi.server.codebase", aBuffer.toString());
        
      } catch (RenderingException re) {
        StringBuffer aBuffer = new StringBuffer("Unable to render the codebase of the");
        if (_theProfileName == null) {
          aBuffer.append(" default profile");
        } else {
          aBuffer.append(" profile ").append(_theProfileName);
        }
                  
        throw new RenderingException(aBuffer.toString(), re);
      }
    }
  }

  /**
   * Returns a string representation of this codebase.
   *
   * @return A string representation of this codebase.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[profile=").append(_theProfileName).
            append("]");

    return aBuffer.toString();
  }
  
}
