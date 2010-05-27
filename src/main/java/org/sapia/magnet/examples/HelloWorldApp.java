package org.sapia.magnet.examples;

/**
 * This example class shows the args passed in a the messages
 * 
 */
public class HelloWorldApp {

  public static void main(String[] args) {
    System.out.println("HelloWorldApp started...");
    StringBuffer aMessage = new StringBuffer("Message --> ");
    
    for (int i = 0; i < args.length; i++) {
      aMessage.append(args[i]).append(" ");
    }
    
    System.out.println(aMessage.toString());
    
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
    }

    System.out.println("HelloWorldApp finished");
  }
}
