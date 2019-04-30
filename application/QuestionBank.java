package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;
import java.io.IOException;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

public class QuestionBank {
	List<Question> questions = new ArrayList<Question>();
	List<String> fileTracker = new ArrayList<String>();
	private boolean debug = true;
	public QuestionBank() {
	}
	public QuestionBank(File jsonFilePath) throws FileNotFoundException, IOException, ParseException {
		List<Question> questions = readJson(jsonFilePath);
	}
	protected QuestionBank filterQuestions(String indicatedTopic) {
		List<Question> topicQuestionList = new ArrayList<Question>();
		for(Question q: questions) {
			if (q.getQuestionTopic().equals(indicatedTopic)) {
				topicQuestionList.add(q);
			}
		}
		QuestionBank filteredBank = new QuestionBank();
		for(int i = 0; i< topicQuestionList.size();i++) {
			filteredBank.addQuestion(topicQuestionList.get(i));
		}
		return filteredBank;
	}
	protected void addQuestion(Question question) {
		questions.add(question);
	}
	
	protected void addAllQuestions(File jsonFile) throws FileNotFoundException, IOException, ParseException {
		if(!fileTracker.contains(jsonFile.getAbsolutePath())) {
			List<Question> questions = readJson(jsonFile);
			for(Question q: questions) {
				this.addQuestion(q);
			}
			fileTracker.add(jsonFile.getAbsolutePath());
		}
		if(debug) {
			System.out.println("Debug Mode");
			System.out.println("==========================");
			System.out.println("current number count is: " + this.questions.size());
			System.out.println("==========================");
		}
	}
	
	private List<Question> readJson(File jsonFile) throws FileNotFoundException, IOException, ParseException {
		// parse a json file and return it as a list of questions.
		
		//init 
		List<Question> questions = new ArrayList<Question>();
		// parsing json file 
        Object obj = new JSONParser().parse(new FileReader(jsonFile)); 
        
        // typecasting obj to JSONObject 
        JSONObject jo = (JSONObject) obj; 
          
        // getting name and lastName 
        JSONArray questionArray = (JSONArray) jo.get("questionArray");
        for(int i = 0; i< questionArray.size();i++) {
        	JSONObject questionInfo = (JSONObject) questionArray.get(i);
        	String metaData = (String) questionInfo.get("meta-data");
        	String questionText = (String) questionInfo.get("questionText");
        	String topic = (String) questionInfo.get("topic");
        	String image = (String) questionInfo.get("image");
        	JSONArray choiceArray = (JSONArray) questionInfo.get("choiceArray");
        	List<Answer> answers = new ArrayList<Answer>();
        	String True = "T";
        	
        	for(int j = 0 ; j < choiceArray.size();j++) {
        		boolean boolAns = false;
        		JSONObject CurrAnswer = (JSONObject) choiceArray.get(j);
        		String isCorrect = (String) CurrAnswer.get("isCorrect");
        		if(isCorrect.equals(True)) {
        			boolAns = true;
        		}
        		else {
        			boolAns = false;
        		}
        		String choice = (String) CurrAnswer.get("choice");
        		Answer ans = new Answer(boolAns,choice);
        		answers.add(ans);
        	}
        	Question curQuestion = new Question(topic,questionText,answers,metaData);
        	if(!image.equals("none")) {
        		File img = new File(image);
        		curQuestion = new Question(topic,questionText,answers,metaData,img);
        	}
        	questions.add(curQuestion);
    	}
        return questions;
	}
	

}
