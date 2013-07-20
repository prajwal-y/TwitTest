package com.timepass.twittest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class TweetGetter {

	private static final String TAG = "TweetGetter";
	
	public static String getBearerToken(String APIKEY, String APISECRET) {
		String bearerToken = null;
		try {
			bearerToken = new GetBearerToken().execute(APIKEY,APISECRET).get();
		} catch (InterruptedException e) {
			Log.e(TAG, "InterruptedException occurred:", e);
		} catch (ExecutionException e) {
			Log.e(TAG, "ExecutionException occurred:", e);
		}
		return bearerToken;
	}

	public static ArrayList<Tweet> getTweets(String bearerToken, String URL) {
		String result = null;
		ArrayList<Tweet> list = new ArrayList<Tweet>();
		try {
			result = new GetTweets().execute(bearerToken, URL).get();
			JSONArray root = new JSONArray(result);
            for (int i = 0; i < root.length(); i++) {
                    JSONObject session = root.getJSONObject(i);
                    Tweet tweet = new Tweet();
                    tweet.setContent(session.getString("text"));
                    tweet.setTimeStamp(session.getString("created_at"));
                    list.add(tweet);
            }
		} catch (InterruptedException e) {
			Log.e(TAG, "InterruptedException occurred:", e);
		} catch (ExecutionException e) {
			Log.e(TAG, "ExecutionException occurred:", e);
		} catch(JSONException e) {
			Log.e(TAG, "JSONException occurred:", e);
		} catch(Exception e) {
			Log.e(TAG, "Exception occurred:", e);
		}
		return list;
	}
	
	private static class GetBearerToken extends AsyncTask<String, Void, String> {

		//private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			/*dialog = new ProgressDialog(AppData.activity);
			dialog.setCancelable(true);
			dialog.setMessage("Getting bearer token!");
			dialog.show();*/
		}

		@Override
		protected void onPostExecute(String val) {
			//dialog.cancel();
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {
				DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
				HttpPost httppost = new HttpPost("https://api.twitter.com/oauth2/token");
				
				String apiString = params[0] + ":" + params[1];
				String authorization = "Basic " + Base64.encodeToString(apiString.getBytes(), Base64.NO_WRAP);
		
				httppost.setHeader("Authorization", authorization);
				httppost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				httppost.setEntity(new StringEntity("grant_type=client_credentials"));
		
				InputStream inputStream = null;
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
		
				inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
		
				String line = null;
				while ((line = reader.readLine()) != null) {
				    sb.append(line + "\n");
				}
				JSONObject root = new JSONObject(sb.toString());
				return root.getString("access_token");
			}catch (Exception e){
				Log.e("GetBearerTokenTask", "Error:" + e.getMessage());
				return null;
			}
		}
	}

	
	private static class GetTweets extends AsyncTask<String, Void, String> {
		
		//private ProgressDialog dialog;
	
		@Override
		protected void onPreExecute() {
			/*dialog = new ProgressDialog(AppData.activity);
			dialog.setCancelable(true);
			dialog.setMessage("Getting bearer token!");
			dialog.show();*/
		}

		@Override
		protected void onPostExecute(String rp) {
			//dialog.cancel();
		}
	
	    @Override
		protected String doInBackground(String... params) {
			
			try {
				DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
				HttpGet httpget = new HttpGet(params[1]);
				httpget.setHeader("Authorization", "Bearer " + params[0]);
				httpget.setHeader("Content-type", "application/json");
				HttpResponse response = httpclient.execute(httpget);
				return EntityUtils.toString(response.getEntity());
			}catch (Exception e){
				Log.e("GetTweets", "Exception occurred:", e);
				return null;
			}
		}
	}
	
}
