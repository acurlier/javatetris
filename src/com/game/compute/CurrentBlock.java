package com.game.compute;

public class CurrentBlock extends GenericBlock {

    private static final int CLIPPING_WIDTH = 2;

    public boolean[][] _tentativeGlobalBlockMatrix;
    public boolean[][] _verifiedGlobalBlockMatrix;
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

        /*
        the _tentativeGlobalBlockMatrix is a temp matrix which reflex the last user input. It is analysed by the
        game manager that might accept it - which cause it to transfer it to _verifiedGlobalBlockMatrix or reject it
        the _tentativeGlobalBlockMatrix has two additional columns on each side in order to allow the elements to
        stick to the wall even if they have empty cells
         */

        _tentativeGlobalBlockMatrix =
                new boolean[_gameHeight][_gameWidth+2*CLIPPING_WIDTH];

        _verifiedGlobalBlockMatrix = new boolean[_gameHeight][_gameWidth];
        _resetTentativeGlobalBlockMatrix();

        System.out.println("New block instantiated");

    }

    private void _resetTentativeGlobalBlockMatrix() {

        for (int i = 0; i < _gameHeight; i++) {
            for (int j = 0; j < _gameWidth+ 2*CLIPPING_WIDTH; j++) {
                _tentativeGlobalBlockMatrix[i][j] = false;
            }
        }
        System.out.println("Current block global matrix reset");
    }

    public boolean[][] rotate() {
        rotateShape();
        updateTentativeGlobalBlockMatrix();
        return _tentativeGlobalBlockMatrix;
    }

    public boolean[][] moveDown() {
        _positionOnGrid[0] = _positionOnGrid[0]+1;
        updateTentativeGlobalBlockMatrix();
        return _tentativeGlobalBlockMatrix;
    }

    public boolean[][] moveLeft() {
        _positionOnGrid[1] = _positionOnGrid[1]-1;
        updateTentativeGlobalBlockMatrix();
        return _tentativeGlobalBlockMatrix;
    }

    public boolean[][] moveRight() {
        _positionOnGrid[1] = _positionOnGrid[1]+1;
        updateTentativeGlobalBlockMatrix();
        return _tentativeGlobalBlockMatrix;
    }

    public int[] getBlockPosition() {
        return _positionOnGrid;
    }

    public boolean[][] getGlobalBlockMatrix() {
        updateTentativeGlobalBlockMatrix();
        return _tentativeGlobalBlockMatrix;
    }

    private void updateTentativeGlobalBlockMatrix() {
    /*
    generate a game sized matrix with a proposal for the current block matrix
    this proposal (tentative) can be either accepted or rejected by the game manager
     */
        _resetTentativeGlobalBlockMatrix();

        int startRowIndex = _positionOnGrid[0];
        int startColumnsIndex = _positionOnGrid[1];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                _tentativeGlobalBlockMatrix[i+startRowIndex][j+startColumnsIndex] = _blockMatrix[i][j];
            }
        }

    }

    }
