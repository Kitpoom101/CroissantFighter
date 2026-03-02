package component.scene2;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import logic.entity.BaseProjectileAttack;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerLogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.image.ImageView;
import java.awt.*;
import java.util.ArrayList;

import static logic.gameLogic.Selection.getPlayer_1_Character;
import static logic.gameLogic.Selection.getPlayer_2_Character;

public class Scene2 extends Pane {
    
    private static final double HEALTH_BAR_TOP_MARGIN = 20;
    private static final double HEALTH_BAR_SIDE_MARGIN = 20;

    public Player player1;
    public Player player2;
    private PlayerLogic playerLogic1;
    private PlayerLogic playerLogic2;
    private HealthBar player1HealthBar;
    private HealthBar player2HealthBar;
    private List<BaseProjectileAttack> projectileList = new ArrayList<>();

    private static Scene2 instance;

    public static Scene2 getInstance() {
        return instance;
    }

    public Scene2() {


        player1 = new Player(getPlayer_1_Character(), 1);
        player2 = new Player(getPlayer_2_Character(), 2);

        playerLogic1 = new PlayerLogic(player1, player2, 1);
        playerLogic2 = new PlayerLogic(player2, player1, 2);
        
        player1HealthBar = new HealthBar(player1.getCharacter().getHp());
        player2HealthBar = new HealthBar(player2.getCharacter().getHp());

        player1HealthBar.setLayoutX(HEALTH_BAR_SIDE_MARGIN);
        player1HealthBar.setLayoutY(HEALTH_BAR_TOP_MARGIN);

        player2HealthBar.layoutXProperty().bind(
                widthProperty().subtract(
                        player2HealthBar.prefWidth(-1) + HEALTH_BAR_SIDE_MARGIN
                )
        );
        player2HealthBar.setLayoutY(HEALTH_BAR_TOP_MARGIN);

        getChildren().addAll(
                player1HealthBar,
                player2HealthBar,
                player1.getSprite(),
                player2.getSprite(),
                playerLogic1.getAttackHitbox(),
                playerLogic2.getAttackHitbox()
        );

        draw(Color.GREEN);

        /* ✅ WAIT UNTIL SCENE EXISTS */
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setupControls(newScene);
                startGameLoop();
            }
        });

            instance = this;

    }

    private void draw(Color backgroundColor) {
        BackgroundFill bgFill = new BackgroundFill(backgroundColor, new CornerRadii(5), Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};

        this.setBackground(new Background(bgFillA));
    }


    private void setupControls(Scene scene) {

        scene.setOnKeyPressed(e -> {
            playerLogic1.handleKeyPressed(e);
            playerLogic2.handleKeyPressed(e);
        });

        scene.setOnKeyReleased(e -> {
            playerLogic1.handleKeyReleased(e);
            playerLogic2.handleKeyReleased(e);
        });
    }

    private void startGameLoop() {

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                playerLogic1.update(); // runs every frame
                playerLogic2.update(); // runs every frame

                player1HealthBar.setCurrentHp(player1.getCharacter().getHp());
                player2HealthBar.setCurrentHp(player2.getCharacter().getHp());

                Iterator<BaseProjectileAttack> iterator = projectileList.iterator();

                while (iterator.hasNext()) {
                    BaseProjectileAttack p = iterator.next();
                    p.update();

                    Player target = (p.getOwner() == player1) ? player2 : player1;

                    if (p.getSprite().getBoundsInParent()
                            .intersects(target.getSprite().getBoundsInParent())) {


                                target.getCharacter().takeDamage(p.getOwner().getCharacter().getAtk());


                        getChildren().remove(p.getSprite());
                        iterator.remove();
                        continue;
                    }

                    if (p.isOutOfRange()) {
                        getChildren().remove(p.getSprite());
                        iterator.remove();
                    }
                }
            }
        };

        gameLoop.start();
    }

    public void addProjectile(BaseProjectileAttack p) {
        projectileList.add(p);   // 👈 สำคัญมาก
        getChildren().add(p.getSprite());
        p.getSprite().toFront();
        System.out.println("Projectile ถูก add แล้ว");
    }

    public void removeProjectile(BaseProjectileAttack p) {
        getChildren().remove(p.getSprite());
        projectileList.remove(p);
    }
}
