import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCompoundExpression implements CompoundExpression {
	private CompoundExpression _parent;
	private List<Expression> _children;
 	
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
		AbstractCompoundExpression  copy = new AbstractCompoundExpression();
		 for (Expression child : _children) {
			 copy._children.add((Expression) child.deepCopy()); // recurse
		 }
		 return copy;
	}

	@Override
	public void flatten() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void convertToString(StringBuilder stringBuilder, int indentLevel) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addSubexpression(Expression subexpression) {
		_children.add(subexpression);
	}
}
