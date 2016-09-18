package ambiesoft.start.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import ambiesoft.start.R;
import io.fabric.sdk.android.Fabric;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterFragment extends Fragment {

    private static final String HASHTAG = "#STARTinMelb";

    public TwitterLoginButton loginButton;
    public Button postTweetButton, getTweetButton;

    public TwitterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_twitter, container, false);
        loginButton = (TwitterLoginButton) view.findViewById(R.id.loginButton);
        postTweetButton = (Button) view.findViewById(R.id.postTweetButton);
        getTweetButton = (Button) view.findViewById(R.id.getTweetButton);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        addTweetButton.setVisibility(View.INVISIBLE);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                Log.i("System.out", "Login success.");

                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                Log.i("System.out", "Token: " + token);
                Log.i("System.out", "Secret: " + secret);
                postTweetButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                showAlertBox("Error", "Twitter login failed.", TwitterFragment.this.getActivity());
            }
        });

        postTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postTweet();
            }
        });
        getTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllTweet();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void postTweet() {
        TweetComposer.Builder builder = new TweetComposer.Builder(this.getActivity())
                .text("#STARTinMelb");
        builder.show();
    }

    public void getAllTweet() {
        this.getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new TwitterResultListFragment()).addToBackStack(null).commit();
    }

}
