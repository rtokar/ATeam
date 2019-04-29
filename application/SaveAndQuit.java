package application;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SaveAndQuit extends Application {



  @Override
  public void start(Stage primaryStage) throws Exception {
    try {
      HBox root = new HBox(30);
      Scene scene = new Scene(root,375,350);
      primaryStage.setScene(scene);
      primaryStage.setTitle("SaveAndQuit");
      primaryStage.show();
      
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    
    
  }
  
  public static void main(String[] args) {
    System.out.println();
    Application.launch(args);

  }

}
