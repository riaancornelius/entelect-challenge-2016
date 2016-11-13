package com.riaancornelius.bot.data;

import com.riaancornelius.bot.Bot;
import com.riaancornelius.bot.pathfinding.AStarPathFinder;
import com.riaancornelius.bot.pathfinding.Path;
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
        System.out.println("Finding path from " + (map.getPlayerPosition(BOT_KEY).getX()-1) + "," + (map.getPlayerPosition(BOT_KEY).getY()-1));
        System.out.println("To " + (map.getPlayerPosition("C").getX()-1) + "," + (map.getPlayerPosition("C").getY()-1));

        Path path = finder.findPath(new Bot(BOT_KEY, "."),
                map.getPlayerPosition(BOT_KEY).getX()-1,
                map.getPlayerPosition(BOT_KEY).getY()-1,
                map.getPlayerPosition("C").getX()-1,
                map.getPlayerPosition("C").getY()-1);
        if (path == null) {
            System.out.println("No path found");
        } else {
            for (int i = 0; i < path.getLength(); i++) {
                System.out.println("Step " + i + ":" + path.getStep(i));
            }
            System.out.println("Moving: " + Move.from(path.getStep(0), map.getPlayerPosition("C")).name());
        }
    }


}