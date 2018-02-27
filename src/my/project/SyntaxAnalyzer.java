package my.project;

import java.util.ArrayList;

public class SyntaxAnalyzer {

	public SqlLexer sqlLex;
	public int curState;

	public SyntaxAnalyzer(SqlLexer sqlLex) {
		this.sqlLex = sqlLex;
		curState = -1;
	}
	
	public ArrayList<Node> buildTree() {
		ArrayList<Node> treeNodes = new ArrayList<Node>(0);
		sqlLex.parse();
		Token tok = sqlLex.nextToken();
		curState++;
		boolean andWasUsed = false;
		for (;curState < sqlLex.numberOfTokens-1;) {			
			if (tok.type.equals("word") && tok.value.equals("SELECT")){
				ArrayList<Object> operands = new ArrayList<Object>(0);
				Token t = sqlLex.nextToken();
				curState++;
				while(!(t.type.equals("word") && t.value.equals("FROM"))){
					if (t.type.equals("symbol") && t.value.equals(",")){
						t = sqlLex.nextToken();
						curState++;
					}
					else if (t.type.equals("term")){
						operands.add(t.value);
						t = sqlLex.nextToken();
						curState++;
					}
					else if (t.type.equals("symbol") && t.value.equals("*")){
						operands.add(t.value);
						t = sqlLex.nextToken();
						curState++;
					}
				}
				tok = t;
				treeNodes.add(new Node("SELECT", operands));
			}
			else if (tok.type.equals("word") && tok.value.equals("FROM")){
				ArrayList<Object> operands = new ArrayList<Object>(0);
				Token t = sqlLex.nextToken();
				curState++;
				while(!(t.type.equals("word") && t.value.equals("WHERE"))){
					if (t.type.equals("symbol") && t.value.equals(",")){
						t = sqlLex.nextToken();
						curState++;
					}
					else if (t.type.equals("term")){
						operands.add(t.value);
						t = sqlLex.nextToken();
						curState++;
					}
				}
				tok = t;
				treeNodes.add(new Node("FROM", operands));
			}
			else if (tok.type.equals("word") && tok.value.equals("WHERE")){				
				ArrayList<Object> operands = new ArrayList<Object>(0);
				Token t = sqlLex.nextToken();
				curState++;
				while (!(t.type.equals("word") && t.value.equals("AND"))){
					if (t.type.equals("term")){
						operands.add(t.value);
						t = sqlLex.nextToken();
						curState++;
					}
					else if (t.type.equals("symbol") && (t.value.equals("=")
							|| t.value.equals(">") || t.value.equals("<"))){
						operands.add(t.value);
						t = sqlLex.nextToken();
						curState++;
					}
					else {
						t = sqlLex.nextToken();
						curState++;
					}
				}
				tok = t;
				treeNodes.add(new Node("FIRST", operands));
			}
			else if (tok.type.equals("word") && tok.value.equals("AND") && !andWasUsed){
				ArrayList<Object> operands = new ArrayList<Object>(0);
				Token t = sqlLex.nextToken();
				curState++;
				while (!(t.type.equals("word") && t.value.equals("AND"))){
					if (t.type.equals("term") || (t.type.equals("symbol") && t.value.equals("="))){
						operands.add(t.value);
						t = sqlLex.nextToken();
						curState++;
					}
					else {
						t = sqlLex.nextToken();
						curState++;
					}
				}
				tok = t;
				treeNodes.add(new Node("SECOND", operands));
				andWasUsed = true;
			}
			else if (tok.type.equals("word") && tok.value.equals("AND") && andWasUsed){				
				ArrayList<Object> operands = new ArrayList<Object>(0);
				Token t = sqlLex.nextToken();
				curState++;
				while (!(t.type.equals("symbol") && t.value.equals(";"))){
					if (t.type.equals("term") || (t.type.equals("symbol") && (t.value.equals("=")
							|| t.value.equals(">") || t.value.equals("<")))){
						operands.add(t.value);
						t = sqlLex.nextToken();
						curState++;
					}
					else {
						t = sqlLex.nextToken();
						curState++;
					}
				}
				tok = t;
				treeNodes.add(new Node("THIRD", operands));
			}
		}
		return treeNodes;
	}
}
