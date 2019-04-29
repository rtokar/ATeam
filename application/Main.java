package application;
import java.io.File;
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
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

//TODO: QuestionScene, QuizResults, MakeQuestion
// Are you all making guis for these that I will open?

// comment

/**
 * This is the main driver for the quiz generator.
 * @author jthalacker, ascherer
 *
 */
public class Main extends Application{
  
  QuestionBank masterQuestionBank; // bank of questions from all loaded json files
  QuestionBank topicQuestionBank; // all questions with topic matching selected topics
  QuestionBank quizQuestionBank; // a subset of randomized questions from selected topics, 
                                 //number equal to numQuizQuestions (or num of qs in topicQuestionBank, whichever is lower)
  
  List<String> topicList; // list of topics from masterQuestionBank
  List<String> currentTopics; // list of chosen topics from topicList
  int numQuizQuestions; // num entered in the TextField numQuestionsTextField
  
  Stage mainStage;

  public void start(Stage primaryStage) {
    try {
      this.mainStage = primaryStage;
      
      //Create main window
      HBox root = new HBox(30);
      //Create two sides
      VBox vBox1 = new VBox(51);
      VBox vBox2 = new VBox(7);
      //vBox2's hBoxes
      HBox addAndClear = new HBox(5);
      
      //vBox1 UI controls
      Button makeQuestionButton = new Button("Make Question");
      Button loadQuestionsButton = new Button("Load Questions");
      Button takeTestButton = new Button("Take Test");
      Button saveAndQuitButton = new Button("Save and Quit");
      
      //vbox2 UI controls
      
      TextField numQuestionsTextField = new TextField();
      numQuestionsTextField.setPromptText("# of Questions in Quiz");
      
        //Hard-coded list
        //TODO change to getting from questions
      ObservableList<String> comboBoxList = FXCollections.observableArrayList("English",
          "Reading", "Quantum Physics");
      ComboBox<String> loadedTopicsComboBox = new ComboBox<String>(comboBoxList);
      loadedTopicsComboBox.setPromptText("<Select a Topic to add>");
      Label comboBoxLabel = new Label("<Load Questions to have Topics>");
      loadedTopicsComboBox.setPlaceholder(comboBoxLabel);
      
      
        //addAndClear HBox buttons
      Button addTopicButton = new Button("Add Topic");
      Button clearTopicListButton = new Button("Clear Topic List");
      
      Label chosenTopicListLabel = new Label("Chosen Topics:");
      ListView<String> chosenTopicsListView = new ListView<String>();
      chosenTopicsListView.setMaxSize(175, 100);
      Button dontSaveAndQuitButton = new Button("Quit Without Saving");
      
      //vbox and hbox creation/updates
      addAndClear.getChildren().addAll(addTopicButton, clearTopicListButton);
      vBox1.getChildren().addAll(makeQuestionButton, loadQuestionsButton, takeTestButton, saveAndQuitButton);
      vBox2.getChildren().addAll(numQuestionsTextField, loadedTopicsComboBox, 
          addAndClear, chosenTopicListLabel, chosenTopicsListView, dontSaveAndQuitButton);
      root.getChildren().addAll(vBox1, vBox2);
      
      //Create scene and update stage
      Scene scene = new Scene(root,375,350);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Quiz Generator");
      primaryStage.show();
      
      // Set up actions for each ui component
      
      // load quiz (via lambda expression, note can also do multi line expression by putting lines in {} after ->)
      // loadQuestionsButton.setOnAction((event) -> System.out.println("loadQuizButton"));
      
      // display quiz using method reference, note that method must have an ActionEvent as parameter
      loadQuestionsButton.setOnAction(this::loadQuestions);
      makeQuestionButton.setOnAction(this::displayMakeQuestion);
      takeTestButton.setOnAction(this::displayQuiz);
      saveAndQuitButton.setOnAction(this::displaySaveExit);
      dontSaveAndQuitButton.setOnAction(this::displayNoSaveExit);
      addTopicButton.setOnAction(this::addTopic);
      clearTopicListButton.setOnAction(this::clearTopicList);
      
    }catch(Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  /**
   * Load questions from a chosen (with file chooser) json file when the loadQuestionsButton is pressed,
   * then adds them to master question bank
   */
  private void loadQuestions(ActionEvent event) {
    // get file with FileChooser
    Stage chooseStage = new Stage(); // new window for file chooser
    chooseStage.initOwner(mainStage); // set main stage as owner
    chooseStage.initModality(Modality.WINDOW_MODAL); // lock focus to file chooser
    FileChooser choose = new FileChooser();
    choose.setTitle("Choose a .json file to load questions");
    FileChooser.ExtensionFilter filt = new FileChooser.ExtensionFilter(".json", "*.json"); // only allow .json files
    choose.getExtensionFilters().add(filt);
    File jsonFile = choose.showOpenDialog(chooseStage); // choose file
    if (jsonFile != null) {
      masterQuestionBank.addAllQuestions(jsonFile); // adds all questions from the chosen json file      
    }

    System.out.println("loadQuestions()");
    System.out.println("Selected File: "+jsonFile);
  }
  
  private void getNumQuestions(ActionEvent event) {
    try {
      this.numQuizQuestions = Integer.parseInt(((TextField) event.getSource()).getText());
    } catch (NumberFormatException e) {
      //TODO: throw popup that says to enter an int into text field in upper right -> vbox with label containing message and OK button to close window

    }
  }
  
  /**
   * Called when the addTopicButton is pressed.
   * Adds the currently selected topic in the loadedTopicsComboBox to the currentTopics (no duplicates)
   */
  private void addTopic(ActionEvent event) {
    System.out.println("addTopic()");
  }
  
  /**
   * Called when the clearTopicListButton is pressed.
   * Removes the most recently added? all? topic/s from currentTopics
   */
  private void clearTopicList(ActionEvent event) {
    System.out.println("clearTopicList()");
  }
  
  /**
   * Displays the MakeQuestion screen when makeQuestionButton is pressed.
   *  (call from Jake's class? I forget if I am making this or he is)
   */
  private void displayMakeQuestion(ActionEvent event) {
    System.out.println("displayMakeQuestion()");
    Stage makeQuestionWindow = new Stage(); // make new window
    makeQuestionWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window
    makeQuestionWindow.initOwner(this.mainStage);
    MakeQuestionScene mqs = new MakeQuestionScene();
    try {
      mqs.start(makeQuestionWindow);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  /**
   * Helper method to add an individual question to the masterQuestionBank from the json file chosen by loadQuestionsButton
   * and loadQuestions() is called. Will repeatedly call this from loadQuestions() as long as there is a question to add.
   */
  private void addQuestion() {
    System.out.println("addQuestion()");
  }
  
  /**
   * Displays UI screen to save and quit (saves to json?)
   * Calls when saveAndQuitButton is pressed
   */
  private void displaySaveExit(ActionEvent event) {
    System.out.println("displaySaveExit()");
    Stage saveExitWindow = new Stage(); // make new window
    saveExitWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window
    saveExitWindow.initOwner(this.mainStage);
    SaveAndQuit save = new SaveAndQuit();
    try {
      save.start(saveExitWindow); // open new window
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  /**
   * Displays UI screen to confirm that the user want to quit without saving
   * Calls when dontSaveAndQuitButton is pressed
   */
  private void displayNoSaveExit(ActionEvent event) {
    System.out.println("displayNoSaveExit()"); // debug message
    Stage noSaveExitWindow = new Stage(); // make new window
    noSaveExitWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window
    noSaveExitWindow.initOwner(this.mainStage);
    NoSaveAndQuit noSave = new NoSaveAndQuit(); // save without quitting instance
    
    try {
      noSave.start(noSaveExitWindow); // open new window
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  /**
   * Calls when takeTestButton is pressed
   * Displays quiz screen, maybe called from another class?
   */
  private void displayQuiz(ActionEvent event) {
    // check numQuestionsTextField entry for an int, close and reprompt if there isnt one
    try {
      System.out.println("displayQuiz()");

    } catch (NumberFormatException e) {
    }

  }
  
  /**
   * Calls after quiz and series of quiz questions end (from pressing takeTestButton)
   * Displays results of the quiz (from quizResults class)
   */
  private void displayQuizResult() {
    System.out.println("displayQuizResult()");
  }


  public static void main(String[] args) {
    Application.launch(args);
  }
}