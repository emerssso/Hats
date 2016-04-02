package com.emerssso.hats.manage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.emerssso.hats.R;
import com.emerssso.hats.realm.models.Hat;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmViewHolder;

/**
 * A ViewHolder for a RecyclerView containing a list of Hats
 */
public class HatsHolder extends RealmViewHolder {
    @NonNull final public View button;
    private final HatSwitcher switcher;
    @Nullable public Hat hat;
    public int currentIndex;
    public int otherIndex;
    @Bind(R.id.hat_name) TextView name;
    @Bind(R.id.current_indicator) ViewSwitcher currentIndicator;

    public HatsHolder(@NonNull View itemView, HatSwitcher hatSwitcher) {
        super(itemView);

        this.switcher = hatSwitcher;

        ButterKnife.bind(this, itemView);
        button = itemView;

        currentIndex = currentIndicator.indexOfChild(currentIndicator.findViewById(R.id.current));
        otherIndex = currentIndicator.indexOfChild(currentIndicator.findViewById(R.id.other));

        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (hat != null) {
                    switcher.switchHat(hat);
                }
            }
        });
    }

    /**
     * Interface allowing the holder to indicate that a Hat should be put on/removed (switched)
     */
    public interface HatSwitcher {
        /**
         * Switch the passed hat
         *
         * @param hat Hat to be switched (put on or taken off)
         */
        void switchHat(Hat hat);
    }
}
