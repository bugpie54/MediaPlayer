package com.example.android.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity {

    private Button play, backward, forward;
    private MediaPlayer mediaPlayer;
    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                mediaPlayer.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
               mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            releaseMediaPlayer();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Toast.makeText(MainActivity.this, "Song Finished!", Toast.LENGTH_SHORT).show();
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        backward = findViewById(R.id.backward);
        forward = findViewById(R.id.forward);

        //Create and set up the AudioManager to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request audio focus so in order to play the audio file. The app needs
                // to play a song audio file, so we will request audio focus
                // with AUDIOFOCUS_GAIN.
                int result = mAudioManager.requestAudioFocus
                        (mOnAudioFocusChangeListener,
                                AudioManager.STREAM_MUSIC,
                                AudioManager.AUDIOFOCUS_GAIN);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //Create and set up the media player resource
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.justin_bieber_love_yourself);
                    if (mediaPlayer.isPlaying()) {
                        Toast.makeText(MainActivity.this, "Pausing song", Toast.LENGTH_SHORT).show();
                        mediaPlayer.pause();
                        play.setBackgroundResource(R.mipmap.ic_play);
                    } else {
                        Toast.makeText(MainActivity.this, "Playing sound", Toast.LENGTH_SHORT).show();
                        mediaPlayer.start();
                        play.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    }
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
                });

    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            // Regardless of whether or not we were granted audio focus, abandon it.
            // This also unregisters the AudioFocusChangeListener so we don't get
            //anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
