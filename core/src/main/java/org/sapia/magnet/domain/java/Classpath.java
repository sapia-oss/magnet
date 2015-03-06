package org.sapia.magnet.domain.java;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.sapia.magnet.MagnetException;
import org.sapia.magnet.domain.Path;
import org.sapia.magnet.domain.Resource;
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
public class Classpath extends AbstractRenderable {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The identifier of this classpath. */
  private String _theId;

  /** The parent identifier of this classpath. */
  private String _theParent;

  /** The path elements of this classpath. */
  private List<Path> _thePaths;

  /** The class loader that is created by this classpath. */
  private ClassLoader _theClassLoader;
  
  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Classpath instance.
   */
  public Classpath() {
    _thePaths = new ArrayList<Path>();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the identifier of this classpath.
   *
   * @return The identifier of this classpath.
   */
  public String getId() {
    return _theId;
  }

  /**
   * Returns the parent identifier of this classpath.
   *
   * @return The parent identifier of this classpath.
   */
  public String getParent() {
    return _theParent;
  }

  /**
   * Returns the path elements of this classpath.
   *
   * @return The path elements of this classpath.
   */
  public List<Path> getPaths() {
    return _thePaths;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the identifier of this classpath.
   *
   * @param anId The new identifier.
   */
  public void setId(String anId) {
    _theId = anId;
  }

  /**
   * Changes the parent identifier of this classpath.
   *
   * @param aParent The new parent identifier.
   */
  public void setParent(String aParent) {
    _theParent = aParent;
  }

  /**
   * Adds the path element passed in to this classpath.
   *
   * @param aPath The path element to add.
   */
  public void addPath(Path aPath) {
    _thePaths.add(aPath);
  }

  /**
   * Removes the path element passed in from this classpath.
   *
   * @param aPath The path element to remove.
   */
  public void removePath(Path aPath) {
    _thePaths.remove(aPath);
  }

  /**
   * Removes all the paths elements of this classpath.
   */
  public void clearPaths() {
    _thePaths.clear();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  HELPER METHODS  ////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a ClassLoader with the resources of this classpath.
   *
   * @param aParent The parent classloader of one to create.
   * @return The created classloader.
   * @exception MagnetException If an error occurs creating the classloader.
   */
  public ClassLoader createClassLoader(ClassLoader aParent) throws MagnetException {

    if (_theClassLoader == null) {
      try {
        ArrayList<URL> someURLs = new ArrayList<URL>();
        for (Path path: _thePaths) {
          for (Resource resource: path.getSelectedResources()) {
            someURLs.add(resource.toURL());
          }
        }
  
        if (someURLs.size() == 0) {
          _theClassLoader = aParent;
        } else {
          _theClassLoader = URLClassLoader.newInstance((URL[]) someURLs.toArray(new URL[0]), aParent);
        }
  
      } catch (MalformedURLException mue) {
        throw new MagnetException("Error creating a classloader for this classpath", mue);
      }
    }
    
    return _theClassLoader;
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
      _theId = resolveValue(aContext, _theId);
      _theParent = resolveValue(aContext, _theParent);
    } catch (RenderingException re) { 
      StringBuffer aBuffer = new StringBuffer("Unable to resolve an attribute of the");
      if (_theId != null) {
        aBuffer.append(" classpath \'").append(_theId).append("\'");
      } else {
        aBuffer.append(" classpath");
      }
        
      throw new RenderingException(aBuffer.toString(), re);
    }
  
    // Render the paths
    try {
      for (Path path: _thePaths) {
        path.render(aContext);
      }
    } catch (RenderingException re) {
      StringBuffer aBuffer = new StringBuffer("Unable to render the");
      if (_theId != null) {
        aBuffer.append(" classpath \'").append(_theId).append("\'");
      } else {
        aBuffer.append(" classpath");
      }
      
      throw new RenderingException(aBuffer.toString(), re);
    }
  }

  /**
   * Returns a string representation of this classpath.
   *
   * @return A string representation of this classpath.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[id=").append(_theId).
            append(" parent=").append(_theParent).
            append(" paths=").append(_thePaths).
            append("]");

    return aBuffer.toString();
  }
  
}

