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
	public QuestionResult(Boolean correct) {
		this.correct = correct;
	}


	  @Override
	  public void start(Stage primaryStage) throws Exception {
	    try {
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
	      
	      Scene scene = new Scene(root,375,350);
	      primaryStage.setScene(scene);
	      primaryStage.setTitle("QuestionResult");
	      primaryStage.show();
	      
	      
	      nextButton.setOnAction();
	      
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    
	    
	  }
	  
	  
	  
	  public static void main(String[] args) {
	    Application.launch(args);

	  }
}
