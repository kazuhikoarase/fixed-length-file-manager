package flfm.model;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class SimpleTreeNode implements TreeNode {

	private static final Enumeration<?> EMPTY = new Vector<Object>().elements();

	private Object userObject = null;
	private SimpleTreeNode parent = null;
	private boolean leaf = true;
	private Vector<SimpleTreeNode> children = null;
	
	public SimpleTreeNode() {
		this(null);
	}
	public SimpleTreeNode(Object userObject) {
		this.userObject = userObject;
	}
	public Object getUserObject() {
		return userObject;
	}
	@Override
	public String toString() {
		return (userObject != null)? userObject.toString() : super.toString();
	}
	public void addChild(SimpleTreeNode treeNode) {
		treeNode.parent = this;
		if (children == null) {
			children = new Vector<SimpleTreeNode>();
		}
		children.add(treeNode);
	}
	public Enumeration<?> children() {
		if (children == null) {
			return EMPTY;
		}
		return children.elements();
	}
	public boolean getAllowsChildren() {
		return true;
	}
	public TreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}
	public int getChildCount() {
		if (children == null) {
			return 0;
		}
		return children.size();
	}
	public int getIndex(TreeNode node) {
		if (children == null) {
			return -1;
		}
		return children.indexOf(node);
	}
	public TreeNode getParent() {
		return parent;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
}
