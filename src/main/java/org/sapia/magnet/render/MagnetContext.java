package org.sapia.magnet.render;

// Import of Sun's JDK classes
// ---------------------------
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// Import of Sapia's utility classes
// ---------------------------------
import org.sapia.util.text.TemplateContextIF;

// Import of Sapia's magnet classes
// ---------------------------------
import org.sapia.magnet.domain.Param;


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
public class MagnetContext implements TemplateContextIF {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The profile name of this context. */
  private String _theProfile;

  /** The parent context of this context. */
  private MagnetContext _theParent;

  /** The map of parameters of this context associated with theyre param name. */
  private Map _theParameters;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new MagnetContext with the argument passed in.
   *
   * @param aProfile The profile name of this context.
   */
  public MagnetContext(String aProfile) {
    _theProfile = aProfile;
    _theParameters = new HashMap();
  }

  /**
   * Creates a new MagnetContext with the argument passed in.
   *
   * @param aParent The parent contex of this context.
   */
  public MagnetContext(MagnetContext aParent) {
    _theParent = aParent;
    _theProfile = aParent.getProfile();
    _theParameters = new HashMap();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the parent context of this magnet context.
   *
   * @return The parent context of this magnet context.
   */
  public MagnetContext getParent() {
    return _theParent;
  }

  /**
   * Returns the profile name of this magnet context.
   *
   * @return The profile name of this magnet context.
   */
  public String getProfile() {
    return _theProfile;
  }

  /**
   * Returns the collection of param of this context.
   *
   * @return The collection of <CODE>Param</CODE> objects.
   * @see org.sapia.magnet.domain.Param
   */
  public Collection getParameters() {
    return _theParameters.values();
  }

  /**
   * Returns the param associated with the param name passed in.
   *
   * @param aName The name of the param to retrieve.
   * @return The param object found or null if it's no found.
   * @exception IllegalArgumentException If the name passed in is null.
   */
  public Param getParameterFor(String aName) {
    if (aName == null) {
      throw new IllegalArgumentException("The name passed in is null");
    }

    Param aParam = (Param) _theParameters.get(aName);
    if (aParam == null && _theParent != null) {
      aParam = _theParent.getParameterFor(aName);
    }

    return aParam;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the profile name of this context.
   *
   * @param aProfile The new profile name.
   */
  public void setProfile(String aProfile) {
    _theProfile = aProfile;
  }

  /**
   * Adds the parameter passed to this magnet context.
   *
   * @param aParameter The param to add.
   * @param anOverwriteFlag Indicates if the the passed in parameter should overwrite
   *        the value of a parameter of the same name present in this context.
   * @exception IllegalArgumentException If the paramter passed in or the
   *            name of the parameter is null.
   */
  public void addParameter(Param aParameter, boolean anOverwriteFlag) {
    if (aParameter == null) {
      throw new IllegalArgumentException("The parameter passed in is null");
    } else if (aParameter.getName() == null) {
      throw new IllegalArgumentException("The name of the parameter passed in is null");
    }

    if (anOverwriteFlag == true) {
      _theParameters.put(aParameter.getName(), aParameter);
    } else if (!_theParameters.containsKey(aParameter.getName())) {
      _theParameters.put(aParameter.getName(), aParameter);
    }
  }

  /**
   * Removes the param passed in from this magnet context.
   *
   * @param aParameter The parameter to remove.
   * @exception IllegalArgumentException If the paramter passed in or the
   *            name of the parameter is null.
   */
  public void removeParameter(Param aParameter) {
    if (aParameter == null) {
      throw new IllegalArgumentException("The parameter passed in is null");
    } else if (aParameter.getName() == null) {
      throw new IllegalArgumentException("The name of the parameter passed in is null");
    }

    _theParameters.remove(aParameter.getName());
  }

  /**
   * Removes all the parameter from this magnet context.
   */
  public void clearParameters() {
    _theParameters.clear();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  OVERRIDEN METHODS  //////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns a string representation of this magnet context.
   *
   * @return A string representation of this magnet context.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[profile=").append(_theProfile).
            append(" param=").append(_theParameters).
            append(" parent=").append(_theParent).
            append("]");

    return aBuffer.toString();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////  INTERACE IMPLEMENTATION  ///////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the value of this context for the property name passed in.
   *
   * @param aName The name of the property.
   * @return The value of this context for the property name passed in.
   */
  public Object getValue(String aName) {
    Param aParameter = getParameterFor(aName);

    if (aParameter == null) {
      return System.getProperty(aName);
    } else {
      return aParameter.getValue();
    }
  }

  /**
   * Puts the given value in this context; if one already exists
   * under the given name, it is overwritten.
   *
   * @param name the name under which to map the given value.
   * @param value an <code>Object</code>.
   */
  public void put(String name, Object value) {
    if (!(value instanceof Param)) {
      throw new IllegalArgumentException("The object to put in this magnet context is not a Param instance");
    } else if (name == null || !name.equals(((Param) value).getName())) {
      throw new IllegalArgumentException("The name of the value to add [" + name + "] is invalid");
    }

    addParameter((Param) value, true);
  }
}
