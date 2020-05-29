package com.game.compute;

import java.util.Arrays;

public class CurrentBlock extends GenericBlock {

    private static final int CLIPPING_WIDTH = 2;

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

        _resetGlobalBlockMatrix();

    }

    private void _resetGlobalBlockMatrix() {

        for (boolean[] row : _globalBlockMatrix) {
            Arrays.fill(row, false);
        }
        System.out.println("Current block global matrix reset");
    }

    public boolean[][] rotate() {
        rotateShape();
        updateGlobalBlockMatrix();
        return _globalBlockMatrix;
    }

    // useful only to avoid clipping with the ground during rotation at the bottom of the screen
    public boolean[][] moveUp() {
        _positionOnGrid[0] = _positionOnGrid[0] - 1;
        updateGlobalBlockMatrix();
        return _globalBlockMatrix;
    }

    public boolean[][] moveDown() {
        _positionOnGrid[0] = _positionOnGrid[0] + 1;
        updateGlobalBlockMatrix();
        return _globalBlockMatrix;
    }

    public boolean[][] moveLeft() {
        _positionOnGrid[1] = _positionOnGrid[1] - 1;
        updateGlobalBlockMatrix();
        return _globalBlockMatrix;
    }

    public boolean[][] moveRight() {
        _positionOnGrid[1] = _positionOnGrid[1] + 1;
        updateGlobalBlockMatrix();
        return _globalBlockMatrix;
    }

    public int[] getBlockPosition() {
        return _positionOnGrid;
    }

    public boolean[][] getGlobalBlockMatrix() {
        updateGlobalBlockMatrix();
        return _globalBlockMatrix;
    }

    private void updateGlobalBlockMatrix() {
    /*
    generate a game sized matrix with a proposal for the current block matrix
    this proposal (tentative) can be either accepted or rejected by the game manager
     */
        _resetGlobalBlockMatrix();

        int startRowIndex = _positionOnGrid[0];
        int startColumnsIndex = _positionOnGrid[1];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                _globalBlockMatrix[i + startRowIndex][j + startColumnsIndex] = _blockMatrix[i][j];
            }
        }

    }

}
