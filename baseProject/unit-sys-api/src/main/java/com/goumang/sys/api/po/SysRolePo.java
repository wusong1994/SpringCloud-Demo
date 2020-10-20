package com.goumang.sys.api.po;

import com.goumang.core.base.BasePo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table ( name ="sys_role" )
public class SysRolePo extends BasePo {

	/* 角色ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleId;

	/* 代码，唯一 */
	private String roleCode;

	/* 名称 */
	private String roleName;

	/* 备注 */
	private String roleRemark;

	@Override
	public Object getPk() { return this.roleId; }

	@Override
	public void setPk(Object pk) { this.roleId = pk == null || pk.toString().length() == 0 ? null : Long.parseLong(pk.toString()); }

	public Long getRoleId() { return this.roleId; }

	public void setRoleId(Long roleId) { this.roleId = roleId; }

	public String getRoleCode() { return this.roleCode; }

	public void setRoleCode(String roleCode) { this.roleCode = roleCode; }

	public String getRoleName() { return this.roleName; }

	public void setRoleName(String roleName) { this.roleName = roleName; }

	public String getRoleRemark() { return this.roleRemark; }

	public void setRoleRemark(String roleRemark) { this.roleRemark = roleRemark; }

}
