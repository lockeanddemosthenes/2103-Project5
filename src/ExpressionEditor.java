import javafx.application.Application;
import java.util.*;

import javafx.geometry.Bounds;
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
		
		/**
		 * Mouse event handler initiator
		 * @param pane_ 
		 * @param rootExpression_ 
		 */
		public MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
			_pane = pane_;
			_rootExpression = rootExpression_;
			if (focusedExpression == null) focusedExpression = _rootExpression;
		}
		
		/**
		 * Resets the font style for the node.
		 * @param node the node with the font style being reset
		 */
		private void clearStyle(Node node) {
			if (node instanceof Text || node instanceof Label) return;
			((HBox) node).setBorder(null);
			for (Node child : ((HBox) node).getChildren()) {
				clearStyle(child);
			}
		}
		
		/**
		 * Resets the focus to an inner node.
		 */
		private void resetFocus() {
			clearStyle(_rootExpression.getNode());
			focusedExpression = _rootExpression;
		}
		
		/**
		 * Updates the pane.
		 */
		private void refreshPane() {
			((SimpleCompoundExpression) _rootExpression).updateNode();
			_pane.getChildren().clear();
			_pane.getChildren().add(_rootExpression.getNode());
			_pane.getChildren().add(copy.getNode());
			_rootExpression.getNode().setLayoutX(WINDOW_WIDTH/4);
			_rootExpression.getNode().setLayoutY(WINDOW_HEIGHT/2);
		}
		
		/**
		 * Helper method to find the possible permutations when moving a node.
		 * @param inList the list of the nodes generated from the expression
		 * @param movingItem the node being moved
		 * @return the list containing the possible permutations
		 */
		private <T> List<List<T>> getPossiblePermutations(List<T> inList, T movingItem){
			List<List<T>> list = new ArrayList<List<T>>();
			int currentIndex = inList.indexOf(movingItem);
			List<T> temp = new ArrayList<T>();
			temp.addAll(inList);
			list.add(temp);
			//System.out.println("Focused in heerya" + movingItem);
			while(currentIndex < inList.size()-1) { //permutations to the right of the moving item
				List<T> temp2 = new ArrayList<T>();
				currentIndex++;
				switchElements(temp, movingItem, inList.get(currentIndex));
				temp2.addAll(temp);
				list.add(temp2);
			}
			temp.clear();
			//System.out.println(temp);
			temp.addAll(inList);
			currentIndex = inList.indexOf(movingItem);
			
			while(currentIndex > 0) { //permutations to the left of the moving item
				List<T> temp2 = new ArrayList<T>();
				currentIndex--;
				switchElements(temp, movingItem, temp.get(currentIndex));
				temp2.addAll(temp);
				list.add(temp2);
			}
			return list;
		}
		
		/**
		 * Retrieves the 
		 * @param copied section of expression
		 * @param focused current focused expression
		 * @param list the list of expression nodes
		 * @param mouseX X coordinate of the mouse
		 * @return the index of the smallest Xi
		 */
		private List<Expression> getClosestXi(Expression copied, Expression focused, List<List<Expression>> list, double mouseX) {
			System.out.println(list);
			int indexOfSmallestXi = -1;
			double smallestXi = Double.MAX_VALUE;
			for (int i = 0; i < list.size(); i++) {
				double temp = calculateXi(copied, (CompoundExpression) focused, list.get(i), mouseX);
				System.out.println(temp + " temp : smallestXi " + smallestXi);
				if (temp < smallestXi) {
					smallestXi = temp;
					indexOfSmallestXi = i;
				}
			}
			//System.out.println(indexOfSmallestXi);
			//System.out.println(list.get(indexOfSmallestXi));
			return list.get(indexOfSmallestXi);
				
		}
		
		/**
		 * Helper expression for getClosestXi
		 * @param copied section of expression
		 * @param focused current focused expression
		 * @param list the list of expression nodes
		 * @param mouseX X coordinate of the mouse
		 * @return calculated value of Xi
		 */
		private double calculateXi(Expression copied, CompoundExpression focused, List<Expression> list, double mouseX) {
			SimpleCompoundExpression c = (SimpleCompoundExpression) focused.deepCopy();
			c.getChildren().clear();
			HBox focusTemp = null;
			//System.out.println("AY focus" + focused);
			//if (!list.contains(focused))System.out.println("whelp");
			System.out.println(list);
			//s_pane.getChildren().add(c.getNode());
			list.get(list.indexOf(focusedExpression));
			for (int i = 0; i < list.size(); i++) {
				SimpleCompoundExpression child = (SimpleCompoundExpression) list.get(i).deepCopy();
				//((HBox) c.getNode()).getChildren().add(child.getNode());
				_pane.getChildren().add(child.getNode());
				child.getNode().setLayoutX(WINDOW_WIDTH/4);
				child.getNode().setLayoutY(WINDOW_HEIGHT/3);
				if (list.get(i).equals(focusedExpression)) {
					System.out.println("FOcus index"+i);
					focusTemp =(HBox) child.getNode();
				}
			}
			System.out.println("Focus temp "+focusTemp.getLayoutX());
			_pane.getChildren().add(c.getNode());
			c.getNode().setLayoutX(WINDOW_WIDTH/4);
			c.getNode().setLayoutY(WINDOW_HEIGHT/3);
			//c.getNode().setOpacity(0);
			
			//focusTemp has the same absolute bounds for each permutation 
			//THIS IS THE ERROR WITH THE DRAG AND DROP IF THIS WORKED THE FEATURE WOULD BE FINE
			Bounds focusb = focusTemp.getBoundsInParent();//focusTemp.localToScene(focusTemp.getBoundsInLocal());
			double focusCenter = focusb.getMinX()+(focusb.getMaxX()-focusb.getMinX())/2;
			System.out.println(focusedExpression);
			//System.out.println("HEEEYEYEYEYE " + focusCentere);
			
			System.out.println("Focus Center: " +focusCenter);
			
			Bounds copyb = copied.getNode().localToScene(copied.getNode().getBoundsInParent());
			double copyCenter = copyb.getMinX()+(copyb.getMaxX()-copyb.getMinX())/2;
			//System.out.println("Copy Center " + copyCenter);
			System.out.println("get layout "+focusTemp.localToScene(focusTemp.getLayoutX(), focusTemp.getLayoutY()).getX());
			double temp = Math.abs(focusCenter-mouseX);
			System.out.println(temp);
			//_pane.getChildren().remove(c.getNode());
			return temp;
		}
		
		/**
		 * Mouse event handler
		 */
		public void handle (MouseEvent event) {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				List<Expression> children = ((SimpleCompoundExpression) focusedExpression).getChildren();
				
				if (children.size()==0&&!focusedExpression.getNode().isPressed()) {
					resetFocus();
				}
				
				for (int i = 0; i <children.size(); i++) {
					if (children.get(i).getNode() instanceof HBox) {
						
						if (focusedExpression.getNode().isPressed()) {
							
							
							if (children.get(i).getNode().isPressed()) {
								 clearStyle(focusedExpression.getNode());
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
				
				final double newX = event.getX()-focusedExpression.getNode().sceneToLocal(ogEvent.getSceneX(),ogEvent.getSceneY()).getX();
				final double newY = event.getY()-focusedExpression.getNode().sceneToLocal(ogEvent.getSceneX(),ogEvent.getSceneY()).getY();
				((HBox) copy.getNode()).setTranslateX(newX);
				((HBox) copy.getNode()).setTranslateY(newY);
				
				List<List<Expression>> possiblePermutations = 
						getPossiblePermutations(((SimpleCompoundExpression) focusedExpression.getParent()).getChildren(), focusedExpression);
				
				List<Expression> closestXiList = getClosestXi(copy, focusedExpression.getParent(), possiblePermutations, newX);
				((SimpleCompoundExpression) focusedExpression.getParent()).getChildren().clear();
				((SimpleCompoundExpression) focusedExpression.getParent()).getChildren().addAll(closestXiList);
				refreshPane();
				
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
