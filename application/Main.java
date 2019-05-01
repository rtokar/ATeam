package application;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//other notes: display trueness of answer when making question?

/**
 * This is the main driver for the quiz generator.
 * @author jthalacker, ascherer
 *
 */
public class Main extends Application{
  
  protected static QuestionBank masterQuestionBank; // bank of questions from all loaded json files
  protected QuestionBank topicQuestionBank; // all questions with topic matching selected topics
  protected QuestionBank quizQuestionBank; // a subset of randomized questions from selected topics, 
                                           // number equal to numQuizQuestions (or num of qs in topicQuestionBank, whichever is lower)
  
  protected List<String> topicList = new ArrayList<String>(); // list of topics from masterQuestionBank
  protected List<String> currentTopics; // list of chosen topics from topicList
  protected int numQuizQuestions; // num entered in the TextField numQuestionsTextField
  protected String selectedTopic; // topic currently selected with the loadedTopicsComboBox
  
  private static Stage mainStage;
  
  private ObservableList<String> comboBoxList = FXCollections.observableArrayList(this.topicList); // ComboBox must be a field in order to be properly updated when new topics are added
  private ComboBox<String> loadedTopicsComboBox = new ComboBox<String>(comboBoxList);
  
  private ListView<String> chosenTopicsListView = new ListView<String>(); // displays chosen topics, a field for updating purposes
  private int numTopicQuestions = 0; // for display label above curentTopics display, will update as more topics are added
  private int numTotalQuestions = 0; // for display label above currentTopics display, updates as more questions are loaded to masterQuestionBank
  private Label numOfTopicQuestionsLabel = new Label("("+this.numTopicQuestions+" topic questions) / ("+this.numTotalQuestions+" total questions)"); // displays number of topic questions, updates when new topic is added to list of current topics
  
  protected static Boolean closeFlag = false; // quit windows can set this flag to true to close this main window after they close
                                                                                                
  
  

  /**
   * Primary driver of the application, creates main GUI from which all functions and other GUIS stem from.
   */
  public void start(Stage primaryStage) {
    try {
      mainStage = primaryStage;
      
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
      numQuestionsTextField.setPromptText("<# of Questions, Press Enter>");
      
        //Hard-coded list
        //TODO change to getting from questions
//      ObservableList<String> comboBoxList = FXCollections.observableArrayList("English", "Reading", "Quantum Physics");
//      ObservableList<String> comboBoxList = FXCollections.observableArrayList(this.topicList); // observable list of topicList
//      ComboBox<String> loadedTopicsComboBox = new ComboBox<String>(comboBoxList);
      loadedTopicsComboBox.setPromptText("<Select a Topic to add>");
      Label comboBoxLabel = new Label("<Load Questions to have Topics>");
      loadedTopicsComboBox.setPlaceholder(comboBoxLabel);
      
      
        //addAndClear HBox buttons
      Button addTopicButton = new Button("Add Topic");
      Button clearTopicListButton = new Button("Clear Topic List");
      
      Label chosenTopicListLabel = new Label("Chosen Topics:");
       // TODO: may need to make this a field too so we can update it as topics are added
      VBox topicLabels = new VBox(5);
      topicLabels.getChildren().addAll(numOfTopicQuestionsLabel, chosenTopicListLabel);
      
      chosenTopicsListView.setMaxSize(175, 100);
      Button dontSaveAndQuitButton = new Button("Quit Without Saving");
      
      //vbox and hbox creation/updates
      
      addAndClear.getChildren().addAll(addTopicButton, clearTopicListButton);
      vBox1.getChildren().addAll(makeQuestionButton, loadQuestionsButton, takeTestButton, saveAndQuitButton);
      vBox2.getChildren().addAll(numQuestionsTextField, loadedTopicsComboBox, 
          addAndClear, topicLabels, chosenTopicsListView, dontSaveAndQuitButton);
      root.getChildren().addAll(vBox1, vBox2);
      
      //Create scene and update stage
      Scene scene = new Scene(root,415,350);
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
      numQuestionsTextField.setOnAction(this::getNumQuestions);
      loadedTopicsComboBox.setOnAction((event) -> this.selectedTopic = ((ComboBoxBase<String>) event.getSource()).getValue()); // sets the selected topic when a topic is selected
      //mainStage.setOnCloseRequest(this::displayNoSaveExit);  // attempt to make NoSaveAndQuit happen when pressing red [x] in corner
      
      
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
    File initDirectory = new File("..");// set inital directory for searching as parent directory of this file
    choose.setInitialDirectory(initDirectory);
    File jsonFile = choose.showOpenDialog(chooseStage); // launch file choosing dialog box
    if (jsonFile != null) {
      //masterQuestionBank.addAllQuestions(jsonFile); // adds all questions from the chosen json file TODO: uncomment this and next line when QuestionBank is fixed, delete last line in this method, updateTopicList ill take care of it
      //this.updateTopicList();
      this.topicList.add("blarg"); this.topicList.add("blarg2");
      this.loadedTopicsComboBox.setItems(FXCollections.observableArrayList(this.topicList)); // update topic list after loading new questions from file
      //this.numTotalQuestions = masterQuestionBank.questions.size();
      this.numTotalQuestions = 23;
      this.numOfTopicQuestionsLabel.setText("("+this.numTopicQuestions+" topic questions) / ("+this.numTotalQuestions+" total questions)");
    }

    System.out.println("loadQuestions()");
    System.out.println("Selected File: "+jsonFile);
  }
  
  /**
   * Called when the user presses enter while focus is in the numQuizQuestionsTextField,
   * saves the currently entered int as the number of quiz questions wanted in the quiz
   */
  private void getNumQuestions(ActionEvent event) {
    System.out.println("pressed enter");
    try {
      this.numQuizQuestions = Integer.parseInt(((TextField) event.getSource()).getText());
    } catch (NumberFormatException e) {
      //TODO: throw popup that says to enter an int into text field in upper right -> vbox with label containing message and OK button to close window
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("A Non-Integer Was Entered");
      alert.setHeaderText(null);
      alert.setContentText("A non-integer was entered!\n\nPlease type a positive integer in this field and press the \"Enter\" key.");
      alert.showAndWait();
      
      System.out.println("no int!");
    }
  }
  
  /**
   * Called when the addTopicButton is pressed.
   * Adds the currently selected topic in the loadedTopicsComboBox to the currentTopics (no duplicates)
   */
  private void addTopic(ActionEvent event) {
    if (this.currentTopics == null) { // if currentTopics hasnt been initialized yet, do it
      this.currentTopics = new ArrayList<String>();
    }
    System.out.println("selectedTopic: "+this.selectedTopic);
    if (this.selectedTopic != null && !this.currentTopics.contains(this.selectedTopic)) { // add selectedTopic to currentTopics if it isnt null and not already there
      this.currentTopics.add(this.selectedTopic);
    }
    System.out.println("addTopic()");
    System.out.println("currentTopics: "+this.currentTopics);
    this.chosenTopicsListView.setItems(FXCollections.observableArrayList(this.currentTopics)); // update visible list with added topic
    //this.topicQuestionBank = masterQuestionBank.filterQuestions(this.currentTopics); // create the topicQuestionBank as a filtered masterQuestionBank TODO: check if Clarence is overloading filterQuestions()
    //this.numTopicQuestions = this.topicQuestionBank.questions.size();
    this.numTopicQuestions = 5;
    this.numOfTopicQuestionsLabel.setText("("+this.numTopicQuestions+" topic questions) / ("+this.numTotalQuestions+" total questions)");
  }
  
  /**
   * Called when the clearTopicListButton is pressed.
   * Removes all topic/s from currentTopics.
   */
  private void clearTopicList(ActionEvent event) {
    this.currentTopics = new ArrayList<String>(); // reset currentTopics with a blank ArrayList
    this.chosenTopicsListView.setItems(FXCollections.observableArrayList(this.currentTopics)); // update visible list
    this.topicQuestionBank = null; //TODO: check that resetting topicQuestionBank like this when clearing topics doesnt break anything
    this.numTopicQuestions = 0;
    this.numOfTopicQuestionsLabel.setText("("+this.numTopicQuestions+" topic questions) / ("+this.numTotalQuestions+" total questions)");
    System.out.println("clearTopicList()");
  }
  
  /**
   * Displays the MakeQuestion screen when makeQuestionButton is pressed.
   */
  private void displayMakeQuestion(ActionEvent event) {
    System.out.println("displayMakeQuestion()");
    Stage makeQuestionWindow = new Stage(); // make new window
    makeQuestionWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window
    makeQuestionWindow.initOwner(mainStage);
    MakeQuestionScene mqs = new MakeQuestionScene();
    try {
      mqs.start(makeQuestionWindow);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    // when questions are done being made, update the # of total questions label
    this.updateNumOfQuestionsLabel();
  }
  
  /**
   * Displays UI screen to save and quit (saves to json?)
   * Calls when saveAndQuitButton is pressed
   */
  private void displaySaveExit(ActionEvent event) {
    System.out.println("displaySaveExit()");
//    Stage saveExitWindow = new Stage(); // make new window
//    saveExitWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window
//    saveExitWindow.initOwner(mainStage);
//    SaveAndQuit save = new SaveAndQuit();
//    try {
//      save.start(saveExitWindow); // open new window
//    } catch (Exception e) {
//      System.out.println(e.getMessage());
//    }
    
    // pull up file chooser to select save .json file, cancel will cancel the quitting process
    Stage chooseStage = new Stage(); // new window for file chooser
    chooseStage.initOwner(mainStage); // set main stage as owner
    chooseStage.initModality(Modality.WINDOW_MODAL); // lock focus to file chooser
    FileChooser choose = new FileChooser();
    choose.setTitle("Choose a .json file to load questions");
    FileChooser.ExtensionFilter filt = new FileChooser.ExtensionFilter(".json", "*.json"); // only allow .json files
    choose.getExtensionFilters().add(filt); // TODO: .json files are still greyed out when selecting
    File initDirectory = new File("..");// set inital directory for searching as parent directory of this file
    choose.setInitialDirectory(initDirectory);
    File jsonFile = choose.showSaveDialog(chooseStage); // launch file choosing dialog box
    
    if (jsonFile != null) { // if a file was chosen
      this.save2(jsonFile); // save the masterQuestionBank in it
      kill(); // close main GUI
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
    noSaveExitWindow.initOwner(mainStage);
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
    if (!(this.numQuizQuestions > 0)) { // check that appropriate int was entered in numQuestionsTextField and enter was pressed
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Invalid Number of Quiz Questions");
      alert.setHeaderText(null);
      alert.setContentText("Invalid Number of Quiz Questions!\n\nPlease double check the \"number of questions\" text field in the upper right. Type in a positive integer and press the \"Enter\" key when you are done.");
      alert.showAndWait();
      
    } else if (quizQuestionBank == null || !(quizQuestionBank.questions.size() > 0)) { // check that quizQuestionBank exists has at least one question in it, throw popup error 
      // if no questions are loaded, tell the user such
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("No Questions Loaded");
      alert.setHeaderText(null);
      alert.setContentText("No questions were loaded! \n\nCheck that you have selected a topic with the drop down menu and loaded it with the \"Add Topic\" button.");
      alert.showAndWait();
      
    } else { // if it exists and has at least one question, open test window
      int numQs = Math.min(this.topicQuestionBank.questions.size(), this.numQuizQuestions); // gets the smaller of user-entered number and available quiz questions in topicQuestionBank
      this.quizQuestionBank = this.topicQuestionBank.randomPick(this.currentTopics, numQs); // TODO: double check with Clarence about randomly choosing questions from a question bank as a new bank
      Stage quizWindow = new Stage();
      QuestionScene quiz = new QuestionScene(quizQuestionBank, 0, new QuizResult()); // 0 is the starting question number
      quizWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window
      quizWindow.initOwner(mainStage);
      try {
        quiz.start(quizWindow);
        System.out.println("displayQuiz()");
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

  }

  /**
   * Called when a new file is loaded and its questions are added to the masterQuestionBank.
   * Updates the topicList variable, reflecting all of the topics in the masterQuestionBank.
   */
  private void updateTopicList() {
    if (this.topicList == null) { // initialize topicList
      this.topicList = new ArrayList<String>();
    }
    // search through masterQuestionBank, update the list of all topics (no duplicate listings). create the list if it is currently null
    for (Question q : masterQuestionBank.questions) {
      if (!this.topicList.contains(q.getQuestionTopic())) { // if topicList doesnt already have topic, add it
        this.topicList.add(q.getQuestionTopic());
      }
    }
    this.loadedTopicsComboBox.setItems(FXCollections.observableArrayList(this.topicList)); // update topic list in combobox
  }

  /**
   * Call this whenever you want to update the label showing the total number of questions in masterQuestionBank
   * and the number of questions matching the topics selected.
   */
  private void updateNumOfQuestionsLabel() {
    this.numTotalQuestions = masterQuestionBank.questions.size();
    if (this.topicQuestionBank != null)
      this.numTopicQuestions = this.topicQuestionBank.questions.size();
    this.numOfTopicQuestionsLabel.setText("("+this.numTopicQuestions+" topic questions) / ("+this.numTotalQuestions+" total questions)");
  }
  
  /**
   * Saves the masterQuestionBank to the selected json file.
   * @parameter jsonFile is the file in which we will save the contents of masterQuestionBank
   */
  @SuppressWarnings("unchecked")
  private void save(File jsonFile) {
    // save the question objects of masterQuestionBank in the provided json file
    JSONObject jobj = new JSONObject();
    JSONArray qlist = new JSONArray();
    JSONArray meta = new JSONArray();
    meta.add("this is the meta");
    qlist.add(meta);
    jobj.put("qlist", qlist);
    
    try {
      FileWriter file = new FileWriter(jsonFile);
      file.write(jobj.toJSONString());
      file.close();
      System.out.println("saved");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    
//    for (Question question : masterQuestionBank.questions) {
//      JSONArray meta = new JSONArray();
//      meta.add(""+question.getMetadata());
//      qlist.add(meta);
//      jobj.put("outside",qlist); 
  }
  
  /**
   * Saves the masterQuestionBank to the selected json file using strings instead of json objects
   * @param jsonFile is the file in which we will save the contents of masterQuestionBank
   */
  private void save2(File jsonFile) {
    String fileString = "";
    fileString += "{\n\t\"questionArray\":\n\t[\n\t\t{"; // this brings us to the "meta-data" part of the json file
//    for (Question q : masterQuestionBank.questions) {
//      
//    }
    
    //question loop here
    fileString += "\"meta-data\":\""+ "metadata call here" +"\",\n\t\t\t";
    fileString += "\"questionText\":\""+ "questionText call here" +"\",\n\t\t\t";
    fileString += "\"topic\":\""+ "topic call here" +"\",\n\t\t\t";
    fileString += "\"image\":\""+ "image call here" +"\",\n\t\t\t";
    fileString += "\"choiceArray\":\n\t\t\t";
    fileString += "[\n\t\t\t";
    
    //choice loop here
    fileString += "\t{\"isCorrect\":\"" + "question.true?" + "\",\"choice\":" + "questionChoice" + "\"},"; // TODO: last comma here wont be there for last choice, figure that out
    fileString += "\n\t\t\t]";
    
    //close choiceArray curly bracket
    fileString += "\n\t\t},"; // TODO: only put comma here if there are other questions left (no comma on last question)
    
    //close questionArray bracket
    fileString += "\n\t]";
    //close whole file curly bracket
    fileString += "\n}";
    
    try {
      FileWriter file = new FileWriter(jsonFile);
      file.write(fileString);
      file.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    
        
    
  }
  
  /**
   * Closes the main GUI window, mainly called from NoSaveAndQuit window.
   */
  protected static void kill() {
    mainStage.close();
  }
  
  public static void main(String[] args) {
    Application.launch(args);
  }
}