package org.sapia.magnet.domain;

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
public class Extend {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The magnet file of this extend. */
  private String _theMagnetFile;

  /** The path element of this extend. */
  private Path _thePathElement;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Extend instance.
   */
  public Extend() {
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the magnet file of this extend.
   *
   * @return The magnet file of this extend.
   */
  public String getMagnetFile() {
    return _theMagnetFile;
  }

  /**
   * Returns the path element of this extend.
   *
   * @return The path element of this extend.
   */
  public Path getPath() {
    return _thePathElement;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  MUTATOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Changes the magnet file of this extend to the value passed in.
   *
   * @param aMagnetFile The new magnet file.
   */
  public void setMagnetFile(String aMagnetFile) {
    _theMagnetFile = aMagnetFile;
  }

  /**
   * Changes the path element of this extend to the one passed in.
   *
   * @param aPathElement The new path element.
   */
  public void setPath(Path aPathElement) {
    _thePathElement = aPathElement;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  OVERRIDEN METHODS  //////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns a string representation of thi extend.
   *
   * @return A string representation of thi extend.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[magnetFile=").append(_theMagnetFile).
            append("[path=").append(_thePathElement).
            append("]");

    return aBuffer.toString();
  }
}
