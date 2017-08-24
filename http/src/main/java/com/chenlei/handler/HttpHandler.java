package com.chenlei.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by chenlei on 2017/8/24.
 */
public class HttpHandler implements Runnable {

    private final static int bufferSize = 1024;
    private final static String charset = "UTF-8";
    private SelectionKey selectionKey;

    public HttpHandler(SelectionKey selectionKey){
        this.selectionKey = selectionKey;
    }

    public void handleAccept() throws IOException{
        SocketChannel socketChannel = ((ServerSocketChannel)selectionKey.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selectionKey.selector(),SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
    }

    public void handleRead() throws IOException{
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
        byteBuffer.clear();

        if(socketChannel.read(byteBuffer) == -1){
            socketChannel.close();
        }else{
            byteBuffer.flip();
            String requestMessage = Charset.forName(charset).newDecoder().decode(byteBuffer).toString();
            String messageLines[] = requestMessage.split("\n");
            StringBuilder messageBuilder = new StringBuilder();
            for (String messageLine : messageLines) {
                System.out.println(messageLine);
                messageBuilder.append(messageLine).append("<br>");
            }

            //only analysis 1st line
            String[] firstLine = messageLines[0].split(" ");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("      Method: " + firstLine[0]);
            System.out.println("         Url: " + firstLine[1]);
            System.out.println("Http Version: " + firstLine[2]);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            //mock return
            StringBuilder sb = new StringBuilder();
            sb.append("HTTP/1.1 200 OK\n")//＜status-line＞
              .append("Content-Type:text/html;charset=").append(charset).append("\n")//＜headers＞
              .append("\n")//＜blank line＞
              .append("<html><head>HTTP Response</head><body>").append(messageBuilder).append("</body></html>");//[＜response-body＞]

            byteBuffer = ByteBuffer.wrap(sb.toString().getBytes(charset));

            socketChannel.write(byteBuffer);
            socketChannel.close();
        }
    }

    public void run() {
        if(selectionKey.isAcceptable()){
            try {
                handleAccept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(selectionKey.isReadable()){
            try {
                handleRead();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
