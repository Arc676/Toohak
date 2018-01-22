//Written by Alessandro Vinciguerra <alesvinciguerra@gmail.com>
//Copyright (C) 2017  Arc676/Alessandro Vinciguerra

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation (version 3).

//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.
//See README.txt and LICENSE.txt for more details

package backend;

import java.io.Serializable;

/**
 * Encapsulates player feedback after a question
 * ends. Includes information for players regarding
 * their position relative to other players and
 * acceptable answers to the question.
 * @author Ale
 *
 */
public class PlayerFeedback implements Serializable {

	private static final long serialVersionUID = -3445887382336801372L;
	
	private String precedingPlayer;
	private int scoreDelta;
	private int position;
	private boolean wasCorrect;
	private Question question;
	
	/**
	 * Create new instance
	 * @param player Nickname of player ahead of this player
	 * @param delta Score difference between this player and the player ahead
	 * @param wasCorrect Whether the player answered this question correctly
	 * @param position Player's current position in the leaderboard
	 * @param question The question that was just answered
	 */
	public PlayerFeedback(String player, int delta, boolean wasCorrect, int position, Question question) {
		precedingPlayer = player;
		scoreDelta = delta;
		this.wasCorrect = wasCorrect;
		this.position = position;
		this.question = question;
	}
	
	public Question getQuestion() {
		return question;
	}
	
	public int getPosition() {
		return position;
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