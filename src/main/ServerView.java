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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
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
import backend.Question;
import backend.Quiz;
import backend.Updatable;
import backend.Updater;
import net.AcceptThread;
import net.ClientHandler;
import net.MessageHandler;
import net.NetworkMessages;

public class ServerView extends JFrame implements MessageHandler, ActionListener, Updatable {

	private static final long serialVersionUID = -6532875835478176408L;

	// networking
	private int portNum;
	private ServerSocket serverSocket;

	private ArrayList<ClientHandler> clientArray;
	private boolean wasCorrect[];
	private int answersReceived = 0;

	private AcceptThread acceptThread;
	private Thread gameThread;
	public boolean isRunning = true;
	private GameState currentState = GameState.WAITING_FOR_PLAYERS;

	private LeaderboardModel leaderboardModel;
	private JTable leaderboard;

	private Quiz quiz;
	private Question currentQuestion;
	private int timeRemaining;

	private JLabel lblQuizName;

	private JLabel lblCurrentQ;

	private JLabel lblA;
	private JLabel lblB;
	private JLabel lblC;
	private JLabel lblD;
	private JLabel[] answers;

	private JLabel lblTime;
	private JButton btnNext;

	private JLabel lblEvent;
	private JPanel southPanel;

	private JButton btnKickUser;
	private JButton btnExit;

	public ServerView() {
		setTitle("Toohak: Hosting Game");
		setBounds(200, 200, 700, 500);

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

		answers = new JLabel[] { lblA, lblB, lblC, lblD };

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		leaderboardModel = new LeaderboardModel();
		leaderboard = new JTable(leaderboardModel);
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
			public void actionPerformed(ActionEvent e) {
				kickSelectedUser();
			}
		});

		btnExit = new JButton("Cancel");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeServer();
			}
		});
	}

	public void startServer(int givenPort, Quiz givenQuiz) {
		quiz = givenQuiz;
		lblQuizName.setText(quiz.quizName);
		try {
			lblCurrentQ.setText("IP: " + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			lblCurrentQ.setText("Failed to get IP address");
		}

		lblA.setText("A");
		lblB.setText("B");
		lblC.setText("C");
		lblD.setText("D");

		btnNext.setText("Begin!");

		southPanel.add(btnKickUser);
		southPanel.add(btnExit);

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
			ch.send(NetworkMessages.userKicked);
			ch.send("Server shutting down");
			ch.stopRunning();
		}
		leaderboardModel.clear();
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		backToMain();
	}

	private void backToMain() {
		isRunning = false;
		currentState = GameState.WAITING_FOR_PLAYERS;
		setVisible(false);
	}

	private void kickSelectedUser() {
		int selectedUser = leaderboard.getSelectedRow();
		if (selectedUser >= 0) {
			String username = (String) leaderboardModel.getValueAt(selectedUser, 0);
			leaderboardModel.removePlayer(selectedUser);
			int i = 0;
			for (; i < clientArray.size(); i++) {
				if (clientArray.get(i).username.equals(username)) {
					break;
				}
			}
			ClientHandler ch = clientArray.get(i);
			ch.send(NetworkMessages.userKicked);
			ch.send("Kicked by server owner");
			ch.stopRunning();
			clientArray.remove(i);
		}
	}

	public void addClientHandler(ClientHandler clientHandler) {
		leaderboardModel.addPlayer(clientHandler.username, 0);
		clientArray.add(clientHandler);
		sendToClient(clientHandler.username, NetworkMessages.userAccepted);
	}

	private void broadcastToClients(Object obj) {
		for (ClientHandler ch : clientArray) {
			ch.send(obj);
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
	public void handleMessage(String msg, String username) {
		if (currentState == GameState.WAITING_FOR_ANSWERS) {
			try {
				int chosen = Integer.parseInt(msg);
				if (currentQuestion.acceptAnswer(chosen)) {
					int index = 0;
					for (ClientHandler ch : clientArray) {
						if (ch.username.equals(username)) {
							break;
						}
						index++;
					}
					wasCorrect[index] = true;
					leaderboardModel.changeScore(username, currentQuestion.getPoints());
				}
			} catch (NumberFormatException e) {
			}
			answersReceived++;
			if (answersReceived >= wasCorrect.length) {
				answersReceived = 0;
				timeRemaining = 0;
			}
		}
	}

	private void startGame() {
		acceptThread.running = false;
		gameThread = new Thread(new Updater(this, 1));
		gameThread.start();

		southPanel.remove(btnKickUser);
		southPanel.remove(btnExit);

		leaderboardModel.initializeDeltas();
		broadcastToClients(NetworkMessages.startGame);
		broadcastToClients(quiz);
		wasCorrect = new boolean[clientArray.size()];
		getNextQuestion();
	}

	public void update() {
		if (currentState == GameState.WAITING_FOR_ANSWERS) {
			timeRemaining--;
			if (timeRemaining <= 0) {
				actionPerformed(null);
			}
			lblTime.setText(Integer.toString(timeRemaining));
		}
	}

	public boolean stillRunning() {
		return isRunning;
	}

	private boolean getNextQuestion() {
		currentQuestion = quiz.nextQuestion();
		for (int i = 0; i < wasCorrect.length; i++) {
			wasCorrect[i] = false;
		}
		broadcastToClients(NetworkMessages.nextQ);
		if (currentQuestion == null) {
			currentState = GameState.GAME_OVER;
			return false;
		}
		lblCurrentQ.setText(currentQuestion.getQ());
		int index = 0;
		for (String ans : currentQuestion.getAnswers()) {
			answers[index].setText(ans);
			index++;
		}
		timeRemaining = currentQuestion.getTimeLimit();
		lblTime.setText(Integer.toString(timeRemaining));
		currentState = GameState.WAITING_FOR_ANSWERS;
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (currentState) {
		case WAITING_FOR_ANSWERS:
			currentState = GameState.WAITING_FOR_NEXT_Q;
			broadcastToClients(NetworkMessages.timeup);
			int i = 0;
			for (ClientHandler ch : clientArray) {
				if (wasCorrect[i]) {
					ch.send(NetworkMessages.wasCorrect);
				} else {
					ch.send(NetworkMessages.wasIncorrect);
				}
				i++;
			}
			btnNext.setText("Next");
			leaderboardModel.updateData();
			break;
		case WAITING_FOR_PLAYERS:
			startGame();
			currentState = GameState.WAITING_FOR_ANSWERS;
			btnNext.setText("Skip");
			break;
		case GAME_OVER:
			closeServer();
			break;
		case WAITING_FOR_NEXT_Q:
			if (getNextQuestion()) {
				btnNext.setText("Skip");
			} else {
				btnNext.setText("Exit");
			}
			break;
		default:
			break;
		}
	}

}