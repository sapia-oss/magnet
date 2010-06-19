package org.sapia.magnet.domain;

import java.util.Collection;

import org.sapia.magnet.render.RenderableIF;


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
public interface LaunchHandlerIF extends RenderableIF {

  /**
   * Returns the type of this launcher.
   *
   * @return The type of this launcher.
   */
  public String getType();


  /**
   * Returns the name of this launcher.
   *
   * @return The name of this launcher.
   */
  public String getName();


  /**
   * Returns the default profile name of this launcher.
   *
   * @return The default profile name of this launcher.
   */
  public String getDefault();


  /**
   * Returns the number of milliseoncds to wait after executing this launcher.
   * 
   * @return the number of milliseoncds to wait after executing this launcher.
   */
  public int geWaitTime();


  /**
   * Returns the parent magnet of this launch handler.
   *
   * @return The parent magnet of this launch handler.
   */
  public Magnet getMagnet();


  /**
   * Returns the collection of profiles of this launcher.
   *
   * @return The collection of <CODE>Profile</CODE> objects of this launcher.
   */
  public Collection<Profile> getProfiles();


  /**
   * Returns the profile for the profile name passed in.
   *
   * @param aProfileName The name of the profile.
   * @return The profile for the profile name passed in.
   */
  public Profile getProfile(String aProfileName);


  /**
   * Changes the type of this launcher handler.
   *
   * @param aType The new type.
   */
  public void setType(String aType);


  /**
   * Changes the name of this launcher handler.
   *
   * @param aName The new name.
   */
  public void setName(String aName);


  /**
   * Changes the default profile name of this launcher handler.
   *
   * @param aProfileName The new default profile name.
   */
  public void setDefault(String aProfileName);


  /**
   * Changes the value of the time to wait after executing this launcher.
   * 
   * @param aWaitTime The new wait time value in milliseconds.
   */
  public void setWaitTime(int aWaitTime);


  /**
   * Changes the parent magnet of this launch handler.
   *
   * @param aMagnet the new Magnet.
   */
  public void setMagnet(Magnet aMagnet);


  /**
   * Adds the profile passed in to this launch handler.
   *
   * @param aProfile The profile to add.
   */
  public void addProfile(Profile aProfile);


  /**
   * Executes this launch handler for the passed in profile.
   *
   * @param aProfile The profile to execute.
   */
  public void execute(String aProfile);
}
