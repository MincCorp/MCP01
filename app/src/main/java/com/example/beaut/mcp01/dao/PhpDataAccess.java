package com.example.beaut.mcp01.dao;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by beaut on 2017-02-12.
 */

public class PhpDataAccess extends AsyncTask<String, Void, String> {

    private String url = "http://52.78.216.195/datafiles/";

    @Override
    protected String doInBackground(String... params) {

        // String uri = params[0];
        String dataFilePath = params[0];
        String psmt = params[1];

        String uri = url + dataFilePath;

        BufferedReader bufferedReader = null;
        try {

            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            Map<Object, Object> parameters = new LinkedHashMap<Object, Object>();
            parameters.put("psmt",psmt);

            String data = addQueryStringToUrlString(parameters);

            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);

            DataOutputStream dos = null;
            dos = new DataOutputStream(con.getOutputStream());
            dos.writeBytes(data);
            dos.flush();
            dos.close();

            InputStream is = con.getInputStream();
            Scanner scan = new Scanner(is);

            int line = 1;
            StringBuilder sb = new StringBuilder();

            while(scan.hasNext()) {
                sb.append(scan.nextLine()+"\n");
                //System.out.println((line++) + " : " + scan.nextLine());
            }
            scan.close();

            return sb.toString().trim();

        }catch(Exception e){
            return null;
        }

    }

    @Override
    protected void onPostExecute(String result){

    }

    String addQueryStringToUrlString(final Map<Object, Object> parameters) throws UnsupportedEncodingException {
        String returnStr = "";
        if(parameters!= null) {
            for(Map.Entry<Object, Object> parameter : parameters.entrySet()) {
                final String encodedKey = URLEncoder.encode(parameter.getKey().toString(),"UTF-8");
                final String encodedValue = URLEncoder.encode(parameter.getValue().toString(),"UTF-8");
                if(returnStr.equals("")) {
                    returnStr += encodedKey + "=" + encodedValue;
                } else {
                    returnStr += "&" + encodedKey + "=" + encodedValue;
                }
            }
        }
        return returnStr;
    }

}
