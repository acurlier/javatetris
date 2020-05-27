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

    private int _blockType;
    private int[] _blockPosition;
    private boolean[][] _blockMatrix;

    public GenericBlock(final int blockType, final int[] blockInitPosition) {
        _blockMatrix = TYPE_BLOCK_MATRIX[blockType];
        _blockPosition = blockInitPosition;
        _blockType = blockType;
    }

    public void printShape() {
        for(boolean[] line :_blockMatrix) {
            for(boolean el:line) {
                System.out.print(el ? "X": "O");
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

    public void rotateBlock() {
        _transposeBlock();
        _symAxis1Block();

    }

    private void _transposeBlock() {
        boolean[][] temp = new boolean[_blockMatrix[0].length][_blockMatrix.length];
        for (int i = 0; i < _blockMatrix.length; i++)
            for (int j = 0; j < _blockMatrix[0].length; j++)
                temp[j][i] = _blockMatrix[i][j];

        _blockMatrix = temp;
    }

    public void _symAxis1Block() {


    }

}
