package com.chenlei.socket;

import com.chenlei.common.util.DateUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by chenlei on 2017/8/16.
 */
public class Server {

    public static void main(String args[]){
        try {
            //New ServerSocket listen to port 8080
            ServerSocket serverSocket = new ServerSocket(8080);

            while(true){

                //Wait a request
                Socket socket = serverSocket.accept();

                //Receive message from client
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String lineFromClient = br.readLine();

                //Send message to client
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                String serverMessage = "[" + DateUtil.nowDateStr() + "] Server received message from client : " + lineFromClient;
                pw.println(serverMessage);
                System.out.println(serverMessage);
                pw.flush();

                //Close resource
                pw.close();
                br.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
