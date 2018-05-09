package my.project;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CreateNewField {

	public static void addNewField(String stuffToInsert, String entity, String modelFileName, String packageName) {
		editFieldsFile(stuffToInsert, entity, modelFileName);
		createJavaFile("mymodel.dmodel");
		compile(entity);
		moveGeneratedFiles(
				new File("/home/ksusha/runtime-EclipseXtext/dsl-project/src-gen/com/rws/data/" + entity + ".class"), packageName);
	}

	private static void editFieldsFile(String stuffToInsert, String entity, String modelFileName) {
		File file = new File("/home/ksusha/runtime-EclipseXtext/dsl-project/src/" + modelFileName);
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(file, "rw");
			String line = raf.readLine();
			while (line != null && !line.equals("\tentity " + entity + " {")) {
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
			raf.writeBytes("\t\t" + stuffToInsert + "\n");
			for (String currLine : restOfTheFile) {
				raf.writeBytes(currLine+"\n");
			}
			raf.close();
			System.out.println("file has been overwritten");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static void createJavaFile(String modelFileName) {
		final String javaBin = System.getProperty("java.home") + File.separator + "bin";
		final String java = javaBin + File.separator + "java";
		try {
			final File currentJar = new File(
					MainClass.class.getProtectionDomain().getCodeSource().getLocation().toURI());

			final ArrayList<String> command = new ArrayList<String>();
			command.add(java);
			command.add("-jar");
			command.add("/home/ksusha/XtextGenerator/model-compiler.jar");
			command.add("/home/ksusha/runtime-EclipseXtext/dsl-project/src/" + modelFileName);

			final ProcessBuilder builder = new ProcessBuilder(command);
			Process process = builder.start();
			process.waitFor();
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

	public static void compile(String entity) {

		final String javac = "/usr/lib/jvm/java-8-oracle/bin" + File.separator + "javac";
		// System.getProperty("java.home") + File.separator + "bin" + File.separator
		// + "javac";
		final ArrayList<String> commandCompile = new ArrayList<String>();
		commandCompile.add(javac);
		commandCompile.add("/home/ksusha/runtime-EclipseXtext/dsl-project/src-gen/com/rws/data/" + entity + ".java");
		final ProcessBuilder compileBuilder = new ProcessBuilder(commandCompile);
		try {
			Process process = compileBuilder.start();
			process.waitFor();
			System.out.println("done compiling 2 " + commandCompile.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void moveGeneratedFiles(File file, String packageName) {
		File destFolder = new File("/home/ksusha/runtime-EclipseXtext/dsl-project/bin/com/rws/" + packageName);// not necessarily
																										// rws/data
		File temp = new File(destFolder, file.getName());
		System.out.println(temp.getAbsolutePath());
		try {
			System.out.println(Files.deleteIfExists(temp.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(file.renameTo(new File(destFolder, file.getName())));
	}

	public static void main(String[] args) {
		addNewField("name2 : Integer", "BrigadeInfo", "mymodel.dmodel", "data");
		// moveFile(new
		// File("/home/ksusha/runtime-EclipseXtext/dsl-project/src-gen/com/rws/data/RailwayInfo.class"));
		//editFieldsFile("name", "BrigadeInfo", "mymodel.dmodel");
	}
}
