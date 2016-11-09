package server.Finder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

/**
 * Created by dishq on 08-11-2016.
 */

public class YearlyLeaderBoardFinder {

    private int yearPoints;
    private int yearRank;
    private String yearUserName;
    private Boolean yearIsCurrentUser;
    private int yearUserId;

    public YearlyLeaderBoardFinder(int yearPoints, int yearRank, String yearUserName, Boolean yearIsCurrentUser,
                                   int yearUserId) {
        this.yearPoints = yearPoints;
        this.yearRank = yearRank;
        this.yearUserName = yearUserName;
        this.yearIsCurrentUser = yearIsCurrentUser;
        this.yearUserId = yearUserId;
    }

    public int getYearPoints() {
        return yearPoints;
    }

    public void setYearPoints(int yearPoints) {
        this.yearPoints = yearPoints;
    }

    public int getYearRank() {
        return yearRank;
    }

    public void setYearRank(int yearRank) {
        this.yearRank = yearRank;
    }

    public String getYearUserName() {
        return yearUserName;
    }

    public void setYearUserName(String yearUserName) {
        this.yearUserName = yearUserName;
    }

    public Boolean getYearIsCurrentUser() {
        return yearIsCurrentUser;
    }

    public void setYearIsCurrentUser(Boolean yearIsCurrentUser) {
        this.yearIsCurrentUser = yearIsCurrentUser;
    }

    public int getYearUserId() {
        return yearUserId;
    }

    public void setYearUserId(int yearUserId) {
        this.yearUserId = yearUserId;
    }

}
