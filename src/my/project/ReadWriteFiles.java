package my.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class ReadWriteFiles {

	public static void writeToFile(String fileName, List stuffToWrite) {
		FileOutputStream fosPut = null;
		ObjectOutputStream outPut = null;
		try {
			fosPut = new FileOutputStream("src/files/" + fileName);
			outPut = new ObjectOutputStream(fosPut);
			outPut.writeObject(stuffToWrite);
			outPut.flush();
			outPut.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static Object readFromFile(String fileName) {
		Object res = null;
		FileInputStream fisPut = null;
		ObjectInputStream inPut = null;
		try {
			File fieldsFile = new File("src/files/" + fileName);
			boolean fileNotExist = fieldsFile.createNewFile();
			if (!fileNotExist) {
				fisPut = new FileInputStream("src/files/" + fileName);
				inPut = new ObjectInputStream(fisPut);
				res = inPut.readObject();
				inPut.close();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static void main(String[] args) {
		Map<String, String> fields = new HashMap<String, String>(0);
		fields = getTheFields();
		for (int i = 0; i < fields.size(); i++) {
			System.out.println(fields.get(i));
		}
	}

	//get all classes from the package
	public static Class<?>[] getClasses(String packageName, Boolean storeOnly) {
		List<ClassLoader> classLoaderList = new LinkedList<ClassLoader>();
		classLoaderList.add(ClasspathHelper.contextClassLoader());
		classLoaderList.add(ClasspathHelper.staticClassLoader());
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().setScanners(new SubTypesScanner(false), new ResourcesScanner())
						.setUrls(ClasspathHelper.forClassLoader(classLoaderList.toArray(new ClassLoader[0])))
						.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));// get
																											// package
																											// name from
																											// properties?

		Set<Class<?>> classesSet = reflections.getSubTypesOf(Object.class);
		Class<?>[] classes = new Class[0];		
		classes = classesSet.toArray(classes);
		List<Class<?>> classesList = new ArrayList(0);
		if (storeOnly) {
			for (int i = 0; i < classes.length; i++) {
				Field[] classesFields = classes[i].getDeclaredFields();
				for (int j = 0; j < classesFields.length; j++) {
					String methodName = classesFields[j].getName();
					System.out.println("m " + methodName);
					if (methodName.equals("isStore")) {
						System.out.println("method is " + methodName);
						System.out.println("class is " + classes[i].getSimpleName());
						Class<?> classInstance;
						try {
							classInstance = Class.forName(packageName + "."+classes[i].getSimpleName());
								
								String firstLetter = String.valueOf(methodName.charAt(0)).toUpperCase();
								methodName = firstLetter + methodName.substring(1);
								Method m = classInstance.getDeclaredMethod("get"+methodName, new Class[] {});
								Object o = classInstance.newInstance();
								System.out.println((Boolean)m.invoke(o));
								if ((Boolean)m.invoke(o)) {
									classesList.add(classes[i]);
								}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}
			classes = new Class[0];
			classes = classesList.toArray(classes);
			return classes;
		}
		return classes;
	}

	//fill the fields with fields from every class
	public static Map<String, String> getTheFields() {
		Map<String, String> fields = new HashMap<String, String>(0);
		Class<?>[] classes = getClasses("com.rws.data", false);
		for (int i = 0; i < classes.length; i++) {
			Field[] classesFields = classes[i].getDeclaredFields();
			for (int j = 0; j < classesFields.length; j++) {
				System.out.println("field is " + classesFields[j].getName());
				fields.put(classesFields[j].getName(), classes[i].getSimpleName());
			}
		}
		return fields;
	}
}
