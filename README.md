This game uses JDK 18. We suggest downloading the SDK here at Adoptium because it has many built-in libraries.
https://adoptium.net/temurin/releases/?version=18&package=jdk&arch=any

## Crowd Survivor
Inspired by the game "Vampire Survivors".

## TODO
Draw art assets

** HAVE TO FIX **
Create spawning enemies (adjust to spawn outside of camera)
Fix camera jittering (stuttering)
research to see if texture can be rotated and only texture rotated, while keeping actual hitbox the same
Test to see if batch.begin() on every sprite is causing performance issues
Create working pause menu
Different enemies
enemy drop currency 
enemy drop exp (enemy manager, add xp worth instance variable to enemies -> randomize)
implement boss
player level up system
player stat choice when level up
game UI in stageUI in CrowdSurvivor file
implement SFX for game
implement save feature
implement currency

** OPTIMIZATIONS **
consolidate all mouse functionality into one page (preferably InGameScreen)
move player control handling into player manager

** UNIT TESTS  MAYBE **


** REMOVED IF NO TIME **
Create ultimate effect
Create player loadout page
create gacha
create shop
create perma upgrades
