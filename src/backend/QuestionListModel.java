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

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * Mutable table model for tables storing questions. Used
 * by QuizEditor.
 * @author Ale
 *
 */
public class QuestionListModel extends AbstractTableModel {

	private static final long serialVersionUID = -1939562168124350380L;

	private ArrayList<Question> questions;

	public QuestionListModel() {
		questions = new ArrayList<Question>();
	}

	/**
	 * Tells the table that new data is available
	 */
	public void updateData() {
		fireTableDataChanged();
	}
	
	public ArrayList<Question> getObjects() {
		return questions;
	}

	public void addQuestion(Question q) {
		questions.add(q);
		updateData();
	}

	public void removeQuestion(int index) {
		questions.remove(index);
		updateData();
	}

	public void clear() {
		questions.clear();
		updateData();
	}

	public String getColumnName(int col) {
		return "Question";
	}

	@Override
	public int getRowCount() {
		return questions.size();
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return questions.get(rowIndex).toString();
	}

}