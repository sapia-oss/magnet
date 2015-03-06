package org.sapia.magnet;

import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.sapia.magnet.domain.LaunchHandlerIF;
import org.sapia.util.CompositeException;
import org.sapia.util.CompositeRuntimeException;
import org.xml.sax.SAXException;


/**
 * This class act as the central hub for logging application messages. All the messages
 * the an end user should see on the console should pass by this <code>Log</code> class
 * instead of using the traditionnal <code>System.out</code> and <code>System.err</code>. 
 * 
 * @author Jean-Cedric Desrochers
 * <dl>
 * <dt><b>Copyright:</b><dd>Copyright &#169; 2002-2003 <a href="http://www.sapia-oss.org">Sapia Open Source Software</a>.
 *        All Rights Reserved.</dd></dt>
 * <dt><b>License:</b><dd>Read the license.txt file of the jar or visit the
 *        <a href="http://www.sapia-oss.org/license.html">license page</a> at the Sapia OSS web site</dd></dt>
 * </dl>
 */
public class Log {

  /** Defines the logger instance for this class. */
  private static Logger _theLogger;
  
  static {
    Layout aLayout = new PatternLayout("%m%n");
    Appender anAppender = new ConsoleAppender(aLayout, ConsoleAppender.SYSTEM_OUT);

    _theLogger = Logger.getLogger("magnet");
    _theLogger.setAdditivity(false);
    _theLogger.setLevel(Level.INFO);
    _theLogger.addAppender(anAppender);
  }
  
  /**
   * Logs the message passed in as an information message.
   * 
   * @param aMessage The information message to log. 
   */
  public static void info(String aMessage) {
    _theLogger.info(aMessage);
  }

  /**
   * Logs the message passed in as an information message.
   * 
   * @param aMessage The information message to log.
   * @param aLauncher The launcher handler that log the message. 
   */
  public static void info(String aMessage, LaunchHandlerIF aLauncher) {
    StringBuffer aBuffer = new StringBuffer();
    aBuffer.append("[").append(aLauncher.getName()).
            append("::").append(aLauncher.getType()).
            append("] ").append(aMessage);
    _theLogger.info(aBuffer.toString());
  }

  /**
   * Logs the message passed in as a warning message.
   * 
   * @param aMessage The warning message to log. 
   */
  public static void warn(String aMessage) {
    _theLogger.info("WARNING: " + aMessage);
  }

  /**
   * Logs the message passed in as a warning message.
   * 
   * @param aMessage The warning message to log.
   * @param aLauncher The launcher handler that log the message. 
   */
  public static void warn(String aMessage, LaunchHandlerIF aLauncher) {
    StringBuffer aBuffer = new StringBuffer();
    aBuffer.append("[").append(aLauncher.getName()).
            append("::").append(aLauncher.getType()).
            append("] WARNING: ").append(aMessage);
    _theLogger.info(aBuffer.toString());
  }

  /**
   * Logs the message passed in as an error message.
   * 
   * @param aMessage The error message to log. 
   */
  public static void error(String aMessage) {
    _theLogger.info("ERROR: " + aMessage);
  }
  
  /**
   * Logs the message passed in as an error message.
   * 
   * @param aMessage The error message to log.
   * @param aLauncher The launcher handler that log the message. 
   */
  public static void error(String aMessage, LaunchHandlerIF aLauncher) {
    StringBuffer aBuffer = new StringBuffer();
    aBuffer.append("[").append(aLauncher.getName()).
            append("::").append(aLauncher.getType()).
            append("] ERROR: ").append(aMessage);
    _theLogger.info(aBuffer.toString());
  }
  
  /**
   * Logs the message passed in as an error message.
   * 
   * @param aMessage The error message to log.
   * @param aCause The exception that caused the error.
   */
  public static void error(String aMessage, Throwable aCause) {
    _theLogger.info("ERROR: " + aMessage, aCause);
  }

  /**
   * Extracts the messages from the exception passed in.
   * 
   * @param anException The exception to format.
   * @return The formatted string. 
   */
  public static String extactMessage(Throwable anException) {
    StringBuffer aMessage = new StringBuffer();
    
    int level = 0;
    while (anException != null) {
      level++;
      // Format the message
      if (level == 1) {
        aMessage.append(anException.getMessage());
      } else {
        aMessage.append("\n\t---> ").append(anException.getMessage());
      }

      if (anException instanceof CompositeException) {
        anException = ((CompositeException) anException).getSourceError();
      } else if (anException instanceof CompositeRuntimeException) {
        anException = ((CompositeRuntimeException) anException).getSourceError();
      } else if (anException instanceof SAXException) {
        anException = ((SAXException) anException).getException();
      } else {
        anException = null;
      }
    }
    
    return aMessage.toString();
  }
  
  /**
   * Utility method that formats the properties passed into a String format. 
   * 
   * @param someProperties The properties to format.
   * @return The String format of the properties.
   */
  public static String formatProperties(Properties someProperties) {
    StringBuffer aBuffer = new StringBuffer("\n ---------- listing properties ----------\n");
                    
    for (Iterator<Object> it = new TreeSet<Object>(someProperties.keySet()).iterator(); it.hasNext(); ) {
      String aName = (String) it.next();
      String aValue = someProperties.getProperty(aName);
      aBuffer.append(aName).append("=").append(aName.indexOf("password") >= 0 ? "***********" : aValue).append("\n");
    }
    
    return aBuffer.toString();
  }
}
