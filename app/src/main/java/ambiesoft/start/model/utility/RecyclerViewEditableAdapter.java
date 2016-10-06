package ambiesoft.start.model.utility;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.view.fragment.CreatePerformanceFragment;
import ambiesoft.start.view.fragment.PerformanceDetailFragment;

import static ambiesoft.start.model.utility.FirebaseUtility.deletePerformanceFromFirebase;
import static ambiesoft.start.model.utility.FirebaseUtility.setUserPortraitUri;

/**
 * Created by Bryanyhy on 31/8/2016.
 */
public class RecyclerViewEditableAdapter extends RecyclerView.Adapter<ViewHolderEditable> {

    public ArrayList performanceList;
    public ArrayList performanceListForValidation;
    public Activity activity;
    public int previousFragmentID;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    public Context context;

    // Constructor
    public RecyclerViewEditableAdapter(ArrayList list, Activity activity, int previousFragmentID) {
        this.performanceList = list;
        this.performanceListForValidation = new ArrayList<Performance>(this.performanceList);
        this.activity = activity;
        this.previousFragmentID = previousFragmentID;
        binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public ViewHolderEditable onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_editable, parent, false);
        ViewHolderEditable holder = new ViewHolderEditable(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderEditable holder, final int position) {
        // check if there is performance result
        if (performanceList.size() != 0) {
            // if there is result
            // populate the current row with performance's data, on the RecyclerView as a cardview
            final Performance performance = (Performance) performanceList.get(position);
            setUserPortraitUri(performance.getEmail(), context, holder.portrait);
            holder.name.setText(performance.getName());
            holder.category.setText(performance.getCategory());
            holder.date.setText(performance.getDate());
            holder.time.setText(performance.getsTime() + " - " + performance.geteTime());
            setCardIcon(holder, performance.getCategory());
            binderHelper.bind(holder.swipeLayout, performance.getName());
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // when the cardview is clicked
                    Log.i("System.out", "Edit is clicked, " + performance.getName());
                    performanceListForValidation.remove(position);
                    editPerformance(performance, performanceListForValidation);
                }
            });
            holder.editPer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("System.out", "Edit is clicked, " + performance.getName());
                    performanceListForValidation.remove(position);
                    editPerformance(performance, performanceListForValidation);
                }
            });

            holder.deletePer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("System.out", "Delete is clicked");
                    deletePerformance(performance);
//                mDataSet.remove(getAdapterPosition());
//                notifyItemRemoved(getAdapterPosition());
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

    public void setCardIcon(ViewHolderEditable holder, String category){
        int imageID = 0;
        switch (category){
            case "Instruments":
                imageID = R.drawable.ic_card_instrument;
                break;
            case "Singing":
                imageID = R.drawable.ic_card_sing;
                break;
            case "Conjuring":
                imageID = R.drawable.ic_card_conjuring;
                break;
            case "Juggling":
                imageID = R.drawable.ic_card_juggling;
                break;
            case "Puppetry":
                imageID = R.drawable.ic_card_puppetry;
                break;
            case "Miming":
                imageID = R.drawable.ic_card_miming;
                break;
            case "Dancing":
                imageID = R.drawable.ic_card_dancing;
                break;
            case "Drawing":
                imageID = R.drawable.ic_card_drawing;
                break;
            default:
                imageID = R.drawable.ic_card_other;
                break;
        }
        holder.cardImg.setImageResource(imageID);
    }

    public void editPerformance(Performance performance, ArrayList pList) {
        Fragment createPerformanceFragment = new CreatePerformanceFragment();
        // create bundle, add all performance information into it
        Bundle bundle = new Bundle();
        bundle.putParcelable("performancesDetailFromPreviousFragment", performance);
        bundle.putParcelableArrayList("performanceListFromPreviousFragment", pList);
        createPerformanceFragment.setArguments(bundle);
        // pass bundle to the new createPerformanceFragment
        activity.getFragmentManager().beginTransaction().replace(R.id.content_frame, createPerformanceFragment).commit();
    }

    public void deletePerformance(final Performance performance) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Confirmation");
        alert.setMessage("Are you sure to delete performance?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deletePerformanceFromFirebase(performance.getKey());
                Toast.makeText(activity, "Performance is deleted.", Toast.LENGTH_SHORT);
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
