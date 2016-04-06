package edu.utep.cs.cs4330.hw4.model;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by juanrazo on 4/5/16.
 */
public class WebServiceHandler {

    private Coordinates coordinates;
    private boolean isWin = false;
    private boolean isDraw = false;
    private boolean response = false;
    private Coordinates[] winRow = new Coordinates[5];
    private String pid = "";
    private String strategy = "";
    private OmokServer server;

    public WebServiceHandler(){
        coordinates = new Coordinates(1,1);
    }

    public void passCoordinates(int x, int y){
        String url = "http://www.cs.utep.edu/cheon/cs4330/project/omok/play?pid="+pid+"&move="+x+","+y;
        Log.i("URL", url);
        OmokServer coordinates = new OmokServer();
        coordinates.execute(url);
        //http://www.cs.utep.edu/cheon/cs4330/project/omok/play?pid=570498d22d0ec&move=0,5
        //http://www.cs.utep.edu/cheon/cs4330/project/omok/play/?pid=5705256bbe934&move=0,5
    }

    public void setStrategy(String strategy){
        server = new OmokServer();
        this.strategy = "http://www.cs.utep.edu/cheon/cs4330/project/omok/new?strategy="+strategy;
    }

    public void executeStrategy(){
        server = new OmokServer();
        server.execute(strategy);
    }

    public class OmokServer extends AsyncTask<String, Void, String > {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url =new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data!=-1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.i("JSON object", s);
                if(jsonObject.has("response") && jsonObject.has("pid")){
                    String response = jsonObject.getString("response");
                    pid = jsonObject.getString("pid");
                    Log.i("Response", response);
                    Log.i("PID", pid);
                }

                if(jsonObject.has("ack_move")){
                    String ack = jsonObject.getString("ack_move");
                    JSONObject win = new JSONObject(ack);
                    isWin = Boolean.valueOf(win.getString("isWin"));
                    isDraw = Boolean.valueOf(win.getString("isDraw"));
                    // win.getString("row"));
                }
                if(jsonObject.has("move")){
                    String ack = jsonObject.getString("move");
                    JSONObject win = new JSONObject(ack);
                    isWin = Boolean.valueOf(win.getString("isWin"));
                    isDraw = Boolean.valueOf(win.getString("isDraw"));
                    coordinates.setX(Integer.parseInt(win.getString("x")));
                    coordinates.setY(Integer.parseInt(win.getString("y")));
                    Log.i("Coordinates ", ""+coordinates.getX()+", "+coordinates.getY());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //{"response":true,"ack_move":{"x":0,"y":4,"isWin":false,"isDraw":false,"row":[]},"move":{"x":1,"y":2,"isWin":false,"isDraw":false,"row":[]}}

            Log.i("Omok Info content", s);
        }
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public boolean isWin(){
        return isWin;
    }

    public boolean isDraw(){
        return isDraw;
    }

    public String getPid(){
        return pid;
    }
}
