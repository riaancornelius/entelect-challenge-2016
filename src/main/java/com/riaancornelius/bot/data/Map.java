package com.riaancornelius.bot.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riaancornelius.bot.pathfinding.Mover;
import com.riaancornelius.bot.pathfinding.TileBasedMap;
import com.riaancornelius.bot.state.GameBlock;
import com.riaancornelius.bot.state.State;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Riaan
 */
public class Map implements TileBasedMap {

    private String botDir;

    private int width = -1;
    private int height = -1;

    private MapEntity[][] mapData;

    public Map(String botDir) {
        this.botDir = botDir;

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

        mapData = new MapEntity[height][width];
        for (int i = 0; i < state.getGameBlocks().size(); i++) {
            List<GameBlock> gameBlocks = state.getGameBlocks().get(i);
            for (int j = 0; j < gameBlocks.size(); j++) {
                GameBlock gameBlock = gameBlocks.get(j);
                System.out.println("Reading pos["+i+"]["+j+"]");
                MapEntity entity = gameBlock.getEntity() == null ?
                        MapEntity.OPEN : MapEntity.parse(gameBlock.getEntity().get$type());
                mapData[gameBlock.getLocation().getX()-1][gameBlock.getLocation().getY()-1] = entity;
            }
        }
        System.out.println("Done");
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
        return mapData[x][y] == MapEntity.INDESTRUCTIBLE_WALL
                || mapData[x][y] == MapEntity.PLAYER
                || mapData[x][y] == MapEntity.WALL;
    }

    public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
        return 1;
    }
}
