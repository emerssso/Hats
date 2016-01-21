package com.emerssso.hats;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * A fragment for adding, deleting, and switching between hats
 */
public class ManageHatsFragment extends Fragment {

    private static final String TAG = "ManageHatsFragment";
    @Inject Realm realm;
    @Bind(R.id.switcher) ViewSwitcher switcher;
    @Bind(R.id.hats_list) RecyclerView hatsList;
    private CoordinatorLayout layout;
    private DataProvider dataProvider;
    private HatsAdapter hatsAdapter;
    private RealmChangeListener listener = new RealmChangeListener() {
        @Override public void onChange() {
            if (hatsAdapter != null) {
                hatsAdapter.currentHat = dataProvider.getCurrentHat();
                hatsAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DataProvider) {
            this.dataProvider = (DataProvider) context;
        } else {
            Log.e(TAG, "ManageHatsFragment used by activity which does not implement DataProvider");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        layout = (CoordinatorLayout) inflater.inflate(
                R.layout.fragment_manage_hats, container, false);
        ButterKnife.bind(this, layout);
        HatsApplication.getApplicationComponent(getActivity().getApplication()).inject(this);

        hatsList.setHasFixedSize(false);
        hatsList.setLayoutManager(new LinearLayoutManager(getContext()));

        RealmResults<Hat> hats = dataProvider.getAllHats();
        Hat currentHat = dataProvider.getCurrentHat();

        hatsAdapter = new HatsAdapter(hats, currentHat);
        hatsList.setAdapter(hatsAdapter);

        if (hats != null && hats.size() > 0) {
            switcher.showNext();
        }

        return layout;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.add_hat) public void showAddHatDialog() {
        new AddHatDialogFragment().show(getFragmentManager(), "addHatDialog");
    }

    @Override public void onResume() {
        super.onResume();
        realm.addChangeListener(listener);

        hatsAdapter.currentHat = dataProvider.getCurrentHat();
        hatsAdapter.notifyDataSetChanged();
    }

    @Override public void onPause() {
        super.onPause();
        realm.removeChangeListener(listener);
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

    public void switchHat(@NonNull final Hat hat) {
        if (hat.equals(hatsAdapter.currentHat)) {
            removeHat(hat);
        } else {
            putOnHat(hat);
        }
    }

    private void removeHat(@NonNull Hat hat) {
        final long wearMillis = System.currentTimeMillis();

        Snackbar snackbar = Snackbar.make(layout,
                getContext().getString(R.string.removing_hat, hat.getName()),
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.cancel_caps, new View.OnClickListener() {
            @Override public void onClick(View v) {

            }
        });

        snackbar.setCallback(new Snackbar.Callback() {
            @Override public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);

                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    //TODO: deal with case where callback is called after fragment is detached
                    Intent intent = new Intent(getContext(), StartWearingHatIntentService.class);
                    intent.putExtra(HatsIntents.EXTRA_HAT_NAME, Hat.NO_HAT_NAME);
                    intent.putExtra(HatsIntents.EXTRA_START_MILLIS, wearMillis);
                    getActivity().startService(intent);

                    Log.d(TAG, "clearing current hat");
                } else {
                    Log.d(TAG, "canceled clearing current hat");
                }
            }
        });
        snackbar.show();
    }

    public void putOnHat(@NonNull final Hat hat) {
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
                    //TODO: deal with case where callback is called after fragment is detached
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
                                ((Callbacks) getActivity()).saveWithSnackbar(name);
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel_caps, null)
                    .show();
        }
    }

    public class HatsAdapter extends RecyclerView.Adapter<HatsHolder> {

        @Nullable Hat currentHat;
        RealmResults<Hat> hats;

        public HatsAdapter(RealmResults<Hat> hats, @Nullable Hat currentHat) {
            this.hats = hats;
            this.currentHat = currentHat;
        }

        @Override
        public HatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HatsHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_hat_card, parent, false));
        }

        @Override public void onBindViewHolder(HatsHolder holder, int position) {
            holder.name.setText(hats.get(position).getName());
            holder.hat = (hats.get(position));

            holder.currentIndicator.setDisplayedChild((
                    currentHat != null && currentHat.equals(holder.hat)) ?
                    holder.currentIndex : holder.otherIndex);

            //holder.stroke.setVisibility(position == hats.size() - 1 ? View.GONE : View.VISIBLE);
        }

        @Override public int getItemCount() {
            return hats != null ? hats.size() : 0;
        }
    }

    public class HatsHolder extends RecyclerView.ViewHolder {
        @NonNull final public View button;
        @Nullable public Hat hat;
        public int currentIndex;
        public int otherIndex;
        @Bind(R.id.hat_name) TextView name;
        @Bind(R.id.divider) View stroke;
        @Bind(R.id.current_indicator) ViewSwitcher currentIndicator;

        public HatsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            button = itemView;

            currentIndex = currentIndicator.indexOfChild(currentIndicator.findViewById(R.id.current));
            otherIndex = currentIndicator.indexOfChild(currentIndicator.findViewById(R.id.other));

            button.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (hat != null) {
                        switchHat(hat);
                    }
                }
            });
        }
    }
}
