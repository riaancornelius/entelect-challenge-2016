package com.riaancornelius.bot;

import com.riaancornelius.bot.data.Map;
import com.riaancornelius.bot.data.Move;
import com.riaancornelius.bot.pathfinding.AStarPathFinder;
import com.riaancornelius.bot.pathfinding.Mover;
import com.riaancornelius.bot.pathfinding.Path;
import com.riaancornelius.bot.pathfinding.PathFinder;
import com.riaancornelius.bot.pathfinding.example.UnitMover;

import java.util.Random;

/**
 * @author Riaan
 */
public class Bot implements Mover {

    /** The path finder we'll use to search our map */
    private PathFinder finder;
    /** The last path found for the current unit */
    private Path path;

    public Bot(String botKey, String botDir) {
        System.out.println("Bot Key: " + botKey);
        System.out.println("Bot Dir: " + botDir);

        Map map = new Map(botDir, botKey);

        calculateMove(map).saveToFile(botDir);
    }

    private Move calculateMove(Map map) {
        finder = new AStarPathFinder(map, 500, false);

        //path = finder.findPath(this, selectedx, selectedy, x, y);

        return Move.values()[new Random().nextInt(7)];
    }
}
