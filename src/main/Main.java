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

import backend.View;
import backend.WindowHandler;

/**
 * Main class that delegates control to and activates
 * other views
 * @author Ale
 *
 */
public class Main {
	
	private WindowHandler wh;
	
	private MainMenu mainMenu;
	
	private ServerView serverView;
	private ClientView clientView;
	private QuizEditor quizEditor;
	private AboutWindow aboutWindow;
	
	public Main() {
		wh = new WindowHandler();
		
		mainMenu = new MainMenu(this);
		aboutWindow = new AboutWindow();
		
		serverView = new ServerView();
		serverView.addWindowListener(wh);
		
		clientView = new ClientView(this);
		
		quizEditor = new QuizEditor();
		
		mainMenu.setVisible(true);
		
		// ensure that server and client are closed when the
		// program exits
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				serverView.closeServer();
				clientView.closeClient();
			}
		});
	}
	
	/**
	 * Show a desired view
	 * @param v The desired view type
	 */
	public void showView(View v) {
		switch (v) {
		case CLIENT_MODE:
			clientView.setVisible(true);
			break;
		case MAIN_MENU:
			mainMenu.setVisible(true);
			break;
		case QUIZ_EDITOR:
			quizEditor.setVisible(true);
			break;
		case SERVER_MODE:
			serverView.startServer(1616, mainMenu.getQuiz());
			serverView.setVisible(true);
			break;
		case ABOUT_WINDOW:
			aboutWindow.setVisible(true);
			break;
		default:
			break;
		}
	}
	
	public boolean serverIsRunning() {
		return serverView.stillRunning();
	}

	public static void main(String[] args) {
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s %4$s: %5$s%n");
		new Main();
	}

}