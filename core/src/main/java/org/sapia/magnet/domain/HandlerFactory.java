package org.sapia.magnet.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;


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
public class HandlerFactory {

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  CLASS ATTRIBUTES  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the logger instance for this class. */
  private static final Logger _theLogger = Logger.getLogger(HandlerFactory.class);

  /** Defines the singleton instance of this class. */
  private static final HandlerFactory _theInstance = new HandlerFactory();

  /** Defines the resource name that contains the launch handler factory configuration. */
  private static final String RESOURCE_LAUNCH_HANDLER_FACTORY = "resources/launchHandlerFactory.properties";

  /** Defines the resource name that contains the protocol handler factory configuration. */
  private static final String RESOURCE_PROTOCOL_HANDLER_FACTORY = "resources/protocolHandlerFactory.properties";

  /** Defines the resource name that contains the script handler factory configuration. */
  private static final String RESOURCE_SCRIPT_HANDLER_FACTORY = "resources/scriptHandlerFactory.properties";

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  STATIC METHODS  ////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the singleton handler factory instance.
   *
   * @return The singleton handler factory instance.
   */
  public static HandlerFactory getInstance() {
    return _theInstance;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the map that contains the classes for launch handlers. */
  private Map<String, String> _theLaunchHandlerClasses;

  /** Defines the map that contains the classes for protocol handlers. */
  private Map<String, String> _theProtocolHandlerClasses;

  /** Defines the map that contains the classes for script handlers. */
  private Map<String, String> _theScriptHandlerClasses;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new HandlerFactory instance. Constructor is hidden on purpose.
   */
  protected HandlerFactory() {
    _theLaunchHandlerClasses = new HashMap<String, String>();
    _theProtocolHandlerClasses = new HashMap<String, String>();
    _theScriptHandlerClasses = new HashMap<String, String>();
    initialize();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  UTILITY METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Initializes this handler factory instance.
   */
  private void initialize() {
    InputStream anInput = null;
    Properties someProperties;

    // Loading the launch handler factory configuration properties
    try {
      anInput = getClass().getResourceAsStream(RESOURCE_LAUNCH_HANDLER_FACTORY);
      someProperties = new Properties();
      someProperties.load(anInput);
      for (Enumeration<?> enumeration = someProperties.propertyNames(); enumeration.hasMoreElements(); ) {
        String aName = (String) enumeration.nextElement();
        String aValue = someProperties.getProperty(aName);
        _theLaunchHandlerClasses.put(aName, aValue);
      }
      
    } catch (IOException ioe) {
      _theLogger.warn("Error getting the input stream of the laucnh handler factory configuration", ioe);
    } finally {
      if (anInput != null) {
        try {
          anInput.close();
        } catch (IOException ioe) {
          _theLogger.warn("Error closing the input stream of the laucnh handler factory configuration", ioe);
        }
      }
    }

    // Loading the protocol handler factory configuration properties
    try {
      anInput = getClass().getResourceAsStream(RESOURCE_PROTOCOL_HANDLER_FACTORY);
      someProperties = new Properties();
      someProperties.load(anInput);
      for (Enumeration<?> enumeration = someProperties.propertyNames(); enumeration.hasMoreElements(); ) {
        String aName = (String) enumeration.nextElement();
        String aValue = someProperties.getProperty(aName);
        _theProtocolHandlerClasses.put(aName, aValue);
      }
    } catch (IOException ioe) {
      _theLogger.warn("Error getting the input stream of the protocol handler factory configuration", ioe);
    } finally {
      if (anInput != null) {
        try {
          anInput.close();
        } catch (IOException ioe) {
          _theLogger.warn("Error closing the input stream of the protocol handler factory configuration", ioe);
        }
      }
    }

    // Loading the script handler factory configuration properties
    try {
      anInput = getClass().getResourceAsStream(RESOURCE_SCRIPT_HANDLER_FACTORY);
      someProperties = new Properties();
      someProperties.load(anInput);
      for (Enumeration<?> enumeration = someProperties.propertyNames(); enumeration.hasMoreElements(); ) {
        String aName = (String) enumeration.nextElement();
        String aValue = someProperties.getProperty(aName);
        _theScriptHandlerClasses.put(aName, aValue);
      }
    } catch (IOException ioe) {
      _theLogger.warn("Error getting the input stream of the script handler factory configuration", ioe);
    } finally {
      if (anInput != null) {
        try {
          anInput.close();
        } catch (IOException ioe) {
          _theLogger.warn("Error closing the input stream of the script handler factory configuration", ioe);
        }
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Adds the new association between the launcher type and the class name
   * passed in.
   *
   * @param aType The laucnher type.
   * @param aClassName The name of the class that handles the laucnher.
   */
  public void addLaunchHandler(String aType, String aClassName) {
    _theLaunchHandlerClasses.put(aType, aClassName);
  }

  /**
   * Adds the new association between the protocol name and the class name
   * passed in.
   *
   * @param aProtocol A protocol name.
   * @param aClassName The name of the class that handles the protocol.
   */
  public void addProtocolHandler(String aProtocol, String aClassName) {
    _theProtocolHandlerClasses.put(aProtocol, aClassName);
  }

  /**
   * Adds the new association between the script type and the class name
   * passed in.
   *
   * @param aType The script type.
   * @param aClassName The name of the class that handles the script.
   */
  public void addScriptHandler(String aType, String aClassName) {
    _theScriptHandlerClasses.put(aType, aClassName);
  }

  /**
   * Creates protocol handler for the protocol name passed in.
   *
   * @param aProtocol The protocol name.
   * @return The created protocol handler.
   * @exception ObjectCreationException If an error occurs while creating the protocol handler.
   */
  public ProtocolHandlerIF createProtocolHandler(String aProtocol) throws ObjectCreationException {
    String aClassName = (String) _theProtocolHandlerClasses.get(aProtocol);
    if (aClassName != null) {
      try {
        Class<?> aClass = Class.forName(aClassName);
        Object aHandler = aClass.newInstance();
        return (ProtocolHandlerIF) aHandler;
        
      } catch (ClassNotFoundException cnfe) {
        String aMessage = "Unable to create a handler for the protocol " + aProtocol +
                          " - the associated class is not found " + aClassName;
        throw new ObjectCreationException(aMessage, cnfe);
        
      } catch (IllegalAccessException iae) {
        String aMessage = "Unable to create a handler for the protocol " + aProtocol +
                          " - could not access the associated class " + aClassName;
        throw new ObjectCreationException(aMessage, iae);
        
      } catch (InstantiationException ie) {
        String aMessage = "Unable to create a handler for the protocol " + aProtocol +
                          " - could not call the default constructor of " + aClassName;
        throw new ObjectCreationException(aMessage, ie);
        
      } catch (ClassCastException cce) {
        String aMessage = "Unable to create a handler for the protocol " + aProtocol +
                          " - the associated class " + aClassName + " is not a ProtocolHandlerIF";
        throw new ObjectCreationException(aMessage, cce);
      }
      
    } else {
      throw new ObjectCreationException("The protocol of the path to render is invalid: " + aProtocol);
    }
  }

  /**
   * Creates a new launcher handler for the type passed in.
   *
   * @param aLauncherType The type for which to create a handler.
   * @return The launcher handler created.
   * @exception ObjectCreationException If an error occurs while creating the launch handler.
   */
  public LaunchHandlerIF createLaunchHandler(String aLauncherType) throws ObjectCreationException {
    String aClassName = (String) _theLaunchHandlerClasses.get(aLauncherType);
    if (aClassName != null) {
      try {
        Class<?> aClass = Class.forName(aClassName);
        Object aHandler = aClass.newInstance();
        return (LaunchHandlerIF) aHandler;
        
      } catch (ClassNotFoundException cnfe) {
        String aMessage = "Unable to create a handler for the launcher type " + aLauncherType +
                          " - the associated class is not found " + aClassName;
        throw new ObjectCreationException(aMessage, cnfe);
        
      } catch (IllegalAccessException iae) {
        String aMessage = "Unable to create a handler for the launcher type " + aLauncherType +
                          " - could not access the associated class " + aClassName;
        throw new ObjectCreationException(aMessage, iae);
        
      } catch (InstantiationException ie) {
        String aMessage = "Unable to create a handler for the launcher type " + aLauncherType +
                          " - could not call the default constructor of " + aClassName;
        throw new ObjectCreationException(aMessage, ie);
        
      } catch (ClassCastException cce) {
        String aMessage = "Unable to create a handler for the launcher type " + aLauncherType +
                          " - the associated class " + aClassName + " is not a LaunchHandlerIF";
        throw new ObjectCreationException(aMessage, cce);
      }
      
    } else {
      throw new ObjectCreationException("The launcher type is invalid: " + aLauncherType);
    }
  }

  /**
   * Creates a new script handler for the script type passed in.
   *
   * @param aScriptType The type of script for which to create a handler.
   * @return The created script handler.
   * @exception ObjectCreationException If an error occurs while creating the script handler.
   */
  public ScriptHandlerIF createScriptHandler(String aScriptType) throws ObjectCreationException {
    String aClassName = (String) _theScriptHandlerClasses.get(aScriptType);
    if (aClassName != null) {
      try {
        Class<?> aClass = Class.forName(aClassName);
        Object aHandler = aClass.newInstance();
        return (ScriptHandlerIF) aHandler;
        
      } catch (ClassNotFoundException cnfe) {
        String aMessage = "Unable to create a handler for the script type " + aScriptType +
                          " - the associated class is not found " + aClassName;
        throw new ObjectCreationException(aMessage, cnfe);
        
      } catch (IllegalAccessException iae) {
        String aMessage = "Unable to create a handler for the script type " + aScriptType +
                          " - could not access the associated class " + aClassName;
        throw new ObjectCreationException(aMessage, iae);
        
      } catch (InstantiationException ie) {
        String aMessage = "Unable to create a handler for the script type " + aScriptType +
                          " - could not call the default constructor of " + aClassName;
        throw new ObjectCreationException(aMessage, ie);
        
      } catch (ClassCastException cce) {
        String aMessage = "Unable to create a handler for the script type " + aScriptType +
                          " - the associated class " + aClassName + " is not a LaunchHandlerIF";
        throw new ObjectCreationException(aMessage, cce);
      }
      
    } else {
      throw new ObjectCreationException("The script type is invalid: " + aScriptType);
    }
  }
}
