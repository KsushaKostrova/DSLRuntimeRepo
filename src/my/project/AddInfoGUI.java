package my.project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import layout.SpringUtilities;

public class AddInfoGUI extends JFrame {

	public AddInfoGUI(String frameName, List<String> fields) {
		this.setTitle(frameName);
		this.setVisible(true);
		this.setPreferredSize(new Dimension(300, 300));

		JPanel panel = new JPanel(new SpringLayout());
		int numPairs = fields.size();
		for (int i = 0; i < numPairs; i++) {
			JLabel label = new JLabel(fields.get(i) + ": ", JLabel.TRAILING);
			panel.add(label);
			JTextField textField = new JTextField(10);
			label.setLabelFor(textField);
			panel.add(textField);
		}
		
		SpringUtilities.makeCompactGrid(panel, numPairs, 2, 6, 6, 6, 6);

		this.add(panel);
		this.pack();
	}
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>(0);
		list.add("trainId");
		list.add("startNick");
		list.add("SDGA");
		list.add("SDGA");
		list.add("SDGA");
		list.add("SDGA");
		list.add("SDGA");
		new AddInfoGUI("Add information", list);
	}
}
