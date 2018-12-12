import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

public abstract class CompoundExpressionAbstract implements Expression {
	private List<Expression> _children;
	CompoundExpression _parent;
	private HBox _node;
	private String _name;
	
	void addSubexpression (Expression subexpression) {
	}
	
	@Override
	public CompoundExpression getParent() {
		return _parent;
	}

	@Override
	public Node getNode() {
		return _node;
	}

	@Override
	public void setParent(CompoundExpression parent) {
		_parent = parent;
		((SimpleCompoundExpression) _parent).setNode(_node);
	}

	@Override
	public Expression deepCopy() {
		// TODO Implement for R2
		return null;
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
		 _node = (HBox) createNode(); //freshen the node
	}

	@Override
	public void convertToString(StringBuilder stringBuilder, int indentLevel) {
		Expression.indent(stringBuilder, indentLevel);
		stringBuilder.append(_name+"\n");
		for (Expression child : getChildren() ) {
			stringBuilder.append(child.convertToString(indentLevel+1));
		}
	}
	
	public List<Expression> getChildren(){
		return _children;
	}
	
	public String getName() {
		return _name;
	}
	
	public Node createNode() {
		for (Expression child : getChildren())
			_node.getChildren().add(child.getNode());
		return _node;
	}
	
}
