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
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.esotericsoftware.kryonet.Server;
import com.ggi.uparty.data.Datapoint;
import com.ggi.uparty.data.World;
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
	private String path = debug ? "D:\\profiles\\" : "C:\\Users\\Administrator\\Google Drive\\uParty\\DATA\\";
	private RightPane right;
	private LeftPane left;

	public World world;

	private String htmlTemplate = "", forgotTemplate = "", inviteTemplate = "";

	public long lastResponse = 0;

	public Client client;

	public boolean isLoad = false;
	public boolean isSave = false;

	public boolean newReport = true;

	public ArrayList<Object> stuffToDo = new ArrayList<Object>();

	public UPServer() {
		world = loadWorld();
		if (world == null) {
			System.out.println("world null");
		}
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
		

		Timer t2 = new Timer();
		TimerTask ta2 = new TimerTask() {
			@Override
			public void run() {
				boolean needSave = false;
				right.printConsole("[LOAD]-Start Loading");
				right.printConsole("\t-Stuff to do: " + stuffToDo.size());
				World world = loadWorld();
				if(world != null){
					if(stuffToDo.size() >0){needSave = true;}
				long st = System.currentTimeMillis();
				while(stuffToDo.size()>0){
					//right.printConsole("\t-Stuff to do: " + stuffToDo.size());
					Object object = stuffToDo.get(0);

					if (object instanceof Report) {

						newReport = true;
						Report o = (Report) object;

						if (o.group.length() > 0) {
							Group g = loadGroup(o.group);
							ArrayList<Event> events = g.events;
							for (int i = 0; i < events.size(); i++) {
								Event e = events.get(i);
								if (e.ID.equals(o.ID)) {
									e.reporters.add(o.e);
									if (!world.reported.contains(e)) {
										world.reported.add(e);
									}
								}
							}

							saveGroup(g);
						} else {
							ArrayList<Event> events = world.getAround(o.lat, o.lng).get(0).events;
							for (int i = 0; i < events.size(); i++) {
								System.out.println("Searching");
								Event e = events.get(i);
								if (e.ID.equals(o.ID)) {

									e.reporters.add(o.e);
									if (!world.reported.contains(e)) {
										world.reported.add(e);
									}
								}
							}
						}
					}
					
					if (object instanceof Event) {
						Event o = (Event) object;
						if (o.group.length() == 0) {
							world.addToClosest(o);

						} else {
							Group g = loadGroup(o.group);
							g.events.add(o);
							saveGroup(g);
						}

						giveXp(o.owner, 2);

					}
					
					if (object instanceof Comment) {
						Comment o = (Comment) object;
						if (o.group.length() > 0) {
							Group g = loadGroup(o.group);
							ArrayList<Event> events = g.events;
							for (int i = 0; i < events.size(); i++) {
								if (events.get(i).ID.equals(o.ID)) {
									Event e = events.get(i);
									e.comments.add(o.c);
									giveXp(o.e, 2);
								}
							}
							saveGroup(g);
						} else {
							ArrayList<Event> events = world.getAround(o.lat, o.lng).get(0).events;
							for (int i = 0; i < events.size(); i++) {
								if (events.get(i).ID.equals(o.ID)) {
									Event e = events.get(i);
									e.comments.add(o.c);
									giveXp(o.e, 2);
								}
							}

						}
					}

					if (object instanceof UpVote) {
						UpVote o = (UpVote) object;
						if (o.group.length() > 0) {
							Group g = loadGroup(o.group);
							ArrayList<Event> events = g.events;
							for (int i = 0; i < events.size(); i++) {
								if (events.get(i).ID.equals(o.ID)) {
									Event e = events.get(i);
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
							}
							saveGroup(g);
						} else {
							ArrayList<Event> events = world.getAround(o.lat, o.lng).get(0).events;
							for (int i = 0; i < events.size(); i++) {
								if (events.get(i).ID.equals(o.ID)) {
									Event e = events.get(i);
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
							}

						}
					}

					if (object instanceof DownVote) {
						DownVote o = (DownVote) object;
						if (o.group.length() > 0) {
							Group g = loadGroup(o.group);
							ArrayList<Event> events = g.events;

							for (int i = 0; i < events.size(); i++) {
								if (events.get(i).ID.equals(o.ID)) {
									Event e = events.get(i);
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
							}
							saveGroup(g);
						} else {
							ArrayList<Event> events = world.getAround(o.lat, o.lng).get(0).events;
							for (int i = 0; i < events.size(); i++) {
								if (events.get(i).ID.equals(o.ID)) {
									Event e = events.get(i);
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
							}

						}
					}
					
					if(object instanceof Remove){
						Remove o = (Remove)object;
						removeEvent(o.e,world);
						if(world.reported.contains(o.e)){
							world.reported.remove(o.e);
						}
					}
					
					if(object instanceof Allow){
						Allow o = (Allow)object;
						if(world.reported.contains(o.e)){
							world.reported.remove(o.e);
						}
					}
					
					stuffToDo.remove(0);
					right.printConsole("\t-Stuff to do: " + stuffToDo.size());
				}
				right.printConsole("\t-Stuff took: "+ (System.currentTimeMillis()-st)+" ms");
				right.printConsole("\t-Setting World");
				st = System.currentTimeMillis();
				setWorld(world);
				right.printConsole("\t-Set took: " + (System.currentTimeMillis()-st)+" ms");
				if(needSave){
				right.printConsole("\t-Saving World");
				st = System.currentTimeMillis();
				saveWorld(world);
				right.printConsole("\t-Save took: " + (System.currentTimeMillis()-st)+" ms");
				if((System.currentTimeMillis()-st)>45000){
					System.out.println("Exiting because slow save");
					System.exit(0);
				}
				}
				right.printConsole("[LOAD]-World Loaded");
				

				send(new ConnectServ());
				}
				else{
					right.printConsole("[ERROR]-World Loading FAILED world was null");
					right.printConsole("\t-Saving World to cover up null");
					long st = System.currentTimeMillis();
					saveWorld(world);
					right.printConsole("\t-Save took: " + (System.currentTimeMillis()-st)+" ms");
				}
			}
		};
		t2.schedule(ta2, 0, 60000);

		runServer();
	}


	protected void setWorld(World world2) {
		if(world2 != null){
		world.eventsInStorage=world2.eventsInStorage;
		ArrayList<Datapoint> pts = (ArrayList<Datapoint>) world2.points.clone();
		Collections.copy(pts, world2.points);
		world.points=pts;
		world.radius=new Float(world2.radius);
		world.refX = new Float(world2.refX);
		world.refY = new Float(world2.refY);
		ArrayList<Event> rep = (ArrayList<Event>) world2.reported.clone();
		Collections.copy(rep, world2.reported);
		world.reported = rep;
		}
	}


	protected void giveXp(String e, int i) {
		Account a = loadAccount(e);
		a.xp += i;
		if (a.xp < 5)
			a.xp = 5;
		saveAccount(a);

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

					if (o.group.length() > 0) {
						Group g = loadGroup(o.group);
						ArrayList<Event> events = g.events;
						for (int i = 0; i < events.size(); i++) {
							Event e = events.get(i);
							if (e.ID.equals(o.ID)) {
								e.reporters.add(o.e);
								if (!world.reported.contains(e)) {
									world.reported.add(e);
								}
							}
						}

						saveGroup(g);
					} else {
						stuffToDo.add(object);
						ArrayList<Event> events = world.getAround(o.lat, o.lng).get(0).events;
						for (int i = 0; i < events.size(); i++) {
							System.out.println("Searching");
							Event e = events.get(i);
							if (e.ID.equals(o.ID)) {

								e.reporters.add(o.e);
								if (!world.reported.contains(e)) {
									world.reported.add(e);
								}
							}
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
						stuffToDo.add(object);
						world.addToClosest(o);

					} else {
						Group g = loadGroup(o.group);
						g.events.add(o);
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
					if (o.group.length() > 0) {
						Group g = loadGroup(o.group);
						ArrayList<Event> events = g.events;
						for (int i = 0; i < events.size(); i++) {
							if (events.get(i).ID.equals(o.ID)) {
								Event e = events.get(i);
								e.comments.add(o.c);
								giveXp(o.e, 2);
							}
						}
						saveGroup(g);
					} else {
						stuffToDo.add(object);
						ArrayList<Event> events = world.getAround(o.lat, o.lng).get(0).events;
						for (int i = 0; i < events.size(); i++) {
							if (events.get(i).ID.equals(o.ID)) {
								Event e = events.get(i);
								e.comments.add(o.c);
								giveXp(o.e, 2);
							}
						}

					}
				}

				if (object instanceof UpVote) {
					UpVote o = (UpVote) object;
					if (o.group.length() > 0) {
						Group g = loadGroup(o.group);
						ArrayList<Event> events = g.events;
						for (int i = 0; i < events.size(); i++) {
							if (events.get(i).ID.equals(o.ID)) {
								Event e = events.get(i);
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
						}
						saveGroup(g);
					} else {
						stuffToDo.add(object);
						ArrayList<Event> events = world.getAround(o.lat, o.lng).get(0).events;
						for (int i = 0; i < events.size(); i++) {
							if (events.get(i).ID.equals(o.ID)) {
								Event e = events.get(i);
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
						}

					}
				}

				if (object instanceof DownVote) {
					DownVote o = (DownVote) object;
					if (o.group.length() > 0) {
						Group g = loadGroup(o.group);
						ArrayList<Event> events = g.events;

						for (int i = 0; i < events.size(); i++) {
							if (events.get(i).ID.equals(o.ID)) {
								Event e = events.get(i);
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
						}
						saveGroup(g);
					} else {
						stuffToDo.add(object);
						ArrayList<Event> events = world.getAround(o.lat, o.lng).get(0).events;
						for (int i = 0; i < events.size(); i++) {
							if (events.get(i).ID.equals(o.ID)) {
								Event e = events.get(i);
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
						}

					}
				}

				if (object instanceof Refresh) {
					Refresh o = (Refresh) object;
					ArrayList<Event> events = new ArrayList<Event>();
					ArrayList<Datapoint> points = world.getAround(o.lat, o.lng);
					Date d = new Date();
					for (Datapoint p : points) {
						for (int i = 0; i < p.events.size(); i++) {
							if (p.events.get(i).end.before(d)
									|| p.events.get(i).upVote.size() - p.events.get(i).downVote.size() < -5) {
								p.events.remove(i);
								world.eventsInStorage--;
							} else {
								events.add(p.events.get(i));
							}
						}
					}

					for (Event e : events) {
						sendEvent(connection, e, o.e);
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
					Group g = a.groups.get(i);
					boolean sent = false;
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

	public void saveAccount(Account a) {

		String loc = a.e;
		String dir = "";
		// System.out.println(a.e);
		String[] split = loc.split("@")[1].split("\\.");
		// System.out.println(split.length);
		dir = split[split.length - 2] + "_" + split[split.length - 1];
		loc = loc.replace('.', '_');
		loc = loc.replace('@', '_');
		loc += ".profile";
		File directory = new File(path + dir);
		if (!directory.exists()) {
			directory.mkdir();
		}
		File f = new File(path + dir + "\\" + loc);

		try {
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(a);
			oos.close();
		} catch (Exception e) {
			right.printConsole("[Error]-Account save error");
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
			File f = new File(path + dir + "\\" + loc);
			if (f.exists()) {

				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);
				result = (Account) ois.readObject();
				ois.close();
			}
		} catch (Exception e) {
			right.printConsole("[Error]-Account load error");
			// e.printStackTrace();
		}
		return result;
	}

	public void saveWorld(World w) {
		//world = w;
		File directory = new File(path);
		if (!directory.exists()) {
			directory.mkdir();
		}
		File f = new File(path + "world.uPWorld");
		while (isLoad) {
		}
		try {
			isSave = true;
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(w);
			oos.close();
			fos.close();
			isSave = false;
		} catch (Exception e) {
			e.printStackTrace();
			right.printConsole("[Error]-World save error");
			//System.exit(0);
		}
	}

	public World loadWorld() {
		World result = null;
		try {
			File f = new File(path + "world.uPWorld");
			while (isSave) {
			}
			if (f.exists()) {
				isLoad = true;
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);
				result = (World) ois.readObject();
				ois.close();
				fis.close();
				isLoad = false;
			} else {
				result = new World();
				result.init();
				saveWorld(result);
			}

		} catch (Exception e) {

			// right.printConsole("[Error]-World load error");
		}
		return result;
	}

	public void saveGroup(Group g) {
		File directory = new File(path + "Groups\\");
		if (!directory.exists()) {
			directory.mkdir();
		}
		File f = new File(
				path + "Groups\\" + g.name.replace(" ", "") + g.owner.replace(".", "_").replace("@", "_") + ".uPGroup");

		try {
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(g);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
			right.printConsole("[Error]-Group save error");
		}
	}

	public Group loadGroup(String id) {
		Group result = null;
		try {
			File f = new File(path + "Groups\\" + id + ".uPGroup");

			if (f.exists()) {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);
				result = (Group) ois.readObject();
				ois.close();

			}

		} catch (Exception e) {

			// right.printConsole("[Error]-World load error");
		}
		return result;
	}

	public static void main(String[] args) {
		new UPServer();
	}

	public void removeEvent(Event e,World world) {
		if (e.group.length() > 0) {
			Group group = loadGroup(e.group);
			for (int i = 0; i < group.events.size(); i++) {
				if (group.events.get(i).ID.equals(e.ID)) {
					group.events.remove(i);
				}
			}
			saveGroup(group);
		} else {
			ArrayList<Event> evs = world.getAround(e.lat, e.lng).get(0).events;
			for (int i = 0; i < evs.size(); i++) {
				if (evs.get(i).ID.equals(e.ID)) {
					evs.remove(i);
					world.eventsInStorage--;
				}

			}
		}

		saveWorld(world);

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

}
