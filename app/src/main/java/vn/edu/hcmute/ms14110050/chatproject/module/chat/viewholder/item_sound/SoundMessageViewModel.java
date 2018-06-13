package vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.item_sound;

import android.content.res.Resources;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.MessageViewModel;

import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_MEDIA_PLAYER_IS_RUNNING;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_PREPARE_MEDIA_PLAYER_FAILED;

/**
 * Created by Vo Ngoc Hanh on 6/6/2018.
 */

public class SoundMessageViewModel extends MessageViewModel {
    public final ObservableField<Drawable> playDrawable = new ObservableField<>();
    private boolean isPlaying = false;
    PlayAudioTask audioTask;
    private MediaPlayer mediaPlayer;
    public final ObservableInt audioDuration = new ObservableInt(0);
    public final ObservableInt playAudioProgress = new ObservableInt(0);

    public SoundMessageViewModel(@NonNull Resources resources, @NonNull String uid, String chatterUserUrlImage) {
        super(resources, uid, chatterUserUrlImage);
        playDrawable.set(resources.getDrawable(R.drawable.ic_play_media_36dp_0dp));
    }

    public void onClickPlayBtn(View imageView) {
        if (!isPlaying) {
            onPlayAudio(imageView);
        }else{
            onResetPlayAudio();

        }
    }

    private void onPlayAudio(final View imageView) {
        // nếu file audio đang play
        if (audioTask != null && audioTask.getStatus() == AsyncTask.Status.RUNNING) {
            // thông báo trên UI cho người dùng biết
            notify(new SendObject(TAG_MEDIA_PLAYER_IS_RUNNING, null));
        }else{
            // dừng tác vụ nếu nó tồn tại và không phải đang trong trạng thái running
            if (audioTask != null) {
                audioTask.cancel(true);
            }
            isPlaying = true;
            playDrawable.set(getResources().getDrawable(R.drawable.ic_prepared_play_media_36dp_0dp));

            // tạo mới tác vụ play audio
            audioTask = new PlayAudioTask(new SimpleCallback<MediaPlayer>() {
                @Override
                public void onSuccess(MediaPlayer mediaPlayer) {
                    SoundMessageViewModel.this.mediaPlayer = mediaPlayer;
                    onSetupMediaPlayerBeforeStart(imageView);
                    mediaPlayer.start();
                }

                @Override
                public void onError() {
                    onPrepareMediaPlayerFailed();
                }
            });
            audioTask.execute(message.getContent());
        }
    }

    private void onSetupMediaPlayerBeforeStart(View imageView) {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                onResetPlayAudio();
            }
        });

        audioDuration.set(mediaPlayer.getDuration());
        handler.postDelayed(runnable, UPDATE_CYCLE);
        imageView.clearAnimation();
        imageView.startAnimation(AnimationUtils.loadAnimation(imageView.getContext(), R.anim.fade_in_2));
        playDrawable.set(getResources().getDrawable(R.drawable.ic_stop_media_42dp_0dp));
    }

    private final int UPDATE_CYCLE = 60;

    // todo: mới chỉ set observable cho item send
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                playAudioProgress.set(mediaPlayer.getCurrentPosition());
                if (isPlaying) {
                    handler.postDelayed(this, UPDATE_CYCLE);
                }
            }else{
                playAudioProgress.set(0);
            }
        }
    };

    private void onPrepareMediaPlayerFailed() {
        notify(new SendObject(TAG_PREPARE_MEDIA_PLAYER_FAILED, null));
        onResetPlayAudio();
    }

    private void onResetPlayAudio() {
        isPlaying = false;
        playDrawable.set(getResources().getDrawable(R.drawable.ic_play_media_36dp_0dp));
        stopMediaPlayer();
        onStopAudio();
    }

    private void stopMediaPlayer() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void onStopAudio() {
        if (audioTask != null) {
            audioTask.cancel(true);
            audioTask = null;
        }
    }

    public void onProgressSeekbarChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (mediaPlayer == null) {
                seekBar.setProgress(0);
            }else{
                mediaPlayer.seekTo(progress * mediaPlayer.getDuration());
            }
        }
    }
}
