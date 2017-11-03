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
import java.util.Comparator;

import javax.swing.table.AbstractTableModel;

public class LeaderboardModel extends AbstractTableModel {

	private static final long serialVersionUID = 551612281831791001L;

	private final String[] columnNames = { "Player Name", "Score" };
	private ArrayList<Object[]> objects;

	private boolean gameHasStarted = false;
	private int[] deltas;

	public LeaderboardModel() {
		objects = new ArrayList<Object[]>();
	}

	public void initializeDeltas() {
		gameHasStarted = true;
		deltas = new int[objects.size()];
	}

	public void updateData() {
		if (gameHasStarted) {
			//update the scores on the leaderboard
			for (int i = 0; i < deltas.length; i++) {
				objects.get(i)[1] = (int) objects.get(i)[1] + deltas[i];
				deltas[i] = 0;
			}
			//sort leaderboard based on score
			objects.sort(new Comparator<Object[]>() {
				public int compare(Object[] o1, Object[] o2) {
					if ((int) o1[1] > (int) o2[1]) {
						return -1;
					} else if ((int) o1[1] < (int) o2[1]) {
						return 1;
					}
					return 0;
				}
			});
		}
		//redraw
		fireTableDataChanged();
	}

	public void changeScore(String player, int delta) {
		int index = 0;
		for (Object[] tableItem : objects) {
			if (tableItem[0].toString().equals(player)) {
				deltas[index] = delta;
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
		deltas = new int[0];
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