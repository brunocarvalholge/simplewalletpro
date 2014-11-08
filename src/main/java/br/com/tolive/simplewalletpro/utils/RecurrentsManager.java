package br.com.tolive.simplewalletpro.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewalletpro.app.AddRecurrentService;
import br.com.tolive.simplewalletpro.constants.Constants;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Entry;

/**
 * Created by bruno.carvalho on 08/09/2014.
 */
public class RecurrentsManager {
    public static final int RECURRENT_NONE = -1;
    public static final int RECURRENT_NORMAL = 0;
    public static final int RECURRENT_DAILY = 1;
    public static final int RECURRENT_MONTHY = 2;

    private static final String KEY_LIST = "list";
    public static final int NOT_FOUND = -1;
    public static final int NOT_INSERTED = -1;

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

        createAlarm(entry, recurrency);
    }

    public void createAlarm(Entry entry, int recurrency) {
        EntryDAO dao = EntryDAO.getInstance(context);
        //getting current time and set to 7:00 AM set an interval
        Calendar cal = Calendar.getInstance();
        long currentTime = cal.getTimeInMillis();

        String entryDate = entry.getDate();
        String[] entryDateArray = entryDate.split("/");
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(entryDateArray[0]));
        cal.set(Calendar.MONTH, Integer.valueOf(entryDateArray[1]) - 1);
        cal.set(Calendar.YEAR, Integer.valueOf(entryDateArray[2]));
        Log.d("TESTE", "currentDay: " + currentDay + " currentMonth: " + currentMonth + " currentYear: " + currentYear);
        Log.d("TESTE", "cal.Day: " + cal.get(Calendar.DAY_OF_MONTH) + " cal.Month: " + cal.get(Calendar.MONTH) + " cal.Year: " + cal.get(Calendar.YEAR));

        long inserted = NOT_INSERTED;
        if(currentDay == cal.get(Calendar.DAY_OF_MONTH) && currentMonth == cal.get(Calendar.MONTH) && currentYear == cal.get(Calendar.YEAR)) {
            Log.d("TESTE", "currentDate == entryDate");
            //currentDate == entryDate we need to add the entry for today and them set alarm for the future
            inserted = dao.insert(entry);
            switch (recurrency) {
                case RECURRENT_DAILY:
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    break;
                case RECURRENT_MONTHY:
                    cal.add(Calendar.MONTH, 1);
                    break;
                default:
                    return;
            }
        }

        cal.set(Calendar.HOUR, 7);
        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);

        long addTime = cal.getTimeInMillis();
        if(addTime < currentTime){
            //If its a past date we need to add the entry before we set the alarm
            //This can produce an issue and not add the entries from the past date to current one :/
            //if(inserted == NOT_INSERTED) {
            //    dao.insert(entry);
            //}
            //Do not need to add service if its a past date
            //if(recurrency == RECURRENT_NORMAL){
                Log.d("TESTE", "add past entry and return");
            //    return;
            //}
            //Log.d("TESTE", "add past entry and set alarm");
        }

        Log.d("TESTE", "[RecurrentMa] \ncal seted: " + cal.toString() + "\nrecurrency: " + recurrency);

       setAlarm(entry, recurrency, cal);
    }

    public void setAlarm(Entry entry, int recurrency){
        setAlarm(entry, recurrency, null);
    }

    public void setAlarm(Entry entry, int recurrency, Calendar calendar){
        switch (recurrency) {
            case RECURRENT_NORMAL:
                registerAlarm(entry, recurrency, calendar);
                Log.d("TESTE", "[RecurrentMa][NORMAL] alarm seted, entry: " + entry.toString());
                break;
            case RECURRENT_DAILY:
                if(calendar == null) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR, 7);
                    calendar.set(Calendar.HOUR_OF_DAY, 7);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.AM_PM, Calendar.AM);
                }
                registerAlarm(entry, recurrency, calendar);
                Log.d("TESTE", "[RecurrentMa][DAILY] alarm seted, entry: " + entry.toString());
                break;
            case RECURRENT_MONTHY:
                if(calendar == null) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.MONTH, 1);
                    calendar.set(Calendar.HOUR, 7);
                    calendar.set(Calendar.HOUR_OF_DAY, 7);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.AM_PM, Calendar.AM);
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
        if(contains(recurrentsDaily, entry) != NOT_FOUND){
            if(removeEntry(recurrentsDaily, entry)){
                saveRecurrentDaily(recurrentsDaily);
                removeAlarm(entry, recurrency);
                return;
            } else {
                //Should trows an exception, because tried to remove an recurrent entry but its not a recurrent one
            }
        }

        ArrayList<Entry> recurrentsMonthly = getRecurrentMonthly();
        if(contains(recurrentsMonthly, entry) != NOT_FOUND){
            recurrency = RECURRENT_MONTHY;
            if(removeEntry(recurrentsMonthly, entry)){
                saveRecurrentMonthly(recurrentsMonthly);
                removeAlarm(entry, recurrency);
                return;
            } else {
                //Should trows an exception, because tried to remove an recurrent entry but its not a recurrent one
            }
        }
    }

    private boolean removeEntry(ArrayList<Entry> recurrent, Entry entry) {
        int size = recurrent.size();
        for(int i = 0; i < size; i++){
            if(recurrent.get(i).getId().equals(entry.getId())){
                recurrent.remove(i);
                if(recurrent.size() < size) {
                    return true;
                }
            }
        }
        return false;
    }

    private int contains(ArrayList<Entry> recurrentsDaily, Entry entry) {
        for(Entry temp : recurrentsDaily){
            if(temp.getId().equals(entry.getId())){
                return entry.getId().intValue();
            }
        }
        return NOT_FOUND;
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
            int index = contains(recurrentsDaily, entry);
            if (index != NOT_FOUND) {
                removeAlarm(entry, recurrency);
                createAlarm(entry, recurrency);
                recurrentsDaily.set(index, entry);
                saveRecurrentDaily(recurrentsDaily);
            }
        }

        else if (recurrency == RECURRENT_MONTHY) {
            ArrayList<Entry> recurrentsMonthly = getRecurrentMonthly();
            int index = contains(recurrentsMonthly, entry);
            if (index != NOT_FOUND) {
                removeAlarm(entry, recurrency);
                createAlarm(entry, recurrency);
                recurrentsMonthly.set(index, entry);
                saveRecurrentMonthly(recurrentsMonthly);
            }
        }

        //Should trows an exception, because tried to edit an recurrent entry but its not a recurrent one
    }
}
