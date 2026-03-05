package logic.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Random;

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


    // bubble <3
    private static final String[] bubblePops = {
            "/audio/sfx/attack/bubble/bubblePop1.mp3",
            "/audio/sfx/attack/bubble/bubblePop2.mp3",
            "/audio/sfx/attack/bubble/bubblePop3.mp3"
    };

    private static final Random random = new Random();

    public static void playRandomBubblePop() {
        int index = random.nextInt(bubblePops.length);
        playSFX(bubblePops[index]);
    }
}