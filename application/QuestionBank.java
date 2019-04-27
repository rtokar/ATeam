package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 


public class QuestionBank {
	List<Question> questions = new ArrayList<Question>();
	String topic;
	int numQuizQuestions;
	public QuestionBank() {
		topic = "undefined";
		numQuizQuestions = 0;
	}
	public QuestionBank(String jsonFilePath) {
		List<Question> questions = readJson(jsonFilePath);
		topic = "undefined";
		numQuizQuestions = questions.size();
	}
	protected QuestionBank filterQuestions(String topics) {
		//return filtered questions as a QuestionBank type.
	}
	protected void addQuestion(Question question) {
		questions.add(question);
	}
	protected List<Question> getQuestionOfSameTopic(String topic) {
		List<Question> topicQuestionList = new ArrayList<Question>();
		for(Question q: questions) {
			if (q.topic.equals(topic)) {
				topicQuestionList.add(q);
			}
		}
		return topicQuestionList;
	}
	
	protected void addAllQuestions(File jsonFile) {
		
	}
	private List<Question> readJson(File jsonFilepath) {
		// parse a json file and return it as a list of questions.
		
		//init 
		List<Question> questions = new ArrayList<Quesiton>();
		
		// parsing json file 
        Object obj = new JSONParser().parse(new FileReader(jsonFilepath)); 
        
        // typecasting obj to JSONObject 
        JSONObject jo = (JSONObject) obj; 
          
        // getting name and lastName 
        JSONArray questionArray = (JSONArray) jo.get("questionArray");
        for(int i = 0; i< questionArray.size();i++) {
        	Quesiton curQuestion = new Question();
        	JSONObject questionInfo = (JSONObject) questionArray.get(i);
        	curQuestion.metaData = (String) questionInfo.get("meta-data");
        	curQuestion.questionText = (String) questionInfo.get("questionText");
        	curQuestion.topic = (String) questionInfo.get("topic");
        	curQuestion.image = (String) questionInfo.get("image");
        	curQuestion.choiceArray = (String) questionInfo.get("choiceArray");
        	questions.add(curQuestion);
    	}
        return questions;
	}
	

}
