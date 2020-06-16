package com.game.compute;

import java.util.Arrays;

public class CurrentBlock extends GenericBlock {

    private static final int CLIPPING_WIDTH = 4;

    public boolean[][] _globalBlockMatrix;

    private final int _gameWidth;
    private final int _gameHeight;

    public CurrentBlock(int blockType, int rotationPattern, int gameWidth, int gameHeight) {
        super(blockType, new int[]{0, gameWidth / 2});
        /*
        generate a new CurrentBlock instance, rotationPattern is the initial rotation of the new element popping in the
        game. gameWidth and gameHeight defines the size of the game matrix
         */
        _gameWidth = gameWidth;
        _gameHeight = gameHeight;

        // the 2 additional pairs of columns allow for the configurations where an element with a fully clear row on
        // its 3x3 matrix want to stick to the side wall. It also allows to prevent clipping during rotation.
        _globalBlockMatrix =
                new boolean[_gameHeight + CLIPPING_WIDTH][_gameWidth + 2 * CLIPPING_WIDTH];

        resetGlobalBlockMatrix();

    }

    private void resetGlobalBlockMatrix() {

        for (boolean[] row : _globalBlockMatrix) {
            Arrays.fill(row, false);
        }
    }

    public boolean[][] rotate() {
        rotateShape();
        updateGlobalBlockMatrix(true);
        return _globalBlockMatrix;
    }

    // useful only to avoid clipping with the ground during rotation at the bottom of the screen
    public boolean[][] moveUp() {
        _positionOnGrid[0] = _positionOnGrid[0] - 1;
        updateGlobalBlockMatrix(false);
        return _globalBlockMatrix;
    }

    public boolean[][] moveDown() {
        _positionOnGrid[0] = _positionOnGrid[0] + 1;
        updateGlobalBlockMatrix(false);
        return _globalBlockMatrix;
    }

    public boolean[][] moveLeft() {
        _positionOnGrid[1] = _positionOnGrid[1] - 1;
        updateGlobalBlockMatrix(false);
        return _globalBlockMatrix;
    }

    public boolean[][] moveRight() {
        _positionOnGrid[1] = _positionOnGrid[1] + 1;
        updateGlobalBlockMatrix(false);
        return _globalBlockMatrix;
    }

    public int[] getBlockPosition() {
        return _positionOnGrid;
    }

    public boolean[][] getGlobalBlockMatrix() {
        updateGlobalBlockMatrix(false);
        return _globalBlockMatrix;
    }

    private synchronized void updateGlobalBlockMatrix(boolean rotate) {
    /*
    generate a game sized matrix for the current block.
    For a rotation: translate the block in order to have a rotation along the center of gravity (and not along
    the (0,0) cell)
     */
        resetGlobalBlockMatrix();
        int startRowIndex;
        int startColumnsIndex;

        if (rotate) {
            int[] shift = computeCentGravityShift();
            _positionOnGrid[0] = _positionOnGrid[0] + shift[0];
            _positionOnGrid[1] = _positionOnGrid[1] + shift[1];
        }
        startRowIndex = _positionOnGrid[0];
        startColumnsIndex = _positionOnGrid[1];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                _globalBlockMatrix[i + startRowIndex][j + startColumnsIndex] = _blockMatrix[i][j];
            }
        }
    }

    private int[] computeCentGravityShift() {
        boolean[][] tempMatrix = _gravityCenterMatrix;
        _gravityCenterMatrix = rotateShape(tempMatrix);
        int[] indexBeforeRotation = findIndexofCGInMatrix(tempMatrix);
        int[] indexAfterRotation = findIndexofCGInMatrix(_gravityCenterMatrix);

        int[] translationCG = new int[indexBeforeRotation.length];
        Arrays.setAll(translationCG, i -> indexBeforeRotation[i] - indexAfterRotation[i]);

        return translationCG;
    }

    private int[] findIndexofCGInMatrix(boolean[][] matrix) {

        int[] cgLocation = new int[2];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j]) {
                    cgLocation = new int[]{i, j};
                    break;
                }
            }
        return cgLocation;
    }
}