import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    // Constants for piece types
    public static final char WHITE_PIECE = 'W';
    public static final char BLACK_PIECE = 'B';
    public static final char EMPTY = 'x';

    // Game board representation
    private char[] board;

    public Game() {
        // Initialize the game board here
        board = new char[23];
    }

    public Game(char[] board) {
        // Initialize the game board here
        this.board = board;
    }

    public char[] getBoard() {
        return copyBoard(board);
    }

    // Main game logic methods
    public List<char[]> generateBlackMoves(char[] b) {

        char[] tempB = copyBoard(b);
        // Swap colors
        tempB = swapColors(tempB);

        List<char[]> list = generateMovesOpening(tempB);

        // Swap back
        for (int i = 0; i < list.size(); i++) {
            list.set(i, swapColors(list.get(i)));
        }

        // System.out.println("After 2. ");
        // for (char[] blackMove : list) {
        // System.out.println(Arrays.toString(blackMove));
        // }

        return list;
        // Swap colors and generate moves for white, then swap back
        // 1. Compute tempb
        // 2. Generate moves for tempb
        // 3. Swap colors back
        // return new List<char[]>;
    }

    public char[] swapColors(char[] b) {
        char[] swappedB = copyBoard(b);
        // Swap colors
        for (int i = 0; i < b.length; i++) {
            if (b[i] == WHITE_PIECE)
                swappedB[i] = BLACK_PIECE;
            else if (b[i] == BLACK_PIECE)
                swappedB[i] = WHITE_PIECE;
        }
        return swappedB;

    }

    // Generate moves for the opening phase
    // Calls generateAdd
    public List<char[]> generateMovesOpening(char[] b) {
        return generateAdd(b);

    }
    // Generate moves for midgame/endgame phase
    // Adds between GenerateMove and GenerateHopping based on number of pieces

    public List<char[]> generateMovesMidgameEndgame(char[] b) {
        List<char[]> list = new ArrayList<char[]>();

        int count = 0;
        for (int i = 0; i < b.length; i++) {
            if (b[i] == WHITE_PIECE) {
                count++;
            }
        }
        if (count <= 3) {
            for (char[] move : generateHopping(b)) {
                list.add(move);
            }
        }
        for (char[] move : generateMove(b)) {
            list.add(move);
        }
        return list;
    }

    // Generates moves by adding a white piece
    private List<char[]> generateAdd(char[] b) {
        List<char[]> list = new ArrayList<char[]>();

        for (int location = 0; location < b.length; location++) {
            if (b[location] == EMPTY) {
                char[] newB = copyBoard(b);
                newB[location] = WHITE_PIECE;
                if (closeMill(location, newB)) {
                    generateRemove(list, newB);
                } else {
                    list.add(newB);
                }

            }
        }

        return list;
    }

    private char[] copyBoard(char[] b) {
        char[] newBoard = new char[b.length];
        for (int i = 0; i < b.length; i++) {
            newBoard[i] = b[i];
        }
        return newBoard;
    }

    // Generates moves by moving a white piece to an adjacent location
    private List<char[]> generateMove(char[] b) {
        List<char[]> list = new ArrayList<char[]>();
        for (int location = 0; location < b.length; location++) {
            if (b[location] == WHITE_PIECE) {
                List<Integer> neighbors = neighbors(location);
                for (int j : neighbors) {
                    if (b[j] == EMPTY) {
                        char[] addB = copyBoard(b);
                        addB[location] = EMPTY;
                        addB[j] = WHITE_PIECE;
                        if (closeMill(j, addB)) {
                            generateRemove(list, addB);
                        } else {
                            list.add(addB);
                        }
                    }
                }
            }
        }
        return list;
    }

    // Generates moves by hopping a white piece to any empty location
    private List<char[]> generateHopping(char[] b) {
        List<char[]> possibleList = new ArrayList<char[]>();
        for (int alpha = 0; alpha < b.length; alpha++) {
            if (b[alpha] == WHITE_PIECE) {
                for (int beta = 0; beta < b.length; beta++) {
                    if (b[beta] == EMPTY) {
                        char[] addB = copyBoard(b);
                        addB[alpha] = EMPTY;
                        addB[beta] = WHITE_PIECE;
                        if (closeMill(beta, addB)) {
                            generateRemove(possibleList, addB);
                        } else {
                            possibleList.add(addB);
                        }
                    }
                }
            }
        }
        return possibleList;
    }

    // Generates moves by removing a black piece when a mill is formed
    private void generateRemove(List<char[]> moveList, char[] newBoard) {
        boolean isIsolatedPiece = false;
        for (int i = 0; i < newBoard.length; i++) {
            // The piece is black and we do not have a mill, ie it is isolated
            if (newBoard[i] == BLACK_PIECE && !closeMill(i, newBoard)) {
                isIsolatedPiece = true;
                char[] b = copyBoard(newBoard);
                b[i] = EMPTY;
                moveList.add(b);
            }

        }

        // There are no isolated pieces
        // remove any piece at all
        if (!isIsolatedPiece) {
            for (int i = 0; i < newBoard.length; i++) {
                // The piece is black and we do have a mill
                if (newBoard[i] == BLACK_PIECE) {
                    char[] b = copyBoard(newBoard);
                    b[i] = EMPTY;
                    moveList.add(b);
                }

            }
        }
    }

    // Returns a list of neighbor positions for a given location
    private List<Integer> neighbors(int location) {

        switch (location) {
            case 0:
                return Arrays.asList(1, 3, 8);
            case 1:
                return Arrays.asList(0, 2, 4);
            case 2:
                return Arrays.asList(1, 5, 13);
            case 3:
                return Arrays.asList(0, 9, 6, 4);
            case 4:
                return Arrays.asList(1, 3, 5);
            case 5:
                return Arrays.asList(4, 2, 12, 7);
            case 6:
                return Arrays.asList(3, 12, 7);
            case 7:
                return Arrays.asList(6, 5, 11);
            case 8:
                return Arrays.asList(0, 20, 9);
            case 9:
                return Arrays.asList(8, 3, 17, 10);
            case 10:
                return Arrays.asList(9, 6, 14);
            case 11:
                return Arrays.asList(7, 16, 12);
            case 12:
                return Arrays.asList(11, 5, 19, 13);
            case 13:
                return Arrays.asList(12, 2, 22);
            case 14:
                return Arrays.asList(10, 17, 15);
            case 15:
                return Arrays.asList(14, 16, 18);
            case 16:
                return Arrays.asList(15, 11, 19);
            case 17:
                return Arrays.asList(9, 20, 14, 18);
            case 18:
                return Arrays.asList(17, 19, 21);
            case 19:
                return Arrays.asList(18, 22, 12, 16);
            case 20:
                return Arrays.asList(8, 21, 17);
            case 21:
                return Arrays.asList(20, 22, 18);
            case 22:
                return Arrays.asList(21, 19, 13);
        }

        return null;
    }

    // Checks if placing a piece at location closes a mill ie makes a 3 in a row of
    // the same color
    private boolean closeMill(int location, char[] newBoard) {
        char color = newBoard[location];
        if (color == EMPTY)
            return false;
        int[][] millCombinations = {
                { 0, 1, 2 }, { 3, 4, 5 }, { 8, 9, 10 }, { 11, 12, 13 }, // Horizontal mills
                { 14, 15, 16 }, { 17, 18, 19 }, { 20, 21, 22 },
                { 0, 8, 20 }, { 3, 9, 17 }, { 6, 10, 14 }, { 7, 11, 16 }, // Vertical Mills
                { 5, 12, 19 }, { 2, 13, 22 },
                { 0, 3, 6 }, { 2, 5, 7 }, { 20, 17, 15 }, { 22, 19, 16 } // Diagnoal Mills
        };

        // Check every combination, if a piece in the comvination is empty then you know
        // its not three in a row
        // If the combination does not contain the location Im looking at then forget
        // about this combination
        for (int[] combination : millCombinations) {
            int numberOfPieces = 0;
            boolean contains = false;
            for (int pieceIndex : combination) {
                int piece = newBoard[pieceIndex];
                if (piece == EMPTY || piece != color) {
                    break;
                }

                numberOfPieces++;
                if (pieceIndex == location) {
                    contains = true;
                }
            }
            if (numberOfPieces == 3 && contains) {
                return true;
            }

        }
        return false;
    }

    // Static estimation for MidgameEndgame
    public int staticEstimationMidgameEndgame(char[] b) {
        int whitePieces = calculateColorPieces(WHITE_PIECE, b);
        int blackPieces = calculateColorPieces(BLACK_PIECE, b);
        int blackMoves = generateBlackMoves(b).size();
        if (blackPieces <= 2)
            return 10000;
        else if (whitePieces <= 2)
            return -10000;
        else if (blackMoves == 0)
            return 10000;
        return (1000 * (whitePieces - blackPieces)) - blackMoves;
    }

    // Static estimation for Opening
    public int staticEstimationOpening(char[] b) {
        return calculateColorPieces(WHITE_PIECE, b) - calculateColorPieces(BLACK_PIECE, b);
    }

    private int calculateColorPieces(char color, char[] b) {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            if (b[i] == color)
                count++;
        }
        return count;
    }

    // Add any additional methods or helper functions you need
}