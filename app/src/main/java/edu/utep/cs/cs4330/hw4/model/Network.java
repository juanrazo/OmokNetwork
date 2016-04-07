package edu.utep.cs.cs4330.hw4.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.security.Permission;

/**
 * Created by juanrazo on 4/5/16.
 */
public class Network extends Player {

    private boolean isStrategy = false;
    private WebServiceHandler webServiceHandler;
    private String pid = "";

    public Network(boolean playerOne) {
        super(playerOne);
        webServiceHandler = new WebServiceHandler();
    }

    protected Network(Parcel in){
        super(in);
    }

    public void smartWebService(){
        webServiceHandler.setStrategy("smart");
        isStrategy = true;
    }

    public void randomWebService(){
        webServiceHandler.setStrategy("random");
    }

    public boolean isStrategy(){
        return isStrategy;
    }

    public Coordinates getCoordinates(){
        return webServiceHandler.getCoordinates();
    }

    public void sendCoordinates(Coordinates coordinates){
        Log.i("PID send", pid);
        webServiceHandler.passCoordinates(coordinates.getX(), coordinates.getY());
    }

    public void startStrategy(){
        webServiceHandler.executeStrategy();
        pid = webServiceHandler.getPid();
        Log.i("Network PID", pid);
    }
    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


    public static final Parcelable.Creator<Network> CREATOR = new Parcelable.Creator<Network>() {
        @Override
        public Network createFromParcel(Parcel in) {
            return new Network(in);
        }

        @Override
        public Network[] newArray(int size) {
            return new Network[size];
        }
    };
}
