package com.ggi.uparty.loadbalance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.esotericsoftware.kryonet.Server;
import com.ggi.uparty.data.World;
import com.ggi.uparty.network.Authenticate;
import com.ggi.uparty.network.ConnectClient;
import com.ggi.uparty.network.ConnectServ;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.network.Network;
import com.ggi.uparty.network.RefreshServ;

public class UPLoad extends JFrame{

	public Server server;
	public String version = "2.1.2";
	public String version2 = "2.1.3";
	
	public World world;
	private boolean debug = false;
	private String path = debug?"D:\\profiles\\":"C:\\Users\\Administrator\\Google Drive\\uParty\\DATA\\";
	
	//public LeftPane left;
	
	public boolean isSave = false;
	public boolean isLoad = false;
	
	public ArrayList<ServData> servs = new ArrayList<ServData>();
	
	public ServPanel sp;
	public UsagePane up;
	
	public Tracker track = new Tracker();
	
	//public boolean newReport=true;
	
	public UPLoad(){
		//world = loadWorld();
		Thread t = new Thread(track);
		
		//left = new LeftPane(this);
		setTitle("uParty Load Balancer");
		setSize(800,400);
		super.setBackground(Color.black);
		super.setForeground(Color.orange);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sp = new ServPanel(this);
		up = new UsagePane(this);
		//this.add(left,BorderLayout.CENTER);
		this.add(sp, BorderLayout.CENTER);
		this.add(up, BorderLayout.EAST);
		setVisible(true);
		
		runServer();
		t.start();
		
	}
	
	private void runServer(){
		server = new Server();
		server.start();
		try {
			server.bind(36696);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Network.register(server);
		server.addListener(new ThreadedListener(new Listener(){
			public void connected(Connection connection){
				
			}
			public void disconnected(Connection connection){
				for(int i = 0; i < servs.size(); i++){
					if(servs.get(i).c.equals(connection)){
						servs.remove(i);
						break;
					}
				}
				//left.repaint();
				sp.repaint();
				repaint();
			}
			public void received (Connection connection, Object object) {
				System.out.println("Received");
				if(object instanceof ConnectServ){
					System.out.println("Connect Server");
					for(int i = 0; i < servs.size(); i++){
						if(servs.get(i).c.getRemoteAddressTCP().getHostString().equals(connection.getRemoteAddressTCP().getHostString())){
							//servs.remove(i);
							break;
						}
						else if(i == servs.size()-1){
							servs.add(new ServData(connection));
							break;
						}
					}
					if(servs.size()==0){
						servs.add(new ServData(connection));
					}
				}
				else if(object instanceof ConnectClient){
					track.newClient();
					ConnectClient o = (ConnectClient) object;
					Authenticate auth = new Authenticate();
					if(o.version.equals(version) || o.version.equals(version2)){
						Collections.sort(servs);
						auth.vcheck=true;
						if(servs.size()>0){
							System.out.println("Selecting Server to send:");
							System.out.println(servs.get(0).c.getRemoteAddressTCP().getHostString());
							String split[] = servs.get(0).c.getRemoteAddressTCP().getHostString().split("\\.");
							System.out.println(split.length);
							auth.servIP="ec2-"+split[0]+"-"+split[1]+"-"+split[2]+"-"+split[3]+".us-west-2.compute.amazonaws.com";
							System.out.println("Converting to DNS");
							System.out.println(auth.servIP);
						}
						
					}
					connection.sendTCP(auth);
					connection.close();
				}
				else if(object instanceof RefreshServ){
					RefreshServ o = (RefreshServ) object;
					for(int i = 0; i < servs.size(); i++){
						if(servs.get(i).c.equals(connection)){
							servs.get(i).cons = o.cons;
							break;
						}
					}
				}
				//left.repaint();
				sp.repaint();
				up.repaint();
				repaint();
			}
		}));
	}
	
	public static void main(String[] args){
		new UPLoad();
	}
	
	public void saveWorld(World w){
		File directory = new File(path);
		if(!directory.exists()){directory.mkdir();}
		File f = new File(path+"world.uPWorld");
		while(isLoad || isSave){}
		try{
			isSave=true;
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(w);
			oos.close();
			isSave = false;
		}catch(Exception e){
			e.printStackTrace();
			//right.printConsole("[Error]-World save error");
		}
	}
	
	public World loadWorld(){
		World result = null;
		try{
		File f = new File(path+"world.uPWorld");
		while(isSave || isLoad){}
		if(f.exists()){
			isLoad = true;
			System.out.println("WORLD EXISTS");
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			result = (World) ois.readObject();
			ois.close();
			isLoad = false;
		}else{
			System.out.println("WORLD DNE");
			result = new World();
			result.init();
			saveWorld(result);
		}
		
		}catch(Exception e){
			
		//e.printStackTrace();
		}
		
		System.out.println(result);
		return result;
	}
	
	public void removeEvent(Event e) {
		if(e.group.length()>0){
			Group group = loadGroup(e.group);
			for(int i = 0; i < group.events.size(); i++){
				if(group.events.get(i).ID.equals(e.ID)){
					group.events.remove(i);
				}
			}
			saveGroup(group);
		}else{
			ArrayList<Event> evs = world.getAround(e.lat, e.lng).get(0).events;
			for(int i = 0; i < evs.size();i++){
				if(evs.get(i).ID.equals(e.ID)){
					evs.remove(i);
					world.eventsInStorage--;
				}
				
			}
		}
		
		
		
	}
	
	public void saveGroup(Group g){
		File directory = new File(path+"Groups\\");
		if(!directory.exists()){directory.mkdir();}
		File f = new File(path+"Groups\\"+g.name.replace(" ", "")+g.owner.replace(".", "_").replace("@", "_")+".uPGroup");
		
		try{
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(g);
			oos.close();
		}catch(Exception e){
			e.printStackTrace();
			//right.printConsole("[Error]-Group save error");
		}
	}
	
	public Group loadGroup(String id){
		Group result = null;
		try{
		File f = new File(path+"Groups\\"+id+".uPGroup");
		
		
		if(f.exists()){
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			result = (Group) ois.readObject();
			ois.close();
		}
		
		}catch(Exception e){
			
		//right.printConsole("[Error]-World load error");
		}
		return result;
	}
}
