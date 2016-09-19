package ambiesoft.start.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import ambiesoft.start.R;
import ambiesoft.start.presenter.fragment.PostTweetFragmentPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostTweetFragment extends Fragment {

    private PostTweetFragmentPresenter presenter;

    public EditText tweetContent;
    public Button imageButton, postTweetButton, backButton;
    public ImageView imageView;

    public PostTweetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_tweet, container, false);
        tweetContent = (EditText) view.findViewById(R.id.tweetContent);
        imageButton = (Button) view.findViewById(R.id.imageButton);
        postTweetButton = (Button) view.findViewById(R.id.postTweetButton);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        backButton = (Button) view.findViewById(R.id.backButton);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (presenter == null) {
            presenter = new PostTweetFragmentPresenter(this);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.chooseImage();
            }
        });

        postTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.postTweet();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.back();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

}
