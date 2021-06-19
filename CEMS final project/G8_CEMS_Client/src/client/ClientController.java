// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package client;
import java.io.IOException;
import java.util.function.Consumer;

import common.CemsIF;


/**
 * This class constructs the UI for a cems client.  It implements the
 * cems interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author CEMS_TEAM
 */
@SuppressWarnings("unused")
public class ClientController implements CemsIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
   public static int DEFAULT_PORT ;
   
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleCems.
   */
  CEMSClient client;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientController(String host, int port) 
  {
    try 
    {
      client= new CEMSClient(host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"+ " Terminating client.");
      System.exit(1);
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   * 
   * @param str to send to server
   */
  
  public void accept(String str) 
  {
	  client.handleMessageFromClientUI(str);
  }
  
  //prototype: we sand string in order to check if exist.
  public void accept(Object obj) 
  {
	  client.handleMessageFromClientUI(obj);
  }
  
  /**
   * This method overrides the method in the CemsIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
	  // call loadTable from tableController
    System.out.println("> " + message);
  }
  
}
//End of ConsoleCems class
