package logic.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {
    private static MediaPlayer bgmPlayer;

    public static void playBGM(String path) {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }

        Media media = new Media(AudioManager.class.getResource(path).toExternalForm());
        bgmPlayer = new MediaPlayer(media);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // loop forever
        bgmPlayer.play();
    }

    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }
}