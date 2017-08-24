package com.chenlei.niosocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by chenlei on 2017/8/21.
 */
public class EchoSelectorProtocol implements TCPProtocol {

    private int bufSize; // 缓冲区的长度
    public EchoSelectorProtocol(int bufSize){
        this.bufSize = bufSize;
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        System.out.println("Handle Accept");
        SocketChannel socketChannel = ((ServerSocketChannel)key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));

    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        SocketChannel clntChan = (SocketChannel) key.channel();
        //获取该信道所关联的附件，这里为缓冲区
        ByteBuffer buf = (ByteBuffer) key.attachment();
        buf.clear();
        long bytesRead = clntChan.read(buf);
        //如果read（）方法返回-1，说明客户端关闭了连接，那么客户端已经接收到了与自己发送字节数相等的数据，可以安全地关闭
        if (bytesRead == -1){
            clntChan.close();
        }else if(bytesRead > 0){
            //如果缓冲区总读入了数据，则将该信道感兴趣的操作设置为为可读可写
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
        System.out.println("Handle Read : " + Charset.forName("UTF-8").newDecoder().decode(buf).toString());

    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        // TODO Auto-generated method stub
        ByteBuffer buffer=(ByteBuffer) key.attachment();
        buffer.flip();
        SocketChannel clntChan = (SocketChannel) key.channel();
        String message = Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
        System.out.println("Handle Write : " + message);
        //将数据写入到信道中
        ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes("UTF-8"));
        while (byteBuffer.hasRemaining()){
            byteBuffer.flip();
            clntChan.write(byteBuffer);
        }
        //如果缓冲区中的数据已经全部写入了信道，则将该信道感兴趣的操作设置为可读
        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        //为读入更多的数据腾出空间
        //buffer.compact();

    }

}