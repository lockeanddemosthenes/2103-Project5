import java.util.ArrayList;
import java.util.List;

public class SimpleCompoundExpression extends SimpleExpression implements CompoundExpression { 	
	private List<Expression> _children;
	public SimpleCompoundExpression(String str) {
		_children = new ArrayList<Expression>();
		_name = str;
	}
	
	public List<Expression> getChildren(){
		return _children;
	}
	
	
	
	@Override
	public void addSubexpression(Expression subexpression) {
		_children.add(subexpression);
	}

	
}
