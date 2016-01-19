package com.candao.file.fastdfs.pool;

import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * 
 *  <pre>
 * 
 * Copyright : Copyright  Pandoranews 2014 ,Inc. All right
 * Company : 凯盈资讯科技有限公司
 * </pre>
 * @author  tom
 * @version 1.0
 * @date 2014年9月26日 下午12:02:22
 * @history
 *
 */
public class FastdfsPoolConfig extends GenericObjectPool.Config
{
  public FastdfsPoolConfig()
  {
    setTestWhileIdle(true);
    setMinEvictableIdleTimeMillis(60000L);
    setTimeBetweenEvictionRunsMillis(30000L);
    setNumTestsPerEvictionRun(-1);
  }

  public int getMaxIdle() {
    return this.maxIdle;
  }

  public void setMaxIdle(int maxIdle)
  {
    this.maxIdle = maxIdle;
  }

  public int getMinIdle()
  {
    return this.minIdle;
  }

  public void setMinIdle(int minIdle)
  {
    this.minIdle = minIdle;
  }

  public int getMaxActive()
  {
    return this.maxActive;
  }

  public void setMaxActive(int maxActive)
  {
    this.maxActive = maxActive;
  }

  public long getMaxWait()
  {
    return this.maxWait;
  }

  public void setMaxWait(long maxWait)
  {
    this.maxWait = maxWait;
  }

  public byte getWhenExhaustedAction()
  {
    return this.whenExhaustedAction;
  }

  public void setWhenExhaustedAction(byte whenExhaustedAction)
  {
    this.whenExhaustedAction = whenExhaustedAction;
  }

  public boolean isTestOnBorrow()
  {
    return this.testOnBorrow;
  }

  public void setTestOnBorrow(boolean testOnBorrow)
  {
    this.testOnBorrow = testOnBorrow;
  }

  public boolean isTestOnReturn()
  {
    return this.testOnReturn;
  }

  public void setTestOnReturn(boolean testOnReturn)
  {
    this.testOnReturn = testOnReturn;
  }

  public boolean isTestWhileIdle()
  {
    return this.testWhileIdle;
  }

  public void setTestWhileIdle(boolean testWhileIdle)
  {
    this.testWhileIdle = testWhileIdle;
  }

  public long getTimeBetweenEvictionRunsMillis()
  {
    return this.timeBetweenEvictionRunsMillis;
  }

  public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis)
  {
    this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
  }

  public int getNumTestsPerEvictionRun()
  {
    return this.numTestsPerEvictionRun;
  }

  public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun)
  {
    this.numTestsPerEvictionRun = numTestsPerEvictionRun;
  }

  public long getMinEvictableIdleTimeMillis()
  {
    return this.minEvictableIdleTimeMillis;
  }

  public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis)
  {
    this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
  }

  public long getSoftMinEvictableIdleTimeMillis()
  {
    return this.softMinEvictableIdleTimeMillis;
  }

  public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis)
  {
    this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
  }
}