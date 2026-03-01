package logic.gameLogic;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerLogic {

    // store player
    private Player player;
    private Player enemy;
    private int playerNum;

    // store keycode
    private KeyCode leftKey;
    private KeyCode rightKey;
    private KeyCode attackKey;

    // ====== HITBOX ===== //
    private Rectangle attackHitbox;

    private final double HITBOX_WIDTH = 50;
    private boolean attacking = false;
    // ====== HITBOX ===== //

    // store movement
    private boolean moveLeft;
    private boolean moveRight;

    private final double speed = 5;

    /* ===== MOVEMENT PHYSICS ===== */

    private double velocityX = 0;

    private final double ACCELERATION = 0.4;
    private final double MAX_SPEED = 5;
    private final double FRICTION = 0.85;

    public PlayerLogic(Player player, Player enemy, int i) {
        this.player = player;
        this.enemy = enemy;
        this.playerNum = i;

        if(playerNum == 1){
            leftKey = KeyCode.A;
            rightKey = KeyCode.D;
            attackKey = KeyCode.E;
        }
        else if(playerNum == 2){
            leftKey = KeyCode.LEFT;
            rightKey = KeyCode.RIGHT;
            attackKey = KeyCode.L;
        }

        // for hit box //
        attackHitbox = new Rectangle();

        attackHitbox.setStroke(Color.RED);
        attackHitbox.setFill(Color.TRANSPARENT);
        attackHitbox.setVisible(false);
    }

    /* ---------- INPUT ---------- */

    /* KEY PRESSED */
    public void handleKeyPressed(KeyEvent event) {

        if(event.getCode() == leftKey)
            moveLeft = true;

        if(event.getCode() == rightKey)
            moveRight = true;

        if(event.getCode() == attackKey)
            attack();
    }

    private void attack() {


            attacking = true;

            ImageView sprite = player.getSprite();

            double playerX = sprite.getLayoutX();
            double playerY = sprite.getLayoutY();

            double spriteHeight = sprite.getBoundsInParent().getHeight();

            // match sprite height
            attackHitbox.setHeight(spriteHeight);
            attackHitbox.setWidth(HITBOX_WIDTH);

            // START FROM PLAYER BOTTOM
            attackHitbox.setLayoutY(playerY);

            if(player.isFacingRight()) {
                attackHitbox.setLayoutX(
                        playerX + sprite.getBoundsInParent().getWidth()
                );
            } else {
                attackHitbox.setLayoutX(
                        playerX - HITBOX_WIDTH
                );
            }

            attackHitbox.setVisible(true);

            checkHit();
    }

    private void checkHit() {

        if (attackHitbox.getBoundsInParent()
                .intersects(enemy.getSprite().getBoundsInParent())) {

            System.out.println("HIT!");
        }
    }

    public Rectangle getAttackHitbox() {
        return attackHitbox;
    }

    /* KEY RELEASED */
    public void handleKeyReleased(KeyEvent event) {

        if(event.getCode() == leftKey)
            moveLeft = false;

        if(event.getCode() == rightKey)
            moveRight = false;
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