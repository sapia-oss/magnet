package org.sapia.magnet.domain;

// Import of Sun's JDK classes
// ---------------------------
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Comparator;

/**
 *
 * @author Jean-Cedric Desrochers
 *
 * <dl>
 * <dt><b>Copyright:</b><dd>Copyright &#169; 2002-2003 <a href="http://www.sapia-oss.org">Sapia Open Source Software</a>. All Rights Reserved.</dd></dt>
 * <dt><b>License:</b><dd>Read the license.txt file of the jar or visit the
 *        <a href="http://www.sapia-oss.org/license.html" target="sapia-license">license page</a> at the Sapia OSS web site</dd></dt>
 * </dl>
 */
public class Resource implements Comparable {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The URL of this resource. */
  private String _theURL;

  /** The index of this resource. */
  private int _theIndex;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new Resource instance with the passed in arguments.
   *
   * @param aURL The URL of the new resource.
   * @param anIndex The index of this new resource.
   */
  public Resource(String aURL, int anIndex) {
    _theURL = aURL;
    _theIndex = anIndex;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  ACCESSOR METHODS  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the URL of this resource.
   *
   * @return The URL of this resource.
   */
  public String getURL() {
    return _theURL;
  }

  /**
   * Returns the index of this resource.
   *
   * @return The index of this resource.
   */
  public int getIndex() {
    return _theIndex;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  HELPER METHODS  ////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns this resource as a URL object.
   *
   * @return This resource as a URL object.
   * @exception MalformedURLException If an error occurs while converting
   *           this resource as an URL object.
   */
  public URL toURL() throws MalformedURLException {
    return new URL(_theURL);
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  OVERRIDEN METHODS  //////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Compares the object passed in for equality.
   *
   * @param anObject The object to compare.
   * @return True if the object is a Resource and it has the same URL.
   */
  public boolean equals(Object anObject) {
    if (anObject instanceof Resource) {
      return _theURL.equals(((Resource) anObject)._theURL);
    } else {
      return false;
    }
  }

  /**
   * Returns the hash code value for this resource.
   *
   * @return The hash code value for this resource.
   */
  public int hashCode() {
    return _theURL.hashCode();
  }

  /**
   * Returns a string representation of this resource.
   *
   * @return A string representation of this resource.
   */
  public String toString() {
    StringBuffer aBuffer = new StringBuffer(super.toString());
    aBuffer.append("[url=").append(_theURL).
            append(" index=").append(_theIndex).
            append("]\n");

    return aBuffer.toString();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////  INTERACE IMPLEMENTATION  ///////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.<p>
   *
   * @param anObject The object to be compared.
   * @return A negative integer, zero, or a positive integer as this object
   *		     is less than, equal to, or greater than the specified object.
   * @exception ClassCastException if the specified object's type prevents it
   *            from being compared to this Object.
   */
  public int compareTo(Object anObject) {
    return _theIndex - ((Resource) anObject)._theIndex;
  }


  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  INNER CLASSES  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  public static class AscendingComparator implements Comparator {
    public int compare(Object anObject, Object anotherObject) {
      return ((Resource) anObject)._theURL.compareTo(((Resource) anotherObject)._theURL);
    }
  }

  public static class DescendingComparator implements Comparator {
    public int compare(Object anObject, Object anotherObject) {
      return ((Resource) anObject)._theURL.compareTo(((Resource) anotherObject)._theURL) * -1;
    }
  }
}
