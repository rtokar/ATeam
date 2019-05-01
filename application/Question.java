package application;

import java.io.File;
import java.util.ArrayList;

/**
 * An instance of a question that will contain a list 
 * of answers to be displayed with the question String.
 * 
 * @author Rose Tokar
 *
 */
public class Question {
	private String questionTopic; // the question topic value as a string
	private String questionText; // the question that is being asked
	private ArrayList<Answer> answers; // list of the Answer objects for question
	private String metadata; // undetermined, but included to keep consistency with JSON file
	private File imageFile; // image file name if question requires an image
	
	public Question(String topic, String text, ArrayList<Answer> answers, String metadata) {
		this.questionTopic = topic;
		this.questionText = text;
		this.answers = answers;
		this.metadata = metadata;
	}
	
	public Question(String topic, String text, ArrayList<Answer> answers, String metadata, File image) {
		this.questionTopic = topic;
		this.questionText = text;
		this.answers = answers;
		this.metadata = metadata;
		this.imageFile = image;
	}
	
	// Returns number of answers to question
	public int numAnswers() {
		return answers.size();
	}
	
	public String getQuestionTopic() {
		return questionTopic;
	}
	
	public String getQuestionText() {
		return questionText;
	}
	
	public ArrayList<Answer> getAnswersList() {
		return answers;
	}
	
	public String getMetadata() {
		return metadata;
	}
	
	public File getImage() {
		return imageFile;
	}
}
