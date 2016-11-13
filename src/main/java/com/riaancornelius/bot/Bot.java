package com.riaancornelius.bot;

import com.riaancornelius.bot.data.Map;
import com.riaancornelius.bot.data.Move;

import java.util.Random;

/**
 * @author Riaan
 */
public class Bot {

    public Bot(String botKey, String botDir) {
        System.out.println("Bot Key: " + botKey);
        System.out.println("Bot Dir: " + botDir);

        Map map = new Map(botDir);

        calculateMove(map).saveToFile(botDir);
    }

    private Move calculateMove(Map map) {
        return Move.values()[new Random().nextInt(7)];
    }
}
