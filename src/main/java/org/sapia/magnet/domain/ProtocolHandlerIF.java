package org.sapia.magnet.domain;

// Import of Sun's JDK classes
// ---------------------------
import java.util.Collection;

// Import of Sapia's magnet classes
// --------------------------------
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
public interface ProtocolHandlerIF {

  /**
   * Handle the rendering of the path object for a specific protocol by
   * resolving the resources of the path.
   *
   * @param aPath The path object to render.
   * @param aSortingOrder The sorting order of the collection to return.
   * @return The collection of <CODE>Resource</CODE> objects.
   * @exception RenderingException If an error occurs while resolving the path.
   */
  public Collection resolveResources(Path aPath, String aSortingOrder) throws RenderingException;
}
