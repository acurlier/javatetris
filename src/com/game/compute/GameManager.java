package com.game.compute;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager {
    /*
    This class defines the game manager, responsible to manage the game
    clock, to instantiate new blocks, to keep track of the pixel boundary
    of the static blocks and to detect the collisions
     */
    private static final int CLIPPING_WIDTH = 3;

    private boolean[][] _gameMatrix;
    private boolean[][] _currentBlockMatrix;
    private boolean[][] _staticBlocksMatrix;
    private CurrentBlock _currentBlock;
    private StaticBlocks _staticBlocks;
    private final int _gameHeight;
    private final int _gameWidth;

    public boolean _lose;


    public GameManager(int gameWidth, int gameHeight, float gamePace) {

        _gameWidth = gameWidth;
        _gameHeight = gameHeight;
        _gameMatrix = new boolean[_gameHeight][_gameWidth];
        _currentBlockMatrix = new boolean[_gameHeight + CLIPPING_WIDTH][_gameWidth + 2 * CLIPPING_WIDTH];
        _staticBlocksMatrix = new boolean[_gameHeight + CLIPPING_WIDTH][_gameWidth + 2 * CLIPPING_WIDTH];

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

    public synchronized void instantiateCurrentBlock() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 6 + 1);
        _currentBlock = new CurrentBlock(randomNum, 0, _gameWidth, _gameHeight);
        _currentBlockMatrix = _currentBlock.getGlobalBlockMatrix();
        detectGameOver();

    }

    public boolean[][] getGameMatrices() {
        for (int i = 0; i < _gameHeight; i++) {
            for (int j = 0; j < _gameWidth; j++) {
                _gameMatrix[i][j] = _staticBlocksMatrix[i][j + CLIPPING_WIDTH] ||
                        _currentBlockMatrix[i][j + CLIPPING_WIDTH];
            }
        }
        return _gameMatrix;
    }

    public synchronized void rotateCurrentBlock() {
        if (_currentBlock.getType() != 4) { // if not a "O" square element
            _currentBlockMatrix = _currentBlock.rotate();
            preventWallClipping();
            preventDownwardBlockClipping();
            preventGroundClipping();
            preventLateralBlockClipping("ROTATION");
        }
    }

    public synchronized void moveLateralCurrentBlock(String input) {
        if (input.equals("LEFT")) {
            _currentBlockMatrix = _currentBlock.moveLeft();
        } else if (input.equals("RIGHT")) {
            _currentBlockMatrix = _currentBlock.moveRight();
        }
        preventWallClipping();
        preventLateralBlockClipping(input);
    }

    public synchronized void moveDownCurrentBlock() {
        boolean isDockable;
        _currentBlockMatrix = _currentBlock.moveDown();
        boolean isDockable_pt1 = preventGroundClipping();
        boolean isDockable_pt2 = preventDownwardBlockClipping();
        isDockable = isDockable_pt1 || isDockable_pt2;
        if (isDockable) {
            dockBlock();
        }
    }

    private synchronized void dockBlock() {
        _staticBlocks.addBlockToMatrix(_currentBlock);
        _staticBlocksMatrix = _staticBlocks.getStaticBlocksMatrix();
        instantiateCurrentBlock();
    }

    private boolean preventDownwardBlockClipping() {
        /*
        avoids clipping with the ground during rotation, returns if a docking with the ground is possible
         */
        boolean collisionWithBlockAvoided = false;
        int[] position = _currentBlock.getBlockPosition();

        for (int j = position[1]; j < position[1] + 4; j++) { // sweep through the current block matrix
            for (int i = position[0]; i < position[0] + 4; i++) { // on the 2 last rows of blocks
                if (_currentBlockMatrix[i][j] && _staticBlocksMatrix[i][j]) {
                    _currentBlock.moveUp();
                    collisionWithBlockAvoided = true;
                }
            }
        }
        System.out.println("collision :" + collisionWithBlockAvoided);
        return collisionWithBlockAvoided;
    }

    private boolean preventGroundClipping() {
        /*
        avoids clipping of the low side of the block with another element, returns if a docking with the ground is possible
         */
        boolean collisionWithGroundAvoided = false;
        int[] position = _currentBlock.getBlockPosition();

        if (position[0] <= _gameHeight + CLIPPING_WIDTH - 3) {
            for (int i = position[1]; i < position[1] + 4; i++) { // sweep through the current block matrix
                if (_currentBlockMatrix[_gameHeight + CLIPPING_WIDTH - 3][i] ||
                        _currentBlockMatrix[_gameHeight + CLIPPING_WIDTH - 2][i] ||
                        _currentBlockMatrix[_gameHeight + CLIPPING_WIDTH - 1][i]) {
                    _currentBlock.moveUp();
                    collisionWithGroundAvoided = true;

                }
            }
        }
        return collisionWithGroundAvoided;
    }

    private void preventWallClipping() {
        /*
        avoid clipping with the walls during translation or rotation
         */

        int[] position = _currentBlock.getBlockPosition();
        if (position[1] <= CLIPPING_WIDTH || position[1] >= (_gameWidth + 2 * CLIPPING_WIDTH - 6)) { // verification is required
            // if there is any cell in one of the free gaps, the block is pushed in the opposite direction
            // this allows to manage nicely the rotations close to the game side border
            for (int i = position[0]; i < position[0] + 4; i++) {

                if (_currentBlockMatrix[i][0] || _currentBlockMatrix[i][1] || _currentBlockMatrix[i][2]) {
                    _currentBlock.moveRight();
                    break;

                } else if (_currentBlockMatrix[i][_gameWidth + 2 * CLIPPING_WIDTH - 1] ||
                        _currentBlockMatrix[i][_gameWidth + 2 * CLIPPING_WIDTH - 2] ||
                        _currentBlockMatrix[i][_gameWidth + 2 * CLIPPING_WIDTH - 3]) {
                    _currentBlock.moveLeft();
                    break;
                }
            }

        }
    }

    private void preventLateralBlockClipping(String action) {
        /*
        avoid clipping with other blocks during translation or rotation. This sweeps the integrality of the block global
        matrix for coincidence with the static blocks matrix. In case of coincidence, it sends back the block from the
        direction it came from. In case of a rotation, it checks which portion of the block (left of right) is in interference
        and push the block back in the opposite direction.

         */

        int[] position = _currentBlock.getBlockPosition();
        for (int i = position[0]; i < position[0] + 4; i++) {
            for (int j = position[1]; j < position[1] + 4; j++) { // sweep the entire block for collision
                if ((_currentBlockMatrix[i][j] && _staticBlocksMatrix[i][j])) {
                    if (action.equals("LEFT")) { // if the block is coming from the left (trying to move right)
                        _currentBlock.moveRight();
                    } else if (action.equals("RIGHT")) { // if the block is coming from the right (trying to move left)
                        _currentBlock.moveLeft();
                    } else { // when caused by a rotation
                        if (j == position[1] || j == position[1] + 1) {
                            _currentBlock.moveRight();
                        } else if (j == position[1] + 2 || j == position[1] + 3) {
                            _currentBlock.moveLeft();
                        }

                    }
                }
            }
        }
    }

    private void upDateScore() {
    /*
    this method updates the score when at least one line are removed due to completeness
    1 line : 40
    2 lines : 100
    3 lines : 300
    4 lines : 1200
     */

    }

    private void applyGamePolicy() {// detectContact() {

    }

    public synchronized boolean detectGameOver() {
    /*
    upon the creation of a new block, if it interferes with the static blocks matrix (coincidence in the global current
    and static blocks matrices, then the game is over!
     */
        int[] position = _currentBlock.getBlockPosition();
        for (int i = position[0]; i < position[0] + 4; i++) {
            for (int j = position[1]; j < position[1] + 4; j++) { // sweep the entire block for collision
                if ((_currentBlockMatrix[i][j] && _staticBlocksMatrix[i][j])) {
                    _lose = true;
                    break;
                }
            }
        }
        return _lose;
    }


    private void exitGame() {

    }

}
