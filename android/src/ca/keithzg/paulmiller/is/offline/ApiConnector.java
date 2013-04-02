package ca.keithzg.paulmiller.is.offline;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class ApiConnector extends AsyncTask<String, Void, Long>{
	private URL toGet = null;
	
	@Override
	protected Long doInBackground(String... urls) {
		URL u = null;
		Long toReturn = null;
		try {
			u = new URL(urls[0]);
			toGet = u;
		} catch (MalformedURLException e) {
			Log.d("HTTPGET", "no valid URL");
			return null;
		}
		try {
			u.toURI();
		} catch (URISyntaxException e) {
			Log.d("HTTPGET", "not valid URI");
			return null;
		}
		
		HttpURLConnection conn = null;
		BufferedReader br;
		String line;
		String result = "";
		try{
			conn = (HttpURLConnection)toGet.openConnection();
			conn.setRequestMethod("GET");
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = br.readLine()) != null) {
	            result += line;
	        }
			br.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(result);
			 toReturn = Long.parseLong(jsonObject.get("seconds").toString());
		} catch (JSONException e) {
			Log.d("JSONPARSE", "could not parse input to JSON object");
			e.printStackTrace();
			return null;
		}
		Log.d("ApiConnect", "Got time from server: " + toReturn.toString());
		return toReturn;
	}

}
