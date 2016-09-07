package ambiesoft.start.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
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

    public TextView logout;
    private AppBarLayout abl;


    private FirebaseAuth mFirebaseAuth;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        logout = (TextView) view.findViewById(R.id.tvLogOut);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // extend the app bar layout
        abl = ((MainActivity) getActivity()).getAppBarLayout();
        abl.setExpanded(true, true);
        abl.setActivated(true);

        //set backdrop image
        ImageView banner = ((MainActivity) getActivity()).getBackdrop();
        banner.setBackgroundResource(R.drawable.register_banner);

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
