package com.candao.print.v2;

import com.candao.print.entity.PrinterConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaoy on 2016/6/12.
 */
public class PrinterTest {
    public static void main(String[] args) throws InterruptedException {

        List<Object> msg = new ArrayList<>();
        msg.add(PrinterConstant.getFdDoubleFont());
        msg.add("第1行\r\n第2行\r\n第3行\r\n第4行\r\n11===\r\n");
        msg.add(PrinterConstant.getClear_font());
        msg.add("的任务二问问\r\n");
        PrinterManager.getPrinter("10.66.18.3").print(msg.toArray());
//        PrinterManager.getPrinter("10.66.18.250").print(msg);
//        Thread.sleep(10000000);
    }
//public static void main(String[] args) throws IOException, InterruptedException {
//   Socket socket = new Socket();
//    socket.connect(new InetSocketAddress("10.66.18.3", 9100), 5000);//建立连接5秒超时
//    System.out.println(1);
//    socket.setKeepAlive(true);
//    socket.setSoTimeout(5 * 1000);      //从inputStream中读打印机状态返回值的超时时间
//    OutputStream socketOut = socket.getOutputStream();
//    OutputStreamWriter writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
//    socketOut.write(new byte[] { 29, 97, 1 });
//    socketOut.write(27);
//    socketOut.write(27);
//    writer.close();
//    socketOut = socket.getOutputStream();
//    writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
//    writer.flush();//
//    writer.write("第1行\r\n第2行\r\n第3行\r\n第4行\r\n");
//    writer.write(27);
//    writer.write(100);
//    writer.write(4);
//    writer.write(10);
//    writer.flush();//
//    socketOut.write(new byte[] { 0x1B, 0x69 });// 切纸
//    socketOut.flush();
//}
}
