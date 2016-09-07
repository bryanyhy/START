package ambiesoft.start.view.fragment;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ambiesoft.start.R;
import ambiesoft.start.view.activity.LogOnActivity;
import ambiesoft.start.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {

    public TextView logout,about,privacy,version,help;
    private AppBarLayout abl;
    //private FragmentManager fm;


    private FirebaseAuth mFirebaseAuth;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        //this.fm = view().getFragmentManager();
        logout = (TextView) view.findViewById(R.id.tvLogOut);
        about = (TextView) view.findViewById(R.id.tvAbout);
        privacy = (TextView) view.findViewById(R.id.tvPrivacy);
        version = (TextView) view.findViewById(R.id.tvVersion);
        help = (TextView) view.findViewById(R.id.tvHelp);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // extend the app bar layout
        abl = ((MainActivity) getActivity()).getAppBarLayout();
        abl.setExpanded(true, true);
        abl.setActivated(true);

        // hide the floating action button in main activity
        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.hide();

        //set backdrop image
        ImageView banner = ((MainActivity) getActivity()).getBackdrop();
        banner.setBackgroundResource(R.drawable.register_banner);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment helpFragment = new HelpFragment();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, helpFragment).addToBackStack(null).commit();
            }
        });
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment versionFragment = new VersionFragment();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, versionFragment).addToBackStack(null).commit();

            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment privacyFragment = new PrivacyFragment();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, privacyFragment).addToBackStack(null).commit();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment aboutFragment = new AboutUsFragment();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, aboutFragment).addToBackStack(null).commit();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), LogOnActivity.class);
                startActivity(intent);
            }
        });
    }



}
