package com.trixsoft.cityrangers.activity;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;
 
public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    // constructor
    public JSONParser() {
 
    }
 
    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
            List<NameValuePair> params) {
 
        // Making HTTP request
        try {
 
            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
 
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
 
            }else if(method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
 
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }           
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
 
    }
    
    public JSONObject makeHttpRequestforIssues()
    {
    	Log.i("JSONParser","Before string declaration");
    	 String sb = new String();
    	 sb="{'issues': [{'pid': '1','issue': 'Road needs repair','place': 'Ward 19','created_at': '2012-04-29 02:04:02','updated_at': '0000-00-00 00:00:00'},{'pid': '2','issue': 'Electricity not present since long time','place': 'Ward 23','created_at': '2012-04-29 02:04:51','updated_at': '0000-00-00 00:00:00'},{'pid': '3','issue': 'Man hole not covered','place': 'Ward 27','created_at': '2012-04-29 02:05:57','updated_at': '0000-00-00 00:00:00'},{'pid': '4','issue': 'Garbage bin not cleaned up','place': 'Ward 29','created_at': '2012-04-29 02:07:14','updated_at': '0000-00-00 00:00:00'}],'success': 1,'IMEI':929383929}";
                 
    	 try {
			jObj = new JSONObject(sb);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			 Log.e("JSON Parser", "Error parsing data " + e.toString());
			e.printStackTrace();
		}
         
         
         return jObj;
    }
}