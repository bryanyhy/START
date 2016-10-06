package ambiesoft.start.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import ambiesoft.start.R;
import ambiesoft.start.view.activity.MainActivity;

/**
 * Created by Zelta on 7/09/16.
 */
public class AboutUsFragment extends Fragment {

    private NavigationTabBar navigationTabBar;
    private Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aboutus, container, false);

        backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment moreFragment = new MoreFragment();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, moreFragment).commit();

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationTabBar = ((MainActivity) getActivity()).getNavigationTabBar();
        navigationTabBar.hide();
    }
}
