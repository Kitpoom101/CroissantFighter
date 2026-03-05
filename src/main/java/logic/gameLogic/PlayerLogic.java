package logic.gameLogic;

import component.scene2.Scene2;
import javafx.scene.Group;
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
import logic.entity.characters.hybridCharacters.Vampire;
import logic.interfaces.HaveWeapon;

import java.security.Key;

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
    private KeyCode jumpKey;

    // ====== JUMP =======
    private double velocityY = 0;

    private final double GRAVITY = 0.3;
    private final double JUMP_FORCE = -12;
    private final double MAX_FALL_SPEED = 7;

    private boolean onGround = true;

    // ====== HITBOX ===== //
    private Rectangle attackHitbox;

    private boolean attacking = false;
    private long hitboxStartTime = 0;
    private final long HITBOX_DURATION = 500_000_000L; // 0.5 sec in nanoseconds
    // ====== HITBOX ===== //

    // Attack
    private long lastAttackTime = 0;
    private boolean attackHeld = false;

    // store movement
    private boolean moveLeft;
    private boolean moveRight;

    private final double speed = 5;

    // ===== Skill ===== //
    private boolean skillActive = false;
    private static final long SKILL_COOLDOWN_NANOS = 8_000_000_000L;
    private long skillCooldownStartNanos = -1L;

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
            jumpKey = KeyCode.W;
        }
        else if(playerNum == 2){
            leftKey = KeyCode.J;
            rightKey = KeyCode.L;
            attackKey = KeyCode.O;
            specialAttackKey = KeyCode.U;
            jumpKey = KeyCode.I;
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
            if(attackHeld) return; // ✅ prevent repeat

            attackHeld = true;

            if (player.getCharacter().getAttackState() == AttackState.NotAttacking){
                player.setState(PlayerState.ATTACK);
                player.getCharacter().setAttackState(AttackState.Attacking);
                player.getCharacter().startAttack(player);
            } else if (!(player.getCharacter() instanceof MeleeClass)) {

                long now = System.nanoTime();

                if (now - lastAttackTime >= getAttackInterval()) {

                    lastAttackTime = now;

                    player.setState(PlayerState.ATTACK);
                    attack();
                }
            }
        }

        if(event.getCode() == specialAttackKey){
            if (player.getCharacter().getSkillState() == SkillState.CanUseSkill){
                player.getCharacter().setSkillState(SkillState.CanUseSkill);
                skill();
            }
        }

        if(event.getCode() == jumpKey){
            if(onGround){
                velocityY = JUMP_FORCE;
                onGround = false;
            }
        }
    }

    /* KEY RELEASED */
    public void handleKeyReleased(KeyEvent event) {

        if(event.getCode() == leftKey)
            moveLeft = false;

        if(event.getCode() == rightKey)
            moveRight = false;

        if(event.getCode() == attackKey){
            attackHeld = false;
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
            if (character instanceof Vampire){
                showBuffText("ATTACK UP BY " + character.getBuff() + " !!" +
                        "\nAND MORE LIFE STEAL");
            }else{
                showBuffText("ATTACK UP BY " + character.getBuff() + " !!");
            }

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
        skillCooldownStartNanos = System.nanoTime();

        PauseTransition cooldown =
                new PauseTransition(Duration.seconds(8));

        cooldown.setOnFinished(e -> {
                player.getCharacter()
                        .setSkillState(SkillState.CanUseSkill);
                skillCooldownStartNanos = -1L;
        });

        cooldown.play();
    }

    public double getSkillCooldownProgress() {
        SkillState state = player.getCharacter().getSkillState();
        if (state == SkillState.CanUseSkill) {
            return 1.0;
        }
        if (state != SkillState.CooldownSkill || skillCooldownStartNanos < 0) {
            return 0.0;
        }

        long elapsed = System.nanoTime() - skillCooldownStartNanos;
        return Math.max(0.0, Math.min((double) elapsed / SKILL_COOLDOWN_NANOS, 1.0));
    }

    private void showBuffText(String message){

        buffText.setText(message);

        Group sprite = player.getPlayerRoot();

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
        Group sprite = player.getPlayerRoot();

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
            if (player.getCharacter() instanceof Vampire){
                attackHitbox.setLayoutX(
                        playerX + sprite.getBoundsInParent().getWidth() + 30
                );
                attackHitbox.setLayoutY(
                        playerY + 35
                );
            }else{
                attackHitbox.setLayoutX(
                        playerX + sprite.getBoundsInParent().getWidth()
                );
            }

        } else {
            if (player.getCharacter() instanceof Vampire){
                attackHitbox.setLayoutX(
                        playerX - data.getWidth() - 30
                );
                attackHitbox.setLayoutY(
                        playerY + 35
                );
            }else{
                attackHitbox.setLayoutX(
                        playerX - data.getWidth()
                );
            }

        }

        attackHitbox.setVisible(true);
        // store hitbox time
        hitboxStartTime = System.nanoTime();
        checkHit();
    }

    private void checkHit() {

        if (attackHitbox.getBoundsInParent()
                .intersects(enemy.getHitbox()
                        .localToScene(enemy.getHitbox().getBoundsInLocal())
                )) {

            int rawDamage = player.getCharacter().getAtk();
            enemy.getCharacter().takeDamage(rawDamage);

            int finalDamage = rawDamage - enemy.getCharacter().getDef();

            if (finalDamage > 0) {
                Scene2.getInstance().showFloatingText(enemy, finalDamage, Color.DARKRED, "-");
            }

            System.out.println("HIT!");

            enemy.getCharacter().takeDamage(player.getCharacter().getAtk());

            // for calculating vampire damage
            if (player.getCharacter() instanceof Vampire vampire){
                player.getCharacter().setHp((int) Math.min(
                            vampire.getMaxHp(),
                            ((vampire.getAtk() - enemy.getCharacter().getDef() * 0.5)
                                    * vampire.getLifeStealMultiplier())
                                    + vampire.getHp()
                        )
                );
            }
        }
    }

    public Rectangle getAttackHitbox() {
        return attackHitbox;
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
        // apply gravity
        velocityY += GRAVITY;

        if (velocityY > MAX_FALL_SPEED)
            velocityY = MAX_FALL_SPEED;

// move player
        player.translate(velocityX, velocityY);

        // ===== PREVENT PLAYER MOVEMENT ===== //
        clampToArenaBounds();
        checkGroundCollision();


        // ===== WEAPON SPRITE FOLLOW ===== //
        weaponSpriteFollow();

        // ===== ATTACK ANIMATION ===== //
        // Only melee
        attackAnimation();
        handleAttackSpeed();

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
        if(player.getState() == PlayerState.ATTACK && player.getCharacter() instanceof HaveWeapon){
            player.getWeaponSprite().setVisible(true);
            player.getCharacter().updateAttack(player);
            if(player.getCharacter().getAttackState() == AttackState.WillAttack){
                attack();
                lastAttackTime = System.nanoTime();
                player.getCharacter().setAttackState(AttackState.AttackCooldown);
            }

            if(player.getCharacter().isAttackFinished()){
                player.setState(PlayerState.WALK);
                player.getWeaponSprite().setVisible(false);
            }
        }
    }

    private void handleAttackSpeed() {

        if(player.getCharacter().getAttackState() != AttackState.AttackCooldown)
            return;

        long now = System.nanoTime();

        if(now - lastAttackTime >= getAttackInterval()){
            player.getCharacter()
                    .setAttackState(AttackState.NotAttacking);
        }
    }

    private long getAttackInterval() {
        float attackSpeed =
                player.getCharacter().getAttackSpeed();

        return (long)(1_000_000_000L / attackSpeed);
    }

    private void weaponSpriteFollow() {
        if (player.getCharacter() instanceof HaveWeapon){
            Group body = player.getPlayerRoot();

            // weapon sprite position
            if (!(player.getCharacter() instanceof Vampire)){
                player.getWeaponSprite().setLayoutY(body.getLayoutY());
            }

            if(player.isFacingRight()){
                player.getWeaponSprite().setScaleX(1);
                if (player.getCharacter() instanceof Vampire){
                    vampireWeapon(body, player.isFacingRight());
                    return;
                }
                player.getWeaponSprite().setLayoutX(
                        body.getLayoutX() + body.getBoundsInParent().getWidth()
                );
            }else{
                player.getWeaponSprite().setScaleX(-1);
                if (player.getCharacter() instanceof Vampire){
                    vampireWeapon(body, player.isFacingRight());
                    return;
                }
                player.getWeaponSprite().setLayoutX(
                        body.getLayoutX() - player.getWeaponSprite().getImage().getWidth()
                );
            }
        }
    }

    private void vampireWeapon(Group body, boolean right) {
        if (right){
            if (player.getCharacter().getFrameIndex() == 0) {
                player.getWeaponSprite().setLayoutY(
                        body.getLayoutY() + body.getBoundsInParent().getHeight() / 2
                );
                player.getWeaponSprite().setLayoutX(
                        body.getLayoutX() - body.getBoundsInParent().getWidth() / 8
                );
            } else {
                player.getWeaponSprite().setLayoutY(
                        body.getLayoutY()
                );
                player.getWeaponSprite().setLayoutX(
                        body.getLayoutX() + body.getBoundsInParent().getWidth()
                );
            }
        }else{
            if (player.getCharacter().getFrameIndex() == 0) {
                player.getWeaponSprite().setLayoutY(
                        body.getLayoutY() + body.getBoundsInParent().getHeight() / 2
                );
                player.getWeaponSprite().setLayoutX(
                        body.getLayoutX() + body.getBoundsInParent().getWidth() / 3
                );
            } else {
                player.getWeaponSprite().setLayoutY(
                        body.getLayoutY()
                );
                player.getWeaponSprite().setLayoutX(
                        body.getLayoutX() - body.getBoundsInParent().getWidth() * 1.5
                );
            }
        }
    }

    private void clampToArenaBounds() {
        if (!(player.getPlayerRoot().getParent() instanceof Region arena)) {
            return;
        }

        double arenaWidth = arena.getWidth() > 0 ? arena.getWidth() : arena.prefWidth(-1);
        if (arenaWidth <= 0) {
            return;
        }

        Group sprite = player.getPlayerRoot();
        double spriteWidth = sprite.getBoundsInParent().getWidth();

        double minX = 0;
        double maxX = Math.max(minX, arenaWidth - spriteWidth);

        if (sprite.getLayoutX() < minX) {
            sprite.setLayoutX(minX);
        } else if (sprite.getLayoutX() > maxX) {
            sprite.setLayoutX(maxX);
        }
    }

    private void checkGroundCollision() {

        if (!(player.getPlayerRoot().getParent() instanceof Region arena))
            return;

        Group sprite = player.getPlayerRoot();

        double groundY = arena.getHeight() - sprite.getBoundsInParent().getHeight();

        if (sprite.getLayoutY() >= groundY) {
            sprite.setLayoutY(groundY);
            velocityY = 0;
            onGround = true;
        }
    }
}
