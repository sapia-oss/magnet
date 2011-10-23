package org.sapia.magnet.domain.java;

import java.util.Collection;

import org.sapia.magnet.domain.Include;
import org.sapia.magnet.domain.Path;
import org.sapia.magnet.domain.Profile;
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
public class TomcatLauncher extends JavaLauncher {

  /**
   * Creates a new JavaLauncher instance.
   */
  public TomcatLauncher() {
    // Define the default attributes for the launch of Tomcat
    super.setIsDaemon(false);
    super.setMainClass("org.sapia.corus.tomcat.CatalinaHook");
    super.setArgs("startd");
  }


  /**
   * Renders this objects to the magnet context passed in.
   *
   * @param aContext The magnet context to use.
   * @exception RenderingException If an error occurs while rendering this object.
   */
  @Override
  public void render(MagnetContext aContext) throws RenderingException {
    // Find a profile object
    Profile aProfile = findProfile(aContext.getProfile());

    if (aProfile != null) {
      // Validate that we are in the context of Corus
      String corusHome = System.getProperty("corus.home");
      if (corusHome != null) {
      
        // Get the classpath
        Collection<Object> someClasspath = aProfile.getObjectsFor("classpath");
        Classpath classpath;
        if (someClasspath.isEmpty()) {
          // Manually create a new classpath object
          classpath = new Classpath();
          aProfile.handleObject("classpath", classpath);
        } else {
          classpath = (Classpath) someClasspath.iterator().next();
        }
      
        // Create a new path for the corus third party tomcat jar file
        Path path = new Path();
        path.setDirectory(corusHome + "/lib/thirdparty");
        path.addInclude(Include.createNew("*tomcat*.jar"));
        classpath.getPaths().add(0, path);
      }
    }
    
    // Render the super class... let the normal process take place
    super.render(aContext);
  }
  
}
