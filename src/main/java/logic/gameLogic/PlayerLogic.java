package logic.gameLogic;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.entity.AttackData;
import logic.entity.characterClass.RangedClass;
import logic.entity.characters.meleeCharacters.Katana;

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

        if(event.getCode() == attackKey){
            attack();
            player.setState(PlayerState.ATTACK);
            player.getCharacter().startAttack(player);
        }

    }

    private void attack() {

        attacking = true;

        AttackData data = player.getCharacter().getAttackData();
        ImageView sprite = player.getSprite();

        double playerX = sprite.getLayoutX();
        double playerY = sprite.getLayoutY();

        // 🔥 ถ้าเป็น ranged
        if (data == null) {

            float startX = (float) (playerX + sprite.getBoundsInParent().getWidth() / 2);
            float startY = (float) (playerY + sprite.getBoundsInParent().getHeight() / 2);

            player.getCharacter().attack(
                    startX,
                    startY,
                    player.isFacingRight(),
                    player
            );

            return;
        }

        // 🔥 ถ้าเป็น melee ทำเหมือนเดิม
        attackHitbox.setWidth(data.getWidth());
        attackHitbox.setHeight(data.getHeight());

        attackHitbox.setLayoutY(playerY);

        if (player.isFacingRight()) {
            attackHitbox.setLayoutX(
                    playerX + sprite.getBoundsInParent().getWidth()
            );
        } else {
            attackHitbox.setLayoutX(
                    playerX - data.getWidth()
            );
        }

        attackHitbox.setVisible(true);
        checkHit();
    }

    private void checkHit() {

        if (attackHitbox.getBoundsInParent()
                .intersects(enemy.getSprite().getBoundsInParent())) {

            System.out.println("HIT!");

            enemy.getCharacter().takeDamage(player.getCharacter().getAtk());
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
        // only when walk state
        //if (player.getState() == PlayerState.WALK){
        player.translate(velocityX, 0);


        // ==== WEAPON SPRITE ===== //
        ImageView body = player.getSprite();

        // weapon sprite position
        player.getWeaponSprite().setLayoutY(body.getLayoutY());

        if(player.isFacingRight()){
            player.getWeaponSprite().setScaleX(1);
            player.getWeaponSprite().setLayoutX(
                    body.getLayoutX() + body.getBoundsInParent().getWidth()
            );
        }else{
            player.getWeaponSprite().setScaleX(-1);
            player.getWeaponSprite().setLayoutX(
                    body.getLayoutX() - player.getWeaponSprite().getImage().getWidth()
            );
        }

        // ==== ATTACK ANIMATION ===== //
        if(player.getState() == PlayerState.ATTACK){

//            (Katana) player.getCharacter()
//                    .updateAttack(player);
//
//            if(player.getCharacter()
//                    .isAttackFinished()){
//
//                player.setState(PlayerState.WALK);
//            }
            player.getWeaponSprite().setVisible(true);
            player.getCharacter().updateAttack(player);

            if(player.getCharacter().isAttackFinished()){
                player.setState(PlayerState.WALK);
                player.getWeaponSprite().setVisible(false);
            }

        }
    }
}

