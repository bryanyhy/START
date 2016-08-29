package ambiesoft.start.model.utility;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.view.fragment.PerformanceDetailFragment;

/**
 * Created by Bryanyhy on 17/8/2016.
 */
// Class to setup the recycler view, which is responsible to hold the cardview
public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    ArrayList performanceList;
    Activity activity;

    // Constructor
    public RecyclerViewAdapter(ArrayList list, Activity activity) {
        this.performanceList = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // check if there is performance result
        if (performanceList.size() != 0) {
            // if there is result
            // populate the current row with performance's data, on the RecyclerView as a cardview
            final Performance performance = (Performance) performanceList.get(position);
            holder.name.setText(performance.getName());
            holder.category.setText(performance.getCategory());
            holder.date.setText(performance.getDate());
            holder.time.setText(performance.getsTime() + " - " + performance.geteTime());
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // when the cardview is clicked
                    Log.i("System.out","CV selected");
                    Fragment performanceDetailFragment = new PerformanceDetailFragment();
                    // create bundle, with data added into it
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("performancesDetailFromPreviousFragment", performance);
                    bundle.putInt("previousFragmentID", 0);
                    performanceDetailFragment.setArguments(bundle);
                    // transact to performanceDetailFragment
                    activity.getFragmentManager().beginTransaction().replace(R.id.content_frame, performanceDetailFragment).addToBackStack(null).commit();
                }
            });
        }
    }

    //returns the number of cardview the RecyclerView will display
    @Override
    public int getItemCount() {
        // the number is equal to the number of performance result
        return performanceList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // TODO: card icon

//    public void setCardIcon(String category){
//        switch (category){
//            case "Instruments":
//                ImageView image  = (ImageView)
//                Resources res = getResources(); /** from an Activity */
//                image.setImageDrawable(res.getDrawable(R.drawable.myimage));
//                break;
//            case "Singing":
//
//                break;
//            case "Conjuring":
//
//                break;
//            case "Juggling":
//
//                break;
//            case "Puppetry":
//
//                break;
//            case "Miming":
//
//                break;
//            case "Dancing":
//
//                break;
//            case "Drawing":
//
//                break;
//            default:
//
//                break;
//        }
//    }

//    // Insert a new item to the RecyclerView on a predefined position
//    public void insert(int position, Performance performance) {
//        performanceList.add(position, performance);
//        notifyItemInserted(position);
//    }
//
//    // Remove a RecyclerView item containing a specified Data object
//    public void remove(Performance performance) {
//        int position = performanceList.indexOf(performance);
//        performanceList.remove(position);
//        notifyItemRemoved(position);
//    }

}
