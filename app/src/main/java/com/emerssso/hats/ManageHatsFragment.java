package com.emerssso.hats;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.emerssso.hats.realm.models.Hat;

import org.apache.commons.lang3.StringUtils;

import io.realm.Realm;
import io.realm.RealmResults;

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
        ViewSwitcher switcher = (ViewSwitcher) layout.findViewById(R.id.switcher);

        FloatingActionButton add = (FloatingActionButton) layout.findViewById(R.id.add_hat);

        add.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new AddHatDialogFragment().show(getFragmentManager(), "addHatDialog");
            }
        });

        RecyclerView hatsList = (RecyclerView) layout.findViewById(R.id.hats_list);
        hatsList.setHasFixedSize(false);
        hatsList.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO: take this offline or use RealmAdapter
        long start = SystemClock.currentThreadTimeMillis();
        RealmResults<Hat> hats = Realm.getDefaultInstance().where(Hat.class).findAll();
        long end = SystemClock.currentThreadTimeMillis();
        Log.d(TAG, "online realm query took " + (end - start) + " millis");

        hatsList.setAdapter(new HatsAdapter(hats));

        if (hats != null && hats.size() > 0) {
            switcher.showNext();
        }

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

    public void putOnHat(final Hat hat) {
        final long wearMillis = System.currentTimeMillis();

        Snackbar snackbar = Snackbar.make(layout,
                getContext().getString(R.string.putting_on_hat, hat.getName()),
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.cancel_caps, new View.OnClickListener() {
            @Override public void onClick(View v) {

            }
        });

        snackbar.setCallback(new Snackbar.Callback() {
            @Override public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);

                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    Intent intent = new Intent(getContext(), StartWearingHatIntentService.class);
                    intent.putExtra(HatsIntents.EXTRA_HAT_NAME, hat.getName());
                    intent.putExtra(HatsIntents.EXTRA_START_MILLIS, wearMillis);
                    getActivity().startService(intent);

                    Log.d(TAG, "put on " + hat.getName() + " hat");
                } else {
                    Log.d(TAG, "canceled putting on " + hat.getName());
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

    public class HatsAdapter extends RecyclerView.Adapter<HatsHolder> {

        RealmResults<Hat> hats;

        public HatsAdapter(RealmResults<Hat> hats) {
            this.hats = hats;
        }

        @Override
        public HatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HatsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_hat_card, parent, false));
        }

        @Override public void onBindViewHolder(HatsHolder holder, int position) {
            holder.name.setText(hats.get(position).getName());
            holder.hat = (hats.get(position));
            holder.stroke.setVisibility(position == hats.size() - 1 ? View.GONE : View.VISIBLE);
        }

        @Override public int getItemCount() {
            return hats != null ? hats.size() : 0;
        }
    }

    public class HatsHolder extends RecyclerView.ViewHolder {
        @NonNull
        final public TextView name;
        @NonNull
        final public View button;
        @NonNull
        final public View stroke;
        @Nullable
        public Hat hat;

        public HatsHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.hat_name);
            button = itemView;
            stroke = itemView.findViewById(R.id.divider);

            button.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (hat != null) {
                        putOnHat(hat);
                    }
                }
            });
        }
    }
}
