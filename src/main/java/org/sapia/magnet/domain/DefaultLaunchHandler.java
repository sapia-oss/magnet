package org.sapia.magnet.domain;

// Import of Sun's JDK classes
// ---------------------------
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// Import of Apache's log4j
// ------------------------
import org.apache.log4j.Logger;

// Import of Sapia's magnet classes
// --------------------------------
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
public class DefaultLaunchHandler extends AbstractObjectHandler implements LaunchHandlerIF {

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  CLASS ATTRIBUTES  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the logger instance for this class. */
  private static final Logger _theLogger = Logger.getLogger(DefaultLaunchHandler.class);

  /** The default time to wait after executing this launcher. */  
  public static final int DEFAULT_WAIT_TIME = 1000;

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The type of this default launcher. */
  private String _theType;

  /** The name of this default launcher. */
  private String _theName;

  /** The default profile name of this launcher. */
  private String _theDefault;
  
  /** The number of milliseconds to wait after executing this launcher. */ 
  private int _theWaitTime;
  
  /** The name of the OS that this launcher is expected to be executed under */
  private String _os;

  /** The magnet of this default launcher. */
  private Magnet _theMagnet;

  /** The list of profiles of this default launcher. */
  private Map _theProfiles;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new DefaultLaunchHandler instance.
   */
  protected DefaultLaunchHandler() {
    _theProfiles = new HashMap();
    _theWaitTime = DEFAULT_WAIT_TIME;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the type of this default launch handler.
   *
   * @return The type of this default launch handler.
   */
  public String getType() {
    return _theType;
  }

  /**
   * Returns the name of this default launch handler.
   *
   * @return The name of this default launch handler.
   */
  public String getName() {
    return _theName;
  }

  /**
   * Returns the default profile name of this default launch handler.
   *
   * @return The default program name of this default launch handler.
   */
  public String getDefault() {
    return _theDefault;
  }
  
  /**
   * Returns the number of milliseoncds to wait after executing this launcher.
   * 
   * @return the number of milliseoncds to wait after executing this launcher.
   */
  public int geWaitTime() {
    return _theWaitTime;
  }

  /**
   * Returns the parent magnet of this default laucn handler.
   *
   * @return The parent magnet of this default laucn handler.
   */
  public Magnet getMagnet() {
    return _theMagnet;
  }

  /**
   * Returns the collection of profiles of this default launch handler.
   *
   * @return The collection of <CODE>Profile</CODE> objects of this default launch handler.
   */
  public Collection getProfiles() {
    return _theProfiles.values();
  }

  /**
   * Returns the profile for the profile name passed in.
   *
   * @param aProfileName The name of the profile to retrieve.
   * @return The profile for the profile name passed in.
   */
  public Profile getProfile(String aProfileName) {
    return (Profile) _theProfiles.get(aProfileName);
  }

  /**
   * Searches for the profile object of this launcher identified by the profile name
   * passed in. If no profile is found, it searches for the default profile.
   *
   * @param aProfileName The name of the profile to search.
   * @return The asspciated profile object or null if no profile is found.
   * @exception IllegalArgumentException If the profile name passed in is null.
   */
  public Profile findProfile(String aProfileName) {
    // Validate the argument
    if (aProfileName == null) {
      throw new IllegalArgumentException("The profile name passed in is null");
    }

    // Search for the profile
    Profile aProfile = getProfile(aProfileName);
    if (aProfile == null && _theDefault != null) {
      aProfile = getProfile(_theDefault);
    }

    return aProfile;
  }
  
  public String getOs(){
    return _os;
  }

  public void setOs(String os) {
    _os = os;
  }
  
  public boolean isOsMatch(){
    if(_os != null){
      return System.getProperty("os.name").toLowerCase().indexOf(_os) >= 0;
    }
    else{
      return true;
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the type of this system launcher to the one passed in.
   *
   * @param aType The new type.
   */
  public void setType(String aType) {
    _theType = aType;
  }

  /**
   * Changes the name of this system launcher to the one passed in.
   *
   * @param aName The new name.
   */
  public void setName(String aName) {
    _theName = aName;
  }

  /**
   * Changes the default profile name of this default launch handler to the one passed in.
   *
   * @param aProfileName The name of the new default profile.
   */
  public void setDefault(String aProfileName) {
    _theDefault = aProfileName;
  }

  /**
   * Changes the value of the time to wait after executing this launcher.
   * 
   * @param aWaitTime The new wait time value in milliseconds.
   */
  public void setWaitTime(int aWaitTime) {
    _theWaitTime = aWaitTime;
  }

  /**
   * Changes the parent magnet of this launch handler.
   *
   * @param aMagnet the new Magnet.
   */
  public void setMagnet(Magnet aMagnet) {
    _theMagnet = aMagnet;
  }

  /**
   * Adds the profile passed in to this default launch handler.
   *
   * @param aProfile the profile to add.
   */
  public void addProfile(Profile aProfile) {
    if (aProfile == null) {
      throw new IllegalArgumentException("The profile passed in is null");
    } else if (aProfile.getName() == null) {
      throw new IllegalArgumentException("The name of the profile passed in is null");
    }

    _theProfiles.put(aProfile.getName(), aProfile);
  }

  /**
   * Executes this launch handler for the passed in profile.
   *
   * @param aProfile The profile to execute.
   */
  public void execute(String aProfile) {
    if (_theLogger.isInfoEnabled()) {
      _theLogger.info("Executing this launch handler for the profile " +
              aProfile + "\n\t" + toString());
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
    super.renderHandlerDefs(aContext);
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  OVERRIDEN METHODS  //////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns a string representation of this system launcher.
   *
   * @return A string representation of this system launcher.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[type=").append(_theType).
            append(" name=").append(_theName).
            append(" default=").append(_theDefault).
            append(" waitTime=").append(_theWaitTime).
            append(" profiles=").append(_theProfiles.values()).
            append("]");

    return aBuffer.toString();
  }
}
