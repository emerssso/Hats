package com.emerssso.hats.manage;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.emerssso.hats.R;
import com.emerssso.hats.realm.models.Hat;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * An adapter for a RecyclerView containing a list of Hats
 */
public class HatsAdapter extends RealmBasedRecyclerViewAdapter<Hat, HatsHolder> {

    private final HatsHolder.HatSwitcher switcher;
    @Nullable private Hat currentHat;
    private RealmResults<Hat> hats;

    public HatsAdapter(RealmResults<Hat> hats, @Nullable Hat currentHat, HatsHolder.HatSwitcher switcher, Context context) {
        super(context, hats, true, true);
        this.setHats(hats);
        this.setCurrentHat(currentHat);
        this.switcher = switcher;
    }

    @Override public HatsHolder onCreateRealmViewHolder(ViewGroup parent, int i) {
        return new HatsHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_hat_card, parent, false), switcher);
    }

    @Override public void onBindRealmViewHolder(HatsHolder holder, int position) {
        holder.name.setText(getHats().get(position).getName());
        holder.hat = (getHats().get(position));

        holder.currentIndicator.setDisplayedChild((
                getCurrentHat() != null && getCurrentHat().equals(holder.hat)) ?
                holder.currentIndex : holder.otherIndex);
    }

    @Nullable public Hat getCurrentHat() {
        return currentHat;
    }

    public void setCurrentHat(@Nullable Hat currentHat) {
        this.currentHat = currentHat;
        notifyDataSetChanged();
    }

    public RealmResults<Hat> getHats() {
        return hats;
    }

    public void setHats(RealmResults<Hat> hats) {
        this.hats = hats;
    }
}
