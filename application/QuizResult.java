package application;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;

/**
 * Created when the user takes a quiz, keeps track of correct 
 * answers, calculates % correct when the quiz is finished, and
 * displays the results in a pop-up window.
 * 
 * @author Rose Tokar
 *
 */
public class QuizResult {
	private int numCorrect; // number of questions correct
	private int numAnswered; // number of questions answered
	
	// numCorrect setter
	public void setNumCorrect(int num) {
		this.numCorrect = num;
	}
	
	// numAnswered setter
	public void setNumAnswered(int num) {
		this.numAnswered = num;
	}
	
	/**
	 * This will calculate the percent correct out 
	 * of number answered.
	 * 
	 * @return percentage correctly answered
	 */
	public double getPercent() {
		return numCorrect/numAnswered*100;	
	}
	
	
	public void start(Stage primaryStage) throws Exception {
		try {
			BorderPane root = new BorderPane();
			Label label1 = new Label("You answered " + numCorrect + " question(s) correctly.");
			Label label2 = new Label("You answered " + numAnswered + " total question(s).");
			Label label3 = new Label("Your score: " + getPercent() + "%");
			root.setCenter(label1);
			root.setCenter(label2);
			root.setCenter(label3);
			Scene scene = new Scene(root, 400, 300);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Quiz Results");
			primaryStage.show();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	

	public static void Main(String[] args) {
		Application.launch(args);
	}
}