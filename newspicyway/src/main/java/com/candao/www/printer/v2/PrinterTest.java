package com.candao.www.printer.v2;

import com.candao.print.entity.PrinterConstant;
import com.candao.www.constant.Constant;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

/**
 * Created by liaoy on 2016/6/12.
 */
public class PrinterTest {
//    public static void main(String[] args) throws InterruptedException {

    //        List<Object> msg = new ArrayList<>();
//        msg.add(PrinterConstant.getFdDoubleFont());
//        msg.add("第1行\r\n第2行\r\n第3行\r\n第4行\r\n11===\r\n");
//        msg.add(PrinterConstant.getClear_font());
//        msg.add("的任务二问问\r\n");
//        PrinterManager.getPrinter("10.66.18.3").print(msg.toArray(),null);
//        PrinterManager.getPrinter("10.66.18.250").print(msg);
//        Thread.sleep(10000000);
//    }
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("10.66.18.3", 9100), 5000);//建立连接5秒超时
        socket.setKeepAlive(true);
        socket.setSoTimeout(5 * 1000);      //从inputStream中读打印机状态返回值的超时时间
        OutputStream socketOut = socket.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
        socketOut.write(new byte[]{29, 97, 1});
        socketOut.write(27);
        socketOut.write(27);
        socketOut = socket.getOutputStream();
        writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
        writer.flush();//
//    writer.write("第1行\r\n第2行\r\n第3行\r\n第4行\r\n");
        writer.write(27);

//        Image img = Toolkit.getDefaultToolkit().getImage("D:/结账单.bmp");
        PrintOne(socketOut);
        socketOut.write(PrinterConstant.getLineN((byte) 4));
        socketOut.write(new byte[]{10});
        socketOut.flush();
        socketOut.write(PrinterConstant.CUT);
        socketOut.flush();
    }

    /// 打印图片方法
    /// </summary>
    public static void PrintOne(OutputStream out) throws IOException {
        //获取图片
        BufferedImage bmp = ImageIO.read(new File("D:\\doc\\餐道\\PrintCtrlSDK V1.11\\Beta_PrintCtrlSDK V1.11\\PrintCtrlDemo\\Bin\\Bmp\\miss you.bmp"));
//        BufferedImage bmp = ImageIO.read(new File("D:/结账单.bmp"));
        bmp = zoomInImage(bmp, 2);
        out.write(new byte[]{0x1B, 0x33, 0x05});
        byte[] data = new byte[3];
        data[0] = (byte) 0;
        data[1] = (byte) 0;
        data[2] = (byte) 0;

        Color pixelColor;


        //ESC * m nL nH d1…dk   选择位图模式
        // ESC * m nL nH
        byte[] escBmp = new byte[]{0x1B, 0x2A, 0x00, 0x00, 0x00};

        escBmp[2] = (byte) 0x21;

        //nL, nH
        escBmp[3] = (byte) (bmp.getWidth() % 256);
        escBmp[4] = (byte) (bmp.getWidth() / 256);

        //循环图片像素打印图片
        //循环高
        for (int i = 0; i < (bmp.getHeight() / 24 + 1); i++) {
            //设置模式为位图模式
            out.write(escBmp);
            //循环宽
            for (int j = 0; j < bmp.getWidth(); j++) {
                for (int k = 0; k < 24; k++) {
                    if (((i * 24) + k) < bmp.getHeight())  // if within the BMP size
                    {
//                        pixelColor = bmp.(j, (i * 24) + k);
                        pixelColor = new Color(bmp.getRGB(j, (i * 24) + k));
                        if (pixelColor.getRed() == 0) {
                            data[k / 8] += (byte) (128 >> (k % 8));
                        }
                    }
                }
                //一次写入一个data，24个像素
                out.write(data);

                data[0] = (byte) 0;
                data[1] = (byte) 0;
                data[2] = (byte) 0;    // Clear to Zero.
            }

            //换行，打印第二行
            byte[] data2 = {0xA};
            out.write(data2);
        } // data
//        lc.Write("\n\n");
    }

    public static BufferedImage zoomInImage(BufferedImage originalImage, Integer times) {
        int width = originalImage.getWidth() * times;
        int height = originalImage.getHeight() * times;
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }
}
