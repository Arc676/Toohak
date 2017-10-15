package backend;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class QuestionListModel extends AbstractTableModel {

	private static final long serialVersionUID = -1939562168124350380L;

	private ArrayList<Question> questions;

	public QuestionListModel() {
		questions = new ArrayList<Question>();
	}

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