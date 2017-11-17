package edu.miami.cse.reversi.strategy;

import edu.miami.cse.reversi.Board;
import edu.miami.cse.reversi.Player;
import edu.miami.cse.reversi.Square;
import edu.miami.cse.reversi.Strategy;

public class AlphaBeta implements Strategy {

    private static final int MAX_DEPTH = 2;
    private static Player color;
    private static Square bestMove = null;

    private static Square chooseOne(Board board) {
        return alphaBeta(board);
    }

    private static Square alphaBeta(Board board) {
        maxValue(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, board);
        return bestMove;
    }

    private static int maxValue(int alpha, int beta, int depth, Board board) {
        if (depth >= MAX_DEPTH) {
            return evaluateMove(board);
        }

        if (board.getCurrentPossibleSquares().isEmpty()) {
            return minValue(alpha, beta, board, depth + 1);
        }

        Square tempMove = null;
        int tempResult = Integer.MIN_VALUE;
        Board newBoard = board;

        for (Square move : newBoard.getCurrentPossibleSquares()) {

            newBoard = board.play(move);

            alpha = minValue(alpha, beta, newBoard, depth + 1);

            if (alpha > tempResult) {
                tempMove = move;
                tempResult = alpha;
            }

            if (alpha >= beta) {
                break;
            }
        }
        bestMove = tempMove;
        return alpha;
    }

    private static int minValue(int alpha, int beta, Board board, int depth) {
        if (depth >= MAX_DEPTH) {
            return evaluateMove(board);
        }

        Board newBoard = board;

        if (board.getCurrentPossibleSquares().isEmpty()) {
            return maxValue(alpha, beta, depth, board);
        }

        for (Square move : newBoard.getCurrentPossibleSquares()) {
            newBoard = newBoard.play(move);

            beta = Math.min(beta, maxValue(alpha, beta, depth + 1, newBoard));

            if (beta <= alpha) {
                break;
            }

            newBoard = board;
        }

        return beta;
    }

    private static int evaluateMove(Board board) {
        int ourScore = board.getPlayerSquareCounts().get(color);
        int opponentScore = board.getPlayerSquareCounts().get(color.opponent());
        return ourScore - opponentScore
    }

    @Override
    public Square chooseSquare(Board board) {
        color = board.getCurrentPlayer();
        return chooseOne(board);
    }
}
