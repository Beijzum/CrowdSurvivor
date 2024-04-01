package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public interface ActorManager {
    void addActor(Actor actor);

    default void addActors(Stage stage, Actor[] actors) {
        for (Actor actor : actors) {
            stage.addActor(actor);
        }
    }

    default void clearStage(Stage stage) {
        stage.clear();
    }

    default void removeActor(Actor actor) {
        actor.remove();
    }
}
