package com.candao.file.fastdfs.pool;

import com.candao.file.fastdfs.StorageClient1;
import com.candao.file.fastdfs.StorageServer;
import com.candao.file.fastdfs.TrackerServer;

 

public class StorageClient extends StorageClient1
{
  public TrackerServer trackerServer;
  public StorageServer storageServer;

  public StorageClient()
  {
  }

  public StorageClient(TrackerServer trackerServer, StorageServer storageServer)
  {
    super(trackerServer, storageServer);
    this.trackerServer = this.trackerServer;
    this.storageServer = this.storageServer;
  }

  public TrackerServer getTrackerServer() {
    return this.trackerServer;
  }

  public void setTrackerServer(TrackerServer trackerServer) {
    this.trackerServer = trackerServer;
  }

  public StorageServer getStorageServer() {
    return this.storageServer;
  }

  public void setStorageServer(StorageServer storageServer) {
    this.storageServer = storageServer;
  }
}