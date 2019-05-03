package application;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * This is the main driver for the quiz generator.
 * @author ascherer, jthalacker
 *
 */
public class Main extends Application{
  
  protected static QuestionBank masterQuestionBank = new QuestionBank(); // bank of questions from all loaded json files
  protected QuestionBank topicQuestionBank; // all questions with topic matching selected topics
  protected QuestionBank quizQuestionBank; // a subset of randomized questions from selected topics, 
                                           // number equal to numQuizQuestions (or num of qs in topicQuestionBank, whichever is lower)
  
  protected List<String> topicList = new ArrayList<String>(); // list of topics from masterQuestionBank
  protected List<String> currentTopics; // list of chosen topics from topicList
  protected int numQuizQuestions = 0; // num entered in the TextField numQuestionsTextField
  protected String selectedTopic; // topic currently selected with the loadedTopicsComboBox
  
  private static Stage mainStage;
  
  private ObservableList<String> comboBoxList = FXCollections.observableArrayList(this.topicList); // ComboBox must be a field in order to be properly updated when new topics are added
  private ComboBox<String> loadedTopicsComboBox = new ComboBox<String>(comboBoxList);
  
  private ListView<String> chosenTopicsListView = new ListView<String>(); // displays chosen topics, a field for updating purposes
  private int numTopicQuestions = 0; // for display label above curentTopics display, will update as more topics are added
  private int numTotalQuestions = 0; // for display label above currentTopics display, updates as more questions are loaded to masterQuestionBank
  private Label numOfTopicQuestionsLabel = new Label("("+this.numTopicQuestions+" topic questions) / ("+this.numTotalQuestions+" total questions)"); // displays number of topic questions, updates when new topic is added to list of current topics
  
  protected static Boolean closeFlag = false; // quit windows can set this flag to true to close this main window after they close
  
  protected static Main mainInstance; // this is a weird Inception-style static field that hold the only instance of Main that will ever be created
                          
  protected static Stage firstQuestion; // first question scene window, for easy murder
  
  

  /**
   * Primary driver of the application, creates main GUI from which all functions and other GUIS stem from.
   */
  @SuppressWarnings("unchecked")
  public void start(Stage primaryStage) {
    try {
      mainInstance = this;
      mainStage = primaryStage;
      
      //Create main window
      HBox root = new HBox(30);
      //Create two sides
      VBox vBox1 = new VBox(58);
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
      
      loadedTopicsComboBox.setPromptText("<Select a Topic to add>");
      Label comboBoxLabel = new Label("<Load Questions to have Topics>");
      loadedTopicsComboBox.setPlaceholder(comboBoxLabel);
      
      
        //addAndClear HBox buttons
      Button addTopicButton = new Button("Add Topic");
      Button clearTopicListButton = new Button("Clear Topic List");
      
      Label chosenTopicListLabel = new Label("Chosen Topics:");

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

      
    //basically calls displayNoSaveExit() when pressing red [x] in corner
      primaryStage.setOnCloseRequest(event -> { // calls on closing program
        event.consume(); // stops them from cancelling
        Stage noSaveExitWindow = new Stage(); // make new window
        noSaveExitWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window
        noSaveExitWindow.initOwner(mainStage);
        NoSaveAndQuit noSave = new NoSaveAndQuit(); // save without quitting instance
        try {
          noSave.start(noSaveExitWindow); // open new window
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
        
      });
      
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
      try {
      masterQuestionBank.addAllQuestions(jsonFile); // adds all questions from the chosen json file

      } catch (FileNotFoundException e) {

      } catch (IOException e) {

      } catch (ParseException e) {

        System.out.println(e.getMessage());
      } catch (Exception e) {

        System.out.println(e.getMessage());
      }
      this.updateTopicList();
      this.loadedTopicsComboBox.setItems(FXCollections.observableArrayList(this.topicList)); // update topic list after loading new questions from file
      this.numTotalQuestions = masterQuestionBank.questions.size();
      this.numOfTopicQuestionsLabel.setText("("+this.numTopicQuestions+" topic questions) / ("+this.numTotalQuestions+" total questions)");
    }
  }
  
  /**
   * Called when the user presses enter while focus is in the numQuizQuestionsTextField,
   * saves the currently entered int as the number of quiz questions wanted in the quiz
   */
  private void getNumQuestions(ActionEvent event) {

    try {
      int n = Integer.parseInt(((TextField) event.getSource()).getText());
      if (n < 1)
        throw new Exception();
      this.numQuizQuestions = n;
    } catch (NumberFormatException e) {
      
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("A Non-Integer Was Entered");
      alert.setHeaderText(null);
      alert.setContentText("A non-integer was entered!\n\nPlease type a positive integer in this field and press the \"Enter\" key.");
      alert.showAndWait();

    } catch (Exception e) {
      
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("An Invalid Integer Was Entered");
      alert.setHeaderText(null);
      alert.setContentText("An invalid integer was entered!\n\nPlease type a positive integer in this field and press the \"Enter\" key.");
      alert.showAndWait();
    }
  }
  //
  /**
   * Called when the addTopicButton is pressed.
   * Adds the currently selected topic in the loadedTopicsComboBox to the currentTopics (no duplicates)
   */
  private void addTopic(ActionEvent event) {
    if (this.currentTopics == null) { // if currentTopics hasnt been initialized yet, do it
      this.currentTopics = new ArrayList<String>();
    }

    if (this.selectedTopic != null && !this.currentTopics.contains(this.selectedTopic)) { // add selectedTopic to currentTopics if it isnt null and not already there
      this.currentTopics.add(this.selectedTopic);
      this.updateTopicList();
    }
    Collections.sort(this.currentTopics);
    this.chosenTopicsListView.setItems(FXCollections.observableArrayList(this.currentTopics)); // update visible list with added topic
    this.topicQuestionBank = masterQuestionBank.filterQuestions(this.currentTopics); // create the topicQuestionBank as a filtered masterQuestionBank

    this.numTopicQuestions = this.topicQuestionBank.questions.size(); 

    this.numOfTopicQuestionsLabel.setText("("+this.numTopicQuestions+" topic questions) / ("+this.numTotalQuestions+" total questions)");
  }
  
  /**
   * Called when the clearTopicListButton is pressed.
   * Removes all topic/s from currentTopics.
   */
  private void clearTopicList(ActionEvent event) {
    this.currentTopics = new ArrayList<String>(); // reset currentTopics with a blank ArrayList
    this.chosenTopicsListView.setItems(FXCollections.observableArrayList(this.currentTopics)); // update visible list
    this.topicQuestionBank = null;
    this.numTopicQuestions = 0;
    this.numOfTopicQuestionsLabel.setText("("+this.numTopicQuestions+" topic questions) / ("+this.numTotalQuestions+" total questions)");
  }
  
  /**
   * Displays the MakeQuestion screen when makeQuestionButton is pressed.
   */
  private void displayMakeQuestion(ActionEvent event) {
    Stage makeQuestionWindow = new Stage(); // make new window
    makeQuestionWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window
    makeQuestionWindow.initOwner(mainStage);
    MakeQuestionScene mqs = new MakeQuestionScene();
    try {
      mqs.start(makeQuestionWindow);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }
  
  /**
   * Displays UI screen to save and quit (saves to json?)
   * Calls when saveAndQuitButton is pressed
   */
  private void displaySaveExit(ActionEvent event) {
    
    // pull up file chooser to select save .json file, cancel will cancel the quitting process
    Stage chooseStage = new Stage(); // new window for file chooser
    chooseStage.initOwner(mainStage); // set main stage as owner
    chooseStage.initModality(Modality.WINDOW_MODAL); // lock focus to file chooser 
    FileChooser choose = new FileChooser();
    choose.setTitle("Choose a .json file to load questions");
    FileChooser.ExtensionFilter filt = new FileChooser.ExtensionFilter(".json", "*.json"); // only allow .json files
    choose.getExtensionFilters().add(filt);
    File initDirectory = new File("..");// set inital directory for searching as parent directory of this file
    choose.setInitialDirectory(initDirectory);
    File jsonFile = choose.showSaveDialog(chooseStage); // launch file choosing dialog box
    
    if (jsonFile != null) { // if a file was chosen
      this.save(jsonFile); // save the masterQuestionBank in it
      kill(); // close main GUI
    }
  }
  
  /**
   * Displays UI screen to confirm that the user want to quit without saving
   * Calls when dontSaveAndQuitButton is pressed
   */
  private void displayNoSaveExit(ActionEvent event) {
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

    try {
      int numQs = Math.min(this.topicQuestionBank.questions.size(), this.numQuizQuestions);
      this.quizQuestionBank = this.topicQuestionBank.getQuizQuestionBank(numQs);

    } catch (Exception e) {
        // do nothing
    }
      
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
      this.quizQuestionBank = this.topicQuestionBank.getQuizQuestionBank(numQs);
      Stage quizWindow = new Stage();
      firstQuestion = quizWindow;
      QuestionScene quiz = new QuestionScene(this.quizQuestionBank, 0, new QuizResult()); // 0 is the starting question number
      quizWindow.initModality(Modality.WINDOW_MODAL); // lock user to new window
      quizWindow.initOwner(mainStage);

      try {
        quiz.start(quizWindow);
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
   * Intermediary method to add a question to the masterQuestionBank in order to properly 
   * update topic list and displayed num of questions.
   */
  protected static void addToMasterQuestionBank(Question q) {
    masterQuestionBank.addQuestion(q);
    // when questions are done being made, update the # of total questions label
    mainInstance.updateTopicList();
    mainInstance.updateNumOfQuestionsLabel();
  }
  
  /**
   * Saves the masterQuestionBank to the selected json file.
   * @parameter jsonFile is the file in which we will save the contents of masterQuestionBank
   */
  @SuppressWarnings("unchecked")
  private void save(File jsonFile) {
    
    JSONObject wholeThang = new JSONObject(); // put "question array" in here
    JSONArray questions = new JSONArray(); // add all question s to this
    
    // loop through questions
    for (int q_i = 0; q_i < masterQuestionBank.questions.size(); q_i ++) {
      Question bankQuestion = masterQuestionBank.questions.get(q_i);
      JSONObject question = new JSONObject();
      question.put("meta-data", bankQuestion.getMetadata()); 
      question.put("questionText", bankQuestion.getQuestionText());
      question.put("topic", bankQuestion.getQuestionTopic());
      File imageFile = bankQuestion.getImage();
      String image = "";
      if (imageFile == null)
        image += "none";
      else
        image += imageFile.toString();
      question.put("image", image);
      JSONArray answers = new JSONArray();
      // loop through answers
      for (int a_i = 0; a_i < bankQuestion.getAnswersList().size(); a_i++) {
        Answer bankAnswer = bankQuestion.getAnswersList().get(a_i);
        JSONObject answer = new JSONObject();
        String trueness = "";
        if (bankAnswer.getCorrectness())
          trueness += "T";
        else
          trueness += "F";
        answer.put("isCorrect", trueness);
        answer.put("choice", bankAnswer.getAnswerText());
        answers.add(answer);
      }
      // put everything together
      question.put("choiceArray", answers);
      questions.add(question);
      wholeThang.put("questionArray", questions);
    }
    
    try { // write json to file
      FileWriter file = new FileWriter(jsonFile);
      file.write(wholeThang.toJSONString());
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
  
  /**
   * To be called by Quiz Result -> exits all question scene windows
   */
  protected static void killAllQuestionScenes() {
    firstQuestion.close();
  }
  
  public static void main(String[] args) {
    Application.launch(args);
  }
}