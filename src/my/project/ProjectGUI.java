package my.project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.chocosolver.solver.Model;

@SuppressWarnings("serial")
public class ProjectGUI extends JFrame implements Serializable {

	static String user = "ksu";
	static String password = "ksupass";
	static Connection conn;
	static String defaultDb = "project_information";
	static String putPatternsFile = "src\\files\\putPatterns";
	static String relocatePatternsFile = "src\\files\\relocatePatterns";
	static String moveBackPatternsFile = "src\\files\\moveBackPatterns";
	static String moveForwardPatternsFile = "src\\files\\moveForwardPatterns";
	static ExternalParser extP = new ExternalParser();
	static String putInstruction = "put train 1 on 1 with 0";
	static String moveInstructionBack = "move back 1 by 10";
	static String moveInstructionForward = "move forward 1 by 10";
	static String relocateInstruction = "relocate train 1 to 2 with 0";
	JTextArea instruction = new JTextArea("Write here a request. Please, look at the examples:" + "\n●" + putInstruction
			+ " - to put train\n●" + relocateInstruction + " - to relocate train" + "\n●" + moveInstructionBack
			+ " - to move train back\n●" + moveInstructionForward + " - to move train forward");

	transient JFrame mainFrame = new JFrame("Service");
	transient JPanel visualization = new JPanel();
	// JPanel mainPanel = new JPanel(new BorderLayout());
	transient JPanel mainPanel = null;
	transient GridBagConstraints c = new GridBagConstraints();

	private List<String> fields = new ArrayList<String>();
	private Map<String, ArrayList<Object>> operationsMap = new HashMap<String, ArrayList<Object>>(0);
	

	public ProjectGUI() {
		readFields();

		mainFrame.setVisible(true);
		mainFrame.setPreferredSize(new Dimension(1800, 700));
		MenuBar menu = new MenuBar();
		mainFrame.setMenuBar(menu);
		Menu settings = new Menu("Settings");
		settings.setFont(new Font("Comic Sans MS", 30, 22));

		Menu addInfo = new Menu("Add information");
		addInfo.setFont(new Font("Comic Sans MS", 30, 22));

		menu.add(settings);
		menu.add(addInfo);
		MenuItem setTrain = new MenuItem("Set trains");
		setTrain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame addTrainInfo = new JFrame("Train");
				addTrainInfo.setVisible(true);
				addTrainInfo.setPreferredSize(new Dimension(300, 450));
				addTrainInfo.setResizable(false);
				JTextField location = new JTextField("location");
				location.setEditable(false);
				JTextField locationText = new JTextField("");
				JTextField size = new JTextField("size");
				size.setEditable(false);
				JTextField sizeText = new JTextField("");

				GridLayout gl = new GridLayout(2, 2);
				// gl.addLayoutComponent("location", location);
				// gl.addLayoutComponent("locationText", locationText);
				// gl.addLayoutComponent("size", size);
				// gl.addLayoutComponent("sizeText", sizeText);

				addTrainInfo.setLayout(gl);
				addTrainInfo.add(location);
				addTrainInfo.add(locationText);
				addTrainInfo.add(size);
				addTrainInfo.add(sizeText);
				addTrainInfo.setLocation(350, 350);
				addTrainInfo.pack();
			}
		});
		addInfo.add(setTrain);

		MenuItem dbSettings = new MenuItem("Database settings");
		dbSettings.setFont(new Font("Comic Sans MS", 30, 22));
		settings.add(dbSettings);
		dbSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame setDb = new JFrame("Database settings");
				setDb.setVisible(true);
				setDb.setPreferredSize(new Dimension(700, 100));
				setDb.setResizable(false);
				JTextField str = new JTextField("Current database name is");
				str.setVisible(true);
				str.setEditable(false);
				str.setPreferredSize(new Dimension(200, 50));
				str.setFont(new Font("Times New Roman", 18, 18));
				JTextField dbName = new JTextField(defaultDb);
				dbName.setVisible(true);
				dbName.setEditable(true);
				dbName.setEnabled(true);
				dbName.setPreferredSize(new Dimension(200, 50));
				dbName.setFont(new Font("Times New Roman", 18, 18));
				dbName.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (dbName.getText().equals(defaultDb)) {
							dbName.setText("");
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
					}

					@Override
					public void mousePressed(MouseEvent e) {
					}

					@Override
					public void mouseReleased(MouseEvent e) {
					}
				});
				dbName.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) {
					}

					@Override
					public void focusLost(FocusEvent arg0) {
						if (dbName.getText().equals(""))
							dbName.setText(defaultDb);
					}

				});
				JButton ok = new JButton("OK");
				ok.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String connectionString = "jdbc:postgresql://localhost:5433/" + dbName.getText();
						try {
							conn = DriverManager.getConnection(connectionString, user, password);
							defaultDb = dbName.getText();
							refresh();
							setDb.dispose();
						} catch (SQLException ex) {
							JOptionPane.showMessageDialog(null, "No database with this name", "Wrong database name",
									JOptionPane.ERROR_MESSAGE);
						}

					}
				});
				ok.setPreferredSize(new Dimension(70, 50));
				setDb.setLayout(new FlowLayout());
				setDb.add(str);
				setDb.add(dbName);
				setDb.add(ok);
				setDb.setLocation(350, 350);
				setDb.pack();
			}
		});
		settings.addSeparator();
		MenuItem languageSettings = new MenuItem("Customize language");
		languageSettings.setFont(new Font("Comic Sans MS", 30, 22));
		settings.add(languageSettings);
		languageSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame changeLanguage = new JFrame("Language settings");
				changeLanguage.setVisible(true);
				changeLanguage.setPreferredSize(new Dimension(1000, 700));
				MenuBar menuLang = new MenuBar();
				changeLanguage.setMenuBar(menuLang);
				Menu construction = new Menu("Add");
				construction.setFont(new Font("Comic Sans MS", 30, 22));
				MenuItem newConstruction = new MenuItem("New construction");
				newConstruction.setFont(new Font("Comic Sans MS", 30, 22));
				newConstruction.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFrame createConstruction = new JFrame("Create new constrcution");
						createConstruction.setVisible(true);
						createConstruction.setPreferredSize(new Dimension(1050, 300));
						createConstruction.setLayout(new BorderLayout());
						JPanel constructionPanel = new JPanel();
						
						AbstractTableModel dataModel = new AbstractTableModel() {
							public int getColumnCount() {
								return 1;
							}

							public int getRowCount() {
								return fields.size()+1;
							}

							public Object getValueAt(int row, int col) {
								if (row == 0) {
									return new JTextArea("").getText();
								}
								return new JTextArea(fields.get(row-1)).getText();
							}
						};
						JTable fieldsTable = new JTable(dataModel);
						fieldsTable.setPreferredSize(new Dimension(300, 150));						
						constructionPanel.add(fieldsTable);
						JPanel resultPanel = new JPanel();
						resultPanel.add(new JTextField("Your new construction:"));
						JPanel resultTextPanel = new JPanel();
						resultPanel.add(resultTextPanel);
						
						createConstruction.add(resultPanel, BorderLayout.NORTH);
						createConstruction.add(constructionPanel, BorderLayout.SOUTH);
						createConstruction.setLocation(350, 350);
						createConstruction.pack();
					}

				});
				construction.add(newConstruction);
				MenuItem newField = new MenuItem("New field");
				newField.setFont(new Font("Comic Sans MS", 30, 22));
				newField.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFrame addField = new JFrame("Add new field");
						addField.setVisible(true);
						addField.setPreferredSize(new Dimension(230, 63));
						addField.setResizable(false);
						addField.setLayout(new FlowLayout());
						JTextField fieldName = new JTextField("");
						fieldName.setPreferredSize(new Dimension(130, 30));
						JButton ok = new JButton("OK");
						ok.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								if (fields.indexOf(fieldName.getText()) == -1) {
									fields.add(fieldName.getText());
									saveFields();
									addField.dispose();
								}
							}
						});

						addField.add(fieldName);
						addField.add(ok);

						addField.setLocation(380, 380);
						addField.pack();
					}
				});
				construction.add(newField);
				menuLang.add(construction);

				Menu pattern = new Menu("Choose existing pattern");
				pattern.setFont(new Font("Comic Sans MS", 30, 22));
				MenuItem pat = new MenuItem("Choose pattern");
				pat.setFont(new Font("Comic Sans MS", 30, 22));
				pat.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFrame choosePattern = new JFrame("Choose pattern");
						choosePattern.setVisible(true);
						choosePattern.setPreferredSize(new Dimension(1050, 300));
						choosePattern.setLayout(new FlowLayout());

						JPanel patternPanel = new JPanel();
						String[] status = null;
						try {
							BufferedReader in = new BufferedReader(new FileReader(putPatternsFile));
							String l;
							List<String> options = new ArrayList<String>(0);
							while ((l = in.readLine()) != null) {
								options.add(l);
							}
							status = new String[options.size()];
							options.toArray(status);
							for (int i = 0; i < status.length; i++)
								System.out.println(status[i]);
							in.close();
						} catch (IOException e1) {
							status = new String[] { putInstruction };
						}
						JComboBox<String> combo = new JComboBox<String>(status);
						combo.setPreferredSize(new Dimension(600, 40));
						combo.setFont(new Font("Arial", 30, 22));
						combo.setSelectedItem(putInstruction);
						JButton okP = new JButton("OK");
						okP.setPreferredSize(new Dimension(80, 40));
						okP.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e1) {
								String[] stuff = ((String) combo.getSelectedItem()).split(" ");
								ArrayList<String> arr = new ArrayList<String>(0);
								String temp = "";
								for (int i = 0; i < stuff.length; i++) {
									if (!stuff[i].equals("1") && !stuff[i].equals("0")) {
										if (!temp.equals(""))
											temp = temp + " " + stuff[i];
										else
											temp = stuff[i];
									} else {
										arr.add(temp);
										temp = "";
									}
								}
								changePutPattern(arr);
							}
						});
						JTextField p = new JTextField("choose put pattern");
						p.setEditable(false);
						p.setFont(new Font("Arial", 30, 22));
						p.setPreferredSize(new Dimension(320, 40));
						patternPanel.add(p);
						patternPanel.add(combo);
						patternPanel.add(okP);

						JPanel patternPanelRelocate = new JPanel();
						String[] statusR = null;
						try {
							BufferedReader in = new BufferedReader(new FileReader(relocatePatternsFile));
							String l;
							List<String> options = new ArrayList<String>(0);
							while ((l = in.readLine()) != null) {
								options.add(l);
							}
							statusR = new String[options.size()];
							options.toArray(statusR);
							for (int i = 0; i < statusR.length; i++)
								System.out.println(statusR[i]);
							in.close();
						} catch (IOException e1) {
							statusR = new String[] { relocateInstruction };
						}
						JComboBox<String> comboR = new JComboBox<String>(statusR);
						comboR.setPreferredSize(new Dimension(600, 40));
						comboR.setFont(new Font("Arial", 30, 22));
						comboR.setSelectedItem(relocateInstruction);
						JButton okR = new JButton("OK");
						okR.setPreferredSize(new Dimension(80, 40));
						okR.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String[] stuff = ((String) comboR.getSelectedItem()).split(" ");
								ArrayList<String> arr = new ArrayList<String>(0);
								String temp = "";
								for (int i = 0; i < stuff.length; i++) {
									if (!stuff[i].equals("1") && !stuff[i].equals("0") && !stuff[i].equals("2")) {
										if (!temp.equals(""))
											temp = temp + " " + stuff[i];
										else
											temp = stuff[i];
									} else {
										System.out.println("temp" + temp);
										arr.add(temp);
										temp = "";
									}
								}
								extP.setRelocateParser(arr);
								relocateInstruction = arr.get(0) + " " + "1" + " " + arr.get(1) + " " + "2" + " "
										+ arr.get(2) + " " + "0";
								instruction.setText(
										"Write here a request. Please, look at the examples:" + "\n●" + putInstruction
												+ " - to put train\n●" + relocateInstruction + " - to relocate train"
												+ "\n●" + moveInstructionBack + " - to move train back\n●"
												+ moveInstructionForward + " - to move train forward");
							}
						});
						JTextField r = new JTextField("choose relocate pattern");
						r.setEditable(false);
						r.setFont(new Font("Arial", 30, 22));
						r.setPreferredSize(new Dimension(320, 40));
						patternPanelRelocate.add(r);
						patternPanelRelocate.add(comboR);
						patternPanelRelocate.add(okR);

						JPanel patternPanelMoveBack = new JPanel();
						String[] statusMB = null;
						try {
							BufferedReader in = new BufferedReader(new FileReader(moveBackPatternsFile));
							String l;
							List<String> options = new ArrayList<String>(0);
							while ((l = in.readLine()) != null) {
								options.add(l);
							}
							statusMB = new String[options.size()];
							options.toArray(statusMB);
							for (int i = 0; i < statusMB.length; i++)
								System.out.println(statusMB[i]);
							in.close();
						} catch (IOException e1) {
							status = new String[] { moveInstructionBack };
						}
						JComboBox<String> comboMB = new JComboBox<String>(statusMB);
						comboMB.setPreferredSize(new Dimension(600, 40));
						comboMB.setFont(new Font("Arial", 30, 22));
						comboMB.setSelectedItem(moveInstructionBack);
						JButton okMB = new JButton("OK");
						okMB.setPreferredSize(new Dimension(80, 40));
						okMB.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String[] stuff = ((String) comboMB.getSelectedItem()).split(" ");
								ArrayList<String> arr = new ArrayList<String>(0);
								String temp = "";
								for (int i = 0; i < stuff.length; i++) {
									if (!stuff[i].equals("1") && !stuff[i].equals("10")) {
										if (!temp.equals(""))
											temp = temp + " " + stuff[i];
										else
											temp = stuff[i];
									} else {
										arr.add(temp);
										temp = "";
									}
								}
								extP.setMoveParser(arr, "back");
								moveInstructionForward = arr.get(0) + " " + arr.get(1) + " " + "1" + " " + arr.get(2)
										+ " " + "10";
								instruction.setText(
										"Write here a request. Please, look at the examples:" + "\n●" + putInstruction
												+ " - to put train\n●" + relocateInstruction + " - to relocate train"
												+ "\n●" + moveInstructionBack + " - to move train back\n●"
												+ moveInstructionForward + " - to move train forward");
							}
						});
						JTextField mb = new JTextField("choose move back pattern");
						mb.setEditable(false);
						mb.setFont(new Font("Arial", 30, 22));
						mb.setPreferredSize(new Dimension(320, 40));
						patternPanelMoveBack.add(mb);
						patternPanelMoveBack.add(comboMB);
						patternPanelMoveBack.add(okMB);

						JPanel patternPanelMoveForward = new JPanel();
						String[] statusMF = null;
						try {
							BufferedReader in = new BufferedReader(new FileReader(moveForwardPatternsFile));
							String l;
							List<String> options = new ArrayList<String>(0);
							while ((l = in.readLine()) != null) {
								options.add(l);
							}
							statusMF = new String[options.size()];
							options.toArray(statusMF);
							for (int i = 0; i < statusMF.length; i++)
								System.out.println(statusMF[i]);
							in.close();
						} catch (IOException e1) {
							status = new String[] { moveInstructionForward };
						}
						JComboBox<String> comboMF = new JComboBox<String>(statusMF);
						comboMF.setPreferredSize(new Dimension(600, 40));
						comboMF.setFont(new Font("Arial", 30, 22));
						comboMF.setSelectedItem(moveInstructionForward);
						JButton okMF = new JButton("OK");
						okMF.setPreferredSize(new Dimension(80, 40));
						okMF.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String[] stuff = ((String) comboMF.getSelectedItem()).split(" ");
								ArrayList<String> arr = new ArrayList<String>(0);
								String temp = "";
								for (int i = 0; i < stuff.length; i++) {
									if (!stuff[i].equals("1") && !stuff[i].equals("10")) {
										if (!temp.equals(""))
											temp = temp + " " + stuff[i];
										else
											temp = stuff[i];
									} else {
										arr.add(temp);
										temp = "";
									}
								}
								extP.setMoveParser(arr, "forward");
								moveInstructionForward = arr.get(0) + " " + arr.get(1) + " " + "1" + " " + arr.get(2)
										+ " " + "10";
								instruction.setText(
										"Write here a request. Please, look at the examples:" + "\n●" + putInstruction
												+ " - to put train\n●" + relocateInstruction + " - to relocate train"
												+ "\n●" + moveInstructionBack + " - to move train back\n●"
												+ moveInstructionForward + " - to move train forward");
							}
						});
						JTextField mf = new JTextField("choose move forward pattern");
						mf.setEditable(false);
						mf.setFont(new Font("Arial", 30, 22));
						mf.setPreferredSize(new Dimension(320, 40));
						patternPanelMoveForward.add(mf);
						patternPanelMoveForward.add(comboMF);
						patternPanelMoveForward.add(okMF);

						choosePattern.add(new JScrollPane(patternPanel));
						choosePattern.add(new JScrollPane(patternPanelRelocate));
						choosePattern.add(new JScrollPane(patternPanelMoveBack));
						choosePattern.add(new JScrollPane(patternPanelMoveForward));
						choosePattern.setLocation(350, 350);
						choosePattern.pack();
					}
				});
				pattern.add(pat);
				menuLang.add(pattern);
				GridBagLayout gr = new GridBagLayout();
				JPanel langPanel = new JPanel(gr);
				langPanel.setPreferredSize(new Dimension(950, 600));
				String[] putLine = putInstruction.split(" ");
				String[] relocateLine = relocateInstruction.split(" ");
				String[] moveBackLine = moveInstructionBack.split(" ");
				String[] moveForwardLine = moveInstructionForward.split(" ");
				int tempPut = 0;
				int tempRelocate = 0;
				int tempMoveBack = 0;
				int tempMoveForward = 0;

				GridBagConstraints co = new GridBagConstraints();
				co.gridx = 0;
				co.gridy = 0;
				co.weighty = 0.25;
				co.weightx = 1;

				GridBagLayout lay = new GridBagLayout();
				JPanel putP = new JPanel(lay);
				putP.setPreferredSize(new Dimension(900, 200));
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 0;
				// c.anchor = c.ABOVE_BASELINE;
				c.gridwidth = 7;
				c.weightx = 1;
				// c.anchor = c.PAGE_START;
				JTextArea putSaying = new JTextArea(
						"You can change words but this operation means to put a train. Fields with ids cannot be changed    ");
				putSaying.setPreferredSize(new Dimension(700, 30));
				putSaying.setBackground(null);
				putSaying.setEditable(false);
				putSaying.setFont(new Font("Comic Sans MS", 18, 18));
				putP.add(putSaying, c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 1;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.14;
				JTextField putT = new JTextField(putLine[0]);
				putT.setPreferredSize(new Dimension(100, 30));
				putT.setFont(new Font("Comic Sans MS", 18, 18));
				putP.add(putT, c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 1;
				c.gridy = 1;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.14;
				String tr = "";
				for (int i = 1; i < putLine.length; i++) {
					if (putLine[i].equals("1")) {
						tempPut = i;
						break;
					} else {
						if (i == 1)
							tr = putLine[i];
						else
							tr = tr + " " + putLine[i];
					}
				}
				JTextField trainT = new JTextField(tr);
				trainT.setPreferredSize(new Dimension(100, 30));
				trainT.setFont(new Font("Comic Sans MS", 18, 18));
				putP.add(trainT, c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 2;
				c.gridy = 1;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.14;
				JTextField trainIdT = new JTextField("train_id");
				trainIdT.setEditable(false);
				trainIdT.setPreferredSize(new Dimension(100, 30));
				trainIdT.setFont(new Font("Comic Sans MS", 18, 18));
				putP.add(trainIdT, c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 3;
				c.gridy = 1;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.14;
				String On = "";
				for (int i = tempPut + 1; i < putLine.length; i++) {
					if (putLine[i].equals("1")) {
						tempPut = i;
						break;
					} else {
						if (i == tempPut + 1)
							On = putLine[i];
						else
							On = On + " " + putLine[i];
					}
				}
				JTextField onT = new JTextField(On);
				onT.setEditable(true);
				onT.setPreferredSize(new Dimension(100, 30));
				onT.setFont(new Font("Comic Sans MS", 18, 18));
				putP.add(onT, c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 4;
				c.gridy = 1;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.14;
				JTextField railwayIdT = new JTextField("railway_id");
				railwayIdT.setEditable(false);
				railwayIdT.setPreferredSize(new Dimension(100, 30));
				railwayIdT.setFont(new Font("Comic Sans MS", 18, 18));
				putP.add(railwayIdT, c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 5;
				c.gridy = 1;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.14;
				String With = "";
				for (int i = tempPut + 1; i < putLine.length; i++) {
					if (putLine[i].equals("0")) {
						tempPut = i;
						break;
					} else {
						if (i == tempPut + 1)
							With = putLine[i];
						else
							With = With + " " + putLine[i];
					}
				}
				JTextField withT = new JTextField(With);
				withT.setEditable(true);
				withT.setPreferredSize(new Dimension(100, 30));
				withT.setFont(new Font("Comic Sans MS", 18, 18));
				putP.add(withT, c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 6;
				c.gridy = 1;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.16;
				JTextField startNickT = new JTextField("startNick");
				startNickT.setEditable(false);
				startNickT.setPreferredSize(new Dimension(100, 30));
				startNickT.setFont(new Font("Comic Sans MS", 18, 18));
				putP.add(startNickT, c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 2;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.2;
				JButton okPut = new JButton("Save changes");
				okPut.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							ArrayList<String> arr = new ArrayList<String>(0);
							arr.add(putT.getText() + " " + trainT.getText());
							arr.add(onT.getText());
							arr.add(withT.getText());
							changePutPattern(arr);
							BufferedWriter out = new BufferedWriter(new FileWriter(putPatternsFile, true));
							out.write("\n" + putInstruction);
							out.close();
							out.flush();
						} catch (IOException e1) {

						}
					}
				});
				okPut.setFont(new Font("Comic Sans MS", 18, 18));
				putP.add(okPut, c);
				langPanel.add(putP, co);

				co.gridx = 0;
				co.gridy = 1;
				co.weighty = 0.25;
				GridBagLayout layBMove = new GridBagLayout();
				JPanel moveBP = new JPanel(layBMove);
				moveBP.setPreferredSize(new Dimension(900, 200));
				GridBagConstraints cBM = new GridBagConstraints();
				cBM.fill = GridBagConstraints.HORIZONTAL;
				cBM.gridx = 0;
				cBM.gridy = 0;
				// c.anchor = c.ABOVE_BASELINE;
				cBM.gridwidth = 5;
				cBM.weightx = 1;
				// c.anchor = c.PAGE_START;
				JTextArea moveBSaying = new JTextArea(
						"You can change words but this operation means to move the train back. Fields with ids cannot be\nchanged");
				moveBSaying.setPreferredSize(new Dimension(900, 60));
				moveBSaying.setBackground(null);
				moveBSaying.setEditable(false);
				moveBSaying.setFont(new Font("Comic Sans MS", 18, 18));
				moveBP.add(moveBSaying, cBM);
				cBM.fill = GridBagConstraints.HORIZONTAL;
				cBM.gridx = 0;
				cBM.gridy = 1;
				cBM.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cBM.weightx = 0.2;
				JTextField moveBT = new JTextField(moveBackLine[0]);
				moveBT.setPreferredSize(new Dimension(50, 30));
				moveBT.setFont(new Font("Comic Sans MS", 18, 18));
				moveBP.add(moveBT, cBM);
				cBM.fill = GridBagConstraints.HORIZONTAL;
				cBM.gridx = 1;
				cBM.gridy = 1;
				cBM.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cBM.weightx = 0.2;
				String back = "";
				for (int i = 1; i < moveBackLine.length; i++) {
					if (moveBackLine[i].equals("1")) {
						tempMoveBack = i;
						break;
					} else {
						if (i == 1)
							back = moveBackLine[i];
						else
							back = back + " " + moveBackLine[i];
					}
				}
				JTextField directionB = new JTextField(back);
				directionB.setPreferredSize(new Dimension(50, 30));
				directionB.setFont(new Font("Comic Sans MS", 18, 18));
				directionB.setEditable(true);
				moveBP.add(directionB, cBM);
				cBM.fill = GridBagConstraints.HORIZONTAL;
				cBM.gridx = 2;
				cBM.gridy = 1;
				cBM.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cBM.weightx = 0.2;
				JTextField trainIdBM = new JTextField("train_id");
				trainIdBM.setEditable(false);
				trainIdBM.setPreferredSize(new Dimension(50, 30));
				trainIdBM.setFont(new Font("Comic Sans MS", 18, 18));
				moveBP.add(trainIdBM, cBM);
				cBM.fill = GridBagConstraints.HORIZONTAL;
				cBM.gridx = 3;
				cBM.gridy = 1;
				cBM.gridwidth = 1;
				cBM.weightx = 0.17;
				String byB = "";
				for (int i = tempMoveBack + 1; i < moveBackLine.length; i++) {
					if (moveBackLine[i].equals("10")) {
						tempMoveBack = i;
						break;
					} else {
						if (i == tempMoveBack + 1)
							byB = moveBackLine[i];
						else
							byB = byB + " " + moveBackLine[i];
					}
				}
				JTextField byBM = new JTextField(byB);
				byBM.setEditable(true);
				byBM.setPreferredSize(new Dimension(50, 30));
				byBM.setFont(new Font("Comic Sans MS", 18, 18));
				moveBP.add(byBM, cBM);
				cBM.fill = GridBagConstraints.HORIZONTAL;
				cBM.gridx = 4;
				cBM.gridy = 1;
				cBM.gridwidth = 1;
				cBM.weightx = 0.2;
				JTextField cellsBM = new JTextField("number_of_cells");
				cellsBM.setEditable(false);
				cellsBM.setPreferredSize(new Dimension(50, 30));
				cellsBM.setFont(new Font("Comic Sans MS", 18, 18));
				moveBP.add(cellsBM, cBM);
				cBM.fill = GridBagConstraints.HORIZONTAL;
				cBM.gridx = 0;
				cBM.gridy = 2;
				cBM.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cBM.weightx = 0.2;
				JButton okMove = new JButton("Save changes");
				okMove.setFont(new Font("Comic Sans MS", 18, 18));
				okMove.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ArrayList<String> arr = new ArrayList<String>(0);
						arr.add(moveBT.getText() + " " + directionB.getText());
						arr.add(byBM.getText());
						extP.setMoveParser(arr, "back");
						moveInstructionBack = moveBT.getText() + " " + directionB.getText() + " " + "1" + " "
								+ byBM.getText() + " " + "10";
						instruction.setText("Write here a request. Please, look at the examples:" + "\n●"
								+ putInstruction + " - to put train\n●" + relocateInstruction + " - to relocate train"
								+ "\n●" + moveInstructionBack + " - to move train back\n●" + moveInstructionForward
								+ " - to move train forward");
						try {
							BufferedWriter out = new BufferedWriter(new FileWriter(moveBackPatternsFile, true));
							out.write("\n" + moveInstructionBack);
							out.close();
							out.flush();
						} catch (IOException e1) {

						}
					}
				});
				moveBP.add(okMove, cBM);
				langPanel.add(moveBP, co);

				co.gridx = 0;
				co.gridy = 2;
				co.weighty = 0.25;
				GridBagLayout layFMove = new GridBagLayout();
				JPanel moveFP = new JPanel(layFMove);
				moveFP.setPreferredSize(new Dimension(900, 200));
				GridBagConstraints cFM = new GridBagConstraints();
				cFM.fill = GridBagConstraints.HORIZONTAL;
				cFM.gridx = 0;
				cFM.gridy = 0;
				// c.anchor = c.ABOVE_BASELINE;
				cFM.gridwidth = 5;
				cFM.weightx = 1;
				// c.anchor = c.PAGE_START;
				JTextArea moveFSaying = new JTextArea(
						"You can change words but this operation means to move the train forward. Fields with ids cannot\nbe changed");
				moveFSaying.setPreferredSize(new Dimension(900, 60));
				moveFSaying.setBackground(null);
				moveFSaying.setEditable(false);
				moveFSaying.setFont(new Font("Comic Sans MS", 18, 18));
				moveFP.add(moveFSaying, cFM);
				cFM.fill = GridBagConstraints.HORIZONTAL;
				cFM.gridx = 0;
				cFM.gridy = 1;
				cFM.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cFM.weightx = 0.2;
				JTextField moveFT = new JTextField(moveForwardLine[0]);
				moveFT.setPreferredSize(new Dimension(50, 30));
				moveFT.setFont(new Font("Comic Sans MS", 18, 18));
				moveFP.add(moveFT, cFM);
				cFM.fill = GridBagConstraints.HORIZONTAL;
				cFM.gridx = 1;
				cFM.gridy = 1;
				cFM.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cFM.weightx = 0.2;
				String forward = "";
				for (int i = 1; i < moveForwardLine.length; i++) {
					if (moveForwardLine[i].equals("1")) {
						tempMoveForward = i;
						break;
					} else {
						if (i == 1)
							forward = moveForwardLine[i];
						else
							forward = forward + " " + moveForwardLine[i];
					}
				}
				JTextField directionF = new JTextField(forward);
				directionF.setPreferredSize(new Dimension(50, 30));
				directionF.setFont(new Font("Comic Sans MS", 18, 18));
				directionF.setEditable(true);
				moveFP.add(directionF, cFM);
				cFM.fill = GridBagConstraints.HORIZONTAL;
				cFM.gridx = 2;
				cFM.gridy = 1;
				cFM.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cFM.weightx = 0.2;
				JTextField trainIdFM = new JTextField("train_id");
				trainIdFM.setEditable(false);
				trainIdFM.setPreferredSize(new Dimension(50, 30));
				trainIdFM.setFont(new Font("Comic Sans MS", 18, 18));
				moveFP.add(trainIdFM, cFM);
				cFM.fill = GridBagConstraints.HORIZONTAL;
				cFM.gridx = 3;
				cFM.gridy = 1;
				cFM.gridwidth = 1;
				cFM.weightx = 0.17;
				String byF = "";
				for (int i = tempMoveForward + 1; i < moveForwardLine.length; i++) {
					if (moveForwardLine[i].equals("10")) {
						tempMoveForward = i;
						break;
					} else {
						if (i == tempMoveForward + 1)
							byF = moveForwardLine[i];
						else
							byF = byF + " " + moveForwardLine[i];
					}
				}
				JTextField byFM = new JTextField(byF);
				byFM.setEditable(true);
				byFM.setPreferredSize(new Dimension(50, 30));
				byFM.setFont(new Font("Comic Sans MS", 18, 18));
				moveFP.add(byFM, cFM);
				cFM.fill = GridBagConstraints.HORIZONTAL;
				cFM.gridx = 4;
				cFM.gridy = 1;
				cFM.gridwidth = 1;
				cFM.weightx = 0.2;
				JTextField cellsFM = new JTextField("number_of_cells");
				cellsFM.setEditable(false);
				cellsFM.setPreferredSize(new Dimension(50, 30));
				cellsFM.setFont(new Font("Comic Sans MS", 18, 18));
				moveFP.add(cellsFM, cFM);
				cFM.fill = GridBagConstraints.HORIZONTAL;
				cFM.gridx = 0;
				cFM.gridy = 2;
				cFM.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cFM.weightx = 0.2;
				JButton okFMove = new JButton("Save changes");
				okFMove.setFont(new Font("Comic Sans MS", 18, 18));
				okFMove.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ArrayList<String> arr = new ArrayList<String>(0);
						arr.add(moveFT.getText() + " " + directionF.getText());
						arr.add(byFM.getText());
						extP.setMoveParser(arr, "forward");
						moveInstructionForward = moveFT.getText() + " " + directionF.getText() + " " + "1" + " "
								+ byFM.getText() + " " + "10";
						instruction.setText("Write here a request. Please, look at the examples:" + "\n●"
								+ putInstruction + " - to put train\n●" + relocateInstruction + " - to relocate train"
								+ "\n●" + moveInstructionBack + " - to move train back\n●" + moveInstructionForward
								+ " - to move train forward");
						try {
							BufferedWriter out = new BufferedWriter(new FileWriter(moveForwardPatternsFile, true));
							out.write("\n" + moveInstructionForward);
							out.close();
							out.flush();
						} catch (IOException e1) {

						}
					}
				});
				moveFP.add(okFMove, cFM);
				langPanel.add(moveFP, co);

				co.gridx = 0;
				co.gridy = 3;
				co.weighty = 0.25;
				GridBagLayout layRelocate = new GridBagLayout();
				JPanel relocateP = new JPanel(layRelocate);
				relocateP.setPreferredSize(new Dimension(900, 200));
				GridBagConstraints cR = new GridBagConstraints();
				cR.fill = GridBagConstraints.HORIZONTAL;
				cR.gridx = 0;
				cR.gridy = 0;
				// c.anchor = c.ABOVE_BASELINE;
				cR.gridwidth = 7;
				cR.weightx = 1;
				// c.anchor = c.PAGE_START;
				JTextArea relocateSaying = new JTextArea(
						"You can change words but this operation means to relocate train. Fields with ids cannot be changed");
				relocateSaying.setPreferredSize(new Dimension(900, 30));
				relocateSaying.setBackground(null);
				relocateSaying.setEditable(false);
				relocateSaying.setFont(new Font("Comic Sans MS", 18, 18));
				relocateP.add(relocateSaying, cR);
				cR.fill = GridBagConstraints.HORIZONTAL;
				cR.gridx = 0;
				cR.gridy = 1;
				cR.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cR.weightx = 0.14;
				JTextField relocateT = new JTextField("relocate");
				relocateT.setPreferredSize(new Dimension(100, 30));
				relocateT.setFont(new Font("Comic Sans MS", 18, 18));
				relocateP.add(relocateT, cR);
				cR.fill = GridBagConstraints.HORIZONTAL;
				cR.gridx = 1;
				cR.gridy = 1;
				cR.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cR.weightx = 0.14;
				String trR = "";
				for (int i = 1; i < relocateLine.length; i++) {
					if (relocateLine[i].equals("1")) {
						tempRelocate = i;
						break;
					} else {
						if (i == 1)
							trR = relocateLine[i];
						else
							trR = trR + " " + relocateLine[i];
					}
				}
				JTextField trainR = new JTextField(trR);
				trainR.setPreferredSize(new Dimension(100, 30));
				trainR.setFont(new Font("Comic Sans MS", 18, 18));
				relocateP.add(trainR, cR);
				cR.fill = GridBagConstraints.HORIZONTAL;
				cR.gridx = 2;
				cR.gridy = 1;
				cR.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cR.weightx = 0.14;
				JTextField trainIdR = new JTextField("train_id");
				trainIdR.setEditable(false);
				trainIdR.setPreferredSize(new Dimension(100, 30));
				trainIdR.setFont(new Font("Comic Sans MS", 18, 18));
				relocateP.add(trainIdR, cR);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 3;
				c.gridy = 1;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.14;
				String OnR = "";
				for (int i = tempRelocate + 1; i < relocateLine.length; i++) {
					if (relocateLine[i].equals("2")) {
						tempRelocate = i;
						break;
					} else {
						if (i == tempRelocate + 1)
							OnR = relocateLine[i];
						else
							OnR = OnR + " " + relocateLine[i];
					}
				}
				JTextField onRT = new JTextField(OnR);
				onRT.setEditable(true);
				onRT.setPreferredSize(new Dimension(100, 30));
				onRT.setFont(new Font("Comic Sans MS", 18, 18));
				relocateP.add(onRT, c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 4;
				c.gridy = 1;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.14;
				JTextField railwayIdRT = new JTextField("railway_id");
				railwayIdRT.setEditable(false);
				railwayIdRT.setPreferredSize(new Dimension(100, 30));
				railwayIdRT.setFont(new Font("Comic Sans MS", 18, 18));
				relocateP.add(railwayIdRT, c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 5;
				c.gridy = 1;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.14;
				String WithR = "";
				for (int i = tempRelocate + 1; i < relocateLine.length; i++) {
					if (relocateLine[i].equals("0")) {
						tempRelocate = i;
						break;
					} else {
						if (i == tempRelocate + 1)
							WithR = relocateLine[i];
						else
							WithR = WithR + " " + relocateLine[i];
					}
				}
				JTextField withRT = new JTextField(WithR);
				withRT.setEditable(true);
				withRT.setPreferredSize(new Dimension(100, 30));
				withRT.setFont(new Font("Comic Sans MS", 18, 18));
				relocateP.add(withRT, c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 6;
				c.gridy = 1;
				c.gridwidth = 1;
				// c.anchor = c.BASELINE;
				c.weightx = 0.16;
				JTextField startNickRT = new JTextField("startNick");
				startNickRT.setEditable(false);
				startNickRT.setPreferredSize(new Dimension(100, 30));
				startNickRT.setFont(new Font("Comic Sans MS", 18, 18));
				relocateP.add(startNickRT, c);
				cR.fill = GridBagConstraints.HORIZONTAL;
				cR.gridx = 0;
				cR.gridy = 2;
				cR.gridwidth = 1;
				// c.anchor = c.BASELINE;
				cR.weightx = 0.2;
				JButton okRelocate = new JButton("Save changes");
				okRelocate.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ArrayList<String> arr = new ArrayList<String>(0);
						arr.add(relocateT.getText() + " " + trainR.getText());
						arr.add(onRT.getText());
						arr.add(withRT.getText());
						extP.setRelocateParser(arr);
						relocateInstruction = relocateT.getText() + " " + trainR.getText() + " " + "1" + " "
								+ onRT.getText() + " " + "2" + " " + withRT.getText() + " " + "0";
						instruction.setText("Write here a request. Please, look at the examples:" + "\n●"
								+ putInstruction + " - to put train\n●" + relocateInstruction + " - to relocate train"
								+ "\n●" + moveInstructionBack + " - to move train back\n●" + moveInstructionForward
								+ " - to move train forward");
						try {
							BufferedWriter out = new BufferedWriter(new FileWriter(relocatePatternsFile, true));
							out.write("\n" + relocateInstruction);
							out.close();
							out.flush();
						} catch (IOException e1) {

						}
					}
				});
				okRelocate.setFont(new Font("Comic Sans MS", 18, 18));
				relocateP.add(okRelocate, cR);
				langPanel.add(relocateP, co);

				changeLanguage.add(new JScrollPane(langPanel));
				changeLanguage.setLocation(350, 350);
				changeLanguage.pack();
			}
		});

		instruction.setBackground(null);
		// instruction.setPreferredSize(new Dimension(500, 130));
		instruction.setEditable(false);
		instruction.setFont(new Font("Comic Sans MS", 30, 22));
		// JTextArea query = new JTextArea("", 10, 40);

		JTextArea query = new JTextArea("", 10, 50);
		// query.setTabSize(5);
		query.setPreferredSize(new Dimension(1500, 400));
		query.setMaximumSize(new Dimension(1500, 400));
		query.setEditable(true);
		query.setFont(new Font("Comic Sans MS", 30, 22));
		JScrollPane queryPane = new JScrollPane(query);
		JButton ok = new JButton("OK");
		ok.setPreferredSize(new Dimension(50, 40));
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> requests = null;
				String str = "";
				try {
					System.out.println(extP.relocatePattern);
					requests = extP.parse(query.getText(), conn);
					for (int j = 0; j < requests.toArray().length; j++) {
						str = requests.get(j);
						processRequest(str, extP.getRelocateSqls());
					}
				} catch (Exception ex) {// catch должен быть ниже
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Sorry write in another way", "Wrong request format",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		GridBagLayout gbl = new GridBagLayout();
		mainPanel = new JPanel(gbl);
		mainPanel.setPreferredSize(new Dimension(1700, 500));
		GridBagConstraints c = new GridBagConstraints();
		// c.weightx = 0.5;
		c.weighty = 0.25;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(instruction, c);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.25;
		c.weightx = 0.75;
		c.anchor = GridBagConstraints.WEST;
		mainPanel.add(queryPane, c);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 1;
		c.weighty = 0.25;
		c.weightx = 0.25;
		c.anchor = GridBagConstraints.EAST;
		mainPanel.add(ok, c);

		JButton[][] a = fill();
		int rows = a.length;
		int colomns = a[0].length;
		GridLayout grid = new GridLayout(rows, colomns);
		visualization.setLayout(grid);
		visualization.setPreferredSize(new Dimension(1700, 100));
		for (int k = 0; k < rows; k++) {
			for (int m = 0; m < colomns; m++) {
				visualization.add(a[k][m]);
			}
		}
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0;
		c.gridx = 0;
		c.weighty = 0.5;
		c.weightx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.PAGE_END;
		mainPanel.add(new JScrollPane(visualization), c);

		mainFrame.add(new JScrollPane(mainPanel));
		mainFrame.setLocation(0, 270);
		mainFrame.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				saveStuff("src\\files\\PutInstruction", putInstruction);
				saveStuff("src\\files\\RelocateInstruction", relocateInstruction);
				saveStuff("src\\files\\MoveBackInstruction", moveInstructionBack);
				saveStuff("src\\files\\MoveForwardInstruction", moveInstructionForward);
				saveStuff("src\\files\\PutPattern", extP.putPattern);
				saveStuff("src\\files\\RelocatePattern", extP.relocatePattern);
				saveStuff("src\\files\\MoveBackPattern", extP.moveBackPattern);
				saveStuff("src\\files\\MoveForwardPattern", extP.moveForwardPattern);
				mainFrame.dispose();
				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}
		});
		mainFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		mainFrame.pack();
	}

	public void saveStuff(String fileName, String instruction) {
		FileOutputStream fosPut = null;
		ObjectOutputStream outPut = null;
		try {
			fosPut = new FileOutputStream(fileName);
			outPut = new ObjectOutputStream(fosPut);
			outPut.writeObject(instruction);
			outPut.flush();
			outPut.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void saveFields() {
		FileOutputStream fosPut = null;
		ObjectOutputStream outPut = null;
		try {
			fosPut = new FileOutputStream("src/files/Fields");
			outPut = new ObjectOutputStream(fosPut);
			outPut.writeObject(fields);
			outPut.flush();
			outPut.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void changePutPattern(ArrayList<String> arr) {
		extP.setPutParser(arr);
		putInstruction = arr.get(0) + " " + "1" + " " + arr.get(1) + " " + "1" + " " + arr.get(2) + " " + "0";
		instruction.setText("Write here a request. Please, look at the examples:" + "\n●" + putInstruction
				+ " - to put train\n●" + relocateInstruction + " - to relocate train" + "\n●" + moveInstructionBack
				+ " - to move train back\n●" + moveInstructionForward + " - to move train forward");
	}

	public JButton[][] fill() {
		JButton[][] arr = new JButton[1][1];
		JButton button = new JButton();
		button.setVisible(true);
		button.setEnabled(false);
		button.setPreferredSize(new Dimension(20, 20));
		arr[0][0] = button;
		// int max = 0;
		// int countRailways = 0;
		// ResultSet rs = null;
		// ArrayList<Integer> ids = new ArrayList<Integer>(0);
		// ArrayList<Integer> lengths = new ArrayList<Integer>(0);
		// try{
		// PreparedStatement psR = conn.prepareStatement("select id, total_length from
		// railways");
		// rs = psR.executeQuery();
		// while(rs.next()){
		// if (rs.getInt(2) > max)
		// max = rs.getInt(2);
		// ids.add(rs.getInt(1));
		// lengths.add(rs.getInt(2));
		// countRailways++;
		// }
		// } catch(SQLException e){
		//
		// }
		// JButton[][] arr = new JButton[countRailways][max];
		// for (int i = 0; i < countRailways; i++){
		// for (int j = 0; j < max; j++){
		// JButton button = new JButton();
		// if (j >= lengths.get(i))
		// button.setVisible(false);
		// button.setEnabled(false);
		// button.setPreferredSize(new Dimension(20,20));
		// arr[i][j] = button;
		// }
		// }
		// try{
		// Hashtable<Integer, Integer> trainsLengths = new Hashtable<Integer,
		// Integer>();
		// PreparedStatement psT = conn.prepareStatement("select id, length from
		// trains");
		// rs = psT.executeQuery();
		// while(rs.next()){
		// trainsLengths.put(rs.getInt(1), rs.getInt(2));
		// }
		//
		// PreparedStatement psA = conn.prepareStatement("select id_train, id_railway,
		// startnick from allocation");
		// rs = psA.executeQuery();
		// while(rs.next()){
		// for (int j = rs.getInt(3) ; j < rs.getInt(3)+
		// trainsLengths.get(rs.getInt(1)); j++){
		// arr[rs.getInt(2)-1][j].setBackground(new Color(1));//change first index,
		// могут быть несовпадения
		// }
		// }
		// } catch(SQLException e){
		//
		// }
		return arr;
	}

	public void refresh() {
		visualization.removeAll();
		JButton[][] a = fill();
		int rows = a.length;
		int colomns = a[0].length;
		GridLayout grid = new GridLayout(rows, colomns);
		visualization.setLayout(grid);
		visualization.setPreferredSize(new Dimension(1700, 100));
		for (int k = 0; k < rows; k++) {
			for (int m = 0; m < colomns; m++) {
				visualization.add(a[k][m]);
			}
		}
		mainFrame.pack();
	}

	public void processRequest(String request, Hashtable<Integer, ArrayList<String>> relocateSqls) {
		SyntaxAnalyzer stAnalyzer = new SyntaxAnalyzer(new SqlLexer(request));
		ArrayList<Node> nodes = stAnalyzer.buildTree();
		Object[] ar = nodes.toArray();
		for (int i = 0; i < ar.length; i++) {
			System.out.println(nodes.get(i).type + " " + nodes.get(i).values.toString());
		}
		Object[] firstNode = null;
		Object[] secondNode = null;
		Object[] thirdNode = null;
		for (int i = 0; i < ar.length; i++) {
			if (nodes.get(i).type.equals("FIRST"))
				firstNode = (nodes.get(i).values).toArray();
			else if (nodes.get(i).type.equals("SECOND"))
				secondNode = (nodes.get(i).values).toArray();
			else if (nodes.get(i).type.equals("THIRD"))
				thirdNode = (nodes.get(i).values).toArray();
		}
		Hashtable<String, Constraint> table = new Hashtable<String, Constraint>();
		String firstLine = "";
		String secondLine = "";
		String thirdLine = "";
		ArrayList<String> domainLines = new ArrayList<String>(0);
		int railway = 0;
		for (int i = 0; i < firstNode.length; i++) {
			if (!firstNode[i].equals("=") && !firstNode[i].equals("T")) {
				System.out.println("r " + Integer.valueOf(firstNode[i].toString()));
				PreparedStatement pr;
				try {
					pr = conn.prepareStatement("select length, wagonstoservice, type from trains where id = ?");
					pr.setInt(1, Integer.valueOf(firstNode[i].toString()));
					ResultSet rs = pr.executeQuery();
					while (rs.next()) {
						Constraint cVar1 = new Constraint(String.valueOf(rs.getInt(1)));
						table.put("T" + firstNode[i].toString(), cVar1);
						if (!firstLine.equals(""))
							firstLine = firstLine + " + T" + firstNode[i].toString();
						else
							firstLine = "T" + firstNode[i].toString();
						Constraint cVar2 = new Constraint(String.valueOf(rs.getInt(2)));
						table.put("W" + firstNode[i].toString(), cVar2);
						PreparedStatement prW = conn
								.prepareStatement("select length from wagonslength where wagon_type = ?");
						prW.setInt(1, rs.getInt(3));
						ResultSet rsW = prW.executeQuery();
						while (rsW.next()) {
							Constraint cVar3 = new Constraint(String.valueOf(rsW.getInt(1)));
							table.put("L" + firstNode[i].toString(), cVar3);
							if (!secondLine.equals(""))
								secondLine = secondLine + " + W" + firstNode[i].toString() + " * L"
										+ firstNode[i].toString();
							else
								secondLine = "W" + firstNode[i].toString() + " * L" + firstNode[i].toString();

						}

					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i = 0; i < thirdNode.length; i++) {
			if (!thirdNode[i].equals("=") && !thirdNode[i].equals("R")) {
				railway = Integer.valueOf(thirdNode[i].toString());
				System.out.println("rrr " + Integer.valueOf(thirdNode[i].toString()));
				PreparedStatement pr;
				try {
					pr = conn.prepareStatement("select total_length, useful_length from railways where id = ?");
					pr.setInt(1, Integer.valueOf(thirdNode[i].toString()));
					ResultSet rs = pr.executeQuery();
					while (rs.next()) {
						Constraint cVar1 = new Constraint(String.valueOf(rs.getInt(1)));
						Constraint cVar2 = new Constraint(String.valueOf(rs.getInt(2)));
						table.put("R" + thirdNode[i].toString(), cVar1);
						table.put("U" + thirdNode[i].toString(), cVar2);
						firstLine = firstLine + " <= R" + thirdNode[i].toString();
						secondLine = secondLine + " <= U" + thirdNode[i].toString();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i = 0; i < secondNode.length; i++) {
			if (!secondNode[i].equals("=") && !secondNode[i].equals("S")) {
				System.out.println("rr " + Integer.valueOf(secondNode[i].toString()));
				PreparedStatement pr;
				try {
					pr = conn.prepareStatement("select length from trains where id = ?");
					pr.setInt(1, Integer.valueOf(firstNode[i].toString()));
					ResultSet rs = pr.executeQuery();
					while (rs.next()) {
						Constraint cVar = new Constraint("[" + secondNode[i].toString() + ","
								+ String.valueOf(Integer.valueOf(secondNode[i].toString()) + rs.getInt(1)) + "]");
						table.put("D" + firstNode[i].toString(), cVar);
						System.out.println("lala " + table.get("D" + firstNode[i].toString()));
						PreparedStatement prS = conn
								.prepareStatement("select id_train, startnick from allocation where id_railway = ?");
						prS.setInt(1, railway);
						ResultSet rsS = prS.executeQuery();
						int counter = 0;
						while (rsS.next()) {
							if (!trainWillBeRelocated(rsS.getInt(1), relocateSqls)) {
								counter++;
								PreparedStatement prT = conn.prepareStatement("select length from trains where id = ?");
								prT.setInt(1, rsS.getInt(1));
								ResultSet rsT = prT.executeQuery();
								while (rsT.next()) {
									Constraint cVarF = new Constraint("[" + String.valueOf(rsS.getInt(2)) + ","
											+ String.valueOf(rsS.getInt(2) + rsT.getInt(1)) + "]");
									table.put("F" + firstNode[i].toString() + String.valueOf(counter), cVarF);
									thirdLine = "D" + firstNode[i].toString() + " + F" + firstNode[i].toString()
											+ String.valueOf(counter) + " > D" + firstNode[i].toString() + "F"
											+ firstNode[i].toString() + String.valueOf(counter);
									domainLines.add(thirdLine);
								}
							}
						}
					}
				} catch (SQLException ex) {

				}
			}
		}

		Object[] array1 = table.keySet().toArray();
		Object[] array = table.values().toArray();
		for (int i = 0; i < array.length; i++) {
			System.out.println(array1[i] + " " + array[i]);
		}
		System.out.println(firstLine);
		System.out.println(secondLine);
		ArrayList<String> firstLines = new ArrayList<String>(0);
		firstLines.add(firstLine);
		firstLines.add(secondLine);
		for (int i = 0; i < domainLines.toArray().length; i++) {
			System.out.println(domainLines.get(i));
		}
		Modelling mo = new Modelling(firstLines, domainLines, table);
		Model m = mo.buildModel();
		try {
			ArrayList<String> queries = mo.getSolution(m);
			ArrayList<String> relocateSqlsArr = relocateSqls.get(railway);
			if (relocateSqlsArr != null) {
				for (int j = 0; j < relocateSqlsArr.toArray().length; j++) {
					try {
						PreparedStatement prep = conn.prepareStatement(relocateSqlsArr.get(j));
						prep.execute();
						// refresh();
					} catch (SQLException exception) {
						exception.printStackTrace();
					}
				}
			}
			for (int i = 0; i < queries.toArray().length; i++) {
				try {
					System.out.println("query is " + queries.get(i));
					PreparedStatement pr = conn.prepareStatement(queries.get(i));
					pr.execute();
					// refresh();
				} catch (SQLException exception) {
					exception.printStackTrace();
				}
			}
			refresh();
		} catch (NoSolutionException ex) {
			System.out.println("No solution at all, sorry");
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
	}

	public static boolean trainWillBeRelocated(int train, Hashtable<Integer, ArrayList<String>> relocatingTrains) {
		for (int i : relocatingTrains.keySet()) {
			if (i == train)
				return true;
		}
		return false;
	}

	private void readFields() {
		FileInputStream fisPut = null;
		ObjectInputStream inPut = null;
		try {
			File fieldsFile = new File("src/files/Fields");
			boolean fileNotExist = fieldsFile.createNewFile();
			fisPut = new FileInputStream(fieldsFile);
			inPut = new ObjectInputStream(fisPut);
			if (!fileNotExist) {
				List tempMap = (ArrayList) inPut.readObject();
				System.out.println(tempMap.size());
				if (tempMap != null) {
					fields = tempMap;
				} else {
					fields = new ArrayList<String>(0);
				}
			}
			inPut.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void readPatterns(String fileName, String instruction) {
		FileInputStream fisPut = null;
		ObjectInputStream inPut = null;
		try {
			fisPut = new FileInputStream(fileName);
			inPut = new ObjectInputStream(fisPut);
			if (instruction.equals("put"))
				ProjectGUI.extP.putPattern = (String) inPut.readObject();
			else if (instruction.equals("relocate"))
				ProjectGUI.extP.relocatePattern = (String) inPut.readObject();
			else if (instruction.equals("moveBack"))
				ProjectGUI.extP.moveBackPattern = (String) inPut.readObject();
			else if (instruction.equals("moveForward"))
				ProjectGUI.extP.moveForwardPattern = (String) inPut.readObject();
			inPut.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void readInstruction(String fileName, String instruction) {
		FileInputStream fisPut = null;
		ObjectInputStream inPut = null;
		try {
			fisPut = new FileInputStream(fileName);
			inPut = new ObjectInputStream(fisPut);
			if (instruction.equals("put"))
				ProjectGUI.putInstruction = (String) inPut.readObject();
			else if (instruction.equals("relocate"))
				ProjectGUI.relocateInstruction = (String) inPut.readObject();
			else if (instruction.equals("moveBack"))
				ProjectGUI.moveInstructionBack = (String) inPut.readObject();
			else if (instruction.equals("moveForward"))
				ProjectGUI.moveInstructionForward = (String) inPut.readObject();
			inPut.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// try {
		// Class.forName("org.postgresql.Driver");
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		// String connectionString = "jdbc:postgresql://localhost:5433/" + defaultDb;
		// try {
		// conn = DriverManager.getConnection(connectionString, user, password);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		ProjectGUI project = new ProjectGUI();
		readInstruction("src/files/PutInstruction", "put");
		readInstruction("src/files/RelocateInstruction", "relocate");
		readInstruction("src/files/MoveBackInstruction", "moveBack");
		readInstruction("src/files/MoveForwardInstruction", "moveForward");
		readPatterns("src/files/PutPattern", "put");
		readPatterns("src/files/MoveBackPattern", "moveBack");
		readPatterns("src/files/MoveForwardPattern", "moveForward");
		readPatterns("src/files/RelocatePattern", "relocate");
		System.out.println("ext " + extP.relocatePattern);
		project.instruction.setText("Write here a request. Please, look at the examples:" + "\n●" + putInstruction
				+ " - to put train\n●" + relocateInstruction + " - to relocate train" + "\n●" + moveInstructionBack
				+ " - to move train back\n●" + moveInstructionForward + " - to move train forward");
	}
}
