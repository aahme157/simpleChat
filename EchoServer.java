// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    if(msg.toString().contains("#login")){
      if(client.getInfo("id") == null){
        System.out.println(msg);
        client.setInfo("id", msg.toString().split("<")[1].substring(0, msg.toString().split("<")[1].length() - 1)); 
      }
      else{
        try { client.sendToClient("ERROR: #login command should only be used at the begining."); }
        catch(IOException ioe) {}
      }
    }
    else if(client.getInfo("id") == null){
      try { client.sendToClient("ERROR: #login command should be reveiced first."); client.close(); }
      catch(IOException ioe) {}
      
    }
    else{
      System.out.println("Message received: " + msg + " from " + client.getInfo("id"));
      this.sendToAllClients(client.getInfo("id") + ": " + msg);
    }
  }

    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for clients on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

 /**
   * Implementation of AbstractServer hook to print a welcome message
   * when a new client connects to the server.
   */
  @Override
  protected void clientConnected(ConnectionToClient client){
    String info = "A new client just joined the chat!";
    this.sendToAllClients(info);
  }

  /**
   * Implementation of AbstractServer hook to print a message when
   * a client gets disconnected from the server.
   */
  @Override
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
      String info = "<" + client.getInfo("id") + "> has disconnected from the server";
      this.sendToAllClients(info);
  }

  /**
   * Implementation of AbstractServer hook to print a message when
   * a client gets disconnected from the server.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    String info = "<" + client.toString() + "> has disconnected from the server";
    System.out.println(info);
    this.sendToAllClients(info);
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
  
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
