package org.sapia.magnet.domain;

import java.io.InputStreamReader;

import org.sapia.magnet.MagnetException;

import bsh.EvalError;
import bsh.Interpreter;


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
public class BeanShellHandler implements ScriptHandlerIF {

  /**
   *
   */
  public void execute(String aCode) throws MagnetException {
    try {
      Interpreter anInterpreter = new Interpreter(new InputStreamReader(System.in), System.out, System.err, false);
      anInterpreter.eval(aCode);
    } catch (EvalError ee) {
      throw new MagnetException("Error executing the bsh script", ee);
    }
  }
}
