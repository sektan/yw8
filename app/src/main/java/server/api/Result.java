package server.api;

import org.json.JSONObject;

/**
 * Created by dishq on 28-10-2016.
 */

public interface Result {
    void onSuccess(JSONObject response);
    void onError(Object error);
}
