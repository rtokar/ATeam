package application;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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

	public QuizResult() {
		numCorrect = 0;
		numAnswered = 0;
	}
	
	// numCorrect incrementer 
	public void incNumCorrect() {
		this.numCorrect++;
	}
	
	// numAnswered incrementer
	public void incNumAnswered() {
		this.numAnswered++;
	}
	
	/**
	 * This will calculate the percent correct out 
	 * of number answered.
	 * 
	 * @return percentage correctly answered
	 */
	public double getPercent() {
		if (numAnswered==0) {return 0;}
		return Math.floor((double)numCorrect/numAnswered*100);
	}
	
	/**
	 * Creates a GUI to display the results of the quiz
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		try {
			VBox root = new VBox(30);
			int percent = (int)getPercent(); // users score
			Label label1 = new Label("	You answered " + numCorrect + " question(s) correctly.");
			Label label2 = new Label("	You answered " + numAnswered + " total question(s).");
			Label label3 = new Label("	Your score: " + percent + "%");
			Label label4;
			if (percent<50) {
				label4 = new Label("	Practice makes perfect!");
			} else if (percent>=50 && percent<80) {
				label4 = new Label("	Almost there, keep practicing!");
			} else if (percent>=80 && percent<100) {
				label4 = new Label("	Keep up the great work! :)");
			} else {
				label4 = new Label("	You rock! :D");
			}
			
			// adds all the labels to the layout
			root.getChildren().addAll(label1,label2,label3,label4);
			Scene scene = new Scene(root, 300, 200);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Quiz Results");
			primaryStage.show();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		// close the previous screens
		primaryStage.setOnCloseRequest(event -> Main.killAllQuestionScenes());
	}
	

	public static void Main(String[] args) {
		Application.launch(args);
	}
}