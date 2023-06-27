package app.jbomberman.view;

import app.jbomberman.util.Maybe;

/**
 * KeyBoardControllerKeyCode is an enumeration of the currently available
 * keys that this class can handle. Since this class is a sort of abstraction
 * layer between the specific (now it's javafx) implementation and another
 * representation, these codes are the new representation.
 *
 * @author gp
 */
public enum KeyBoardControllerKeyCode
{
	/**
	 * the enum for LEFT
	 */
	LEFT(),
	/**
	 * the enum for RIGHT
	 */
	RIGHT(),
	/**
	 * the enum for UP
	 */
	UP(),
	/**
	 * the enum for DOWN
	 */
	DOWN(),
	/**
	 * the enum for ESC
	 */
	ESC(),
	/**
	 * the enum for ENTER
	 */
	ENTER(),
	/**
	 * the enum for DEL
	 */
	DEL(),
	/**
	 * the enum for BACK_SPACE
	 */
	BACK_SPACE(),
	/**
	 * the enum for SPACE
	 */
	SPACE(' '),
	/**
	 * the enum for A letter
	 */
	A('a'),
	/**
	 * the enum for B letter
	 */
	B('b'),
	/**
	 * the enum for C letter
	 */
	C('c'),
	/**
	 * the enum for D letter
	 */
	D('d'),
	/**
	 * the enum for E letter
	 */
	E('e'),
	/**
	 * the enum for F letter
	 */
	F('f'),
	/**
	 * the enum for G letter
	 */
	G('g'),
	/**
	 * the enum for H letter
	 */
	H('h'),
	/**
	 * the enum for I letter
	 */
	I('i'),
	/**
	 * the enum for J letter
	 */
	J('j'),
	/**
	 * the enum for K letter
	 */
	K('k'),
	/**
	 * the enum for L letter
	 */
	L('l'),
	/**
	 * the enum for M letter
	 */
	M('m'),
	/**
	 * the enum for N letter
	 */
	N('n'),
	/**
	 * the enum for O letter
	 */
	O('o'),
	/**
	 * the enum for P letter
	 */
	P('p'),
	/**
	 * the enum for Q letter
	 */
	Q('q'),
	/**
	 * the enum for R letter
	 */
	R('r'),
	/**
	 * the enum for S letter
	 */
	S('s'),
	/**
	 * the enum for T letter
	 */
	T('t'),
	/**
	 * the enum for U letter
	 */
	U('u'),
	/**
	 * the enum for V letter
	 */
	V('v'),
	/**
	 * the enum for W letter
	 */
	W('w'),
	/**
	 * the enum for X letter
	 */
	X('x'),
	/**
	 * the enum for Y letter
	 */
	Y('y'),
	/**
	 * the enum for Z letter
	 */
	Z('z'),

	/**
	 * OTHER represents an unknown character (thus it's not one of the ones
	 * listed above)
	 */
	OTHER();

	/**
	 * repr is nothing if the character is not printable
	 * repr is just if it's printable, and the content of the just is the
	 * character representation of the code
	 */
	private final Maybe<Character> repr;

	/**
	 * KeyBoardControllerKeyCode() initializes the enum given its character
	 * representation
	 *
	 * @param repr the character representation of the enum
	 */
	KeyBoardControllerKeyCode(char repr)
	{
		this.repr = Maybe.just(repr);
	}

	/**
	 * KeyBoardControllerKeyCode() must be used to initialize the enum
	 * constants that have no associated symbol
	 */
	KeyBoardControllerKeyCode()
	{
		this.repr = Maybe.nothing();
	}

	/**
	 * isPrintable() checks whether a character is printable or not
	 *
	 * @return true if the character is printable otherwise false
	 */
	public boolean isPrintable()
	{
		return repr.isJust();
	}

	/**
	 * getRepr() returns the Maybe that might contain the representation
	 * of the KeyBoardControllerKeyCode
	 *
	 * @return the Maybe that might contain the representation
	 * of the KeyBoardControllerKeyCode
	 */
	public Maybe<Character> getRepr()
	{
		return repr;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
