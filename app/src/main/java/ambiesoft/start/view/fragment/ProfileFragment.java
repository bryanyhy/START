package ambiesoft.start.view.fragment;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.model.utility.RecyclerViewAdapter;
import ambiesoft.start.presenter.fragment.ProfileFragmentPresenter;
import ambiesoft.start.view.activity.MainActivity;

/**
 * Created by Zelta on 5/09/16.
 */
public class ProfileFragment extends Fragment {


    public TextView buskerName, buskerHashtag, buskerCategory, buskerDesc, editProfile;
    public ImageView portrait;
    public Button backButton;
    public LinearLayout buskerPerformance;
    public RecyclerView recyclerView;

    private AppBarLayout abl;

    private ProfileFragmentPresenter presenter;
    public RecyclerViewAdapter adapter;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        buskerName = (TextView) view.findViewById(R.id.profileBuskerName);
        buskerHashtag = (TextView) view.findViewById(R.id.buskerHashtag);
        buskerCategory = (TextView) view.findViewById(R.id.buskerCategory);
        buskerDesc = (TextView) view.findViewById(R.id.buskerDesc);
        buskerPerformance = (LinearLayout) view.findViewById(R.id.busker_per_content);
        editProfile = (TextView) view.findViewById(R.id.editProfile);
        backButton = (Button) view.findViewById(R.id.backButton);
        recyclerView = (RecyclerView) view.findViewById(R.id.perRecyclerView);
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

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.editProfile();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.backToPreviousFragment();
            }
        });
    }

    // setup the menu items on the top action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
