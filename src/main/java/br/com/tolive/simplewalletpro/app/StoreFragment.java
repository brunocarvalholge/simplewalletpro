package br.com.tolive.simplewalletpro.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.utils.DialogEmailMaker;

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
                AlertDialog emailDialog = dialogEmailMaker.makeAddDialog();
                emailDialog.show();
            }
        });

        return view;
    }

    private String formatEmailBody(ArrayList<Entry> entries) {
        return entries.toString();
    }
}
