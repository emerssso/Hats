package com.emerssso.hats.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emerssso.hats.AddHatIntentService;
import com.emerssso.hats.DataProvider;
import com.emerssso.hats.HatsApplication;
import com.emerssso.hats.HatsIntents;
import com.emerssso.hats.R;
import com.emerssso.hats.StartWearingHatIntentService;
import com.emerssso.hats.realm.models.Hat;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * A fragment for adding, deleting, and switching between hats
 */
public class ManageHatsFragment extends Fragment implements HatsHolder.HatSwitcher {

    public static final String TAG_ADD_HAT_DIALOG = "addHatDialog";
    private static final String TAG = "ManageHatsFragment";
    @Inject Realm realm;
    @Bind(R.id.hats_list) RealmRecyclerView hatsList;
    private CoordinatorLayout layout;
    private DataProvider dataProvider;
    private HatsAdapter hatsAdapter;
    private RealmChangeListener listener = new RealmChangeListener() {
        @Override public void onChange() {
            if (hatsAdapter != null) {
                hatsAdapter.setCurrentHat(dataProvider.getCurrentHat());
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

        RealmResults<Hat> hats = dataProvider.getAllHats();
        Hat currentHat = dataProvider.getCurrentHat();

        hatsAdapter = new HatsAdapter(hats, currentHat, this, getActivity());
        hatsList.setAdapter(hatsAdapter);

        //hatsList.addItemDecoration(new DividerDecoration(getDividerColor()));

        return layout;
    }

    /**
     * @return The color to use for the divider (using the appropriate API for different
     * android versions
     */
    @ColorInt private int getDividerColor() {
        int dividerColor;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            dividerColor = getResources().getColor(R.color.divider, getActivity().getTheme());
        } else {
            //noinspection deprecation we're using this in the context where it is not deprecated
            dividerColor = getResources().getColor(R.color.divider);
        }
        return dividerColor;
    }

    @Override public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @OnClick(R.id.add_hat) public void showAddHatDialog() {
        new AddHatDialogFragment().show(getFragmentManager(), TAG_ADD_HAT_DIALOG);
    }

    @Override public void onResume() {
        super.onResume();
        realm.addChangeListener(listener);

        hatsAdapter.setCurrentHat(dataProvider.getCurrentHat());
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
        if (hat.equals(hatsAdapter.getCurrentHat())) {
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

}
