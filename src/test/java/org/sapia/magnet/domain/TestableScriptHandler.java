package org.sapia.magnet.domain;

import org.sapia.magnet.MagnetException;

public class TestableScriptHandler implements ScriptHandlerIF {

  public static boolean isThrowingExceptionOnExecution;

  private boolean _isExecuted;
  
  /**
   * Creates a new {@link TestableScriptHandler} instance.
   */
  public TestableScriptHandler() {
    _isExecuted = false;
  }
  
  /**
   * Returns the isExecuted attribute.
   *
   * @return The isExecuted value.
   */
  public boolean isExecuted() {
    return _isExecuted;
  }

  /* (non-Javadoc)
   * @see org.sapia.magnet.domain.ScriptHandlerIF#execute(java.lang.String)
   */
  public void execute(String aCode) throws MagnetException {
    _isExecuted = true;
    if (isThrowingExceptionOnExecution) {
      throw new MagnetException("Configured exception");
    }
  }

}
