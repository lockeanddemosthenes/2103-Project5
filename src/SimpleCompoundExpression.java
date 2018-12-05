import java.util.ArrayList;
import java.util.List;

public class SimpleCompoundExpression implements Expression, CompoundExpression { 	
	private List<Expression> _children;
	private String _name;
	private CompoundExpression _parent;
	public SimpleCompoundExpression(String str) {
		_name = str;
		_children = new ArrayList<Expression>();
		
	}
	
	public List<Expression> getChildren(){
		return _children;
	}
	
	@Override
	public void addSubexpression(Expression subexpression) {
		_children.add(subexpression);
	}
	
	@Override
	public void setParent(CompoundExpression parent) {
		_parent = parent;
	}

	@Override
	public Expression deepCopy() {
		//TODO Implement for R2 
		return null;
	}
	
	public String getName() {
		return _name;
	}

	@Override
	public void flatten() {
		 for (Expression child : _children) {
			 child.flatten();
			 if(((SimpleCompoundExpression) child).getName() == getName()) {
				 
			 }
		 }
	}
	
	@Override
	public void convertToString(StringBuilder stringBuilder, int indentLevel) {
		Expression.indent(stringBuilder, indentLevel);
		stringBuilder.append(_name+"\n");
		for (Expression child : getChildren() ) {
			stringBuilder.append(child.convertToString(indentLevel+1));
		}
	}

	

	@Override
	public CompoundExpression getParent() {
		return _parent;
	}
	
}
