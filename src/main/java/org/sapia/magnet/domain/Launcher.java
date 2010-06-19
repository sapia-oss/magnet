package org.sapia.magnet.domain;

import org.sapia.magnet.render.AbstractRenderable;
import org.sapia.magnet.render.MagnetContext;
import org.sapia.magnet.render.RenderingException;
import org.sapia.util.xml.confix.ObjectWrapperIF;


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
public class Launcher extends AbstractRenderable implements ObjectWrapperIF {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The type of this launcher. */
  private String _theType;

  /** The wrapped object of this launcher. */
  private LaunchHandlerIF _theLaunchHandler;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Launcher instance.
   */
  public Launcher() {
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the type of this launcher.
   *
   * @return The type of this launcher.
   */
  public String getType() {
    return _theType;
  }

  /**
   * Returns the wrapped launcher of this launcher.
   *
   * @return The wrapped launcher of this launcher.
   */
  public LaunchHandlerIF getLaunchHandler() {
    if (_theLaunchHandler == null) {
      throw new IllegalStateException("The laucher type is not define");
    }

    return _theLaunchHandler;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the type of this launcher for the value passed in.
   *
   * @param aType The new type.
   * @exception IllegalArgumentException If the launcher type passed in
   *            is not recognized by the launcher.
   */
  public void setType(String aType) {
    try {
      _theType = aType;
      _theLaunchHandler = HandlerFactory.getInstance().createLaunchHandler(aType);
      _theLaunchHandler.setType(aType);
    } catch (ObjectCreationException oce) {
      System.out.println("***************************************************************");
      throw new IllegalArgumentException("Unable to assign the launcher type " + aType , oce);

    } catch (Exception e){
      System.out.println("***************************************************************");
      e.printStackTrace();
      System.out.println("***************************************************************");

      throw new IllegalStateException(e);
    } catch(ExceptionInInitializerError err){
      err.printStackTrace();
      throw err;
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  HELPER METHODS  ////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Executes this launch handler for the passed in profile.
   *
   * @param aProfile The profile to execute.
   */
  public void execute(String aProfile) {
    _theLaunchHandler.execute(aProfile);
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
    if (_theLaunchHandler == null) {
      throw new RenderingException("Unable to render the laucher - the laucher type is not define");
    }
    
    _theLaunchHandler.render(aContext);
  }

  /**
   * Returns a string representation of this launcher.
   *
   * @return A string representation of this launcher.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[type=").append(_theType).
            append(" launchHandler=").append(_theLaunchHandler).
            append("]");

    return aBuffer.toString();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////  INTERACE IMPLEMENTATION  ///////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the object encapsulated by this wrapper.
   *
   * @return The object encapsulated by this wrapper.
   */
  public Object getWrappedObject(){
    if (_theLaunchHandler == null) {
      return this;
    } else {
      return _theLaunchHandler;
    }
  }
}
