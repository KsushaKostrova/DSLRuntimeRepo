package my.project;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class OperateConstruction {

	//TODO rename class, don't return that
	public static List<ConstructionComponent> getComponents(String request) {
		List<String> values = new ArrayList<>(0);
		Construction construction = recognizeConstruction(request, values);
		int count = 0;
		for (ConstructionComponent component : construction.constructionComponents) {
			if(component.isField) {
				System.out.println(component.getValue() + " class " + component.getParentClassName());
				System.out.println(values.get(count));
				count++;
			}
		}
		
		List<String> constraints = getConstraints(construction.getName());
		return construction.constructionComponents;
	}
	
	public static void processConstraints(List<String> constraints) {
		
	}
	
	public static List<String> getConstraints (String className){
		className = className + "Rule";
		List<String> constraints = new ArrayList<String>(0);
		Class<?> classInstance;
		try {
			classInstance = Class.forName("com.rws.constraints."+className);
			Field[] classesFields = classInstance.getDeclaredFields();
			for (int j = 0; j < classesFields.length; j++) {
				//System.out.println("field is " + classesFields[j].getName());
				//System.out.println("field type is " + classesFields[j].getType());
				String methodName = classesFields[j].getName();
				String firstLetter = String.valueOf(methodName.charAt(0)).toUpperCase();
				methodName = firstLetter + methodName.substring(1);
				Method m = classInstance.getDeclaredMethod("get"+methodName, new Class[] {});
				Object o = classInstance.newInstance();
				System.out.println((String)m.invoke(o));
				constraints.add((String)m.invoke(o));
				//constraints.add(classesFields[j].getName());
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
		
		
		return constraints;
	}

	public static Construction recognizeConstruction(String request, List<String> values) {
		String[] requestParts = request.split(" ");
		List<Construction> constructions = getAllConstructions();
		Construction returnConstruction = null;
		for (Construction construction : constructions) {
			String constructionPattern = "";
			for (ConstructionComponent component : construction.getConstructionComponents()) {
				if (!component.getIsField()) {
					constructionPattern = constructionPattern + "( )" + "(" + component.getValue() + ")";
				} else if (component.getIsField() && component.getFieldType().equals("String")) {
					constructionPattern = constructionPattern + "( )" + "(\\s+)";
				} else if (component.getIsField() && component.getFieldType().equals("Integer")) {
					constructionPattern = constructionPattern + "( )" + "(\\d+)";
				}
			}
			constructionPattern = constructionPattern.substring(3);
			System.out.println(constructionPattern);
			Pattern pattern = Pattern.compile(constructionPattern);
			Matcher matcher = pattern.matcher(request);
			System.out.println(matcher.matches());

			String[] temp = constructionPattern.split(" ");
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].contains("\\d+") || temp[i].contains("\\s+")) {
					values.add(requestParts[i]);
				}
			}
			
			if (matcher.matches()) {
				returnConstruction = construction;
				System.out.println(construction.getName());
				break;
			}
		}
		return returnConstruction;
	}

	public static List<Construction> getAllConstructions() {
		List<Construction> constructions = new ArrayList<Construction>(0);

		Construction construction = new Construction("PutTrain");
		ConstructionComponent putText = new ConstructionComponent("put train", "String", false, "String");
		ConstructionComponent trainId = new ConstructionComponent("trainId", "Integer", true, "TrainInfo");
		ConstructionComponent onText = new ConstructionComponent("on railway", "String", false, "String");
		ConstructionComponent railwayId = new ConstructionComponent("railwayId", "Integer", true, "RailwayInfo");
		List<ConstructionComponent> componentsList = new ArrayList<ConstructionComponent>(4);
		componentsList.add(putText);
		componentsList.add(trainId);
		componentsList.add(onText);
		componentsList.add(railwayId);
		construction.setConstructionComponents(componentsList);

		constructions.add(construction);

		return constructions;
	}

	public static void main(String[] args) {
		getComponents("put train 12 on railway 25");
	}
}
