package rest;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import net.sf.json.JSONObject;

/**
 * 接口测试记录
 * 
 * @author zgt
 * 
 */
public class TestPas {

  /**
   * 手工优免 接口单元调试
   */
  @Test
  public void test1() {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      HttpPost httppost = new HttpPost(
          "http://localhost:8080/newspicyway/padinterface/getPreferentialList.json");
      JSONObject jsonParam = new JSONObject();
//      jsonParam.put("userid", "004");
//      jsonParam.put("orderid", "H20160314503223000169");
//      jsonParam.put("preferentialid", "0b5e0716f1474e2982da459585234e6f");
//      jsonParam.put("disrate", "8.5");
        jsonParam.put("typeid", "08");
        jsonParam.put("branchid", "503223");
//      jsonParam.put("preferentialAmt", "0");

      httppost.setEntity(new StringEntity(jsonParam.toString(), "UTF-8"));
      CloseableHttpResponse response = httpclient.execute(httppost);
      try {
        String resData = EntityUtils.toString(response.getEntity());
        if (resData == null || resData.equals("") || !resData.startsWith("{")
            || !resData.endsWith("}")) {
          System.out.println("............");
        }
        JSONObject json = JSONObject.fromObject(resData);
        System.out.println(json);
      } finally {
        response.close();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  @Test
  public void test2() {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      HttpPost httppost = new HttpPost(
          "http://localhost:8080/newspicyway/padinterface/usePreferentialItem.json");
      JSONObject jsonParam = new JSONObject();
      jsonParam.put("userid", "004");
      jsonParam.put("orderid", "H20160314503223000171");
      jsonParam.put("preferentialid", "409b2996df1f43d9a92c4b21d4bb529f");
      jsonParam.put("disrate", "8.5");
      jsonParam.put("type", "08");
      jsonParam.put("branchid", "503223");
      jsonParam.put("preferentialAmt", "0");

      httppost.setEntity(new StringEntity(jsonParam.toString(), "UTF-8"));
      CloseableHttpResponse response = httpclient.execute(httppost);
      try {
        String resData = EntityUtils.toString(response.getEntity());
        if (resData == null || resData.equals("") || !resData.startsWith("{")
            || !resData.endsWith("}")) {
          System.out.println("............");
        }
        JSONObject json = JSONObject.fromObject(resData);
        System.out.println(json);
      } finally {
        response.close();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
