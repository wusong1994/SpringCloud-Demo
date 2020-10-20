package com.goumang.sys.api.po;

import com.goumang.core.base.BasePo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table ( name ="sys_role_func" )
public class SysRoleFuncPo extends BasePo {

	/* ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rofuId;

	/* 角色ID */
	private Long roleId;

	/* 功能点ID */
	private Long funcId;

	@Override
	public Object getPk() { return this.rofuId; }

	@Override
	public void setPk(Object pk) { this.rofuId = pk == null || pk.toString().length() == 0 ? null : Long.parseLong(pk.toString()); }

	public Long getRofuId() { return this.rofuId; }

	public void setRofuId(Long rofuId) { this.rofuId = rofuId; }

	public Long getRoleId() { return this.roleId; }

	public void setRoleId(Long roleId) { this.roleId = roleId; }

	public Long getFuncId() { return this.funcId; }

	public void setFuncId(Long funcId) { this.funcId = funcId; }

}
