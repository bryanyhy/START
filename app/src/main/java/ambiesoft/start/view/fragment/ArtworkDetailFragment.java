package ambiesoft.start.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ambiesoft.start.R;
import ambiesoft.start.presenter.fragment.ArtworkDetailFragmentPresenter;

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
        nameText = (TextView) view.findViewById(R.id.nameText);
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
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

}
