package backend;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class QuestionListModel extends AbstractTableModel {

	private static final long serialVersionUID = -1939562168124350380L;

	private final String[] columnNames = { "Question", "Answer A", "Answer B", "Answer C", "Answer D" };
	private ArrayList<String[]> objects;

	public QuestionListModel() {
		this.objects = new ArrayList<String[]>();
	}

	public void updateData() {
		fireTableDataChanged();
	}

	public void addQuestion(String...objs) {
		if (objs.length != 5) {
			throw new IllegalArgumentException("Must give 5 arguments");
		}
		objects.add(objs);
		updateData();
	}

	public void removeQuestion(int index) {
		objects.remove(index);
		updateData();
	}

	public void clear() {
		objects.clear();
		updateData();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		return objects.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return objects.get(rowIndex)[columnIndex];
	}

}