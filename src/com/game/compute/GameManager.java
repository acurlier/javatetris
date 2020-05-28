package com.game.compute;

public class GameManager {
    /*
    This class defines the game manager, responsible to manage the game
    clock, to instantiate new blocks, to keep track of the pixel boundary
    of the static blocks and to detect the collisions
     */

    private boolean[][] _gameMatrix;
    private CurrentBlock _currentBlock;
    private final int _gameHeight;
    private final int _gameWidth;

    public GameManager(int gameWidth, int gameHeight, float gamePace) {

        _gameWidth = gameWidth;
        _gameHeight = gameHeight;
        _gameMatrix = new boolean[gameHeight][gameWidth];

        for (int i = 0; i < gameHeight; i++) {
            for (int j = 0; j < gameWidth; j++) {
                _gameMatrix[i][j] = false;
            }
        }

        System.out.println("GameManager instantiated");

    }

    private void goToNextTick() {

    }

    public void instantiateCurrentBlock() {
        _currentBlock = new CurrentBlock(2, 0, _gameWidth, _gameHeight);
    }

    private void updateGameMatrices() {
        System.out.println("Game matrix updated");
        _gameMatrix = _currentBlock.getGlobalBlockMatrix();
    }

    public boolean[][] getGameMatrices() {
        updateGameMatrices();
        return _gameMatrix;
    }

    public void rotateCurrentBlock() {
        _currentBlock.rotateBlock();
    }

    public void moveLateralCurrentBlock(String input) {
        if (input.equals("LEFT")) {
            _currentBlock.moveLeft();
        } else if (input.equals("RIGHT")) {
            _currentBlock.moveRight();
        }
    }

    public void moveFastCurrentBlock() {

    }

    private void upDateScore() {

    }

    private void detectContact() {

    }

    private void detectGameOver() {

    }

    private void exitGame() {

    }

}
