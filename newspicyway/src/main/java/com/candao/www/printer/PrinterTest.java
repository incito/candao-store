package com.candao.www.printer;

import com.candao.common.utils.Constant;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by liaoy on 2016/6/12.
 */
public class PrinterTest {
    public static void main(String[] args) throws InterruptedException {
        Printer printer = new Printer();
        printer.setIp("10.66.18.3");
        printer.setPort(9100);
        printer.setKey("10.66.18.3");

        Printer printer1 = new Printer();
        printer1.setIp("10.66.18.250");
        printer1.setPort(9100);
        printer1.setKey("10.66.18.250");

        PrinterManager.addPrinter(printer);
        PrinterManager.addPrinter(printer1);
        String msg="第1行\r\n第2行\r\n第3行\r\n第4行\r\n";
        PrinterManager.getPrinter("10.66.18.3").print(msg);
//        PrinterManager.getPrinter("10.66.18.250").print(msg);
        Thread.sleep(10000000);
    }
//public static void main(String[] args) throws IOException {
//   Socket socket = new Socket();
//    socket.connect(new InetSocketAddress("10.66.18.3", 9100), 5000);//建立连接5秒超时
//    socket.setKeepAlive(true);
//    socket.setSoTimeout(5 * 1000);      //从inputStream中读打印机状态返回值的超时时间
//    OutputStream socketOut = socket.getOutputStream();
//    OutputStreamWriter writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
//    socketOut.write(new byte[] { 29, 97, 1 });
//    socketOut.write(27);
//    socketOut.write(27);
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
