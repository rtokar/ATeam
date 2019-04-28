package application;

/**
 * Created when the user takes a quiz, keeps track of correct 
 * answers and calculates % correct when the quiz is finished.
 * 
 * @author Rose Tokar
 *
 */
public class QuizResult {
	private int numCorrect; // number of questions correct
	private int numAnswered; // number of questions answered
	
	public QuizResult() {
		//n/a?
	}
	
	/**
	 * This will calculate the percent correct out 
	 * of number answered.
	 * 
	 * @return percentage correctly answered
	 */
	public double getPercent() {
		return numCorrect/numAnswered;	
	}

}
