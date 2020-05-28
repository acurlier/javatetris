package com.game.compute;

public class CurrentBlock extends GenericBlock {

    public boolean[][] _globalBlockMatrix;
    private final int _gameWidth;
    private final int _gameHeight;

    public CurrentBlock  (int blockType, int rotationPattern, int gameWidth, int gameHeight) {
        super(blockType,new int[] {0,gameWidth/2});
        /*
        generate a new CurrentBlock instance, rotationPattern is the initial rotation of the new element popping in the
        game. gameWidth and gameHeight defines the size of the game matrix
         */
        _gameWidth =gameWidth;
        _gameHeight =gameHeight;
        _globalBlockMatrix = new boolean[gameHeight][gameWidth];
        _resetGlobalBlockMatrix();
        System.out.println("New block instantiated");

    }

    private void _resetGlobalBlockMatrix() {

        for (int i = 0; i < _gameHeight; i++) {
            for (int j = 0; j < _gameWidth; j++) {
                _globalBlockMatrix[i][j] = false;
            }
        }
        System.out.println("Current block global matrix reset");
    }

    public boolean[][] getGlobalBlockMatrix() {
        updateGlobalBlockMatrix();
        return _globalBlockMatrix;
    }

    public void moveLeft() {
        _positionOnGrid[1] = _positionOnGrid[1]-1;
    }

    public void moveRight() {
        _positionOnGrid[1] = _positionOnGrid[1]+1;
    }

    private void updateGlobalBlockMatrix() {
    /*
    generate a game sized matrix with only the current block
     */
        _resetGlobalBlockMatrix();

        int startRowIndex = _positionOnGrid[0];
        int startColumnsIndex = _positionOnGrid[1];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                _globalBlockMatrix[i+startRowIndex][j+startColumnsIndex] = _blockMatrix[i][j];
            }
        }

    }

    }
