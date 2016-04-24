package com.daviddone.voa.util;

import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;

public class Player implements OnBufferingUpdateListener, OnCompletionListener,
		MediaPlayer.OnPreparedListener {
	public MediaPlayer mediaPlayer;
	private SeekBar skbProgress;
	private Timer mTimer = new Timer();
	private String videoUrl;
	private boolean pause;
	private int playPosition;
	public Player(String videoUrl, SeekBar skbProgress) {
		this.skbProgress = skbProgress;
		this.videoUrl = videoUrl;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}

		mTimer.schedule(mTimerTask, 0, 1000);
	}

	/*******************************************************
	 * ͨ��ʱ����Handler�����½����
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			int position = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();
			if (duration > 0) {
				long pos = skbProgress.getMax() * position / duration;
				skbProgress.setProgress((int) pos);
			}
		};
	};

	/**
	 * ���绰��
	 */
	public void callIsComing() {
		if (mediaPlayer.isPlaying()) {
			playPosition = mediaPlayer.getCurrentPosition();// ��õ�ǰ����λ��
			mediaPlayer.stop();
		}
	}

	/**
	 * ͨ������
	 */
	public void callIsDown() {
		if (playPosition > 0) {
			playNet(playPosition);
			playPosition = 0;
		}
	}

	/**
	 * ����
	 */
	public void play() {
		playNet(0);
	}

	/**
	 * �ز�
	 */
	public void replay() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);// �ӿ�ʼλ�ÿ�ʼ��������
		} else {
			playNet(0);
		}
	}

	/**
	 * ��ͣ
	 */
	public boolean pause() {
		if (mediaPlayer.isPlaying()) {// ������ڲ���
			mediaPlayer.pause();// ��ͣ
			pause = true;
		} else {
			if (pause) {// �������ͣ״̬
				mediaPlayer.start();// �����
				pause = false;
			}
		}
		return pause;
	}

	/**
	 * ֹͣ
	 */
	public void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
	}

	@Override
	/**  
	 * ͨ��onPrepared����  
	 */
	public void onPrepared(MediaPlayer arg0) {
		arg0.start();
		Log.e("mediaPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		Log.e("mediaPlayer", "onCompletion");
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		skbProgress.setSecondaryProgress(bufferingProgress);//���û�������
		int currentProgress = skbProgress.getMax()
				* mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
		Log.e(currentProgress + "% play", bufferingProgress + "% buffer");
	}

	/**
	 * ��������
	 * 
	 * @param playPosition
	 */
	private void playNet(int playPosition) {
		try {
			mediaPlayer.reset();// �Ѹ������ָ�����ʼ״̬
			/**
			 * ͨ��MediaPlayer.setDataSource()
			 * �ķ���,��URL���ļ�·�����ַ�ķ�ʽ����.ʹ��setDataSource ()����ʱ,Ҫע���������:
			 * 1.������ɵ�MediaPlayer ����ʵ��Null ����ļ��.
			 * 2.����ʵ�ֽ���IllegalArgumentException ��IOException
			 * ���쳣,�ںܶ������,�����õ��ļ����²�������. 3.��ʹ��URL ����������ý���ļ�,���ļ�Ӧ��Ҫ��֧��pragressive
			 * ����.
			 */
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepare();// ���л���
			mediaPlayer.setOnPreparedListener(new MyPreparedListener(
					playPosition));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final class MyPreparedListener implements
			android.media.MediaPlayer.OnPreparedListener {
		private int playPosition;

		public MyPreparedListener(int playPosition) {
			this.playPosition = playPosition;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start();
			if (playPosition > 0) {
				mediaPlayer.seekTo(playPosition);
			}
		}
	}

}