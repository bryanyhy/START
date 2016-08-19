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
    private Button backButton;

    private Performance selectedPerformance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_performance_detail, container, false);
        nameText = (TextView) view.findViewById(R.id.nameText);
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
            nameText.setText(selectedPerformance.getName());
        }
    }

    public void back() {
        getFragmentManager().popBackStackImmediate();
    }
}
