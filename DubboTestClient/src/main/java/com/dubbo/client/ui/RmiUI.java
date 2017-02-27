package com.dubbo.client.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class RmiUI extends JPanel{
	
	private static JLabel jl=new JLabel("this is rmi!");
	public RmiUI() {
		this.add(BorderLayout.NORTH,jl);
	}
}
