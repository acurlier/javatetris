package com.game.compute;

import org.junit.Test;

import static org.junit.Assert.*;

public class GenericBlockTest {

    @Test
    public void printShape() {
    }

    @Test
    public void getType() {
        GenericBlock myBlock = new GenericBlock(6, new int[] {0,0});
        assertEquals(6,myBlock.getType());
    }

    @Test
    public void rotateBlock() {
        GenericBlock myBlock = new GenericBlock(6, new int[] {0,0});
        GenericBlock compareBlock = new GenericBlock(6, new int[] {0,0});
        for(int i = 0; i<4;i++) {
            myBlock.rotateShape();
            myBlock.printShape();
            System.out.println();
        }
        assertEquals(compareBlock.getBlockMatrix(),myBlock.getBlockMatrix());
    }
}