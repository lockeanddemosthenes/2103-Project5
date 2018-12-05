import java.util.ArrayList;
import java.util.List;

public class SimpleCompoundExpression implements CompoundExpression {
	private CompoundExpression _parent;
	private List<Expression> _children;
	private String _name;
 	
	
	public SimpleCompoundExpression(String str) {
		_children = new ArrayList<Expression>();
		_name = str;
	}
	
	@Override
	public CompoundExpression getParent() {
		return _parent;
	}

	@Override
	public void setParent(CompoundExpression parent) {
		_parent = parent;
	}

	@Override
	public Expression deepCopy() {
		SimpleCompoundExpression  copy = new SimpleCompoundExpression(new String(_name));
		 for (Expression child : _children) {
			 copy._children.add((Expression) child.deepCopy()); // recurse
		 }
		 return copy;
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
	public void addSubexpression(Expression subexpression) {
		_children.add(subexpression);
	}
}
