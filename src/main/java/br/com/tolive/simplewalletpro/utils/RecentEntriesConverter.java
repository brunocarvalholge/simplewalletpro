package br.com.tolive.simplewalletpro.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import br.com.tolive.simplewalletpro.constants.Constants;
import br.com.tolive.simplewalletpro.model.Entry;

/**
 * Created by bruno.carvalho on 05/09/2014.
 */
public class RecentEntriesConverter {
    public static final String RECENT = "recent";
    public static final String DESCRIPTION = "description";

    public static String toJson(HashSet<String> recentEntries) {
        try {
            JSONStringer json = new JSONStringer();
            json.object().key(RECENT).array();

            for (String description : recentEntries) {
                json.object().key(DESCRIPTION).value(description).endObject();
            }

            String sJson = json.endArray().endObject().toString();

            return sJson;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashSet<String> fromJson(String recents){
        HashSet<String> recentEntries = new HashSet<String>();
        if(recents.equals(Constants.SP_RECENT_ENTRIES_DEFAULT)){
            return recentEntries;
        }
        try {
            JSONObject json = new JSONObject(recents);
            JSONArray recent = json.getJSONArray(RECENT);
            for (int i = 0; !recent.isNull(i); i++) {
                JSONObject description = recent.getJSONObject(i);
                recentEntries.add(description.getString(DESCRIPTION));
            }
        } catch (JSONException e){
            throw new RuntimeException(e);
        }
        return recentEntries;
    }
}
