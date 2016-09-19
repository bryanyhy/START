package ambiesoft.start.presenter.fragment;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import ambiesoft.start.R;
import ambiesoft.start.view.fragment.PostTweetFragment;
import ambiesoft.start.view.fragment.TwitterResultListFragment;

/**
 * Created by Bryanyhy on 17/9/2016.
 */
public class TwitterResultListFragmentPresenter {

    private static final String HASHTAG = "#STARTinMelb";
//    #STARTinMelb

    private TwitterResultListFragment view;
    private SearchTimeline searchTimeline;
    private TweetTimelineListAdapter adapter;
//    private CustomTweetTimelineListAdapter adapter;

    public TwitterResultListFragmentPresenter(TwitterResultListFragment view) {
        this.view = view;
        setRefreshListSize(getScreenSize());
        twitterResultListSetup();
    }

    public void twitterResultListSetup() {
        searchTimeline = new SearchTimeline.Builder()
                .query(HASHTAG)
                .build();

//        final UserTimeline userTimeline = new UserTimeline.Builder()
//                .screenName("Ambie_Soft")
//                .build();

        adapter = new TweetTimelineListAdapter.Builder(view.getActivity())
                .setTimeline(searchTimeline)
                .build();
        view.tweetList.setAdapter(adapter);
        view.tweetList.setNestedScrollingEnabled(true);

//        adapter = new CustomTweetTimelineListAdapter(view.getActivity(), searchTimeline);
//        view.tweetList.setAdapter(adapter);
//        view.tweetList.setNestedScrollingEnabled(true);
    }

    public int getScreenSize() {
        Display display = view.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

    public void setRefreshListSize(int height) {
        // get layout parameters for that view
        ViewGroup.LayoutParams params1 = view.tweetList.getLayoutParams();
        ViewGroup.LayoutParams params2 = view.swipeRefreshLayout.getLayoutParams();
        ViewGroup.LayoutParams params3 = view.postTweetButton.getLayoutParams();

        // change height of the params
        params1.height = height - 60 - 56 - params3.height;
        params2.height = height - 60 - 56 - params3.height;

        // initialize new parameters for my element
        view.tweetList.setLayoutParams(new LinearLayout.LayoutParams(params1));
        view.swipeRefreshLayout.setLayoutParams(new LinearLayout.LayoutParams(params2));
    }

    public void postTweet() {
        view.getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new PostTweetFragment(), "PostTweetFragment").commit();
    }

    public void refreshTwitterList() {
        view.swipeRefreshLayout.setRefreshing(true);
        adapter.refresh(new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                view.swipeRefreshLayout.setRefreshing(false);
                Log.i("System.out", "update succ");
                Toast.makeText(view.getContext(), "Twitter result refresh successes.", Toast.LENGTH_SHORT);
            }

            @Override
            public void failure(TwitterException exception) {
                // Toast or some other action
                Log.i("System.out", "update fail");
                Toast.makeText(view.getContext(), "Twitter result refresh fails.", Toast.LENGTH_SHORT);
            }
        });
    }
}


// ref: https://gist.github.com/sabadow/a7dd2575325b676a113e
/**
 * Custom Adapter to overrides view onClickListener
 */
class CustomTweetTimelineListAdapter extends TweetTimelineListAdapter {

    public CustomTweetTimelineListAdapter(Context context, Timeline<Tweet> timeline) {
        super(context, timeline);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        //disable subviews
        if(view instanceof ViewGroup){
            disableViewAndSubViews((ViewGroup) view);
        }

        //enable root view and attach custom listener
        view.setEnabled(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tweet tweet = getItem(position);
                String username = tweet.user.name;
                Log.i("System.out", "username: " + username);
                if (tweet.coordinates != null) {
                    Double lat = tweet.coordinates.getLatitude();
                    Double lng = tweet.coordinates.getLongitude();
                    Log.i("System.out", "geo: " + lat + ", " + lng);
                }
                String text = tweet.text;
                String tweetId = "click tweetId:"+getItemId(position);
                Toast.makeText(context, tweetId, Toast.LENGTH_SHORT).show();
                Log.i("System.out", "text: " + text);
            }
        });
        return view;
    }

    private void disableViewAndSubViews(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disableViewAndSubViews((ViewGroup) child);
            } else {
                child.setEnabled(false);
                child.setClickable(false);
                child.setLongClickable(false);
            }
        }
    }

}