package ambiesoft.start.view.fragment;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ambiesoft.start.R;
import ambiesoft.start.presenter.fragment.ProfileFragmentPresenter;
import ambiesoft.start.view.activity.MainActivity;

/**
 * Created by Zelta on 5/09/16.
 */
public class ProfileFragment extends Fragment {


    public TextView buskerName;
    public ImageView portrait;

    private AppBarLayout abl;

    private ProfileFragmentPresenter presenter;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        buskerName = (TextView) view.findViewById(R.id.profileBuskerName);
        portrait = (ImageView) view.findViewById(R.id.profilePortrait);

        return view;
        
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // extend the app bar layout
        abl = ((MainActivity) getActivity()).getAppBarLayout();
        abl.setExpanded(true, true);
        abl.setActivated(true);

        //set backdrop image
        ImageView banner = ((MainActivity) getActivity()).getBackdrop();
        banner.setBackgroundResource(R.drawable.banner_performancelist);

        // hide the floating action button in main activity
        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.hide();

        if (presenter == null) {
            presenter = new ProfileFragmentPresenter(this);
        }

    }

    // setup the menu items on the top action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
