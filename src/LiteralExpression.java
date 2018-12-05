
public class LiteralExpression implements Expression {
	String sValue;
	Integer iValue;

	@Override
	public CompoundExpression getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(CompoundExpression parent) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}
	public LiteralExpression(Integer i) {
		iValue = i;
		sValue = null;
	}
	public LiteralExpression(String s) {
		sValue = s;
		iValue = null;
	}
}
