package org.sapia.magnet.domain.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.sapia.console.CmdLine;
import org.sapia.magnet.Log;
import org.sapia.magnet.MagnetException;
import org.sapia.magnet.domain.DefaultLaunchHandler;
import org.sapia.magnet.domain.Magnet;
import org.sapia.magnet.domain.Param;
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
public class JavaLauncher extends DefaultLaunchHandler {

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  CLASS ATTRIBUTES  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the logger instance for this class. */
  private static final Logger _theLogger = Logger.getLogger(JavaLauncher.class);

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The main class of this java launcher. */
  private String _theMainClass;

  /** The arguments of this java launcher. */
  private String _theArguments;

  /** The daemon indicator of this java launcher. */
  private String _isDaemon;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new JavaLauncher instance.
   */
  public JavaLauncher() {
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the main class of this java launcher.
   *
   * @return The main class of this java launcher.
   */
  public String getMainClass() {
    return _theMainClass;
  }

  /**
   * Returns the application arguments of this java launcher.
   *
   * @return The application arguments of this java launcher.
   */
  public String getArgs() {
    return _theArguments;
  }

  /**
   * Returns the daemon indicator of this java launcher.
   *
   * @return The daemon indicator of this java launcher.
   */
  public boolean isDaemon() {
    return _isDaemon != null && _isDaemon.equals("true");
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the main class of this java launcher.
   *
   * @param aMainClass The name of the new main class.
   */
  public void setMainClass(String aMainClass) {
    _theMainClass = aMainClass;
  }

  /**
   * Changes the application arguments of this java launcher.
   *
   * @param anArguments The new application arguments.
   */
  public void setArgs(String anArguments) {
    _theArguments = anArguments;
  }

  /**
   * Changes the daemon indicator of this java launcher.
   *
   * @param isDaemon The daemon indicator.
   */
  public void setIsDaemon(boolean isDaemon) {
    if (isDaemon) {
      _isDaemon = "true";
    } else {
      _isDaemon = "false";
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  HELPER METHODS  ////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Finds the classpath identifier by the ID passed in.
   *
   * @param anId The identifier of the classpath to retrieve.
   * @param someMagnets The collection of magnets in which the classpath is.
   * @return The found classpath or <CODE>null</CODE> if it's not found.
   * @exception IllegalArgumentException If the id passed in is null.
   */
  protected Classpath findClasspath(String anId, Collection<Magnet> someMagnets) {
    // Validate the arguments
    if (anId == null) {
      throw new IllegalArgumentException("The classpath identifer passed in is null");
    } else if (someMagnets == null) {
      throw new IllegalArgumentException("The collection of magnet passed in is null");
    }
    
    Classpath aResult = null;
    for (Iterator<Magnet> it = someMagnets.iterator(); aResult == null && it.hasNext(); ) {
      Magnet aParentMagnet = it.next();
      aResult = findClasspath(anId, aParentMagnet);
    }
    
    return aResult;
  }
  
  /**
   * Finds the classpath identifier by the ID passed in.
   *
   * @param anId The identifier of the classpath to retrieve.
   * @param aMagnet The magnet in which the classpath is.
   * @return The found classpath or <CODE>null</CODE> if it's not found.
   * @exception IllegalArgumentException If the id passed in is null.
   */
  protected Classpath findClasspath(String anId, Magnet aMagnet) {
    // Validate the arguments
    if (anId == null) {
      throw new IllegalArgumentException("The classpath identifer passed in is null");
    } else if (aMagnet == null) {
      throw new IllegalArgumentException("The magnet passed in is null");
    }

    // Search for the classpath
    boolean isFound = false;
    Classpath aResult = null;

    for (Iterator<Object> it = aMagnet.getObjectsFor("classpath").iterator(); !isFound && it.hasNext(); ) {
      Classpath aClasspath = (Classpath) it.next();
      if (anId.equals(aClasspath.getId())) {
        isFound = true;
        aResult = aClasspath;
      }
    }

    if (aResult == null && aMagnet.getParents().size() != 0) {
      return findClasspath(anId, aMagnet.getParents());
    } else {
      return aResult;
    }
  }

  /**
   * Returns the class loader of the parent classpath passed.
   *
   * @param aClasspath The child classpath.
   * @return The parent class loader created.
   */
  protected ClassLoader getParentClassloader(Classpath aClasspath) throws MagnetException {
    // Validate the argument
    if (aClasspath == null) {
      throw new RuntimeException("The classpath passed in is null");
    }

    ArrayList<String> someIdentifiers = new ArrayList<String>();
    return getParentClassloaderIter(aClasspath, someIdentifiers);
  }

  /**
   *
   */
  private ClassLoader getParentClassloaderIter(Classpath aClasspath, List<String> someIdentifiers) throws MagnetException {
    // Validate for circular references
    if (someIdentifiers.contains(aClasspath.getId())) {
      throw new IllegalStateException("Circular referenced classpath objects detected: " + aClasspath.getId());
    } else {
      someIdentifiers.add(aClasspath.getId());
    }

    if (aClasspath.getParent() == null) {
      return ClassLoader.getSystemClassLoader();
    } else {
      Classpath aParentClasspath = findClasspath(aClasspath.getParent(), getMagnet());
      if (aParentClasspath == null) {
        String aMessage = "Unable to find the parent classpath: " + aClasspath.getParent();
        _theLogger.error(aMessage);
        throw new MagnetException(aMessage);
      }

      ClassLoader aParentClassloader = getParentClassloaderIter(aParentClasspath, someIdentifiers);
      return aParentClasspath.createClassLoader(aParentClassloader);
    }
  }

  /**
   * Parses the command string of this launcher to generate an array of command arguments.
   *
   * @return An array of command arguments
   */
  protected String[] parseCommandString() {
    if (_theArguments != null && _theArguments.length() > 0) {
      CmdLine aCommandLine = CmdLine.parse(_theArguments);
      return aCommandLine.toArray();
    } else {
      return new String[0];
    }
  }

  /**
   *
   */
  private void logClassLoader(ClassLoader aClassLoader) {
    StringBuffer aBuffer = new StringBuffer();
    aBuffer.append("Context class loader is: ").append(aClassLoader).append("\n");
    if (aClassLoader instanceof java.net.URLClassLoader) {
      java.net.URL[] someURLs = ((java.net.URLClassLoader) aClassLoader).getURLs();
      for (int i = 0; i < someURLs.length; i++) {
        aBuffer.append("\t[").append(i+1).append("] ").append(someURLs[i]).append("\n");
      }
    }
    while (aClassLoader.getParent() != null) {
      aClassLoader = aClassLoader.getParent();
      aBuffer.append("Parent class loader is: ").append(aClassLoader).append("\n");
      if (aClassLoader instanceof java.net.URLClassLoader) {
        java.net.URL[] someURLs = ((java.net.URLClassLoader) aClassLoader).getURLs();
        for (int i = 0; i < someURLs.length; i++) {
          aBuffer.append("\t[").append(i+1).append("] ").append(someURLs[i]).append("\n");
        }
      }
    }
    _theLogger.info(aBuffer.toString());
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  OVERRIDEN METHODS  //////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Renders this objects to the magnet context passed in.
   *
   * @param aContext The magnet context to use.
   * @exception RenderingException If an error occurs while rendering this object.
   */
  public void render(MagnetContext aContext) throws RenderingException {
    // Render the super class
    super.render(aContext);

    // Find a profile object
    Profile aProfile = findProfile(aContext.getProfile());

    if (aProfile != null) {
      // Render the profile and generate a new context
      aProfile.render(aContext);
      MagnetContext aSubContext = new MagnetContext(aContext);
      if (aProfile.getParameters() != null) {
        for (Param param: aProfile.getParameters().getParams()) {
          aSubContext.addParameter(param, false);
        }
      }

      // Resolving the attribute with the subcontext
      _theMainClass = resolveValue(aSubContext, _theMainClass);
      _theArguments = resolveValue(aSubContext, _theArguments);
      _isDaemon = resolveValue(aSubContext, _isDaemon);
      
    } else {
      // The profile specified is not found
      if (aContext.getProfile() == null || aContext.getProfile().length() == 0) {
        Log.warn("Unable to find a default profile in this system launcher", this);
      } else {
        Log.warn("Unable to find the profile " + aContext.getProfile() + " in this system launcher", this);
      }
    }
  }

  /**
   * Executes this launch handler for the passed in profile.
   *
   * @param aProfileName The name of the profile to execute.
   */
  public void execute(String aProfileName) {
    try {
      // Find a profile object
      Profile aProfile = findProfile(aProfileName);
      if (aProfile == null) {
        if (aProfileName == null || aProfileName.length() == 0) {
          Log.warn("Skipping this java launcher --> no default profile defined", this);
        } else {
          Log.warn("Skipping this java launcher --> no profile found for the name " + aProfileName, this);
        }
        return;
      }

      Log.info("Executing profile " + aProfile.getName(), this);

      // Find the classpath
      ClassLoader aClassloader = ClassLoader.getSystemClassLoader();
      Collection<Object> someClasspath = aProfile.getObjectsFor("classpath");
      if (!someClasspath.isEmpty()) {
        // Create the class loader
        Classpath aClasspath = (Classpath) someClasspath.iterator().next();
        ClassLoader aParentClassloader = getParentClassloader(aClasspath);
        aClassloader = aClasspath.createClassLoader(aParentClassloader);
      }

      // Defining the arguments of the main method 
      String[] someArguments = parseCommandString();

      // Logging info on the java class to execute
      logClassLoader(aClassloader);
      Log.info("Calling the main() method on the class " + _theMainClass, this);

      if (isDaemon()) {
        Log.info("Executing the main() method using a daemon thread", this);
      }

      if (someArguments.length > 0) {
        StringBuffer aBuffer = new StringBuffer();
        Log.info("Applicatiom arguments passed to the main() method:", this);
        for (int i = 0; i < someArguments.length; i++) {
          aBuffer.setLength(0);
          aBuffer.append("\targs[").append(i).append("] = ").append(someArguments[i]);
          Log.info(aBuffer.toString(), this);
        }
      }
      
      // Load the main class
      Class<?> aClass = aClassloader.loadClass(_theMainClass);
      JavaTask aTask = new JavaTask(aClass, someArguments);
      Thread aThread = new Thread(aTask);
      aThread.setName("MagnetThread-" + getName() + ":" + aProfile.getName());
      aThread.setContextClassLoader(aClassloader);
      aThread.setDaemon(isDaemon());
      aThread.start();

    } catch (MagnetException de) {
      String aMessage = "Application error spawning this java process";
      _theLogger.error(aMessage, de);
      Log.error(aMessage + " - " + Log.extactMessage(de), this);

    } catch (ClassNotFoundException cnfe) {
      String aMessage = "Unable to spawn a java process\n\t---> the class specified is not found";
      _theLogger.error(aMessage, cnfe);
      Log.error(aMessage + " - " + Log.extactMessage(cnfe), this);

    } catch (RuntimeException re) {
      String aMessage = "System error spawning this java process";
      _theLogger.error(aMessage, re);
      Log.error(aMessage + " - " + Log.extactMessage(re), this);
    }
  }

  /**
   * Returns a string representation of this java launcher.
   *
   * @return The string representation of this java launcher.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[mainClass=").append(_theMainClass).
            append(" arguments=").append(_theArguments).
            append(" isDaemon=").append(_isDaemon).
            append("]");

    return aBuffer.toString();
  }
  
}
