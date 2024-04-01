package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;

public interface ActorManager {

    default void addActors(Stage stage, Actor[] actors) {
        for (Actor actor : actors) {
            stage.addActor(actor);
        }
    }

    default void clearStage(Stage stage) {
        SnapshotArray<Actor> actors = new SnapshotArray<>(stage.getActors());
        for (Actor actor : actors) {
            actor.remove();
        }
    }
}
