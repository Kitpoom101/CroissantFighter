package logic.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {
    private static MediaPlayer bgmPlayer;
    private static MediaPlayer priorityPlayer;

    private static double bgmVolume = 0.4;  // 40%
    private static double sfxVolume = 1.0;  // 100%

    public static void playBGM(String path) {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }

        Media media = new Media(AudioManager.class.getResource(path).toExternalForm());
        bgmPlayer = new MediaPlayer(media);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // loop forever
        bgmPlayer.setVolume(bgmVolume);
        bgmPlayer.play();
    }

    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    public static void playSFX(String path) {
        AudioClip clip = new AudioClip(
                AudioManager.class.getResource(path).toExternalForm()
        );
        clip.setVolume(sfxVolume);
        clip.play();
    }

    public static void playPrioritySFX(String path) {
        if (bgmPlayer != null) {
            bgmPlayer.pause();
        }

        Media media = new Media(
                AudioManager.class.getResource(path).toExternalForm()
        );

        priorityPlayer = new MediaPlayer(media);

        priorityPlayer.setOnEndOfMedia(() -> {
            if (bgmPlayer != null) {
                bgmPlayer.play(); // resume BGM after
            }
        });

        priorityPlayer.play();
    }
}