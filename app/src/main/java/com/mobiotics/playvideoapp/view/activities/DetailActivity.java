package com.mobiotics.playvideoapp.view.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.mobiotics.playvideoapp.R;
import com.mobiotics.playvideoapp.model.Highlight;
import com.mobiotics.playvideoapp.model.repository.DBHelper;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private SimpleExoPlayer player;
    private PlayerView videoView;
    private DataSource.Factory mDataSourceFactory;
    private DataSource.Factory mFactory;
    private AdaptiveTrackSelection.Factory mAdaptiveTrackSelectionFactory;
    private TrackSelector mTrackSelector;
    private LoadControl mLoadControl;
    private DefaultBandwidthMeter mBandwidthMeter;
    private ProgressBar progressBar;
    private Uri videoURI;
    private SimpleExoPlayer exoPlayer;
    private DefaultHttpDataSourceFactory dataSourceFactory;
    private MediaSource mediaSource;
    private int position;
    private ExtractorsFactory extractorsFactory;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBar=findViewById(R.id.amPrgbrLoading);
        recyclerView=findViewById(R.id.video_list);

        Highlight highlight=getIntent().getParcelableExtra("highlight");
        position=getIntent().getIntExtra("position",0);
        videoView=findViewById(R.id.video_view);
        mAdaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
        mTrackSelector = new DefaultTrackSelector(mAdaptiveTrackSelectionFactory);

        mLoadControl = new DefaultLoadControl();
        extractorsFactory = new DefaultExtractorsFactory();

        /*videoView.setPlayer(player);
        MediaSource video=new ExtractorMediaSource(Uri.parse(highlight.getUrl()),mDataSourceFactory,extractorsFactory,null,null);
        player.prepare(video);
        player.setPlayWhenReady(true);*/
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        videoURI = Uri.parse(highlight.getUrl());

        dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
        mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

        videoView.setPlayer(exoPlayer);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState){
                    case Player.STATE_BUFFERING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case Player.STATE_ENDED:
                        progressBar.setVisibility(View.GONE);
                        playNextVideo();
                        break;
                    default:
                        progressBar.setVisibility(View.GONE);
                        break;

                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

    }

    private void playNextVideo() {
        List<Highlight> highlights=DBHelper.getInstance().getHighlights();
        if (position<highlights.size()-1){
            videoURI=Uri.parse(highlights.get(position+1).getUrl());
            position++;
        }else {
            videoURI=Uri.parse(highlights.get(0).getUrl());
            position=0;
        }

        mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayer!=null){
            exoPlayer.stop();
            exoPlayer.release();
        }
    }
}
