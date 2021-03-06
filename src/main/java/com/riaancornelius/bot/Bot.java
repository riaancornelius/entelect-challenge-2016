package com.riaancornelius.bot;

import com.riaancornelius.bot.data.Map;
import com.riaancornelius.bot.data.MapEntity;
import com.riaancornelius.bot.data.Move;
import com.riaancornelius.bot.pathfinding.AStarPathFinder;
import com.riaancornelius.bot.pathfinding.Mover;
import com.riaancornelius.bot.pathfinding.Path;
import com.riaancornelius.bot.pathfinding.PathFinder;
import com.riaancornelius.bot.state.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
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
    private String botDir;

    public Bot(String botKey, String botDir) {
        this.botKey = botKey;
        this.botDir = botDir;
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

        System.out.print("Player is at " + currentX + "," + currentY);

        MapEntity entityAtCurrent = map.getLocation(currentX, currentY);
        System.out.println(" on " + entityAtCurrent.name());
        if (entityAtCurrent == MapEntity.EXPLOSION) {
            System.out.println("Position is unsafe - finding safe location");
            path = map.findSafeLocation(playerPosition, this, finder);
        } else {
            Location botInRange = map.botInRange(botKey, 6);
            if (botInRange != null) {
                path = finder.findPath(this, currentX, currentY, botInRange.getX() - 1, botInRange.getY() - 1);
            } else {
                path = findClosestWall(playerPosition, map, finder);
                System.out.println("Closest wall found " + path.getLength() + " steps away");
            }
            // Agro version goes after other bots:
            //Location nextTarget = map.findClosestPlayerTo(botKey, this, finder);
            //path = finder.findPath(this, currentX, currentY, nextTarget.getX() - 1, nextTarget.getY() - 1);
        }
        if (path == null) {
            System.out.println("No path found");
        } else {
            Path.Step nextStep = path.getStep(1);
            MapEntity entityAtNextStep = map.getLocation(nextStep.getX(), nextStep.getY());
            System.out.println(nextStep + "Moving onto: " + entityAtNextStep);
            if (entityAtCurrent == MapEntity.EXPLOSION && entityAtNextStep == MapEntity.WALL) {
                int x = playerPosition.getX() - 1;
                int y = playerPosition.getY() - 1;
                if (map.getMapLocationInDirection(x, y, Move.LEFT) != MapEntity.WALL
                        && map.getMapLocationInDirection(x, y, Move.LEFT) != MapEntity.INDESTRUCTIBLE_WALL){
                    System.out.println("Overriding with " + Move.LEFT);
                    return Move.LEFT;
                } else if (map.getMapLocationInDirection(x, y, Move.RIGHT) != MapEntity.WALL
                        && map.getMapLocationInDirection(x, y, Move.RIGHT) != MapEntity.INDESTRUCTIBLE_WALL){
                    System.out.println("Overriding with " + Move.RIGHT);
                    return Move.RIGHT;
                } else if (map.getMapLocationInDirection(x, y, Move.UP) != MapEntity.WALL
                        && map.getMapLocationInDirection(x, y, Move.UP) != MapEntity.INDESTRUCTIBLE_WALL){
                    System.out.println("Overriding with " + Move.UP);
                    return Move.UP;
                } else if (map.getMapLocationInDirection(x, y, Move.DOWN) != MapEntity.WALL
                        && map.getMapLocationInDirection(x, y, Move.DOWN) != MapEntity.INDESTRUCTIBLE_WALL){
                    System.out.println("Overriding with " + Move.DOWN);
                    return Move.DOWN;
                }
            } else if (entityAtCurrent != MapEntity.EXPLOSION && entityAtNextStep == MapEntity.EXPLOSION) {
                System.out.println("Overriding with " + Move.TRIGGER);
                return Move.TRIGGER;
            } else if (entityAtCurrent != MapEntity.EXPLOSION && map.isPowerUpInDirection(botKey, Move.UP)){
                System.out.println("Overriding with " + Move.UP);
                return Move.UP;
            } else if (entityAtCurrent != MapEntity.EXPLOSION && map.isPowerUpInDirection(botKey, Move.DOWN)){
                System.out.println("Overriding with " + Move.DOWN);
                return Move.DOWN;
            } else if (entityAtCurrent != MapEntity.EXPLOSION && map.isPowerUpInDirection(botKey, Move.LEFT)){
                System.out.println("Overriding with " + Move.LEFT);
                return Move.LEFT;
            } else if (entityAtCurrent != MapEntity.EXPLOSION && map.isPowerUpInDirection(botKey, Move.RIGHT)){
                System.out.println("Overriding with " + Move.RIGHT);
                return Move.RIGHT;
            } else if (entityAtCurrent != MapEntity.EXPLOSION
                    && (map.isPlayerNextToWall(botKey) || map.isPlayerNextToOpponent(botKey)
                        || map.isTargetInRange(botKey, path.getStep(path.getLength()-1))
                        || map.isWallInRange(botKey))
                    && map.playerHasBomb(botKey) && map.canSafelyPlantBomb(botKey, playerPosition, this, finder)) {
                System.out.println("Overriding with " + Move.PLANT);
                return Move.PLANT;
            }

            Move move = Move.from(nextStep, map.getPlayerPosition(botKey));
            System.out.println("Not overriding - Path with " + path.getLength() + " starts at " + nextStep);
            System.out.println("Moving: " + move.name());
            return move;
        }

        return Move.values()[new Random().nextInt(5)];
    }

    private Path findClosestWall(Location player, Map map, PathFinder finder) {
        int minCost = Integer.MAX_VALUE;
        Path path = null;
        for (int i = (player.getX() - map.getWidthInTiles()); i < player.getX() + map.getWidthInTiles(); i++) {
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

                if (mapEntity == MapEntity.WALL) {
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
