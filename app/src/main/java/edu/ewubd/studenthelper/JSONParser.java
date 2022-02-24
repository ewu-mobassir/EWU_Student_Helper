package edu.ewubd.studenthelper;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


@SuppressWarnings("ALL")
public class JSONParser {

	private String TAG = "JSONParser";
	private static JSONParser instance = new JSONParser();
	private JSONParser() {}
	public static JSONParser getInstance() {
		return instance;
	}

	public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {

		HttpURLConnection http = null;
		InputStream is = null;
		JSONObject jObj = null;
		String json = "";
		// Making HTTP request
		try {
			// check for request method
			if (method == "POST") {
				if(params != null) {
					String paramString = URLEncodedUtils.format(params, "utf-8");
					url += "?" + paramString;
				}
				URL urlc = new URL(url);
				http = (HttpURLConnection) urlc.openConnection();
				is = http.getInputStream();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			// try parse the string to a JSON object
			try {
				jObj = new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				http.disconnect();
			} catch (Exception e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jObj;
	}
}
