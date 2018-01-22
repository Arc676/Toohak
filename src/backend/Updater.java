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

/**
 * Generic runnable class for views that need to be updated at regular
 * intervals. Used by both server and client.
 * @author Ale
 *
 */
public class Updater implements Runnable {
	
	private Updatable updatable;
	private double delay;

	public Updater(Updatable comp, double delay) {
		this.updatable = comp;
		this.delay = delay;
	}
	
	public void run() {
		long lastUpdate = System.nanoTime();
		double delta = 0;
		while (updatable.stillRunning()) {
			long now = System.nanoTime();
			delta = (now - lastUpdate) / 1000000000;
			if (delta >= delay - 0.1) {
				updatable.update();
				lastUpdate = now;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}