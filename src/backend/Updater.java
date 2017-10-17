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

public class Updater implements Runnable {
	
	private Updatable updatable;

	public Updater(Updatable comp) {
		this.updatable = comp;
	}
	
	public void run() {
		long lastUpdate = System.nanoTime();
		double delta = 0;
		while (updatable.stillRunning()) {
			long now = System.nanoTime();
			delta += (now - lastUpdate) / 1000000000;
			if (delta >= 0.49) {
				updatable.update();
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}