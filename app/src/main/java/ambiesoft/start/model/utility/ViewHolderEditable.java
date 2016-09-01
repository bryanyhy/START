package ambiesoft.start.model.utility;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;

/**
 * Created by Bryanyhy on 31/8/2016.
 */
public class ViewHolderEditable extends RecyclerView.ViewHolder {

    public CardView cv;
    public TextView name;
    public TextView category;
    public TextView date;
    public TextView time;
    public ImageView cardImg;
    public SwipeRevealLayout swipeLayout;
    public LinearLayout deleteLayout;
    public TextView editPer;
    public TextView deletePer;

    public ViewHolderEditable(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        name = (TextView) itemView.findViewById(R.id.nameView);
        category = (TextView) itemView.findViewById(R.id.categoryView);
        date = (TextView) itemView.findViewById(R.id.dateView);
        time = (TextView) itemView.findViewById(R.id.timeView);
        cardImg = (ImageView) itemView.findViewById(R.id.cardImg);
        swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
        deleteLayout = (LinearLayout) itemView.findViewById(R.id.delete_layout);
        editPer = (TextView) itemView.findViewById(R.id.editPer);
        deletePer = (TextView) itemView.findViewById(R.id.deletePer);
    }
}
