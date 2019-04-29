package application;

import java.io.File;
import java.util.ArrayList;

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
	QuestionBank currTopics;


	QuestionScene(QuestionBank currTopics){
		question = currTopics.getNextQuestion();
		this.currTopics = currTopics;
	}

	//Any Object that has an action event is intialized as a field
	Button addAnswerButton = new Button("Add Answer");
	Button imagePickButton = new Button("Find Image"); //Opens a file reader
	Button backButton = new Button("Back"); //Goes to main page
	Button makeQuestion = new Button("Make Question"); //Make question then go back to main page

	//List of answers for the question
	ArrayList<Answer> answerListObject = new ArrayList<Answer>();	

	//File for image needed publically
	File image;

	//Constructor // None needed?


	//Methods
	@Override
	public void start(Stage primaryStage) {
		try {

			//Create main window
			HBox root = new HBox(30);

			//Create both sides
			VBox vBox1 = new VBox(15);
			VBox vBox2 = new VBox(15);



			//Create scene and update stage
			Scene scene = new Scene(root,550, 700);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Make Question");
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

	}
	/**
	 * This method returns the scene back to the main screen
	 * It will be called on quit, or when quiz is over
	 */
	private void backToMain() {

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
