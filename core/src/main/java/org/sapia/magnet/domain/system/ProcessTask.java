package org.sapia.magnet.domain.system;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.sapia.corus.interop.api.ShutdownListener;
import org.sapia.corus.interop.client.InteropClient;
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
public class ProcessTask implements Runnable {

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  CLASS ATTRIBUTES  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the logger instance for this class. */
  private static final Logger _theLogger = Logger.getLogger(ProcessTask.class);

  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////  INSTANCE ATTRIBUTES  /////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** The commands of this process to execute. */
  private String[] _theCommands;

  /** The environments variables of this process. */
  private String[] _theEnvironmentVariables;

  /** The working directory of this process. */
  private File _theWorkingDirectory;
  
  final private CountDownLatch shutdownLatch = new CountDownLatch(1);

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new ProcessTask instance.
   *
   * @param someCommands The array of commands to execute.
   * @param someVariables The array of environment variables.
   * @param aDirectory The working directory of the process.
   */
  public ProcessTask(String[] someCommands, String[] someVariables, File aDirectory) {
    _theCommands = someCommands;
    _theEnvironmentVariables = someVariables;
    _theWorkingDirectory = aDirectory;
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////  INTERACE IMPLEMENTATION  ///////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Runs this process task.
   */
  public void run() {
     Process aProcess = startProcess();

     if (aProcess != null) {
       monitorProcess(aProcess);
    }
  }
  
  protected Process startProcess() {
    try {
      // Execute this process
      final Process aProcess = Runtime.getRuntime().exec(
              _theCommands, _theEnvironmentVariables, _theWorkingDirectory);

      // Use Corus interop to close the OS process (if available)
      if (InteropClient.getInstance().isDynamic()) {
        InteropClient.getInstance().addShutdownListener(() -> terminateProcess(aProcess));
      } else {
        // Otherwise rely on a JVM shutdown hook
        Runtime.getRuntime().addShutdownHook(
            new Thread(() -> terminateProcess(aProcess), "OsProcess-ShutdownThread"));
      }
      
      return aProcess;
      
    } catch (IOException ioe) {
      String aMessage = "Unable to execute the process";
      _theLogger.error(aMessage, ioe);
      Log.error(aMessage + " - " + Log.extactMessage(ioe));
      return null;
    }
  }
  
  protected void monitorProcess(Process aProcess) {
    // Monitor the streams untill the end of the process
    boolean isTerminated = false;
    while (!isTerminated) {
      try {
        // Sleep for a second
        Thread.sleep(1000);

        // Get the output stream
        StringBuffer aBuffer = new StringBuffer();
        while (aProcess.getInputStream().available() > 0) {
          aBuffer.append((char) aProcess.getInputStream().read());
        }
        if (aBuffer.length() > 0) {
          _theLogger.info("OUTPUT >>> " + aBuffer.toString());
        }

        // Get the error stream
        aBuffer = new StringBuffer();
        while (aProcess.getErrorStream().available() > 0) {
          aBuffer.append((char) aProcess.getErrorStream().read());
        }
        if (aBuffer.length() > 0) {
          _theLogger.error("ERROR >>> " + aBuffer.toString());
        }

      } catch (InterruptedException ie) {
        isTerminated = true;
        _theLogger.warn("INTERRUPTED: Force termination of the process");
        aProcess.destroy();

      } catch (IOException ioe) {
        String aMessage = "Caugh an I/O error while monitoring the running process";
        _theLogger.warn(aMessage, ioe);

      } catch (RuntimeException re) {
        String aMessage = "Caugh a system error while monitoring the running process";
        _theLogger.warn(aMessage, re);
      }
      
      // Check the status of the running process (throws exception if not terminated)
      try {
        int anExitValue = aProcess.exitValue();
        isTerminated = true;
        _theLogger.info("Process terminated with exit value: " + anExitValue);
        Log.info("Process terminated with exit value: " + anExitValue);

      } catch (IllegalThreadStateException itse) {
        // Called aProcess.exitValue() on a running process... continue
      }
    }
    
    shutdownLatch.countDown();
  }
    
  protected void terminateProcess(Process aProcess) {
    try {
      Log.info("Killing OS process...");
      aProcess.destroy();
      
      if (!shutdownLatch.await(5000, TimeUnit.MILLISECONDS)) {
        Log.warn("Couldn't get process kill confirmation after 5 seconds");
      }
    } catch (Exception e) {
      Log.warn("Error trying to kill OS process - " + Log.extactMessage(e));
    }
  }
}
