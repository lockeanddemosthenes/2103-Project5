import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SimpleCompoundExpression implements Expression, CompoundExpression { 	
	private List<Expression> _children;
	private String _name;
	private CompoundExpression _parent;
	private HBox _node = null;
	public SimpleCompoundExpression(String str) {
		_name = str;
		_children = new ArrayList<Expression>();
		_node = (HBox) createNode();
	}
	
	public List<Expression> getChildren(){
		return _children;
	}
	
	@Override
	public void addSubexpression(Expression subexpression) {
		_children.add(subexpression);
		
		//if (!((HBox) getNode()).getChildren().contains(subexpression.getNode()))
		((HBox) getNode()).getChildren().add(subexpression.getNode());
		if (_parent!= null) ((SimpleCompoundExpression) _parent).updateNode();
		subexpression.setParent(this);
		
	}
	
	@Override
	public void setParent(CompoundExpression parent) {
		_parent = parent;
		((SimpleCompoundExpression) _parent).updateNode();
	}

	@Override
	public Expression deepCopy() {
		CompoundExpression copy = new SimpleCompoundExpression(new String(_name));
		for (Expression child : this.getChildren()) {
			child = child.deepCopy();
			copy.addSubexpression(child);
		}
		return copy;
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

	@Override
	public CompoundExpression getParent() {
		return _parent;
	}

	@Override
	public Node getNode() {
		if (_node == null) {
			_node = (HBox) createNode();
		}
		return _node;
	}
	
	public Node createNode() {
		
		if (getChildren().size() == 0) {
			_node = new HBox(new Text(_name));
			return _node;
		}
		_node = new HBox();
		if ("()".equals(_name)) { 
			_node.getChildren().add(new Label("("));
			_node.getChildren().add(getChildren().get(0).getNode());
			_node.getChildren().add(new Label(")"));
		}
		
		else if ("+".equals(_name)||"*".equals(_name)) {
			for (int i = 0; i<getChildren().size();i++) {
				_node.getChildren().add(getChildren().get(i).getNode());
				if (i != getChildren().size()-1) _node.getChildren().add(new Label(_name));
			}
		}
		else {
			_node = new HBox(new Text(_name));
		}
		return _node;
	}
	
	public void setNode(Node node) {
		_node = (HBox) node;
		((SimpleCompoundExpression) _parent).updateNode();
	}
	
	public void updateNode() {
		_node = (HBox) createNode();
	}
	
}
