package com.ggi.uparty.loadbalance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.ggi.uparty.network.RefreshServ;

public class ServPanel extends JPanel{
	
	public JTextArea stats = new JTextArea();
	public JButton ref = new JButton("Refresh");
	public ScrollPane scroll = new ScrollPane();
	
	public UPLoad u;

	public ServPanel(UPLoad u){
		this.u=u;
		this.setBackground(Color.black);
		this.setForeground(Color.orange);
		this.setSize(200,400);
		
		stats.setBackground(Color.black);
		stats.setForeground(Color.orange);
		stats.setEditable(false);
		
		scroll.setBackground(Color.black);
		scroll.setForeground(Color.orange);
		scroll.setSize(200, 400);
		
		scroll.add(stats);
		
		ref.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent a) {
				u.server.sendToAllTCP(new RefreshServ());
				
			}
			
		});
		this.setLayout(new BorderLayout());
		this.add(scroll,BorderLayout.CENTER);
		this.add(ref, BorderLayout.SOUTH);
	}
	
	public void repaint(){
		super.repaint();
		String sts = "Servers: ";
		if(u!=null){
			System.out.println("U Check");
			System.out.println(u.servs.size());
			if(u.servs!=null &&u.servs.size()>0){
		for(int i = 0; i < u.servs.size(); i++){
			if(u.servs.get(i).c != null){
			sts += "\n" + u.servs.get(i).c.getRemoteAddressTCP().getHostString()+" has " + u.servs.get(i).cons +" connections.";
			}
			}
			}
		}
		if(stats!=null){
			System.out.println("UPD Check");
		stats.setText(sts);}
	}
	
}
