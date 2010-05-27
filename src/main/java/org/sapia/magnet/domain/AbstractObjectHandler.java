package org.sapia.magnet.domain;

// Import of Sun's JDK classes
// ---------------------------
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// Import of Sapia's utility classes
// ---------------------------------
import org.sapia.util.xml.confix.ObjectHandlerIF;

// Import of Sapia's Corus classes
// --------------------------------
import org.sapia.magnet.render.AbstractRenderable;
import org.sapia.magnet.render.MagnetContext;
import org.sapia.magnet.render.RenderableIF;
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
public abstract class AbstractObjectHandler extends AbstractRenderable implements ObjectHandlerIF {

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  CLASS ATTRIBUTES  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines an empty immutable list. */
  private static final List EMPTY_LIST = Collections.unmodifiableList(new ArrayList(0));

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The list of script handler definitions. */
  private List _theScriptHandlerDefs;

  /** The list of protocol handler definitions. */
  private List _theProtocolHandlerDefs;

  /** The list of launch handler definitions. */
  private List _theLaunchHandlerDefs;

  /** The map of objects identified by the element name. */
  private Map _theObjects;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new AbstractObjectHandler instance.
   */
  protected AbstractObjectHandler() {
    _theLaunchHandlerDefs = new ArrayList();
    _theProtocolHandlerDefs = new ArrayList();
    _theScriptHandlerDefs = new ArrayList();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the list of launch handler definition objects.
   *
   * @return The list of <CODE>LaunchHandlerDef</CODE> objects.
   */
  public Collection getLaunchHandlerDefs() {
    return _theLaunchHandlerDefs;
  }

  /**
   * Returns the list of protocol handler definition objects.
   *
   * @return The list of <CODE>ProtocolHandlerDef</CODE> objects.
   */
  public Collection getProtocolHandlerDefs() {
    return _theProtocolHandlerDefs;
  }

  /**
   * Returns the list of script handler definition objects.
   *
   * @return The list of <CODE>ScriptHandlerDef</CODE> objects.
   */
  public Collection getScriptHandlerDefs() {
    return _theScriptHandlerDefs;
  }

  /**
   * Returns a collection of objects associated to the element name passed in.
   * If there's no mapping, it returns an empty collection.
   *
   * @param anElementName The element name for which to retrieve the objects.
   * @return A collection of <CODE>Object</CODE> objects.
   */
  public Collection getObjectsFor(String anElementName) {
    List someObjects = EMPTY_LIST;

    if (_theObjects != null) {
      someObjects = (List) _theObjects.get(anElementName.toLowerCase());

      if (someObjects == null) {
        someObjects = EMPTY_LIST;
      }
    }

    return someObjects;
  }

  /**
   * Returns the collection of element names that represents the keys
   * of the object of this object handler.
   *
   * @return The collection of element name.
   */
  public Collection getElementNames() {
    Collection someNames = EMPTY_LIST;

    if (_theObjects != null) {
      someNames = _theObjects.keySet();
    }

    return someNames;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Adds the launch handler definition to this abstract object handler.
   *
   * @param aHandlerDef The launch handler definition to add.
   */
  public void addLaunchHandlerDef(LaunchHandlerDef aHandlerDef) {
    _theLaunchHandlerDefs.add(aHandlerDef);
  }

  /**
   * Adds the protocol handler definition to this abstract object handler.
   *
   * @param aHandlerDef The protocol handler definition to add.
   */
  public void addProtocolHandlerDef(ProtocolHandlerDef aHandlerDef) {
    _theProtocolHandlerDefs.add(aHandlerDef);
  }

  /**
   * Adds the script handler definition to this abstract object handler.
   *
   * @param aHandlerDef The script handler definition to add.
   */
  public void addScriptHandlerDef(ScriptHandlerDef aHandlerDef) {
    _theScriptHandlerDefs.add(aHandlerDef);
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////  INTERACE IMPLEMENTATION  ///////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Render the handler definitions of this abstract object handler.
   *
   * @param aContext The magnet context to use.
   * @exception RenderingException If an error occurs while rendering the handlers.
   */
  public void renderHandlerDefs(MagnetContext aContext) throws RenderingException {
    // Render the launch handler definitions
    for (Iterator it = _theLaunchHandlerDefs.iterator(); it.hasNext(); ) {
      LaunchHandlerDef aHandlerDef = (LaunchHandlerDef) it.next();
      aHandlerDef.render(aContext);
    }

    // Render the protocol handler definitions
    for (Iterator it = _theProtocolHandlerDefs.iterator(); it.hasNext(); ) {
      ProtocolHandlerDef aHandlerDef = (ProtocolHandlerDef) it.next();
      aHandlerDef.render(aContext);
    }

    // Render the script handler definitions
    for (Iterator it = _theScriptHandlerDefs.iterator(); it.hasNext(); ) {
      ScriptHandlerDef aHandlerDef = (ScriptHandlerDef) it.next();
      aHandlerDef.render(aContext);
    }
  }

  /**
   * Renders this objects to the magnet context passed in.
   *
   * @param aContext The magnet context to use.
   * @exception RenderingException If an error occurs while rendering this object.
   */
  public void render(MagnetContext aContext) throws RenderingException {
    for (Iterator it = getElementNames().iterator(); it.hasNext(); ) {
      String anElementName = (String) it.next();

      for (Iterator someObjects = getObjectsFor(anElementName).iterator(); someObjects.hasNext(); ) {
        Object anObject = someObjects.next();

        if (anObject instanceof RenderableIF) {
          ((RenderableIF) anObject).render(aContext);
        }
      }
    }
  }

  /**
   * Handles the passed in object that was created for the element name passed in.
   *
   * @param anElementName The xml element name for which the object was created.
   * @param anObject The object to handle.
   */
  public void handleObject(String anElementName, Object anObject) {
    // Lazy creation of the map
    if (_theObjects == null) {
      _theObjects = new HashMap();
    }

    // Retrieve the list associated with the element name
    List someObjects = (List) _theObjects.get(anElementName.toLowerCase());
    if (someObjects == null) {
      someObjects = new ArrayList();
      _theObjects.put(anElementName.toLowerCase(), someObjects);
    }

    // Add the object to its list
    someObjects.add(anObject);
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  OVERRIDEN METHODS  //////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns a string representation of this magnet.
   *
   * @return A string representation of this magnet.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[launchHandlerDefs=").append(_theLaunchHandlerDefs).
            append(" protocolHandlerDefs=").append(_theProtocolHandlerDefs).
            append(" scriptHandlerDefs=").append(_theScriptHandlerDefs).
            append(" objects=").append(_theObjects).
            append("]");

    return aBuffer.toString();
  }
}
