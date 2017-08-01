package cn.wuxia.epay.epaylinks.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * 客户端使用的http提交类
 * @author FengHanhao
 * @create time 2013-4-18 下午04:12:52
 */
public class HttpPostUtil {
	
	/** 应答内容 */
	private String resContent;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://10.200.48.135:80/paycenter/queryOrder.do?order_no=1364953038575&partner=130&sign=4ecb9b1225b2599e270c937071bd482a";
		HttpPostUtil postutil = new HttpPostUtil();
		if(postutil.postRequest(url)){
			System.out.print(postutil.getResContent());
		}
		
	}
	
	/**
	 * 发送post请求
	 * @param strUrl
	 * @return
	 */
	public boolean postRequest(String strUrl){
		//请求url
		String url = this.getURL(strUrl);  
		//请求参数
		String queryString = this.getQueryString(strUrl); 
		try{
		if (url.indexOf("http://") != -1) {
			this.resContent = this.postHttp(url, queryString);
			return true;
		 } else if (url.indexOf("https://") != -1) {
			this.resContent = this.postHttps(url, queryString);
		    return true;
		  }
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String postHttp(String urlStr, String params) { 
		System.out.println("http方式提交的URL: "+urlStr + "?" + params); 
		PrintWriter out = null; 
		BufferedReader in = null; 
		StringBuffer result = new StringBuffer();
		try { 
			URL realUrl = new URL(urlStr); 
			// 打开和URL之间的连接 
			URLConnection conn = realUrl.openConnection(); 
			// 设置通用的请求属性 
			conn.setRequestProperty("accept", "*/*"); 
			conn.setRequestProperty("connection", "Keep-Alive"); 
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)"); 
			// 发送POST请求必须设置如下两行 
			conn.setDoOutput(true); 
			conn.setDoInput(true); 
			// 获取URLConnection对象对应的输出流 
			out = new PrintWriter(conn.getOutputStream()); 
			// 发送请求参数 
			out.print(params); 
			// flush输出流的缓冲 
			out.flush(); 
			// 定义BufferedReader输入流来读取URL的响应 
			in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
			String line = ""; 
			while ((line = in.readLine()) != null) { 
				result.append(line); 
			} 
//			System.out.println("remote host respone : "+result);
			System.out.println("远程主机返回文本结果："+result); 
		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			try { 
				if (out != null) { 
					out.close(); 
					out = null; 
				} 
				if (in != null) { 
					in.close(); 
					in = null; 
				} 
			} catch (Exception ex) { 
				ex.printStackTrace();
			} 
		}
		
		return result.toString(); 
	}

	/**
	 * 以SSL方式提交请求
	 * @param urlString  提交的url地址
	 * @param data		   提交的请求参数
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public String postHttps(String urlString, String data) throws NoSuchAlgorithmException, KeyManagementException, IOException
			{
		System.out.println("https方式提交的URL:"+urlString);
		System.out.println("提交的参数："+data);
		OutputStreamWriter os = null;

			URL url = new URL(urlString);
			URLConnection con = url.openConnection();
			System.out.println("连接类型："+ con.getClass());
			BufferedReader in = null; 
			StringBuffer result = new StringBuffer();
	
			if (con instanceof javax.net.ssl.HttpsURLConnection) {
				System.out.println("*** openConnection returns an instanceof javax.net.ssl.HttpsURLConnection");
				
				//信任所有证书 开始
				javax.net.ssl.SSLContext sc = null;
				javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[] { new javax.net.ssl.X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				} 
				} };

				// Install the all-trusting trust manager
				sc = javax.net.ssl.SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				//信任所有证书 结束
					
				javax.net.ssl.HostnameVerifier hv = new javax.net.ssl.HostnameVerifier() {
					public boolean verify(String urlHostName, javax.net.ssl.SSLSession session) {
						return urlHostName.equals(session.getPeerHost());
					}
				};
				javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(hv);
				javax.net.ssl.HttpsURLConnection conn = null;
				conn = (javax.net.ssl.HttpsURLConnection) con;
				// conn.setRequestProperty("Content-Type", "text/xml");
				conn.setDoOutput(true);
				conn.setFollowRedirects(true);
				// conn.setReadTimeout(30000);
				os = new OutputStreamWriter(conn.getOutputStream());
				os.write(data);
				os.flush();
				if (conn.getResponseCode() == 302 || conn.getResponseCode() == 200) {
					System.out.println("https请求发送成功。");
					System.out.println("返回码：" + conn.getResponseCode());
				}
				// 定义BufferedReader输入流来读取URL的响应 
				in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
				String line = ""; 
				while ((line = in.readLine()) != null) { 
					result.append(line); 
				}
			}
			else if (con instanceof com.sun.net.ssl.HttpsURLConnection) {
				System.out.println("***openConnection returns an instanceof com.sun.net.ssl.HttpsURLConnection");
				//信任所有证书 开始
				com.sun.net.ssl.SSLContext sc = null;
				com.sun.net.ssl.TrustManager[] trustAllCerts = new com.sun.net.ssl.TrustManager[] { new com.sun.net.ssl.X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
//				@Override
				public boolean isClientTrusted(X509Certificate[] arg0) {
					// TODO Auto-generated method stub
					return true;
				}
//				@Override
				public boolean isServerTrusted(X509Certificate[] arg0) {
					// TODO Auto-generated method stub
					return true;
				}
				} };
				// Install the all-trusting trust manager
				sc = com.sun.net.ssl.SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				com.sun.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				//信任所有证书 结束
				
				com.sun.net.ssl.HttpsURLConnection conn = null;
				conn = (com.sun.net.ssl.HttpsURLConnection) con;
				com.sun.net.ssl.HostnameVerifier hv = new com.sun.net.ssl.HostnameVerifier() {
//					@Override
					public boolean verify(String arg0, String arg1) {
						return true;
					}
				};
				com.sun.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(hv);
				conn.setAllowUserInteraction(true);
				conn.setDoOutput(true);
				
			    os = new OutputStreamWriter(conn.getOutputStream());
				os.write(data);
				os.flush();

				if (conn.getResponseCode() == 302 || conn.getResponseCode() == 200) {
					System.out.println("https请求发送成功。");
					System.out.println("返回码：" + conn.getResponseCode());
				}
				// 定义BufferedReader输入流来读取URL的响应 
				in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
				String line = ""; 
				while ((line = in.readLine()) != null) { 
					result.append(line);
				}
			}
			System.out.println("远程主机返回的文本结果："+result); 
			
		return result.toString();
	}
	
	/**
	 * 获取不带查询串的url
	 * @param strUrl
	 * @return String
	 */
	private String getURL(String strUrl) {

		if(null != strUrl) {
			int indexOf = strUrl.indexOf("?");
			if(-1 != indexOf) {
				return strUrl.substring(0, indexOf);
			} 
			
			return strUrl;
		}
		
		return strUrl;
		
	}
	
	/**
	 * 获取查询串
	 * @param strUrl
	 * @return String
	 */
	private String getQueryString(String strUrl) {
		
		if(null != strUrl) {
			int indexOf = strUrl.indexOf("?");
			if(-1 != indexOf) {
				return strUrl.substring(indexOf+1, strUrl.length());
			} 
			
			return "";
		}
		
		return strUrl;
	}

	public String getResContent() {
		return resContent;
	}

	public static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
	
}
