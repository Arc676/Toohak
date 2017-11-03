package backend;

import java.io.Serializable;

public class PlayerFeedback implements Serializable {

	private static final long serialVersionUID = -3445887382336801372L;
	
	private String precedingPlayer;
	private int scoreDelta;
	private boolean wasCorrect;
	
	public PlayerFeedback(String player, int delta, boolean wasCorrect) {
		precedingPlayer = player;
		scoreDelta = delta;
		this.wasCorrect = wasCorrect;
	}
	
	public boolean answerWasCorrect() {
		return wasCorrect;
	}
	
	public String getPrecedingPlayer() {
		return precedingPlayer;
	}
	
	public int getScoreDelta() {
		return scoreDelta;
	}

}