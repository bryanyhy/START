package ambiesoft.start.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ambiesoft.start.R;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.presenter.fragment.PerformanceDetailFragmentPresenter;

/**
 *  Class for showing the performance detail which user selected
 */
public class PerformanceDetailFragment extends Fragment {

    private PerformanceDetailFragmentPresenter presenter;

    public TextView nameText;
    public TextView buskerName;
    public TextView categoryText;
    public TextView dateText;
    public TextView timeText;
    public TextView descText;
    public TextView locText;
    private Button postTweetButton, backButton;
    public ImageView portrait;

    private AppBarLayout abl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_performance_detail, container, false);
        nameText = (TextView) view.findViewById(R.id.nameInput);
        buskerName = (TextView) view.findViewById(R.id.buskerName);
        categoryText = (TextView) view.findViewById(R.id.categoryText);
        dateText = (TextView) view.findViewById(R.id.dateText);
        timeText = (TextView) view.findViewById(R.id.timeText);
        descText = (TextView) view.findViewById(R.id.descText);
        locText = (TextView) view.findViewById(R.id.locText);
        portrait = (ImageView) view.findViewById(R.id.portrait);
        postTweetButton = (Button) view.findViewById(R.id.postTweetButton);
        postTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.postTweet();
            }
        });
        backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.backToPreviousFragment();
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
            presenter = new PerformanceDetailFragmentPresenter(this);
        }
        presenter.getBundleFromPreviousFragment();
    }

    // setup the menu items on the top action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
