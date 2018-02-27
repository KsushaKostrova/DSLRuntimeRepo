package my.project;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class ExternalParser implements Serializable {
	String putPattern = "^( )*(put)( )+(train)( )+\\d+( )+(on)( )+\\d+( )+(with)( )+\\d+( )*$";
	String moveBackPattern = "^( )*(move)( )+(back)( )+\\d+( )+(by)( )+\\d+( )*$";
	String moveForwardPattern = "^( )*(move)( )+(forward)( )+\\d+( )+(by)( )+\\d+( )*$";
	String relocatePattern = "^( )*(relocate)( )+(train)( )+\\d+( )+(to)( )+\\d+( )+(with)( )+\\d+( )*$";
	Hashtable<Integer, ArrayList<String>> relocateSqls;

	public ExternalParser() {
		relocateSqls = new Hashtable<Integer, ArrayList<String>>(0);
	}

	public Hashtable<Integer, ArrayList<String>> getRelocateSqls() {
		return relocateSqls;
	}

	public void parse(Map<String, ArrayList<Object>> operationsMap, String request) {
		String[] lines = request.split("\n");
		
		Set<String> operationsKeySet = operationsMap.keySet();
		operationsKeySet.toArray();
		
		for (int i = 0; i < lines.length; i++) {
			for(String key : operationsKeySet) {
				Pattern pattern = Pattern.compile(operationsMap.get(key).toString());
				Matcher matcher = pattern.matcher(lines[i]);
			}			
		}
	}

	public ArrayList<String> parse(String input, Connection conn) throws Exception {
		ArrayList<String> res = new ArrayList<String>(0);

		String result = "SELECT * FROM Trains T , Railways R WHERE ( ? ) AND ( _ ) AND ( ! ) ; ";
		String relocateSql = "delete from allocation where id_train = ? and id_railway = !";
		Hashtable<Integer, String> results = new Hashtable<Integer, String>();

		ArrayList<Integer> railwaysUsed = new ArrayList<Integer>(0);

		String[] lines = input.split("\n");
		int[] types = new int[lines.length];
		// 1 - put
		// 2 - relocate
		// 3 - move
		int counter = 0;
		for (int i = 0; i < lines.length; i++) {// ������� exception, ���� ���
			Pattern patternPut = Pattern.compile(putPattern);
			Matcher matcherPut = patternPut.matcher(lines[i]);
			Pattern p1 = Pattern.compile(relocatePattern);
			Matcher m1 = p1.matcher(lines[i]);
			Pattern p2 = Pattern.compile(moveBackPattern);
			Matcher m2 = p2.matcher(lines[i]);
			Pattern p3 = Pattern.compile(moveForwardPattern);
			Matcher m3 = p3.matcher(lines[i]);
			if (matcherPut.matches()) {
				counter++;
				types[i] = 1;
			} else if (m1.matches()) {
				counter++;
				types[i] = 2;
			} else if (m2.matches()) {
				counter++;
				types[i] = 3;
			} else if (m3.matches()) {
				counter++;
				types[i] = 4;
			}
		}
		if (counter == lines.length) {
			for (int i = 0; i < types.length; i++) {
				if (types[i] == 1) {
					int trainId = 0;
					int railwayId = 0;
					int startNick = 0;
					int spaceLocation = 0;
					int count = 0;
					for (int j = 0; j < lines[i].length(); j++) {
						if (isDigit(lines[i].charAt(j)) && count == 0) {
							spaceLocation = j;// -1?
							count++;
						}
						if ((isSpace(lines[i].charAt(j)) || j == lines[i].length() - 1) && spaceLocation != 0) {
							if (trainId == 0)
								trainId = Integer.valueOf(lines[i].substring(spaceLocation, j));
							else if (trainId != 0 && railwayId == 0)
								railwayId = Integer.valueOf(lines[i].substring(spaceLocation, j));
							else if (trainId != 0 && railwayId != 0 && startNick == 0) {
								if (j != lines[i].length() - 1)
									startNick = Integer.valueOf(lines[i].substring(spaceLocation, j));
								else if (j == lines[i].length() - 1)
									startNick = Integer.valueOf(lines[i].substring(spaceLocation));
							}
							spaceLocation = 0;
							count = 0;
						}
					}
					if (!isUsedRailway(railwayId, railwaysUsed)) {
						result = "SELECT * FROM Trains T , Railways R WHERE ( ? ) AND ( _ ) AND ( ! ) ; ";
						result = result.replace("?", "T = " + String.valueOf(trainId));
						result = result.replace("_", "S = " + String.valueOf(startNick));
						result = result.replace("!", "R = " + String.valueOf(railwayId));
						results.put(railwayId, result);
						res.add(result);
					} else {
						String temp = results.get(railwayId);
						results.remove(temp);
						res.remove(temp);
						boolean secondCondition = true;
						for (int j = 0; j < temp.length(); j++) {
							if (temp.charAt(j) == ')' && secondCondition) {
								String secondPart = temp.substring(j + 1);
								temp = temp.substring(0, j) + "OR T = " + String.valueOf(trainId) + " )" + secondPart;
								System.out.println("look " + temp);
								results.put(railwayId, temp);
								secondCondition = false;
								j = j + 9 + String.valueOf(trainId).length();
							} else if (temp.charAt(j) == ')' && !secondCondition) {
								String secondPart = temp.substring(j + 1);
								temp = temp.substring(0, j) + "OR S = " + String.valueOf(startNick) + " )" + secondPart;
								System.out.println("look here " + temp);
								results.put(railwayId, temp);
								secondCondition = false;
								break;
							}
						}
						res.add(temp);
					}
					railwaysUsed.add(railwayId);

				} else if (types[i] == 2) {
					int trainId = 0;
					int railwayId = 0;
					int startNick = 0;
					int spaceLocation = 0;
					int count = 0;
					for (int j = 0; j < lines[i].length(); j++) {
						if (isDigit(lines[i].charAt(j)) && count == 0) {
							spaceLocation = j;// -1?
							count++;
						}
						if ((isSpace(lines[i].charAt(j)) || j == lines[i].length() - 1) && spaceLocation != 0) {
							if (trainId == 0)
								trainId = Integer.valueOf(lines[i].substring(spaceLocation, j));
							else if (trainId != 0 && railwayId == 0)
								railwayId = Integer.valueOf(lines[i].substring(spaceLocation, j));
							else if (trainId != 0 && railwayId != 0 && startNick == 0) {
								if (j != lines[i].length() - 1)
									startNick = Integer.valueOf(lines[i].substring(spaceLocation, j));
								else if (j == lines[i].length() - 1)
									startNick = Integer.valueOf(lines[i].substring(spaceLocation));
							}
							spaceLocation = 0;
							count = 0;
						}
					}
					if (!isUsedRailway(railwayId, railwaysUsed)) {
						result = "SELECT * FROM Trains T , Railways R WHERE ( ? ) AND ( _ ) AND ( ! ) ; ";
						result = result.replace("?", "T = " + String.valueOf(trainId));
						result = result.replace("_", "S = " + String.valueOf(startNick));
						result = result.replace("!", "R = " + String.valueOf(railwayId));
						results.put(railwayId, result);
						res.add(result);
						relocateSql = "delete from allocation where id_train = ? and id_railway = !";
						relocateSql = relocateSql.replace("?", String.valueOf(trainId));
						int oldRailway = 0;
						try {

							PreparedStatement pr = conn
									.prepareStatement("select id_railway from allocation where id_train = ?");
							pr.setInt(1, trainId);
							ResultSet rs = pr.executeQuery();
							while (rs.next()) {
								oldRailway = rs.getInt(1);
							}
						} catch (SQLException e) {

						}
						relocateSql = relocateSql.replace("!", String.valueOf(oldRailway));
						ArrayList<String> relocateSqlsArr = new ArrayList<String>(0);
						relocateSqlsArr.add(relocateSql);
						relocateSqls.put(railwayId, relocateSqlsArr);
					} else {
						String temp = results.get(railwayId);
						results.remove(temp);
						res.remove(temp);
						boolean secondCondition = true;
						for (int j = 0; j < temp.length(); j++) {
							if (temp.charAt(j) == ')' && secondCondition) {
								String secondPart = temp.substring(j + 1);
								temp = temp.substring(0, j) + "OR T = " + String.valueOf(trainId) + " )" + secondPart;
								System.out.println("look " + temp);
								results.put(railwayId, temp);
								secondCondition = false;
								j = j + 9 + String.valueOf(trainId).length();
							} else if (temp.charAt(j) == ')' && !secondCondition) {
								String secondPart = temp.substring(j + 1);
								temp = temp.substring(0, j) + "OR S = " + String.valueOf(startNick) + " )" + secondPart;
								System.out.println("look here " + temp);
								results.put(railwayId, temp);
								secondCondition = false;
								break;
							}
						}
						res.add(temp);
						ArrayList<String> relocateSqlsArr = relocateSqls.get(railwayId);
						relocateSql = "delete from allocation where id_train = ? and id_railway = !";
						relocateSql = relocateSql.replace("?", String.valueOf(trainId));
						relocateSql = relocateSql.replace("!", String.valueOf(railwayId));
						relocateSqlsArr.add(relocateSql);
						relocateSqls.remove(railwayId);
						relocateSqls.put(railwayId, relocateSqlsArr);
					}
					railwaysUsed.add(railwayId);
				} else if (types[i] == 3) {
					int trainId = 0;
					int railwayId = 0;
					int startNick = 0;
					int spaceLocation = 0;
					int count = 0;
					for (int j = 0; j < lines[i].length(); j++) {
						if (isDigit(lines[i].charAt(j)) && count == 0) {
							spaceLocation = j;// -1?
							count++;
						}
						if ((isSpace(lines[i].charAt(j)) || j == lines[i].length() - 1) && spaceLocation != 0) {
							if (trainId == 0) {
								trainId = Integer.valueOf(lines[i].substring(spaceLocation, j));
								try {
									PreparedStatement pr = conn
											.prepareStatement("select id_railway from allocation where id_train=?");
									pr.setInt(1, trainId);
									ResultSet rs = pr.executeQuery();
									while (rs.next()) {
										railwayId = rs.getInt(1);
									}
								} catch (SQLException e) {

								}
							} else if (trainId != 0 && railwayId != 0 && startNick == 0) {
								if (j != lines[i].length() - 1)
									startNick = Integer.valueOf(lines[i].substring(spaceLocation, j));
								else if (j == lines[i].length() - 1)
									startNick = Integer.valueOf(lines[i].substring(spaceLocation));
								int temp = startNick;
								try {
									PreparedStatement pr = conn
											.prepareStatement("select startnick from allocation where id_train = ?");
									pr.setInt(1, trainId);
									ResultSet rs = pr.executeQuery();
									while (rs.next()) {
										startNick = rs.getInt(1) - temp;
									}
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							spaceLocation = 0;
							count = 0;
						}
					}
					if (!isUsedRailway(railwayId, railwaysUsed)) {
						result = "SELECT * FROM Trains T , Railways R WHERE ( ? ) AND ( _ ) AND ( ! ) ; ";
						result = result.replace("?", "T = " + String.valueOf(trainId));
						result = result.replace("_", "S = " + String.valueOf(startNick));
						result = result.replace("!", "R = " + String.valueOf(railwayId));
						results.put(railwayId, result);
						res.add(result);
						relocateSql = "delete from allocation where id_train = ? and id_railway = !";
						relocateSql = relocateSql.replace("?", String.valueOf(trainId));
						int oldRailway = 0;
						try {

							PreparedStatement pr = conn
									.prepareStatement("select id_railway from allocation where id_train = ?");
							pr.setInt(1, trainId);
							ResultSet rs = pr.executeQuery();
							while (rs.next()) {
								oldRailway = rs.getInt(1);
							}
						} catch (SQLException e) {

						}
						relocateSql = relocateSql.replace("!", String.valueOf(oldRailway));
						ArrayList<String> relocateSqlsArr = new ArrayList<String>(0);
						relocateSqlsArr.add(relocateSql);
						relocateSqls.put(railwayId, relocateSqlsArr);
					} else {
						String temp = results.get(railwayId);
						results.remove(temp);
						res.remove(temp);
						boolean secondCondition = true;
						for (int j = 0; j < temp.length(); j++) {
							if (temp.charAt(j) == ')' && secondCondition) {
								String secondPart = temp.substring(j + 1);
								temp = temp.substring(0, j) + "OR T = " + String.valueOf(trainId) + " )" + secondPart;
								System.out.println("look " + temp);
								results.put(railwayId, temp);
								secondCondition = false;
								j = j + 9 + String.valueOf(trainId).length();
							} else if (temp.charAt(j) == ')' && !secondCondition) {
								String secondPart = temp.substring(j + 1);
								temp = temp.substring(0, j) + "OR S = " + String.valueOf(startNick) + " )" + secondPart;
								System.out.println("look here " + temp);
								results.put(railwayId, temp);
								secondCondition = false;
								break;
							}
						}
						res.add(temp);
						ArrayList<String> relocateSqlsArr = relocateSqls.get(railwayId);
						relocateSql = "delete from allocation where id_train = ? and id_railway = !";
						relocateSql = relocateSql.replace("?", String.valueOf(trainId));
						relocateSql = relocateSql.replace("!", String.valueOf(railwayId));
						relocateSqlsArr.add(relocateSql);
						relocateSqls.remove(railwayId);
						relocateSqls.put(railwayId, relocateSqlsArr);
					}
					railwaysUsed.add(railwayId);
				} else if (types[i] == 4) {
					int trainId = 0;
					int railwayId = 0;
					int startNick = 0;
					int spaceLocation = 0;
					int count = 0;
					for (int j = 0; j < lines[i].length(); j++) {
						if (isDigit(lines[i].charAt(j)) && count == 0) {
							spaceLocation = j;// -1?
							count++;
						}
						if ((isSpace(lines[i].charAt(j)) || j == lines[i].length() - 1) && spaceLocation != 0) {
							if (trainId == 0) {
								trainId = Integer.valueOf(lines[i].substring(spaceLocation, j));
								try {
									PreparedStatement pr = conn
											.prepareStatement("select id_railway from allocation where id_train=?");
									pr.setInt(1, trainId);
									ResultSet rs = pr.executeQuery();
									while (rs.next()) {
										railwayId = rs.getInt(1);
									}
								} catch (SQLException e) {

								}
							} else if (trainId != 0 && railwayId != 0 && startNick == 0) {
								if (j != lines[i].length() - 1)
									startNick = Integer.valueOf(lines[i].substring(spaceLocation, j));
								else if (j == lines[i].length() - 1)
									startNick = Integer.valueOf(lines[i].substring(spaceLocation));
								int temp = startNick;
								System.out.println("temp" + temp);
								try {
									PreparedStatement pr = conn
											.prepareStatement("select startnick from allocation where id_train = ?");
									pr.setInt(1, trainId);
									ResultSet rs = pr.executeQuery();
									while (rs.next()) {
										startNick = rs.getInt(1) + temp;
										System.out.println("Staaart " + startNick);
									}
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							System.out.println("tr " + trainId + " " + "r " + railwayId);
							spaceLocation = 0;
							count = 0;
						}
					}
					if (!isUsedRailway(railwayId, railwaysUsed)) {
						result = "SELECT * FROM Trains T , Railways R WHERE ( ? ) AND ( _ ) AND ( ! ) ; ";
						result = result.replace("?", "T = " + String.valueOf(trainId));
						result = result.replace("_", "S = " + String.valueOf(startNick));
						result = result.replace("!", "R = " + String.valueOf(railwayId));
						results.put(railwayId, result);
						res.add(result);
						relocateSql = "delete from allocation where id_train = ? and id_railway = !";
						relocateSql = relocateSql.replace("?", String.valueOf(trainId));
						int oldRailway = 0;
						try {

							PreparedStatement pr = conn
									.prepareStatement("select id_railway from allocation where id_train = ?");
							pr.setInt(1, trainId);
							ResultSet rs = pr.executeQuery();
							while (rs.next()) {
								oldRailway = rs.getInt(1);
							}
						} catch (SQLException e) {

						}
						relocateSql = relocateSql.replace("!", String.valueOf(oldRailway));
						ArrayList<String> relocateSqlsArr = new ArrayList<String>(0);
						relocateSqlsArr.add(relocateSql);
						relocateSqls.put(railwayId, relocateSqlsArr);
					} else {
						String temp = results.get(railwayId);
						results.remove(temp);
						res.remove(temp);
						boolean secondCondition = true;
						for (int j = 0; j < temp.length(); j++) {
							if (temp.charAt(j) == ')' && secondCondition) {
								String secondPart = temp.substring(j + 1);
								temp = temp.substring(0, j) + "OR T = " + String.valueOf(trainId) + " )" + secondPart;
								System.out.println("look " + temp);
								results.put(railwayId, temp);
								secondCondition = false;
								j = j + 9 + String.valueOf(trainId).length();
							} else if (temp.charAt(j) == ')' && !secondCondition) {
								String secondPart = temp.substring(j + 1);
								temp = temp.substring(0, j) + "OR S = " + String.valueOf(startNick) + " )" + secondPart;
								System.out.println("look here " + temp);
								results.put(railwayId, temp);
								secondCondition = false;
								break;
							}
						}
						res.add(temp);
						ArrayList<String> relocateSqlsArr = relocateSqls.get(railwayId);
						relocateSql = "delete from allocation where id_train = ? and id_railway = !";
						relocateSql = relocateSql.replace("?", String.valueOf(trainId));
						relocateSql = relocateSql.replace("!", String.valueOf(railwayId));
						relocateSqlsArr.add(relocateSql);
						relocateSqls.remove(railwayId);
						relocateSqls.put(railwayId, relocateSqlsArr);
					}
					railwaysUsed.add(railwayId);
				}
			}
		} else
			throw new Exception();
		return res;
	}

	static boolean isDigit(char c) {
		if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8'
				|| c == '9')
			return true;
		return false;
	}

	static boolean isSpace(char c) {
		if (c == ' ')
			return true;
		return false;
	}

	static boolean isUsedRailway(int railway, ArrayList<Integer> list) {
		for (int i = 0; i < list.toArray().length; i++) {
			if (list.get(i) == railway)
				return true;
		}
		return false;
	}

	public void setPutParser(ArrayList<String> newPattern) {
		String newPat = "^( )*";
		String[] first = newPattern.get(0).split(" ");
		String[] second = newPattern.get(1).split(" ");
		String[] third = newPattern.get(2).split(" ");
		for (int i = 0; i < first.length; i++)
			newPat = newPat + "(" + first[i] + ")" + "( )+";
		newPat = newPat + "\\d+( )+";
		for (int i = 0; i < second.length; i++)
			newPat = newPat + "(" + second[i] + ")" + "( )+";
		newPat = newPat + "\\d+( )+";
		for (int i = 0; i < third.length; i++)
			newPat = newPat + "(" + third[i] + ")" + "( )+";
		newPat = newPat + "\\d+( )*$";
		putPattern = newPat;
	}

	public void setMoveParser(ArrayList<String> newPattern, String command) {
		String newPat = "^( )*";
		String[] first = newPattern.get(0).split(" ");
		String[] second = newPattern.get(1).split(" ");
		for (int i = 0; i < first.length; i++)
			newPat = newPat + "(" + first[i] + ")" + "( )+";
		newPat = newPat + "\\d+( )+";
		for (int i = 0; i < second.length; i++)
			newPat = newPat + "(" + second[i] + ")" + "( )+";
		newPat = newPat + "\\d+( )*$";
		if (command.equals("back"))
			moveBackPattern = newPat;
		else if (command.equals("forward"))
			moveForwardPattern = newPat;
	}

	public void setRelocateParser(ArrayList<String> newPattern) {
		String newPat = "^( )*";
		String[] first = newPattern.get(0).split(" ");
		String[] second = newPattern.get(1).split(" ");
		String[] third = newPattern.get(2).split(" ");
		for (int i = 0; i < first.length; i++)
			newPat = newPat + "(" + first[i] + ")" + "( )+";
		newPat = newPat + "\\d+( )+";
		for (int i = 0; i < second.length; i++)
			newPat = newPat + "(" + second[i] + ")" + "( )+";
		newPat = newPat + "\\d+( )+";
		for (int i = 0; i < third.length; i++)
			newPat = newPat + "(" + third[i] + ")" + "( )+";
		newPat = newPat + "\\d+( )*$";
		relocatePattern = newPat;
	}
}
