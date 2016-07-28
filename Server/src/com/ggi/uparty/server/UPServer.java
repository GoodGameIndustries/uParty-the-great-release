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
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.esotericsoftware.kryonet.Server;
import com.ggi.uparty.data.Datapoint;
import com.ggi.uparty.data.World;
import com.ggi.uparty.network.Account;
import com.ggi.uparty.network.Confirm;
import com.ggi.uparty.network.DownVote;
import com.ggi.uparty.network.ErrorMessage;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.network.Friend;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.network.Login;
import com.ggi.uparty.network.Network;
import com.ggi.uparty.network.Refresh;
import com.ggi.uparty.network.Resend;
import com.ggi.uparty.network.SignUp;
import com.ggi.uparty.network.UpVote;

public class UPServer extends JFrame{

	
	
	public Server server;
	
	private boolean debug = true;
	private String path = debug?"D:\\profiles\\":"C:\\Users\\Administrator\\Google Drive\\uParty\\profiles\\";
	private RightPane right;
	private LeftPane left;
	
	public World world;

	private String htmlTemplate="";

	public long lastResponse=0;
	
	public UPServer(){
		world = loadWorld();
		if(world==null){System.out.println("world null");}
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
				 long startTime = System.currentTimeMillis();
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
	        		 
	        		sendAccount(connection,a);
	        		
	        		
	        		
					}else{
						right.printConsole("[Warning]-Duplicate sign up attempt");
						connection.sendTCP(new ErrorMessage("Email in use"));
					}
				 }
				 
				if(object instanceof Login){
					Login o = (Login)object;
					Account a = loadAccount(o.e);
					if(a!=null){
						if(a.p.equals(o.p)){sendAccount(connection,a);}
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
				
				if(object instanceof Confirm){
					Confirm o = (Confirm)object;
					Account a = loadAccount(o.e);
					a.confirmed=true;
					saveAccount(a);
				}
				
				if(object instanceof Event){
					Event o = (Event)object;
					world.addToClosest(o);
					saveWorld(world);
				}
				
				if(object instanceof UpVote){
					UpVote o = (UpVote)object;
					ArrayList<Event> events = world.getAround(o.lat, o.lng).get(0).events;
					for(int i = 0; i < events.size();i++){
						if(events.get(i).ID.equals(o.ID)){
							Event e = events.get(i);
							if(e.downVote.contains(o.e)){e.downVote.remove(o.e);}
							if(!e.upVote.contains(o.e)){e.upVote.add(o.e);}
						}
					}
					saveWorld(world);
				}
				
				if(object instanceof DownVote){
					DownVote o = (DownVote)object;
					ArrayList<Event> events = world.getAround(o.lat, o.lng).get(0).events;
					for(int i = 0; i < events.size();i++){
						if(events.get(i).ID.equals(o.ID)){
							Event e = events.get(i);
							if(!e.downVote.contains(o.e)){e.downVote.add(o.e);}
							if(e.upVote.contains(o.e)){e.upVote.remove(o.e);}
						}
					}
					saveWorld(world);
				}
				
				
				if(object instanceof Refresh){
					Refresh o = (Refresh)object;
					ArrayList<Event> events = new ArrayList<Event>();
					ArrayList<Datapoint> points = world.getAround(o.lat, o.lng);
					Date d = new Date();
						d.setDate(d.getDate()+1);
					for(Datapoint p:points){
						for(int i = 0; i < p.events.size();i++){
							if(p.events.get(i).end.before(d)||p.events.get(i).upVote.size()-p.events.get(i).downVote.size()<-5){p.events.remove(i);world.eventsInStorage--;}
							else{events.add(p.events.get(i));}
						}
					}
					saveWorld(world);
					for(Event e:events){
						sendEvent(connection,e);
					}
					
					sendAccount(connection,loadAccount(o.e));
					
					connection.sendTCP(new Refresh());
				}
				
			 lastResponse = System.currentTimeMillis()-startTime;}

			private void sendAccount(Connection c,Account a) {
				Account n = new Account();
				n.u=a.u;n.e=a.e;n.p=a.p;n.xp=a.xp;n.code=a.code;n.confirmed=a.confirmed;
				c.sendTCP(n);
				
				for(Friend f:a.friends){
					c.sendTCP(f);
				}
				
				for(Group g:a.groups){
					c.sendTCP(g);
				}
				
			}

			private void sendEvent(Connection connection,Event e) {
				Event s = new Event(e.lng,e.lat,e.name,e.description,e.location,e.start,e.end,e.owner);
				s.posted=e.posted;
				connection.sendTCP(s);
				for(String a:e.upVote){
					UpVote u = new UpVote();
					u.e=a;
					u.ID=s.ID;
				connection.sendTCP(u);
				}
				for(String a:e.downVote){
					DownVote u = new DownVote();
					u.e=a;
					u.ID=s.ID;
					connection.sendTCP(u);
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
		oos.close();
	} catch (Exception e) {
		right.printConsole("[Error]-Account save error");
		//e.printStackTrace();
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
			ois.close();
	   	  }
		} catch (Exception e) {
			right.printConsole("[Error]-Account load error");
			//e.printStackTrace();
		}
		return result;
	}
	
	public void saveWorld(World w){
		File directory = new File(path);
		if(!directory.exists()){directory.mkdir();}
		File f = new File(path+"world.uPWorld");
		
		try{
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(w);
			oos.close();
		}catch(Exception e){
			e.printStackTrace();
			right.printConsole("[Error]-World save error");
		}
	}
	
	public World loadWorld(){
		World result = null;
		try{
		File f = new File(path+"world.uPWorld");
		
		if(f.exists()){
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			result = (World) ois.readObject();
			ois.close();
		}else{
			result = new World();
			result.init();
			saveWorld(result);
		}
		
		}catch(Exception e){
			
		//right.printConsole("[Error]-World load error");
		}
		return result;
	}
	
	public static void main(String[] args){
		new UPServer();
	}
	
}
