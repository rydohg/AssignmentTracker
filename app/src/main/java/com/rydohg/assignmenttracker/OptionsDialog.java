package com.rydohg.assignmenttracker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OptionsDialog extends DialogFragment {

    public static OptionsDialog newInstance(String title, ArrayList<String> options, int pos){
        OptionsDialog ourInstance = new OptionsDialog();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArrayList("options", options);
        args.putInt("pos", pos);
        ourInstance.setArguments(args);

        return ourInstance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ArrayList<String> options = getArguments().getStringArrayList("options");
        builder.setTitle(getArguments().getString("title"))
                .setItems(options.toArray(new String[options.size()]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.w("OnClick", options.get(i));
                                ListView listView = (ListView) OptionsDialog.this.getActivity().findViewById(R.id.options_list_view);
                                android.view.View v = listView.getAdapter().getView(OptionsDialog.this.getArguments().getInt("pos"),
                                        listView.getChildAt(OptionsDialog.this.getArguments().getInt("pos")), listView);
                                ((TextView) v.findViewById(R.id.selectedOption))
                                        .setText(options.get(i));
                            }
                        }
                );
        return builder.create();
    }
}
