package my.project;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class ProcessRule {
	
	private static String modelFileName = "constraintsSystems.dmodel";
	
	//add rule to constraintsSystems.dmodel
	public static void createRule(String commandName, List<String> constraints) {
		commandName = commandName + "Rule";
		String stuffToInsert = "\tentity " + commandName + " {\n";
		for (int i = 0; i < constraints.size(); i++) {
			stuffToInsert = stuffToInsert + "\t\t" + commandName + "Constraint" + i + " : String : " + constraints.get(i) + "\n";
		}
		stuffToInsert = stuffToInsert + "\t}\n";
		
		editFieldsFile(stuffToInsert, null, ProcessRule.modelFileName, false);
		CreateNewField.createJavaFile("constraintsSystems.dmodel");
		CreateNewField.compile(commandName);
		CreateNewField.moveGeneratedFiles(
				new File("/home/ksusha/runtime-EclipseXtext/dsl-project/src-gen/com/rws/data/" + commandName + ".class"), "constraints");
	}
	
	public static void editRule(String commandName, List<String> constraints) {
		commandName = commandName + "Rule";
		
	}
	
	private static void editFieldsFile(String stuffToInsert, String entity, String modelFileName, boolean isEdit) {
		File file = new File("/home/ksusha/runtime-EclipseXtext/dsl-project/src/" + modelFileName);
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(file, "rw");
			String line = raf.readLine();
			
			if (isEdit) {
				
			} else {
				while (line != null && !line.equals("package com.rws.constraints {")) {
					line = raf.readLine();
				}
				long position = raf.getFilePointer();
				List<String> restOfTheFile = new ArrayList<String>(0);
				while (line != null) {
					line = raf.readLine();
					if (line != null) {
						restOfTheFile.add(line);
					}
				}
				raf.seek(position);
				raf.writeBytes(stuffToInsert);
				for (String currLine : restOfTheFile) {
					raf.writeBytes(currLine+"\n");
				}
			}
			
			raf.close();
			System.out.println("file has been overwritten");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	//create and serialize construction in the certain package
	public static void createConstruction(String constructionName, List<ConstructionComponent> componentsList) {
		Construction construction = new Construction(constructionName);
		construction.setConstructionComponents(componentsList);
		
	}
	
	public static void editConstruction(String constructionName) {
		
	}
	
	public static void main (String[] args) {
		List<String> constraints = new ArrayList<String>(0);
		constraints.add("a < b");
		constraints.add("x <= y");
		createRule("Move", constraints);
	}
}
