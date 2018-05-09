package my.project;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Modelling {
	ArrayList<IntVar> vars = new ArrayList<IntVar>(0);
	ArrayList<IntVar> results = new ArrayList<IntVar>(0);
	ArrayList<String> constraints = null;
	ArrayList<String> domainLines = null;
	Hashtable<String, Constraint> constraintTable = null;
	IntVar res = null;
	String resName = "";
	int start = 0;
	int finish = 0;
	
	public void method(String ruleName) {
		
	}

	public Modelling(ArrayList<String> firstLines, ArrayList<String> domainLines,
			Hashtable<String, Constraint> constraintTable) {
	//	this.firstLines = firstLines;
		for (int i = 0; i < firstLines.size(); i++) {
			System.out.println("firstline " + firstLines.get(i));
		}
		for (int i = 0; i < domainLines.size(); i++) {
			System.out.println("domainline " + domainLines.get(i));
		}
		this.domainLines = domainLines;
		this.constraintTable = constraintTable;
	}

	// returns a choco model
	public Model buildModel() {

		Model model = new Model("some model");
		start = 0;
		finish = 0;
		for (int k = 0; k < constraints.size(); k++) {
			String line = constraints.get(k);
			start = finish;
			sum(line, model);
		}
		for (int i = 0; i < domainLines.size(); i++) {
			String line = domainLines.get(i);
			checkDomain(line, model);
		}
		return model;
	}
	
	public void getConstraintsSystem(String fileName, Map input) {
		ArrayList constraints = (ArrayList) ReadWriteFiles.readFromFile(fileName);
		;
	}

	public void sum(String line, Model model) {
		int temp = -1;
		int currSpace = 0;
		int eqPlace = 0;
		boolean waitingForMultiplying = false;
		boolean makeOpposite = false;
		IntVar firstM = null;
		for (int i = 0; i < line.length(); i++) {
			char currChar = line.charAt(i);
			if (currChar == ' ' && eqPlace == 0) {
				currSpace = i;
				String name = line.substring(temp + 1, currSpace);
				if (name.equals("-")) {
					makeOpposite = true;
				} else if (name.equals("*")) {
					finish--;
					firstM = vars.remove(finish);
					waitingForMultiplying = true;
				} else if (!name.equals("+") && !name.equals("*") && !name.equals("-")) {
					Constraint c = constraintTable.get(name);
					if (c.type.equals("int")) {
						IntVar intSummand = model.intVar(name, Integer.valueOf(c.value.toString()));
						// if (!varExists(name)){
						if (waitingForMultiplying) {
							IntVar tempMult = model.intScaleView(firstM, intSummand.getValue());
							IntVar multipliedRes = model.intVar(firstM.getName() + "*" + intSummand.getName(),
									tempMult.getValue());
							vars.add(multipliedRes);
							waitingForMultiplying = false;
						} else {
							if (makeOpposite) {
								
								IntVar opposite = model.intVar(name, -intSummand.getValue());
								vars.add(opposite);
								makeOpposite = false;
							} else {
								vars.add(intSummand);
							}
						}
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
						// if (!varExists(name))
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
			for (int k = 0; k < vars.size(); k++) {
				System.out.println("vars" + i + " is " + vars.get(k));
			}
		}
		IntVar[] intVars = new IntVar[finish - start];
		for (int i = start; i < finish; i++) {
			intVars[i - start] = vars.get(i);
			System.out.println("intvar " + intVars[i - start]);
		}
		System.out.println("res " + res);
		model.sum(intVars, "<=", res).post();
	}

	public boolean varExists(String name) {
		for (int i = 0; i < vars.toArray().length; i++) {
			if (vars.get(i).getName().equals(name))
				return true;
		}
		return false;
	}

	public void checkDomain(String line, Model model) {
		IntVar varD = null;
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
				if (!name.equals("+") && !Dprocessed) {
					Constraint c = constraintTable.get(name);
					if (c.type.equals("intInterval")) {
						String s[] = c.value.toString().substring(1, c.value.toString().length() - 1).split(",");
						varD = model.intVar(name, Integer.valueOf(s[0]), Integer.valueOf(s[1]));
					}
					Dprocessed = true;
				}
				if (!name.equals("+") && Dprocessed) {
					Constraint c = constraintTable.get(name);
					if (c.type.equals("intInterval")) {
						String s[] = c.value.toString().substring(1, c.value.toString().length() - 1).split(",");
						varF = model.intVar(name, Integer.valueOf(s[0]), Integer.valueOf(s[1]));
					}
				}
				temp = currSpace;
			}

			else if (currChar == ' ' && eqPlace != 0) {
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
		if (domainsUnited.length < (varD.getDomainSize() + varF.getDomainSize())) {
			model.arithm(model.intVar(1), "<", 0).post();
			System.out.println("doesnt fit");
		} else {
			model.arithm(model.intVar(1), ">", 0).post();
			System.out.println("fits");
		}
	}

	// returns a list of choco solutions
	public ArrayList<String> getSolution(Model m) throws NoSolutionException {
		ArrayList<String> solutions = new ArrayList<String>(0);
		// Object[] temp = vars.toArray();
		int counter = 0;
		// m.getSolver().solve();
		if (m.getSolver().solve()) {
			counter++;
			// System.out.println("solved");

			System.out.println(constraintTable.get(resName).value);
		}
		if (counter == 0) {
			throw new NoSolutionException();
		}

		System.out.println("constraint" + constraints.get(0));
		String[] output = constraints.get(0).split(" ");
		String railway = "";
		for (int i = 0; i < output.length; i++) {
			if (output[i].equals("<=")) {
				railway = output[i + 1].substring(1);
				System.out.println("rail" + railway);
				break;
			}
		}
		for (int j = 0; j < output.length; j++) {
			if (output[j].equals("<="))
				break;
			else if (!output[j].equals("+")) {
				System.out.println("output " + output[j]);
				String train = output[j].substring(1);
				System.out.println("train " + train);
				String temp = "D" + train;
				Constraint startNickC = constraintTable.get(temp);
				System.out.println("name is " + temp);
				System.out.println("st " + startNickC.value);
				String startNick = String.valueOf(startNickC.value).substring(1).split(",")[0];
				String str = "insert into allocation values(" + train + "," + railway + "," + startNick + ");";
				System.out.println(str);
				solutions.add(str);
			}
		}
		/*
		 * for (int i = 0; i < temp.length; i++) System.out.println(vars.get(i));
		 * System.out.println(res);
		 */
		return solutions;
	}
	
	public static void main(String args[]) {
		ArrayList<String> firstLines = new ArrayList<String>(1);
		firstLines.add("a1 + b1 * f1 + g1 * s1 * t1 - u1 <= c1");
		Hashtable<String, Constraint> table = new Hashtable<>();
		table.put("a1", new Constraint("1"));
		table.put("b1", new Constraint("{2,3}"));
		table.put("c1", new Constraint("4"));
		table.put("f1", new Constraint("1"));
		table.put("g1", new Constraint("1"));
		table.put("s1", new Constraint("3"));
		table.put("t1", new Constraint("3"));
		table.put("u1", new Constraint("8"));
		table.put("D1", new Constraint("2"));
		ArrayList<String> domainLines = new ArrayList(0);
		Modelling mo = new Modelling(firstLines, domainLines, table);
		mo.constraints = firstLines;
		Model model = mo.buildModel();
		try {
			mo.getSolution(model);
		} catch (NoSolutionException e) {
			e.printStackTrace();
		}
	}
}