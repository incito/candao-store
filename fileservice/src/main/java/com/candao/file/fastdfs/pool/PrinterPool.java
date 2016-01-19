package com.candao.file.fastdfs.pool;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * 
 *  <pre>
 * 
 * Copyright : Copyright  Pandoranews 2015 ,Inc. All right
 * Company : 凯盈资讯科技有限公司
 * </pre>
 * 
 * @author  zhao
 * @version 1.0
 * @date 2015年1月27日 下午1:13:48
 * @history  打印连接池初始化
 *
 */
public class PrinterPool extends Pool{

	
	  public PrinterPool(GenericObjectPool.Config poolConfig, PoolableObjectFactory factory)
	  {
	    super(poolConfig, factory);
	  }
	  public PrinterPool(GenericObjectPool.Config poolConfig) {
	    super(poolConfig, new PrinterPoolFactory());
	  }

}
