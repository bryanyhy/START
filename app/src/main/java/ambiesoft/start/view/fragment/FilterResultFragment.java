package ambiesoft.start.view.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.text.ParseException;

import ambiesoft.start.R;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.presenter.fragment.FilterResultFragmentPresenter;

import static ambiesoft.start.model.utility.SoftKeyboard.hideSoftKeyboard;

/**
 * Class for filtering performance result
 */
public class FilterResultFragment extends Fragment {

    // The previous called fragment's ID
    // 0 is HomeFragment, 1 is GoogleMapFragment
    private static final int HOME_FRAGMENT = 0;
    private static final int MAP_FRAGMENT = 1;

    private FilterResultFragmentPresenter presenter;
    private AppBarLayout abl;

    public EditText keywordInput;
    private Spinner categorySpinner;
    public Button filterButton;
    public Button dateButton;
    public Button timeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_result, container, false);
        keywordInput = (EditText) view.findViewById(R.id.keywordInput);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        filterButton = (Button) view.findViewById(R.id.filterButton);
        dateButton = (Button) view.findViewById(R.id.dateButton);
        timeButton = (Button) view.findViewById(R.id.timeButton);
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
        banner.setBackgroundResource(R.drawable.banner_searching);

        // hide the floating action button in main activity
        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.hide();
        if (presenter == null) {
            presenter = new FilterResultFragmentPresenter(this);
        }
        presenter.getBundleFromPreviousFragment();
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    hideSoftKeyboard(getActivity());
                    presenter.submit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    hideSoftKeyboard(getActivity());
                    presenter.chooseDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hideSoftKeyboard(getActivity());
                presenter.chooseTime();
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.performance_category_array_for_filter, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setSelectedCategory(parent, position);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    // setup the menu items on the top action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
