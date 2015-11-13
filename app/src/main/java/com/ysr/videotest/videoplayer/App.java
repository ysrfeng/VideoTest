package com.ysr.videotest.videoplayer;

import android.app.Application;
import android.media.MediaPlayer;

public class App extends Application {
	public static MediaPlayer mPlayer;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public static MediaPlayer getMediaPlayer() {
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
		}
		return mPlayer;
	}

	public static void setMediaPlayerNull() {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
