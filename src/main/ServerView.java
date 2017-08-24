// MIT License

// Copyright (c) 2017 Matthew Chen, Arc676/Alessandro Vinciguerra

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import backend.GameState;
import backend.LeaderboardModel;
import backend.Quiz;
import backend.View;
import net.AcceptThread;
import net.ClientHandler;
import net.MessageHandler;

public class ServerView extends JFrame implements MessageHandler, ActionListener {

	private static final long serialVersionUID = -6532875835478176408L;

	// networking
	private int portNum;
	private ServerSocket serverSocket;
	private ArrayList<ClientHandler> clientArray;
	private AcceptThread acceptThread;
	
	private Main main;
	
	private Thread gameThread;
	private boolean isRunning = true;
	private GameState currentState = GameState.WAITING_FOR_PLAYERS;
	
	private LeaderboardModel tableModel;
	private JTable leaderboard;
	
	private Quiz quiz;
	private JLabel lblQuizName;
	
	private JLabel lblCurrentQ;
	
	private JLabel lblA;
	private JLabel lblB;
	private JLabel lblC;
	private JLabel lblD;
	
	private JLabel lblTime;
	private JButton btnNext;
	
	private JLabel lblEvent;
	private JButton btnKickUser;
	private JPanel southPanel;
	
	public ServerView(Main main) {
		setTitle("Toohak: Hosting Game");
		this.main = main;
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		lblQuizName = new JLabel("New label");
		lblQuizName.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblQuizName);
		
		lblCurrentQ = new JLabel("No Question Yet");
		lblCurrentQ.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
		lblCurrentQ.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblCurrentQ);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new GridLayout(2, 2, 0, 0));
		
		lblA = new JLabel("A");
		lblA.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblA.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblA);
		
		lblB = new JLabel("B");
		lblB.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblB.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblB);
		
		lblC = new JLabel("C");
		lblC.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblC.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblC);
		
		lblD = new JLabel("D");
		lblD.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblD.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblD);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		leaderboard = new JTable();
		leaderboard.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		scrollPane.setViewportView(leaderboard);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		lblTime = new JLabel("60");
		lblTime.setFont(new Font("Lucida Grande", Font.PLAIN, 70));
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblTime);
		
		btnNext = new JButton("Begin!");
		panel.add(btnNext);
		btnNext.addActionListener(this);
		
		southPanel = new JPanel();
		getContentPane().add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		lblEvent = new JLabel("Nothing eventful yet");
		lblEvent.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
		southPanel.add(lblEvent);
		
		btnKickUser = new JButton("Kick User");
		btnKickUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickSelectedUser();
			}
		});
	}

	public void startServer(int givenPort, Quiz givenQuiz) {
		quiz = givenQuiz;
		
		southPanel.add(btnKickUser);
		
		clientArray = new ArrayList<ClientHandler>();
		portNum = givenPort;
		// Listens for socket
		try {
			serverSocket = new ServerSocket(portNum);
		} catch (IOException e1) {
			System.err.println("Failed to create socket");
			return;
		}
		acceptThread = new AcceptThread(serverSocket, this);
		acceptThread.start();
	}

	private void closeServer() {
		for (ClientHandler ch : clientArray) {
			ch.stopRunning();
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void kickSelectedUser() {
	}

	public void addClientHandler(ClientHandler clientHandler) {
		clientArray.add(clientHandler);
	}

	private void broadcastToClients(String text) {
		for (ClientHandler ch : clientArray) {
			ch.send(text);
		}
	}
	
	private void sendToClient(String username, String text) {
		for (ClientHandler ch : clientArray) {
			if (ch.username.equals(username)) {
				ch.send(text);
				return;
			}
		}
	}

	@Override
	public void handleMessage(String msg) {}
	
	private void startGame() {
		acceptThread.running = false;
		gameThread = new Thread(new Runnable(){
			@Override
			public void run() {
				long lastUpdate = System.nanoTime();
				while (isRunning) {
					long now = System.nanoTime();
					if (now - lastUpdate >= 1.0/60*1000000000) {
						update();
						lastUpdate = now;
					}
				}
			}
		});
		gameThread.start();
		southPanel.remove(btnKickUser);
	}

	private void update() {
		//
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (currentState) {
		case WAITING_FOR_ANSWERS:
			break;
		case WAITING_FOR_PLAYERS:
			startGame();
			break;
		case GAME_OVER:
			closeServer();
			main.showView(View.MAIN_MENU);
			break;
		case WAITING_FOR_NEXT_Q:
			break;
		default:
			break;
		}
	}

}