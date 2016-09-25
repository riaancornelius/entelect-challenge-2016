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

    private static String botDir;

    public static void main(String... args){

        String botKey = args[0];
        botDir = args[1].replace("\"","");

        Bot bot = new Bot(botKey, botDir);

        Move nextMove = calculateMove();

        saveMoveFile(nextMove);
    }

    private static void saveMoveFile(Move nextMove) {
        File move = new File(botDir + File.separator + "move.txt");
        try {
            if (move.createNewFile()) {
                FileWriter write = new FileWriter(move);
                write.write(String.valueOf(nextMove.getRepresentation()));
                write.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Move calculateMove() {
        return Move.values()[new Random().nextInt(7)];
    }
}
