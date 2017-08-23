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

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.JPanel;

import net.AcceptThread;
import net.MessageHandler;
import net.ClientHandler;

public class ServerView extends JPanel implements MessageHandler {

	private static final long serialVersionUID = -6532875835478176408L;
	
	// networking
	private int portNum;
	private ServerSocket serverSocket;
	private ArrayList<ClientHandler> clientArray;
	private AcceptThread acceptThread;

	public ServerView(int givenPort) {
		setBounds(150, 150, 700, 500);

		setVisible(true);

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
		acceptThread.running = false;
		for (ClientHandler ch : clientArray) {
			ch.stopRunning();
		}
		handleMessage("Closing server");
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addClientHandler(ClientHandler clientHandler) {
		clientArray.add(clientHandler);
	}

	public void broadcastToClients(String text) {
		for (ClientHandler ch : clientArray) {
			ch.send(text);
		}
	}

	@Override
	public void handleMessage(String msg) {
	}
	
}