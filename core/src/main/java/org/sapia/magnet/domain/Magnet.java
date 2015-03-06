package org.sapia.magnet.domain;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
public class Magnet extends AbstractObjectHandler {

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  CLASS ATTRIBUTES  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines a null object for mapping of null profiles. */
  private static final String _theNullProfile = Magnet.class.getName()+".NULL_PROFILE";

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The name of this magnet. */
  private String _theName;

  /** The description of this magnet. */
  private String _theDescription;

  /** The extends of this magnet. */
  private String _theExtends;
  
  /** The parent magnets of this magnet. */
  private List<Magnet> _theParents;

  /** The map of scripts objects of this magnet by the profile name. */
  private Map<String, Script> _theScriptsByProfile;

  /** The map of parameters objects of this magnet by the profile name. */
  private Map<String, Parameters> _theParametersByProfile;

  /** The list of launchers of this magnet. */
  private List<Launcher> _theLaunchers;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Magnet instance.
   */
  public Magnet() {
    super();
    _theParametersByProfile = new HashMap<String, Parameters>();
    _theScriptsByProfile = new HashMap<String, Script>();
    _theParents = new ArrayList<Magnet>();
    _theLaunchers = new ArrayList<Launcher>();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the name of this magnet.
   *
   * @return The name of this magnet.
   */
  public String getName() {
    return _theName;
  }

  /**
   * Returns the description of this magnet.
   *
   * @return The description of this magnet.
   */
  public String getDescription() {
    return _theDescription;
  }

  /**
   * Returns the magnets that are extended by this magnet.
   *
   * @return The magnets that are extended by this magnet.
   */
  public String getExtends() {
    return _theExtends;
  }
  
  /**
   * Returns the parent magnets of this magnet.
   *
   * @return The parent magnets of this magnet.
   */
  public Collection<Magnet> getParents() {
    return _theParents;
  }

  /**
   * Returns the collection of scripts of this magnet.
   *
   * @return The collection of <CODE>Script</CODE> objects.
   * @see Script
   */
  public Collection<Script> getScripts() {
    return _theScriptsByProfile.values();
  }

  /**
   * Returns the collection of parameters of this magnet.
   *
   * @return The collection of <CODE>Parameters</CODE> objects.
   * @see Parameters
   */
  public Collection<Parameters> getParameters() {
    Set<Parameters> sorted = new TreeSet<Parameters>(new Comparator<Parameters>() {
      public int compare(Parameters p1, Parameters p2) {
        return p1.getOrder() - p2.getOrder();
      }
    });
    
    sorted.addAll(_theParametersByProfile.values());
    return sorted;
  }

  /**
   * Returns the collection of launcher of this magnet.
   *
   * @return The collection of <CODE>Launcher</CODE> objects.
   * @see Launcher
   */
  public Collection<Launcher> getLaunchers() {
    return _theLaunchers;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the name of this magnet for the value passed in.
   *
   * @param aName The new name of this magnet.
   */
  public void setName(String aName) {
    _theName = aName;
  }

  /**
   * Changes the description of this magnet for the value passed in.
   *
   * @param aDescription The new name of this magnet.
   */
  public void setDescription(String aDescription) {
    _theDescription = aDescription;
  }

  /**
   * Changes the extends of this magnet for the value passed in.
   *
   * @param anExtends The new extends of this magnet.
   */
  public void setExtends(String anExtends) {
    _theExtends = anExtends;
  }

  /**
   * Insert the magnet passed in at the beginning of
   * th list of parent magnets.
   *
   * @param aMagnet The new parent magnet to insert.
   */
  public void insertParent(Magnet aMagnet) {
    _theParents.add(0, aMagnet);
  }

  /**
   * Adds the script passed in to this magnet.
   *
   * @param aScript The script to add.
   */
  public void addScript(Script aScript) {
    if (aScript == null) {
      throw new IllegalArgumentException("The script passed in is null");
    }

    if (aScript.getProfile() == null) {
      _theScriptsByProfile.put(_theNullProfile, aScript);
    } else {
      _theScriptsByProfile.put(aScript.getProfile(), aScript);
    }
  }

  /**
   * Removes the script passed in from this magnet.
   *
   * @param aScript The script to remove.
   */
  public void removeScript(Script aScript) {
    if (aScript == null) {
      throw new IllegalArgumentException("The script passed in is null");
    }

    if (aScript.getProfile() == null) {
      _theScriptsByProfile.remove(_theNullProfile);
    } else {
      _theScriptsByProfile.remove(aScript.getProfile());
    }
  }

  /**
   * Removes all the scripts from this magnet.
   */
  public void clearScripts() {
    _theScriptsByProfile.clear();
  }

  /**
   * Adds the parameters passed in to this magnet.
   *
   * @param aParameters The parameters to add.
   */
  public void addParameters(Parameters aParameters) {
    if (aParameters == null) {
      throw new IllegalArgumentException("The parameters passed in is null");
    }
    
    aParameters.setOrder(_theParametersByProfile.size());

    // Look if we are getting the global parameters or not
    String aKey;
    if (aParameters.getProfile() == null) {
      aKey = _theNullProfile;
    } else {
      aKey = aParameters.getProfile(); 
    }

    // Add all the param to the existing parameters
    if (_theParametersByProfile.containsKey(aKey)) {
      Parameters aMasterParameters = (Parameters) _theParametersByProfile.get(aKey);
      for (Param p: aParameters.getParams()) {
        aMasterParameters.addParam(p);
      }
    } else {
      // Add the new parameters
      _theParametersByProfile.put(aKey, aParameters);
    }
  }

  /**
   * Removes the parameters passed in from this magnet.
   *
   * @param aParameters The parameters to remove.
   */
  public void removeParameters(Parameters aParameters) {
    if (aParameters == null) {
      throw new IllegalArgumentException("The parameters passed in is null");
    }

    if (aParameters.getProfile() == null) {
      _theParametersByProfile.remove(_theNullProfile);
    } else {
      _theParametersByProfile.remove(aParameters.getProfile());
    }
  }

  /**
   * Removes all the parameters from this magnet.
   */
  public void clearParameters() {
    _theParametersByProfile.clear();
  }

  /**
   * Adds the launcher passed in to this magnet.
   *
   * @param aLauncher The launcher to add.
   */
  public void addLauncher(Launcher aLauncher) {
    if (aLauncher == null) {
      throw new IllegalArgumentException("The launcher passed in is null");
    }

    _theLaunchers.add(aLauncher);
    aLauncher.getLaunchHandler().setMagnet(this);
  }

  /**
   * Removes the launcher passed in from this magnet.
   *
   * @param aLauncher The launcher to remove.
   */
  public void removeLauncher(Launcher aLauncher) {
    if (aLauncher == null) {
      throw new IllegalArgumentException("The launcher passed in is null");
    }

    aLauncher.getLaunchHandler().setMagnet(null);
    _theLaunchers.remove(aLauncher);
  }

  /**
   * Removes all the launchers from this magnet.
   */
  public void clearLaunchers() {
    _theLaunchers.clear();
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
    // Rendering the parameters of the default profile
    try {
      Parameters aParameters = (Parameters) _theParametersByProfile.get(_theNullProfile);
      if (aParameters != null) {
        aParameters.render(aContext);
      }
    } catch (RenderingException re) {
      throw new RenderingException("Unable to render the global properties", re);
    }

    // Rendering the parameters of the profile passed in
    try {
      Parameters aParameters = (Parameters) _theParametersByProfile.get(aContext.getProfile());
      if (aParameters != null) {
        aParameters.render(aContext);
      }
    } catch (RenderingException re) {
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to render the properties of the profile ").
              append(aContext.getProfile());
      throw new RenderingException(aBuffer.toString(), re);
    }

    // Resolving the attributes of this magnet
    _theName = resolveValue(aContext, _theName);

    // Render the handler definitions
    try {
      super.renderHandlerDefs(aContext);
    } catch (RenderingException re) {
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to render the handler defs of the magnet ").
              append(_theName);
      throw new RenderingException(aBuffer.toString(), re);
    }

    // Render the script of the profile passed in or, if not found, of the default profile
    try {
      Script aScript = (Script) _theScriptsByProfile.get(aContext.getProfile());
      if (aScript != null) {
        aScript.render(aContext);
      } else {
        aScript = (Script) _theScriptsByProfile.get(_theNullProfile);
        if (aScript != null) {
          aScript.render(aContext);
        }
      }
    } catch (RenderingException re) {
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to render the script of the magnet ").
              append(_theName);
      throw new RenderingException(aBuffer.toString(), re);
    }

    // Render all the objects handled by this magnet
    try {
      super.render(aContext);
    } catch (RenderingException re) {
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to render an object of the magnet ").
              append(_theName);
      throw new RenderingException(aBuffer.toString(), re);
    }

    // Render the launchers of this magnet
    Launcher aLauncher = null;
    try {
      for (Iterator<Launcher> it = _theLaunchers.iterator(); it.hasNext(); ) {
        aLauncher = it.next();
        aLauncher.render(aContext);
      }
    } catch (RenderingException re) {
      StringBuffer aBuffer = new StringBuffer("Unable to render the launcher ");
      if (aLauncher != null) {
        aBuffer.append(aLauncher.getLaunchHandler().getName());
      }
      throw new RenderingException(aBuffer.toString(), re);
    }
  }

  /**
   * Returns a string representation of this magnet.
   *
   * @return A string representation of this magnet.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[name=").append(_theName).
            append(" description=").append(_theDescription).
            append(" extends=").append(_theExtends).
            append(" parents=").append(_theParents).
            append(" scripts=").append(_theScriptsByProfile.values()).
            append(" parameters=").append(_theParametersByProfile.values()).
            append(" launchers=").append(_theLaunchers).
            append("]");

    return aBuffer.toString();
  }
}
