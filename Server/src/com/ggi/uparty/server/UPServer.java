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
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.esotericsoftware.kryonet.Server;
import com.ggi.uparty.data.Haversine;
import com.ggi.uparty.network.Account;
import com.ggi.uparty.network.ChangePass;
import com.ggi.uparty.network.ChangeUser;
import com.ggi.uparty.network.Comment;
import com.ggi.uparty.network.Confirm;
import com.ggi.uparty.network.ConnectServ;
import com.ggi.uparty.network.DeleteGroup;
import com.ggi.uparty.network.DownVote;
import com.ggi.uparty.network.ErrorMessage;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.network.Forgot;
import com.ggi.uparty.network.Friend;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.network.Invite;
import com.ggi.uparty.network.InviteNew;
import com.ggi.uparty.network.LeaveGroup;
import com.ggi.uparty.network.Login;
import com.ggi.uparty.network.Member;
import com.ggi.uparty.network.Network;
import com.ggi.uparty.network.NewGroup;
import com.ggi.uparty.network.Refresh;
import com.ggi.uparty.network.RefreshServ;
import com.ggi.uparty.network.Report;
import com.ggi.uparty.network.Resend;
import com.ggi.uparty.network.SignUp;
import com.ggi.uparty.network.UpVote;

public class UPServer extends JFrame {

	public Server server;

	private boolean debug = false;
	public String path = debug ? "D:\\profiles\\" : "C:\\Users\\Administrator\\Google Drive\\uParty\\UP_DATA\\";
	private RightPane right;
	private LeftPane left;
	
	private ArrayList<String> endings;

	private String htmlTemplate = "", forgotTemplate = "", inviteTemplate = "";

	public long lastResponse = 0;

	public Client client;

	public boolean isLoad = false;
	public boolean isSave = false;

	public boolean newReport = true;

	public UPServer() {
		right = new RightPane(this);
		left = new LeftPane(this);
		setTitle("uParty Server");
		setSize(800, 400);
		super.setBackground(Color.black);
		super.setForeground(Color.orange);
		this.setLayout(new BorderLayout());
		this.add(right, BorderLayout.EAST);
		this.add(left, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		endings = new ArrayList<String>();
		endings.add(".edu");
		endings.add("mymdc.net");

		createClient();

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
		contentBuilder = new StringBuilder();

		try {
			BufferedReader in = new BufferedReader(new FileReader("uPartyForgotEmail.html"));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();
		} catch (IOException e) {
		}
		forgotTemplate = contentBuilder.toString();
		contentBuilder = new StringBuilder();

		try {
			BufferedReader in = new BufferedReader(new FileReader("uPartyInviteEmail.html"));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();
		} catch (IOException e) {
		}
		inviteTemplate = contentBuilder.toString();

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				right.repaint();
				left.repaint();
				repaint();
			}
		};
		timer.schedule(task, 0, 1000);
		

		runServer();
	}




	protected void giveXp(String e, int i) {
		Account a = loadAccount(e);
		if(a!=null){
		a.xp += i;
		if (a.xp < 5)
			a.xp = 5;
		saveAccount(a);
		}

	}


	private void runServer() {
		server = new Server();
		server.start();
		try {
			server.bind(36695);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Network.register(server);
		server.addListener(new ThreadedListener(new Listener() {
			public void connected(Connection connection) {
				RefreshServ r = new RefreshServ();
				r.cons = server.getConnections().length;
				send(r);
			}

			public void disconnected(Connection connection) {
				RefreshServ r = new RefreshServ();
				r.cons = server.getConnections().length;
				send(r);
			}

			public void received(Connection connection, Object object) {
				// System.out.println(connection.getRemoteAddressTCP().getHostString());
				long startTime = System.currentTimeMillis();
				if (object instanceof SignUp) {
					SignUp o = (SignUp) object;
					if(isAllowed(o.e)){
					if (loadAccount(o.e) == null) {
						right.printConsole("[Sign Up]-" + o.u + ":" + o.e);
						Account a = new Account();
						a.u = o.u;
						a.e = o.e;
						a.p = o.p;
						Random ran = new Random();
						a.code = (100000 + ran.nextInt(900000));
						saveAccount(a);

						try {
							String msg = htmlTemplate.replace("$confirmation", "" + a.code);
							new SendMailSSL().send(o.e, "uParty Confirmation", msg);
						} catch (Exception e1) {
							right.printConsole("[Error]-Unable to send email");
						}

						sendAccount(connection, a);

					} else {
						right.printConsole("[Warning]-Duplicate sign up attempt");
						connection.sendTCP(new ErrorMessage("Email in use"));
					}
					}
					else{
						connection.sendTCP(new ErrorMessage("Must end in .edu or be approved!"));
					}
				}

				if (object instanceof Login) {
					Login o = (Login) object;
					Account a = loadAccount(o.e);
					if (a != null) {
						if (a.p.equals(o.p)) {
							sendAccount(connection, a);
						} else {
							connection.sendTCP(new ErrorMessage("Incorrect Password"));
						}
					} else {
						connection.sendTCP(new ErrorMessage("No such account exists"));
					}
				}

				if (object instanceof Confirm) {
					Confirm o = (Confirm) object;
					Account a = loadAccount(o.e);
					a.confirmed = true;
					saveAccount(a);
				}

				if (object instanceof Forgot) {
					Forgot o = (Forgot) object;
					Account a = loadAccount(o.e);

					try {
						String msg = forgotTemplate.replace("$password", "" + a.p);
						new SendMailSSL().send(o.e, "uParty Forgot Password", msg);
					} catch (Exception e1) {
						right.printConsole("[Error]-Unable to send email");
					}

				}

				if (object instanceof Report) {
					
					newReport = true;
					Report o = (Report) object;
					
					Event e = find(o.lat,o.lng,o.ID);
					if(e!=null){
						e.reporters.add(o.e);
						if(!isReported(e)){
							report (e);
						}
					}
					
				}

				if (object instanceof Resend) {
					Resend o = (Resend) object;
					Account a = loadAccount(o.e);
					try {
						String msg = htmlTemplate.replace("$confirmation", "" + a.code);
						new SendMailSSL().send(o.e, "uParty Confirmation", msg);
					} catch (Exception e1) {
						right.printConsole("[Error]-Unable to send email");
					}
				}

				if (object instanceof NewGroup) {
					NewGroup o = (NewGroup) object;
					Group g = new Group();
					g.owner = o.owner;
					g.name = o.name;

					Account a = loadAccount(g.owner);

					Member m = new Member();
					m.e = a.e;
					m.u = a.u;
					m.xp = a.xp;
					m.rank = 2;
					g.members.add(m);
					a.groups.add(g);
					saveAccount(a);
					saveGroup(g);
					sendGroup(connection, g, g.owner);
				}

				if (object instanceof Event) {
					Event o = (Event) object;
					if (o.group.length() == 0) {
						saveEvent(o);

					} else {
						Group g = loadGroup(o.group);
						g.events.add(o);
						saveEvent(o);
						saveGroup(g);
					}

					giveXp(o.owner, 2);

				}

				if (object instanceof DeleteGroup) {
					DeleteGroup o = (DeleteGroup) object;
					File f = new File(path + "Groups\\" + o.group + ".uPGroup");
					f.delete();

				}

				if (object instanceof InviteNew) {
					InviteNew o = (InviteNew) object;
					if (loadAccount(o.e) == null) {
						SendMailSSL mail = new SendMailSSL();
						try {
							mail.send(o.e, "uParty Invitation", inviteTemplate);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

				if (object instanceof ChangeUser) {
					ChangeUser o = (ChangeUser) object;
					Account a = loadAccount(o.e);
					a.u = o.u;
					saveAccount(a);
				}

				if (object instanceof ChangePass) {
					ChangePass o = (ChangePass) object;
					Account a = loadAccount(o.e);
					a.p = o.p;
					saveAccount(a);
				}

				if (object instanceof LeaveGroup) {
					LeaveGroup o = (LeaveGroup) object;
					Group g = loadGroup(o.group);
					for (int i = 0; i < g.members.size(); i++) {
						if (g.members.get(i).e.equals(o.e)) {
							g.members.remove(i);
						}
					}
					saveGroup(g);

					Account a = loadAccount(o.e);
					a.groups.remove(g);
					saveAccount(a);

				}

				if (object instanceof Invite) {
					Invite o = (Invite) object;
					Group g = loadGroup(o.group);
					Account a = loadAccount(o.e);
					if (a != null) {
						Member m = new Member();
						m.e = a.e;
						m.u = a.u;
						m.xp = a.xp;
						if (!g.members.contains(m)) {
							a.groups.add(g);
							g.members.add(m);
						}
						saveGroup(g);
						saveAccount(a);
					}
				}

				if (object instanceof Comment) {
					Comment o = (Comment) object;
					
					Event e = find(o.lat,o.lng,o.ID);
					if(e!=null){
						e.comments.add(o.c);
						giveXp(o.e,2);
					}
					saveEvent(e);
					
				}

				if (object instanceof UpVote) {
					UpVote o = (UpVote) object;
					Event e = find(o.lat,o.lng,o.ID);
					if(e != null){
						if (!e.downVote.contains(o.e) && !e.upVote.contains(o.e)) {
							giveXp(o.e, 1);
						}

						if (e.downVote.contains(o.e)) {
							e.downVote.remove(o.e);
							giveXp(e.owner, 1);
						}
						if (!e.upVote.contains(o.e)) {
							e.upVote.add(o.e);
							giveXp(e.owner, 1);
						}
					}
					saveEvent(e);
					
				}

				if (object instanceof DownVote) {
					DownVote o = (DownVote) object;
					Event e = find(o.lat,o.lng,o.ID);
					if(e!=null){
						if (!e.downVote.contains(o.e) && !e.upVote.contains(o.e)) {
							giveXp(o.e, 1);
						}

						if (!e.downVote.contains(o.e)) {
							e.downVote.add(o.e);
							giveXp(e.owner, -1);
						}
						if (e.upVote.contains(o.e)) {
							e.upVote.remove(o.e);
							giveXp(e.owner, -1);
						}
					}
					saveEvent(e);
					
				}

				if (object instanceof Refresh) {
					Refresh o = (Refresh) object;
					ArrayList<Event> events = loadAllEvents();
					Date d = new Date();
					for(int i = 0; i < events.size(); i++){
						if(Haversine.distance(o.lat, o.lng, events.get(i).lat, events.get(i).lng)<=40){
							if(events.get(i).end.before(d) || events.get(i).upVote.size() - events.get(i).downVote.size() <-5){
								removeEvent(events.get(i));
							}
							else{
								sendEvent(connection, events.get(i), o.e);
							}
						}
					}

					sendAccount(connection, loadAccount(o.e));

					connection.sendTCP(new Refresh());
				}

				lastResponse = System.currentTimeMillis() - startTime;
			}

			

			private void sendAccount(Connection c, Account a) {
				Account n = new Account();
				n.u = a.u;
				n.e = a.e;
				n.p = a.p;
				n.xp = a.xp;
				n.code = a.code;
				n.confirmed = a.confirmed;
				c.sendTCP(n);

				for (Friend f : a.friends) {
					c.sendTCP(f);
				}

				for (int i = 0; i < a.groups.size(); i++) {
					Group g = null;
					if(a.groups.get(i)!=null&&a.groups.get(i).name.length()>0){
						g = a.groups.get(i);
					}
					boolean sent = false;
					if(g!=null&&g.name.length()>0){
					Group g2 = loadGroup(g.name.replace(" ", "") + g.owner.replace(".", "_").replace("@", "_"));
					if (g2 != null) {

						for (int j = 0; j < g2.members.size(); j++) {
							if (g2.members.get(j).e.equals(a.e)) {
								sent = true;
								sendGroup(c, g2, a.e);
								g = g2;
							}
						}
					} else {
						a.groups.remove(i);
						sent = true;
					}
					if (!sent) {
						a.groups.remove(i);
					}
				}
				}
				saveAccount(a);

			}

			private void sendGroup(Connection c, Group g, String email) {
				Group n = new Group();
				n.name = g.name;
				n.owner = g.owner;
				c.sendTCP(n);

				for (int i = 0; i < g.events.size(); i++) {
					Event e = g.events.get(i);
					Date d = new Date();
					if (e.end.before(d) || e.upVote.size() - e.downVote.size() < -5) {
						g.events.remove(e);
					} else {
						sendEvent(c, e, email);
					}
				}

				for (Member m : g.members) {
					c.sendTCP(m);
				}

			}

			private void sendEvent(Connection connection, Event e, String email) {
				Event s = new Event(e.lng, e.lat, e.name, e.description, e.location, e.start, e.end, e.owner,
						e.ownerXp);
				s.group = e.group;
				s.posted = e.posted;
				if (!e.reporters.contains(email)) {
					connection.sendTCP(s);
					for (String a : e.upVote) {
						UpVote u = new UpVote();
						u.e = a;
						u.ID = s.ID;
						u.group = e.group;
						connection.sendTCP(u);
					}
					for (String a : e.downVote) {
						DownVote u = new DownVote();
						u.e = a;
						u.ID = s.ID;
						u.group = e.group;
						connection.sendTCP(u);
					}
					for (String c : e.comments) {
						Comment com = new Comment();
						com.e = "";
						com.c = c;
						com.ID = s.ID;
						com.group = e.group;
						connection.sendTCP(com);
					}

					/*
					 * for(String a:e.reporters){ Report u = new Report();
					 * u.e=a; u.ID=s.ID; u.group=e.group; connection.sendTCP(u);
					 * }
					 */
				}
			}
		}));

	}


	protected boolean isAllowed(String e) {
		boolean result = false;
		for(int i = 0; i < endings.size(); i++){
			if(e.endsWith(endings.get(i))){
				result = true;
				break;
			}
		}
		return result;
	}


	public void saveAccount(Account a) {
		try {	
			String loc = a.e;
			String dir = "";
			// System.out.println(a.e);
			String[] split = loc.split("@")[1].split("\\.");
			// System.out.println(split.length);
			dir = split[split.length - 2] + "_" + split[split.length - 1];
			loc = loc.replace('.', '_');
			loc = loc.replace('@', '_');
			loc += ".profile";
			File directory = new File(path +"Accounts\\"+ dir);
			if (!directory.exists()) {
				directory.mkdir();
			}
			File f = new File(path +"Accounts\\"+ dir + "\\" + loc);
			
			f.createNewFile();
			String info = "";
			PrintWriter out = new PrintWriter(f);
			
			/**building info*/
			
			info+="<u>"+a.u+"</u>\n";
			info+="<e>"+a.e+"</e>\n";
			info+="<p>"+a.p+"</p>\n";
			info+="<xp>"+a.xp+"</xp>\n";
			info+="<code>"+a.code+"</code>\n";
			info+="<confirmed>"+a.confirmed+"</confirmed>\n";
			
			for(int i = 0; i < a.friends.size(); i++){
				info+="<friend>"+a.friends.get(i)+"</friend>\n";
			}
			for(int i = 0; i < a.groups.size(); i++){
				Group g = a.groups.get(i);
				info+="<group>"+g.name.replace(" ", "") + g.owner.replace(".", "_").replace("@", "_")+"</group>\n";
			}
			
			/**end building info*/
			
			out.print(info);
			out.close();
		} catch (Exception e) {
			//right.printConsole("[Error]-Account save error");
			// e.printStackTrace();
		}
		
	}

	public Account loadAccount(String l) {

		Account result = null;
		try {
			String loc = l;
			String dir = "";

			String[] split = loc.split("@")[1].split("\\.");
			dir = split[split.length - 2] + "_" + split[split.length - 1];
			loc = loc.replace('.', '_');
			loc = loc.replace('@', '_');
			loc += ".profile";
			File f = new File(path+"Accounts\\" + dir + "\\" + loc);
			if (f.exists()) {
				result = new Account();
				List<String> lines = Files.readAllLines(f.toPath());
				
				for(int i = 0; i< lines.size(); i++){
					if(lines.get(i).startsWith("<u>")){result.u = lines.get(i).substring(3, lines.get(i).length()-4);}
					else if(lines.get(i).startsWith("<e>")){result.e = lines.get(i).substring(3, lines.get(i).length()-4);}
					else if(lines.get(i).startsWith("<p>")){result.p = lines.get(i).substring(3, lines.get(i).length()-4);}
					else if(lines.get(i).startsWith("<xp>")){result.xp = Long.parseLong(lines.get(i).substring(4, lines.get(i).length()-5));}
					else if(lines.get(i).startsWith("<code>")){result.code = Integer.parseInt(lines.get(i).substring(6, lines.get(i).length()-7));}
					else if(lines.get(i).startsWith("<confirmed>")){result.confirmed = Boolean.parseBoolean(lines.get(i).substring(11, lines.get(i).length()-12));}
					else if(lines.get(i).startsWith("<group>")){
						if(lines.get(i).substring(7, lines.get(i).length()-8).length()>0){
							Group g = loadGroup(lines.get(i).substring(7, lines.get(i).length()-8));
							if(g!=null){
						result.groups.add(g);
							}
							}
						
					}
					
				}
			}
		} catch (Exception e) {
			right.printConsole("[Error]-Account load error");
			// e.printStackTrace();
		}
		return result;
	}

	public void saveGroup(Group g) {
		File f = new File(path + "Groups\\" + g.name.replace(" ", "") + g.owner.replace(".", "_").replace("@", "_") + ".uPGroup");
		
		try {
			f.createNewFile();
			String info = "";
			PrintWriter out = new PrintWriter(f);
			
			info += "<owner>"+g.owner+"</owner>\n";
			info += "<name>" + g.name + "</name>\n";
			
			for(int i = 0; i < g.events.size();i++){
				Event e = g.events.get(i);
				info += "<event>" + Math.abs(e.lat)+(e.lat<0?"S":"N")+"_"+Math.abs(e.lng)+(e.lng<0?"W":"E")+"_"+e.ownerXp+".event</event>\n";
			}
			
			for(int i = 0; i < g.members.size();i++){
				Member m = g.members.get(i);
				info += "<member>" + m.rank+"_"+m.e+"</member>\n";
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public Group loadGroup(String id) {
		Group result = null;
		try {
			File f = new File(path + "Groups\\" + id + ".uPGroup");

			if (f.exists()) {
				result = new Group();
				result.name="";
				result.owner="";
				List<String> lines = Files.readAllLines(f.toPath());
				for(int i = 0; i < lines.size(); i++){
					if(lines.get(i).startsWith("<owner>")){result.owner = lines.get(i).substring(7, lines.get(i).length()-8);}
					else if(lines.get(i).startsWith("<name>")){result.name = lines.get(i).substring(6, lines.get(i).length()-7);}
					else if(lines.get(i).startsWith("<event>")){ File ev = new File(path+"Events\\"+lines.get(i).substring(7, lines.get(i).length()-8));
						if(ev.exists()){result.events.add(loadEvent(ev));}
					}
					else if(lines.get(i).startsWith("<member>")){
						String[] breakdown = lines.get(i).substring(8, lines.get(i).length()-9).split("_");
						//Account a = loadAccount(breakdown[1]);
						//if(a!=null){
							Member m = new Member();
							m.e=breakdown[1];
							//m.u=a.u;
							//m.xp=a.xp;
							m.rank=Integer.parseInt(breakdown[0]);
							result.members.add(m);
						//}
					}
				}
			}

		} catch (Exception e) {

			// right.printConsole("[Error]-World load error");
		}
		return result;
	}

	public static void main(String[] args) {
		new UPServer();
	}

	public void removeEvent(Event e) {
		File f = new File(path+"Events\\"+Math.abs(e.lat)+(e.lat<0?"S":"N")+"_"+Math.abs(e.lng)+(e.lng<0?"W":"E")+"_"+e.ownerXp+".event");
		if(f.exists()){
		f.delete();
		}

	}

	public void connect() {
		if (!client.isConnected()) {
			try {

				client.start();
				client.connect(5000, debug ? "localhost" : "35.164.43.2", 36696);
				Network.register(client);
				send(new ConnectServ());
			} catch (IOException e) {
				right.printConsole("[ERROR]-Cannot connect to Load Balance.");

				e.printStackTrace();
			}
		}
	}

	public void send(Object o) {
		connect();
		int t = 0;
		boolean noSend = true;
		while (noSend && t <= 3) {
			try {
				t++;
				client.sendTCP(o);
				noSend = false;
			} catch (Exception e) {
				connect();
			}
		}
	}

	public void createClient() {
		client = new Client();
		client.addListener(new ThreadedListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof RefreshServ) {
					RefreshServ r = new RefreshServ();
					r.cons = server.getConnections().length;
					send(r);
				}
			}
		}));
		connect();
	}
	
	public ArrayList<Event> loadAllEvents(){
		ArrayList<Event> events = new ArrayList<Event>();
		File folder = new File(path+"Events");
		File[] listOfFiles = folder.listFiles();
		for(int i = 0; i < listOfFiles.length; i++){
			events.add(loadEvent(listOfFiles[i]));
		}
		return events;
		
	}
	
	public Event find(float lat, float lng, String ID){
		Event result = null;
		
		File folder = new File(path+"Events");
		File[] listOfFiles = folder.listFiles();
		for(int i = 0; i < listOfFiles.length; i++){
			if(listOfFiles[i].getPath().contains(Math.abs(lat)+(lat<0?"S":"N")+"_"+Math.abs(lng)+(lng<0?"W":"E"))){
				Event temp = loadEvent(listOfFiles[i]);
				if(temp.ID.equals(ID)){
					result = temp;
					break;
				}
			}
		}
		
		
		return result;
		
	}
	
	public boolean isReported(Event e){
		return new File(path+"Reported\\"+Math.abs(e.lat)+(e.lat<0?"S":"N")+"_"+Math.abs(e.lng)+(e.lng<0?"W":"E")+"_"+e.ownerXp+".event").exists();
		
	}
	
	public void report(Event e){
		saveEvent(e);
		File f = new File(path+"Events\\"+Math.abs(e.lat)+(e.lat<0?"S":"N")+"_"+Math.abs(e.lng)+(e.lng<0?"W":"E")+"_"+e.ownerXp+".event");
		try {
			if(f.exists()){
			Files.copy(f.toPath(), new File(path+"Reported\\"+Math.abs(e.lat)+(e.lat<0?"S":"N")+"_"+Math.abs(e.lng)+(e.lng<0?"W":"E")+"_"+e.ownerXp+".event").toPath());
			}
			} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public Event loadEvent(File f){
		
		
				Event temp = new Event();
					temp.start=new Date();
					temp.end=new Date();
					temp.posted=new Date();
				
				//READING INFO
					try {
						List<String> lines = Files.readAllLines(f.toPath());
						
						for(int j = 0; j < lines.size(); j++){
							if(lines.get(j).startsWith("<name>")){temp.name=lines.get(j).substring(6, lines.get(j).length()-7);}
							else if(lines.get(j).startsWith("<description>")){temp.description=lines.get(j).substring(13, lines.get(j).length()-14);}
							else if(lines.get(j).startsWith("<location>")){temp.location=lines.get(j).substring(10, lines.get(j).length()-11);}
							else if(lines.get(j).startsWith("<lng>")){temp.lng=Float.parseFloat(lines.get(j).substring(5, lines.get(j).length()-6));}
							else if(lines.get(j).startsWith("<lat>")){temp.lat=Float.parseFloat(lines.get(j).substring(5, lines.get(j).length()-6));}
							else if(lines.get(j).startsWith("<ID>")){temp.ID=lines.get(j).substring(4, lines.get(j).length()-5);}
							else if(lines.get(j).startsWith("<group>")){temp.group=lines.get(j).substring(7, lines.get(j).length()-8);}
							else if(lines.get(j).startsWith("<start>")){temp.start.setTime(Long.parseLong(lines.get(j).substring(7, lines.get(j).length()-8)));}
							else if(lines.get(j).startsWith("<end>")){temp.end.setTime(Long.parseLong(lines.get(j).substring(5, lines.get(j).length()-6)));}
							else if(lines.get(j).startsWith("<posted>")){temp.posted.setTime(Long.parseLong(lines.get(j).substring(8, lines.get(j).length()-9)));}
							else if(lines.get(j).startsWith("<owner>")){temp.owner=lines.get(j).substring(7, lines.get(j).length()-8);}
							else if(lines.get(j).startsWith("<ownerXp>")){temp.ownerXp=Long.parseLong(lines.get(j).substring(9, lines.get(j).length()-10));}
							else if(lines.get(j).startsWith("<upVote>")){temp.upVote.add(lines.get(j).substring(8, lines.get(j).length()-9));}
							else if(lines.get(j).startsWith("<downVote>")){temp.downVote.add(lines.get(j).substring(10, lines.get(j).length()-11));}
							else if(lines.get(j).startsWith("<comment>")){temp.comments.add(lines.get(j).substring(9, lines.get(j).length()-10));}
							else if(lines.get(j).startsWith("<reporter>")){temp.reporters.add(lines.get(j).substring(10, lines.get(j).length()-11));}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//END READING INFO
					
		
		return temp;
		
	}
	
	protected void saveEvent(Event e) {
		try {
			File f = new File(path+"Events\\"+Math.abs(e.lat)+(e.lat<0?"S":"N")+"_"+Math.abs(e.lng)+(e.lng<0?"W":"E")+"_"+e.ownerXp+".event");
			f.createNewFile();
			String info = "";
			PrintWriter out = new PrintWriter(f);
			info += "<name>" + e.name + "</name>\n";
			info += "<description>"+e.description+"</description>\n";
			info += "<location>"+e.location+"</location>\n";
			info += "<lng>"+e.lng +"</lng>\n";
			info += "<lat>"+e.lat+"</lat>\n";
			info += "<ID>" + e.ID+"</ID>\n";
			info += "<group>"+e.group+"</group>\n";
			info += "<start>"+e.start.getTime()+"</start>\n";
			info += "<end>"+e.end.getTime()+"</end>\n";
			info += "<posted>"+e.posted.getTime()+"</posted>\n";
			info += "<owner>"+e.owner+"</owner>\n";
			info += "<ownerXp>"+e.ownerXp+"</ownerXp>\n";
			
			for(int i = 0 ;i < e.upVote.size(); i++){
				info += "<upVote>" + e.upVote.get(i) + "</upVote>\n";
			}
			
			for(int i = 0; i < e.downVote.size(); i++){
				info += "<downVote>" + e.downVote.get(i) +"</downVote>\n";
			}
			
			for(int i = 0; i < e.comments.size(); i++){
				info += "<comment>" + e.comments.get(i) + "</comment>\n";
			}
			
			for(int i = 0; i < e.reporters.size(); i++){
				info += "<reporter>" + e.reporters.get(i) + "</reporter>\n";
			}
			
			out.write(info);
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
	}

}
