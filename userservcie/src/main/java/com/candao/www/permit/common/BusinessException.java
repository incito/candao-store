/**
 * 
 */
package com.candao.www.permit.common;


/**
 * 业务操作异常，必须初始化异常消息。
 * @author zhao
 *
 */
public class BusinessException extends RuntimeException {
  public BusinessException(String message){
    super(message);
  }
}
