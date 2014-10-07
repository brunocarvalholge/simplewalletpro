package br.com.tolive.simplewalletpro.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
public class RecentEntriesManager {
    public static final String RECENT = "recent";
    public static final String DESCRIPTION = "description";

    private Context context;
    private SharedPreferences sharedPreferences;

    public RecentEntriesManager(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

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

    public HashSet<String> getRecents() {
        return RecentEntriesManager.fromJson(sharedPreferences.getString(Constants.SP_KEY_RECENT_ENTRIES, Constants.SP_RECENT_ENTRIES_DEFAULT));
    }

    public void insert(String description) {
        HashSet<String> recentEntry = RecentEntriesManager.fromJson(sharedPreferences.getString(Constants.SP_KEY_RECENT_ENTRIES, Constants.SP_RECENT_ENTRIES_DEFAULT));
        recentEntry.add(description);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SP_KEY_RECENT_ENTRIES, RecentEntriesManager.toJson(recentEntry));
        editor.apply();
    }
}
