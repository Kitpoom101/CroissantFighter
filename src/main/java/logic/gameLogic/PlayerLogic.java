package logic.gameLogic;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PlayerLogic {

    private Player player;

    private boolean moveLeft;
    private boolean moveRight;

    private final double speed = 5;

    /* ===== MOVEMENT PHYSICS ===== */

    private double velocityX = 0;

    private final double ACCELERATION = 0.8;
    private final double MAX_SPEED = 6;
    private final double FRICTION = 0.85;

    public PlayerLogic(Player player) {
        this.player = player;
    }

    /* ---------- INPUT ---------- */

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

        /* acceleration */
        if (moveLeft) {
            velocityX -= ACCELERATION;
            player.faceLeft();
        }

        if (moveRight) {
            velocityX += ACCELERATION;
            player.faceRight();
        }

        /* clamp max speed */
        if (velocityX > MAX_SPEED)
            velocityX = MAX_SPEED;

        if (velocityX < -MAX_SPEED)
            velocityX = -MAX_SPEED;

        /* friction */
        velocityX *= FRICTION;

        /* stop tiny sliding */
        if (Math.abs(velocityX) < 0.05)
            velocityX = 0;

        /* apply movement */
        player.translate(velocityX, 0);
    }
}