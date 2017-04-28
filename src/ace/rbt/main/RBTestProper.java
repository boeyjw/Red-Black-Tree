package ace.rbt.main;

import static org.junit.Assert.*;

import org.junit.Test;

public class RBTestProper {
	RBTree rbt = new RBTree(Integer.MIN_VALUE);
	String regex = "\\s+|,\\s+|,";
	
	@Test
	public void testPositionSortedString() {
		String input = "47B1 14R2 67R3 6B4, 43B5 51B6    91B7 21R10 84R14";
		assertTrue(rbt.validateUserTree(input.trim().toUpperCase().split(regex)));
	}
	
	@Test
	public void testElementSortedString() {
		String input = "6B4 14R2    21R10 43B5 47B1 51B6 67R3 84R14 91B7";
		assertTrue(rbt.validateUserTree(input.trim().toUpperCase().split(regex)));
	}
	
	@Test
	public void testEmptyTree() {
		assertTrue(rbt.validateUserTree("".trim().toUpperCase().split(regex)));
		assertTrue(rbt.validateUserTree("\n".trim().toUpperCase().split(regex)));
	}
	
	@Test
	public void testSingleNodeTree() {
		assertTrue(rbt.validateUserTree("1B1".trim().toUpperCase().split(regex)));
		assertFalse(rbt.validateUserTree("1R1".trim().toUpperCase().split(regex)));
		assertFalse(rbt.validateUserTree("1B2".trim().toUpperCase().split(regex)));
		assertFalse(rbt.validateUserTree("1R2".trim().toUpperCase().split(regex)));
	}
	
	@Test
	public void testInvalidColour() {
		//Left node red
		assertFalse(rbt.validateUserTree("2B1 1R2 3B3".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
		//Right node red
		assertFalse(rbt.validateUserTree("2B1 1B2 3R3".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
		//Root red
		assertFalse(rbt.validateUserTree("2R1 1B2 3B3".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
		//Tree red
		assertFalse(rbt.validateUserTree("2R1 1R2 3R3".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
	}
	
	@Test
	public void testInvalidPosition() {
		//No root
		assertFalse(rbt.validateUserTree("2B2 1B3 3B4".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
		//Isolated child
		assertFalse(rbt.validateUserTree("2B1 1R4 3R3".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
	}
	
	@Test
	public void testLeafNodeRedValidity() {
		//All black
		assertTrue(rbt.validateUserTree("2B1 1B2 3B3".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
		//Only non-null leaf node red rightmost
		assertTrue(rbt.validateUserTree("2B1 1B2 3B3 4R7".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
		//Only non-null leaf node red leftmost
		assertTrue(rbt.validateUserTree("2B1 1B2 3B3 0R4".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
		//Only non-null leaf node red left subtree right
		assertTrue(rbt.validateUserTree("10B1 5B2 15B3 6R5".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
		//Only non-null leaf node red right subtree left
		assertTrue(rbt.validateUserTree("10B1 5B2 15B3 14R6".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
	}
	
	@Test
	public void testBlackHeight() {
		assertTrue(rbt.validateUserTree("47B1 14R2 67R3 6B4 43B5 51B6 91B7".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
		assertTrue(rbt.validateUserTree("47B1 14B2 67B3 6R4 43R5 51R6 91R7".trim().toUpperCase().split(regex)));
		rbt.makeEmpty();
	}

}
