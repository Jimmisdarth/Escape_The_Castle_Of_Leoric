package jscrollable;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JSPDemo {

	public static void main(String[] args) {

		JFrame frame = new JFrame("JScrollPane Demo");
		frame.setLayout(new FlowLayout());
		
		JTextArea ta = new JTextArea(8,20);
		
		JScrollPane sp = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		for(int i = 0; i < 10; ++i) {
			ta.append("HOLA\n");
		}
		
		frame.add(sp);
		frame.setSize(400,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
