package com.riaancornelius.bot;

import com.riaancornelius.bot.data.Map;
import com.riaancornelius.bot.data.MapEntity;
import com.riaancornelius.bot.data.Move;
import com.riaancornelius.bot.pathfinding.AStarPathFinder;
import com.riaancornelius.bot.pathfinding.Mover;
import com.riaancornelius.bot.pathfinding.Path;
import com.riaancornelius.bot.pathfinding.PathFinder;
import com.riaancornelius.bot.state.Location;

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

        Location targetEnemy = map.findClosestPlayerTo(botKey);

        path = finder.findPath(this,
                map.getPlayerPosition(botKey).getX()-1,
                map.getPlayerPosition(botKey).getY()-1,
                targetEnemy.getX()-1,
                targetEnemy.getY()-1);
        if (path == null) {
            System.out.println("No path found");
        } else {
            Path.Step nextStep = path.getStep(1);
            MapEntity entityAtNextStep = map.getLocation(nextStep.getX(), nextStep.getY());
            System.out.println(nextStep + "Moving onto: " + entityAtNextStep);
            if (entityAtNextStep == MapEntity.WALL) {
                return Move.PLANT;
            }

            Move move = Move.from(nextStep, map.getPlayerPosition("C"));
            System.out.println("Path with " + path.getLength() + " starts at " + nextStep);
            System.out.println("Moving: " + move.name());
            return move;
        }

        return Move.values()[new Random().nextInt(5)];
    }
}
