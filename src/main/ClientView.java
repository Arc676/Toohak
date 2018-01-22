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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import backend.GameState;
import backend.PlayerFeedback;
import backend.Question;
import backend.Updatable;
import backend.Updater;
import backend.View;
import net.MessageHandler;
import net.MsgThread;
import net.NetworkMessages;

/**
 * Window for client when joining another game
 * @author Ale
 *
 */
public class ClientView extends JFrame {

	private static final long serialVersionUID = -2058238427240422768L;

	private static final int width = 700, height = 500;

	/**
	 * Custom drawn view for the game
	 * @author Ale
	 *
	 */
	private class DrawingView extends JPanel implements MouseListener, MessageHandler, Updatable {

		private static final long serialVersionUID = -2592273103017659873L;

		private GameState currentState = GameState.WAITING_FOR_PLAYERS;
		private Thread gameThread;
		private boolean running;
		private boolean isConnected = false;

		private Question currentQuestion;
		private String answerA, answerB, answerC, answerD;
		private BufferedImage image;

		private PlayerFeedback feedback;

		private JPanel panel;

		private final Rectangle backToMainButton = new Rectangle(width / 3, height / 2, width / 3, height / 4);

		private static final int buttonHeight = 100, buttonMargin = 50, buttonWidth = width / 2 - buttonMargin * 2;

		private final Rectangle ansA = new Rectangle(buttonMargin, height - 2 * (buttonHeight + buttonMargin),
				buttonWidth, buttonHeight);
		private final Rectangle ansB = new Rectangle(buttonWidth + 2 * buttonMargin,
				height - 2 * (buttonHeight + buttonMargin), buttonWidth, buttonHeight);
		private final Rectangle ansC = new Rectangle(buttonMargin, height - (buttonHeight + buttonMargin), buttonWidth,
				buttonHeight);
		private final Rectangle ansD = new Rectangle(buttonWidth + 2 * buttonMargin,
				height - (buttonHeight + buttonMargin), buttonWidth, buttonHeight);

		public DrawingView() {
			panel = new JPanel();
			panel.setBackground(Color.WHITE);
			panel.setLayout(new GridLayout(0, 1));

			JTextField ipField = new JTextField();
			JTextField usernameField = new JTextField();
			JButton connectButton = new JButton("Connect");
			JButton exitButton = new JButton("Cancel");

			panel.add(new JLabel("IP Address"));
			panel.add(ipField);
			panel.add(new JLabel("Username"));
			panel.add(usernameField);
			panel.add(connectButton);
			panel.add(exitButton);

			connectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					connect(usernameField.getText(), ipField.getText(), 1616);
				}
			});
			exitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					running = false;
					backToMain();
				}
			});
			add(panel);

			addMouseListener(this);
		}

		/**
		 * Show or hide the Swing UI for inputting server data
		 * @param show Swing UI visibility
		 */
		private void showUI(boolean show) {
			if (show) {
				add(panel);
			} else {
				remove(panel);
			}
		}

		/**
		 * Draw a rectangle given a Rectangle object
		 * @param g Graphics context in which the rectangle should be drawn
		 * @param rect Rectangle object
		 * @param fill Fill the rectangle?
		 */
		private void drawRect(Graphics g, Rectangle rect, boolean fill) {
			if (fill) {
				g.fillRect(rect.x, rect.y, rect.width, rect.height);
			} else {
				g.drawRect(rect.x, rect.y, rect.width, rect.height);
			}
		}

		public void paintComponent(Graphics g) {
			// clear background
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);
			g.setColor(Color.BLACK);
			if (!isConnected) {
				panel.repaint();
				return;
			}
			switch (currentState) {
			// if game is over, offer a back button
			case GAME_OVER:
				g.drawString("Back to Main", backToMainButton.x + 20, backToMainButton.y + 40);
				drawRect(g, backToMainButton, false);
				break;
			// draw answers if server is waiting for them
			case WAITING_FOR_ANSWERS:
				if (currentQuestion == null) {
					break;
				}
				g.drawString(currentQuestion.getQ(), 10, 20);

				// draw image for question if present
				if (image != null) {
					g.drawImage(image.getScaledInstance(-1, 150, BufferedImage.SCALE_DEFAULT), 10, 40, null);
				}

				// draw each answer that exists
				if (!answerA.equals("")) {
					g.setColor(Color.RED);
					drawRect(g, ansA, true);
					g.setColor(Color.WHITE);
					g.drawString(answerA, ansA.x + 10, ansA.y + 40);
				}

				if (!answerB.equals("")) {
					g.setColor(Color.BLUE);
					drawRect(g, ansB, true);
					g.setColor(Color.WHITE);
					g.drawString(answerB, ansB.x + 10, ansB.y + 40);
				}

				if (!answerC.equals("")) {
					g.setColor(Color.GREEN);
					drawRect(g, ansC, true);
					g.setColor(Color.WHITE);
					g.drawString(answerC, ansC.x + 10, ansC.y + 40);
				}

				if (!answerD.equals("")) {
					g.setColor(Color.ORANGE);
					drawRect(g, ansD, true);
					g.setColor(Color.WHITE);
					g.drawString(answerD, ansD.x + 10, ansD.y + 40);
				}

				break;
			case WAITING_FOR_OTHER_PLAYERS:
				g.drawString("Waiting for others to respond...", 40, 80);
				break;
			// show feedback
			case WAITING_FOR_NEXT_Q:
				if (feedback.answerWasCorrect()) {
					g.drawString("Correct!", 40, 80);
				} else {
					g.drawString("Incorrect", 40, 80);
				}
				String player = feedback.getPrecedingPlayer();
				if (player == null) {
					g.drawString("You're in first place!", 40, 110);
				} else {
					g.drawString("You're in " + posToString(feedback.getPosition()) + " place, "
							+ feedback.getScoreDelta() + " points behind " + feedback.getPrecedingPlayer(), 40, 110);
				}
				Question q = feedback.getQuestion();
				g.drawString("Acceptable answers:", 40, 130);
				int y = 150;
				int i = 0;
				for (String answer : q.getAnswers()) {
					if (q.acceptAnswer(i)) {
						g.drawString(answer, 60, y);
						y += 30;
					}
					i++;
				}
				break;
			case WAITING_FOR_PLAYERS:
				g.drawString("Waiting for game to start", 10, 20);
				break;
			default:
				break;
			}
		}

		/**
		 * Convert position to human readable ordinal number
		 * @param pos Position as integer
		 * @return Ordinal number as string
		 */
		private String posToString(int pos) {
			switch (pos) {
			case 1:
				return "first";
			case 2:
				return "second";
			case 3:
				return "third";
			default:
				return pos + "th";
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			switch (currentState) {
			case GAME_OVER:
				if (backToMainButton.contains(e.getPoint())) {
					closeClient();
					backToMain();
				}
				break;
			case WAITING_FOR_ANSWERS:
				if (!answerA.equals("") && ansA.contains(e.getPoint())) {
					sendToServer("0");
				} else if (!answerB.equals("") && ansB.contains(e.getPoint())) {
					sendToServer("1");
				} else if (!answerC.equals("") && ansC.contains(e.getPoint())) {
					sendToServer("2");
				} else if (!answerD.equals("") && ansD.contains(e.getPoint())) {
					sendToServer("3");
				} else {
					break;
				}
				currentState = GameState.WAITING_FOR_OTHER_PLAYERS;
				break;
			default:
				break;
			}
		}

		private void startRunning() {
			running = true;
			gameThread = new Thread(new Updater(this, 0.1));
			gameThread.start();
		}

		public void update() {
			repaint();
		}

		public boolean stillRunning() {
			return running;
		}

		@Override
		public void handleMessage(String msg, String src) {
			if (msg.equals(NetworkMessages.userKicked)) {
				String reason = "(Unknown reason)";
				try {
					reason = (String) oin.readObject();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				isConnected = false;
				closeClient();
				showUI(true);
				repaint();
				JOptionPane.showMessageDialog(this, reason, "Kicked from game", JOptionPane.INFORMATION_MESSAGE);
			} else if (msg.equals(NetworkMessages.userAccepted)) {
				isConnected = true;
				showUI(false);
			} else if (msg.equals(NetworkMessages.nextQ)) {
				try {
					currentQuestion = (Question) oin.readObject();
					if (currentQuestion.questionHasImage()) {
						ByteArrayInputStream bais = new ByteArrayInputStream(currentQuestion.getImageBytes());
						image = ImageIO.read(bais);
					} else {
						image = null;
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				ArrayList<String> answers = currentQuestion.getAnswers();
				answerA = answers.get(0);
				answerB = answers.get(1);
				answerC = answers.get(2);
				answerD = answers.get(3);
				currentState = GameState.WAITING_FOR_ANSWERS;
			} else if (msg.equals(NetworkMessages.timeup)) {
				try {
					feedback = (PlayerFeedback) oin.readObject();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				currentState = GameState.WAITING_FOR_NEXT_Q;
			} else if (msg.equals(NetworkMessages.gameOver)) {
				currentState = GameState.GAME_OVER;
			}
		}

		// unnecessary mouse methods
		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

	}

	// network IO
	private Socket sock;
	private ObjectOutputStream oout;
	private ObjectInputStream oin;
	private int port;
	private String host;
	private MsgThread msgThread;

	private String username;

	private Main main;

	private DrawingView drawView;

	public ClientView(Main main) {
		setBounds(200, 200, width, height);
		this.main = main;
		drawView = new DrawingView();
		setContentPane(drawView);
	}

	public void startRunning() {
		drawView.startRunning();
	}

	/**
	 * Connect to game
	 * @param uname Desired nickname
	 * @param host Host address
	 * @param port Post on which game is hosted
	 * @return
	 */
	private boolean connect(String uname, String host, int port) {
		username = uname;
		this.host = host;
		this.port = port;
		startRunning();

		try {
			sock = new Socket(this.host, this.port);
			oout = new ObjectOutputStream(sock.getOutputStream());
			oout.writeObject(username);
			oin = new ObjectInputStream(sock.getInputStream());
			msgThread = new MsgThread(oin, username, drawView);
			msgThread.start();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private void backToMain() {
		setVisible(false);
		main.showView(View.MAIN_MENU);
	}

	/**
	 * Send object to server
	 * @param o Relevant object
	 */
	private void sendToServer(Object o) {
		try {
			oout.reset();
			oout.writeObject(o);
			oout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeClient() {
		if (!drawView.isConnected) {
			return;
		}
		try {
			oout.writeObject(NetworkMessages.disconnect);
			msgThread.running = false;
			drawView.running = false;
			drawView.currentState = GameState.WAITING_FOR_PLAYERS;
			drawView.isConnected = false;
			sock.close();
		} catch (IOException e) {
		}
	}

}