package server.Finder;

/**
 * Created by dishq on 03-11-2016.
 */

public class SimilarRestInfoFinder {
    public String[] similarRestCuisine;
    public String similarRestAddr;
    public String similarRestName;
    public String similarRestId;
    public String similarRestIsOpenOn;
    public String[] similarRestType;

    public SimilarRestInfoFinder(String[] similarRestCuisine, String similarRestAddr, String similarRestName, String similarRestId,
                                 String similarRestIsOpenOn, String[] similarRestType) {
        this.similarRestCuisine = similarRestCuisine;
        this.similarRestAddr = similarRestAddr;
        this.similarRestName = similarRestName;
        this.similarRestId = similarRestId;
        this.similarRestIsOpenOn = similarRestIsOpenOn;
        this.similarRestType = similarRestType;
    }

    public String[] getSimilarRestCuisine() {
        return similarRestCuisine;
    }

    public void setSimilarRestCuisine(String[] similarRestCuisine) {
        this.similarRestCuisine = similarRestCuisine;
    }

    public String getSimilarRestAddr() {
        return similarRestAddr;
    }

    public void setSimilarRestAddr(String similarRestAddr) {
        this.similarRestAddr = similarRestAddr;
    }

    public String getSimilarRestName() {
        return similarRestName;
    }

    public void setSimilarRestName(String similarRestName) {
        this.similarRestName = similarRestName;
    }

    public String getSimilarRestId() {
        return similarRestId;
    }

    public void setSimilarRestId(String similarRestId) {
        this.similarRestId = similarRestId;
    }

    public String getSimilarRestIsOpenOn() {
        return similarRestIsOpenOn;
    }

    public void setSimilarRestIsOpenOn(String similarRestIsOpenOn) {
        this.similarRestIsOpenOn = similarRestIsOpenOn;
    }

    public String[] getSimilarRestType() {
        return similarRestType;
    }

    public void setSimilarRestType(String[] similarRestType) {
        this.similarRestType = similarRestType;
    }
}
