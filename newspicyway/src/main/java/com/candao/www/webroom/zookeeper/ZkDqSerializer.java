package com.candao.www.webroom.zookeeper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.curator.framework.recipes.queue.QueueSerializer;

import com.candao.www.webroom.model.SynSqlObject;

public class ZkDqSerializer implements QueueSerializer<SynSqlObject> {

	public SynSqlObject deserialize(byte[] buffer) {


		InputStream is = new ByteArrayInputStream(buffer); 
		
		ObjectInputStream oos;
		try {
			oos = new ObjectInputStream(is);
			SynSqlObject object = (SynSqlObject)oos.readObject();
			oos.close();
			return object;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		return null;
		 
	}

	public byte[] serialize(SynSqlObject receData) {
		
//		byte[] objByte = new byte[20000];
//		OutputStream is = new ByteArrayOutputStream(20000); 
		
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream out;
        byte[] objByte = null;
		try {
			out = new ObjectOutputStream(buf);
			 out.writeObject(receData);
			 objByte = buf.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return objByte;
	}

}
