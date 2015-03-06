package org.sapia.magnet.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class Parameters extends AbstractRenderable {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The profile name associated with this parameters. */
  private String _theProfile;

  /** The list of params of this parameters. */
  private List<Param> _theParams;

  /** The map of params identified with the name of each param. */
  private Map<String, Param> _theParamsByName;
  
  private int order;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////
  
  /**
   * Creates a new Parameters instance.
   */
  public Parameters() {
    _theParams = new ArrayList<Param>();
    _theParamsByName =  new HashMap<String, Param>();
  }
  
  /**
   * Creates a new Parameters instance with the argument passed in.
   *
   * @param aProfile The profile name of this parameters.
   */
  public Parameters(String aProfile) {
    _theParams = new ArrayList<Param>();
    _theParamsByName =  new HashMap<String, Param>();
    _theProfile = aProfile;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the profile name of this parameters.
   *
   * @return The profile name of this parameters.
   */
  public String getProfile() {
    return _theProfile;
  }

  /**
   * Returns the collection of param objects.
   *
   * @return The collection of param objects.
   */
  public Collection<Param> getParams() {
    return _theParams;
  }

  /**
   * Return the parameter identified by the name passed in.
   *
   * @param aName The name of the parameter to retrieve.
   * @return The parameter identified by the name or null if no parameter is
   *         found with the name passed in.
   * @exception IllegalArgumentException If the name passed in is null.
   */
  public Param getParam(String aName) {
    if (aName == null) {
      throw new IllegalArgumentException("The name passed in is null");
    }

    return _theParamsByName.get(aName);
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the profile name of this parameters.
   *
   * @param aProfile The new profile name.
   */
  public void setProfile(String aProfile) {
    _theProfile = aProfile;
  }

  /**
   * Adds the parameter passed to this parameters collection.
   *
   * @param aParam The parameter to add.
   * @exception IllegalArgumentException If the paramter passed in or the
   *            name of the parameter is null.
   */
  public void addParam(Param aParam) {
    if (aParam == null) {
      throw new IllegalArgumentException("The parameter passed in is null");
    } else if (aParam.getName() == null) {
      throw new IllegalArgumentException("The name of the parameter passed in is null");
    }

    Param anOldParam = _theParamsByName.get(aParam.getName());
    if (anOldParam != null) {
      _theParams.remove(anOldParam);
    }
    
    _theParams.add(aParam);
    _theParamsByName.put(aParam.getName(), aParam);
  }

  /**
   * Removes the parameter passed in from this parameters.
   *
   * @param aParam The parameter to remove.
   * @exception IllegalArgumentException If the paramter passed in or the
   *            name of the parameter is null.
   */
  public void removeParam(Param aParam) {
    if (aParam == null) {
      throw new IllegalArgumentException("The parameter passed in is null");
    } else if (aParam.getName() == null) {
      throw new IllegalArgumentException("The name of the parameter passed in is null");
    }

    _theParams.remove(aParam);
    _theParamsByName.remove(aParam.getName());
  }

  /**
   * Removes all the parameter from this parameters.
   */
  public void clearParams() {
    _theParams.clear();
    _theParamsByName.clear();
  }

  public int getOrder() {
    return order;
  }

  void setOrder(int order) {
    this.order = order;
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
    if (_theProfile == null ||
        (aContext.getProfile() != null && _theProfile.equals(aContext.getProfile()))) {

      for (Param param: _theParams) {

        // Render the parameter
        param.render(aContext);
      
        // Validate if we use the param or not
        if ((param.getIf() == null || aContext.getValue(param.getIf()) != null) &&
            (param.getUnless() == null || aContext.getValue(param.getUnless()) == null)) {
          
          // Add the param in the right context
          if (param.getScope().equals(Param.SCOPE_MAGNET)) {
            aContext.addParameter(param, (_theProfile != null));
            
          } else if (param.getScope().equals(Param.SCOPE_SYSTEM)) {
            System.setProperty(param.getName(), param.getValue());
            
          } else {
            StringBuffer aBuffer = new StringBuffer();
            aBuffer.append("The param ").append(param.getName()).
                    append(" has an invalid scope: ").append(param.getScope()).
                    append(". The scope must either be ").append(Param.SCOPE_MAGNET).
                    append(" or ").append(Param.SCOPE_SYSTEM);
            throw new RenderingException(aBuffer.toString());
          }
        }
      }
    }
  }

  /**
   * Returns a string representation of this parameters.
   *
   * @return A string representation of this parameters.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[profile=").append(_theProfile).
            append(" params=").append(_theParams).
            append("]");

    return aBuffer.toString();
  }
}
