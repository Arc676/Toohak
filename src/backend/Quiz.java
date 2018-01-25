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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class for representing quizzes
 * @author Ale
 *
 */
public class Quiz implements Serializable {
	
	private static final long serialVersionUID = -348125175644252486L;
	
	public final String quizName;
	
	private transient int currentQuestion = 0;
	private ArrayList<Question> questionList;

	protected static Random rgen = new Random();
	
	public Quiz(String name, ArrayList<Question> questions) {
		quizName = name;
		questionList = questions;
	}
	
	public ArrayList<Question> getQuestionList() {
		return questionList;
	}
	
	/**
	 * Shuffle the questions in the quiz such
	 * that they don't appear in the same order
	 * in which they were saved
	 */
	public void shuffleQuestions() {
		for (int i = questionList.size() - 1; i > 0; i--) {
			int idx = Quiz.rgen.nextInt(i + 1);
			if (idx != i) {
				// swap questions
				Question tmp = questionList.get(i);
				questionList.set(i, questionList.get(idx));
				questionList.set(idx, tmp);
			}
		}
	}
	
	/**
	 * Gets the next question in the quiz
	 * @return The next question, or null if there aren't any left
	 */
	public Question nextQuestion() {
		if (currentQuestion == questionList.size()) {
			return null;
		}
		return questionList.get(currentQuestion++);
	}
	
	/**
	 * Read a quiz from file
	 * @param filename Desired input filename
	 * @return Quiz object read from file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Quiz read(String filename) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(filename);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Quiz q = (Quiz)ois.readObject();
		ois.close();
		fis.close();
		return q;
	}
	
	/**
	 * Write a quiz to a file
	 * @param filename Desired output filename
	 * @throws IOException
	 */
	public void save(String filename) throws IOException {
		FileOutputStream fos = new FileOutputStream(filename);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this);
		oos.close();
		fos.close();
	}

}