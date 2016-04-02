package com.emerssso.hats.manage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emerssso.hats.R;

import org.apache.commons.lang3.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A DialogFragment allowing the user to fill in the name of a new Hat to be added to the Realm
 */
public class AddHatDialogFragment extends DialogFragment {

    @Bind(R.id.hat_name_layout) TextInputLayout til;
    @Bind(R.id.field_hat_name) EditText hatName;

    @Override @NonNull @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle state) {
        final LinearLayout layout = (LinearLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_add_hat, null);
        ButterKnife.bind(this, layout);

        til.setHint(getString(R.string.hat_name));

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
                            ((ManageHatsFragment.Callbacks) getActivity()).saveWithSnackbar(name);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_caps, null)
                .show();
    }
}
