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

package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.AcceptThread;
import net.Chat;
import net.ClientHandler;

public class Server extends JFrame implements Chat, ActionListener {

	private static final long serialVersionUID = 1L;

	// networking
	private int portNum;
	private ServerSocket serverSocket;
	private ArrayList<ClientHandler> clientArray;
	private AcceptThread acceptThread;

	// UI
	private JTextField msgField;
	private JTextArea transcript;
	private JButton btnSend;
	private JButton btnClose;

	public Server(int givenPort) {
		setBounds(150, 150, 450, 300);

		getContentPane().setLayout(null);

		transcript = new JTextArea();
		transcript.setEditable(false);
		transcript.setBounds(0, 0, 450, 214);
		getContentPane().add(transcript);

		msgField = new JTextField();
		msgField.setBounds(10, 232, 288, 28);
		getContentPane().add(msgField);
		msgField.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.addActionListener(this);
		btnSend.setBounds(295, 233, 75, 29);
		getContentPane().add(btnSend);

		btnClose = new JButton("Close");
		btnClose.addActionListener(this);
		btnClose.setBounds(365, 233, 79, 29);
		getContentPane().add(btnClose);

		setVisible(true);

		clientArray = new ArrayList<ClientHandler>();
		portNum = givenPort;
		// Listens for socket
		printMessage("Hosting chat server on port " + portNum);
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
		acceptThread.running = false;
		for (ClientHandler ch : clientArray) {
			ch.stopRunning();
		}
		printMessage("Closing server");
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addClientHandler(ClientHandler clientHandler) {
		clientArray.add(clientHandler);
	}

	public void broadcastToClients(String text, String source) {
		for (ClientHandler ch : clientArray) {
			if (!ch.username.equals(source)) {
				ch.send(text);
			}
		}

		if (!source.equals("server")) {
			printMessage(text);
		}
	}

	@Override
	public void printMessage(String msg) {
		transcript.append(msg + "\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSend) {
			if (msgField.getText().length() > 0) {
				String msg = "Server: " + msgField.getText();
				printMessage(msg);
				broadcastToClients(msg, "server");
				msgField.setText("");
			}
		} else if (e.getSource() == btnClose) {
			closeServer();
		}
	}
}