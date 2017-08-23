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

package main;

import javax.swing.JFrame;

import backend.View;

public class Main {
	
	private MainMenu mainMenu;
	private JFrame window;
	
	private ServerView serverView;
	
	public Main() {
		mainMenu = new MainMenu(this);
		window = new JFrame();
		
		mainMenu.setVisible(true);
	}
	
	public void showView(View v) {
		switch (v) {
		case CLIENT_MODE:
			break;
		case MAIN_MENU:
			mainMenu.setVisible(true);
			break;
		case QUIZ_EDITOR:
			break;
		case SERVER_MODE:
			window.setContentPane(serverView);
			window.pack();
			window.setVisible(true);
			break;
		default:
			break;
		}
	}

	public static void main(String[] args) {
		new Main();
	}

}