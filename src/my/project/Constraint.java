package my.project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constraint {
	String type;
	Object value;
	
	public Constraint(String c){
		Pattern pInt = Pattern.compile("(-){0,1}\\d+");
		Matcher mInt = pInt.matcher(c);
		Pattern pIntInterv = Pattern.compile("\\[(-){0,1}\\d+\\,(-){0,1}\\d+\\]");
		Matcher mIntInterv = pIntInterv.matcher(c);
		Pattern pIntAny = Pattern.compile("\\{((-){0,1}\\d+\\,)*(-){0,1}\\d+\\}");
		Matcher mIntAny = pIntAny.matcher(c);
		Pattern p1 = Pattern.compile("(-){0,1}\\d+\\.+\\d+\\,(-){0,1}\\d+\\.+\\d+"
				+ "");//!!!
		Matcher m1 = p1.matcher(c);
		Pattern pBool = Pattern.compile("true|false");
		Matcher mBool = pBool.matcher(c);
		if (mInt.matches()){
			type = "int";
			value = Integer.valueOf(c);
		}
		else if (mIntInterv.matches()){
			type = "intInterval";
			value = c;
		}
		else if (mIntAny.matches()){
			type = "intAny";
			value = c;
			System.out.println("intAny");
		}
		else if (m1.matches()){
			type = "double";
			value = Double.valueOf(c);
		}
		else if (mBool.matches()){
			type = "boolean";
			value = Boolean.valueOf(c);
		}
	}
	
	@Override
	public String toString(){
		return value.toString();
	}
}
