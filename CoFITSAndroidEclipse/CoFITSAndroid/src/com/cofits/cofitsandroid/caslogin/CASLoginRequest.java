package com.cofits.cofitsandroid.caslogin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cofits.cofitsandroid.caslogin.ReadAllBufferedReader;

/**
 * Request which will, for a given service, do a CAS login and give back the 
 * redirect url and the ticket
 *
 */
public class CASLoginRequest {
	private static final String CAS_URL = "https://cas.utc.fr/cas/";
	private static final String LOGIN_URL = "login?service=";

	private String jSessionId;
	private URL redirectUrl;

	public CASLoginRequest(String username, String password, String service) throws IOException {

		URL casUrl = new URL(CAS_URL + LOGIN_URL + service);
		URLConnection firstCasConnection = casUrl.openConnection();
        ReadAllBufferedReader in = new ReadAllBufferedReader(new InputStreamReader(firstCasConnection.getInputStream()));
        String firstCasConnectionData = in.readAll();
        in.close();
        
        String jSessionId = firstCasConnection.getHeaderFields().get("Set-Cookie").get(0);
        jSessionId = jSessionId.substring(0, jSessionId.indexOf(";"));
		Pattern ltPattern = Pattern.compile("<input type=\"hidden\" name=\"lt\" value=\"(.+?)\" />");
		Pattern executionPattern = Pattern.compile("<input type=\"hidden\" name=\"execution\" value=\"(.+?)\" />");

		Matcher ltMatcher = ltPattern.matcher(firstCasConnectionData);
		if (! ltMatcher.find()) {
			throw new IOException("Wrong answer sent by the remote server !");
		}

		Matcher executionMatcher = executionPattern.matcher(firstCasConnectionData);
		if (! executionMatcher.find()) {
			throw new IOException("Wrong answer sent by the remote server !");			
		}

        String lt = ltMatcher.group(1);
        String execution = executionMatcher.group(1); 
        
        HttpURLConnection loginCasConnection = (HttpURLConnection)casUrl.openConnection();
        loginCasConnection.setRequestProperty("Cookie", jSessionId);
        loginCasConnection.setDoOutput(true);
        loginCasConnection.setInstanceFollowRedirects(true);
        OutputStreamWriter out = new OutputStreamWriter(loginCasConnection.getOutputStream());
        out.write("username=" + username);
        out.write("&password=" + password);
        out.write("&lt=" + lt);
        out.write("&execution=" + execution);
        out.write("&_eventId=submit&submit_btn=LOGIN");
        out.close();
        
        if (loginCasConnection.getHeaderField("Location") == null) {
        	throw new IllegalStateException("The server didn't sent a correct answer. Maybe your login is incorrect.");
        }
            	    
        redirectUrl = new URL(loginCasConnection.getHeaderField("Location"));		
	}

	public String getjSessionId() {
		return jSessionId;
	}

	public void setjSessionId(String jSessionId) {
		this.jSessionId = jSessionId;
	}

	public URL getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(URL redirectUrl) {
		this.redirectUrl = redirectUrl;
	}




}