package com.goumang.sys.api.po;

import com.goumang.core.base.BasePo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table ( name ="sys_user_role" )
public class SysUserRolePo extends BasePo {

	/* 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long usroId;

	/* 用户ID */
	private Long userId;

	/* 角色ID */
	private Long roleId;

	@Override
	public Object getPk() { return this.usroId; }

	@Override
	public void setPk(Object pk) { this.usroId = pk == null || pk.toString().length() == 0 ? null : Long.parseLong(pk.toString()); }

	public Long getUsroId() { return this.usroId; }

	public void setUsroId(Long usroId) { this.usroId = usroId; }

	public Long getUserId() { return this.userId; }

	public void setUserId(Long userId) { this.userId = userId; }

	public Long getRoleId() { return this.roleId; }

	public void setRoleId(Long roleId) { this.roleId = roleId; }

}
