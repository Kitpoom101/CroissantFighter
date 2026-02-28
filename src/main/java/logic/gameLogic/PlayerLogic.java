package logic.gameLogic;

import javafx.scene.input.KeyEvent;

public class PlayerLogic {

    private Player player;
    private double speed = 10;

    public PlayerLogic(Player player) {
        this.player = player;
    }

    public void handleKeyPressed(KeyEvent event) {

        switch (event.getCode()) {

            case W:
            case UP:
                player.move(0, -speed);
                break;

            case S:
            case DOWN:
                player.move(0, speed);
                break;

            case A:
            case LEFT:
                player.move(-speed, 0);
                break;

            case D:
            case RIGHT:
                player.move(speed, 0);
                break;
        }
    }
}