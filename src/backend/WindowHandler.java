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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import main.ClientView;
import main.ServerView;

/**
 * Generic listener class that calls cleanup methods when
 * windows are closed.
 * @author Ale
 *
 */
public class WindowHandler implements WindowListener {

	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	public void windowClosing(WindowEvent e) {
		if (e.getWindow() instanceof ServerView) {
			((ServerView) e.getWindow()).closeServer();
		} else if (e.getWindow() instanceof ClientView) {
			((ClientView) e.getWindow()).closeClient();
		}
	}

}