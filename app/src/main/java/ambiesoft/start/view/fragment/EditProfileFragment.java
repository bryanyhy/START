package ambiesoft.start.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ambiesoft.start.R;
import ambiesoft.start.presenter.fragment.EditProfileFragmentPresenter;
import ambiesoft.start.presenter.fragment.ProfileFragmentPresenter;
import ambiesoft.start.view.activity.MainActivity;

import static ambiesoft.start.model.utility.SoftKeyboard.hideSoftKeyboard;

/**
 * Created by Bryanyhy on 20/9/2016.
 */
public class EditProfileFragment extends Fragment {

    public TextView updatePortrait, username;
    public EditText userCategory, userDesc;
    public ImageView portrait;
    private Button saveBtn, backBtn;

    private AppBarLayout abl;
    private EditProfileFragmentPresenter presenter;

    public EditProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        updatePortrait = (TextView) view.findViewById(R.id.updatePortrait);
        username = (TextView) view.findViewById(R.id.username);
        userCategory = (EditText) view.findViewById(R.id.userCategory);
        userDesc = (EditText) view.findViewById(R.id.userDesc);
        portrait = (ImageView) view.findViewById(R.id.portrait);
        saveBtn = (Button) view.findViewById(R.id.saveBtn);
        backBtn = (Button) view.findViewById(R.id.backBtn);
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
            presenter = new EditProfileFragmentPresenter(this);
        }

        updatePortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hideSoftKeyboard(getActivity());
                presenter.updatePortrait();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hideSoftKeyboard(getActivity());
                presenter.saveChanges();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hideSoftKeyboard(getActivity());
                presenter.backToPreviousFragment();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.getPortraitUri(data);
    }

}
