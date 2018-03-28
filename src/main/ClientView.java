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

// GPL License

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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.border.EmptyBorder;

import backend.GameState;
import backend.PlayerFeedback;
import backend.Question;
import backend.View;
import net.MessageHandler;
import net.MsgThread;
import net.NetworkMessages;

/**
 * Window for client when joining another game
 * 
 * @author Ale
 *
 */
public class ClientView extends JFrame {

	private static final long serialVersionUID = -2058238427240422768L;

	private static final int VIEW_WIDTH = 700, VIEW_HEIGHT = 500;
	private int additionalWidth = 0;

	/**
	 * Custom drawn view for the game
	 * 
	 * @author Ale
	 *
	 */
	private class DrawingView extends JPanel implements MouseListener, MessageHandler {

		private static final int MAX_IMAGE_WIDTH = 400;

		private static final long serialVersionUID = -2592273103017659873L;

		private GameState currentState = GameState.WAITING_FOR_PLAYERS;
		private boolean isConnected = false;

		private Question currentQuestion;
		private Image image;

		private PlayerFeedback feedback;

		private JPanel loginPanel;
		private JPanel gamePanel;

		private final Rectangle backToMainButton = new Rectangle(VIEW_WIDTH / 3, VIEW_HEIGHT / 2, VIEW_WIDTH / 3, VIEW_HEIGHT / 4);
		
		private JLabel ansA, ansB, ansC, ansD;

		public DrawingView() {
			loginPanel = new JPanel();
			loginPanel.setBackground(Color.WHITE);
			loginPanel.setLayout(new GridLayout(0, 1));

			JTextField ipField = new JTextField();
			JTextField usernameField = new JTextField();
			JButton connectButton = new JButton("Connect");
			JButton exitButton = new JButton("Cancel");

			loginPanel.add(new JLabel("IP Address"));
			loginPanel.add(ipField);
			loginPanel.add(new JLabel("Username"));
			loginPanel.add(usernameField);
			loginPanel.add(connectButton);
			loginPanel.add(exitButton);

			connectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					connect(usernameField.getText(), ipField.getText(), 1616);
				}
			});
			exitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					backToMain();
				}
			});
			add(loginPanel);
			
			gamePanel = new JPanel();
			gamePanel.setBackground(Color.WHITE);
			gamePanel.setLayout(new GridLayout(2, 2, 10, 10));
			gamePanel.setPreferredSize(new Dimension(400, 300));
			
			ansA = new JLabel();
			ansA.setBackground(Color.RED);
			
			ansB = new JLabel();
			ansB.setBackground(Color.BLUE);
			
			ansC = new JLabel();
			ansC.setBackground(Color.YELLOW);
			
			ansD = new JLabel();
			ansD.setBackground(Color.ORANGE);
			
			for (JLabel lbl : new JLabel[]{ ansA, ansB, ansC, ansD }) {
				lbl.setBorder(new EmptyBorder(10, 10, 10, 10));
				lbl.setOpaque(true);
				lbl.addMouseListener(this);
				gamePanel.add(lbl);
			}
			add(gamePanel);
			gamePanel.setLocation(50, 100);
			gamePanel.setVisible(false);

			addMouseListener(this);
		}

		/**
		 * Show or hide the Swing UI for inputting server data rather
		 * than the game UI
		 * 
		 * @param show
		 *            Show server data UI rather than game UI
		 */
		private void showUI(boolean show) {
			loginPanel.setVisible(show);
			gamePanel.setVisible(!show);
		}

		/**
		 * Draw a rectangle given a Rectangle object
		 * 
		 * @param g
		 *            Graphics context in which the rectangle should be drawn
		 * @param rect
		 *            Rectangle object
		 * @param fill
		 *            Fill the rectangle?
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
			g.fillRect(0, 0, currentWidth(), VIEW_HEIGHT);
			g.setColor(Color.BLACK);
			if (!isConnected) {
				loginPanel.repaint();
				return;
			}
			switch (currentState) {
			// if game is over, offer a back button
			case GAME_OVER:
				gamePanel.setVisible(false);
				g.drawString("Back to Main", backToMainButton.x + 20, backToMainButton.y + 40);
				drawRect(g, backToMainButton, false);
				break;
			// draw answers if server is waiting for them
			case WAITING_FOR_ANSWERS:
				if (currentQuestion == null) {
					break;
				}
				gamePanel.setLocation(50, 100);
				gamePanel.setVisible(true);
				g.drawString(currentQuestion.getQ(), 10, 20);

				// draw image for question if present
				if (image != null) {
					g.drawImage(image, 700, 20, null);
				}

				break;
			case WAITING_FOR_OTHER_PLAYERS:
				gamePanel.setVisible(false);
				g.drawString("Waiting for others to respond...", 40, 80);
				break;
			// show feedback
			case WAITING_FOR_NEXT_Q:
				gamePanel.setVisible(false);
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
				gamePanel.setLocation(50, 100);
				gamePanel.setVisible(true);
				g.drawString("Waiting for game to start", 10, 20);
				break;
			default:
				break;
			}
		}

		/**
		 * Convert position to human readable ordinal number
		 * 
		 * @param pos
		 *            Position as integer
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
				if (e.getSource() instanceof JLabel) {
					if (e.getSource() == ansA && !ansA.getText().equals("")) {
						sendToServer("0");
					} else if (e.getSource() == ansB && !ansB.getText().equals("")) {
						sendToServer("1");
					} else if (e.getSource() == ansC && !ansC.getText().equals("")) {
						sendToServer("2");
					} else if (e.getSource() == ansD && !ansD.getText().equals("")) {
						sendToServer("3");
					} else {
						break;
					}
					currentState = GameState.WAITING_FOR_OTHER_PLAYERS;
				}
				break;
			default:
				break;
			}
			repaint();
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
				JOptionPane.showMessageDialog(this, reason, "Kicked from game", JOptionPane.INFORMATION_MESSAGE);
			} else if (msg.equals(NetworkMessages.userAccepted)) {
				isConnected = true;
				showUI(false);
			} else if (msg.equals(NetworkMessages.nextQ)) {
				try {
					currentQuestion = (Question) oin.readObject();
					if ((boolean) currentQuestion.getMultimediaDataForKey(Question.keyHasImage, false)) {
						byte[] imageData = (byte[]) currentQuestion.getMultimediaDataForKey(Question.keyImageData, null);
						ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
						image = ImageIO.read(bais);

						double aspectRatio = image.getWidth(null) / image.getHeight(null);
						if (image.getHeight(null) > VIEW_HEIGHT - 40 || image.getWidth(null) > MAX_IMAGE_WIDTH) {
							if ((VIEW_HEIGHT - 40) * aspectRatio < MAX_IMAGE_WIDTH) {
								image = image.getScaledInstance(-1, VIEW_HEIGHT - 40, BufferedImage.SCALE_DEFAULT);
							} else {
								image = image.getScaledInstance(MAX_IMAGE_WIDTH, -1, BufferedImage.SCALE_DEFAULT);
							}
						}
						adaptSize(image.getWidth(null));
					} else {
						image = null;
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				} catch (ClassCastException | NullPointerException e) {
					e.printStackTrace();
				}
				ArrayList<String> answers = currentQuestion.getAnswers();
				
				ansA.setText("<html>" + answers.get(0) + "</html>");
				ansB.setText("<html>" + answers.get(1) + "</html>");
				ansC.setText("<html>" + answers.get(2) + "</html>");
				ansD.setText("<html>" + answers.get(3) + "</html>");
				
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
			repaint();
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

		@Override
		public void log(String msg) {
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
		setBounds(200, 200, VIEW_WIDTH, VIEW_HEIGHT);
		this.main = main;
		drawView = new DrawingView();
		setContentPane(drawView);
	}

	/**
	 * Connect to game
	 * 
	 * @param uname
	 *            Desired nickname
	 * @param host
	 *            Host address
	 * @param port
	 *            Post on which game is hosted
	 * @return
	 */
	private boolean connect(String uname, String host, int port) {
		username = uname;
		this.host = host;
		this.port = port;

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
	 * 
	 * @param o
	 *            Relevant object
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

	private void adaptSize(int width) {
		additionalWidth = width + 40;
		setSize(ClientView.VIEW_WIDTH + additionalWidth, VIEW_HEIGHT);
	}

	private int currentWidth() {
		return VIEW_WIDTH + additionalWidth;
	}

	public void closeClient() {
		if (!drawView.isConnected) {
			return;
		}
		drawView.showUI(true);
		try {
			oout.writeObject(NetworkMessages.disconnect);
			msgThread.running = false;
			drawView.currentState = GameState.WAITING_FOR_PLAYERS;
			drawView.isConnected = false;
			sock.close();
		} catch (IOException e) {
		}
	}

}