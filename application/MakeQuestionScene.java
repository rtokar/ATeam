package application;

//Java regular imports
import java.io.File;
import java.util.ArrayList;
//FX imports
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class is the GUI for making a question along with the creation of said
 * object and passing it into the main question bank.
 * 
 * @author Hunter Celeste
 *
 */
public class MakeQuestionScene extends Application {

	// Fields
	// Any Object that has an action event is intialized as a field
	Button addAnswerButton = new Button("Add Answer");
	Button imagePickButton = new Button("Find Image"); // Opens a file reader
	Button backButton = new Button("Back"); // Goes to main page
	Button makeQuestion = new Button("Make Question"); // Make question then go back to main page

	// List of answers for the question
	private ArrayList<Answer> answerListObject = new ArrayList<Answer>(); 

	// File for image needed publically
	File image;

	/**
	 * This method builds both the GUI and is responsible for calling the correct
	 * events and methods when it is in needed.
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		try {

			// Create main window
			HBox root = new HBox(70);

			// Create both sides
			VBox vBox1 = new VBox(20);
			VBox vBox2 = new VBox(15);

			// Error/Null Label
			Label infoInvalidLabel = new Label("The required fields are not filled in");
			infoInvalidLabel.setFont(Font.font("Comic Sans MS", 15));
			infoInvalidLabel.setTextFill(Color.RED);

			// Required Labels and Box for spicy spacing
			Label requiredLabel = new Label("This required fields are:");
			Label rl1 = new Label("-Question Topic");
			Label rl2 = new Label("-Question Text");
			Label rl3 = new Label("-2 to 6 Answers");
			requiredLabel.setFont(Font.font("", 15));

			VBox requiredText = new VBox(5);
			requiredText.getChildren().addAll(requiredLabel, rl1, rl2, rl3);

			// both vBoxes controls and UIs
			// Meta Data
			Label metaLabel = new Label("Meta-data");
			metaLabel.setFont(Font.font("", 13));
			TextField metaField = new TextField();
			metaField.setMaxWidth(200);

			// Question Text Area
			Label questionTextLabel = new Label("Question Text");
			questionTextLabel.setFont(Font.font("", 13));
			TextArea questionText = new TextArea();
			questionText.setMaxWidth(200);
			questionText.setMaxHeight(300);

			// Answer Data :)
			Label answerLabel = new Label("Answer Text");
			answerLabel.setFont(Font.font("", 13));
			TextField answerField = new TextField();
			answerField.setMaxWidth(200);

			// Box for if correct answer and Add abswer button
			HBox answerHBox = new HBox(25);

			// Checked if correct answer
			CheckBox isCorrectAnswer = new CheckBox("Correct Answer");
			isCorrectAnswer.setIndeterminate(false);
			// Add Question button and event
			answerHBox.getChildren().addAll(isCorrectAnswer, addAnswerButton);

			// List of answers
			ListView<String> answerList = new ListView<String>();
			answerList.setMaxHeight(160);
			answerList.setMaxWidth(160);
			// List for Either True or false
			ListView<String> trueOrFalseList = new ListView<String>();
			trueOrFalseList.setMaxHeight(160);
			trueOrFalseList.setMaxWidth(25);

			// HBox for both of the lists
			HBox listBox = new HBox(25);

			//Event that Adds answer object to both list and GUIs list
			addAnswerButton.setOnAction(event -> {
				if (answerListObject.size() < 6) {
					if (!answerField.getText().equals("") && !answerField.getText().equals("Please fill this field")) {
						answerListObject.add(new Answer(isCorrectAnswer.isSelected(), answerField.getText()));
						answerList.getItems().add(answerField.getText());
						if (isCorrectAnswer.isSelected())
							trueOrFalseList.getItems().add("T");
						else
							trueOrFalseList.getItems().add("F");
					} else {
						answerField.setText("Please fill this field");
					}
				}
			});
			
			listBox.getChildren().addAll(answerList, trueOrFalseList);

			// Topic :)
			Label topicLabel = new Label("Topic");
			TextField topicField = new TextField();
			topicLabel.setFont(Font.font("", 13));

			// Image getter here
			TextField imagePath = new TextField();
			imagePickButton.setOnAction(event -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				// Image files only allowed
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter(" Files", "*.png", "*.jpg", "*.gif"));
				image = fileChooser.showOpenDialog(primaryStage);
				if (image != null) {
					imagePath.setText(image.getPath());
				}
			});
			imagePath.setEditable(false);

			// On press of button find image
			HBox imageHBox = new HBox(25);
			imageHBox.getChildren().addAll(imagePath, imagePickButton);

			// Back and Make question buttons instance ends
			HBox exitingMakeQuestion = new HBox(75);

			exitingMakeQuestion.getChildren().addAll(backButton, makeQuestion);
			// Back button close the make question stage
			backButton.setOnAction(event -> {
				exitMakeQuestion(primaryStage);
			});

			// Make Question event handling
			makeQuestion.setOnAction(event -> {
				boolean flag = makeQuestionObject(topicField.getText(), questionText.getText(), metaField.getText(),
						image);
				if (flag) {
					exitMakeQuestion(primaryStage);
				} else {
					if (!vBox2.getChildren().contains(infoInvalidLabel))
						vBox2.getChildren().addAll(infoInvalidLabel);
				}
			});

			// VBox and HBox creation/updates
			vBox1.getChildren().addAll(requiredText, topicLabel, topicField, questionTextLabel, questionText);
			vBox2.getChildren().addAll(metaLabel, metaField, imageHBox, answerLabel, answerField, answerHBox, listBox,
					exitingMakeQuestion);

			//Style changes to vBoxs
			vBox1.setPadding(new Insets(10, 0, 0, 25));
			vBox2.setPadding(new Insets(10, 25, 0, 0));

			// Two main boxes
			root.getChildren().addAll(vBox1, vBox2);

			// Create scene and update stage
			Scene scene = new Scene(root, 575, 450);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Make Question");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to detect if the input entered by the user is correct and
	 * if so to then make a question, put it in the main question bank, and close
	 * the make question scene
	 * 
	 * @Returns a boolean for whether this process succeeded or failed
	 */
	private boolean makeQuestionObject(String questionTopic, String questionText, String metaData, File image) {
		if (!questionTopic.equals(""))
			if (!questionText.equals("")) {
				if (answerListObject.size() >= 2 && answerListObject.size() <= 6) {
					// Makes Question if image is null
					if (image == null) {
						Question newQuestion = new Question(questionTopic, questionText, answerListObject, metaData);
						Main.addToMasterQuestionBank(newQuestion);

						// Makes Question if image is not null
					} else {
						Question newQuestion = new Question(questionTopic, questionText, answerListObject, metaData,
								image);
						// Main.masterQuestionBank.addQuestion(newQuestion);
						Main.addToMasterQuestionBank(newQuestion);
					}
					return true;
				}
			}
		return false;
	} // End of MakeQuestionObject method

	/**
	 * This method is responsible for exiting the stage
	 */
	private void exitMakeQuestion(Stage primaryStage) {
		primaryStage.close();
	} // End of exitMakeQuestion method

	/**
	 * This launches the stage and start method
	 */
	public static void main(String[] args) {
		launch(args);
	} // End of main method

} // End of MakeQuestionScene Class
