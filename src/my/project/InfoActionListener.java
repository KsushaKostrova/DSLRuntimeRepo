package my.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class InfoActionListener implements ActionListener {
	String className = "";
	Map<String, String> fields;

	public InfoActionListener(String className, Map<String, String> fields) {
		this.className = className;
		this.fields = fields;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new AddInfoGUI(className, fields);
	}
}
