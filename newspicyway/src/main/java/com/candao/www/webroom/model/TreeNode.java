package com.candao.www.webroom.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;





/**
 * @author Administrator
 *
 */
public class TreeNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -632569902016839296L;
	/**
	 * 
	 */
	private String id;
	private String text;
	private String sn;
	private String description;
	private List<TreeNode> children; 
	private TreeNode parent; 
	private int level; 
	private boolean leaf; 
	
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonManagedReference
	public List<TreeNode> getChildren() {
		return children;
	}
	@JsonManagedReference
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	@JsonBackReference
	public TreeNode getParent() {
		return parent;
	}
	@JsonBackReference
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	
	
}
