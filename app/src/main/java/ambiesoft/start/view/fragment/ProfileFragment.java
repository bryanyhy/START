package ambiesoft.start.view.fragment;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ambiesoft.start.R;

/**
 * Created by Zelta on 5/09/16.
 */
public class ProfileFragment extends Fragment {
    public TextView userName;
    public Image profilePic;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
        
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
