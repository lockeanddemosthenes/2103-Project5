import javafx.application.Application;
import java.util.*;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ExpressionEditor extends Application {
	public static void main (String[] args) {
		launch(args);
	}
	private static Expression copy = null;
	/**
	 * Mouse event handler for the entire pane that constitutes the ExpressionEditor
	 */
	private static class MouseEventHandler implements EventHandler<MouseEvent> {
		private CompoundExpression _rootExpression;
		private Pane _pane;
		private static CompoundExpression focusedExpression = null;
		private static MouseEvent ogEvent = null;
		
		
		MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
			_pane = pane_;
			_rootExpression = rootExpression_;
			if (focusedExpression == null) focusedExpression = _rootExpression;
		}
		
		public void clearStyle(Node node) {
			if (node instanceof Text || node instanceof Label) return;
			((HBox) node).setBorder(null);
			for (Node child : ((HBox) node).getChildren()) {
				clearStyle(child);
			}
		}
		
		public void resetFocus() {
			clearStyle(_rootExpression.getNode());
			focusedExpression = _rootExpression;
		}
		
		public void refreshPane() {
			((SimpleCompoundExpression) _rootExpression).updateNode();
			_pane.getChildren().clear();
			_pane.getChildren().add(_rootExpression.getNode());
			_pane.getChildren().add(copy.getNode());
			_rootExpression.getNode().setLayoutX(WINDOW_WIDTH/4);
			_rootExpression.getNode().setLayoutY(WINDOW_HEIGHT/2);
		}
		
		public void handle (MouseEvent event) {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				List<Expression> children = ((SimpleCompoundExpression) focusedExpression).getChildren();
				
				if (children.size()==0&&!focusedExpression.getNode().isPressed()) {
					resetFocus();
				}
				
				for (int i = 0; i <children.size(); i++) {
					if (children.get(i).getNode() instanceof HBox) {
						
						if (focusedExpression.getNode().isPressed()) {
							((HBox) children.get(i).getNode()).setBorder(null);
							
							if (children.get(i).getNode().isPressed()) {
							    ((HBox) children.get(i).getNode()).setBorder(new Border(new BorderStroke(
							    		Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))); //set the border
							    
							    focusedExpression = (CompoundExpression) ((SimpleCompoundExpression) focusedExpression).getChildren().get(i);
							}
						} else {
							resetFocus();
						}
					} 
				}
			} else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				if (focusedExpression == _rootExpression) return;
				
				if (ogEvent == null) {
					ogEvent = event;
				}
				if (copy == null) {
					copy = focusedExpression.deepCopy();
					_pane.getChildren().add(copy.getNode());
				}
				focusedExpression.getNode().setOpacity(.5);
				
				final double newX = event.getX()-focusedExpression.getNode().getParent().sceneToLocal(ogEvent.getSceneX(),ogEvent.getSceneY()).getX();
				final double newY = event.getY()-focusedExpression.getNode().getParent().sceneToLocal(ogEvent.getSceneX(),ogEvent.getSceneY()).getY();
				((HBox) copy.getNode()).setTranslateX(newX);
				((HBox) copy.getNode()).setTranslateY(newY);
				try {
					Expression rightSibling  = ((SimpleCompoundExpression) focusedExpression.getParent()).getChildren()
							.get(((SimpleCompoundExpression) focusedExpression.getParent()).getChildren().indexOf(focusedExpression)+1);
					
					final Point2D targetCoord = focusedExpression.getParent().getNode()
							.localToScene(rightSibling.getNode().getLayoutX(), rightSibling.getNode().getLayoutY());
					final Point2D originalCoord = focusedExpression.getNode()
							.localToScene(focusedExpression.getNode().getLayoutX(), focusedExpression.getNode().getLayoutY());
					final Point2D copyCoord = copy.getNode()
							.localToScene(copy.getNode().getLayoutX(), copy.getNode().getLayoutY());
					System.out.println(copy.getNode().getTranslateX()+focusedExpression.getNode().getLayoutBounds().getMaxX());
					System.out.println("beebop");
					System.out.println(targetCoord);
					if (copyCoord.distance(originalCoord) > copyCoord.distance(targetCoord)){//the leftmost bounds of the focused expression
						
						System.out.println("RIGHT");
						switchElements(((SimpleCompoundExpression) focusedExpression.getParent()).getChildren(), focusedExpression, rightSibling);
						refreshPane();
						//System.out.println(_rootExpression.convertToString(1));
						return;
					}
				} catch (IndexOutOfBoundsException e) {}
				try {
					Expression leftSibling  = ((SimpleCompoundExpression) focusedExpression.getParent()).getChildren()
							.get(((SimpleCompoundExpression) focusedExpression.getParent()).getChildren().indexOf(focusedExpression)-1);
					
					final Point2D targetCoord = focusedExpression.getParent().getNode()
							.localToScene(leftSibling.getNode().getLayoutX(), leftSibling.getNode().getLayoutY());
					final Point2D originalCoord = focusedExpression.getNode()
							.localToScene(focusedExpression.getNode().getLayoutX(), focusedExpression.getNode().getLayoutY());
					final Point2D copyCoord = copy.getNode()
							.localToScene(copy.getNode().getLayoutX(), copy.getNode().getLayoutY());
					
					if (copyCoord.distance(originalCoord) > copyCoord.distance(targetCoord)) { //the rightmost bounds of the focused expression
						System.out.println("LEFT");
						switchElements(((SimpleCompoundExpression) focusedExpression.getParent()).getChildren(), focusedExpression, leftSibling);
						
						refreshPane();
						//System.out.println(_rootExpression.convertToString(1));
						return;
						
					}
				} catch (IndexOutOfBoundsException e) {} 
				
			} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				if (focusedExpression == _rootExpression) return;
				if (ogEvent!=null)	{
					((HBox) focusedExpression.getNode()).setTranslateX(0);
					((HBox) focusedExpression.getNode()).setTranslateY(0);
					focusedExpression.getNode().setOpacity(1);
					_pane.getChildren().remove(copy.getNode());
					copy = null;
					ogEvent = null;
				}
			}
		}
	}

	/**
	 * Size of the GUI
	 */
	private static final int WINDOW_WIDTH = 500, WINDOW_HEIGHT = 250;

	/**
	 * Initial expression shown in the textbox
	 */
	private static final String EXAMPLE_EXPRESSION = "2*x+3*y+4*z+(7+6*z)";

	/**
	 * Parser used for parsing expressions.
	 */
	private final ExpressionParser expressionParser = new SimpleExpressionParser();

	@Override
	public void start (Stage primaryStage) {
		primaryStage.setTitle("Expression Editor");

		// Add the textbox and Parser button
		final Pane queryPane = new HBox();
		final TextField textField = new TextField(EXAMPLE_EXPRESSION);
		final Button button = new Button("Parse");
		queryPane.getChildren().add(textField);

		final Pane expressionPane = new Pane();

		// Add the callback to handle when the Parse button is pressed	
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle (MouseEvent e) {
				// Try to parse the expression
				try {
					// Success! Add the expression's Node to the expressionPane
					final Expression expression = expressionParser.parse(textField.getText(), true);
					System.out.println(expression.convertToString(0));
					expressionPane.getChildren().clear();
					expressionPane.getChildren().add(expression.getNode());
					expression.getNode().setLayoutX(WINDOW_WIDTH/4);
					expression.getNode().setLayoutY(WINDOW_HEIGHT/2);
					// If the parsed expression is a CompoundExpression, then register some callbacks
					if (expression instanceof CompoundExpression) {
						((Pane) expression.getNode()).setBorder(Expression.NO_BORDER);
						final MouseEventHandler eventHandler = new MouseEventHandler(expressionPane, (CompoundExpression) expression);
						expressionPane.setOnMousePressed(eventHandler);
						expressionPane.setOnMouseDragged(eventHandler);
						expressionPane.setOnMouseReleased(eventHandler);
					}
				} catch (ExpressionParseException epe) {
					// If we can't parse the expression, then mark it in red
					textField.setStyle("-fx-text-fill: red");
				}
			}
		});
		
		queryPane.getChildren().add(button);

		// Reset the color to black whenever the user presses a key
		textField.setOnKeyPressed(e -> textField.setStyle("-fx-text-fill: black"));
		
		final BorderPane root = new BorderPane();
		root.setTop(queryPane);
		root.setCenter(expressionPane);

		primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
		primaryStage.show();
	}
	
	public static <T> void switchElements(List<T> list, T firstElement, T secondElement) {
		int firstI = list.indexOf(firstElement);
		int secondI = list.indexOf(secondElement);
		
		list.set(firstI, secondElement);
		list.set(secondI, firstElement);
	}
	
}
