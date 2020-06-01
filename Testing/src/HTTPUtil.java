

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class HTTPUtil {
	static final String RESPONSE_CODE = "response_code";
	static final String RESPONSE_DATA = "response_data";
	static final String HTTP_GET = "GET";
	static final String HTTP_POST = "POST";

	static JSONObject getHttpResponse(String url, String method)
			throws MalformedURLException, IOException {
		return getHttpResponse(url, method, null);
	}

	static JSONObject getHttpResponse(String url, String method, String data)
			throws MalformedURLException, IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url)
				.openConnection();
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json; utf-8");
		if (data != null && !data.isEmpty()) {
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept", "application/json");
			try(OutputStream os = connection.getOutputStream()) {
			    byte[] input = data.getBytes("utf-8");
			    os.write(input, 0, input.length);           
			}
		}
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			String readFromStream = readFromStream(connection.getInputStream());
			JSONObject json = new JSONObject();
			json.put(RESPONSE_CODE, responseCode);
			json.put(RESPONSE_DATA, readFromStream);
			return json;
		} else {
			InputStream errorStream = connection.getErrorStream();
			if (errorStream != null) {
				String readFromStream = readFromStream(errorStream);
				JSONObject json = new JSONObject();
				json.put(RESPONSE_CODE, responseCode);
				json.put(RESPONSE_DATA, readFromStream);
				return json;
			} else {
				JSONObject json = new JSONObject();
				json.put(RESPONSE_CODE, responseCode);
				json.put(RESPONSE_DATA, "Non 200 response code received");
				return json;
			}
		}
	}
	
	static JSONObject getHttpResponse(String url, String method, String data,String token)
			throws MalformedURLException, IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url)
				.openConnection();
		String authorization = "Bearer " + token;
		connection.setRequestProperty("Authorization", authorization);
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json; utf-8");
		if (data != null && !data.isEmpty()) {
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept", "application/json");
			try(OutputStream os = connection.getOutputStream()) {
			    byte[] input = data.getBytes("utf-8");
			    os.write(input, 0, input.length);           
			}
		}
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			String readFromStream = readFromStream(connection.getInputStream());
			JSONObject json = new JSONObject();
			json.put(RESPONSE_CODE, responseCode);
			json.put(RESPONSE_DATA, readFromStream);
			return json;
		} else {
			InputStream errorStream = connection.getErrorStream();
			if (errorStream != null) {
				String readFromStream = readFromStream(errorStream);
				JSONObject json = new JSONObject();
				json.put(RESPONSE_CODE, responseCode);
				json.put(RESPONSE_DATA, readFromStream);
				return json;
			} else {
				JSONObject json = new JSONObject();
				json.put(RESPONSE_CODE, responseCode);
				json.put(RESPONSE_DATA, "Non 200 response code received");
				return json;
			}
		}
	}

	private static String readFromStream(InputStream stream)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder result = new StringBuilder();
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				result.append(line).append(System.lineSeparator());
			}
			return result.toString().trim();
		} finally {
			br.close();
		}
	}
}
