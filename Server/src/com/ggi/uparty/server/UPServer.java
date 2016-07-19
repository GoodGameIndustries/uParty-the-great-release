package com.ggi.uparty.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.esotericsoftware.kryonet.Server;
import com.ggi.uparty.network.Account;
import com.ggi.uparty.network.Confirm;
import com.ggi.uparty.network.ErrorMessage;
import com.ggi.uparty.network.Login;
import com.ggi.uparty.network.Network;
import com.ggi.uparty.network.Resend;
import com.ggi.uparty.network.SignUp;

public class UPServer extends JFrame{

	
	
	private Server server;
	
	private boolean debug = true;
	private String path = debug?"D:\\profiles\\":"C:\\Users\\Administrator\\Google Drive\\uParty\\profiles\\";
	private RightPane right;
	private LeftPane left;

	private String htmlTemplate="";

	public UPServer(){
		right = new RightPane(this);
		left = new LeftPane(this);
		setTitle("uParty Server");
		setSize(800,400);
		super.setBackground(Color.black);
		super.setForeground(Color.orange);
		this.setLayout(new BorderLayout());
		this.add(right,BorderLayout.EAST);
		this.add(left,BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader("uPartyEmail.html"));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
		}
		htmlTemplate = contentBuilder.toString();
		
		
		Timer timer = new Timer();
		  TimerTask task = new TimerTask() {
		      @Override
		   public void run() {
		    right.repaint();
		    left.repaint();
		    repaint();
		       }
		  };
		  timer.schedule(task, 0,1000);

		
		runServer();
	}
	
	private void runServer() {
		server = new Server();
		server.start();
		try {
			server.bind(36693);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Network.register(server);
		server.addListener(new ThreadedListener(new Listener(){
			 public void received (Connection connection, Object object) {
				 
				 if(object instanceof SignUp){
					 SignUp o = (SignUp)object;
					 if(loadAccount(o.e)==null){
					 right.printConsole("[Sign Up]-"+o.u+":"+o.e);
					 Account a = new Account();
					 	a.u=o.u;
					 	a.e=o.e;
					 	a.p=o.p;
					 	Random ran = new Random();
	        			a.code= (100000 + ran.nextInt(900000));
	        		saveAccount(a);
	        		
	        		 try {
	        			  String msg = htmlTemplate.replace("$confirmation", ""+a.code);
	        				new SendMailSSL().send(o.e,"uParty Confirmation",msg);
	        			} catch (Exception e1) {
	        				right.printConsole("[Error]-Unable to send email");
	        			}
	        		 
	        		connection.sendTCP(a);
	        		
	        		
	        		
					}else{
						right.printConsole("[Warning]-Duplicate sign up attempt");
						connection.sendTCP(new ErrorMessage("Email in use"));
					}
				 }
				 
				if(object instanceof Login){
					Login o = (Login)object;
					Account a = loadAccount(o.e);
					if(a!=null){
						if(a.p.equals(o.p)){connection.sendTCP(a);}
						else{
							connection.sendTCP(new ErrorMessage("Incorrect Password"));
						}
					}
					else{
						connection.sendTCP(new ErrorMessage("No such account exists"));
					}
				}
				
				if(object instanceof Confirm){
					Confirm o = (Confirm)object;
					Account a = loadAccount(o.e);
					a.confirmed=true;
					saveAccount(a);
				}
				
				if(object instanceof Resend){
					Resend o = (Resend)object;
					Account a = loadAccount(o.e);
					 try {
	        			  String msg = htmlTemplate.replace("$confirmation", ""+a.code);
	        				new SendMailSSL().send(o.e,"uParty Confirmation",msg);
	        			} catch (Exception e1) {
	        				right.printConsole("[Error]-Unable to send email");
	        			}
				}
				 
			 }
			 }));
		
	}

	public void saveAccount(Account a){
		String loc = a.e;
		 String dir="";
		 //System.out.println(a.e);
	   	 String[] split = loc.split("@")[1].split("\\.");
	   	 //System.out.println(split.length);
	   	  dir = split[split.length-2]+"_"+split[split.length-1];
	   	  loc = loc.replace('.', '_');
	   	  loc = loc.replace('@', '_');
  	  loc+=".profile";
  	  File directory = new File(path+dir);
  	  if(!directory.exists()){directory.mkdir();}
  	  File f = new File(path+dir+"\\"+loc);
  	  
  	  try {
  		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(a);
	} catch (Exception e) {
		right.printConsole("[Error]-Account save error");
		e.printStackTrace();
	}
	}
	
	public Account loadAccount(String l){
		
		Account result = null;
		try {
		String loc = l;
		 String dir="";
	   	  
	   	 String[] split = loc.split("@")[1].split("\\.");
	   	 dir = split[split.length-2]+"_"+split[split.length-1];
	   	  loc = loc.replace('.', '_');
	   	  loc = loc.replace('@', '_');
	   	  loc+=".profile";
	   	  File f = new File(path+dir+"\\"+loc);
	   	  if(f.exists()){
	   	  
		
	   	
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			result = (Account) ois.readObject();
	   	  }
		} catch (Exception e) {
			right.printConsole("[Error]-Account load error");
			//e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args){
		new UPServer();
	}
	
}
