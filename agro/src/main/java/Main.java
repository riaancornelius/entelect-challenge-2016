import com.riaancornelius.bot.Bot;
import com.riaancornelius.bot.data.Move;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * @author Riaan
 */
public class Main {

    public static void main(String... args){
        String botKey = args[0];
        String botDir = args[1].replace("\"","");

        new Bot(botKey, botDir);
    }
}
