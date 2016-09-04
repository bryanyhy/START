package ambiesoft.start.model.utility;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ambiesoft.start.R;

/**
 * Created by Bryanyhy on 17/8/2016.
 */
// class for the viewholder, which is a cardview to hold the information on performance
public class ViewHolder extends RecyclerView.ViewHolder {

    public CardView cv;
    public TextView name;
    public TextView category;
    public TextView date;
    public TextView time;
    public ImageView cardImg;
    public ImageView portrait;

    public ViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        name = (TextView) itemView.findViewById(R.id.nameView);
        category = (TextView) itemView.findViewById(R.id.categoryView);
        date = (TextView) itemView.findViewById(R.id.dateView);
        time = (TextView) itemView.findViewById(R.id.timeView);
        cardImg = (ImageView) itemView.findViewById(R.id.cardImg);
        portrait = (ImageView) itemView.findViewById(R.id.portrait);
    }
}
