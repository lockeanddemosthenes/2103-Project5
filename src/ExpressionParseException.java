/**
 * Exception thrown when a ExpressionParser fails to parse a specified string.
 */
class ExpressionParseException extends Exception {
	public ExpressionParseException (String message) {
		super(message);
	}
}
