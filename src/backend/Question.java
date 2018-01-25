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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Class for representing individual questions
 * @author Ale
 *
 */
public class Question implements Serializable {

	private static final long serialVersionUID = 876478610869525268L;
	
	private int points;
	private int timeLimit;
	private String question;
	private ArrayList<String> answers;
	private boolean[] acceptableAnswers;
	
	private boolean hasImage = false;
	private byte[] imageBytes;
	
	private static Random rgen = new Random();
	
	/**
	 * Create a new question
	 * @param question Human-readable question
	 * @param time Seconds available to answer
	 * @param points Maximum points obtainable by answering
	 * @param ans Possible answers
	 * @param okAns Acceptable answers as a boolean list corresponding to the answer list
	 * @param image Attached image (can be null)
	 * @throws IOException if ImageIO fails to save image as byte array
	 */
	public Question(String question, int time, int points, ArrayList<String> ans, boolean[] okAns, BufferedImage image) throws IOException {
		this.question = question;
		this.points = points;
		timeLimit = time;
		answers = ans;
		acceptableAnswers = okAns;
		if (image != null) {
			hasImage = true;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			imageBytes = baos.toByteArray();
		}
	}
	
	public boolean questionHasImage() {
		return hasImage;
	}
	
	public byte[] getImageBytes() {
		return imageBytes;
	}
	
	/**
	 * Create a copy of the question without the correct answers to
	 * send to the clients and prevent cheating with modified clients
	 * @return A copy of the question without the correct answer
	 */
	public Question getSendableCopy() {
		try {
			Question q = new Question(question, timeLimit, points, answers, new boolean[] {}, null);
			if (hasImage) {
				q.hasImage = true;
				q.imageBytes = Arrays.copyOf(imageBytes, imageBytes.length);
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
			int idx = rgen.nextInt(i + 1);
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