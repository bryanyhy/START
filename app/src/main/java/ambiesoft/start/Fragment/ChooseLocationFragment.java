package ambiesoft.start.fragment;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ambiesoft.start.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ChooseLocationFragment extends Fragment {


    public ChooseLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_location, container, false);
    }

}
