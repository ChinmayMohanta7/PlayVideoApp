package com.mobiotics.playvideoapp.view.activities;

import android.app.ActionBar;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.google.android.exoplayer2.source.MediaPeriod;
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
import com.mobiotics.playvideoapp.view.adapters.HomeVideoListAdapter;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements HomeVideoListAdapter.OnHighlightClickLitener {

    private PlayerView videoView;
    private LoadControl mLoadControl;
    private ProgressBar progressBar;
    private Uri videoURI;
    private SimpleExoPlayer exoPlayer;
    private DefaultHttpDataSourceFactory dataSourceFactory;
    private MediaSource mediaSource;
    private int position;
    private ExtractorsFactory extractorsFactory;
    private RecyclerView recyclerView;
    private List<Highlight> highlights;
    private HomeVideoListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        progressBar=findViewById(R.id.amPrgbrLoading);
        recyclerView=findViewById(R.id.video_list);

        highlights=DBHelper.getInstance().getHighlights();
        adapter=new HomeVideoListAdapter(this,highlights,position);
        adapter.setLitener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        Highlight highlight=getIntent().getParcelableExtra("highlight");
        position=getIntent().getIntExtra("position",0);
        videoView=findViewById(R.id.video_view);

        mLoadControl = new DefaultLoadControl();
        extractorsFactory = new DefaultExtractorsFactory();

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
                        DBHelper.getInstance().setHighlightLastDuration(position,0);
                        position++;
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
        recyclerView.scrollToPosition(position);
        if (position<highlights.size()){
            videoURI=Uri.parse(highlights.get(position).getUrl());
        }else {
            videoURI=Uri.parse(highlights.get(0).getUrl());
            position=0;
        }

        mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
        exoPlayer.prepare(mediaSource);
        exoPlayer.seekTo(highlights.get(position).getDuration());
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

    @Override
    public void onHighlightCLicked(Highlight highlight, int position) {
        DBHelper.getInstance().setHighlightLastDuration(this.position,exoPlayer.getDuration());
        this.position=position;
        playNextVideo();
        Toast.makeText(this,highlight.getTitle(),Toast.LENGTH_SHORT).show();
    }
}
