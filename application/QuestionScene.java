package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class will be the main GUI while the quiz is running. It allows the user to answer
 * a question based on the number of questions and topics selected.
 * @author jthalacker
 *
 */
public class QuestionScene extends Application {

	//Fields

	Question question; //This will be the current question it is displaying
	QuestionBank currQuestions;
	List<Answer> answers;  //List of answers
	List<RadioButton> answerButtons; //list of answer radio buttons
	int numQuizQuestions;  //Total number of questions
	int questionNumber;  //current question displayed


	QuestionScene(QuestionBank currQuestions, int questionNumber){
		question = currQuestions.questions.get(questionNumber);
		this.currQuestions= currQuestions;
		answers = question.getAnswersList();
		this.questionNumber = questionNumber;
		this.numQuizQuestions = currQuestions.questions.size();
	}


	//File for image needed publically
	File image;


	//Methods
	@Override
	public void start(Stage primaryStage) {
		try {

			//Create main window
			HBox root = new HBox(30);

			//Create both sides
			VBox vBox1 = new VBox(15);
			VBox vBox2 = new VBox(15);
			

			//Create Objects to put in the boxes

			//Create Label for Question Number
			Label questionNumberLabel = new Label("Question: " + questionNumber + 1 + "/" + numQuizQuestions);
			
			//Create question text label
			Label questionText = new Label(question.getQuestionText());
			questionText.setWrapText(true);
			
			//Create the radio buttons with the answer values for the questions
			for(int i = 0; i < answers.size(); i++) {
				answerButtons.add(new RadioButton(answers.get(i).getAnswerText()));
			}

			
			
			//Create scene and update stage
			Scene scene = new Scene(root,550, 700);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Question Scene");
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	/**
	 * This method will return whether the answer selected was correct or not
	 * @param Answer answer to check
	 * @return Boolean correct or not
	 */
	private Boolean checkAnswer(Answer answer) {

		return false;
	}
	/**
	 * This method returns the scene back to the main screen
	 * It will be called on quit
	 */
	private void backToMain() {
		
		return;
	}
	/**
	 * This method will update the QuizResult class with whether the question
	 * was answered correctly or not
	 * @param Boolean correct, whether it was correct or not
	 */
	private void updateQuizResults (Boolean correct) {

		return;
	}
	/**
	 * This calls the popup box for whether they got the question right or not
	 * with a button to continue, or finish quiz
	 * When it calls a new QuestionScene, it must pass currTopics
	 */
	private void callPopBox() {

	}
	public static void main(String[] args) {
		launch(args);
	}
}
