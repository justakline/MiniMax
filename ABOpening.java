import java.io.*;
import java.util.*;

public class ABOpening {

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
            char[] newBoard = ABOpeningGame(board, depth);

            // Write the new board position to the output file
            writeBoardToFile(outputFile, newBoard);
            System.out.println(
                    "Input position" + Arrays.toString(board) + " \nOutput Position " + Arrays.toString(newBoard) +
                            "\nPositions Evaluated by Static Estimation: " + positionsEvaluated
                            + "\nAlpha-Beta Opening estimate: "
                            + estimate);

            System.out.println("The Alpha-Beta algorithm was executed successfully.");
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

    private static char[] ABOpeningGame(char[] board, int depth) {
        // Implement the MiniMax algorithm for the opening phase here
        // This is a placeholder for the actual MiniMax implementation
        Game game = new Game(board);
        // Start off at -1000000 because any move is better than that
        char[] newBoard = AB(game, board, depth, true, maxWorst, minWorst);
        estimate = game.staticEstimationOpening(newBoard);
        return newBoard;

    }

    private static char[] AB(Game game, char[] board, int depth, boolean maxPlayer, int alpha, int beta) {
        // Terminal depth check
        if (depth == 0) {
            positionsEvaluated++;
            return board;
        }

        // Generate moves for current player
        List<char[]> childBoards = maxPlayer ? game.generateMovesOpening(board) : game.generateBlackMoves(board);

        char[] bestBoard = null;

        for (char[] child : childBoards) {
            // Recursive call to AB for child board
            char[] result = AB(game, child, depth - 1, !maxPlayer, alpha, beta);
            int childScore = game.staticEstimationOpening(result);

            if (maxPlayer) {
                if (childScore > alpha) {
                    alpha = childScore; // Update alpha
                    bestBoard = child;

                    if (alpha >= beta) {
                        break; // Beta cut-off
                    }
                }
            } else {
                if (childScore < beta) {
                    beta = childScore; // Update beta
                    bestBoard = child;

                    if (alpha >= beta) {
                        break; // Alpha cut-off
                    }
                }
            }
        }

        // No valid move found, return original board
        if (bestBoard == null) {
            return board;
        }

        return bestBoard;
    }

}
