package org.sapia.magnet;

/**
 * This interface act as a central dictionnary of namespace definitions of modules under
 * the magnet umbrella.
 *
 * @author Jean-Cedric Desrochers
 * <dl>
 * <dt><b>Copyright:</b><dd>Copyright &#169; 2002-2003 <a href="http://www.sapia-oss.org">Sapia Open Source Software</a>. All Rights Reserved.</dd></dt>
 * <dt><b>License:</b><dd>Read the license.txt file of the jar or visit the
 *        <a href="http://www.sapia-oss.org/license.html" target="sapia-license">license page</a> at the Sapia OSS web site</dd></dt>
 * </dl>
 */
public interface MagnetDictionary {

  /** Defines the namespaces URI of Magnet. */
  public static final String NAMESPACE_URI_MAGNET = "http://schemas.sapia-oss.org/magnet/core/";

  /** Defines the default namespaces prefix of Magnet. */
  public static final String NAMESPACE_PREFIX_MAGNET = "MAGNET";

  /** Defines the namespaces URI of Corus interoperability. */
  public static final String NAMESPACE_URI_CORUS_INTEROP = "http://schemas.sapia-oss.org/corus/interoperability/";

  /** Defines the default namespaces prefix of Corus interoperability. */
  public static final String NAMESPACE_PREFIX_CORUS_INTEROP = "COR-IOP";

}
