package org.sapia.magnet.domain;

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
public class Exclude extends AbstractRenderable {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The pattern of this exclude. */
  private String _thePattern;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Exclude instance.
   */
  public Exclude() {
  }

  /**
   * Creates a new Exclude instance with the passed in argument.
   *
   * @param aPattern The pattern of this exclude.
   */
  public Exclude(String aPattern) {
    _thePattern = aPattern;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the pattern of this exclude.
   *
   * @return The pattern of this exclude.
   */
  public String getPattern() {
    return _thePattern;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the pattern of this exclude.
   *
   * @param aPattern The new pattern of this exclude.
   */
  public void setPattern(String aPattern){
    _thePattern = aPattern;
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
    _thePattern = resolveValue(aContext, _thePattern);
  }

  /**
   * Returns a string representation of this exclude.
   *
   * @return A string representation of this exclude.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[pattern=").append(_thePattern).
            append("]");

    return aBuffer.toString();
  }
}
