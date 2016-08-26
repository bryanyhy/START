package ambiesoft.start.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public TextView categoryText;
    public TextView dateText;
    public TextView timeText;
    public TextView descText;
    private Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_performance_detail, container, false);
        nameText = (TextView) view.findViewById(R.id.nameText);
        categoryText = (TextView) view.findViewById(R.id.categoryText);
        dateText = (TextView) view.findViewById(R.id.dateText);
        timeText = (TextView) view.findViewById(R.id.timeText);
        descText = (TextView) view.findViewById(R.id.descText);
        backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.backToPreviousFragment();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // hide the floating action button in main activity
        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.hide();
        if (presenter == null) {
            presenter = new PerformanceDetailFragmentPresenter(this);
        }
        presenter.getBundleFromPreviousFragment();
    }
}
