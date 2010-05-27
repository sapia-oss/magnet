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
public class LaunchHandlerDef extends AbstractRenderable {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The type of launch handler of this definition. */
  private String _theType;

  /** The classname of this definition. */
  private String _theClassname;

  /** The classpath of this definition. */
  private String _theClasspath;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new LaunchHandlerDef instance.
   */
  public LaunchHandlerDef() {
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  public String getType() {
    return _theType;
  }

  public String getClassname() {
    return _theClassname;
  }

  public String getClasspath() {
    return _theClasspath;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  public void setType(String aType) {
    _theType = aType;
  }

  public void setClassname(String aClassname) {
    _theClassname = aClassname;
  }

  public void setClasspath(String aClasspath) {
    _theClasspath = aClasspath;
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
      _theType = resolveValue(aContext, _theType);
      _theClassname = resolveValue(aContext, _theClassname);
      _theClasspath = resolveValue(aContext, _theClasspath);
  
      HandlerFactory.getInstance().addLaunchHandler(_theType, _theClassname);
    } catch (RenderingException re) { 
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to resolve an attribute of the launch handler def ").
              append(_theType);
        
      throw new RenderingException(aBuffer.toString(), re);
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
            append(" classname=").append(_theClassname).
            append(" classpath=").append(_theClasspath).
            append("]");

    return aBuffer.toString();
  }
}
