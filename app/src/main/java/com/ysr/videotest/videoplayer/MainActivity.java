package com.ysr.videotest.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.ysr.videotest.R;

import java.util.ArrayList;
import java.util.List;

import view.VideoSuperPlayer;


public class MainActivity extends Activity {
    private String url = "http://flv2.bn.netease.com/videolib3/1511/13/NhxBs6222/SD/NhxBs6222-mobile.mp4";
    private List<String> mList;
    private ListView mListView;
    private boolean isPlaying;
    private int indexPostion = -1;
    private MAdapter mAdapter;

    @Override
    protected void onDestroy() {
        App.setMediaPlayerNull();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);
        mList = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            mList.add(url);
        }
        mAdapter = new MAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if ((indexPostion < mListView.getFirstVisiblePosition() || indexPostion > mListView
                        .getLastVisiblePosition()) && isPlaying) {
                    indexPostion = -1;
                    isPlaying = false;
                    mAdapter.notifyDataSetChanged();
                    App.setMediaPlayerNull();
                }
            }
        });
    }

    class MAdapter extends BaseAdapter {
        private Context context;
        LayoutInflater inflater;

        public MAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public String getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            GameVideoViewHolder holder = null;
            if (v == null) {
                holder = new GameVideoViewHolder();
                v = inflater.inflate(R.layout.list_item, parent, false);
                holder.mVideoViewLayout = (VideoSuperPlayer) v
                        .findViewById(R.id.video);
                holder.mPlayBtnView = (ImageView) v.findViewById(R.id.play_btn);
                v.setTag(holder);
            } else {
                holder = (GameVideoViewHolder) v.getTag();
            }
            holder.mPlayBtnView.setOnClickListener(new MyOnclick(mList
                    .get(position), holder.mVideoViewLayout, position));
            holder.mVideoViewLayout
                    .setVideoPlayCallback(new MyVideoPlayCallback(
                            holder.mPlayBtnView, holder.mVideoViewLayout));
            if (indexPostion == position) {
                holder.mVideoViewLayout.setVisibility(View.VISIBLE);
            } else {
                holder.mVideoViewLayout.setVisibility(View.GONE);
                holder.mVideoViewLayout.close();
            }
            return v;
        }

        class MyVideoPlayCallback implements VideoSuperPlayer.VideoPlayCallbackImpl {
            ImageView mPlayBtnView;
            VideoSuperPlayer mSuperVideoPlayer;

            public MyVideoPlayCallback(ImageView mPlayBtnView,
                                       VideoSuperPlayer mSuperVideoPlayer) {
                this.mPlayBtnView = mPlayBtnView;
                this.mSuperVideoPlayer = mSuperVideoPlayer;
            }

            @Override
            public void onCloseVideo() {
                isPlaying = false;
                indexPostion = -1;
                mSuperVideoPlayer.close();
                App.setMediaPlayerNull();
                mPlayBtnView.setVisibility(View.VISIBLE);
                mSuperVideoPlayer.setVisibility(View.GONE);
            }

            @Override
            public void onSwitchPageType() {
                if (((Activity) context).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    Intent intent = new Intent(new Intent(context,
                            FullActivity.class));
                    context.startActivity(intent);
                }
            }

            @Override
            public void onPlayFinish() {

            }

        }

        class MyOnclick implements OnClickListener {
            String url;
            VideoSuperPlayer mSuperVideoPlayer;
            int position;

            public MyOnclick(String url, VideoSuperPlayer mSuperVideoPlayer,
                             int position) {
                this.url = url;
                this.position = position;
                this.mSuperVideoPlayer = mSuperVideoPlayer;
            }

            @Override
            public void onClick(View v) {
                App.setMediaPlayerNull();
                indexPostion = position;
                isPlaying = true;
                mSuperVideoPlayer.setVisibility(View.VISIBLE);
                mSuperVideoPlayer.loadAndPlay(App.getMediaPlayer(), url, 0,
                        false);
                notifyDataSetChanged();
            }
        }

        class GameVideoViewHolder {

            private VideoSuperPlayer mVideoViewLayout;
            private ImageView mPlayBtnView;

        }

    }

}
