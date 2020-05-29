package com.game.compute;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager {
    /*
    This class defines the game manager, responsible to manage the game
    clock, to instantiate new blocks, to keep track of the pixel boundary
    of the static blocks and to detect the collisions
     */
    private static final int CLIPPING_WIDTH = 2;

    private boolean[][] _gameMatrix;
    private boolean[][] _currentBlockMatrix;
    private boolean[][] _staticBlocksMatrix;
    private CurrentBlock _currentBlock;
    private StaticBlocks _staticBlocks;
    private final int _gameHeight;
    private final int _gameWidth;


    public GameManager(int gameWidth, int gameHeight, float gamePace) {

        _gameWidth = gameWidth;
        _gameHeight = gameHeight;
        _gameMatrix = new boolean[gameHeight][gameWidth];
        _currentBlockMatrix = new boolean[_gameHeight + CLIPPING_WIDTH][_gameWidth + 2 * CLIPPING_WIDTH];
        _staticBlocksMatrix = new boolean[_gameHeight][_gameWidth];

        _staticBlocks = new StaticBlocks(_gameWidth, _gameHeight);

        for (boolean[] row : _gameMatrix) {
            Arrays.fill(row, false);
        }
        for (boolean[] row : _currentBlockMatrix) {
            Arrays.fill(row, false);
        }
        for (boolean[] row : _staticBlocksMatrix) {
            Arrays.fill(row, false);
        }
    }

    private void goToNextTick() {

    }

    public void instantiateCurrentBlock() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 6 + 1);
        _currentBlock = new CurrentBlock(randomNum, 0, _gameWidth, _gameHeight);
        _currentBlockMatrix = _currentBlock.getGlobalBlockMatrix();

    }

    private void updateGameMatrices() {
        //_gameMatrix = _currentBlock.getGlobalBlockMatrix();
    }

    public boolean[][] getGameMatrices() {
        for(int i=0; i< _gameHeight; i++) {
            for(int j=0; j< _gameWidth; j++) {
                _gameMatrix[i][j] = _staticBlocksMatrix[i][j] || _currentBlockMatrix[i][j+2];
            }
        }
        return _gameMatrix;
    }

    public void rotateCurrentBlock() {
        _currentBlockMatrix = _currentBlock.rotate();
        preventWallCollision();
        preventGroundCollision();
    }

    public void moveLateralCurrentBlock(String input) {
        if (input.equals("LEFT")) {
            _currentBlockMatrix = _currentBlock.moveLeft();
            preventWallCollision();
        } else if (input.equals("RIGHT")) {
            _currentBlockMatrix = _currentBlock.moveRight();
            preventWallCollision();
        }
    }

    public void moveDownCurrentBlock() {
        boolean isDockable = false;
        _currentBlockMatrix = _currentBlock.moveDown();
        isDockable = preventGroundCollision();
        if (isDockable) {
            System.out.println("dock block");
            dockBlock();
        }
    }

    private void dockBlock() {
        _staticBlocks.addBlockToMatrix(_currentBlock);
        _staticBlocksMatrix = _staticBlocks.getStaticBlocksMatrix();
        instantiateCurrentBlock();

    }

    private void preventLateralBlockCollision() {
        /*
        avoid clipping with static (frozen) blocks during translation or rotation
         */
        int[] position = _currentBlock.getBlockPosition();
    }

    private boolean preventGroundCollision() {
        /*
        avoid clipping with the ground during rotation
         */
        boolean collisionAvoided = false;
        int[] position = _currentBlock.getBlockPosition();
        if (position[0] >= _gameHeight - 2) {
            for (int i = position[1]; i < position[1] + 3; i++) { // sweep through the current block matrix
                if (_currentBlockMatrix[_gameHeight + CLIPPING_WIDTH - 2][i] ||
                        _currentBlockMatrix[_gameHeight + CLIPPING_WIDTH - 1][i]) {
                    _currentBlock.moveUp();
                    collisionAvoided = true;

                }
            }
        }
        System.out.println("collision :" + collisionAvoided);
        return collisionAvoided;
    }

    private void preventWallCollision() {
        /*
        avoid clipping with the walls during translation or rotation
         */

        int[] position = _currentBlock.getBlockPosition();
        if (position[1] < 2 || position[1] >= (_gameWidth + 2 * CLIPPING_WIDTH - 4)) { // verification is required
            // if there is any cell in one of the free gaps, the block is pushed in the opposite direction
            // this allows to manage nicely the rotations close to the game side border
            for (int i = position[0]; i < position[0] + 3; i++) {

                if (_currentBlockMatrix[i][0] || _currentBlockMatrix[i][1]) {
                    _currentBlock.moveRight();
                    break;

                } else if (_currentBlockMatrix[i][_gameWidth + 2 * CLIPPING_WIDTH - 1] ||
                        _currentBlockMatrix[i][_gameWidth + 2 * CLIPPING_WIDTH - 2]) {
                    _currentBlock.moveLeft();
                    break;
                }
            }

        }

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
