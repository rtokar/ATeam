package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;

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
	QuizResult results;

	//File for image needed publically
	File image;

	Answer chosenAnswer;
	Stage mainStage;

	QuestionScene(QuestionBank currQuestions, int questionNumber){
		question = currQuestions.questions.get(questionNumber);
		this.currQuestions= currQuestions;
		answers = question.getAnswersList();
		this.questionNumber = questionNumber;
		this.numQuizQuestions = currQuestions.questions.size();
		image = question.getImage();
	}



	//Methods
	@Override
	public void start(Stage primaryStage) {
		try {
			mainStage = primaryStage;
			//Create main window
			HBox root = new HBox(30);

			//Create both sides
			VBox vBox1 = new VBox(15);
			VBox vBox2 = new VBox(15);


			//Create Objects to put in the boxes

			//vBox1 Objects
			//Create Label for Question Number
			Label questionNumberLabel = new Label("Question: " + questionNumber + 1 + "/" + numQuizQuestions);

			//Create question text label
			Label questionText = new Label(question.getQuestionText());
			questionText.setWrapText(true);

			//Create the radio buttons with the answer values for the questions
			for(int i = 0; i < answers.size(); i++) {
				answerButtons.add(new RadioButton(answers.get(i).getAnswerText()));
			}

			//Add UI Objects to vBox1
			vBox1.getChildren().addAll(questionNumberLabel, questionText);
			for(int i= 0; i < answerButtons.size(); i++) 
				vBox1.getChildren().add(answerButtons.get(i));


			//vBox2 Objects
			//Create image for question
			ImageView questionImage = new ImageView();
			Image qImage = new Image(image.getPath());
			questionImage.setImage(qImage);
			//Create Exit Quiz button to exit quiz
			Button exitQuizButton = new Button("Exit");
			//Create Submit button to submit question
			Button submitButton = new Button("Submit");

			//Add UI Objects to vBox2
			vBox2.getChildren().addAll(questionImage, exitQuizButton, submitButton);

			root.getChildren().addAll(vBox1, vBox2);
			//Create scene and update stage
			Scene scene = new Scene(root,550, 700);
			mainStage.setScene(scene);
			mainStage.setTitle("Question Scene");
			mainStage.show();

			exitQuizButton.setOnAction(this::backToMain);
			submitButton.setOnAction(this::callPopBox);



		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	/**
	 * This method returns the scene back to the main screen
	 * It will be called on quit
	 */
	private void backToMain(ActionEvent event) {
		//TODO this will need to return to Main's state it was in before calling QuestionScene
		return;
	}
	/**
	 * This method will update the QuizResult class with whether the question
	 * was answered correctly or not
	 * @param Boolean correct, whether it was correct or not
	 */
	private void updateQuizResults (Boolean correct) {
		//TODO this will need to call setters or QuizResults
		return;
	}
	/**
	 * This calls the popup box for whether they got the question right or not
	 * with a button to continue, or finish quiz
	 * When it calls a new QuestionScene, it must pass currTopics
	 */
	private void callPopBox(ActionEvent event) {
		//update the results
		updateQuizResults(chosenAnswer.getCorrectenss());
		//If last question, display quiz results
		if(questionNumber + 1 == numQuizQuestions);
		//TODO displays QuizResult

		//Else create new questionScene with next question
		displayQuestionResult();

	}



	private void displayQuestionResult(){
		Stage questionResultWindow = new Stage(); // make new window
		questionResultWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window
		questionResultWindow.initOwner(this.mainStage);
		QuestionResult save = new QuestionResult(chosenAnswer.getCorrectenss());
		try {
			save.start(questionResultWindow); // open new window
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void displayQuestionScene(ActionEvent event) {
		Stage quizWindow = new Stage();
		QuestionScene quiz = new QuestionScene(currQuestions, questionNumber+1); // 0 is the starting question number
		quizWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window
		quizWindow.initOwner(this.mainStage);
		try {
			quiz.start(quizWindow);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
