package com.emerssso.hats;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

/**
 * A fragment for adding, deleting, and switching between hats
 */
public class ManageHatsFragment extends Fragment {

    private static final String TAG = "ManageHatsFragment";
    private CoordinatorLayout layout;

    @SuppressLint("InflateParams") //can't use container when attaching fragment to ViewPager
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        layout = (CoordinatorLayout) inflater.inflate(R.layout.fragment_manage_hats, null);

        FloatingActionButton add = (FloatingActionButton) layout.findViewById(R.id.add_hat);

        add.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new AddHatDialogFragment().show(getFragmentManager(), "addHatDialog");
            }
        });

        return layout;
    }

    public void saveWithSnackbar(final String name) {
        Snackbar snackbar = Snackbar.make(
                layout,
                getContext().getString(R.string.snackbar_created_hat, name),
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.discard, new View.OnClickListener() {
            @Override public void onClick(View v) {

            }
        });
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);

                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    Intent intent = new Intent(getContext(), AddHatIntentService.class);
                    intent.putExtra(HatsIntents.EXTRA_HAT_NAME, name);

                    getActivity().startService(intent);
                    Log.d(TAG, "sent " + name + " hat to be created");
                } else {
                    Log.d(TAG, "creation of " + name + " hat canceled");
                }
            }
        });
        snackbar.show();
    }

    public interface Callbacks {
        void saveWithSnackbar(String name);
    }

    public static class AddHatDialogFragment extends DialogFragment {

        @Override @NonNull public Dialog onCreateDialog(Bundle state) {
            final LinearLayout layout = (LinearLayout) LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_add_hat, null);

            TextInputLayout til = (TextInputLayout) layout.findViewById(R.id.hat_name_layout);

            til.setHint(getString(R.string.hat_name));
            final EditText hatName = (EditText) til.findViewById(R.id.field_hat_name);
            hatName.clearFocus();

            return new AlertDialog.Builder(getContext(), R.style.AppDialogTheme)
                    .setTitle(R.string.dialog_add_hat_instructions)
                    .setView(layout)
                    .setPositiveButton(R.string.save_caps, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            final String name = hatName.getText().toString();

                            if (StringUtils.isBlank(name)) {
                                Toast.makeText(getContext(), R.string.empty_hat_name_warning,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                ((Callbacks) getActivity()).saveWithSnackbar(name);
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel_caps, null)
                    .show();
        }
    }
}
