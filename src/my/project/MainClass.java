package my.project;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;

//import my.company.blog.SmthNewNew;

public class MainClass implements Serializable {

	private static final long serialVersionUID = 1L;
//	boolean restarted = false;

	public static void main(String[] args) throws IOException {

		MainClass mainClass = new MainClass();
//		FileInputStream fisPut = null;
//		ObjectInputStream inPut = null;
//		try {
//			fisPut = new FileInputStream("src/files/restarted");
//			try {
//				inPut = new ObjectInputStream(fisPut);
//				mainClass.restarted = (Boolean) inPut.readObject();
//				inPut.close();
//			} catch (EOFException ex) {
//				mainClass.restarted = false;
//			}
//		} catch (IOException | ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(100, 100));
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JButton buttonOk = new JButton("OK");
//		if (mainClass.restarted) {
//			buttonOk.setText("YES");
//			System.out.println("YESSS");
//		}
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
		//		if (!mainClass.restarted) {
			
				
//				SmthNewNew smth = new SmthNewNew();
//					smth.pr();
//					System.out.println("pr was called");
					
					
					
					
					
//					try {
//						Files.write(Paths.get("/home/ksusha/runtime-EclipseXtext/dsl-project/src/mymodel.dmodel"),
//								("datatype String\n" + 
//										"\n" + 
//										"package my.company.blog { \n" + 
//										"	\n" + 
//										"	import my.company.common.*\n" + 
//										"	\n" + 
//										"	entity Blog { \n" + 
//										"		title : String \n" + 
//										"	} \n" + 
//										"\n" + 
//										"entity SmthNewNew { \n" + 
//										"		kuku : String\n" + 
//										"		lalala : String\n" + 
//										"		method pr : System.out.println(1)\n" + 
//										"	}\n" + 
//										"	\n" + 
//										"entity NewEntityEntity { \n" + 
//										"		kuku : String\n" + 
//										"		lalala : String\n" + 
//										"		hey : String\n" + 
//										"		method pr : System.out.println(1)\n" + 
//										"	}\n" + 
//										"entity ClassFromSaturday { \n" + 
//										"		kuku : String\n" + 
//										"		lalala : String\n" + 
//										"		method pr : System.out.println(1)\n" + 
//										"	}\n" + 
//										"entity TestClass { \n" + 
//										"		kuku : String\n" + 
//										"		lalala : String\n" + 
//										"		method pr : System.out.println(1)\n" + 
//										"	}\n" + 
//										"}").getBytes());
//					} catch (IOException ex) {
//						ex.printStackTrace();
//					}
//					
//					System.out.println("file has been overwritten");
					
//					File file = new File("/home/ksusha/runtime-EclipseXtext/my.project/src/mymodel.dmodel");
//					if (!Desktop.isDesktopSupported()) {
//						System.out.println("Desktop is not supported");
//						return;
//					}
//					Desktop desktop = Desktop.getDesktop();
//					if (file.exists())
//						try {
//							desktop.open(file);
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}
//					mainClass.restarted = true;
//					FileOutputStream fosPut = null;
//					ObjectOutputStream outPut = null;
//					try {
//						fosPut = new FileOutputStream("src/files/restarted");
//						outPut = new ObjectOutputStream(fosPut);
//						outPut.writeObject(mainClass.restarted);
//						outPut.flush();
//						outPut.close();
//					} catch (IOException ex) {
//						ex.printStackTrace();
//					}

//					final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator
//							+ "java";
//					try {
//						final File currentJar = new File(
//								MainClass.class.getProtectionDomain().getCodeSource().getLocation().toURI());
//
//						if (!currentJar.getName().endsWith(".jar"))
//							buttonOk.setText("oopse");
//						final ArrayList<String> command = new ArrayList<String>();
//						command.add(javaBin);
//						command.add("-jar");
//						command.add(currentJar.getPath());
//						command.add("/home/ksusha/runtime-EclipseXtext/dsl-project/src/mymodel.dmodel");
//
//						final ProcessBuilder builder = new ProcessBuilder(command);
//						builder.start();
//						
//						final String javac = "javac";
//								//System.getProperty("java.home") + File.separator + "bin" + File.separator
//								//+ "javac";
//						final ArrayList<String> commandCompile = new ArrayList<String>();
//						commandCompile.add(javac);
//						commandCompile.add("/home/ksusha/runtime-EclipseXtext/dsl-project/src-gen/my/company/blog/TestClass.java");
//						final ProcessBuilder compileBuilder = new ProcessBuilder(commandCompile);
//						compileBuilder.start();
//					//	System.exit(0);
//					} catch (URISyntaxException e2) {
//						e2.printStackTrace();
//					} catch (IOException e2) {
//						e2.printStackTrace();
//					}
			//	} else {
					buttonOk.setText("R");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e2) {
						e2.printStackTrace();
					}
					System.out.println("before reflection");
					try {
						Class someNewClass = Class.forName("my.company.blog.TestClass");
						Object obj = someNewClass.newInstance();
						Method method = someNewClass.getMethod("pr");
						method.invoke(obj);
						System.out.println("after reflection");
					} catch (ClassNotFoundException el) {
						buttonOk.setText("NOPE");
					} catch (NoSuchMethodException e1) {
						e1.printStackTrace();
					} catch (SecurityException e1) {
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						e1.printStackTrace();
					} catch (InstantiationException e1) {
						e1.printStackTrace();
					}
			//	}
			}
		});
		panel.add(buttonOk);
		frame.setVisible(true);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();		
	}
}
