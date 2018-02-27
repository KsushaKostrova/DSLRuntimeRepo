package my.project;

import java.util.ArrayList;

public class SqlLexer {
	
	public int numberOfTokens;
	
	private static String words="WHERE,SELECT,FROM,JOIN,ON,AND,OR";
	private static String symbols="(),;<>=*";
	public ArrayList<Token> tokens = new ArrayList<Token>(0);
	String str;
	int curTokPos;
	
	public SqlLexer(String str) {
		this.str = str;
		curTokPos = -1;
	}
	
	public boolean isSymbol (String st){
		for (int i =0; i <symbols.length(); i++){
			if (st.equals(String.valueOf(symbols.charAt(i))))
				return true;
		}
		return false;
	}
	
	public boolean isWord (String tok){
		String [] tempWords = words.split(",");
		for (int i = 0; i < tempWords.length; i++) {
			if (tempWords[i].equals(tok))
				return true;
		}
		return false;
	}
	
	public void parse (){
		str = str + " ";
		String [] toks = str.split(" ");
		numberOfTokens = toks.length;
		for (int i = 0; i < toks.length; i++){
			if (isSymbol(toks[i])){
				tokens.add(new Token("symbol", toks[i]));
			}
			else if (isWord(toks[i])){
				tokens.add(new Token("word", toks[i]));
			}
			else {
				tokens.add(new Token("term", toks[i]));
			}
		}
	}
	
	public Token nextToken() {
		curTokPos++;
		return tokens.get(curTokPos);
	}
}
