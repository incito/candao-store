package kaiying;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestSocket {

	public static void main(String [] args) throws UnknownHostException, IOException, InterruptedException{
//		Socket socket = new Socket();
//		socket.connect(endpoint, timeout);
//		return ;
		
		
		Socket socket = new Socket("192.168.40.138", 9100);
		for (int i = 0; i < 20; i++) {
//			System.out.println(ProtoCommon.activeTest(socket));
//			Thread.currentThread().sleep(2000);
		}
		
	}
}
