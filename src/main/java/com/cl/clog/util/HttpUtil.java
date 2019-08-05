package com.cl.clog.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Slf4j
public class HttpUtil {


    /**
     * 无参数GET
     * @param url
     * @return
     */
    public static String doGetNoParam(String url) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);
        // 响应模型
        CloseableHttpResponse response = null;
        StringBuffer sb = new StringBuffer();
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            //System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
                sb.append("响应状态为:" + response.getStatusLine())
                        .append("响应内容长度为:" + responseEntity.getContentLength())
                        .append("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            releaseRes(httpClient, response);
        }
        return sb.toString();
    }

    private static void releaseRes(CloseableHttpClient httpClient, CloseableHttpResponse response) {
        try {
            // 释放资源
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 有参数POST
     * @param url
     */
    public static void doPostWithParams(String url) {

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 参数
        StringBuffer params = new StringBuffer();
        try {
            // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
            params.append("name=" + URLEncoder.encode("&", "utf-8"));
            params.append("&");
            params.append("age=24");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        // 创建Post请求
        HttpPost httpPost = new HttpPost(url + "?" + params);

        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            releaseRes(httpClient, response);
        }
    }


    /**
     * POST form
     * @param scheme 协议
     * @param host 主机
     * @param port 端口
     * @param path  路径
     * @param qparams  参数 表单的key-value
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String postForm(  String  scheme ,String host ,int port ,String path  ,List<NameValuePair> qparams ) throws URISyntaxException, IOException {

        //核心应用类
        HttpClient httpClient = new DefaultHttpClient();

        //设定表单需要提交的参数
        //示例：提交用户名和密码
        //设定需要访问的URL，第四个参数为表单提交路径
        URI uri = URIUtils.createURI(scheme, host, port, path,
                //将参数加入URL中
        URLEncodedUtils.format(qparams, "UTF-8"), null);
        //Post提交
        HttpPost httpPost = new HttpPost(uri);

        //httpClient执行，返回response
        HttpResponse response = httpClient.execute(httpPost);

        //获取实体
        HttpEntity httpEntity= response.getEntity();

        //打印StatusLine
        System.out.println("StatusLine: " + response.getStatusLine());

        //读取内容
        String content = EntityUtils.toString(httpEntity, "UTF-8");
        //打印输出结果内容
        // System.out.println(content);
        content = UnicodeUtil.decodeUnicode(content);
        return content;
    }


    /**
     * 获取端口是否可用
     * @param ip ip
     * @param port 端口
     * @return
     */
   private static boolean  portCanUse(String ip ,int port){
      boolean can = true;
      Socket skt = null;
      try {
          log.info("查看 "+ port);
          skt = new Socket(ip, port);
          log.info("端口 " + port+ " 已被使用");
          can = false;
      }catch (UnknownHostException e) {
          log.info("UnknownHostException occured" + e.getMessage());
      }catch (IOException e) {
          log.info("IOException occured" +  e.getMessage());
      }finally {
          if(skt!=null){
              try {
                  skt.close();
              } catch (IOException e) {
                  log.info(e.getMessage());
              }
          }
          skt = null;
      }
      return can;
   }


   private  static int getResponseCode(String url){
      DefaultHttpClient httpclient = new DefaultHttpClient();
      String location = null;
      int responseCode = 0;
      try {
          final HttpGet request = new HttpGet(url);
          org.apache.http.params.HttpParams params = new BasicHttpParams();
          params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
          request.setParams(params);
          HttpResponse response = httpclient.execute(request);
          responseCode = response.getStatusLine().getStatusCode();
          return responseCode;
      }catch (Exception e){
          log.info(e.getMessage());
      }
      return responseCode;
   }


    private static Map<String,Object> getResponseMsg(String url) {
        Map<String, Object> map = new HashMap<>();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String returnBack = "";
        int responseCode = 0;
        try {
            final HttpGet request = new HttpGet(url);
            org.apache.http.params.HttpParams params = new BasicHttpParams();
            params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
            request.setParams(params);
            HttpResponse response = httpclient.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                log.info(response.toString());
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                OutputStream os = new ByteArrayOutputStream();
                byte[] bs = new byte[512];
                int read = content.read(bs);
                byte[]  bs1  = new byte[read];
                System.arraycopy(bs,0,bs1,0,read);
                returnBack  = new String(bs1,"UTF-8");
            }
        } catch (Exception e) {
            log.error("异常");
        }
        map.put("returnBack",returnBack);
        map.put("responseCode",responseCode);
        return map;
    }



    private static Map<String,Object> getResponseHeader(String url) {
      Map<String, Object> map = new HashMap<>();
      DefaultHttpClient httpclient = new DefaultHttpClient();
      String location = "";
      int responseCode = 0;
      try {
          final HttpGet request = new HttpGet(url);
          org.apache.http.params.HttpParams params = new BasicHttpParams();
          params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
          request.setParams(params);
          HttpResponse response = httpclient.execute(request);
          responseCode = response.getStatusLine().getStatusCode();
          if (responseCode == 200) {
              log.info(response.toString());
          } else if (responseCode == 302) {
              org.apache.http.Header locationHeader = response.getFirstHeader("Location");
              if (locationHeader != null) {
                  location = locationHeader.getValue();
                  log.info(location);
              }
          }
      } catch (Exception e) {
          log.error("异常");
      }
      map.put("responseCode",responseCode);
      map.put("location", location);
      return map;
  }

}
