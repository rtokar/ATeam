package application;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NoSaveAndQuit extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    try {
      VBox root = new VBox(10);
      Label confirmLabel = new Label("Are you sure you want to quit without saving? Seems like a stupid idea.");
      confirmLabel.setWrapText(true);
      HBox choices = new HBox(5);
      Button quit = new Button("Yes, I want to quit stupidly.");
      Button cancel = new Button("Wait, actually cancel that.");
      choices.getChildren().addAll(quit, cancel);
      root.getChildren().addAll(confirmLabel, choices);
      Scene scene = new Scene(root,375,80);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Quit Without Saving?");
      primaryStage.show();
      
      quit.setOnAction(this::closeAll);
      cancel.setOnAction(this::back);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  /**
   * Closes window when cancel button is pressed, returns user to Main GUI
   */
  private void back(ActionEvent event) {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow(); // get this window
    stage.close(); // close it
  }
  
  /**
   * Closes this window and main GUI window, closing the entire program without saving.
   */
  private void closeAll(ActionEvent event) {
    Main.closeFlag = true; // set Main GUI to close after this window closes
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow(); // get this window
    stage.close(); // close it
    Main.kill();
  }
  
  public static void main(String[] args) {
    System.out.println();
    Application.launch(args);

  }

}
