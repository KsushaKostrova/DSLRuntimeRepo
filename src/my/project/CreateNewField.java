package my.project;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CreateNewField {

	public static void addNewField(String stuffToInsert) {
		editFile(stuffToInsert);
		createJavaFile();
		compile();
		moveGeneratedFiles(
				new File("/home/ksusha/runtime-EclipseXtext/dsl-project/src-gen/com/rws/data/BrigadeInfo.class"));
	}

	private static void editFile(String stuffToInsert) {
		try {
			Files.write(Paths.get("/home/ksusha/runtime-EclipseXtext/dsl-project/bin/mymodel.dmodel"),
					("datatype String\n" + 
							"datatype Integer\n" + 
							"\n" + 
							"package com.rws.data { \n" + 
							"	entity TrainInfo {\n" + 
							"		trainId : Integer\n" + 
							"		trainLength : Integer\n" + 
							"		trainStartNick : Integer\n" + 
							"		method pr : System.out.println(1)\n" + 
							"	}\n" + 
							"	\n" + 
							"	entity BrigadeInfo {\n" + 
							"		numberOfPeople : Integer\n" + 
							"		hours : Integer\n" + 
							"		smth : String\n" + 
							"		" + stuffToInsert + "\n" + 
							"	}\n" + 
							"	\n" + 
							"	entity WagonInfo {\n" + 
							"		\n" + 
							"	}\n" + 
							"	\n" + 
							"	entity RailwayInfo {\n" + 
							"		railwayLentgh : Integer\n" + 
							"		railwayInfo : Integer\n" + 
							"	}\n" + 
							"}")
									.getBytes());
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.out.println("file has been overwritten");
	}

	private static void createJavaFile() {
		final String javaBin = System.getProperty("java.home") + File.separator + "bin";
		final String java = javaBin + File.separator + "java";
		try {
			final File currentJar = new File(
					MainClass.class.getProtectionDomain().getCodeSource().getLocation().toURI());

			final ArrayList<String> command = new ArrayList<String>();
			command.add(java);
			command.add("-jar");
			command.add("/home/ksusha/XtextGenerator/model-compiler.jar");
			command.add("/home/ksusha/runtime-EclipseXtext/dsl-project/bin/mymodel.dmodel");

			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
			Thread.sleep(10000);
			System.out.println("done compiling 1 " + command.toString());
		} catch (URISyntaxException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void compile() {

		final String javac = "/usr/lib/jvm/java-8-oracle/bin" + File.separator + "javac";
		// System.getProperty("java.home") + File.separator + "bin" + File.separator
		// + "javac";
		final ArrayList<String> commandCompile = new ArrayList<String>();
		commandCompile.add(javac);
		commandCompile.add("/home/ksusha/runtime-EclipseXtext/dsl-project/src-gen/com/rws/data/BrigadeInfo.java");
		final ProcessBuilder compileBuilder = new ProcessBuilder(commandCompile);
		try {
			compileBuilder.start();
			Thread.sleep(10000);
			System.out.println("done compiling 2 " + commandCompile.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void moveGeneratedFiles(File file) {
		File destFolder = new File("/home/ksusha/runtime-EclipseXtext/dsl-project/bin/com/rws/data");// not necessarily
																										// rws/data
		System.out.println(file.renameTo(new File(destFolder, file.getName())));
	}

	public static void main(String[] args) {
		addNewField("as");
		// moveFile(new
		// File("/home/ksusha/runtime-EclipseXtext/dsl-project/src-gen/com/rws/data/RailwayInfo.class"));
	}
}
