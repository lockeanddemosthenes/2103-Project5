/**
 * Starter code to implement an ExpressionParser. Your parser methods should use the following grammar:
 * E := A | X
 * A := A+M | M
 * M := M*M | X
 * X := (E) | L
 * L := [0-9]+ | [a-z]
 */
public class SimpleExpressionParser implements ExpressionParser {
	private static final String ALL_CHARS = "abcdefghijklmnopqrstuvwxyz";
	
	
	/*
	 * Attempts to create an expression tree -- flattened as much as possible -- from the specified String.
         * Throws a ExpressionParseException if the specified string cannot be parsed.
	 * @param str the string to parse into an expression tree
	 * @param withJavaFXControls you can just ignore this variable for R1
	 * @return the Expression object representing the parsed expression tree
	 */
	public Expression parse (String str, boolean withJavaFXControls) throws ExpressionParseException {
		// Remove spaces -- this simplifies the parsing logic
		str = str.replaceAll(" ", "");
		Expression expression = parseExpression(str);
		if (expression == null) {
			// If we couldn't parse the string, then raise an error
			throw new ExpressionParseException("Cannot parse expression: " + str);
		}
		
		try {
			return new LiteralExpression(Integer.parseInt(str));
		} catch (Exception e) {}
		
		
		
		// Flatten the expression before returning
		expression.flatten();
		return expression;
	}
	
	protected Expression parseExpression (String str) {
		Expression expression = null;
		if (!validateExpression(str)) return null;
		
		//literal cases
		if (ALL_CHARS.contains(str) && str.length() == 1) {
			expression = new LiteralExpression(str);
		}
		else if (isValidInteger(str)) {
			expression = new LiteralExpression(Integer.parseInt(str));
		}
		//multiplicative case
		Expression multEx = splitAtSymbol('*', str);
		if (multEx != null) {
			expression = multEx;
		}
		//additive case
		Expression addEx = splitAtSymbol('+', str);
		if (addEx != null) {
			expression = addEx;
			System.out.println("here");
		}
		//parenthetical case
		
		return expression;
	}	
	
	private boolean validateExpression(String str) {
		if (!str.equals("")) {
			try { 
				Integer.parseInt(str);
				return true;
			} catch (Exception e) {}
		}
		else return false;
		
		if (ALL_CHARS.contains(str)) {
			return true;
		}
		else if (validateHelper('+', str)) {
			return true;
		}
		else if (validateHelper('*', str)) {
			return true;
		}
		else if (str.charAt(0) == '(' && 
				str.charAt(str.length()-1) == ')' &&
				validateExpression(str.substring(1, str.length()-1))){
			return true;
		}
		return false;
	}

	private boolean validateHelper(char c, String str) {
		for (int i = 1; i < str.length() - 1; i++) {
			if (str.charAt(i) == c &&
				validateExpression(str.substring(0, i)) &&
				validateExpression(str.substring(i+1))) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isValidInteger(String str) {
		if (!str.equals("")) {
			try { 
				Integer.parseInt(str);
				return true;
			} catch (Exception e) {return false;}
		}
		else return false;
	}
	
	private Expression splitAtSymbol(char op, String str) {
		if (str.contains(""+op)) {
			String first = str.substring(0 , str.indexOf(op));
			String rest = str.substring(str.indexOf(op)+1, str.length());
			System.out.println(first);
			System.out.println(rest);
			SimpleCompoundExpression out = new SimpleCompoundExpression();
			Expression firstEx = parseExpression(first);
			Expression restEx = parseExpression(rest);
			
			out.addSubexpression(firstEx);
			out.addSubexpression(restEx);
			firstEx.setParent(out);
			restEx.setParent(out);
			return out;
		}
		else return null;
	}
	
	
	public boolean validateTest(String str) {
		return validateExpression(str);
	}
}
