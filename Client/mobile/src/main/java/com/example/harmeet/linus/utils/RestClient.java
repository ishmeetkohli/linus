package com.example.harmeet.linus.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Ishmeet on 11/04/16.
 */
public class RestClient {

    public String requestContent(String urlString) {

        String result = null;
        URL url = null;
        InputStream in = null;
        try {
            url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            result = convertStreamToString(in);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }

        return sb.toString();
    }
}
