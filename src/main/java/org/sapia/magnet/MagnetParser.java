package org.sapia.magnet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.sapia.magnet.domain.Magnet;
import org.sapia.util.xml.ProcessingException;
import org.sapia.util.xml.confix.CompositeObjectFactory;
import org.sapia.util.xml.confix.ConfixProcessorFactory;
import org.sapia.util.xml.confix.ConfixProcessorIF;
import org.sapia.util.xml.confix.ReflectionFactory;


/**
 *
 *
 * @author Jean-Cedric Desrochers
 * <dl>
 * <dt><b>Copyright:</b><dd>Copyright &#169; 2002-2003 <a href="http://www.sapia-oss.org">Sapia Open Source Software</a>. All Rights Reserved.</dd></dt>
 * <dt><b>License:</b><dd>Read the license.txt file of the jar or visit the
 *        <a href="http://www.sapia-oss.org/license.html" target="sapia-license">license page</a> at the Sapia OSS web site</dd></dt>
 * </dl>
 */
public class MagnetParser {

  /////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////  CLASS ATTRIBUTES  ///////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the logger instance for this class. */
  private static final Logger _theLogger = Logger.getLogger(MagnetParser.class);

  /////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////  CONSTRUCTORS  /////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new MagnetParser instance.
   */
  public MagnetParser() {
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////  HELPER METHODS  ////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Parses the input stream passed in and returns a list of magnet object. The list
   * represents the magnet hierarchy with the first element of the list beign the
   * root magnet of the hierarchy.
   *
   * @param anInput The input stream of the magnet definition.
   * @return The list of magnet object created
   * @exception MagnetException If an error occurs while parsing the input stream.
   */
  public List<Magnet> parse(InputStream anInput) throws MagnetException {
    LinkedList<Magnet> someMagnets = new LinkedList<Magnet>();

    // Create the Confix object factory
    ReflectionFactory aFactory = new ReflectionFactory(
            new String[] { "org.sapia.magnet.domain",
                           "org.sapia.magnet.domain.system",
                           "org.sapia.magnet.domain.java" });

    CompositeObjectFactory aCompositeFactory = new CompositeObjectFactory();
    aCompositeFactory.registerFactory(MagnetDictionary.NAMESPACE_URI_MAGNET, aFactory);

    // Create the Confix processor
    ConfixProcessorFactory aProcessorFactory = ConfixProcessorFactory.newFactory();
    ConfixProcessorIF aProcessor = aProcessorFactory.createProcessor(aCompositeFactory);

    // Recursively parse the input stream
    try {
      parseMagnetIter(anInput, aProcessor, someMagnets, new ArrayList<String>());
    } catch (CircularReferenceException cre) {
    }

    return someMagnets;
  }

  /**
   * Iter recursive method that parses the input stream with the processor and adds
   * the magnet object at the begining of the linked list. If the result magnet as
   * an extend resource define, the method will call itself with an input stream over
   * the resourcce define by the <CODE>getExtends()</CODE> method of the magnet.
   *
   * @param anInput The input stream over the magnet to parse.
   * @param aProcessor The Confix processor to use to parse the stream.
   * @param someMagnets The list of magnet into which the result magnet will be added.
   * @param magnetStack The recursive stack of magnet that are currently parsed.  
   * @return The magnet object created by the parsing operation of the stream.
   * @exception MagnetException If an error occurs while parsing the magnet.
   */
  private Magnet parseMagnetIter(InputStream anInput, ConfixProcessorIF aProcessor, 
          LinkedList<Magnet> someMagnets, List<String> magnetStack) throws MagnetException, CircularReferenceException {
    Magnet aMagnet = null;
    
    try {
      // Process the input stream to create the magnet object
      aMagnet = (Magnet) aProcessor.process(anInput);
      
      // Verify circular references
      if (magnetStack.contains(aMagnet.getName())) {
        throw new CircularReferenceException(aMagnet);
      } else {
        magnetStack.add(aMagnet.getName());
      }

      // Insert the current magnet at the beginnin of the list
      someMagnets.addFirst(aMagnet);

      // Parse the extended magnets of this magnet
      if (aMagnet.getExtends() != null) {
        String[] magnetNames = split(aMagnet.getExtends(), ',', true);

        // Going backwards in the list to preserve the ordering of the magnets
        for (int i = magnetNames.length-1; i >= 0; i--) {
          InputStream aParentInput = getResourceAsStream(magnetNames[i]);
          Magnet aParentMagnet = parseMagnetIter(aParentInput, aProcessor, someMagnets, magnetStack);
          aMagnet.insertParent(aParentMagnet);
        }
      }
      
      magnetStack.remove(aMagnet.getName());
      return aMagnet;
      
    } catch (CircularReferenceException cre) {
      String aMessage = "Circular reference in the magnet hierarchy - caused by the the magnet " + aMagnet.getName() +
                        " that extends the magnet " + cre.getMagnet().getName();
      _theLogger.error(aMessage, cre);
      throw new MagnetException(aMessage, cre);      
      
    } catch (ProcessingException pe) {
      String aMessage = "Unable to parse the magnet configuration";
      _theLogger.error(aMessage, pe);
      throw new MagnetException(aMessage, pe);

    } catch (RuntimeException re) {
      String aMessage = "System error parsing the magnet configuration";
      _theLogger.error(aMessage, re);
      throw new MagnetException(aMessage, re);
    }
  }

  /**
   * Utility method that resolve the resource name passed in and returns
   * an input stream over the resource.
   *
   * @param aResourceName The name of the resource to find.
   * @return An input stream over the resource.
   * @exception MagnetException If no resource if found for the name.
   */
  private InputStream getResourceAsStream(String aResourceName) throws MagnetException {
    InputStream anInput = null;

    try {
      // Look if its an URL
      if (aResourceName.startsWith("file:/") || aResourceName.startsWith("http:/")) {
        URL anURL = new URL(aResourceName);
        anInput = anURL.openStream();
      } else {
        // Otherwise look if its an existing file
        File aFile = new File(aResourceName);
        if (aFile.exists() && aFile.isFile()) {
          anInput = new FileInputStream(aFile);
        } else {
          // Finally look if its a file in the working directory
          aFile = new File(System.getProperty("user.dir"), aResourceName);
          if (aFile.exists() && aFile.isFile()) {
            anInput = new FileInputStream(aFile);
          } else {
            throw new MagnetException("Unable to find the resource " + aResourceName);
          }
        }
      }

      return anInput;

    } catch (MalformedURLException mue) {
      throw new MagnetException("Unable to find the resource " + aResourceName, mue);

    } catch (IOException ioe) {
      throw new MagnetException("Unable to find the resource " + aResourceName, ioe);
    }
  }
  
  /*
   * 
   */
	private String[] split(String toSplit, char splitChar, boolean trim) {
		List<String> tokens = new ArrayList<String>();

		StringBuffer token = new StringBuffer();

		for (int i = 0; i < toSplit.length(); i++) {
			if (toSplit.charAt(i) == splitChar) {
				if (trim) {
					tokens.add(token.toString().trim());      		
				} else {
					tokens.add(token.toString());
				}
				
				token.delete(0, token.length());
				
			} else {
				token.append(toSplit.charAt(i));
			}
		}

		if (token.length() > 0) {
			if (trim) {
				tokens.add(token.toString().trim());
			} else {
				tokens.add(token.toString());
			}
		}

		return tokens.toArray(new String[tokens.size()]);
	}    
  
  public class CircularReferenceException extends Exception {
    private Magnet _theMagnet;
    public CircularReferenceException(Magnet aMagnet) {
      super();
      _theMagnet = aMagnet;
    }
    public Magnet getMagnet() {
      return _theMagnet;
    }
  }
}
