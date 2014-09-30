package br.com.tolive.simplewalletpro.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.app.AddRecurrentService;
import br.com.tolive.simplewalletpro.app.MenuActivity;
import br.com.tolive.simplewalletpro.constants.Constants;
import br.com.tolive.simplewalletpro.model.Entry;

/**
 * Created by bruno.carvalho on 08/09/2014.
 */
public class RecurrentsManager {
    public static final int RECURRENT_NORMAL = 0;
    public static final int RECURRENT_DAILY = 1;
    public static final int RECURRENT_MONTHY = 2;

    private static final int ALARM_REPEAT_TIME_DAYLY = (60 * 60 * 24) * 1000;

    private static final String KEY_LIST = "list";

    private Context context;
    private SharedPreferences sharedPreferences;

    public RecurrentsManager(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public ArrayList<Entry> getRecurrentDaily(){
        String jRecurrentDaily = sharedPreferences.getString(Constants.SP_KEY_RECURRENT_DAILY, Constants.SP_RECURRENT_DAILY_DEFAULT);
        if(jRecurrentDaily.equals(Constants.SP_RECURRENT_DAILY_DEFAULT)){
            return new ArrayList<Entry>();
        } else {
            return fromJson(jRecurrentDaily);
        }
    }

    public ArrayList<Entry> getRecurrentMonthly(){
        String jRecurrentMonthly = sharedPreferences.getString(Constants.SP_KEY_RECURRENT_MONTHLY, Constants.SP_RECURRENT_MONTHLY_DEFAULT);
        if(jRecurrentMonthly.equals(Constants.SP_RECURRENT_MONTHLY_DEFAULT)){
            return new ArrayList<Entry>();
        } else {
            return fromJson(jRecurrentMonthly);
        }
    }

    private void saveRecurrentDaily(ArrayList<Entry> recurrentsDaily){
        String json = toJson(recurrentsDaily);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SP_KEY_RECURRENT_DAILY, json);
        editor.apply();
    }

    private void saveRecurrentMonthly(ArrayList<Entry> recurrentsMonthly){
        String json = toJson(recurrentsMonthly);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SP_KEY_RECURRENT_MONTHLY, json);
        editor.apply();
    }

    private static String toJson(ArrayList<Entry> entries) {
        try {
            JSONStringer json = new JSONStringer();
            json.object().key(KEY_LIST).array().object().key(Entry.ENTITY_NAME).array();

            for (Entry entry : entries) {
                json.object().key(Entry.ID).value(entry.getId())
                        .key(Entry.DESCRIPTION).value(entry.getDescription())
                        .key(Entry.VALUE).value(String.format("%.2f",entry.getValue()))
                        .key(Entry.TYPE).value(entry.getType())
                        .key(Entry.CATEGORY).value(entry.getCategory())
                        .key(Entry.DATE).value(entry.getDate())
                        .key(Entry.MONTH).value(entry.getMonth())
                        .endObject();
            }

            String sJson = json.endArray().endObject().endArray().endObject().toString();

            return sJson;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Entry> fromJson(String jEntries){
        ArrayList<Entry> recurrentList = new ArrayList<Entry>();
        try {
            JSONObject json = new JSONObject(jEntries);
            JSONArray list = json.getJSONArray(EntryConverter.LIST);
            JSONArray entries = list.getJSONObject(0).getJSONArray(Entry.ENTITY_NAME);
            for(int i = 0; !entries.isNull(i); i++){
                Entry entry = new Entry();
                JSONObject jEntry = entries.getJSONObject(i);

                entry.setId(jEntry.getLong(Entry.ID));
                entry.setDescription(jEntry.getString(Entry.DESCRIPTION));
                entry.setValue(Float.parseFloat(jEntry.getString(Entry.VALUE).replace(',','.')));
                entry.setType(jEntry.getInt(Entry.TYPE));
                entry.setCategory(jEntry.getInt(Entry.CATEGORY));
                entry.setDate(jEntry.getString(Entry.DATE));
                entry.setMonth(jEntry.getInt(Entry.MONTH));

                recurrentList.add(entry);
            }
            //Toast.makeText(this.context, this.context.getResources().getString(R.string.fragment_recovery_text_sucess), Toast.LENGTH_SHORT).show();
        } catch (JSONException e){
            throw new RuntimeException(e);
        }
        return recurrentList;
    }

    public int getRecurrency(Entry entry) {
        if(isDaily(entry)){
            return RECURRENT_DAILY;
        } else if (isMonthy(entry)){
            return RECURRENT_MONTHY;
        } else{
         return RECURRENT_NORMAL;
        }
    }

    private boolean isDaily(Entry entry) {
        ArrayList<Entry> recurrentsDaily = getRecurrentDaily();
        return recurrentsDaily.contains((Entry) entry);
    }

    private boolean isMonthy(Entry entry) {
        ArrayList<Entry> recurrentsMonthly = getRecurrentMonthly();
        return recurrentsMonthly.contains((Entry) entry);
    }

    public void insert(Entry entry, int recurrency) {

        if(recurrency == RECURRENT_DAILY){
            ArrayList<Entry> recurrentsDaily = getRecurrentDaily();
            recurrentsDaily.add(entry);
            saveRecurrentDaily(recurrentsDaily);
            createAlarm(entry, recurrency);
            return ;
        }

        if (recurrency == RECURRENT_MONTHY) {
            ArrayList<Entry> recurrentsMonthly = getRecurrentMonthly();
            recurrentsMonthly.add(entry);
            saveRecurrentMonthly(recurrentsMonthly);
            createAlarm(entry, recurrency);
            return ;
        }

        //createAlarm(entry, recurrency);
    }

    public void createAlarm(Entry entry, int recurrency) {
        //getting current time and set to 7:00 AM set an interval
        Calendar cal = Calendar.getInstance();
        String entryDate = entry.getDate();
        String[] entryDateArray = entryDate.split("/");
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        Log.d("TESTE", "currentDay: " + currentDay + " currentMonth: " + currentMonth + " currentYear: " + currentYear);
        Log.d("TESTE", "cal.Day: " + entryDateArray[0] + " cal.Month: " + entryDateArray[1] + " cal.Year: " + entryDateArray[2]);

        if(currentDay == Integer.valueOf(entryDateArray[0]) && currentMonth == (Integer.valueOf(entryDateArray[1]) - 1) && currentYear == Integer.valueOf(entryDateArray[2])) {
            Log.d("TESTE", "currentDate == entryDate");
            switch (recurrency) {
                case RECURRENT_DAILY:
                    //TODO something if user set a past date
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    break;
                case RECURRENT_MONTHY:
                    cal.add(Calendar.MONTH, 1);
                    break;
            }
        }

        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.HOUR, 7);
        cal.set(Calendar.MINUTE, 0);
        Log.d("TESTE", "[RecurrentMa] \ncal seted: " + cal.toString() + "\nrecurrency: " + recurrency);

       setAlarm(entry, recurrency, cal);
    }

    public void setAlarm(Entry entry, int recurrency){
        setAlarm(entry, recurrency, null);
    }

    public void setAlarm(Entry entry, int recurrency, Calendar calendar){
        switch (recurrency) {
            case RECURRENT_DAILY:
                if(calendar == null) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 7);
                    calendar.set(Calendar.HOUR, 7);
                    calendar.set(Calendar.MINUTE, 0);
                }
                registerAlarm(entry, recurrency, calendar);
                Log.d("TESTE", "[RecurrentMa][DAILY] alarm seted, entry: " + entry.toString());
                break;
            case RECURRENT_MONTHY:
                if(calendar == null) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.MONTH, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 7);
                    calendar.set(Calendar.HOUR, 7);
                    calendar.set(Calendar.MINUTE, 0);
                }
                registerAlarm(entry, recurrency, calendar);
                Log.d("TESTE", "[RecurrentMa][MONTHLY] alarm seted, entry: " + entry.toString());
                break;
        }
    }

    private void registerAlarm(Entry entry, int recurrency, Calendar calendar) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent ii = new Intent(context, AddRecurrentService.class);
        entry.setDate(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
                + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/"
                + String.valueOf(calendar.get(Calendar.YEAR)));
        entry.setMonth(calendar.get(Calendar.MONTH));
        ii.putExtra(AddRecurrentService.EXTRA_ENTRY, entry);
        ii.putExtra(AddRecurrentService.EXTRA_RECURRENCY, recurrency);
        PendingIntent pii = PendingIntent.getService(context, entry.getId().intValue(), ii,
                PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pii);
    }

    public void remove(Entry entry){
        int recurrency = RECURRENT_DAILY;
        ArrayList<Entry> recurrentsDaily = getRecurrentDaily();
        if(recurrentsDaily.contains((Entry) entry)){
            if(recurrentsDaily.remove((Entry) entry)){
                saveRecurrentDaily(recurrentsDaily);
                removeAlarm(entry, recurrency);
                return;
            } else {
                //Should trows an exception, because tried to remove an recurrent entry but its not a recurrent one
            }
        }

        ArrayList<Entry> recurrentsMonthly = getRecurrentMonthly();
        if(recurrentsMonthly.contains((Entry) entry)){
            recurrency = RECURRENT_MONTHY;
            if(recurrentsMonthly.remove((Entry) entry)){
                saveRecurrentMonthly(recurrentsMonthly);
                removeAlarm(entry, recurrency);
                return;
            } else {
                //Should trows an exception, because tried to remove an recurrent entry but its not a recurrent one
            }
        }
    }

    private void removeAlarm(Entry entry, int recurrency) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent ii = new Intent(context, AddRecurrentService.class);
        ii.putExtra(AddRecurrentService.EXTRA_ENTRY, entry);
        ii.putExtra(AddRecurrentService.EXTRA_RECURRENCY, recurrency);
        PendingIntent pii = PendingIntent.getService(context, entry.getId().intValue(), ii,
                PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pii);
        Log.d("TESTE", "Alarm Removed");
    }

    public void edit(Entry entry, int recurrency){
        if(recurrency == RECURRENT_DAILY) {
            ArrayList<Entry> recurrentsDaily = getRecurrentDaily();
            int index = recurrentsDaily.indexOf((Entry) entry);
            if (index != -1) {
                removeAlarm(entry, recurrency);
                createAlarm(entry, recurrency);
                recurrentsDaily.set(index, entry);
                saveRecurrentDaily(recurrentsDaily);
            }
        }

        else if (recurrency == RECURRENT_MONTHY) {
            ArrayList<Entry> recurrentsMonthly = getRecurrentMonthly();
            int index = recurrentsMonthly.indexOf((Entry) entry);
            if (index != -1) {
                removeAlarm(entry, recurrency);
                createAlarm(entry, recurrency);
                recurrentsMonthly.set(index, entry);
                saveRecurrentMonthly(recurrentsMonthly);
            }
        }

        //Should trows an exception, because tried to edit an recurrent entry but its not a recurrent one
    }
}
