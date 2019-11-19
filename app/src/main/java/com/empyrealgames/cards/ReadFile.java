package com.empyrealgames.cards;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ReadFile  {
    public String readFile(String path){
        StringBuilder answer = new StringBuilder("");
        String ans = "";
        try {
            // Create a URL for the desired page
            URL url = new URL(path);

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String curr;
            while((curr = in.readLine())!=null)
                answer.append(curr);
            ans = answer.toString();
            ans = ans.substring(ans.indexOf('/')+1);
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return ans;
    }

}


