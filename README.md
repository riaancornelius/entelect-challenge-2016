## Compile
The easiest way to compile is to open a new commmand prompt in your bot folder and run `mvn package`. For more details look [here]("https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html").

## Strategy
This bot essentially just tries to stay alive. It treats all bombs as if they are exploding immediately and tries to work out the best route to safety (Using an A* algorithm).

If it is safe, by default it tries to find the closest wall and blows it up as soon as it is in range. 

If an enemy is within a distance of 5 blocks on both the x and y directions, my bot will try to chase them down.

Other than this, there's small tweaks to try to get my bot to not do extremely silly things, but no real intelligence.