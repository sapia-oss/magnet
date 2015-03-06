package org.sapia.magnet.render;

// Import of Sapia's utility classes
// ---------------------------------
import org.sapia.util.text.TemplateElementIF;
import org.sapia.util.text.TemplateFactory;
import org.sapia.util.text.TemplateException;


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
public abstract class AbstractRenderable implements RenderableIF {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The template factory of this abstract renderable object. */
  private TemplateFactory _theTemplateFactory;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new AbstractRenderable instance.
   */
  protected AbstractRenderable() {
    _theTemplateFactory = new TemplateFactory(
            TemplateFactory.DEFAULT_STARTING_DELIMITER, TemplateFactory.DEFAULT_ENDING_DELIMITER);
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  HELPER METHODS  ////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Utility methods that resolve the value passed with for the given context.
   *
   * @param aContext The context to use for the resolution operation.
   * @param aValue The value to resolve.
   * @exception RenderingException If an error occurs resolving the value.
   */
  protected String resolveValue(MagnetContext aContext, String aValue) throws RenderingException {
    try {
      if (aValue == null) {
        return null;
      } else {
        TemplateElementIF aTemplate = _theTemplateFactory.parse(aValue);
        return aTemplate.render(aContext);
      }
    } catch (TemplateException te) {
      throw new RenderingException("Unable to resolve the value " + aValue, te);
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////  INTERACE IMPLEMENTATION  ///////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Renders this objects to the magnet context passed in.
   *
   * @param aContext The magnet context to use.
   * @exception RenderingException If an error occurs while rendering this object.
   */
  public void render(MagnetContext aContext) throws RenderingException {
  }
}
