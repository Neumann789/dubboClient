package com.dubbo.client.test;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
 
/**
 * Java 中GridBagLayout布局管理器的小例子
 *
 * @author 五斗米 <如转载请保留作者和出处>
 * @blog http://blog.csdn.net/mq612
 */
 
public class Test extends JFrame {
 
 private static final long serialVersionUID = -2397593626990759111L;
 
 private JPanel pane = null;
 
 private JButton b_1 = null, b_2 = null, b_3 = null, b_4 = null, b_5 = null;
 
 private int gridx, gridy, gridwidth, gridheight, anchor, fill, ipadx, ipady;
 
 private double weightx, weighty;
 
 private Insets insert = null;
 
 private GridBagLayout gbl = null;
 
 private GridBagConstraints gbc = null;
 
 public Test() {
  super("Test");
 
  // 设置按钮字体
  UIManager.put("Button.font", new Font("Dialog", Font.PLAIN, 12));
 
  pane = new JPanel();
 
  // 五个按钮，按钮名为X、Y坐标，横与列所占单元格数
  b_1 = new JButton("X0Y0W1H3");
  b_2 = new JButton("X1Y0W1H2");
  b_3 = new JButton("X2Y1W1H1");
  b_4 = new JButton("X3Y2W1H1");
  b_5 = new JButton("X0Y3W2H1");
 
  gbl = new GridBagLayout();
  pane.setLayout(gbl);
 
  gridx = 0; // X0
  gridy = 0; // Y0
  gridwidth = 1; // 横占一个单元格
  gridheight = 3; // 列占三个单元格
  weightx = 1.0; // 当窗口放大时，长度随之放大
  weighty = 1.0; // 当窗口放大时，高度随之放大
  anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
  fill = GridBagConstraints.BOTH; // 当有剩余空间时，填充空间
  insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
  ipadx = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
  ipady = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
  gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
  gbl.setConstraints(b_1, gbc);
  pane.add(b_1);
 
  /**
   * 下面每个单元格的设置中都有很多设置是前面已经设好的，不必要重新附值，这么写是为了更容易的看懂每个单元格中的设置。
   */
 
  gridx = 1; // X1
  gridy = 0; // Y0
  gridwidth = 1; // 横占一个单元格
  gridheight = 2; // 列占两个单元格
  weightx = 1.0; // 当窗口放大时，长度随之放大
  weighty = 1.0; // 当窗口放大时，高度随之放大
  anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
  fill = GridBagConstraints.BOTH; // 当格子有剩余空间时，填充空间
  insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
  ipadx = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
  ipady = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
  gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
  gbl.setConstraints(b_2, gbc);
  pane.add(b_2);
 
  // 下面是一个临时的填充面板，否则格子有可能会被挤掉
  gridx = 1; // X1
  gridy = 2; // Y2
  gridwidth = 1; // 横占一个单元格
  gridheight = 1; // 列占一个单元格
  weightx = 0.0; // 当窗口放大时，长度不变
  weighty = 0.0; // 当窗口放大时，高度不变
  anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
  fill = GridBagConstraints.BOTH; // 当格子有剩余空间时，填充空间
  insert = new Insets(0, 0, 0, 0); // 组件彼此的间距
  ipadx = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
  ipady = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
  gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
  JPanel tempPane = new JPanel();
  gbl.setConstraints(tempPane, gbc);
  pane.add(tempPane);
 
  // 又是一个临时的填充面板
  gridx = 2; // X2
  gridy = 0; // Y0
  gridwidth = 1; // 横占一个单元格
  gridheight = 1; // 列占一个单元格
  weightx = 0.0; // 当窗口放大时，长度不变
  weighty = 0.0; // 当窗口放大时，高度不变
  anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
  fill = GridBagConstraints.BOTH; // 当格子有剩余空间时，填充空间
  insert = new Insets(0, 0, 0, 0); // 组件彼此的间距
  ipadx = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
  ipady = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
  gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
  tempPane = new JPanel();
  gbl.setConstraints(tempPane, gbc);
  pane.add(tempPane);
 
  gridx = 2; // X2
  gridy = 1; // Y1
  gridwidth = 1; // 长度为1
  gridheight = 1; // 高度为1
  weightx = 1.0; // 当窗口放大时，长度随之放大
  weighty = 0.0; // 当窗口放大时，高度不变
  anchor = GridBagConstraints.SOUTH; // 当组件没有空间大时，使组件处于底部
  fill = GridBagConstraints.HORIZONTAL; // 当有剩余空间时，横向填充空间
  insert = new Insets(0, 0, 0, 0); // 组件彼此的间距
  ipadx = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
  ipady = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
  gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
  gbl.setConstraints(b_3, gbc);
  pane.add(b_3);
 
  // 又是一个临时的填充面板
  gridx = 3; // X3
  gridy = 0; // Y0
  gridwidth = 1; // 横占一个单元格
  gridheight = 2; // 列占两个单元格
  weightx = 0.0; // 当窗口放大时，长度不变
  weighty = 0.0; // 当窗口放大时，高度不变
  anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
  fill = GridBagConstraints.BOTH; // 当格子有剩余空间时，填充空间
  insert = new Insets(0, 0, 0, 0); // 组件彼此的间距
  ipadx = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
  ipady = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
  gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
  tempPane = new JPanel();
  gbl.setConstraints(tempPane, gbc);
  pane.add(tempPane);
 
  gridx = 3; // X3
  gridy = 2; // Y2
  gridwidth = 1; // 长度为1
  gridheight = 2; // 高度为2
  weightx = 0.0; // 当窗口放大时，长度没有变化
  weighty = 1.0; // 当窗口放大时，高度随之放大
  anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处于顶部
  fill = GridBagConstraints.VERTICAL; // 当有剩余空间时，纵向填充空间
  insert = new Insets(0, 0, 0, 0); // 组件彼此的间距
  ipadx = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
  ipady = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
  gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
  gbl.setConstraints(b_4, gbc);
  pane.add(b_4);
 
  gridx = 0; // X0
  gridy = 3; // Y3
  gridwidth = 2; // 长度为2
  gridheight = 1; // 高度为1
  weightx = 1.0; // 当窗口放大时，长度随之放大
  weighty = 0.0; // 当窗口放大时，高度没有变化
  anchor = GridBagConstraints.SOUTH; // 当组件没有空间大时，使组件处于底部
  fill = GridBagConstraints.HORIZONTAL; // 当有剩余空间时，横向填充空间
  insert = new Insets(0, 0, 0, 0); // 组件彼此的间距
  ipadx = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
  ipady = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
  gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
  gbl.setConstraints(b_5, gbc);
  pane.add(b_5);
 
  this.getContentPane().add(pane);
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  this.setSize(450, 180);
  this.setLocationRelativeTo(null);
  this.setVisible(true);
 }
 
 public static void main(String args[]) {
  new Test();
 }
 
}
