package com.rakesh.sms.beans;

class TrieNode {

	/** Only for Numbers [0-9] **/
	private TrieNode[] childs;
	private boolean end;

	TrieNode() {
		this.childs = new TrieNode[10];
		this.end = false;
	}// End Of Constructor

	TrieNode[] getChilds() {
		return this.childs;
	}// End Of Method

	TrieNode getChild(int index) {
		return this.childs[index];
	}// End Of Method

	void setChild(int index, TrieNode child) {
		this.childs[index] = child;
	}// End Of Method

	boolean isEnd() {
		return this.end;
	}// End Of Method

	void setEnd(boolean end) {
		this.end = end;
	}// End Of Method

}// End Of Class

public class Trie {

	private static final int Zero = (int) '0';
	private TrieNode root;

	public Trie() {
		this.root = new TrieNode();
	}// End Of Constructor

	public boolean isEmpty() {
		TrieNode[] childs = this.root.getChilds();
		for (int i = 0; i < childs.length; i++) {
			if (childs[i] != null) {
				return false;
			}
		} // End Of Loop
		return true;
	}// End Of Method

	public void insert(String word) {

		if (word == null || word.trim().length() == 0)
			return;
		else {
			word = word.trim();
		}

		TrieNode current = this.root;

		for (int i = 0; i < word.length(); i++) {

			int c = (int) word.charAt(i);
			int index = c - Trie.Zero;

			if (current.getChild(index) == null) {
				TrieNode newNode = new TrieNode();
				current.setChild(index, newNode);
				current = newNode;
			} else {
				current = current.getChild(index);
			}

		} // End Of Loop

		current.setEnd(true);

	}// End Of Method

	public boolean search(String word) {

		if (word == null || word.trim().length() == 0)
			return false;
		else {
			TrieNode node = searchNode(word.trim());
			return node == null ? false : node.isEnd();
		}

	}// End Of Method

	private TrieNode searchNode(String word) {

		TrieNode node = this.root;

		for (int i = 0; i < word.length(); i++) {

			int c = (int) word.charAt(i);
			int index = c - Trie.Zero;

			if (node.getChild(index) != null) {
				node = node.getChild(index);
			} else {
				/**
				 * Legally it should return NULL, But since we have series Check here, not exact
				 * match, will return the last Node
				 **/
				return node;
			}

		} // End Of Loop

		return node == this.root ? null : node;

	}// End Of Method

}// End Of Main Class
