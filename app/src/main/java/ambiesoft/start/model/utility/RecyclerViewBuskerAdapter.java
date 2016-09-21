package ambiesoft.start.model.utility;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.model.dataclass.User;
import ambiesoft.start.view.fragment.PerformanceDetailFragment;
import ambiesoft.start.view.fragment.ProfileFragment;

import static ambiesoft.start.model.utility.FirebaseUtility.setUserPortraitUri;

/**
 * Created by Bryanyhy on 18/9/2016.
 */
public class RecyclerViewBuskerAdapter extends RecyclerView.Adapter<ViewHolderBusker> {

    public ArrayList buskerList;
    public Activity activity;
    public int previousFragmentID;
    public Context context;

    // Constructor
    public RecyclerViewBuskerAdapter(ArrayList list, Activity activity, int previousFragmentID) {
        this.buskerList = list;
        this.activity = activity;
        this.previousFragmentID = previousFragmentID;
    }

    @Override
    public ViewHolderBusker onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_busker, parent, false);
        ViewHolderBusker holder = new ViewHolderBusker(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBusker holder, int position) {
        // check if there is performance result
        if (buskerList.size() != 0) {
            // if there is result
            // populate the current row with performance's data, on the RecyclerView as a cardview
            final User busker = (User) buskerList.get(position);
            setUserPortraitUri(busker.getEmail(), context, holder.portrait);
            holder.name.setText(busker.getUsername());
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // when the cardview is clicked
                    // if the calling fragment is HomeFragment
                    Log.i("System.out","CV selected");
                    Fragment profileFragment = new ProfileFragment();
                    // create bundle, with data added into it
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("buskerDetailFromPreviousFragment", busker);
                    bundle.putInt("previousFragmentID", previousFragmentID);
                    profileFragment.setArguments(bundle);
                    // transact to performanceDetailFragment
                    activity.getFragmentManager().beginTransaction().replace(R.id.content_frame, profileFragment).commit();
                }
            });
        }
    }

    //returns the number of cardview the RecyclerView will display
    @Override
    public int getItemCount() {
        // the number is equal to the number of performance result
        return buskerList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
