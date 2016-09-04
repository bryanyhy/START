package ambiesoft.start.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;

import ambiesoft.start.R;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.presenter.fragment.CreatePerformanceFragmentPresenter;

import static ambiesoft.start.model.utility.SoftKeyboard.hideSoftKeyboard;

/**
 * A Fragment for performance creation.
 */
public class CreatePerformanceFragment extends Fragment {

    // for identifying the request involving the usage of Google Place API
    private static final int PLACE_PICKER_REQUEST = 1;

    private CreatePerformanceFragmentPresenter presenter;

    public EditText nameInput;
    public EditText descInput;
    public Spinner categorySpinner;
    public Button createButton;
    public Button locationButton;
    public Button dateButton;
    public Button sTimeButton;
    public Button durationButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_performance, container, false);
        nameInput = (EditText) view.findViewById(R.id.nameInput);
        descInput = (EditText) view.findViewById(R.id.descInput);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        createButton = (Button) view.findViewById(R.id.createButton);
        locationButton = (Button) view.findViewById(R.id.locationButton);
        dateButton = (Button) view.findViewById(R.id.dateButton);
        sTimeButton = (Button) view.findViewById(R.id.sTimeButton);
        durationButton = (Button) view.findViewById(R.id.durationButton);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // hide the floating action button in main activity
        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.hide();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.category_array, android.R.layout.simple_spinner_item);
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

        if (presenter == null) {
            presenter = new CreatePerformanceFragmentPresenter(this);
        }
        createButton.setOnClickListener(new View.OnClickListener() {
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
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hideSoftKeyboard(getActivity());
                presenter.chooseLocation();
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
        sTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hideSoftKeyboard(getActivity());
                presenter.chooseStartTime();
            }
        });
        durationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hideSoftKeyboard(getActivity());
                presenter.chooseDuration();
            }
        });
    }

//    // When a location is selected in Google Place API
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        presenter.getLocationLatAndLng(requestCode, resultCode, data);
//    }

    public void updateLocationInfo(String address, Double lat, Double lng) {
        presenter.setLocationInfo(address, lat, lng);
    }


}
