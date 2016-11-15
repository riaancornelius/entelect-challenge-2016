package com.riaancornelius.bot;

import com.riaancornelius.bot.data.Map;
import com.riaancornelius.bot.data.MapEntity;
import com.riaancornelius.bot.data.Move;
import com.riaancornelius.bot.pathfinding.AStarPathFinder;
import com.riaancornelius.bot.pathfinding.Mover;
import com.riaancornelius.bot.pathfinding.Path;
import com.riaancornelius.bot.pathfinding.PathFinder;
import com.riaancornelius.bot.state.Location;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Riaan
 */
public class Bot implements Mover {

    /** The path finder we'll use to search our map */
    private PathFinder finder;
    /** The last path found for the current unit */
    private Path path;
    private String botKey;

    public Bot(String botKey, String botDir) {
        this.botKey = botKey;
        System.out.println("Bot Key: " + botKey);
        System.out.println("Bot Dir: " + botDir);

        Map map = new Map(botDir, botKey);

        calculateMove(map).saveToFile(botDir);
    }

    private Move calculateMove(Map map) {
        finder = new AStarPathFinder(map, 500, false);

        Location playerPosition = map.getPlayerPosition(botKey);
        int currentX = playerPosition.getX()-1;
        int currentY = playerPosition.getY()-1;

        System.out.println("Player is at " + currentX + "," + currentY);

        Location nextTarget = map.findClosestPlayerTo(botKey);
        if (map.getLocation(currentX, currentY) == MapEntity.EXPLOSION) {
            System.out.println("Position is unsafe - finding safe location");
            path = findSafeLocation(playerPosition, map, finder);
        } else {
            path = finder.findPath(this, currentX, currentY, nextTarget.getX() - 1, nextTarget.getY() - 1);
        }
        if (path == null) {
            System.out.println("No path found");
        } else {
            Path.Step nextStep = path.getStep(1);
            MapEntity entityAtNextStep = map.getLocation(nextStep.getX(), nextStep.getY());
            System.out.println(nextStep + "Moving onto: " + entityAtNextStep);
            if (entityAtNextStep == MapEntity.WALL) {
                System.out.println("Planting bomb");
                return Move.PLANT;
            }

            Move move = Move.from(nextStep, map.getPlayerPosition(botKey));
            System.out.println("Path with " + path.getLength() + " starts at " + nextStep);
            System.out.println("Moving: " + move.name());
            return move;
        }

        return Move.values()[new Random().nextInt(5)];
    }

    private Path findSafeLocation(Location player, Map map, PathFinder finder) {
        int minCost = Integer.MAX_VALUE;
        Path path = null;
        for (int i = (player.getX() - 5); i < player.getX() + 5; i++) {
            if (i <= 0
                    || i >= map.getWidthInTiles()) {
                continue;
            }
            for (int j = player.getY() - 5; j < player.getY() + 5; j++) {
                if (j <= 0
                        || j >= map.getHeightInTiles()) {
                    continue;
                }

                MapEntity mapEntity = map.getLocation(i, j);

                if (mapEntity == MapEntity.OPEN) {
                    Path tmpPath = finder.findPath(this, player.getX() - 1, player.getY() - 1, i, j);
                    int cost = tmpPath.getCost();
                    if (cost < minCost) {
                        minCost = cost;
                        path = tmpPath;
                    }
                }
            }
        }
        return path;
    }

}
