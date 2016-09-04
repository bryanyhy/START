package ambiesoft.start.model.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ambiesoft.start.R;

/**
 * Created by Bryanyhy on 3/9/2016.
 */
public class MoreFragmentAdapter extends ArrayAdapter<String> {

    public MoreFragmentAdapter(Context context, ArrayList list) {
        super(context, R.layout.more_row_layout, list);
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_row_layout, parent, false);
//
//        String listItem = getItem(position);
//
//    }
}
