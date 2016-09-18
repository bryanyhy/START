package ambiesoft.start.model.utility;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ambiesoft.start.R;

/**
 * Created by Bryanyhy on 18/9/2016.
 */
public class ViewHolderBusker extends RecyclerView.ViewHolder {

    public CardView cv;
    public TextView name;
    public ImageView portrait;

    public ViewHolderBusker(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        name = (TextView) itemView.findViewById(R.id.nameView);
        portrait = (ImageView) itemView.findViewById(R.id.portrait);
    }
}
