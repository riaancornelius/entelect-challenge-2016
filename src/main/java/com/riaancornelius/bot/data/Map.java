package com.riaancornelius.bot.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riaancornelius.bot.pathfinding.Mover;
import com.riaancornelius.bot.pathfinding.TileBasedMap;
import com.riaancornelius.bot.state.Bomb;
import com.riaancornelius.bot.state.GameBlock;
import com.riaancornelius.bot.state.Location;
import com.riaancornelius.bot.state.State;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Riaan
 */
public class Map implements TileBasedMap {

    private String botDir;
    private String botRepresentation;

    private int width = -1;
    private int height = -1;

    private MapEntity[][] mapData;
    /** Indicator if a given tile has been visited during the search */
    private boolean[][] visited;

    private java.util.Map<String, Location> players = new HashMap<>();
    private List<Bomb> bombs = new ArrayList<>();

    public Map(String botDir, String botRepresentation) {
        this.botDir = botDir;
        this.botRepresentation = botRepresentation;

        try {
            loadGameState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGameState() throws IOException {
        //read json file data to String
        byte[] jsonData = Files.readAllBytes(Paths.get(botDir + File.separator + "state.json"));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object
        State state = objectMapper.readValue(jsonData, State.class);

        height = state.getMapHeight();
        width = state.getMapWidth();

        visited = new boolean[width][height];

        mapData = new MapEntity[height][width];
        for (int i = 0; i < state.getGameBlocks().size(); i++) {
            List<GameBlock> gameBlocks = state.getGameBlocks().get(i);
            for (int j = 0; j < gameBlocks.size(); j++) {
                GameBlock gameBlock = gameBlocks.get(j);
                //System.out.print("Reading pos["+(i+1)+"]["+(j+1)+"]: ");
                MapEntity entity = MapEntity.parse(gameBlock);
                //System.out.print(entity.name() + " ");
                if (entity==MapEntity.PLAYER) {
                    String key = (String) gameBlock.getEntity().getAdditionalProperties().get("Key");
                    //System.out.print(key + " ");
                    if (key.equals(botRepresentation)) {
                        //System.out.print("** ");
                    }
                    players.put(key, gameBlock.getLocation());
                } else if (entity==MapEntity.BOMB){
                    bombs.add(gameBlock.getBomb());
                }
                //System.out.println("");
                mapData[gameBlock.getLocation().getX()-1][gameBlock.getLocation().getY()-1] = entity;
            }
        }
        System.out.println("Map loaded");
    }

    public int getWidthInTiles() {
        return width;
    }

    public int getHeightInTiles() {
        return height;
    }

    public void pathFinderVisited(int x, int y) {
        visited[x][y] = true;
    }

    public boolean blocked(Mover mover, int x, int y) {
        return mapData[x][y] == MapEntity.INDESTRUCTIBLE_WALL
                //|| mapData[x][y] == MapEntity.PLAYER
                //|| mapData[x][y] == MapEntity.WALL
                || mapData[x][y] == MapEntity.EXPLOSION
                || mapData[x][y] == MapEntity.BOMB;
    }

    public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
        return mapData[tx][ty].getCost();
    }

    /**
     * Clear the array marking which tiles have been visted by the path
     * finder.
     */
    public void clearVisited() {
        for (int x=0;x<getWidthInTiles();x++) {
            for (int y=0;y<getHeightInTiles();y++) {
                visited[x][y] = false;
            }
        }
    }

    public Location getPlayerPosition(String botKey) {
        return players.get(botKey);
    }

    public Location findClosestPlayerTo(String botKey) {
        Location current = players.get(botKey);
        double minDistance = 1000;
        Location closestPlayer = null;
        for (String bot : players.keySet()) {
            if (bot.equals(botKey)) {
                continue;
            } else {
                Location other = players.get(bot);
                double distance = distance(current, other);
                if (distance < minDistance) {
                    closestPlayer = other;
                }
            }
        }
        return closestPlayer;
    }

    private double distance(Location location, Location other) {
        double deltaX = location.getX() - other.getX();
        double deltaY = location.getY() - other.getY();
        double result = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        return result;
    }

    public MapEntity getLocation(int x, int y) {
        return mapData[x][y];
    }
}
