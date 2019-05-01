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
	Boolean correct;
	Stage mainStage = new Stage();
	QuestionBank currQuestions;
	int questionNumber;
	QuizResult results;

	public QuestionResult(Boolean correct, QuestionBank currQuestions, int questionNumber, QuizResult results) {
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


			bottom.getChildren().addAll(correctness, nextButton);
			root.getChildren().add(bottom);
			Scene scene = new Scene(root,375,350);
			mainStage.setScene(scene);
			mainStage.setTitle("QuestionResult");
			mainStage.show();


			//TODO call new questionScene from next button
			nextButton.setOnAction(this::displayNextQuestionScene);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
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
