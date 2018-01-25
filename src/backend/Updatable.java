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
 * Interface for classes that need to be updated at regular intervals.
 * Referred to by Updater.
 * @author Ale
 *
 */
public interface Updatable {
	
	/**
	 * Update the receiver. Called by Updater
	 * at regular intervals
	 */
	public void update();
	
	/**
	 * Used by Updater to determine when the
	 * thread can be stopped
	 * @return Whether the receiver still needs updating
	 */
	public boolean stillRunning();
	
}