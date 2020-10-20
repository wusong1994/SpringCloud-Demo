package com.goumang.sys.api.po;

import com.goumang.core.base.BasePo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table ( name ="sys_menu" )
public class SysMenuPo extends BasePo {

	/* 菜单ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long menuId;

	/* 父Id */
	private Long parentId;

	/* 功能点ID，多个逗号隔开 */
	private String funcIds;

	/* 标题 */
	private String menuTitle;

	/* 路径 */
	private String menuPath;

	/* 图标 */
	private String icon;

	/* 序号 */
	private Long sort;

	/* 是否主页 */
	private Boolean home;

	@Override
	public Object getPk() { return this.menuId; }

	@Override
	public void setPk(Object pk) { this.menuId = pk == null || pk.toString().length() == 0 ? null : Long.parseLong(pk.toString()); }

	public Long getMenuId() { return this.menuId; }

	public void setMenuId(Long menuId) { this.menuId = menuId; }

	public Long getParentId() { return this.parentId; }

	public void setParentId(Long parentId) { this.parentId = parentId; }

	public String getFuncIds() { return this.funcIds; }

	public void setFuncIds(String funcIds) { this.funcIds = funcIds; }

	public String getMenuTitle() { return this.menuTitle; }

	public void setMenuTitle(String menuTitle) { this.menuTitle = menuTitle; }

	public String getMenuPath() { return this.menuPath; }

	public void setMenuPath(String menuPath) { this.menuPath = menuPath; }

	public String getIcon() { return this.icon; }

	public void setIcon(String icon) { this.icon = icon; }

	public Long getSort() { return this.sort; }

	public void setSort(Long sort) { this.sort = sort; }

	public Boolean getHome() { return this.home; }

	public void setHome(Boolean home) { this.home = home; }

}
