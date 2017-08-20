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

package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.Chat;
import net.MsgThread;

//port number: 4267

public class Client extends JFrame implements Chat, ActionListener {

	private static final long serialVersionUID = 1L;

	// network IO
	private Socket sock;
	private PrintWriter out;
	private BufferedReader in;
	private int port;
	private String host;

	private String username;

	private MsgThread msgThread;

	// UI
	private JTextField msgField;
	private JTextArea transcript;
	private JButton btnSend;
	private JButton btnDisconnect;

	public Client(String uname, String host, int port) {
		username = uname;
		this.host = host;
		this.port = port;

		setBounds(200, 200, 450, 300);

		getContentPane().setLayout(null);

		transcript = new JTextArea();
		transcript.setEditable(false);
		transcript.setBounds(0, 0, 450, 223);
		getContentPane().add(transcript);

		msgField = new JTextField();
		msgField.setBounds(10, 235, 253, 28);
		getContentPane().add(msgField);
		msgField.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.addActionListener(this);
		btnSend.setBounds(266, 235, 70, 29);
		getContentPane().add(btnSend);

		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(this);
		btnDisconnect.setBounds(333, 235, 117, 29);
		getContentPane().add(btnDisconnect);

		setVisible(true);

		try {
			sock = new Socket(this.host, this.port);
			out = new PrintWriter(sock.getOutputStream(), true);
			out.println(username);
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			msgThread = new MsgThread(in, null, username, this);
			msgThread.start();
		} catch (IOException e) {
			printMessage("IOException occurred.");
			return;
		}
	}

	private void closeClient() {
		try {
			msgThread.running = false;
			sock.close();
			printMessage("Disconnecting...");
		} catch (IOException e) {
			printMessage("An error occurred while disconnecting");
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
				String msg = username + ": " + msgField.getText();
				printMessage(username + ": " + msgField.getText());
				out.println(msg);
				msgField.setText("");
			}
		} else if (e.getSource() == btnDisconnect) {
			closeClient();
		}
	}

}