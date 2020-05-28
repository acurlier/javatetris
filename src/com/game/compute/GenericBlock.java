package com.game.compute;

public class GenericBlock {
    private static final boolean TYPE_BLOCK_MATRIX[][][] = {
            {{false, false, false}, {false, false, false}, {true, true, true}},     // element 0
            {{false, false, false}, {false, true, true}, {true, true, false}},      // element 1
            {{false, false, false}, {false, true, false}, {true, true, true}},      // element 2
            {{false, false, false}, {true, true, false}, {false, true, true}},      // element 3
            {{false, false, false}, {false, false, true}, {true, true, true}},      // element 4
            {{false, false, false}, {false, true, true}, {false, true, true}},      // element 5
            {{false, false, false}, {true, false, false}, {true, true, true}}       // element 6
    };

    protected int _blockType;
    protected int[] _positionOnGrid;
    protected boolean[][] _blockMatrix;

    public GenericBlock(final int blockType, final int[] blockInitPosition) {
        _blockMatrix = TYPE_BLOCK_MATRIX[blockType];
        _blockType = blockType;
        _positionOnGrid = blockInitPosition; // determine the position of the top left corner of the block on the grid
    }

    public void printShape() {
        for(boolean[] line :_blockMatrix) {
            for(boolean el:line) {
                System.out.print(el ? "X": " ");
            }
            System.out.println();
        }
    }

    public int getType() {
        return _blockType;
    }

    public boolean[][] getBlockMatrix() {
        return _blockMatrix;
    }

    public static void main(String[] args) {
        GenericBlock myBlock = new GenericBlock(6, new int[] {0,0});
        myBlock.printShape();
    }

    public void rotateShape() {
        /*
        rotating a square matrix is equivalent to transposing it then getting its image by
        symetry along the vertical axis
         */
        _transposeBlock();
        _symAxis1Block();

    }

    private void _transposeBlock() {
        /*
        this matrix yields the transposed of the _blockMatrix
         */
        boolean[][] temp = new boolean[_blockMatrix[0].length][_blockMatrix.length];
        for (int i = 0; i < _blockMatrix.length; i++)
            for (int j = 0; j < _blockMatrix[0].length; j++)
                temp[j][i] = _blockMatrix[i][j];

        _blockMatrix = temp;
    }

    public void _symAxis1Block() {
        /*
        this (not so elegant) methods get the symetric of the _blockMatrix along the vertical axis
        it is not generalized to n*m matrices --> will work only for 3x3
         */
        boolean[][] temp = new boolean[_blockMatrix[0].length][_blockMatrix.length];
        for (int i = 0; i < _blockMatrix.length; i++) {
            temp[i][0] = _blockMatrix[i][2];
            temp[i][1] = _blockMatrix[i][1];
            temp[i][2] = _blockMatrix[i][0];
        }
        _blockMatrix = temp;
        }

    }