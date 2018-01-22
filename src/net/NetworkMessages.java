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
 * Contains set messages that can be sent over
 * the network for server/client communication
 * @author Ale
 *
 */
public abstract class NetworkMessages {
	
	public static final String userKicked = "kicked";
	public static final String userAccepted = "accepted";
	
	public static final String disconnect = "disconnect";
	
	public static final String timeup = "timeup";
	public static final String nextQ = "nextquestion";
	public static final String gameOver = "gameOver";
	
}
