package com.game.compute;

public class GenericBlock {
    private static final boolean[][][] TYPE_BLOCK_MATRIX = {
            {{true, true, true, true}, {false, false, false, false}, {false, false, false, false},
                    {false, false, false, false}},     // element 0 : Tetrimino I

            {{false, true, true, false}, {true, true, false, false}, {false, false, false, false},
                    {false, false, false, false}},      // element 1: Tetrimino S
            {{true, true, false, false}, {false, true, true, false}, {false, false, false, false},
                    {false, false, false, false}},      // element 2 : Tetrimino Z

            {{true, true, true, false}, {false, true, false, false}, {false, false, false, false},
                    {false, false, false, false}},      // element 3 : Tetrimino T

            {{true, true, false, false}, {true, true, false, false}, {false, false, false, false},
                    {false, false, false, false}},      // element 4 : Tetrimino O

            {{true, true, true, false}, {false, false, true, false}, {false, false, false, false},
                    {false, false, false, false}},      // element 5 : Tetrimino J
            {{true, true, true, false}, {true, false, false, false}, {false, false, false, false},
                    {false, false, false, false}}       // element 6 : Tetrimino L
    };

    private static final int[] CENTER_GRAVITY = {0,1}; // block center of gravity for rotations
    private static final boolean[][] CENTER_GRAVITY_MATRIX = {{false, true, false, false},
            {false, false, false, false}, {false, false, false, false},
            {false, false, false, false}};

    protected int _blockType;
    protected int[] _positionOnGrid;
    protected boolean[][] _blockMatrix;
    protected boolean[][] _gravityCenterMatrix;

    public GenericBlock(final int blockType, final int[] blockInitPosition) {
        _blockMatrix = TYPE_BLOCK_MATRIX[blockType];
        _blockType = blockType;
        _positionOnGrid = blockInitPosition; // determine the position of the top left corner of the block on the grid
        _gravityCenterMatrix = CENTER_GRAVITY_MATRIX;
    }

    public void printShape() {
        for (boolean[] line : _blockMatrix) {
            for (boolean el : line) {
                System.out.print(el ? "X" : " ");
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
        GenericBlock myBlock = new GenericBlock(6, new int[]{0, 0});
        myBlock.printShape();
    }

    public void rotateShape() {
        /*
        rotating a square matrix is equivalent to transposing it then getting its image by
        symetry along the vertical axis
         */
        _blockMatrix = _transposeBlock(_symAxis1Block(_blockMatrix));

    }

    public boolean[][] rotateShape(boolean[][] matrix) {
        /*
        rotating a square matrix is equivalent to transposing it then getting its image by
        symetry along the vertical axis
         */
        boolean[][] temp = _transposeBlock(_symAxis1Block(matrix));
        return temp;

    }

    protected boolean[][] _transposeBlock(boolean[][] matrix) {
        /*
        this matrix yields the transposed of the _blockMatrix
         */
        boolean[][] temp = new boolean[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                temp[j][i] = matrix[i][j];

        matrix = temp;
        return temp;
    }

    protected boolean[][] _symAxis1Block(boolean[][] matrix) {
        /*
        this (not so elegant) methods get the symetric of the _blockMatrix along the vertical axis
        it is not generalized to n*m matrices --> will work only for 3x3
         */
        boolean[][] temp = new boolean[matrix[0].length][matrix.length];
        for (int i = 0; i < _blockMatrix.length; i++) {
            temp[i][0] = matrix[i][3];
            temp[i][1] = matrix[i][2];
            temp[i][2] = matrix[i][1];
            temp[i][3] = matrix[i][0];
        }
        matrix = temp;
        return temp;
    }

}