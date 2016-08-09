package com.candao.www.webroom.model;

import java.util.List;

import com.candao.www.data.model.Tmenu;
import com.candao.www.data.model.TmenuBranch;
import com.candao.www.data.model.Ttemplate;

public class MenuGroup {
	private Tmenu menu;
	private List<TmenuBranch> menuBranchlist;
	private List<Ttemplate> templatelist;
	public Tmenu getMenu() {
		return menu;
	}
	public void setMenu(Tmenu menu) {
		this.menu = menu;
	}
	public List<TmenuBranch> getMenuBranchlist() {
		return menuBranchlist;
	}
	public void setMenuBranchlist(List<TmenuBranch> menuBranchlist) {
		this.menuBranchlist = menuBranchlist;
	}
	public List<Ttemplate> getTemplatelist() {
		return templatelist;
	}
	public void setTemplatelist(List<Ttemplate> templatelist) {
		this.templatelist = templatelist;
	}

}
