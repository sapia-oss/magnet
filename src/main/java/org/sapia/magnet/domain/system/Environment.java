package org.sapia.magnet.domain.system;

// Import of Sun's JDK classes
// ---------------------------
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


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
public class Environment extends AbstractRenderable {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The identifier of this enironment. */
  private String _theId;

  /** The name of the parent of this environment. */
  private String _theParent;

  /** The list of environment variables of this environment . */
  private Map _theVariables;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Environment instance.d
   */
  public Environment() {
    _theVariables = new TreeMap();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the identifier of this environment.
   *
   * @return The identifier of this environment.
   */
  public String getId() {
    return _theId;
  }

  /**
   * Returns the parent name of this environment.
   *
   * @return The parent name of this environment.
   */
  public String getParent() {
    return _theParent;
  }

  /**
   * Returns the collection of variables of this environment.
   *
   * @return The collection of <CODE>Variable</CODE> objects.
   * @see Variable
   */
  public Collection getVariables() {
    return _theVariables.values();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the identifier of this environment.
   *
   * @param anId The new identifier.
   */
  public void setId(String anId) {
    _theId = anId;
  }

  /**
   * Changes the parent name of this environment.
   *
   * @param aParent The new parent name.
   */
  public void setParent(String aParent) {
    _theParent = aParent;
  }

  /**
   * Adds the variable passed in to this environment.
   *
   * @param aVariable The variable to add.
   */
  public void addVariable(Variable aVariable) {
    _theVariables.put(aVariable.getName(), aVariable);
  }

  /**
   * Removes the variable passed in from this environment.
   *
   * @param aVariable The variable to remove.
   */
  public void removeVariable(Variable aVariable) {
    _theVariables.remove(aVariable.getName());
  }

  /**
   * Removes all the variables of this environment.
   */
  public void clearVariables() {
    _theVariables.clear();
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
    // Resolve the attributes
    try {
      _theId = resolveValue(aContext, _theId);
      _theParent = resolveValue(aContext, _theParent);
    } catch (RenderingException re) {
      StringBuffer aBuffer = new StringBuffer();
      aBuffer.append("Unable to resolve the attribute of the environment '").
              append(_theId).append("'");      
      
      throw new RenderingException(aBuffer.toString(), re);
    }

    // Render the path
    Variable aVariable = null;
    try {
      for (Iterator it = _theVariables.values().iterator(); it.hasNext(); ) {
        aVariable = (Variable) it.next();
        aVariable.render(aContext);
      }
    } catch (RenderingException re) {
      StringBuffer aBuffer = new StringBuffer("Unable to render the variable");
      if (aVariable != null) {
        aBuffer.append(" '").append(aVariable.getName()).append("'");
      }
      aBuffer.append(" of the environment '").append(_theId).append("'");      
      
      throw new RenderingException(aBuffer.toString(), re);
    }
  }

  /**
   * Returns a string representation of this classpath.
   *
   * @return A string representation of this classpath.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[id=").append(_theId).
            append(" parent=").append(_theParent).
            append(" variables=").append(_theVariables).
            append("]");

    return aBuffer.toString();
  }
}
