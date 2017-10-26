package com.example.android.bakingapp.fragments;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.example.android.bakingapp.utils.Constants.SELECTED_STEP;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener {

    public final static String PRAYER_POSITION = "prayer_position";
    public final static String PRAYER_STATE = "prayer_state";
    private static MediaSessionCompat mMediaSession;
    String TAG = StepDetailsFragment.class.getSimpleName();
    private Step selectedStep;
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private Uri videoUri;
    private long position = 0;
    private boolean playing;


    public StepDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Check for selected recipe in arguments
        Bundle data = getArguments();
        if (data.getParcelable(SELECTED_STEP) != null) {
            selectedStep = data.getParcelable(SELECTED_STEP);
        }


        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);


        if (selectedStep != null) {

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;


            mPlayerView = rootView.findViewById(R.id.playerView);
            fillData(rootView);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                ViewGroup.LayoutParams params = mPlayerView.getLayoutParams();
                params.height = height;
                mPlayerView.setLayoutParams(params);

            }

            position = C.TIME_UNSET;
            if (savedInstanceState != null) {
                //...your code...
                position = savedInstanceState.getLong(PRAYER_POSITION, C.TIME_UNSET);
                playing = savedInstanceState.getBoolean(PRAYER_STATE, false);
            }

            // Initialize the Media Session.
            initializeMediaSession();

            if (selectedStep.videoURL != null && !selectedStep.videoURL.equals("")) {
                // Initialize the player.
                videoUri = Uri.parse(selectedStep.videoURL);

            } else {
                ImageView no_video = rootView.findViewById(R.id.no_video);
                no_video.setVisibility(View.VISIBLE);
                no_video.setImageResource(R.drawable.no_video);
                mPlayerView.setVisibility(View.GONE);
            }
        }

        return rootView;
    }

    @Override
    public void onResume() {

        if (videoUri != null)
            initializePlayer(videoUri);

        super.onResume();
    }

    private void fillData(View rootView) {
        TextView shortDescription = rootView.findViewById(R.id.shortDescription);
        shortDescription.setText(selectedStep.shortDescription);
        TextView description = rootView.findViewById(R.id.description);
        description.setText(selectedStep.description);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putLong(PRAYER_POSITION, position);
        outState.putBoolean(PRAYER_STATE, playing);
        super.onSaveInstanceState(outState);
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), TAG);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            if (position != C.TIME_UNSET) mExoPlayer.seekTo(position);
            if (playing) mExoPlayer.setPlayWhenReady(true);
            else mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.getPlaybackState();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            position = mExoPlayer.getCurrentPosition();
            playing = mExoPlayer.getPlayWhenReady();
            Log.d("playing", String.valueOf(playing));
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {

        releasePlayer();
        if (mMediaSession != null) mMediaSession.setActive(false);
        super.onPause();
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            playing = true;
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            playing = false;
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
