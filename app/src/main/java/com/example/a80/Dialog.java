package com.example.a80;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {
    private String Title;
    private String Dialog;

    public Dialog(String title, String dialog) {
        Title = title;
        Dialog = dialog;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        builder.setTitle(Title)
                .setMessage(Dialog)
                .setPositiveButton("הבנתי", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
return builder.create();
    }
}
/*
this class open dialog by the parameters that i give him and show the descriptions of definitions
 */