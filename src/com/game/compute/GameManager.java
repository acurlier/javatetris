package com.game.compute;

public class GameManager {
    /*
    This class defines the game manager, responsible to manage the game
    clock, to instantiate new blocks, to keep track of the pixel boundary
    of the static blocks and to detect the collisions
     */
    private static final int CLIPPING_WIDTH = 2;

    private boolean[][] _gameMatrix;
    private boolean[][] _tentativeCurrentBlockMatrix;
    private CurrentBlock _currentBlock;
    private final int _gameHeight;
    private final int _gameWidth;


    public GameManager(int gameWidth, int gameHeight, float gamePace) {

        _gameWidth = gameWidth;
        _gameHeight = gameHeight;
        _gameMatrix = new boolean[gameHeight][gameWidth];
        _tentativeCurrentBlockMatrix = new boolean[_gameHeight][_gameWidth + 2*CLIPPING_WIDTH];

        for (int i = 0; i < gameHeight; i++) {
            for (int j = 0; j < gameWidth; j++) {
                _gameMatrix[i][j] = false;
            }
        }

        for (int i = 0; i < gameHeight; i++) {
            for (int j = 0; j < gameWidth + 2*CLIPPING_WIDTH; j++) {
                _tentativeCurrentBlockMatrix[i][j] = false;
            }
        }

        System.out.println("GameManager instantiated");

    }

    private void goToNextTick() {

    }

    public void instantiateCurrentBlock() {
        _currentBlock = new CurrentBlock(2, 0, _gameWidth, _gameHeight);
        _tentativeCurrentBlockMatrix = _currentBlock.getGlobalBlockMatrix();

    }

    private void verifyCurrentBlockMatrix() {
        int[] position = _currentBlock.getBlockPosition();
        if (position[1] < 2 || position[1] > (_gameWidth + 2*CLIPPING_WIDTH - 5)) { // verification is required
            // if there is any cell in one of the free gaps, the block is pushed in the opposite direction
            // this allows to manage nicely the rotations close to the game side border
            for (int i = position[0]; i < position[0] + 3; i++) {

                if (_tentativeCurrentBlockMatrix[i][0] || _tentativeCurrentBlockMatrix[i][1]) {
                    _currentBlock.moveRight();
                    break;

                } else if (_tentativeCurrentBlockMatrix[i][_gameWidth + 2*CLIPPING_WIDTH - 1] ||
                        _tentativeCurrentBlockMatrix[i][_gameWidth + 2*CLIPPING_WIDTH - 2]) {
                    _currentBlock.moveLeft();
                    break;
                }
            }

        }

    }

    private void updateGameMatrices() {

        System.out.println("Game matrix updated");
        //_gameMatrix = _currentBlock.getGlobalBlockMatrix();
    }

    public boolean[][] getGameMatrices() {
        return _tentativeCurrentBlockMatrix;
    }

    public void rotateCurrentBlock() {
        _tentativeCurrentBlockMatrix = _currentBlock.rotate();
        verifyCurrentBlockMatrix();
    }

    public void moveLateralCurrentBlock(String input) {
        if (input.equals("LEFT")) {
            _tentativeCurrentBlockMatrix = _currentBlock.moveLeft();
            verifyCurrentBlockMatrix();
        } else if (input.equals("RIGHT")) {
            _tentativeCurrentBlockMatrix = _currentBlock.moveRight();
            verifyCurrentBlockMatrix();
        }
    }

    public void moveFastCurrentBlock() {
        _tentativeCurrentBlockMatrix = _currentBlock.moveDown();
    }

    private void upDateScore() {

    }

    private void applyGamePolicy() {// detectContact() {

    }

    private void detectGameOver() {

    }

    private void exitGame() {

    }

}
