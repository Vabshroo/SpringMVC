package com.chenlei.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by chenlei on 2017/8/16.
 */
public class Client {

    public static void main(String args[]){

        //Client input from console
        Scanner scanner = new Scanner(System.in);

        try {

            while(true){
                //Connect to server
                Socket socket = new Socket("localhost",8080);

                PrintWriter pw = new PrintWriter(socket.getOutputStream());

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //Read line from console
                String clientLine = scanner.nextLine();

                //Write to socket
                pw.println(clientLine);
                pw.flush();

                //Receive server message
                String serverMessage = br.readLine();
                System.out.println(serverMessage);

                pw.close();
                br.close();
                socket.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
