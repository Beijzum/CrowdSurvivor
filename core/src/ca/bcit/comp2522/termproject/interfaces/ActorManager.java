package ca.bcit.comp2522.termproject.interfaces;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Represents an interface for managing actors within a Stage.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public interface ActorManager {

    /**
     * Adds an array of actors to the specified stage.
     *
     * @param stage  Stage object representing the stage.
     * @param actors Actor array objects to be added to the stage.
     */
    default void addActors(Stage stage, Actor[] actors) {
        for (Actor actor : actors) {
            stage.addActor(actor);
        }
    }

    /**
     * Clears all actors from the specified stage.
     *
     * @param stage Stage object representing the stage.
     */
    default void clearStage(Stage stage) {
        SnapshotArray<Actor> actors = new SnapshotArray<>(stage.getActors());
        for (Actor actor : actors) {
            actor.remove();
        }
    }
}
