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


public class MakeQuestionScene extends Application {

	//Fields
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
		      
		      //Error/Null Label
		      Label infoInvalidLabel = new Label("The required fields are not filled in");
		      
		      //Required Labels
		      Label requiredLabel = new Label("This required fields are:\n-Question Topic \n-QuestionText");
		      
		      //vBox1 UI controls and labels
		      //Meta Data
		      Label metaLabel = new Label("meta-data");
		      TextField metaField = new TextField();
		      metaField.setMaxWidth(200);
		      
		      //Question Text Area
		      Label questionTextLabel = new Label("Question Text");
		      TextArea questionText = new TextArea();
		      questionText.setMaxWidth(200);
		      
		      //Answer Data
		      Label answerLabel = new Label("Answer Text");
		      TextField answerField = new TextField();
		      answerField.setMaxWidth(200);
		      
		      //Box for if correct answer and Add abswer button
		      HBox answerHBox = new HBox(70);
		      
		      //Checked if correct answer
		  	  CheckBox isCorrectAnswer = new CheckBox("Correct Answer"); 
		      isCorrectAnswer.setIndeterminate(false);
		      //Add Question button and event
		      answerHBox.getChildren().addAll( isCorrectAnswer, addAnswerButton);
		      
		      //List of answers
		      ListView<String> answerList = new ListView<String>();
		      answerList.setMaxHeight(200);
		      
		      addAnswerButton.setOnAction(event -> {
		    		  if (!answerField.getText().equals("") && !answerField.getText().equals("Please fill this field")) {
		    			  answerListObject.add(new Answer(answerField.getText(), isCorrectAnswer.isSelected()));
		    			  answerList.getItems().add(answerField.getText());
		    		  } else {
		    			  answerField.setText("Please fill this field");
		    		  }
		      });
		      
		      //vbox2 UI controls and labels
		      //Topic
		      Label topicLabel = new Label("Topic");
		      TextField topicField = new TextField();
		     
		     
		      //Image getter
		      TextField imagePath = new TextField();
		      imagePickButton.setOnAction(event -> {
		    	  FileChooser fileChooser = new FileChooser();
		    	  fileChooser.setTitle("Open Resource File");
		    	  //Image files only allowed
		    	  fileChooser.getExtensionFilters().addAll(
		    	          new ExtensionFilter("Image Files", "*.png", "*.jpg"));
		    	  image = fileChooser.showOpenDialog(primaryStage);
		    	  if (image != null) {
		    		  imagePath.setText(image.getPath());
		    	  }
		      });
		      imagePath.setEditable(false);
		      
		      //On press of button find image
		      HBox imageHBox = new HBox(25);
		      imageHBox.getChildren().addAll(imagePath, imagePickButton);
		      
		      Label imageLabel = new Label("Image Description");
		      TextField imageField = new TextField();
		      
		      //Back and Make question buttons instance ends
		      HBox exitingMakeQuestion  = new HBox(75);
		      
		      exitingMakeQuestion.getChildren().addAll(backButton, makeQuestion);
		      //Back button close the make question stage
		      backButton.setOnAction(event -> {
		    	  exitMakeQuestion(primaryStage);
		      });
		      
		      //Make Question event handling
		      makeQuestion.setOnAction(event -> {
		    	  boolean flag = makeQuestionObject(topicField.getText(), questionText.getText(), 
		    			  metaField.getText(), image, imageField.getText());
		    	  //Return question object to main????
		    	  if (flag) {
			    	  exitMakeQuestion(primaryStage); 
		    	  } else {
		    		  if (!vBox2.getChildren().contains(infoInvalidLabel))
		    			  vBox2.getChildren().addAll(infoInvalidLabel);
		    	  }
		      });
		      
		      //Test Button to be removed
		      Button test = new Button("test");
		      test.setOnAction(event -> {
		    		  System.out.println(answerListObject.toString());
			      });
		      
		      //VBox and HBox creation/updates
		      vBox1.getChildren().addAll(requiredLabel, metaLabel, metaField, questionTextLabel, questionText, answerLabel, 
		    		  answerField, answerHBox, answerList);
		      vBox2.getChildren().addAll(topicLabel, topicField, imageHBox, imageLabel, imageField,
		    		  exitingMakeQuestion, test);
		      root.getChildren().addAll(vBox1, vBox2);
		      
		      //Create scene and update stage
		      Scene scene = new Scene(root,550, 700);
		      primaryStage.setScene(scene);
		      primaryStage.setTitle("Make Question");
		      primaryStage.show();
		      
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean makeQuestionObject(String questionTopic, String questionText, String metaData, 
			File image, String imageDescription) {
		if (!questionTopic.equals("")) 
				if (!questionText.equals("")) 
					if (answerListObject.size() >= 2) { //Maybe change??????
						Question newQuestion = new Question(questionTopic, questionText, metaData, 
								image, imageDescription,  answerListObject);
						return true;
						//May need to be edited for adjustment for question class
					}
		return false;
	}

	private void exitMakeQuestion(Stage primaryStage) {
		primaryStage.close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}