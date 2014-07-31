package br.com.tolive.simplewalletpro.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.adapter.CustomSpinnerAdapter;
import br.com.tolive.simplewalletpro.views.CustomTextView;

/**
 * Created by bruno.carvalho on 10/07/2014.
 */
public class DialogEmailMaker {
    public static final int SPINNER_SELECTED_DEFAULT = 0;
    public static final String SPINNER_SELECTED_DEFAULT_STRING = "0";
    private OnClickOkListener mListener;
    private Context context;
    AlertDialog dialog;

    public DialogEmailMaker(Context context){
        this.context = context;
    }

    public AlertDialog makeAddDialog(){
        this.dialog = makeCustomAddDialog();
        setDialog(dialog);
        return dialog;
    }

    private void setDialog(AlertDialog dialog){
        this.dialog = dialog;
    }

    /**
     * After call this method, call setDialog to update dialog reference
     * so the button don't get NullPointeException.
     *
     * @return custom AlertDialog
     */
    private AlertDialog makeCustomAddDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_email, null);

        final Spinner spinnerMonth = (Spinner) view.findViewById(R.id.dialog_email_spinner_month);
        final Spinner spinnerYear = (Spinner) view.findViewById(R.id.dialog_email_spinner_year);

        String[] months = context.getResources().getStringArray(R.array.spinner_email_months);
        String[] years = context.getResources().getStringArray(R.array.spinner_email_years);

        CustomSpinnerAdapter adapterMonth = new CustomSpinnerAdapter(context, R.layout.simple_spinner_item, months);
        adapterMonth.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapterMonth);
        spinnerMonth.setSelection(SPINNER_SELECTED_DEFAULT);

        CustomSpinnerAdapter adapterYear = new CustomSpinnerAdapter(context, R.layout.simple_spinner_item, years);
        adapterYear.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);
        spinnerYear.setSelection(SPINNER_SELECTED_DEFAULT);

        CustomTextView okButton = (CustomTextView) view.findViewById(R.id.dialog_email_text_ok);
        CustomTextView cancelButton = (CustomTextView) view.findViewById(R.id.dialog_email_text_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mListener != null){
                    if(spinnerYear.getSelectedItemPosition() == SPINNER_SELECTED_DEFAULT){
                        mListener.onClickOk(String.valueOf(spinnerMonth.getSelectedItemPosition()), SPINNER_SELECTED_DEFAULT_STRING);
                    } else {
                        mListener.onClickOk(String.valueOf(spinnerMonth.getSelectedItemPosition()), (String) spinnerYear.getSelectedItem());
                    }
                }

                DialogEmailMaker.this.dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogEmailMaker.this.dialog.cancel();
            }
        });

        dialog.setView(view);
        return dialog.create();
    }

    public void setOnClickOkListener (OnClickOkListener listener){
        mListener = listener;
    }

    public interface OnClickOkListener {
        public void onClickOk(String month, String year);
    }
}
