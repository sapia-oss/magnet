package org.sapia.magnet.domain.system;

// Import of Sun's JDK classes
// ---------------------------
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.TreeMap;

// Import of Apache's log4j
// ------------------------
import org.apache.log4j.Logger;

// Import of Sapia's magnet classes
// --------------------------------
import org.sapia.console.CmdLine;
import org.sapia.magnet.Log;
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
public class SystemLauncher extends DefaultLaunchHandler {

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  CLASS ATTRIBUTES  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the logger instance for this class. */
  private static final Logger _theLogger = Logger.getLogger(SystemLauncher.class);

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The system command to execute. */
  private String _theCommand;

  /** The working directory of the system launcher. */
  private String _theWorkingDirectory;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new SystemLauncher instance.
   */
  public SystemLauncher() {
    _theWorkingDirectory = System.getProperty("user.dir");
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the command of this system launch.
   *
   * @return The command of this system launch.
   */
  public String getCommand() {
    return _theCommand;
  }

  /**
   * Returns the working directory of this system launch.
   *
   * @return The working directory of this system launch.
   */
  public String getWorkingDirectory() {
    return _theWorkingDirectory;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the command of this system launcher to the one passed in.
   *
   * @param aCommand The new command.
   */
  public void setCommand(String aCommand) {
    _theCommand = aCommand;
  }

  /**
   * Changes the working directory of this system launcher.
   *
   * @param aWorkingDirectory The new working directory.
   */
  public void setWorkingDirectory(String aWorkingDirectory) {
    _theWorkingDirectory = aWorkingDirectory;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  HELPER METHODS  ////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Finds the environment identifier by the ID passed in.
   *
   * @param anId The identifier of the environment to retrieve.
   * @param someMagnets The collection of magnet in which the environment is.
   * @return The find environment of <CODE>null</CODE> if it's not found.
   * @exception IllegalArgumentException If the id passed in is null.
   */
  protected Environment findEnvironment(String anId, Collection someMagnets) {
    // Validate the arguments
    if (anId == null) {
      throw new IllegalArgumentException("The environment identifer passed in is null");
    } else if (someMagnets == null) {
      throw new IllegalArgumentException("The collection of magnet passed in is null");
    }
    
    Environment aResult = null;
    for (Iterator it = someMagnets.iterator(); aResult == null && it.hasNext(); ) {
      Magnet aParentMagnet = (Magnet) it.next();
      aResult = findEnvironment(anId, aParentMagnet);
    }
    
    return aResult;
  }
  
  /**
   * Finds the environment identifier by the ID passed in.
   *
   * @param anId The identifier of the environment to retrieve.
   * @param aMagnet The magnet in which the environment is.
   * @return The find environment of <CODE>null</CODE> if it's not found.
   * @exception IllegalArgumentException If the id passed in is null.
   */
  protected Environment findEnvironment(String anId, Magnet aMagnet) {
    // Validate the arguments
    if (anId == null) {
      throw new IllegalArgumentException("The environment identifer passed in is null");
    } else if (aMagnet == null) {
      throw new IllegalArgumentException("The magnet passed in is null");
    }

    // Search for the environment
    boolean isFound = false;
    Environment aResult = null;

    for (Iterator it = aMagnet.getObjectsFor("Environment").iterator(); !isFound && it.hasNext(); ) {
      Environment anEnvironment = (Environment) it.next();
      if (anId.equals(anEnvironment.getId())) {
        isFound = true;
        aResult = anEnvironment;
      }
    }

    if (aResult == null && aMagnet.getParents().size() != 0) {
      return findEnvironment(anId, aMagnet.getParents());
    } else {
      return aResult;
    }
  }

  /**
   * Extract the variables from the environment (and all it's parent) passed in.
   *
   * @param anEnvironment The environment from which to extrac the variables.
   * @return The list of variables extracted.
   * @exception IllegalArgumentException If the environment passed in is null.
   */
  protected List extractVariables(Environment anEnvironment) {
    // Validate the arguments
    if (anEnvironment == null) {
      throw new IllegalArgumentException("The environment passed in is null");
    }

    ArrayList someVariables = new ArrayList();
    ArrayList someIdentifiers = new ArrayList();
    extractVariablesIter(anEnvironment, someVariables, someIdentifiers);

    return someVariables;
  }

  /**
   * Iter-recursive method to extract the variables from the environment object.
   */
  private void extractVariablesIter(Environment anEnvironment, List someVariables, List someIdentifiers) {
    // Validate for circular references
    if (someIdentifiers.contains(anEnvironment.getId())) {
      throw new IllegalStateException("Circular referenced environment objects detected");
    } else {
      someIdentifiers.add(anEnvironment.getId());
    }

    if (anEnvironment.getParent() != null) {
      Environment aParent = findEnvironment(anEnvironment.getParent(), getMagnet());
      if (aParent == null) {
        String aMessage = "Unable to find the parent environment: " + anEnvironment.getParent();
        _theLogger.error(aMessage);
        throw new RuntimeException(aMessage);
      } else {
        extractVariablesIter(aParent, someVariables, someIdentifiers);
      }
    }

    for (Iterator it = anEnvironment.getVariables().iterator(); it.hasNext(); ) {
      Variable aVariable = (Variable) it.next();
      someVariables.add(aVariable);
    }
  }

  /**
   * Parses the command string of this launcher to generate an array of command arguments.
   *
   * @return An array of command arguments
   */
  protected String[] parseCommandString() {
    CmdLine aCommandLine = CmdLine.parse(_theCommand);
    return aCommandLine.toArray();
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
    // Render the super class
    super.render(aContext);

    // Find a profile object
    Profile aProfile = findProfile(aContext.getProfile());

    if (aProfile != null) {
      // Render the profile and generate a new context
      aProfile.render(aContext);
      MagnetContext aSubContext = new MagnetContext(aContext);
      for (Iterator it = aProfile.getParameters().getParams().iterator(); it.hasNext(); ) {
        Param aParam = (Param) it.next();
        aSubContext.addParameter(aParam, false);
      }

      // Resolving the attribute with the subcontext
      _theCommand = resolveValue(aSubContext, _theCommand);
      _theWorkingDirectory = resolveValue(aSubContext, _theWorkingDirectory);
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
          Log.warn("Skipping this system launcher --> no default profile defined", this);
        } else {
          Log.warn("Skipping this system launcher --> no profile found for the name " + aProfileName, this);
        }
        return;
      }
      
      if(!isOsMatch()){
        Log.warn("Skipping this system launcher --> expected OS " + getOs(), this);
        return;
      }

      Log.info("Executing profile " + aProfile.getName(), this);

      // Setup the command to run
      String[] someCommands = parseCommandString();

      // Setup the environment variables
      TreeMap someEnvironmentVariables = new TreeMap();

      for (Iterator it = aProfile.getObjectsFor("environment").iterator(); it.hasNext(); ) {
        Environment anEnvironment = (Environment) it.next();

        for (Iterator someVariables = extractVariables(anEnvironment).iterator(); someVariables.hasNext(); ) {
          Variable aVariable = (Variable) someVariables.next();
          StringBuffer aBuffer = new StringBuffer();
          aBuffer.append(aVariable.getName()).append("=").append(aVariable.getValue());
          someEnvironmentVariables.put(aVariable.getName(), aBuffer.toString());
        }
      }

      // Setup the working directory
      File aDirectory = new File(_theWorkingDirectory);
      if (aDirectory == null || !aDirectory.isDirectory()) {
        String aMessage = "The working directory '" + _theWorkingDirectory + "' is invalid";
        Log.error(aMessage, this);
        throw new RuntimeException(aMessage);
      }

      // Logging the info of the process to run
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Set the working directory to '").append(_theWorkingDirectory).append("'");
      Log.info(aBuffer.toString(), this);

      aBuffer.setLength(0);
      aBuffer.append("Running the command '").append(_theCommand).append("'");
      Log.info(aBuffer.toString(), this);
      
      if (someEnvironmentVariables.size() > 0) {
        Log.info("Environment variables of the system process:", this);
        for (Iterator it = someEnvironmentVariables.values().iterator(); it.hasNext(); ) {
          aBuffer.setLength(0);
          aBuffer.append("\t").append(it.next());
          Log.info(aBuffer.toString(), this);
        }
      }

      // Create a process task and starts it in another thread
      ProcessTask aTask = new ProcessTask(someCommands,
              (String[]) someEnvironmentVariables.values().toArray(new String[0]), aDirectory);
      Thread aThread = new Thread(aTask);
      aThread.setName(getName() + "-" + aProfile.getName());
      aThread.start();

    } catch (RuntimeException re) {
      String aMessage = "System error spawning this process";
      _theLogger.error(aMessage, re);
    }
  }

  /**
   * Returns a string representation of this system launcher.
   *
   * @return A string representation of this system launcher.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[command=").append(_theCommand).
            append(" workingDirectory=").append(_theWorkingDirectory).
            append("]");

    return aBuffer.toString();
  }
}
