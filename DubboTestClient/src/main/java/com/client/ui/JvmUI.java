package com.client.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class JvmUI extends JPanel{
	
	private static JLabel jl=new JLabel("this is jvm!");
	public JvmUI() {
		this.add(BorderLayout.NORTH,jl);
	}
}
