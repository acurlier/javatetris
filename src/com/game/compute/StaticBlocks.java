package com.game.compute;

import java.util.Arrays;

public class StaticBlocks {

    public boolean[][] _staticBlocksMatrix;
    int _gameHeight;
    int _gameWidth;

    public StaticBlocks(int gameWidth, int gameHeight) {
        _gameHeight = gameHeight;
        _gameWidth = gameWidth;
        _staticBlocksMatrix = new boolean[_gameHeight][_gameWidth];
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
                _staticBlocksMatrix[i][j] = _staticBlocksMatrix[i][j] || compareMatrix[i][j + 2];
            }
        }
    }
    public boolean[][] getStaticBlocksMatrix() {
        return _staticBlocksMatrix;
    }

}
