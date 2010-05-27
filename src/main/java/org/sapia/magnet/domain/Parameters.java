package org.sapia.magnet.domain;

// Import of Sun's JDK classes
// ---------------------------
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// Import of Sapia's magnet classes
// --------------------------------
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
  private List _theParams;

  /** The map of params identified with the name of each param. */
  private Map _theParamsByName;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  
  private int order;
  
  /**
   * Creates a new Parameters instance.
   */
  public Parameters() {
    _theParams = new ArrayList();
    _theParamsByName =  new HashMap();
  }
  
  void setOrder(int order) {
    this.order = order;
  }
  
  public int getOrder() {
    return order;
  }

  /**
   * Creates a new Parameters instance with the argument passed in.
   *
   * @param aProfile The profile name of this parameters.
   */
  public Parameters(String aProfile) {
    _theParams = new ArrayList();
    _theParamsByName =  new HashMap();
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
  public Collection getParams() {
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

    return (Param) _theParamsByName.get(aName);
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

    Param anOldParam = (Param) _theParamsByName.get(aParam.getName());
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

      for (Iterator it = _theParams.iterator(); it.hasNext(); ) {
        Param aParam = (Param) it.next();
      
        // Validate if we render the param or not
        if ((aParam.getIfDefine() == null ||
                aContext.getValue(aParam.getIfDefine()) != null) &&
            (aParam.getUnlessDefine() == null ||
                aContext.getValue(aParam.getUnlessDefine()) == null)) {

          // Render the parameter
          aParam.render(aContext);
          
          // Add the param in the right context
          if (aParam.getScope().equals(Param.SCOPE_MAGNET)) {
            aContext.addParameter(aParam, (_theProfile != null));
          } else if (aParam.getScope().equals(Param.SCOPE_SYSTEM)) {
            System.setProperty(aParam.getName(), aParam.getValue());
          } else {
            StringBuffer aBuffer = new StringBuffer();
            aBuffer.append("The param ").append(aParam.getName()).
                    append(" has an invalid scope: ").append(aParam.getScope()).
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
