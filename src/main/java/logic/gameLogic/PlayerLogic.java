package logic.gameLogic;

import component.scene2.Scene2;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import logic.entity.AttackData;
import logic.entity.characterClass.HybridClass;
import logic.entity.characterClass.MeleeClass;
import logic.entity.characterClass.RangedClass;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;

public class PlayerLogic {

    // store player
    private Player player;
    private Player enemy;
    private int playerNum;

    // store keycode
    private KeyCode leftKey;
    private KeyCode rightKey;
    private KeyCode attackKey;
    private KeyCode specialAttackKey;

    // ====== HITBOX ===== //
    private Rectangle attackHitbox;

    private final double HITBOX_WIDTH = 50;
    private boolean attacking = false;
    private long hitboxStartTime = 0;
    private final long HITBOX_DURATION = 500_000_000L; // 0.5 sec in nanoseconds
    // ====== HITBOX ===== //

    // store movement
    private boolean moveLeft;
    private boolean moveRight;

    private final double speed = 5;

    // ===== Skill ===== //
    private int originalAtk;
    private int originalDef;
    private boolean skillActive = false;

    private Text buffText;

    /* ===== MOVEMENT PHYSICS ===== */

    private double velocityX = 0;

    private final double ACCELERATION = 0.4;
    private final double MAX_SPEED = 3;
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
        else if(playerNum == 2){
            leftKey = KeyCode.J;
            rightKey = KeyCode.L;
            attackKey = KeyCode.I;
            specialAttackKey = KeyCode.P;
        }

        // for hit box //
        attackHitbox = new Rectangle();

        attackHitbox.setStroke(Color.RED);
        attackHitbox.setFill(Color.TRANSPARENT);
        attackHitbox.setVisible(false);

        // for buff //
        buffText = new Text("");
        buffText.setStyle("-fx-font-size: 24px; -fx-fill: black;");
        buffText.setOpacity(0);
    }

    /* ---------- INPUT ---------- */

    /* KEY PRESSED */
    public void handleKeyPressed(KeyEvent event) {

        if(event.getCode() == leftKey)
            moveLeft = true;

        if(event.getCode() == rightKey)
            moveRight = true;

        if(event.getCode() == attackKey){
            if (player.getCharacter().getAttackState() == AttackState.NotAttacking){
                player.setState(PlayerState.ATTACK);
                player.getCharacter().setAttackState(AttackState.AllowAttack);
                player.getCharacter().startAttack(player);
            } else if (!(player.getCharacter() instanceof MeleeClass)) {
                player.setState(PlayerState.ATTACK);
                attack();
            }
        }

        if(event.getCode() == specialAttackKey){
            if (player.getCharacter().getSkillState() == SkillState.CanUseSkill){
                player.getCharacter().setSkillState(SkillState.CanUseSkill);
                skill();
            }
        }

    }

    private void skill() {

        if(skillActive) return;

        skillActive = true;

        var character = player.getCharacter();

        // ===== APPLY BUFF ===== //
        character.useSpecialSkill();

        player.getCharacter().setSkillState(SkillState.UsedSkill);

        if(character instanceof MeleeClass){
            showBuffText("DEF UP BY " + character.getBuff() + " !!");
        } else if (character instanceof RangedClass) {
            showBuffText("RANGE UP BY " + character.getBuff() + " !!");
        } else if (character instanceof HybridClass){
            showBuffText("ATTACK UP BY " + character.getBuff() + " !!");
        } else {
            showBuffText("How?");
        }


        // ===== SKILL DURATION =====
        PauseTransition skillDuration =
                new PauseTransition(Duration.seconds(5));

        skillDuration.setOnFinished(e -> {

            // restore stats
            character.resetBuff();

            skillActive = false;

            player.getCharacter()
                    .setSkillState(SkillState.CooldownSkill);

            showBuffText("Buff Ended");
        });

        skillDuration.play();
        startSkillCooldown();
    }

    private void startSkillCooldown(){

        player.getCharacter()
                .setSkillState(SkillState.CooldownSkill);

        PauseTransition cooldown =
                new PauseTransition(Duration.seconds(8));

        cooldown.setOnFinished(e ->
                player.getCharacter()
                        .setSkillState(SkillState.CanUseSkill)
        );

        cooldown.play();
    }

    private void showBuffText(String message){

        buffText.setText(message);

        ImageView sprite = player.getSprite();

        buffText.setLayoutX(sprite.getLayoutX());
        buffText.setLayoutY(sprite.getLayoutY() - 40);

        FadeTransition fadeIn =
                new FadeTransition(Duration.seconds(0.3), buffText);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition stay =
                new PauseTransition(Duration.seconds(1));

        FadeTransition fadeOut =
                new FadeTransition(Duration.seconds(0.3), buffText);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        SequentialTransition sequence =
                new SequentialTransition(fadeIn, stay, fadeOut);

        sequence.play();
    }

    public Text getBuffText() {
        return buffText;
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
        // store hitbox time
        hitboxStartTime = System.nanoTime();
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
        // ===== PREVENT PLAYER MOVEMENT ===== //
        clampToArenaBounds();


        // ===== WEAPON SPRITE FOLLOW ===== //
        weaponSpriteFollow();

        // ===== ATTACK ANIMATION ===== //
        // Only melee
        attackAnimation();

        // ===== UPDATE HITBOX ===== //
        updateHitboxTimer();
    }

    private void updateHitboxTimer() {

        if(!attackHitbox.isVisible()) return;

        long now = System.nanoTime();

        if(now - hitboxStartTime >= HITBOX_DURATION){
            attackHitbox.setVisible(false);
        }
    }

    private void attackAnimation(){
        if(player.getState() == PlayerState.ATTACK && player.getCharacter() instanceof MeleeClass){
            player.getWeaponSprite().setVisible(true);
            player.getCharacter().updateAttack(player);
            if(player.getCharacter().getAttackState() == AttackState.WillAttack){
                attack();
                player.getCharacter().setAttackState(AttackState.NotAttacking);
            }

            if(player.getCharacter().isAttackFinished()){
                player.setState(PlayerState.WALK);
                player.getCharacter().setAttackState(AttackState.NotAttacking);
                player.getWeaponSprite().setVisible(false);
            }
        }
    }

    private void weaponSpriteFollow() {
        if (player.getCharacter() instanceof MeleeClass){
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
        }
    }

    private void clampToArenaBounds() {
        if (!(player.getSprite().getParent() instanceof Region arena)) {
            return;
        }

        double arenaWidth = arena.getWidth() > 0 ? arena.getWidth() : arena.prefWidth(-1);
        if (arenaWidth <= 0) {
            return;
        }

        ImageView sprite = player.getSprite();
        double spriteWidth = sprite.getBoundsInParent().getWidth();

        double minX = 0;
        double maxX = Math.max(minX, arenaWidth - spriteWidth);

        if (sprite.getLayoutX() < minX) {
            sprite.setLayoutX(minX);
        } else if (sprite.getLayoutX() > maxX) {
            sprite.setLayoutX(maxX);
        }
    }
}

