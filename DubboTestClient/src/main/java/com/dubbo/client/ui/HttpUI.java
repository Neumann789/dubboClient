package com.dubbo.client.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class HttpUI extends JPanel{
	
	private static JLabel jl=new JLabel("this is http!");
	public HttpUI() {
		this.add(BorderLayout.NORTH,jl);
	}
}
