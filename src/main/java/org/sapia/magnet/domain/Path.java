package org.sapia.magnet.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public class Path extends AbstractRenderable {

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  CLASS ATTRIBUTES  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the FILE protocol value. */
  public static final String PROTOCOL_FILE = "file";

  /** Defines the HTTP protocol value. */
  public static final String PROTOCOL_HTTP = "http";

  /** Defines the default protocol. */
  public static final String DEFAULT_PROTOCOL = "file";

  /** Defines the default host. */
  public static final String DEFAULT_HOST = "localhost";

  /** Defines the ASCENDING sorting order. */
  public static final String SORTING_ASCENDING = "ascending";

  /** Defines the DESCENDING sorting order. */
  public static final String SORTING_DESCENDING = "descending";

  public static Path createNew(String aProtocol, String aHost, String aDirectory, String aSorting) {
    Path created = new Path();
    created.setProtocol(aProtocol);
    created.setHost(aHost);
    created.setDirectory(aDirectory);
    created.setSorting(aSorting);
    
    return created;
  }
  
  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The target directory of this path element. */
  private String _theDirectory;

  /** The host name of this path element. */
  private String _theHost;

  /** The protocol of this path element. */
  private String _theProtocol;

  /** The sorting order of this path element. */
  private String _theSorting;

  /** The list of the includes patterns of this path element. */
  private List<Include> _theIncludePatterns;

  /** The list of the excludes patterns of this path element. */
  private List<Exclude> _theExcludePatterns;

  /** The collection of selected resources (result of rendering). */
  private Collection<Resource> _theSelectedResources;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Path instance.
   */
  public Path() {
    _theProtocol = DEFAULT_PROTOCOL;
    _theHost = DEFAULT_HOST;
    _theIncludePatterns = new ArrayList<Include>();
    _theExcludePatterns = new ArrayList<Exclude>();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the protocol of this path element.
   *
   * @return The protocol of this path element.
   */
  public String getProtocol() {
    return _theProtocol;
  }

  /**
   * Returns the host of this path element.
   *
   * @return The host of this path element.
   */
  public String getHost() {
    return _theHost;
  }

  /**
   * Returns the directory of this path element.
   *
   * @return The directory of this path element.
   */
  public String getDirectory() {
    return _theDirectory;
  }

  /**
   * Returns the sorting order of this path element.
   *
   * @return The sorting order of this path element.
   */
  public String getSorting() {
    return _theSorting;
  }

  /**
   * Returns the collection of include pattern of this path element.
   *
   * @return The collection of <CODE>Include</CODE> objects.
   */
  public Collection<Include> getIncludes() {
    return _theIncludePatterns;
  }

  /**
   * Returns the collection of exclude pattern of this path element.
   *
   * @return The collection of <CODE>Exclude</CODE> objects.
   */
  public Collection<Exclude> getExcludes() {
    return _theExcludePatterns;
  }

  /**
   * Returns the selected resources of this path object after rendering.
   *
   * @return The collection of <CODE>Resource</CODE> of this path.
   */
  public Collection<Resource> getSelectedResources() {
    return _theSelectedResources;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the protocol of this path element.
   *
   * @param aProtocol The new protocol.
   */
  public void setProtocol(String aProtocol) {
    _theProtocol = aProtocol;
  }

  /**
   * Changes the host of this path element.
   *
   * @param aHost The new host.
   */
  public void setHost(String aHost) {
    _theHost = aHost;
  }

  /**
   * Changes the directory of this path element.
   *
   * @param aDirectory The new directory.
   */
  public void setDirectory(String aDirectory) {
    _theDirectory = aDirectory;
  }

  /**
   * Changes the sorting order of this path element.
   *
   * @param aSorting The new sorting order.
   */
  public void setSorting(String aSorting) {
    _theSorting = aSorting;
  }

  /**
   * Adds the include pattern passed in to this path element.
   *
   * @param anInclude The include pattern to add.
   * @exception IllegalArgumentException If the include passed in is null.
   */
  public void addInclude(Include anInclude) {
    if (anInclude == null) {
      throw new IllegalArgumentException("The include pattern passed in is null");
    }

    _theIncludePatterns.add(anInclude);
  }

  /**
   * Removes the include pattern passed in from this path element.
   *
   * @param anInclude The include pattern to remove.
   * @exception IllegalArgumentException If the include passed in is null.
   */
  public void removeInclude(Include anInclude) {
    if (anInclude == null) {
      throw new IllegalArgumentException("The include pattern passed in is null");
    }

    _theIncludePatterns.remove(anInclude);
  }

  /**
   * Removes all the include patterns from this path element.
   */
  public void clearIncludes() {
    _theIncludePatterns.clear();
  }

  /**
   * Adds the exclude pattern passed in to this path element.
   *
   * @param anExclude The exclude pattern to add.
   * @exception IllegalArgumentException If the exclude passed in is null.
   */
  public void addExclude(Exclude anExclude) {
    if (anExclude == null) {
      throw new IllegalArgumentException("The exclude pattern passed in is null");
    }

    _theExcludePatterns.add(anExclude);
  }

  /**
   * Removes the exclude pattern passed in from this path element.
   *
   * @param anExclude The exclude pattern to remove.
   * @exception IllegalArgumentException If the exclude passed in is null.
   */
  public void removeExclude(Exclude anExclude) {
    if (anExclude == null) {
      throw new IllegalArgumentException("The exclude pattern passed in is null");
    }

    _theExcludePatterns.remove(anExclude);
  }

  /**
   * Removes all the exclude patterns from this path element.
   */
  public void clearExcludes() {
    _theExcludePatterns.clear();
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
    // Resolve the path attributes
    try {
      _theProtocol = resolveValue(aContext, _theProtocol);
      _theHost = resolveValue(aContext, _theHost);
      _theDirectory = resolveValue(aContext, _theDirectory);
    } catch (RenderingException re) { 
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to resolve an attribute of the path ").
              append(" protocol=").append(_theProtocol).
              append(" host=").append(_theHost).
              append(" directory=").append(_theDirectory);
        
      throw new RenderingException(aBuffer.toString(), re);
    }

    try {
      // Rendering the include patterns
      for (Include incl: _theIncludePatterns) {
        incl.render(aContext);
      }

      // Rendering the excludes patterns
      for (Exclude excl: _theExcludePatterns) {
        excl.render(aContext);
      }

      // Delegate the resolution of the resource to the protocol handler
      ProtocolHandlerIF aHandler = HandlerFactory.getInstance().createProtocolHandler(_theProtocol);
      _theSelectedResources = aHandler.resolveResources(this, _theSorting);

    } catch (RenderingException re) { 
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to render the patterns of the path ").
              append(" protocol=").append(_theProtocol).
              append(" host=").append(_theHost).
              append(" directory=").append(_theDirectory);
        
      throw new RenderingException(aBuffer.toString(), re);

    } catch (ObjectCreationException oce) {
      StringBuffer aBuffer = new StringBuffer("Unable to create the protocol handler for the path: ");
      aBuffer.append("protocol=").append(_theProtocol).
              append(" host=").append(_theHost).
              append(" directory=").append(_theDirectory);
      throw new RenderingException(aBuffer.toString(), oce);
    }
  }

  /**
   * Returns a string representation of this path element.
   *
   * @return A string representation of this path element.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[protocol=").append(_theProtocol).
            append(" host=").append(_theHost).
            append(" directory=").append(_theDirectory).
            append(" includes=").append(_theIncludePatterns).
            append(" exludes=").append(_theExcludePatterns).
            append(" resources=").append(_theSelectedResources).
            append("]");

    return aBuffer.toString();
  }
}
