package my.project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.rws.data.TrainInfo;

import layout.SpringUtilities;

public class AddInfoGUI extends JFrame {
	
	Map<String, String> fields;
	String className;

	public AddInfoGUI(String className, Map<String, String> fields) {
		this.fields = fields;
		this.className = className;
		this.setTitle(className);
		this.setVisible(true);
		this.setPreferredSize(new Dimension(300, 300));
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel(new SpringLayout());
		List<String> classFields = new ArrayList<String>(0);
		for (Entry<String, String> entry : fields.entrySet()) {
			if (entry.getValue().equals(className)) {
				classFields.add(entry.getKey());
			}
		}
		int numPairs = classFields.size();
		for (int i = 0; i < numPairs; i++) {
			JLabel label = new JLabel(classFields.get(i) + ": ", JLabel.TRAILING);
			panel.add("label", label);
			JTextField textField = new JTextField(10);
			label.setLabelFor(textField);
			panel.add("textField", textField);
		}
		
		SpringUtilities.makeCompactGrid(panel, numPairs, 2, 6, 6, 6, 6);
		JButton okButton = new JButton("OK");
		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				List<String> fieldsFromFile = new ArrayList<String>(0);
				Class<?> classInstance;
				try {
					classInstance = Class.forName("com.rws.data."+className);
					Field[] classesFields = classInstance.getDeclaredFields();
					Constructor<?> constructor = classInstance.getConstructor();
					Object object = constructor.newInstance();					
					int pos = 1;
					for (int j = 0; j < classesFields.length; j++, pos=pos+2) {
						System.out.println("field is " + classesFields[j].getName());
						System.out.println("field type is " + classesFields[j].getType());
						
						String methodName = classesFields[j].getName();
						String firstLetter = String.valueOf(methodName.charAt(0)).toUpperCase();
						methodName = firstLetter + methodName.substring(1);
						Method setMethod = classInstance.getDeclaredMethod("set"+methodName, new Class[] {Integer.class});
						Object o = classInstance.newInstance();
						setMethod.invoke(o, Integer.valueOf(((JTextField)panel.getComponent(pos)).getText()));
//						TrainInfo trainInfo = (TrainInfo) classInstance;
//						Method getMethod = classInstance.getDeclaredMethod("get"+methodName, new Class[] {});
//						System.out.println((Integer)getMethod.invoke(o));
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		this.add(panel, BorderLayout.CENTER);
		this.add(okButton, BorderLayout.PAGE_END);
		this.pack();
	}
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>(0);
		list.add("trainId");
		list.add("startNick");
		list.add("SDGA");
		//new AddInfoGUI("Add information", list);
	}
}
