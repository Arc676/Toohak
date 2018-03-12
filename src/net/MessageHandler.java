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

package net;

/**
 * Interface for classes that can handle
 * network messages
 * @author Ale
 *
 */
public interface MessageHandler {

	/**
	 * Handle an incoming message
	 * @param msg The received message
	 * @param username An identifier for the source (e.g. nickname)
	 */
	public void handleMessage(String msg, String username);
	
	/**
	 * Log an event
	 * @param msg Message to log
	 */
	public void log(String msg);

}