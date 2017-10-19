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

package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import main.ServerView;

public class ClientHandler {
	public ObjectOutputStream oout;
	public ObjectInputStream oin;
	public MsgThread msgThread;
	public String username;
	
	private String ipAddress;

	public ClientHandler(Socket clientSocket, ServerView server) {
		try {
			ipAddress = clientSocket.getInetAddress().toString();
			oout = new ObjectOutputStream(clientSocket.getOutputStream());
			oin = new ObjectInputStream(clientSocket.getInputStream());
			username = (String) oin.readObject();
			msgThread = new MsgThread(oin, username, server);
			msgThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getIP() {
		return ipAddress;
	}

	public void send(Object obj) {
		try {
			oout.reset();
			oout.writeObject(obj);
			oout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopRunning() {
		msgThread.running = false;
	}
}