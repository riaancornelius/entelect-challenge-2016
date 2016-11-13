package com.riaancornelius.bot.data;

import com.riaancornelius.bot.pathfinding.Path;
import com.riaancornelius.bot.state.Location;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Riaan
 */
public enum Move {
    LEFT(1),    // Move Left - Moves one block left.
    RIGHT(2),   // Move Right - Moves one block right.
    UP(3),      // Move Up - Moves one block up.
    DOWN(4),    // Move Down - Moves one block down.
    PLANT(5),   // Plant Bomb - Plants a bomb (If there are bombs in your bomb bag).
    TRIGGER(6); // Trigger Bomb - Takes the bomb with the lowest count down and sets the countdown to 1.

    private int representation;

    Move(int representation) {
        this.representation = representation;
    }

    public int getRepresentation() {
        return representation;
    }

    public static Move from(Path.Step step, Location current) {
        if (step.getY() < current.getY()-1) {
            return Move.DOWN;
        } else if (step.getY() > current.getY()-1) {
            return Move.UP;
        } else if (step.getX() > current.getX()-1) {
            return Move.RIGHT;
        } else if (step.getX() < current.getX()-1) {
            return Move.LEFT;
        }
        return null;
    }

    public void saveToFile(String botDir) {
        File move = new File(botDir + File.separator + "move.txt");
        try {
            if (move.createNewFile()) {
                FileWriter write = new FileWriter(move);
                write.write(String.valueOf(this.getRepresentation()));
                write.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
