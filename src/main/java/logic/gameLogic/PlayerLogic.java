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
    private KeyCode reloadKey; // เรีโหลด

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

    private Text buffText;


    // ===== ammoText ===== //
    private Text ammoText;

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
            reloadKey = KeyCode.R; // ✅ เพิ่มปุ่มรีโหลด P1
        }
        else if(playerNum == 2){
            leftKey = KeyCode.J;
            rightKey = KeyCode.L;
            attackKey = KeyCode.I;
            specialAttackKey = KeyCode.O; // ของเดิม O เป็น skill นะครับ อาจจะต้องระวังปุ่มซ้ำ
            reloadKey = KeyCode.P; // ⚠️ แนะนำให้เปลี่ยนรีโหลด P2 เป็นปุ่ม P (หรือเปลี่ยน specialAttackKey เป็น U) เพื่อไม่ให้ปุ่มซ้ำกันครับ
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

        // 🔽 สร้าง UI แสดงจำนวนกระสุน และนำไปแปะติดกับ playerRoot (ขยับตามตัวละครอัตโนมัติ)
        ammoText = new Text();
        ammoText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        ammoText.setStroke(Color.BLACK);
        ammoText.setStrokeWidth(0.5);
        ammoText.setVisible(false); // ซ่อนไว้ก่อน จะแสดงเฉพาะตอนเป็น Exorcist

        player.getPlayerRoot().getChildren().add(ammoText);
    }


    /* ---------- INPUT ---------- */

    /* KEY PRESSED */
    public void handleKeyPressed(KeyEvent event) {



        if(event.getCode() == leftKey)
            moveLeft = true;

        if(event.getCode() == rightKey)
            moveRight = true;

        if (event.getCode() == attackKey) {
            if (attackHeld) return;
            attackHeld = true;

            var character = player.getCharacter();

            if (character.getAttackState() == AttackState.NotAttacking) {

                // ⭐ แก้จุดนี้: เช็คก่อนว่าตัวละครนี้ "ยิงได้" (SpawnAttack) หรือไม่
                // ถ้าเป็นสายยิง ให้เรียกใช้ระบบยิงกระสุน (attack()) ทันที
                if (character instanceof logic.interfaces.SpawnAttack) {
                    player.setState(PlayerState.ATTACK);
                    attack(); // ฟังก์ชันนี้จะไปเรียก character.attack(...) ในที่สุด
                }
                // กรณีที่ 2: ตัวละครสายประชิดที่มีอนิเมชั่น (Katana, Hammer)
                else if (character instanceof HaveWeapon) {
                    player.setState(PlayerState.ATTACK);
                    character.setAttackState(AttackState.Attacking);
                    character.startAttack(player);
                }
                // กรณีที่ 3: อื่นๆ
                else {
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



        if (event.getCode() == reloadKey) {
            if (player.getCharacter() instanceof logic.entity.characters.hybridCharacters.Exorcist) {
                ((logic.entity.characters.hybridCharacters.Exorcist) player.getCharacter()).reload();
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

        // ⭐ เพิ่มบรรทัดนี้ เพื่อสร้างตัวแปร character ให้ใช้ในเมธอดนี้ได้
        logic.entity.Character character = player.getCharacter();

        AttackData data = character.getAttackData();
        Group sprite = player.getPlayerRoot();

        double playerX = sprite.getLayoutX();
        double playerY = sprite.getLayoutY();
        double bodyWidth = 150;

        // 🔥 ถ้าเป็นสาย Ranged (ไม่มี Data)
        if (data == null) {
            float startX = (float) (playerX + bodyWidth / 2);
            float startY = (float) (playerY + 60);

            // ตอนนี้ 'character' จะไม่ Error แล้ว เพราะเราประกาศไว้ข้างบน
            character.attack(
                    startX,
                    startY,
                    player.isFacingRight(),
                    player
            );
            return;
        }

        attackHitbox.setWidth(data.getWidth());
        attackHitbox.setHeight(data.getHeight());
        attackHitbox.setLayoutY(playerY + 20); // ปรับให้ความสูง hitbox ตรงกับระดับอาวุธ

        double specialOffset = (player.getCharacter() instanceof Vampire) ? 120 : 0;

        if (player.isFacingRight()) {
            // วาง Hitbox ต่อจากขอบขวาของตัวละคร + ระยะเยื้อง
            attackHitbox.setLayoutX(playerX + bodyWidth + specialOffset);
        } else {
            // วาง Hitbox ย้อนไปทางซ้ายจากตัวละคร - ความกว้าง hitbox - ระยะเยื้อง
            attackHitbox.setLayoutX(playerX - data.getWidth() - specialOffset);
        }

        attackHitbox.setVisible(true);
        hitboxStartTime = System.nanoTime();
        checkHit();
    }

    private void checkHit() {

        if (attackHitbox.getBoundsInParent()
                .intersects(enemy.getHitbox()
                        .localToScene(enemy.getHitbox().getBoundsInLocal())
                )) {

            System.out.println("HIT!");

            enemy.getCharacter().takeDamage(player.getCharacter().getAtk());
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
        player.translate(velocityX, 0);
        // ===== PREVENT PLAYER MOVEMENT ===== //
        clampToArenaBounds();


        // ===== WEAPON SPRITE FOLLOW ===== //
        weaponSpriteFollow();

        // ===== ATTACK ANIMATION ===== //
        // Only melee
        attackAnimation();
        handleAttackSpeed();

        // ===== UPDATE HITBOX ===== //
        updateHitboxTimer();
        if (player.getCharacter() instanceof logic.entity.characters.hybridCharacters.Exorcist) {
            ((logic.entity.characters.hybridCharacters.Exorcist) player.getCharacter()).updateExorcistLogic();
        }

        if (player.getCharacter() instanceof logic.entity.characters.hybridCharacters.Exorcist) {
            var exo = (logic.entity.characters.hybridCharacters.Exorcist) player.getCharacter();
            exo.updateExorcistLogic();

            ammoText.setVisible(true);

            // จัดตำแหน่งให้ตัวหนังสืออยู่เหนือหัวตัวละคร (แกน Y ติดลบคือลอยขึ้นไป)
            ammoText.setLayoutX(40);
            ammoText.setLayoutY(-10);

            // เปลี่ยนข้อความและสีตามสถานะ
            if (exo.isReloading()) {
                ammoText.setText("Reloading...");
                ammoText.setFill(Color.YELLOW);
            } else {
                ammoText.setText("Ammo: " + exo.getCurrentAmmo() + "/" + exo.getMaxAmmo());
                // ถ้ากระสุนหมดให้ตัวหนังสือเป็นสีแดงเพื่อเตือนผู้เล่น
                if (exo.getCurrentAmmo() == 0) {
                    ammoText.setFill(Color.RED);
                } else {
                    ammoText.setFill(Color.WHITE);
                }
            }
        } else {
            // ถ้าไม่ใช่ Exorcist ให้ซ่อนข้อความไป
            ammoText.setVisible(false);
        }

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
        if (player.getCharacter() instanceof HaveWeapon) {
            ImageView weapon = player.getWeaponSprite();
            Group spriteGroup = player.getPlayerRoot();

            // 1. ตั้งค่าความสูง (Y) ให้อยู่ในระดับมือ (ปรับตัวเลข 50 ตามความเหมาะสมของรูป)
            weapon.setLayoutY(50);

            // 2. ตรวจสอบระยะ Offset พิเศษ (เช่น Vampire ที่ Hitbox ยื่นไปไกลกว่าปกติ)
            double specialOffset = 0;
            if (player.getCharacter() instanceof Vampire) {
                specialOffset = 120; // ให้ตรงกับค่าที่ตั้งไว้ในเมธอด attack()
            }

            // 3. คำนวณตำแหน่ง X ให้สอดคล้องกับทิศทางและ Hitbox
            // getBoundsInLocal().getWidth() ของตัวละครมักจะเป็น 150 ตามที่ตั้งใน Player
            double bodyWidth = 150;

            if (player.isFacingRight()) {
                weapon.setScaleX(1);
                // วางอาวุธไว้ที่ขอบขวาของตัวละคร + ระยะเยื้องพิเศษ
                weapon.setLayoutX(bodyWidth + specialOffset - 20);
            } else {
                weapon.setScaleX(-1);
                // เมื่อหันซ้าย ต้องคำนวณความกว้างของรูปอาวุธเองด้วยเพื่อให้ปลายดาบชี้ไปทางซ้าย
                double weaponWidth = (weapon.getImage() != null) ? weapon.getImage().getWidth() : 0;
                // วางอาวุธไว้ทางซ้าย (ค่าติดลบ) - ระยะเยื้องพิเศษ
                weapon.setLayoutX(-weaponWidth - specialOffset + 20);
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
}
