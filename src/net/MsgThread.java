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
import java.net.SocketException;

import server.Server;

public class MsgThread extends Thread {

    public boolean running = true;

    private BufferedReader in;
    private Server server;
    private String username;
    private Chat receiver;

    public MsgThread(BufferedReader in, Server server, String username, Chat receiver){
        this.in = in;
        this.server = server;
        this.username = username;
        this.receiver = receiver;
    }

    public void run(){
        String inLine = "";
        while (running){
            try {
                inLine = in.readLine();
                if (inLine == null || inLine.substring(inLine.indexOf(":") + 2).equals("/disconnect")){
                    print(username + " disconnected");
                    running = false;
                } else {
                    print(inLine);
                }
            } catch(SocketException e){
                if(e.getMessage().equals("Socket closed")){
                    receiver.printMessage("Socket closed");
                    running = false;
                }
            } catch (IOException e){
                e.printStackTrace();  
            }
        }
    }
    
    private void print(String text){
        if (server != null){
            server.broadcastToClients(text, username);
        } else {
            receiver.printMessage(text);
        }
    }
}