package com.candao.file.pool;

import java.io.IOException;

import org.apache.commons.pool.BasePoolableObjectFactory;

import com.candao.file.fastdfs.ClientGlobal;
import com.candao.file.fastdfs.ProtoCommon;
import com.candao.file.fastdfs.StorageServer;
import com.candao.file.fastdfs.TrackerClient;
import com.candao.file.fastdfs.TrackerServer;
 

public class PrinterPoolFactory extends BasePoolableObjectFactory {
	
	public Object makeObject() throws Exception {

		ClientGlobal.init();
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = tracker.getConnection();
		StorageServer storageServer = null;
		StorageClient client = new StorageClient(trackerServer, storageServer);
		return client;
	}

	public void destroyObject(Object obj) throws Exception {
		if ((obj == null) || (!(obj instanceof StorageClient)))
			return;
		StorageClient storageClient = (StorageClient) obj;
		TrackerServer trackerServer = storageClient.getTrackerServer();
		StorageServer storageServer = storageClient.getStorageServer();
		trackerServer.close();
		storageServer.close();
	}

	public boolean validateObject(Object obj) {
		SocketInfo socketInfo = (SocketInfo) obj;
		try {
			return ProtoCommon.activeTest(socketInfo.getSocket());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}