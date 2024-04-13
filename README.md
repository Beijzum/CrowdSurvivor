# Crowd Survivor

Crowd Survivor is a game inspired by the game "Vampire Survivors". It is a "bullet hell" game in which many enemies
spawn in a small arena and the player must survive a constant onslaught of enemies charging towards the player.
We made the game to emulate the same hectic play style, while also keeping a fun atmosphere using internet memes and
Edro as the enemies.

## How To Play

Use the WASD keys to move around the map, and use your mouse to aim at where to fire a projectile. Press escape to pause
the game and kill enemies to level up. Upon level-up, choose an upgrade to increase your stats that helps improve damage
or survivability.

You win when all Shreks are dead after 2 minutes have elapsed in-game!

If you lose, don't worry. You can buy permanent upgrades in the shop--found in the main menu--to help you on your
future attempts.

## Features

- Auto-saving
- Shop with long-term progression
- Permanent stat upgrades
- Random organic spawning of enemies

## Important Notes

This game uses JDK 18. We suggest downloading the SDK here at Adoptium because it has many built-in libraries.
https://adoptium.net/temurin/releases/?version=18&package=jdk&arch=any.

### Mac Machines

Crowd Survivor was developed on Windows machines, as such game stability has not been tested for Mac machines and should
be taken into consideration. If any errors occur, you should try starting the game with the
JVM argument -XstartOnFirstThread as cited by LibGDX.

### Resolution

Crowd Survivor was also developed at the window size of 1920 x 1080 resolution, hence menu layouts may not properly
align if the window size deviates from this resolution. For the best gaming experience, the game should be played at the
recommended resolution of 1920 x 1080.

### Commits

The GitHub commits on this repository may seem unbalanced due to both pair programming and different commit styles.
Jonathan tends to frequently commit small changes, while Jason tends to infrequently commit large changes.

### Versions Used

- JDK version 18
- LibGDX version 1.12.1
- Gradle version 8.5

### Project Features

- [Interfaces](core/src/ca/bcit/comp2522/termproject/interfaces)  e.g. ```public interface ActorManager```
- [Enumeration](core/src/ca/bcit/comp2522/termproject/HPBar.java) e.g. ```private enum Colour```
- [Abstract Classes](core/src/ca/bcit/comp2522/termproject/Entity.java) e.g. ```public abstract class Entity```
- [Inheritance](core/src/ca/bcit/comp2522/termproject/enemies/Enemy.java) e.g. ```Enemy inherits Entity```
- [Polymorphism](core/src/ca/bcit/comp2522/termproject/enemies/Charger.java) e.g. ```Charger overrides Enemy```
- [Encapsulation](core/src/ca/bcit/comp2522/termproject/Player.java) e.g. ```Data-hiding in Player```
- [Use of data structures](core/src/ca/bcit/comp2522/termproject/screens/InGameScreen.java) e.g. ```Linked Lists```
- [Unittests](tests/src/ca/bcit/comp2522/termproject) e.g. ```Collection of tests```

## Post-Development Thoughts

This project was our first attempt at making a game using LibGDX. We thought the game would be simple and would involve
only a handful of classes. However, the more we progressed, the more we realized how much planning was needed for an
optimized project structure. This game gave us a lot of experience coding using OOP and SOLID principles and we will
use that experience as a stepping stone in our developer journey.

### Future Consideration

If we were to redo this project, we would make the following changes shown below:

- A more aesthetically pleasing UI
- Fix the UI elements offsetting whenever the player moves
- Animated enemies
- Settings menu to allow for more player customization
- Different death styles on enemies
- More enemy variation
- Special ultimate abilities the player can use
- Load out page to choose different attack styles
- Pick up different items that add power and mechanics
- Different player projectiles
- Different projectile firing types
- Ability to gamble currency for risk/reward experience
- Optimized code structure (generics, lambda)
