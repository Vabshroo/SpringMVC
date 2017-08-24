package com.chenlei.server;

import com.chenlei.handler.HttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * Created by chenlei on 2017/8/24.
 */
public class HttpServer {
    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            if(selector.select(3000) == 0){
                continue;
            }

            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while(keyIterator.hasNext()){
                SelectionKey selectionKey = keyIterator.next();
                new Thread(new HttpHandler(selectionKey)).run();
                keyIterator.remove();
            }
        }
    }
}
