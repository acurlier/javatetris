package com.game.compute;

import java.util.Arrays;

public class StaticBlocks {

    private static final int CLIPPING_WIDTH = 3;

    public boolean[][] _staticBlocksMatrix;
    int _gameHeight;
    int _gameWidth;

    public StaticBlocks(int gameWidth, int gameHeight) {
        _gameHeight = gameHeight;
        _gameWidth = gameWidth;
        _staticBlocksMatrix = new boolean[_gameHeight + CLIPPING_WIDTH][_gameWidth + 2 * CLIPPING_WIDTH];
        for (boolean[] row : _staticBlocksMatrix) {
            Arrays.fill(row, false);
        }

    }

    public void addBlockToMatrix(CurrentBlock block) {
        // attention: CurrentBlock::_globalBlockMatrix is larger (by 4 columns) than _staticBlocksMatrix because
        // of the free gaps
        boolean[][] compareMatrix = block.getGlobalBlockMatrix();

        for (int i = 0; i < _staticBlocksMatrix.length; i++) {
            for (int j = 0; j < _staticBlocksMatrix[0].length; j++) {
                _staticBlocksMatrix[i][j] = _staticBlocksMatrix[i][j] || compareMatrix[i][j];
            }
        }
    }

    public boolean[][] getStaticBlocksMatrix() {
        return _staticBlocksMatrix;
    }

}
