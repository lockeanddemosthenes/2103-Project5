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
	
	
	/**
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
			return new LiteralExpression(str);
		}
		else if (isValidInteger(str)) {
			return new LiteralExpression(Integer.parseInt(str));
		}
		//parenthetical case
		else if (str.charAt(0)=='('&&str.charAt(str.length()-1) == ')') {
			return handleParentheticalExpression(str.substring(1, str.length()-1));
		}
		
		//multiplicative case
		Expression multEx = splitAtSymbol('*', str);
		if (multEx != null) {
			return multEx;
		}
		
		//additive case
		Expression addEx = splitAtSymbol('+', str);
		if (addEx != null) {
			return addEx;
		}
		
		
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
			int start = 0;
			String temp = str;
			System.out.println("test paren "+str);
			System.out.println("op index " + str.indexOf(op));
			if (str.contains("(")&&(str.indexOf('(') < str.indexOf(op))) {
				start = str.indexOf(')')+1;
				System.out.println("hit " + start);
				temp = str.substring(start);
			}
			String first = str.substring(0 , temp.indexOf(op)+start);
			System.out.println("first " + first);
			String rest = str.substring(temp.indexOf(op)+1+start, str.length());
			System.out.println("rest " + rest);
			
			CompoundExpression out = new SimpleCompoundExpression();
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
	
	private Expression handleParentheticalExpression(String str) {
		CompoundExpression out = new ParentheticalExpression();

		Expression middle = parseExpression(str);
		out.addSubexpression(middle);
		middle.setParent(out);
		return out;
	}
	
	public boolean validateTest(String str) {
		return validateExpression(str);
	}
}
