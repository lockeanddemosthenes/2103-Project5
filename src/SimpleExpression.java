import java.util.List;

public class SimpleExpression implements Expression{
	private String _name;
	private CompoundExpression _parent;
	
	@Override
	public void setParent(CompoundExpression parent) {
		_parent = parent;
	}

	@Override
	public Expression deepCopy() {
		SimpleCompoundExpression  copy = new SimpleCompoundExpression(new String(_name));
		 for (Expression child : ) {
			 copy._children.add((Expression) child.deepCopy()); // recurse
		 }
		 return copy;
	}
	
	public String getName() {
		return _name;
	}

	@Override
	public void flatten() {
		 for (Expression child : _children) {
			 if(child == _parent) {
				 
			 }
		 }
	}
	
	@Override
	public void convertToString(StringBuilder stringBuilder, int indentLevel) {
		Expression.indent(stringBuilder, indentLevel);
		stringBuilder.append(_name+"\n");
		for (Expression child : _children) {
			stringBuilder.append(child.convertToString(indentLevel+1));
		}
	}

	

	@Override
	public CompoundExpression getParent() {
		return _parent;
	}
}
