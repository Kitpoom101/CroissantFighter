package logic.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Random;

/**
 * Centralized audio utility for background music and sound effects.
 * <p>
 * Supports looping BGM, one-shot SFX, priority SFX that temporarily pause BGM,
 * and random bubble-pop variation playback.
 * </p>
 */
public class AudioManager {
    /** Active background music player instance. */
    private static MediaPlayer bgmPlayer;
    /** Player used for priority SFX that should temporarily override BGM. */
    private static MediaPlayer priorityPlayer;

    /** Global BGM volume in range {@code [0.0, 1.0]}. */
    private static double bgmVolume = 0.10;  // 40%
    /** Global SFX volume in range {@code [0.0, 1.0]}. */
    private static double sfxVolume = 0.45;  // 100%

    /**
     * Starts looping background music from resource path.
     * If another BGM is active, it is stopped first.
     *
     * @param path classpath resource path to audio file
     */
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

    /**
     * Stops currently playing background music, if any.
     */
    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    /**
     * Plays a one-shot sound effect at configured SFX volume.
     *
     * @param path classpath resource path to audio file
     */
    public static void playSFX(String path) {
        AudioClip clip = new AudioClip(
                AudioManager.class.getResource(path).toExternalForm()
        );
        clip.setVolume(sfxVolume);
        clip.play();
    }

    /**
     * Plays a priority sound effect and pauses BGM until it finishes.
     *
     * @param path classpath resource path to priority audio file
     */
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


    /** Bubble-pop sound pool used for random variation playback. */
    private static final String[] bubblePops = {
            "/audio/sfx/attack/bubble/bubblePop1.mp3",
            "/audio/sfx/attack/bubble/bubblePop2.mp3",
            "/audio/sfx/attack/bubble/bubblePop3.mp3"
    };

    /** Shared random source for selecting bubble-pop variants. */
    private static final Random random = new Random();

    /**
     * Plays a random bubble-pop sound from the configured pool.
     */
    public static void playRandomBubblePop() {
        int index = random.nextInt(bubblePops.length);
        playSFX(bubblePops[index]);
    }
}
