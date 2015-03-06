package org.sapia.magnet;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.sapia.magnet.domain.Magnet;
import org.sapia.magnet.render.MagnetContext;
import org.sapia.magnet.render.RenderingException;


/**
 *
 *
 * @author Jean-Cedric Desrochers
 * <dl>
 * <dt><b>Copyright:</b><dd>Copyright &#169; 2002-2003 <a href="http://www.sapia-oss.org">Sapia Open Source Software</a>. All Rights Reserved.</dd></dt>
 * <dt><b>License:</b><dd>Read the license.txt file of the jar or visit the
 *        <a href="http://www.sapia-oss.org/license.html" target="sapia-license">license page</a> at the Sapia OSS web site</dd></dt>
 * </dl>
 */
public class MagnetRenderer {

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  CLASS ATTRIBUTES  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the logger instance for this class. */
  private static final Logger _theLogger = Logger.getLogger(MagnetRenderer.class);

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new MagnetRenderer instance.
   */
  public MagnetRenderer() {
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  HELPER METHODS  ////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Renders the magnet object passed in and returns a context object that
   * containd the rendering state of the magnet.
   *
   * @param someMagnets The magnets to render.
   * @param aProfile The target profile for the rendering operation.
   * @return The context of the rendering state of the magnet.
   * @exception MagnetException If an error occurs while rendering the magnet.
   */
  public MagnetContext render(List<Magnet> someMagnets, String aProfile) throws MagnetException {
    Magnet aMagnet = null;
    MagnetContext aContext = null;

    try {
      for (Iterator<Magnet> it = someMagnets.iterator(); it.hasNext(); ) {
        if (aContext == null) {
          aContext = new MagnetContext(aProfile);
        } else {
          aContext = new MagnetContext(aContext);
        }
        aMagnet = it.next();
        aMagnet.render(aContext);
      }

      return aContext;
      
    } catch (RenderingException re) {
      String aMessage = "Unable to render the profile " + aProfile + " of the magnet " + aMagnet.getName();
      _theLogger.error(aMessage, re);
      throw new MagnetException(aMessage, re);

    } catch (RuntimeException re) {
      String aMessage = "System error rendering the profile " + aProfile + " of the magnet " + aMagnet.getName();
      _theLogger.error(aMessage, re);
      throw new MagnetException(aMessage, re);
    }
  }
  
}
