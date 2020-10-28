// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI;
  String id; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String id, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.id = id;
    try { openClientConnection(); }
    catch (IOException ioe) { }//closeConnection(); }

  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Cannot open connection. Awaiting command.");
      //quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    
    System.exit(0);
  }

  /**
   * Hook method called after the connection has been closed.
   */
  protected void connectionClosed() 
  {
    clientUI.display
    ("SERVER SHUTTING DOWN! DISCONNECTING!\n" + "Abnormal termination of connection.");
  }

  /**
   * Hook method called each time an exception
   * is raised by the client listening thread.
   *
   * @param exception the exception raised.
   */
  protected void connectionException(Exception exception) 
  {
    clientUI.display
    ("WARNING - The server has stopped listening for connections.");
    try { 
      closeConnection(); 
    }
    catch (IOException ioe) {}
    //quit();
  }

  public void openClientConnection() throws IOException {
    String text;
    openConnection();
    clientUI.display
    (id + " has logged on.");

    text = "A new client is attempting to connect to the server.\n"
    + "Message received: #login " + id + " from null.\n";

    handleMessageFromClientUI(text + "#login <" + id + ">");


  }
}
//End of ChatClient class
