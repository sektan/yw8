package com.dishq.buzz.server.Finder;

/**
 * Created by tania on 04-11-2016.
 * property of dishq
 */

public class BuzzTypeDataFinder {
    private int buzzTypeId;
    private String buzzTypeLabel;

    public BuzzTypeDataFinder(int buzzTypeId, String buzzTypeLabel) {
        this.buzzTypeId = buzzTypeId;
        this.buzzTypeLabel = buzzTypeLabel;
    }

    public int getBuzzTypeId() {
        return buzzTypeId;
    }

    public void setBuzzTypeId(int buzzTypeId) {
        this.buzzTypeId = buzzTypeId;
    }

    public String getBuzzTypeLabel() {
        return buzzTypeLabel;
    }

    public void setBuzzTypeLabel(String buzzTypeLabel) {
        this.buzzTypeLabel = buzzTypeLabel;
    }
}
