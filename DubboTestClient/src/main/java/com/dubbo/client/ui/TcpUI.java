package com.dubbo.client.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TcpUI extends JPanel{
	
	private static JLabel jl=new JLabel("this is tcp!");
	public TcpUI() {
		this.add(BorderLayout.NORTH,jl);
	}
}
