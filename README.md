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

### UML

When exporting the UML to PDF, the export kept splitting the diagram into segmented pages of pieces of the diagram,
only exporting the diagram as an image would preserve the diagram; therefore, our UML diagram is in a .png format.

### Versions Used

- JDK version 18
- LibGDX version 1.12.1
- Gradle version 8.5

## Post-Development Thoughts

### Future Consideration

- A more aesthetically pleasing UI
- Fix the UI elements offsetting whenever the player moves
- Animated enemies
- Settings menu to allow for more player customization
- Different death styles on enemies
- More enemy variation
- Special ultimate abilities the player can use
- Load out page to choose different attack styles
- Different player projectiles
- Different projectile firing types
- Ability to gamble currency for risk/reward experience
- Optimized code structure (generics, lambda)

## TODO BEFORE TUESDAY

change enemy spawning to use camera, not gdx viewport
unit tests
update uml
update readme


