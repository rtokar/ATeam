package application;



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is the pop up box after each question for it to display its correctness
 * @author jthalacker
 *
 */
public class QuestionResult extends Application{
	Boolean correct; //wether the answer was correct or not
	Stage mainStage = new Stage(); //the main Stage for the GUI
	QuestionBank currQuestions; //List of questions for quiz
	int questionNumber; //Current question
	QuizResult results; //the quiz results

	/**
	 * this is the constructor that assigns all the variables the correct values passed in
	 * @param correct
	 * @param currQuestions
	 * @param questionNumber
	 * @param results
	 */
	public QuestionResult(Boolean correct, QuestionBank currQuestions, int questionNumber,
			QuizResult results) {
		this.correct = correct;
		this.currQuestions = currQuestions;
		this.questionNumber = questionNumber;
		this.results = results;
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			mainStage = primaryStage;
			VBox root = new VBox(30);
			VBox bottom = new VBox(15);
			//Create Next Button
			Button nextButton = new Button("Next");

			//Create correctness label and set to whether it is correct or not
			Label correctness = new Label();
			if(correct)
				correctness.setText("Correct!");
			else
				correctness.setText("Incorrect!");

			//Create Scene and display GUI on screen
			bottom.getChildren().addAll(correctness, nextButton);
			root.getChildren().add(bottom);
			Scene scene = new Scene(root,375,350);
			mainStage.setScene(scene);
			mainStage.setTitle("QuestionResult");
			mainStage.show();
			
			//Set action of the nextButton
			nextButton.setOnAction(this::displayNextQuestionScene);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * This displays the next questions Scene
	 * @param event, the event that triggers it.(clicking nextButton)
	 */
	public void displayNextQuestionScene(ActionEvent event) {
		Stage quizWindow = new Stage();
		QuestionScene quiz = new QuestionScene(currQuestions, questionNumber+1, results); // 0 is start
		quizWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window 
		quizWindow.initOwner(this.mainStage);
		try {
			quiz.start(quizWindow);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}




	public static void main(String[] args) {
		Application.launch(args);

	}
}
