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
import java.util.ArrayList;

public class Question implements Serializable {

	private static final long serialVersionUID = 7951903683952122757L;
	
	private int points;
	private int timeLimit;
	private String question;
	private ArrayList<String> answers;
	private boolean[] acceptableAnswers;
	
	public Question(String question, int time, int points, ArrayList<String> ans, boolean[] okAns) {
		this.question = question;
		this.points = points;
		timeLimit = time;
		answers = ans;
		acceptableAnswers = okAns;
	}
	
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