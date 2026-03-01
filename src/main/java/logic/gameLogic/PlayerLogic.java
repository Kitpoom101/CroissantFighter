package logic.gameLogic;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PlayerLogic {

    private Player player;

    private boolean moveLeft;
    private boolean moveRight;

    private final double speed = 5;

    public PlayerLogic(Player player) {
        this.player = player;
    }

    /* KEY PRESSED */
    public void handleKeyPressed(KeyEvent event) {

        switch (event.getCode()) {
            case A, LEFT -> moveLeft = true;
            case D, RIGHT -> moveRight = true;
        }
    }

    /* KEY RELEASED */
    public void handleKeyReleased(KeyEvent event) {

        switch (event.getCode()) {
            case A, LEFT -> moveLeft = false;
            case D, RIGHT -> moveRight = false;
        }
    }

    /* CALLED EVERY FRAME */
    public void update() {

        if (moveLeft) {
            player.translate(-speed, 0);
            player.faceLeft();
        }

        if (moveRight) {
            player.translate(speed, 0);
            player.faceRight();
        }
    }
}