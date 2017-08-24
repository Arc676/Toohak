package backend;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class LeaderboardModel extends AbstractTableModel {

	private static final long serialVersionUID = 551612281831791001L;

	private final String[] columnNames = { "Player Name", "Score" };
	private Object[][] data = {};
	private ArrayList<Object[]> objects;

	public LeaderboardModel() {
		this.objects = new ArrayList<Object[]>();
	}

	public <T> T[][] getObjects(T[][] arr) {
		return objects.toArray(arr);
	}

	public void updateData() {
		data = getObjects(data);
		fireTableDataChanged();
	}

	public void changeScore(String player, int delta) {
		int index = 0;
		for (Object[] tableItem : getObjects(new Object[][] {})) {
			if (tableItem[0].toString().equals(player)) {
				objects.get(index)[1] = (int) tableItem[1] + delta;
				updateData();
				return;
			}
			index++;
		}
		addPlayer(player, delta);
	}

	public void addPlayer(String name, int score) {
		Object[] objs = new Object[] { name, score };
		objects.add(objs);
		updateData();
	}

	public void removePlayer(int index) {
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
		return data[rowIndex][columnIndex];
	}

}