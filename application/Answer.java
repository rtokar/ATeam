package application;

/**
 * A class that has a boolean correctness field for whether 
 * it is the correct answer to the question or not, as well 
 * as a string with the answer text supplied.
 * 
 * @author Rose Tokar
 *
 */
public class Answer {
	private boolean correctness; // determines if this answer is for the question
	private String answerText; // what the answer text is (eg: Time complexity is O(N))
	
	public Answer(boolean correctness, String answerText) {
		this.correctness = correctness;
		this.answerText = answerText;
	}

	public boolean getCorrectness() {
		return correctness;
	}
	
	public String getAnswerText() {
		return answerText;
	}
}
