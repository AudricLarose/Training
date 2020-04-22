package com.example.testentoutgenre;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import static android.content.ContentValues.TAG;

public class ExampleDialo extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private CheckBox choix_1;
    private CheckBox choix_2;
    private ExampleDialogListener listener;
    private String check;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        final View view =inflater.inflate(R.layout.layout, null);
        builder.setView(view)
                .setTitle("exemple")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }

                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String username= editTextUsername.getText().toString();
                        String password= editTextPassword.getText().toString();

                        if (choix_1.isChecked()){
                            String check= choix_1.getText().toString();
                            listener.applyTexts(username,password,check);
                        }
                        if (choix_2.isChecked()){
                            String check=choix_2.getText().toString();
                            listener.applyTexts(username,password,check);
                        }
                        Log.d(TAG, "onClick: "+ check);
                    }
                });
        editTextUsername = (EditText) view.findViewById(R.id.Edite_nm);
        editTextPassword = (EditText) view.findViewById(R.id.Edite_pwd);
        choix_1=(CheckBox) view.findViewById(R.id.checkBox1);
        choix_2=(CheckBox) view.findViewById(R.id.checkBox2);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener= (ExampleDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface ExampleDialogListener{
        void applyTexts(String username, String mdp , String C1);
    }
}
