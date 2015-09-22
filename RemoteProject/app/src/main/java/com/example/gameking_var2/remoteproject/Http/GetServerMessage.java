package com.example.gameking_var2.remoteproject.Http;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
使用POST來網路連線
 */
public class GetServerMessage {
	public String all(String url,String alldata)
	{

		 try
	        {
	        	  URL obj = new URL(url);
				  HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				 

				  con.setRequestMethod("POST");
				  con.setRequestProperty("contentType", "big5"); 

				  con.setDoOutput(true);
				  con.setDoInput(true);   
				  
				  //傳送資料
				  DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				  wr.write(alldata.getBytes("big5"));//為了傳送utf8的文字改成這個
				  wr.flush();
				  wr.close();
				  
				  int responseCode = con.getResponseCode();
				  

				  //回傳資料
				  BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream(),"big5"));
				  String inputLine;
				  StringBuffer response = new StringBuffer();
				  
				  while ((inputLine = in.readLine()) != null) {
					 response.append(inputLine);
				  }
				  in.close();

				  //斷開連接
				  con.disconnect();

				  return(response.toString()); 
				  
	         }
	         catch(Exception e){
	             return "No wifi";
	         }
		
		
	}

		 
}

