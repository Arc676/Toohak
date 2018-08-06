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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for representing individual questions
 * @author Ale
 *
 */
public class Question implements Serializable {

	private static final long serialVersionUID = 876478610869525268L;
	
	public transient static final String keyHasImage = "hasImageData";
	public transient static final String keyImageData = "imageData";
	
	private int points;
	private int timeLimit;
	private String question;
	private ArrayList<String> answers;
	private boolean[] acceptableAnswers;
	
	private Map<String, Object> multimediaData;
	
	/**
	 * Create a new question
	 * @param question Human-readable question
	 * @param time Seconds available to answer
	 * @param points Maximum points obtainable by answering
	 * @param ans Possible answers
	 * @param okAns Acceptable answers as a boolean list corresponding to the answer list
	 * @throws IOException if ImageIO fails to save image as byte array
	 */
	public Question(String question, int time, int points, ArrayList<String> ans, boolean[] okAns) throws IOException {
		this.question = question;
		this.points = points;
		timeLimit = time;
		answers = ans;
		acceptableAnswers = okAns;
		multimediaData = new HashMap<String, Object>();
	}
	
	public Object getMultimediaDataForKey(String key, Object def) {
		return multimediaData.getOrDefault(key, def);
	}
	
	public void setMultimediaDataForKey(String key, Object obj) {
		multimediaData.put(key, obj);
	}
	
	/**
	 * Create a copy of the question without the correct answers to
	 * send to the clients and prevent cheating with modified clients
	 * @return A copy of the question without the correct answer
	 */
	public Question getSendableCopy() {
		try {
			Question q = new Question(question, timeLimit, points, answers, new boolean[] {});
			for (Map.Entry<String, Object> entry : multimediaData.entrySet()) {
				q.setMultimediaDataForKey(entry.getKey(), entry.getValue());
			}
			return q;
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Shuffle the answers such that they
	 * don't appear in the same order in which
	 * they are saved
	 */
	public void shuffleAnswers() {
		for (int i = 3; i > 0; i--) {
			int idx = Quiz.rgen.nextInt(i + 1);
			if (idx != i) {
				// swap acceptable answers
				acceptableAnswers[i] ^= acceptableAnswers[idx];
				acceptableAnswers[idx] ^= acceptableAnswers[i];
				acceptableAnswers[i] ^= acceptableAnswers[idx];
				
				// swap text
				String tmp = answers.get(i);
				answers.set(i, answers.get(idx));
				answers.set(idx, tmp);
			}
		}
	}
	
	/**
	 * Determines whether a given answer is acceptable
	 * @param ans Answer number
	 * @return Whether the answer is acceptable
	 */
	public boolean acceptAnswer(int ans) {
		return acceptableAnswers[ans];
	}
	
	public String getCorrectAnswers() {
		String ans = "";
		int i = 0;
		int correct = 0;
		for (boolean ok : acceptableAnswers) {
			if (ok) {
				if (correct > 0) {
					ans = ans.concat("/");
				}
				correct++;
				ans = ans.concat(answers.get(i));
			}
			i++;
		}
		return ans;
	}
	
	public String getQ() {
		return question;
	}
	
	public ArrayList<String> getAnswers() {
		return answers;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getTimeLimit() {
		return timeLimit;
	}
	
	public String toString() {
		String str = question + " (";
		for (int i = 0; i < 4; i++) {
			str = str.concat(answers.get(i) + (i < 3 ? "," : ") "));
		}
		str = str.concat(timeLimit + "s, " + points + " pts");
		return str;
	}

}