package com.candao.www.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

public class HttpRequestUtil {
    private final static String CHARSET = "utf-8";
    private final static Integer CONNECTIMREOUT = 5000;
    private final static Integer SOCKETTIMROUT = 5000;

    /**
     * Do GET request
     *
     * @param url
     * @return
     * @throws Exception
     * @throws IOException
     */
    public static String doGet(String url) throws Exception {

        URL localURL = new URL(url);

        URLConnection connection = openConnection(localURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

        httpURLConnection.setRequestProperty("Accept-Charset", CHARSET);
        httpURLConnection
                .setRequestProperty("Content-Type", "application/json");
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception(
                    "HTTP Request is not success, Response code is "
                            + httpURLConnection.getResponseCode());
        }

        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, CHARSET);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } finally {

            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return resultBuffer.toString();
    }

    /**
     * Do POST request
     *
     * @param url
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<?, ?> parameterMap)
            throws Exception {

		/* Translate parameter map to parameter date string */
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator<?> iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = parameterMap.get(key) + "";
                } else {
                    value = "";
                }

                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
        }

        System.out.println("POST parameter : " + parameterBuffer.toString());

        URL localURL = new URL(url);

        URLConnection connection = openConnection(localURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", CHARSET);
        httpURLConnection
                .setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Content-Length",
                String.valueOf(parameterBuffer.length()));

        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);

            outputStreamWriter.write(parameterBuffer.toString());
            outputStreamWriter.flush();

            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is "
                                + httpURLConnection.getResponseCode());
            }

            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, CHARSET);
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }

        } finally {

            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

            if (reader != null) {
                reader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        }

        return resultBuffer.toString();
    }

    public static String doPostJson(String url, String param) throws Exception {
        URL localURL = new URL(url);
        URLConnection connection = openConnection(localURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
        httpURLConnection.setRequestProperty("Accept-Charset", CHARSET);
        httpURLConnection
                .setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("ContentType", CHARSET);
        httpURLConnection.setRequestProperty("Content-Length",
                String.valueOf(param.length()));
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream, CHARSET);
            outputStreamWriter.write(param);
            outputStreamWriter.flush();
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is "
                                + httpURLConnection.getResponseCode());
            }
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, CHARSET);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return resultBuffer.toString();
    }

    private static URLConnection openConnection(URL localURL)
            throws IOException {
        URLConnection connection;
        connection = localURL.openConnection();
        connection.setConnectTimeout(CONNECTIMREOUT);
        connection.setReadTimeout(SOCKETTIMROUT);
        return connection;
    }

    public static void main(String[] args) throws Exception {

        // System.out.println(doPostJson("http://localhost:8080/integral/interface/activityImport",
        // "{\"activityId\":\"1\",\"startTime\":\"2015-08-12 11:11:11\",\"storeList\":\"[{\"shopId\":\"1\",\"storeId\":\"3\"}]\"}")
        // );
        System.out
                .println(doPostJson(

                        "http://127.0.0.1:8080/paymentRevoke",
                        "{\"orderId\":\"1903110527e5791\",\"oldOrderId\":\"222\"}"));
//        System.out
//                .println(doPostJson(
//                        "http://117.121.20.146:9092/integral/payment",
//                        "{\"activityId\":\"1820\",\"cardExpDate\":\"2003\",\"cardNum\":\"4392qq260028467460\",\"enCode\":\"82332123\",\"orderId\":\"313539orb216365723513\",\"transAmt\":\"2300\"}"));
       /* System.out
                .println(doPostJson(
                        "http://192.168.23.123:10001/bank/payment",
                        "{\"cardExpDate\":\"0720\",\"cardNum\":\"6226880053372785\",\"code\":\"TPYKF\",\"orderId\":\"79501604113343\",\"tranAmt\":\"23500\"}"));*/
       /* System.out1
                .println(doPostJson(
                        "http://192.168.23.123:10001/bank/payment",
                        "{\"cardExpDate\":\"0320\",\"cardNum\":\"4392260028245056\",\"code\":\"COSTAHB\",\"orderId\":\"01212180262\",\"tranAmt\":\"500\"}"));*/
     /*   System.out
                .println(doPostJson(
                        "http://192.168.100.155:10001/bank/payment",
                        "{\"cardExpDate\":\"1249\",\"cardNum\":\"6013823100097711014\",\"code\":\"MS0001\",\"orderId\":\"7950604113343\",\"tranAmt\":\"23500\"}"));*/

//        System.out
//                .println(doPostJson(
//
//                        "http://117.121.20.146:9092/integral/paymentRevoke",
//                        "{\"orderId\":\"r628112839238 \",\"oldOrderId\":\"628112839238\"}"));
//        System.out
//                .println(doPostJson(
//
//                        "http://117.121.20.146:9092/integral/paymentRevoke",
//                        "{\"orderId\":\"r156112841477\",\"oldOrderId\":\"15611284·1477\"}"));
//        System.out
//                .println(doPostJson(
//
//                        "http://117.121.20.146:9092/integral/paymentRevoke",
//                        "{\"orderId\":\"r158112932363\",\"oldOrderId\":\"158112932363\"}"));
//        System.out
//                .println(doPostJson(
//
//                        "http://117.121.20.146:9092/integral/paymentRevoke",
//                        "{\"orderId\":\"r490111933863\",\"oldOrderId\":\"490111933863\"}"));
//        System.out
//                .println(doPostJson(
//
//                        "http://117.121.20.146:9092/integral/paymentRevoke",
//                        "{\"orderId\":\"r644111936881\",\"oldOrderId\":\"644111936881\"}"));
//        System.out
//                .println(doPostJson(
//
//                        "http://117.121.20.146:9092/integral/paymentRevoke",
//                        "{\"orderId\":\"r490111933863\",\"oldOrderId\":\"490111933863\"}"));

       /* System.out
                .println(doPostJson(

                        "http://117.121.20.146:9092/integral/paymentReserval",
                        "{\"orderId\":\"344111105333163365\",\"oldOrderId\":\"748110527607\"}"));*/


      /*  System.out
                .println(doPostJson(

                        "http://117.121.20.146:9092/integral/paymentRevoke",
                        "{\"orderId\":\"961611054760711\",\"oldOrderId\":\"966110527607\"}"));

        System.out
                .println(doPostJson(

                        "http://117.121.20.146:9092/integral/paymentRevoke",
                        "{\"orderId\":\"11111053333163365\",\"oldOrderId\":\"748110527607\"}"));


        System.out
                .println(doPostJson(

                        "http://117.121.20.146:9092/integral/paymentRevoke",
                        "{\"orderId\":\"19031105275791\",\"oldOrderId\":\"903110527579\"}"));*/

      /*  System.out
                .println(doPostJson(
                        "http://117.121.20.137:10000/bank/payment",
                        "{\"cardExpDate\":\"0924\",\"cardNum\":\"6214850280354785\",\"code\":\"MS0001\",\"orderId\":\"130051931012469\",\"tranAmt\":\"100\"}"));

        System.out
                .println(doPostJson(

                        "http://117.121.20.137:10000/bank/  ",
                        "{\"orderId\":\"13005191012669\",\"oldOrderId\":\"13005191012469\"}"));

        System.out
                .println(doPostJson(

                        "http://117.121.20.137:10000/bank/paymentReserval",
                        "{\"orderId\":\"240059002470\",\"oldOrderId\":\"130059002469\"}"));

        System.out
                .println(doPostJson(

                        "http://117.121.20.137:10000/bank/paymentRevokeReserval",
                        "{\"orderId\":\"250059002470\",\"oldOrderId\":\"240059002470\"}"));

        System.out
                .println(doPostJson(

                        "http://117.121.20.146:9092/integral/activitySyn",
                        "{\"enCode\":\"5773e998\"}"));*/
//        System.out
//                .println(doPostJson(
//                        "http://127.0.0.1:8080/payment",
//                        "{\"activityId\":\"ZSYH11\",\"cardExpDate\":\"2003\",\"cardNum\":\"4392260028467460\",\"enCode\":\"82393007\",\"orderId\":\"5773e998185723513\",\"transAmt\":\"null\"}"));
//        System.out
//                .println(doPostJson(
//                        "http://127.0.0.1:8080/paymentOld?activityId=ZSYH11&cardExpDate=2003&cardNum=4392260028467460&enCode=82393007", ""));
    }
}
