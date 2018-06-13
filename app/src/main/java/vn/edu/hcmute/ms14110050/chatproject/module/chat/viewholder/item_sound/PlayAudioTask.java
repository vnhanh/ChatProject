package vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.item_sound;

import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.IOException;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;

/**
 * Created by Vo Ngoc Hanh on 6/11/2018.
 */

public class PlayAudioTask extends AsyncTask<String,Integer,Boolean> {
    MediaPlayer mediaPlayer;
    SimpleCallback<MediaPlayer> callback;

    public PlayAudioTask(SimpleCallback<MediaPlayer> callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setVolume(100,100);
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean prepared = false;
        String url = params[0];

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            prepared = true;
        } catch (IOException e) {
            e.printStackTrace();
            prepared = false;
        }

        return prepared;
    }

    @Override
    protected void onPostExecute(Boolean prepared) {
        super.onPostExecute(prepared);
        if (prepared) {
            callback.onSuccess(mediaPlayer);
        }else{
            callback.onError();
        }
    }
}
