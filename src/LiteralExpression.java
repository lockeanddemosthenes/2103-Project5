
public class LiteralExpression extends SimpleCompoundExpression {
	String sValue;
	Integer iValue;
	CompoundExpression _parent;

//	@Override
//	public Expression deepCopy() {
//		Expression copy = new LiteralExpression(new String(this.getName()));
//		//Doesn't need to iterate through the children
//		return copy;
//	}


	@Override
	public void convertToString(StringBuilder stringBuilder, int indentLevel) {
		Expression.indent(stringBuilder, indentLevel);
		stringBuilder.append(this.getName()+"\n");
		//Doesn't need to iterate through the children
		
	}
	
	
	public LiteralExpression(String s) {
		super(s);
	}
}
