package com.chenlei.niosocket;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by chenlei on 2017/8/21.
 */
public interface TCPProtocol {

    void handleAccept(SelectionKey key) throws IOException;

    void handleRead(SelectionKey key) throws IOException;

    void handleWrite(SelectionKey key) throws IOException;

}
