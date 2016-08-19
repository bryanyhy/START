package ambiesoft.start.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;

import ambiesoft.start.R;
import ambiesoft.start.dataclass.Performance;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerformanceDetailFragment extends Fragment {

    private TextView nameText;
    private TextView categoryText;
    private TextView dateText;
    private TextView timeText;
    private TextView descText;
    private Button backButton;

    private Performance selectedPerformance;
    private int previousFragment;
    private String filterDate;
    private String filterKeyword;
    private String filterCategory;
    private String filterTime;

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
                back();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("performancesDetailFromPreviousFragment")) {
                selectedPerformance = bundle.getParcelable("performancesDetailFromPreviousFragment");
            }
            if (bundle.containsKey("filterDate")) {
                filterDate = bundle.getString("filterDate");
            }
            if (bundle.containsKey("filterKeyword")) {
                filterKeyword = bundle.getString("filterKeyword");
            }
            if (bundle.containsKey("filterCategory")) {
                filterCategory = bundle.getString("filterCategory");
            }
            if (bundle.containsKey("filterTime")) {
                filterTime = bundle.getString("filterTime");
            }
            if (bundle.containsKey("previousFragment")) {
                previousFragment = bundle.getInt("previousFragment");
            }
            nameText.setText(selectedPerformance.getName());
            categoryText.setText(selectedPerformance.getCategory());
            dateText.setText(selectedPerformance.getDate());
            timeText.setText(selectedPerformance.getsTime() + " - " + selectedPerformance.geteTime());
            descText.setText(selectedPerformance.getDesc());
        }
    }

    public void back() {
        if (previousFragment == 0) {
            getFragmentManager().popBackStack();
        } else {
            Fragment googleMapFragment = new GoogleMapFragment();
            Bundle bundle = new Bundle();
            bundle.putString("dateFromFilter", filterDate);
            bundle.putString("keywordFromFilter", filterKeyword);
            bundle.putString("categoryFromFilter", filterCategory);
            bundle.putString("timeFromFilter", filterTime);
            googleMapFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, googleMapFragment).commit();
        }
    }
}
