package logic.gameLogic;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;

public class PlayerLogic {

    private boolean hasDealtDamage = false;


    private Player player;
    private Player enemy;
    private int playerNum;

    private KeyCode leftKey;
    private KeyCode rightKey;
    private KeyCode attackKey;
    private KeyCode specialAttackKey;

    // ====== HITBOX ===== //
    private Rectangle attackHitbox;
    private boolean attacking = false;
    // ====== HITBOX ===== //

    private boolean moveLeft;
    private boolean moveRight;

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
            specialAttackKey = KeyCode.Q;
        }
        else{
            leftKey = KeyCode.J;
            rightKey = KeyCode.L;
            attackKey = KeyCode.I;
            specialAttackKey = KeyCode.P;
        }

        attackHitbox = new Rectangle();
        attackHitbox.setStroke(Color.RED);
        attackHitbox.setFill(Color.TRANSPARENT);
        attackHitbox.setVisible(false);
    }

    /* ================= INPUT ================= */

    public void handleKeyPressed(KeyEvent event) {

        if(event.getCode() == leftKey)
            moveLeft = true;

        if(event.getCode() == rightKey)
            moveRight = true;

        if(event.getCode() == attackKey){

            // 🔥 ให้ MeleeClass คุม attack state เอง
            if(player.getCharacter() instanceof MeleeClass melee){

                melee.startAttack(player);

            }else{
                // ranged
                player.setState(PlayerState.ATTACK);
                attack();
            }
        }
    }

    public void handleKeyReleased(KeyEvent event) {

        if(event.getCode() == leftKey)
            moveLeft = false;

        if(event.getCode() == rightKey)
            moveRight = false;
    }

    /* ================= ATTACK ================= */

    private void attack() {

        attacking = true;

        AttackData data = player.getCharacter().getAttackData();
        ImageView sprite = player.getSprite();

        double playerX = sprite.getLayoutX();
        double playerY = sprite.getLayoutY();

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

    /* ================= UPDATE LOOP ================= */

    public void update() {

        /* ===== MOVEMENT ===== */

        if (moveLeft) {
            velocityX -= ACCELERATION;
            player.faceLeft();
        }

        if (moveRight) {
            velocityX += ACCELERATION;
            player.faceRight();
        }

        if (velocityX > MAX_SPEED)
            velocityX = MAX_SPEED;

        if (velocityX < -MAX_SPEED)
            velocityX = -MAX_SPEED;

        velocityX *= FRICTION;

        if (Math.abs(velocityX) < 0.05)
            velocityX = 0;

        player.translate(velocityX, 0);
        clampToArenaBounds();

        /* ===== WEAPON SPRITE POSITION ===== */

        if (player.getCharacter() instanceof MeleeClass){

            ImageView body = player.getSprite();

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
        }

        /* ===== MELEE ATTACK FLOW ===== */

        if(player.getState() == PlayerState.ATTACK
                && player.getCharacter() instanceof MeleeClass melee){

            player.getWeaponSprite().setVisible(true);

            melee.updateAttack(player);

            if(melee.getAttackState() == AttackState.WillAttack && !hasDealtDamage){

                attack();
                hasDealtDamage = true;

                melee.setAttackState(AttackState.AllowAttack);
            }

            if(melee.isAttackFinished()){

                player.setState(PlayerState.WALK);
                player.getWeaponSprite().setVisible(false);

                hasDealtDamage = false;
            }
        }

    }

    /* ================= BOUNDS ================= */

    private void clampToArenaBounds() {

        if (!(player.getSprite().getParent() instanceof Region arena))
            return;

        double arenaWidth = arena.getWidth() > 0 ?
                arena.getWidth() : arena.prefWidth(-1);

        if (arenaWidth <= 0)
            return;

        ImageView sprite = player.getSprite();
        double spriteWidth = sprite.getBoundsInParent().getWidth();

        double minX = 0;
        double maxX = Math.max(minX, arenaWidth - spriteWidth);

        if (sprite.getLayoutX() < minX)
            sprite.setLayoutX(minX);

        else if (sprite.getLayoutX() > maxX)
            sprite.setLayoutX(maxX);
    }
}
