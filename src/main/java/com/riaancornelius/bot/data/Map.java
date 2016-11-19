package com.riaancornelius.bot.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riaancornelius.bot.Bot;
import com.riaancornelius.bot.pathfinding.Mover;
import com.riaancornelius.bot.pathfinding.Path;
import com.riaancornelius.bot.pathfinding.PathFinder;
import com.riaancornelius.bot.pathfinding.TileBasedMap;
import com.riaancornelius.bot.state.Bomb;
import com.riaancornelius.bot.state.GameBlock;
import com.riaancornelius.bot.state.Location;
import com.riaancornelius.bot.state.RegisteredPlayerEntity;
import com.riaancornelius.bot.state.State;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
    private java.util.Map<String, RegisteredPlayerEntity> playerData = new HashMap<>();
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

        for (RegisteredPlayerEntity player : state.getRegisteredPlayerEntities()) {
            playerData.put(player.getKey(), player);
        }

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
                    if (gameBlock.getBomb()!=null) {
                        bombs.add(gameBlock.getBomb());
                    }
                } else if (entity==MapEntity.BOMB){
                    bombs.add(gameBlock.getBomb());
                }
                //System.out.println("");
                mapData[gameBlock.getLocation().getX()-1][gameBlock.getLocation().getY()-1] = entity;
            }
        }
        processBombs();
        System.out.println("Map loaded");
    }

    // Converts bombs into explosions - This treats all bombs on the map as exploding. 
    // Simplifies some logic at the expense of efficiency. 
    private void processBombs() {
        for (Bomb bomb : bombs) {
            int x = bomb.getLocation().getX()-1;
            int y = bomb.getLocation().getY()-1;
            System.out.println("Found bomb at " + x + "," + y);
            mapData[x][y] = MapEntity.EXPLOSION;
            for (Integer i = 1; i <= bomb.getBombRadius(); i++) {
                if (x + i < getWidthInTiles() &&
                        (mapData[x + i][y] == MapEntity.OPEN || mapData[x + i][y] == MapEntity.PLAYER)) {
                    //System.out.println("Changing to EXPLOSION: " + (x+i) + "," + (y));
                    mapData[x + i][y] = MapEntity.EXPLOSION;
                } else {
                    break;
                }
            }
            for (Integer i = 1; i <= bomb.getBombRadius(); i++) {
                if (x - i > 0 && (mapData[x - i][y] == MapEntity.OPEN || mapData[x - i][y] == MapEntity.PLAYER)) {
                    mapData[x - i][y] = MapEntity.EXPLOSION;
                } else {
                    break;
                }
            }
            for (Integer i = 1; i <= bomb.getBombRadius(); i++) {
                if (y + i < getHeightInTiles() &&
                        (mapData[x][y + i] == MapEntity.OPEN || mapData[x][y + i] == MapEntity.PLAYER)) {
                    mapData[x][y + i] = MapEntity.EXPLOSION;
                } else {
                    break;
                }
            }
            for (Integer i = 1; i <= bomb.getBombRadius(); i++) {
                if (y-i > 0
                        && (mapData[x][y-i] == MapEntity.OPEN || mapData[x][y-i] == MapEntity.PLAYER)) {
                    mapData[x][y-i] = MapEntity.EXPLOSION;
                } else {
                    break;
                }
            }
        }
    }

    public boolean canSafelyPlantBomb(String botKey, Location location, Bot bot, PathFinder finder) {
        System.out.println("Can safely plant bomb?");
        Bomb bomb = new Bomb();
        RegisteredPlayerEntity player = playerData.get(botKey);
        bomb.setLocation(location);
        bomb.setBombRadius(player.getBombRadius());
        int bombTimer = player.getBombBag() * 3;
        System.out.println("Bombtimer is currently " + bombTimer);
        bomb.setBombTimer(bombTimer);
        bomb.setOwner(player);
        bombs.add(bomb);

        MapEntity[][] backupData = copyMapData();
        mapData[location.getX()-1][location.getY()-1] = MapEntity.BOMB;
        processBombs();

        Path safeLocation = findSafeLocation(location, bot, finder);
        System.out.println("Found closest safe location with " + safeLocation.getLength() + " steps");

        bombs.remove(bombs.size()-1);
        mapData = backupData;

        return safeLocation.getLength() <= bomb.getBombTimer()+1;
    }

    public boolean playerHasBomb(String botKey) {
        RegisteredPlayerEntity player = playerData.get(botKey);
        int count = 0;
        for (Bomb bomb : bombs) {
            if (bomb.getOwner().getKey().equalsIgnoreCase(botKey)){
                count++;
            }
        }
        return count < player.getBombBag();
    }

    public Path findSafeLocation(Location player, Bot bot, PathFinder finder) {
        int minCost = Integer.MAX_VALUE;
        Path path = null;
        for (int i = (player.getX() - 5); i < player.getX() + 5; i++) {
            if (i <= 0 || i >= getWidthInTiles()) {
                continue;
            }
            for (int j = player.getY() - 5; j < player.getY() + 5; j++) {
                if (j <= 0 || j >= getHeightInTiles()) {
                    continue;
                }

                MapEntity mapEntity = getLocation(i, j);

                if (mapEntity == MapEntity.OPEN) {
                    Path tmpPath = finder.findPath(bot, player.getX() - 1, player.getY() - 1, i, j);
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

    public boolean isPlayerNextToWall(String botKey) {
        int x = getPlayerPosition(botKey).getX()-1;
        int y = getPlayerPosition(botKey).getY()-1;
        return (mapData[x+1][y] == MapEntity.WALL
                || mapData[x-1][y] == MapEntity.WALL
                || mapData[x][y+1] == MapEntity.WALL
                || mapData[x][y-1] == MapEntity.WALL);
    }

    public boolean isPlayerNextToOpponent(String botKey) {
        int x = getPlayerPosition(botKey).getX()-1;
        int y = getPlayerPosition(botKey).getY()-1;
        return (mapData[x+1][y] == MapEntity.PLAYER
                || mapData[x-1][y] == MapEntity.PLAYER
                || mapData[x][y+1] == MapEntity.PLAYER
                || mapData[x][y-1] == MapEntity.PLAYER);
    }

    public boolean isPowerUpInDirection(String botKey, Move direction) {
        int x = getPlayerPosition(botKey).getX()-1;
        int y = getPlayerPosition(botKey).getY()-1;
        MapEntity entity = getMapLocationInDirection(x, y, direction);
        return entity == MapEntity.POWERUP_SUPER
                || entity == MapEntity.POWERUP_BOMB_BAG
                || entity == MapEntity.POWERUP_BOMB_RADIUS;
    }

    public boolean isWallInRange(String botKey) {
        Location playerPosition = getPlayerPosition(botKey);
        Integer range = playerData.get(botKey).getBombRadius();
        int x = playerPosition.getX()-1;
        int y = playerPosition.getY()-1;
        for (Integer i = 0; i <= range; i++) {
            if ((y-i > 0 && mapData[x][y-i] == MapEntity.WALL && mapData[x][y-1] != MapEntity.INDESTRUCTIBLE_WALL)
                    || (y+i < width && mapData[x][y+i] == MapEntity.WALL && mapData[x][y+1] != MapEntity.INDESTRUCTIBLE_WALL)
                    || (x-i > 0 && mapData[x-i][y] == MapEntity.WALL && mapData[x-1][y] != MapEntity.INDESTRUCTIBLE_WALL)
                    || (y+i < height && mapData[x+i][y] == MapEntity.WALL && mapData[x+1][y] != MapEntity.INDESTRUCTIBLE_WALL)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTargetInRange(String botKey, Path.Step target) {
        Location playerPosition = getPlayerPosition(botKey);
        Integer range = playerData.get(botKey).getBombRadius();
        return ((playerPosition.getX()-1 == target.getX() && Math.abs((playerPosition.getY()-1)-target.getY()) <= range)
                || (playerPosition.getY()-1 == target.getY() && Math.abs((playerPosition.getX()-1)-target.getX()) <= range));
    }

    public MapEntity getMapLocationInDirection(int x, int y, Move direction) {
        switch (direction) {
            case UP:
                return mapData[x][y-1];
            case DOWN:
                return mapData[x][y+1];
            case LEFT:
                return mapData[x-1][y];
            case RIGHT:
                return mapData[x+1][y];
        }
        return MapEntity.OPEN;
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
                //|| mapData[x][y] == MapEntity.EXPLOSION
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

    public MapEntity[][] copyMapData(){
        MapEntity[][] copy = new MapEntity[width][height];
        for (int i = 0; i < mapData.length; i++) {
            copy[i] = Arrays.copyOf(mapData[i], mapData[i].length);
        }
        return copy;
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
