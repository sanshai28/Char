/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Package_3_Chat;

import java.net.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author user
 */
public class Server {
  public Server () throws IOException {
/* Create new ServerSocket object */
    ServerSocket server = new ServerSocket (1500);
    System.out.println("Waiting for requests from the clients");
    while (true) {
/* Receive client requests for connection */
      Socket client = server.accept ();
/* Display the IP address of the client at the console */
      System.out.println ("Client's InetAddress is : " + client.getInetAddress ());
      Handler h = new Handler (client);
/* Start the thread */
      h.start ();
    }
  }
public static void main (String args[]) throws IOException {
/* Start the server */
    new Server ();
  }
}
class Handler extends Thread {
  Socket socket;
  DataInputStream in;
  DataOutputStream out;
/* Create a Vector object to store client objects */
  static Vector clients = new Vector ();

  public Handler (Socket socket) throws IOException {
    this.socket = socket;
    in = new DataInputStream (new BufferedInputStream (socket.getInputStream ()));
    out = new DataOutputStream (new BufferedOutputStream (socket.getOutputStream ()));
  }
  public void run () {
    try {
/* Add the current client object to the Vector */
      clients.addElement (this);
      while (true) {
        String msg = in.readUTF ();
        transmit (msg);
      }
    } catch (IOException ex) {
 	System.out.println("Error");
    } finally {
      clients.removeElement (this);
      try {
        socket.close ();
      } catch (IOException ex) {
	     	System.out.println("Error");
      }
    }
  }
  static void transmit (String message) {
/* Transfer messages to all the clients */
    synchronized (clients) {
      Enumeration e = clients.elements ();
      while (e.hasMoreElements ()) {
        Handler h = (Handler) e.nextElement ();
        try {
          synchronized (h.out) {
            h.out.writeUTF (message);
          }
          h.out.flush ();
        } catch (IOException ex) {
          h = null;
        }
      }
    }
  }
}

