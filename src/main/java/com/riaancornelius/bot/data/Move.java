package com.riaancornelius.bot.data;

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

    /**
     * @author Riaan
     */
    private int representation;

    Move(int representation) {
        this.representation = representation;
    }

    public int getRepresentation() {
        return representation;
    }
}
