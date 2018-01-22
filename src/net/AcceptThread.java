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

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import main.ServerView;

/**
 * Thread class to accept new clients
 * @author Ale
 *
 */
public class AcceptThread extends Thread{
    
    public boolean running = true;
    
    private ServerSocket sock;
    private ServerView server;
    
    public AcceptThread(ServerSocket sock, ServerView server){
        this.sock = sock;
        this.server = server;
    }
    
    public void run(){
        while (running){
            try {
            		// accept new client
                Socket cSock = sock.accept();
                server.addClientHandler(new ClientHandler(cSock, server));
            } catch (SocketException e){
                if (e.getMessage().equals("Socket closed")){
                    running = false;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}