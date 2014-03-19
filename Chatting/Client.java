/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Package_3_Chat;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author user
 */
public class Client extends JFrame implements Runnable, ActionListener {
/* Variable declaration */
  DataInputStream in;
  DataOutputStream out;
  Thread t;
  GridBagLayout gbl;
  GridBagConstraints gbc;
  JTextArea output;
  JTextField input;
  JLabel text;
  String name;
  JPanel p;	
  public Client (String name) {
    this.name = name;
    setTitle("Welcome -- "+name);
    setSize(300, 300);
    p = new JPanel();
    output = new JTextArea (10, 20);
    input = new JTextField (20);
    gbl = new GridBagLayout();
    gbc = new GridBagConstraints();
/* Add components to panel p */
    p.setLayout (gbl);
    gbc.gridx = 0;
    gbc.gridy = 0;
    p.add (output, gbc);
    output.setEditable (false);
    gbc.gridx = 0;
    gbc.gridy = 1;
    text = new JLabel("Enter your message : ");
    p.add (text, gbc);
    gbc.gridx = 0;
    gbc.gridy = 2;
    p.add (input, gbc);
    input.requestFocus();
    input.addActionListener(this);
    validate();
    getContentPane().add(p);
/* Register the WindowListener to implement windowClosing() method */
    addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				t = null;
				System.exit(0);
			}
		      });	
    try{
/* Connect to the server */
    	Socket s = new Socket ("localhost", 1500);
   	this.in = new DataInputStream (new BufferedInputStream (s.getInputStream()));
	this.out = new DataOutputStream (new BufferedOutputStream (s.getOutputStream()));
    }catch(Exception e){
	System.out.println("Error");
    }
    t = new Thread (this);
/* Start the thread */
    t.start ();
  }
  public void run(){
     try{
	while(true){
	   String line = in.readUTF();
/* Display the message in the output text area */
	   output.append(line+"\n");
	}
     }catch(IOException e){
	System.out.println("ERROR");
     }finally{
	t = null;
	try{
	   out.close();
	}catch(Exception e){
		System.out.println("Error2");
	}
     }
  }
  public void actionPerformed(ActionEvent ae){
      String str = input.getText();
      try {
       synchronized(name){
        out.writeUTF (name+" : "+str);
        out.flush ();
       }
      } catch (IOException ex) {
	System.out.println("Error 4");
        t = null;
      }
      input.setText ("");
  }
  public static void main (String args[]) throws IOException {
    String str;
//    if (args.length != 1)
//      throw new RuntimeException ("Syntax: ChatClient ");
    str = "OSBT Student";
    Client f = new Client (str);
    f.show();
  }
}

