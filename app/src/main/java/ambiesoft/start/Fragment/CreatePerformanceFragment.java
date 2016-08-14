package ambiesoft.start.Fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ambiesoft.start.R;

import static ambiesoft.start.Utility.AlertBox.showAlertBox;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePerformanceFragment extends Fragment{

    private boolean isStartup = true;
    private String selectedCategory;

    private EditText nameInput;
    private EditText descInput;
    private Spinner categorySpinner;
    private Button nextButton;


    private static final String CATEGORY_TYPE[] = {"Instrument", "Dance", "Magic"};

    public CreatePerformanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_performance, container, false);
        nameInput = (EditText) view.findViewById(R.id.nameInput);
        descInput = (EditText) view.findViewById(R.id.descInput);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                submit();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                selectedCategory = parent.getItemAtPosition(position).toString();
                if (isStartup) {
                    isStartup = false;
                } else {
                    Toast.makeText(getActivity(), selectedCategory + " is selected.", Toast.LENGTH_SHORT).show();
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void submit() {
        String name = nameInput.getText().toString();
        String desc = descInput.getText().toString();
        if (name.trim().matches("") || desc.trim().matches("")) {
            // check if any empty field
            showAlertBox("No empty field is allowed.", getActivity());
        } else {
            Toast.makeText(getActivity(), "All input are valid", Toast.LENGTH_SHORT).show();
        }
    }

}
