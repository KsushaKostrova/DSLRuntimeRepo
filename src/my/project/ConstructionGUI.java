package my.project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class ConstructionGUI extends JFrame {

	public ConstructionGUI(String frameName, List<String> fields) {
		
		this.setTitle(frameName);
		this.setVisible(true);
		this.setPreferredSize(new Dimension(1050, 300));
		
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JLabel constructionLabel = new JLabel("Construction");
		JLabel constraintLabel = new JLabel("Corresponding constraints");
		DropPanel constructionPanel = new DropPanel();		
		DropPanel constraintsPanel = new DropPanel();
		JPanel operationsPanel = new JPanel(new BorderLayout());

		operationsPanel.add(new JLabel("Operations you can use:"));
		operationsPanel.setLayout(new BoxLayout(operationsPanel, BoxLayout.LINE_AXIS));
		Box operationsBox = Box.createHorizontalBox();
		Border border = new LineBorder(Color.GRAY);
		DragableLabel plus = new DragableLabel("+", constraintsPanel);
		operationsBox.add(plus);
		DragableLabel minus = new DragableLabel("-", constraintsPanel);
		operationsBox.add(minus);
		DragableLabel multiply = new DragableLabel("*", constraintsPanel);
		operationsBox.add(multiply);
		DragableLabel divide = new DragableLabel("/", constraintsPanel);
		operationsBox.add(divide);
		DragableLabel leftParenthesis = new DragableLabel("(", constraintsPanel);
		operationsBox.add(leftParenthesis);
		DragableLabel rightParenthesis = new DragableLabel(")", constraintsPanel);
		operationsBox.add(rightParenthesis);
		DragableLabel less = new DragableLabel("<", constraintsPanel);
		operationsBox.add(less);
		DragableLabel equal = new DragableLabel("=", constraintsPanel);
		operationsBox.add(equal);
		DragableLabel more = new DragableLabel(">", constraintsPanel);
		operationsBox.add(more);
		
		Component[] boxesComponents = operationsBox.getComponents();
		for (int i = 0; i < boxesComponents.length; i++) {
			DragableLabel label = (DragableLabel) boxesComponents[i];
			label.setPreferredSize(new Dimension(30, 30));
			label.setBorder(border);
			label.setMinimumSize(new Dimension(20,20));
			label.setHorizontalAlignment(JLabel.CENTER);
		}
		
		operationsPanel.add(operationsBox);
		operationsPanel.revalidate();
		operationsPanel.repaint();
		
		JPanel fieldsPanel = new JPanel();

		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
		Box box = Box.createVerticalBox();
		
		for (String fieldName : fields) {
			DragableLabel label = new DragableLabel(fieldName, constructionPanel);
			label.setBorder(border);
			box.add(label);
		}
		
		JScrollPane jscrlBox = new JScrollPane(box);
		fieldsPanel.add(new JLabel("Fields available:"));
		fieldsPanel.add(jscrlBox);
		fieldsPanel.add(new JLabel("Add some text"));
		fieldsPanel.add(new DragableLabel("lala", constructionPanel));
		fieldsPanel.revalidate();
		fieldsPanel.repaint();
		
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(constructionLabel)
						.addComponent(constructionPanel)
						.addComponent(fieldsPanel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(constraintLabel)
						.addComponent(constraintsPanel)
						.addComponent(operationsPanel))
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(constructionLabel)
						.addComponent(constraintLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(constructionPanel)
						.addComponent(constraintsPanel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(fieldsPanel)
						.addComponent(operationsPanel))
		);
		
		this.add(panel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		buttonsPanel.add(cancelButton);
		JButton okButton = new JButton("Ok");
		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Component[] constructionComponents = constructionPanel.getComponents();
				System.out.println(((DragableLabel)constructionComponents[0]).getText());
			}
		});
		
		buttonsPanel.add(okButton);
		
		
		
		this.add(buttonsPanel, BorderLayout.LINE_END);
		this.pack();
	}
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>(2);
		list.add("FSFSF");
		list.add("erdhehlihUgiwugksgslgnadbgs");
		ConstructionGUI cgui = new ConstructionGUI("test", list);
	}
}
