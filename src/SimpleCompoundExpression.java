import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;

public class SimpleCompoundExpression implements Expression, CompoundExpression { 	
	private List<Expression> _children;
	private String _name;
	private CompoundExpression _parent;
	private Node _node;
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
		 for (int i=0; i<getChildren().size(); i++) {
			 SimpleCompoundExpression child = (SimpleCompoundExpression) getChildren().get(i);
			 child.flatten();
			 if((child.getName().equals(this.getName()))) {
				 for(int j=0; j<child.getChildren().size(); j++) {
					 this.addSubexpression(child.getChildren().get(j));
				 }
				 getChildren().remove(child);
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

	@Override
	public Node getNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setNode(Node _node) {
		this._node = _node;
	}
	
}
