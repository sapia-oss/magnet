package org.sapia.magnet.domain.java;

// Import of Sun's JDK classes
// ---------------------------
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.sapia.magnet.Log;


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
public class JavaTask implements Runnable {

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The main class of this java task. */
  private Class _theMainClass;

  /** The arguments of this java task. */
  private String[] _theArguments;

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new JavaTask instance with the passed in arguments.
   */
  public JavaTask(Class aMainClas, String[] someArguments) {
    _theMainClass = aMainClas;
    _theArguments = someArguments;
  }

  /**
   * Run method of the Runnable interface that calls the main method of the main class.
   */
  public void run() {
    try {
      Method aMainMethod = _theMainClass.getDeclaredMethod("main", new Class[] { String[].class });
      aMainMethod.invoke(_theMainClass, new Object[] { _theArguments });
      
    } catch (NoSuchMethodException nsme) {
      String message = "Error running java task: main method not found on class " + _theMainClass.getName();
      Log.error(message, nsme);
      throw new RuntimeException(message + " ==> " + nsme, nsme);
      
    } catch (InvocationTargetException ite) {
      String message = "Error running java task: error calling main method on class " + _theMainClass.getName();
      Log.error(message, ite);
      throw new RuntimeException(message + " ==> " + ite, ite);
      
    } catch (IllegalAccessException iae) {
      String message = "Error running java task: main method was found but is not accessible on class " + _theMainClass.getName();
      Log.error(message, iae);
      throw new RuntimeException(message + " ==> " + iae, iae);
    }
  }
}

