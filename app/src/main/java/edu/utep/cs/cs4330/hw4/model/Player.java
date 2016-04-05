package edu.utep.cs.cs4330.hw4.model;

/**
 * Created by juanrazo on 4/5/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Player implements Parcelable {
    private char stone;
    private boolean playerOne;

    public Player(boolean playerOne) {
        this.playerOne = playerOne;
        if (this.playerOne) {
            stone = 'B';
        } else {
            stone = 'W';
        }
    }
    protected Player(Parcel in) {
        playerOne = in.readByte() != 0;
        stone = (char)in.readInt();
    }

    public boolean isPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(boolean playerOne) {
        this.playerOne = playerOne;
    }

    public char getStone() {
        return stone;
    }

    public void setStone(char stone) {
        this.stone = stone;
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
        dest.writeByte((byte) (playerOne ? 1 : 0));
        dest.writeInt(stone);
    }
}
