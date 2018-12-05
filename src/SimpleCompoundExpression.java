import java.util.ArrayList;
import java.util.List;

public class SimpleCompoundExpression implements CompoundExpression {
	private CompoundExpression _parent;
	private List<Expression> _children;
 	
	
	public SimpleCompoundExpression() {
		_children = new ArrayList<Expression>();
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
		SimpleCompoundExpression  copy = new SimpleCompoundExpression();
		 for (Expression child : _children) {
			 copy._children.add((Expression) child.deepCopy()); // recurse
		 }
		 return copy;
	}

	@Override
	public void flatten() {
		 for (Expression child : _children) {
			 if(child == _parent && ()) {
				 
			 }
		 }
	}
	
	@Override
	public void convertToString(StringBuilder stringBuilder, int indentLevel) {
		String sym = "";
		if (this.getClass() == AdditiveExpression.class) {
			sym = "+";
		}
		else if (this.getClass() == MultiplicativeExpression.class){
			sym = "*";
		}
		else if (this.getClass() == ParentheticalExpression.class) {
			sym = "()";
		}
		else {
			
		}
		
	}

	@Override
	public void addSubexpression(Expression subexpression) {
		_children.add(subexpression);
	}
}
