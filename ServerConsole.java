// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;
import java.util.Scanner;


/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the server that created this ConsoleChat.
   */
  EchoServer server;

  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {

    server= new EchoServer(port);
      
    try 
    {
      server.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }

    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      String message;

      while (true) 
      {
        message = fromConsole.nextLine();

        if(message.contains("setport")) {
          if(server.getNumberOfClients() == 0 && !server.isListening())
          {
            String portnumber = message.split("<")[1].substring(0, message.split("<")[1].length() - 1);
            server.setPort(Integer.parseInt(portnumber));
          }
          else System.out.println("You cannot use this option while the server is not closed.");
        }
        else{
          switch(message) 
          { 
              case "#quit": 
                  System.exit(0);
                  break;
              case "#stop": 
                  server.stopListening();
                  break; 
              case "#close": 
                  try { server.close(); }
                  catch (IOException ioe) {}
                  break;
              case "#start":
                  server.listen();
                  break;
              case "#getport":
                System.out.println("Port Number: " + server.getPort());
                break;
              default: 
                display(message);
                server.sendToAllClients( "SERVER MSG> " +  message);
                break;
          }
        }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    int port = 0;  //The port number

    try
    {
      port = Integer.valueOf(args[0]);
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      port = DEFAULT_PORT;
    }

    ServerConsole chat= new ServerConsole(port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
