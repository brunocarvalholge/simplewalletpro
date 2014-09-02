package br.com.tolive.simplewalletpro.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.constants.Constants;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.utils.DialogEmailMaker;
import br.com.tolive.simplewalletpro.utils.EntryConverter;

/**
 * Created by bruno.carvalho on 10/07/2014.
 */
public class StoreFragment extends Fragment{

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        ImageView imageMailStore = (ImageView) view.findViewById(R.id.fragment_store_image_email);

        imageMailStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogEmailMaker dialogEmailMaker = new DialogEmailMaker(getActivity());
                dialogEmailMaker.setOnClickOkListener(new DialogEmailMaker.OnClickOkListener() {
                    @Override
                    public void onClickOk(String month, String year) {
                        EntryDAO dao = EntryDAO.getInstance(getActivity());
                        ArrayList<Entry> entries;
                        if (year.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING) && month.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING)) {
                            entries = dao.getEntry(null, null);
                        } else if (year.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING)) {
                            entries = dao.getEntry(month, null);
                        } else if (month.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING)) {
                            entries = dao.getEntry(null, year);
                        } else {
                            entries = dao.getEntry(month, year);
                        }
                        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "[Gastos PRO]" + "[" + month + "]" + "[" + year + "] Relatorio de gastos");
                        intent.putExtra(Intent.EXTRA_TEXT, formatEmailBody(entries));
                        intent.setData(Uri.parse("mailto:")); // or just "mailto:" for blank
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                        startActivity(intent);
                    }
                });
                AlertDialog emailDialog = dialogEmailMaker.makeMailDialog();
                emailDialog.show();
            }
        });

        ImageView imageSdCardStore = (ImageView) view.findViewById(R.id.fragment_store_image_sdcard);

        imageSdCardStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExternalStorageWritable()){
                    try {
                        File newFolder = new File(Environment.getExternalStorageDirectory(), Constants.STORE_FOLDER_NAME);
                        if (!newFolder.exists()) {
                            newFolder.mkdir();
                        }
                        try {
                            //Calendar calendar = Calendar.getInstance();
                            String filename = Constants.STORE_FILE_NAME +
                                    //TODO : Option to store more them 1 file
                                    /*"_" +
                                    calendar.get(Calendar.DAY_OF_MONTH) +
                                    "_" +
                                    (calendar.get(Calendar.MONTH) + 1) +
                                    "_" +
                                    calendar.get(Calendar.YEAR) +*/
                                    Constants.STORE_FILE_EXT;
                            File file = new File(newFolder, filename);
                            if(!file.exists()){
                                file.createNewFile();
                            }
                            EntryDAO dao = EntryDAO.getInstance(getActivity());
                            ArrayList<Entry> entries = dao.getEntry(null, null);
                            String entriesJSON = EntryConverter.toJson(entries);
                            FileOutputStream fos;
                            byte[] data = entriesJSON.getBytes();
                            try {
                                fos = new FileOutputStream(file);
                                fos.write(data);
                                fos.flush();
                                fos.close();
                                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_store_text_sucess), Toast.LENGTH_SHORT).show();
                            } catch (FileNotFoundException e) {
                                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_store_text_fail), Toast.LENGTH_SHORT).show();
                                throw new RuntimeException(e);
                            }
                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_store_text_fail), Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(ex);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_store_text_fail), Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_store_text_fail_open_SD), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private String formatEmailBody(ArrayList<Entry> entries) {
        return entries.toString();
    }
}
