import java.io.*;
import java.util.*;

public class MiniMaxOpeningBlack {

    public static int positionsEvaluated = 0;
    public static int estimate = 0;
    public static int minWorst = 1000000;
    public static int maxWorst = -1000000;

    public static void main(String[] args) throws IOException {
        // Check if the correct number of arguments is provided

        if (args.length != 3) {
            System.out.println("Usage: MiniMaxGame <inputfile> <outputfile> <depth>");
            System.exit(1);
        }

        try {
            // Extract arguments
            String inputFile = args[0];
            String outputFile = args[1];
            int depth = Integer.parseInt(args[2]);
            // Read the board position from the input file
            char[] board = readBoardFromFile(inputFile);

            // Perform MiniMax algorithm to find the best move
            char[] newBoard = minimaxGame(board, depth);

            // Write the new board position to the output file
            writeBoardToFile(outputFile, newBoard);
            System.out.println(
                    "Input position" + Arrays.toString(board) + " \nOutput Position " + Arrays.toString(newBoard) +
                            "\nPositions Evaluated by Static Estimation: " + positionsEvaluated
                            + "\nMINIMAXBlack estimate: "
                            + estimate);

            System.out.println("The MiniMaxBlack algorithm was executed successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while reading or writing files: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("The depth provided is not a valid integer.");
            e.printStackTrace();
        }
    }

    // Read the input file and check for any errors
    private static char[] readBoardFromFile(String filePath) throws IOException {
        String s = "";
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        if (!scanner.hasNext()) {
            scanner.close();
            throw new IOException("there is nothing in the file");
        }

        String line = scanner.nextLine();
        scanner.close();
        for (char c : line.toCharArray()) {
            if (c != 'W' && c != 'x' && c != 'B')
                throw new IOException("The only thing allowed is W, x, B in the file");
            else
                s += c;

        }

        if (s.length() != 23) {
            throw new IOException("There has to be exactly 23 positions, you had " + s.length());
        }
        return s.toCharArray();

    }

    private static void writeBoardToFile(String filePath, char[] board) throws IOException {

        String content = new String(board);
        // Write the board String to a file

        FileWriter file = new FileWriter(new File(filePath));
        file.write(content);
        file.close();
    }

    private static char[] minimaxGame(char[] board, int depth) {
        // Implement the MiniMax algorithm for the opening phase here
        // This is a placeholder for the actual MiniMax implementation
        Game game = new Game(board);
        char[] newBoard = miniMax(game, board, depth, false);
        estimate = game.staticEstimationOpening(newBoard);
        return newBoard;

    }

    private static char[] miniMax(Game game, char[] board, int depth, boolean maxPlayer) {

        // We are at the termingal depth
        if (depth == 0) {
            positionsEvaluated += 1;
            return board;
        }

        // Put a move on the board, do it in each position depending on whos turn it is
        List<char[]> childBoards = maxPlayer ? game.generateMovesOpening(board)
                : game.generateBlackOpeningkMoves(board);

        // Best score set to be beyomd the highest possible score for min and beyond the
        // lowest possible score for max
        // So that the first board evaluated is always the best one
        char[] bestBoard = null;
        int bestScore = maxPlayer ? maxWorst : minWorst;

        // Every possible board, let do minimax, and each time find the best board for
        // the player
        for (char[] child : childBoards) {
            char[] result = miniMax(game, child, depth - 1, !maxPlayer);

            // This one wants to find the highest score out of all the children
            int childScore = game.staticEstimationOpening(result);

            if ((maxPlayer && childScore > bestScore) || (!maxPlayer && childScore < bestScore)) {

                bestBoard = child;
                bestScore = childScore;
            }

        }
        // In case no move is possible, return the original board
        if (bestBoard == null) {
            return board;
        }

        return bestBoard;
    }

}
