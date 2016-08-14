package ambiesoft.start.Fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ambiesoft.start.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GreetingFragment extends Fragment {


    public GreetingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_greeting, container, false);
    }

}
