How to run the code: type "gradle clean build run" in the terminal. 
    - There might be times where the program does not perform as it should, in this case, close IntelliJ and open it again. 
    
    - When running the program, make sure you are not in a call (on the same device that you use to run the program) as this might slow down the program and make it behave wrong.

Files and clases involved in the design patterns:
1. Factory method to create projectiles:
    Folder: entities
    Class: AlienProjectile, AlienProjectileCreator, PlayerProjectile, PlayerProjectileCreator, Projectile, ProjectileCreator, Alien

2. State pattern to change the state/colour of bunkers:
    Folder: entities
    Class: BunkerState, GreenBunker, YellowBunker, RedBunker, Bunker

3. Builder pattern to build enemies (aliens):
    Folder: entities
    Class: AlienBuilder, ConcreteAlienBuilder, Alien

4. Builder pattern to build bunkers:
    Folder: entities
    Class: BunkerBuilder, ConcreteBunkerBuilder, Bunker

5. Strategy pattern to control enemy projectiles' behaviour:
    Folder: entities
    Class: FastProjectileStrategy, SlowProjectileStrategy, ProjectileStrategy, AlienProjectile

Anything else that marker should know: when the player dies or in any circumstances that the game has to stop, the game will freeze (i.e., freeze means end game).