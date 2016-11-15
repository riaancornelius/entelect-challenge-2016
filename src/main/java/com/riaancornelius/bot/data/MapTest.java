package com.riaancornelius.bot.data;

import com.riaancornelius.bot.Bot;
import com.riaancornelius.bot.pathfinding.AStarPathFinder;
import com.riaancornelius.bot.pathfinding.Path;
import com.riaancornelius.bot.pathfinding.PathFinder;
import com.riaancornelius.bot.state.Location;
import org.junit.Test;

/**
 * @author Riaan
 */
public class MapTest {

    public static final String BOT_KEY = "A";

    @Test
    public void readMap() throws Exception {
        new Map(".", "A");
    }

    @Test
    public void generatePath() throws Exception {
        Map map = new Map(".", BOT_KEY);
        AStarPathFinder finder = new AStarPathFinder(map, 500, false);
        System.out.println("TEST >> Finding path from " + (map.getPlayerPosition(BOT_KEY).getX()-1) + "," + (map.getPlayerPosition(BOT_KEY).getY()-1));
        System.out.println("TEST >> To " + (map.getPlayerPosition("C").getX()-1) + "," + (map.getPlayerPosition("C").getY()-1));

        Bot bot = new Bot(BOT_KEY, ".");
        Path path;
        Location playerPosition = map.getPlayerPosition(BOT_KEY);
        int currentX = playerPosition.getX()-1;
        int currentY = playerPosition.getY()-1;

        System.out.println("TEST >> Player is at " + currentX + "," + currentY);

        Location nextTarget = map.findClosestPlayerTo(BOT_KEY);
        if (map.getLocation(currentX, currentY) == MapEntity.EXPLOSION) {
            System.out.println("TEST >> Position is unsafe - finding safe location");
            path = findSafeLocation(playerPosition, map, bot, finder);
            System.out.println("TEST >> safe location found at " + path.getStep(path.getLength()-1));
        } else {
            path = finder.findPath(
                    bot, currentX, currentY, nextTarget.getX() - 1, nextTarget.getY() - 1);

        }
        if (path == null) {
            System.out.println("TEST >> No path found");
        } else {
            Path.Step nextStep = path.getStep(1);
            MapEntity entityAtNextStep = map.getLocation(nextStep.getX(), nextStep.getY());
            System.out.println(nextStep + "Moving onto: " + entityAtNextStep);
            if (entityAtNextStep == MapEntity.WALL) {
                System.out.println("TEST >> Move: " + Move.PLANT);
            }

            Move move = Move.from(nextStep, map.getPlayerPosition("C"));
            System.out.println("TEST >> Path with " + path.getLength() + " starts at " + nextStep);
            System.out.println("TEST >> Moving: " + move.name());
        }

    }

    private Path findSafeLocation(Location player, Map map, Bot bot, PathFinder finder) {
        int minCost = Integer.MAX_VALUE;
        Path path = null;
        for (int i = (player.getX() - 5); i < player.getX() + 5; i++) {
            if (i <= 0
                    || i >= map.getWidthInTiles()) {
                System.out.println("Continuing");
                continue;
            }
            for (int j = player.getY() - 5; j < player.getY() + 5; j++) {
                if (j <= 0
                        || j >= map.getHeightInTiles()) {
                    System.out.println("Continuing");
                    continue;
                }
                System.out.print("TEST >> Checking for safe position at " + i + "," + j);

                MapEntity mapEntity = map.getLocation(i, j);

                System.out.print(" Found " + mapEntity);
                if (mapEntity == MapEntity.OPEN) {
                    //System.out.println("TEST >> Position is a " + mapEntity.name());
                    Path tmpPath = finder.findPath(bot,
                            player.getX()-1,
                            player.getY()-1,
                            i,
                            j);
                    int cost = tmpPath.getCost();
                    System.out.println(" with cost " + cost);
                    if (cost < minCost) {
                        minCost = cost;
                        path = tmpPath;
                        System.out.println("TEST >> Found shortest path with cost: " + minCost);
                    }
                } else {
                    System.out.println("");
                }
            }
        }
        return path;
    }

}