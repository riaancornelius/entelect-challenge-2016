package com.riaancornelius.bot.data;

import com.riaancornelius.bot.pathfinding.Mover;
import com.riaancornelius.bot.pathfinding.TileBasedMap;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Riaan
 */
public class Map implements TileBasedMap {

    private String botDir;

    private int width = -1;
    private int height = -1;

    private char[][] mapData;

    public Map(String botDir) {
        this.botDir = botDir;

        loadGameMap();
    }

    private void loadGameMap() {
        Path path = Paths.get(botDir + File.separator + "state.json");
        try {
            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
            String[] mapMeta = lines.get(0).split(",");
            for (String meta : mapMeta) {
                String[] split = meta.split(":");
                if (split[0].equalsIgnoreCase("MapHeight")) {
                    height = Integer.parseInt(split[1].trim());
                } else if (split[0].equalsIgnoreCase("MapWidth")) {
                    width = Integer.parseInt(split[1].trim());
                }
            }
            mapData = new char[height][width];
            for (int i = 0; i < height; i++) {
                String line = lines.get(i);
                System.out.println(line);
                char[] chars = line.toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    mapData[i][j] = chars[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWidthInTiles() {
        return width;
    }

    public int getHeightInTiles() {
        return height;
    }

    public void pathFinderVisited(int x, int y) {

    }

    public boolean blocked(Mover mover, int x, int y) {
        return false;
    }

    public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
        return 1;
    }
}
