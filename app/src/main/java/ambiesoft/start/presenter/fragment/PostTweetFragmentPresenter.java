package ambiesoft.start.presenter.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.model.dataclass.User;
import ambiesoft.start.view.fragment.PostTweetFragment;
import ambiesoft.start.view.fragment.TwitterResultListFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.BundleItemChecker.getPreviousFragmentIDFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedBuskerFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedPerformanceFromBundle;

/**
 * Created by Bryanyhy on 17/9/2016.
 */
public class PostTweetFragmentPresenter {

    private static final String HASHTAG = "#STARTMelb";
    private static final String APP_PACKAGE_NAME = "com.twitter.android";
    private static final int GALLERY_INTENT = 2;
    private final static int PER_DETAIL = 9;

    private PostTweetFragment view;
    private Uri imageUrl;
    private String content;
    private int previousFragmentID;
    private User selectedBusker;
    private Performance selectedPerformance;

    public PostTweetFragmentPresenter(PostTweetFragment view) {
        this.view = view;
        getBundleFromPreviousFragment();
    }

    public void getBundleFromPreviousFragment() {
        Bundle bundle = view.getArguments();
        if (bundle != null) {
            // if bundle exists, get the filter values
            selectedBusker = getSelectedBuskerFromBundle(bundle);
            selectedPerformance = getSelectedPerformanceFromBundle(bundle);
            previousFragmentID = getPreviousFragmentIDFromBundle(bundle);
        }
    }

    // once the select image button is clicked, show Gallery to user
    public void chooseImage() {
        if (isTwitterInstalled(view.getContext())) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            view.getActivity().startActivityForResult(intent, GALLERY_INTENT);
        } else {
//            showAlertBox("Sorry", "Photo uploading funtion requires the usage of Twitter Application in Android.", view.getActivity());
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            try {
                                view.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PACKAGE_NAME)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                view.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + APP_PACKAGE_NAME)));
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Photo uploading funtion requires the usage of Twitter Application in Android. Do you want to download and install Twitter Applicaiton?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_INTENT && resultCode == view.getActivity().RESULT_OK) {
            Log.i("System.out", "" + data.toUri(0));
            imageUrl = data.getData();
            view.imageView.setImageURI(imageUrl);
        }
    }

    public void postTweet() {
        content = view.tweetContent.getText().toString();
        TweetComposer.Builder builder;
        if (imageUrl != null) {
            if (previousFragmentID == PER_DETAIL) {
                builder = new TweetComposer.Builder(view.getActivity())
                        .image(imageUrl)
                        .text(content + "\n\n" + twitterContentForPerformance() + "\n\n" + HASHTAG
                                + " " + selectedBusker.getHashtag());
            } else {
                builder = new TweetComposer.Builder(view.getActivity())
                        .image(imageUrl)
                        .text(content + "\n" + HASHTAG);
            }
        } else {
            if (previousFragmentID == PER_DETAIL) {
                builder = new TweetComposer.Builder(view.getActivity())
                        .text(content + "\n" + twitterContentForPerformance() + "\n" + HASHTAG
                                + " " + selectedBusker.getHashtag());
            } else {
                builder = new TweetComposer.Builder(view.getActivity())
                        .text(content + "\n" + HASHTAG);
            }
        }
        builder.show();
//        if (previousFragmentID == PER_DETAIL) {
//            // do nothing
//        } else {
//            view.getFragmentManager().beginTransaction()
//                    .replace(R.id.content_frame, new TwitterResultListFragment(), "TwitterFragment").remove(view).commit();
//        }
    }

    public String twitterContentForPerformance() {
        String[] splitDate = selectedPerformance.getDate().split("/", 3);
        String date = splitDate[0] + "/" + splitDate[1];

        String tweetContent = selectedPerformance.getName() + "\n" +
                date + "\n" +
                selectedPerformance.getsTime() + "-" + selectedPerformance.geteTime() + "\n" +
                selectedPerformance.getAddress();
        return tweetContent;
    }

    public static boolean isTwitterInstalled(Context context) {
        try {
            context.getPackageManager().getApplicationInfo(APP_PACKAGE_NAME, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void back() {
        if (previousFragmentID == PER_DETAIL) {
            view.getFragmentManager().popBackStack();
        } else {
            view.getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new TwitterResultListFragment(), "TwitterFragment").remove(view).commit();
        }
    }

}
