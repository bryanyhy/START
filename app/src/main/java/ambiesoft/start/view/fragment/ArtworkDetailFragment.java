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
import android.widget.TextView;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import ambiesoft.start.R;
import ambiesoft.start.presenter.fragment.ArtworkDetailFragmentPresenter;
import ambiesoft.start.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtworkDetailFragment extends Fragment {

    private ArtworkDetailFragmentPresenter presenter;

    public TextView nameText;
    public TextView assetText;
    public TextView creatorText;
    public TextView yearText;
    public TextView locText;
    private Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artwork_detail, container, false);
        nameText = (TextView) view.findViewById(R.id.nameInput);
        assetText = (TextView) view.findViewById(R.id.assetText);
        creatorText = (TextView) view.findViewById(R.id.creatorText);
        yearText = (TextView) view.findViewById(R.id.yearText);
        locText = (TextView) view.findViewById(R.id.locText);
        backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                backToPreviousFragment();
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // hide the floating action button in main activity
        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.hide();

        NavigationTabBar navigationTabBar = ((MainActivity) getActivity()).getNavigationTabBar();
        navigationTabBar.show();
        navigationTabBar.setBehaviorEnabled(true);

        if (presenter == null) {
            presenter = new ArtworkDetailFragmentPresenter(this);
        }
        presenter.getBundleFromPreviousFragment();
        presenter.setTextView();
    }

    // Method called when user clicked the "back" button
    public void backToPreviousFragment() {
        presenter.backToPreviousFragment();
    }

    // setup the menu items on the top action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

}
