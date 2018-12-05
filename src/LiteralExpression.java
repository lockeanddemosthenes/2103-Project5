
public class LiteralExpression implements Expression {
	String sValue;
	Integer iValue;
	String _name;
	CompoundExpression _parent;

	@Override
	public CompoundExpression getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(CompoundExpression parent) {
		_parent = parent;
		
	}

	@Override
	public Expression deepCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flatten() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void convertToString(StringBuilder stringBuilder, int indentLevel) {
		Expression.indent(stringBuilder, indentLevel);
		stringBuilder.append(_name+"\n");
		
	}

	public LiteralExpression(String s) {
		_name = s;
	}
}
