package tw.tcnr03.m220t;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class TransTask extends AsyncTask<String, Void, String> {
        String ans;

    @Override
    protected String doInBackground(String... params) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(params[0]);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            String line = in.readLine();
            while (line != null) {
                sb.append(line);
                line = in.readLine();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ans = sb.toString();
        //------------
        return ans;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        parseJson(s);
    }

    private void parseJson(String s) {
    }
}