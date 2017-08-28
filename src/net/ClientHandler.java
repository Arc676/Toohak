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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import main.ServerView;

public class ClientHandler {
	public ObjectOutputStream oout;
	public BufferedReader in;
	public MsgThread msgThread;
	public String username;

	public ClientHandler(Socket clientSocket, ServerView server) {
		try {
			oout = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			username = in.readLine();
			msgThread = new MsgThread(in, server, username, server);
			msgThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(Object text) {
		try {
			oout.reset();
			oout.writeObject(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopRunning() {
		msgThread.running = false;
	}
}