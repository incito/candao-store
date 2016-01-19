package com.candao.file.fastdfs.pool;

import java.io.IOException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.candao.file.fastdfs.ClientGlobal;
import com.candao.file.fastdfs.ProtoCommon;
import com.candao.file.fastdfs.StorageServer;
import com.candao.file.fastdfs.TrackerClient;
import com.candao.file.fastdfs.TrackerServer;

/**
 * 
 *  <pre>
 * 
 * Copyright : Copyright  Pandoranews 2014 ,Inc. All right
 * Company : 凯盈资讯科技有限公司
 * </pre>
 * @author  tom
 * @version 1.0
 * @date 2014年9月26日 下午12:02:02
 * @history
 *
 */
public class FastdfsPool extends Pool
{
  public FastdfsPool(GenericObjectPool.Config poolConfig, PoolableObjectFactory factory)
  {
    super(poolConfig, factory);
  }
  public FastdfsPool(GenericObjectPool.Config poolConfig) {
    super(poolConfig, new FastdfsClientFactory());
  }

  private static class FastdfsClientFactory extends BasePoolableObjectFactory
  {
    public Object makeObject()
      throws Exception
    {
//      String classPath = new File(super.getClass().getResource("/").getFile()).getCanonicalPath();
//      String configFilePath = classPath + File.separator + "fdfs_client.conf";
//      ClientGlobal.init(configFilePath);
      ClientGlobal.init();
      TrackerClient tracker = new TrackerClient();
      TrackerServer trackerServer = tracker.getConnection();
      StorageServer storageServer = null;
      StorageClient client = new StorageClient(trackerServer, storageServer);
      return client;
    }

    public void destroyObject(Object obj)
      throws Exception
    {
      if ((obj == null) || (!(obj instanceof StorageClient)))
        return;
      StorageClient storageClient = (StorageClient)obj;
      TrackerServer trackerServer = storageClient.getTrackerServer();
      StorageServer storageServer = storageClient.getStorageServer();
      trackerServer.close();
      storageServer.close();
    }

    public boolean validateObject(Object obj)
    {
      StorageClient storageClient = (StorageClient)obj;
      try
      {
        return ProtoCommon.activeTest(storageClient.trackerServer.getSocket());
      } catch (IOException e) {
        e.printStackTrace();
      }return false;
    }
  }
}