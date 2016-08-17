package ambiesoft.start.utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ambiesoft.start.R;
import ambiesoft.start.dataclass.Performance;

/**
 * Created by Bryanyhy on 17/8/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    ArrayList list;
//    List<Performance> list = Collections.emptyList();
    Context context;

    public RecyclerViewAdapter(ArrayList list, Context context) {
        this.list = list;
        this.context = context;
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

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        Performance performance = (Performance) list.get(position);
        holder.name.setText(performance.getName());
        holder.category.setText(performance.getCategory());
        holder.date.setText(performance.getDate());
        holder.time.setText(performance.getsTime() + " - " + performance.geteTime());


//        holder.imageView.setImageResource(list.get(position).imageId);

        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Performance performance) {
        list.add(position, performance);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Performance performance) {
        int position = list.indexOf(performance);
        list.remove(position);
        notifyItemRemoved(position);
    }



}
