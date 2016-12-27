package com.ggi.uparty.loadbalance;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class GridPanel extends JPanel{
	public GridPanel(){
		this.setLayout(new GridLayout(0,1,3,3));
		this.setBackground(Color.black);
		this.setForeground(Color.orange);
	}
}
