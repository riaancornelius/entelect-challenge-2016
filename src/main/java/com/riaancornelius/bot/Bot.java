package com.riaancornelius.bot;

import com.riaancornelius.bot.data.Map;

/**
 * @author Riaan
 */
public class Bot {

    private String botKey;
    private String botDir;

    public Bot(String botKey, String botDir) {
        this.botKey = botKey;
        this.botDir = botDir;

        System.out.println("Bot Key: " + botKey);
        System.out.println("Bot Dir: " + botDir);

        Map map = new Map(botDir);
    }
}
