package edu.utep.cs.cs4330.hw4.model;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.utep.cs.cs4330.hw4.view.BoardView;

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
    private String url = "";
    private String json = "";
    private BoardView view;
    public WebServiceHandler(){
        coordinates = new Coordinates(10,10);
    }

    public void passCoordinates(int x, int y,View view) {
        this.view = (BoardView) (BoardView) view;
        OmokServer sendCoordinates = new OmokServer();
        url = "http://www.cs.utep.edu/cheon/cs4330/project/omok/play?pid="+pid+"&move="+x+","+y;
        Log.i("URL", url);
        sendCoordinates.execute(url);

        if(sendCoordinates.getStatus() != AsyncTask.Status.FINISHED){
            // My AsyncTask is done and onPostExecute was called
            Log.i("Omok Set Coor", "" + coordinates.getX() + ", " + coordinates.getY());
            Log.i("json", json);
        }

        response = false;
        //http://www.cs.utep.edu/cheon/cs4330/project/omok/play?pid=570498d22d0ec&move=0,5
        //http://www.cs.utep.edu/cheon/cs4330/project/omok/play/?pid=5705256bbe934&move=0,5
    }

    public void setStrategy(String strategy){
       // server = new OmokServer();
        this.strategy = "http://www.cs.utep.edu/cheon/cs4330/project/omok/new?strategy="+strategy;
        Log.i("Strategy", strategy);
    }

    public void executeStrategy(){

        new OmokServer().execute(strategy);
    }

    public class OmokServer extends AsyncTask<String, Void, String > {

        @Override
        protected String doInBackground(String... params) {
            Log.i("Do Called", "Enter");
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
            Log.i("Do Called", "END");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("PostEx Called", "Enter");
            super.onPostExecute(s);
            json = s;
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
                    Log.i("Omok Coordinates ", "" + coordinates.getX() + ", " + coordinates.getY());
                    response = true;


//                    int x, y;
//                    int stepX, stepY;
//                    stepX = boardView.getWidth() / 9;
//                    stepY = boardView.getHeight() / 9;
//                    x = (int) (event.getX() / stepX);
//                    if (event.getX() % stepX > stepX / 2) {
//                        x++;
//                    }
//                    y = (int) (event.getY() / stepY);
//                    if (event.getY() % stepY > stepY / 2) {
//                        y++;
//                    }
                    long downTime = SystemClock.uptimeMillis();
                    long eventTime = SystemClock.uptimeMillis() + 100;
                    float x = 0.0f;
                    float y = 0.0f;
                    x = (view.getWidth()/9) * Float.parseFloat(win.getString("x"));
                    y = (view.getHeight()/9) * Float.parseFloat(win.getString("y"));
                    Log.i("to float x: ", Float.toString(x));
                    Log.i("to float y: ", Float.toString(y));
                    // List of meta states found here:     developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
                    int metaState = 0;
                    MotionEvent motionEvent = MotionEvent.obtain(
                            downTime,
                            eventTime,
                            MotionEvent.ACTION_UP,
                            x,
                            y,
                            metaState
                    );

                    // Dispatch touch event to view
                    view.dispatchTouchEvent(motionEvent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("PostEx Called", "END");
            //{"response":true,"ack_move":{"x":0,"y":4,"isWin":false,"isDraw":false,"row":[]},"move":{"x":1,"y":2,"isWin":false,"isDraw":false,"row":[]}}
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
