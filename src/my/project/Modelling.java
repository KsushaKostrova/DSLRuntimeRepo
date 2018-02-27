package my.project;

import java.util.ArrayList;
import java.util.Hashtable;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Modelling {
	ArrayList<IntVar> vars = new ArrayList<IntVar>(0);
	ArrayList<IntVar> results = new ArrayList<IntVar>(0);
	ArrayList<String> firstLines = null;
	ArrayList<String> domainLines = null;
	Hashtable<String, Constraint> constraintTable = null;
	IntVar res = null;
	String resName = "";
	int start = 0;
	int finish = 0;

	public Modelling(ArrayList<String> firstLines, ArrayList<String> domainLines, Hashtable<String, Constraint> constraintTable) {
		this.firstLines = firstLines;
		this.domainLines = domainLines;
		this.constraintTable = constraintTable;
	}

	// returns a choco model
	public Model buildModel() {

		Model model = new Model("some model");
		start = 0;
		finish = 0;
		for (int k = 0; k < firstLines.toArray().length; k++) {
			String line = firstLines.get(k);
			start = finish;
			sum(line, model);
		}
		for (int i = 0; i < domainLines.toArray().length; i++){
			String line = domainLines.get(i);
			checkDomain(line, model);
		}
		return model;
	}
	
	public void sum(String line, Model model){
		int temp = -1;
		int currSpace = 0;
		int eqPlace = 0;
		boolean waitingForMultiplying = false;
		IntVar firstM = null;
		for (int i = 0; i < line.length(); i++) {
			char currChar = line.charAt(i);
			if (currChar == ' ' && eqPlace == 0) {
				currSpace = i;
				String name = line.substring(temp + 1, currSpace);
				if (name.equals("*")){
					finish--;
					firstM = vars.remove(finish);
					waitingForMultiplying = true;					
				}
				else if (!name.equals("+") && !name.equals("*")) {
					Constraint c = constraintTable.get(name);
					if (c.type.equals("int")) {
						IntVar intSummand = model.intVar(name, Integer.valueOf(c.value.toString()));
					//	if (!varExists(name)){
							if (waitingForMultiplying){
								IntVar tempMult = model.intScaleView(firstM, intSummand.getValue());
								IntVar multipliedRes = model.intVar(firstM.getName()+"*"+intSummand.getName(), tempMult.getValue());
								vars.add(multipliedRes);
								waitingForMultiplying = false;
							} else
								vars.add(intSummand);
					//	}
						finish++;
					} else if (c.type.equals("intAny")) {
						String constr = (String) c.value;
						String fill = constr.substring(1, constr.length() - 1);// remove
																				// '{'
																				// and
																				// '}'
						String s[] = fill.split(",");
						int[] vals = new int[s.length];
						for (int j = 0; j < vals.length; j++) {
							vals[j] = Integer.valueOf(s[j]);
						}
						IntVar intSummand = model.intVar(name, vals);
					//	if (!varExists(name))
							vars.add(intSummand);
						finish++;
					}
				}
				temp = currSpace;
			} else if (currChar == ' ' && eqPlace != 0) {
				currSpace = i;
				resName = line.substring(currSpace + 1);					
				Constraint c = constraintTable.get(resName);
				
				if (c.type.equals("int"))
					res = model.intVar(resName, Integer.valueOf(c.value.toString()));
				else if (c.type.equals("intAny")) {
						String constr = (String) c.value;
						String fill = constr.substring(1, constr.length() - 1);// remove
																				// '{'
																				// and
																				// '}'
						String s[] = fill.split(",");
						int[] vals = new int[s.length];
						for (int j = 0; j < vals.length; j++) {
							vals[j] = Integer.valueOf(s[j]);
						}
						res = model.intVar(resName, vals);
				}
				
				temp = currSpace;
			} else if (currChar == '<' || currChar == '=') {
				eqPlace = i;
			}
		}
		IntVar[] intVars = new IntVar[finish-start];
		for (int i = start; i < finish; i++) {
			intVars[i-start] = vars.get(i);
		}
		model.sum(intVars, "<=", res).post();
	}
	
	public boolean varExists(String name){
		for (int i = 0; i < vars.toArray().length; i++){
			if (vars.get(i).getName().equals(name))
				return true;
		}
		return false;
	}
	public void checkDomain(String line, Model model){
		IntVar varD= null;
		IntVar varF = null;
		int[] domainsUnited = null;
		boolean Dprocessed = false;
		int eqPlace = 0;		
		int currSpace = 0;
		int temp = -1;
		String name = "";
		for (int j = 0; j < line.length(); j++) {
			char currChar = line.charAt(j);
			if (currChar == ' ' && eqPlace == 0) {
				currSpace = j;
				name = line.substring(temp + 1, currSpace);
				if (!name.equals("+") && !Dprocessed){
					Constraint c = constraintTable.get(name);
					if (c.type.equals("intInterval")) {
						String s[] = c.value.toString().substring(1,
								 c.value.toString().length() - 1).split(",");
								 varD = model.intVar(name, Integer.valueOf(s[0]),
								 Integer.valueOf(s[1]));
					}
					Dprocessed = true;
				}
				if (!name.equals("+") && Dprocessed){
					Constraint c = constraintTable.get(name);
					if (c.type.equals("intInterval")) {
						String s[] = c.value.toString().substring(1,
								 c.value.toString().length() - 1).split(",");
								 varF = model.intVar(name, Integer.valueOf(s[0]),
								 Integer.valueOf(s[1]));
					}
				}
				temp = currSpace;
			}				
			
			else if (currChar == ' ' && eqPlace!=0){
				currSpace = j;
				name = line.substring(currSpace + 1);
				domainsUnited = model.getDomainUnion(varD, varF);
				temp = currSpace;
			}
			
			else if (currChar == '>') {
				currSpace = j;
				eqPlace = j;
				temp = currSpace;
			}
		}
		System.out.println(varD.getDomainSize());
		System.out.println(varF.getDomainSize());
		System.out.println(domainsUnited.length);
		if (domainsUnited.length < (varD.getDomainSize()+varF.getDomainSize())){
			model.arithm(model.intVar(1), "<", 0).post();
			System.out.println("doesnt fit");
		}
		else{
			model.arithm(model.intVar(1), ">", 0).post();
			System.out.println("fits");
		}
	}

	// returns a list of choco solutions
	public ArrayList<String> getSolution(Model m) throws NoSolutionException {
		ArrayList<String> solutions = new ArrayList<String>(0);
	//	Object[] temp = vars.toArray();
		int counter = 0;
//		m.getSolver().solve();
		if (m.getSolver().solve()){
			counter++;
		//	System.out.println("solved");
			
		//	System.out.println(constraintTable.get(resName).value);
		}
		if (counter == 0) {
			throw new NoSolutionException();
		}	
		
			String[] output = firstLines.get(0).split(" ");
			String railway = "";
			for (int i = 0; i < output.length; i++){
				if (output[i].equals("<=")){
					railway = output[i+1].substring(1);
					System.out.println("rail" + railway);
					break;
				}
			}
			for (int j = 0; j < output.length; j++){
				if (output[j].equals("<="))
					break;
				else if (!output[j].equals("+")){
					System.out.println("output "+output[j]);
					String train = output[j].substring(1);
					System.out.println("train "+train);
					String temp = "D" + train;
					Constraint startNickC = constraintTable.get(temp);
					System.out.println("name is " + temp);
					System.out.println("st " + startNickC.value);
					String startNick = String.valueOf(startNickC.value).substring(1).split(",")[0];
					String str = "insert into allocation values("+train+","+railway+","+startNick+");";
					System.out.println(str);
					solutions.add(str);
				}
			}
	/*	for (int i = 0; i < temp.length; i++)
			System.out.println(vars.get(i));
		System.out.println(res);*/
		return solutions;
	}
}