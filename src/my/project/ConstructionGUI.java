package my.project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
	
	public DropPanel constraintsPanel = new DropPanel();

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
		Border border = new LineBorder(Color.GRAY);
		DropPanel constructionPanel = new DropPanel();
		constructionPanel.setBorder(border);
		JPanel constraintsMainPanel = new JPanel();
		constraintsMainPanel.setLayout(new BoxLayout(constraintsMainPanel, BoxLayout.PAGE_AXIS));
		constraintsMainPanel.setPreferredSize(new Dimension(100, 50));
		constraintsPanel.setBorder(border);
		constraintsPanel.setLayout(new FlowLayout());
		constraintsMainPanel.add(constraintsPanel);
		JPanel operationsPanel = new JPanel(new BorderLayout());

		operationsPanel.add(new JLabel("Operations you can use:"), BorderLayout.NORTH);
		operationsPanel.setLayout(new BoxLayout(operationsPanel, BoxLayout.LINE_AXIS));
		Box operationsBox = Box.createHorizontalBox();
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
		DragableLabel all = new DragableLabel("all", constraintsPanel);
		operationsBox.add(all);

		Component[] boxesComponents = operationsBox.getComponents();
		for (int i = 0; i < boxesComponents.length; i++) {
			DragableLabel label = (DragableLabel) boxesComponents[i];
			label.setPreferredSize(new Dimension(30, 30));
			label.setBorder(border);
			label.setMinimumSize(new Dimension(20, 20));
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
		JTextField yourText = new JTextField();
		yourText.setPreferredSize(new Dimension(50, 30));
		yourText.setMaximumSize(new Dimension(200, 20));
		yourText.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				DragableLabel label = new DragableLabel(yourText.getText(), constructionPanel);
				Border border = new LineBorder(Color.GRAY);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setBorder(border);
				// label.setPreferredSize(new Dimension(30, 30));
				label.setIsDropped(true);
				constructionPanel.add(label);

				constructionPanel.revalidate();
				constructionPanel.repaint();
			}
		});
		yourText.setBorder(border);
		fieldsPanel.add(yourText);
		fieldsPanel.revalidate();
		fieldsPanel.repaint();
		
		JButton add = new JButton("Add new line");
		
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DropPanel dropConstraintsPanel = new DropPanel();
				dropConstraintsPanel.setBorder(border);
				dropConstraintsPanel.setLayout(new FlowLayout());
				constraintsMainPanel.add(dropConstraintsPanel);
				constraintsMainPanel.repaint();
				constraintsPanel = dropConstraintsPanel;
			}
			
		});
		
		JPanel toDoPanel = new JPanel(new FlowLayout());
		toDoPanel.setPreferredSize(new Dimension(100, 100));
		JLabel toDo = new JLabel("Choose what to do: Create new");
		Class<?>[] classes = ReadWriteFiles.getClasses("com.rws.data", true);
		JComboBox<String> storeBox = new JComboBox<>();
		for (Class<?> className : classes) {
			JLabel label = new JLabel(className.getSimpleName());
			label.setBorder(border);
			storeBox.addItem(label.getText());
		}

		JScrollPane storeScrollPane = new JScrollPane(storeBox);
		storeScrollPane.setBorder(border);
		toDoPanel.add(toDo);
		toDoPanel.add(storeScrollPane);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(constructionLabel)
						.addComponent(constructionPanel).addComponent(fieldsPanel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(constraintLabel)
						.addGroup(layout.createSequentialGroup().addComponent(constraintsMainPanel).addComponent(add)).addComponent(operationsPanel))
				.addComponent(toDoPanel));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(constructionLabel)
						.addComponent(constraintLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(constructionPanel)
						.addComponent(constraintsMainPanel).addComponent(add)
						.addComponent(toDoPanel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(fieldsPanel)
						.addComponent(operationsPanel)));

		this.add(panel, BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		buttonsPanel.add(cancelButton);
		JButton okButton = new JButton("Ok");

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Component[] constructionComponents = constructionPanel.getComponents();
				//System.out.println(((DragableLabel) constructionComponents[0]).getText());
				Component[] constraintsPanels = constraintsMainPanel.getComponents();
				List<String> constraintsList = new ArrayList<String>(constraintsPanels.length);
				for (int i = 0; i < constraintsPanels.length; i++) {					
					Component[] constraints = ((DropPanel)constraintsPanels[i]).getComponents();
					String constraint = "";
					for (int j = 0; j < constraints.length; j++) {
						if (j != 0) {
							constraint = constraint + " ";
						}
						constraint = constraint + ((DragableLabel)constraints[j]).getText();						
						//System.out.println(((DragableLabel)constraints[j]).getText());
					}
					constraintsList.add(constraint);
				}
				ProcessRule.createRule(frameName, constraintsList);
			}
		});

		buttonsPanel.add(okButton);

		this.add(buttonsPanel, BorderLayout.LINE_END);
		this.pack();
	}

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>(2);
		list.add("trainID");
		list.add("trainLength");
		list.add("trainType");
		list.add("railwayID");
		list.add("railwayTotalLength");
		list.add("railwayUsefulLength");
		ConstructionGUI cgui = new ConstructionGUI("Move", list);
	}
}
