package com.client.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ZKUI extends JPanel{
	
	private static JLabel jl=new JLabel("this is zk!");
	public ZKUI() {
		this.add(BorderLayout.NORTH,jl);
	}
}
