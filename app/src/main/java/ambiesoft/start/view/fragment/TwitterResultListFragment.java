package ambiesoft.start.view.fragment;


import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import ambiesoft.start.R;
import ambiesoft.start.presenter.fragment.TwitterResultListFragmentPresenter;
import ambiesoft.start.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterResultListFragment extends Fragment {

    public Button postTweetButton;
    public ListView tweetList;
    public SwipeRefreshLayout swipeRefreshLayout;

    public TwitterResultListFragmentPresenter presenter;
    private AppBarLayout abl;

    public TwitterResultListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_twitter_result_list, container, false);
        postTweetButton = (Button) view.findViewById(R.id.postTweetButton);
        tweetList = (ListView) view.findViewById(R.id.tweetList);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set backdrop image
        ImageView banner = ((MainActivity) getActivity()).getBackdrop();
        banner.setBackgroundResource(R.drawable.twitter_banner);

        // extend the app bar layout
        abl = ((MainActivity) getActivity()).getAppBarLayout();
        abl.setExpanded(true, true);
        abl.setActivated(true);

        // collapse the app bar layout
        AppBarLayout abl = ((MainActivity) getActivity()).getAppBarLayout();
        abl.setExpanded(false);

        // hide the floating action button in main activity
        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.hide();

        if (presenter == null) {
            presenter = new TwitterResultListFragmentPresenter(this);
        }

        postTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.postTweet();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshTwitterList();
            }
        });
    }
}
