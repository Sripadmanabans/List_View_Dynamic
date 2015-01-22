package com.example.sripadmanaban.listviewdynamic;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Place to hold all the data in position
 * Created by Sripadmanaban on 12/11/2014.
 */
public class Games implements Parcelable
{
    private int gameId;
    private String gameName;
    private String developerName;

    public void setGameId(int gameId)
    {
        this.gameId = gameId;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public void setDeveloperName(String developerName)
    {
        this.developerName = developerName;
    }

    public int getGameId()
    {
        return gameId;
    }

    public String getGameName()
    {
        return gameName;
    }

    public String getDeveloperName()
    {
        return developerName;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(gameId);
        dest.writeString(gameName);
        dest.writeString(developerName);
    }

    @Override
    public String toString()
    {
        return gameName;
    }
}
