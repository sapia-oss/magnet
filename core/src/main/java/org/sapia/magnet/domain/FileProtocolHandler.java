package org.sapia.magnet.domain;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.TreeSet;

import org.apache.tools.ant.DirectoryScanner;
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
public class FileProtocolHandler implements ProtocolHandlerIF {

  /**
   * Handle the rendering of the path object for a specific protocol by
   * resolving the resources of the path.
   *
   * @param aPath The path object to render.
   * @param aSortingOrder The sorting order of the collection to return.
   * @return The collection of <CODE>Resource</CODE> objects.
   * @exception RenderingException If an error occurs while resolving the path.
   */
  public Collection<Resource> resolveResources(Path aPath, SortingOrder aSortingOrder) throws RenderingException {
    // Validate the arguments
    if (aPath == null) {
    } else if (!aPath.getProtocol().equals(Path.PROTOCOL_FILE)) {
      throw new IllegalArgumentException("The protocol of the path is not 'file' but " + aPath.getProtocol());
    } else if (aPath.getDirectory() == null) {
      throw new IllegalArgumentException("The directory of the path passed in is null");
    }

    File aTargetDirectory = new File(aPath.getDirectory());
    if (!aTargetDirectory.exists() || !aTargetDirectory.isDirectory()) {
      // as a fallback, look it the user.dir
      aTargetDirectory = new File(System.getProperty("user.dir") + File.separator + aPath.getDirectory());
      if (!aTargetDirectory.exists() || !aTargetDirectory.isDirectory()) {
        throw new RenderingException("The target directory '" + aPath.getDirectory() + "' is invalid");
      }
    }

    // Create the resources for the included directories
    TreeSet<Resource> someResources;
    if (SortingOrder.ASCENDING == aSortingOrder) {
      someResources = new TreeSet<Resource>(new Resource.AscendingComparator());
    } else if (SortingOrder.DESCENDING == aSortingOrder) {
      someResources = new TreeSet<Resource>(new Resource.DescendingComparator());
    } else {
      someResources = new TreeSet<Resource>();
    }

    if (aPath.getIncludes().size() == 0) {
      // Create a resouce for the directory only
			StringBuffer pathStr = new StringBuffer();      
      
			pathStr.append(aTargetDirectory.getAbsolutePath()).append(File.separator);
			try {
        someResources.add(new Resource(new File(pathStr.toString()).toURI().toURL().toExternalForm(), 0));
			} catch(MalformedURLException e) {
				throw new RenderingException("Invalid path: " + pathStr.toString(), e);
			}
			
    } else {
      // Create the directory scanner
      DirectoryScanner aScanner = new DirectoryScanner();
      aScanner.setBasedir(aTargetDirectory);
      aScanner.setCaseSensitive(true);
      aScanner.setFollowSymlinks(true);

      // Add the inclusion patterns  
      String[] someIncludes = new String[aPath.getIncludes().size()];
      int anIndex = 0;
      for (Include incl: aPath.getIncludes()) {
        someIncludes[anIndex++] = incl.getPattern();
      }
      aScanner.setIncludes(someIncludes);
  
      // Add the exclusion patterns  
      String[] someExcludes = new String[aPath.getExcludes().size()];
      anIndex = 0;
      for (Exclude excl: aPath.getExcludes()) {
        someExcludes[anIndex++] = excl.getPattern();
      }
      aScanner.setExcludes(someExcludes);
  
      // Scan the directory
      aScanner.scan();
  
      // Process the results
      anIndex = 0;
      String[] someDirectories = aScanner.getIncludedDirectories();
      for (int i = 0; i < someDirectories.length; i++) {
        StringBuffer pathStr = new StringBuffer();
				pathStr .append(aTargetDirectory.getAbsolutePath()).append(File.separator).
              append(someDirectories[i]).append(File.separator);
        try {
          someResources.add(new Resource(new File(pathStr.toString()).toURI().toURL().toExternalForm(), anIndex++));
        } catch (MalformedURLException e) {
					throw new RenderingException("Invalid path: " + pathStr.toString(), e);        	
        }
      }
  
      // Create the resources for the included files
      String[] someFiles = aScanner.getIncludedFiles();
      for (int i = 0; i < someFiles.length; i++) {
        StringBuffer pathStr = new StringBuffer();
        pathStr.append(aTargetDirectory.getAbsolutePath()).append(File.separator).
              append(someFiles[i]);
        try {
          someResources.add(new Resource(new File(pathStr.toString()).toURI().toURL().toExternalForm(), anIndex++));
        } catch (MalformedURLException e) {
					throw new RenderingException("Invalid path: " + pathStr.toString(), e);        	
        }
      }
    }
    
    return someResources;
  }
}
