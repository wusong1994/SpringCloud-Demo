package com.goumang.sys.api.po;

import com.goumang.core.base.BasePo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table ( name ="sys_role_action" )
public class SysRoleActionPo extends BasePo {

	/* ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roacId;

	/* 角色ID */
	private Long roleId;

	/* 功能点ID */
	private Long funcId;

	/* 操作点ID */
	private Long actionId;

	@Override
	public Object getPk() { return this.roacId; }

	@Override
	public void setPk(Object pk) { this.roacId = pk == null || pk.toString().length() == 0 ? null : Long.parseLong(pk.toString()); }

	public Long getRoacId() { return this.roacId; }

	public void setRoacId(Long roacId) { this.roacId = roacId; }

	public Long getRoleId() { return this.roleId; }

	public void setRoleId(Long roleId) { this.roleId = roleId; }

	public Long getFuncId() { return this.funcId; }

	public void setFuncId(Long funcId) { this.funcId = funcId; }

	public Long getActionId() { return this.actionId; }

	public void setActionId(Long actionId) { this.actionId = actionId; }

}
