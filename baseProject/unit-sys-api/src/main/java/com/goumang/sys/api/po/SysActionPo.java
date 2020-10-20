package com.goumang.sys.api.po;

import com.goumang.core.base.BasePo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table ( name ="sys_action" )
public class SysActionPo extends BasePo {

	/* 操作点ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long actionId;

	/* 功能点ID */
	private Long funcId;

	/* 访问方式 */
	private String method;

	/* 代码，唯一 */
	private String actionCode;

	/* 名称 */
	private String actionName;

	@Override
	public Object getPk() { return this.actionId; }

	@Override
	public void setPk(Object pk) { this.actionId = pk == null || pk.toString().length() == 0 ? null : Long.parseLong(pk.toString()); }

	public Long getActionId() { return this.actionId; }

	public void setActionId(Long actionId) { this.actionId = actionId; }

	public Long getFuncId() { return this.funcId; }

	public void setFuncId(Long funcId) { this.funcId = funcId; }

	public String getMethod() { return this.method; }

	public void setMethod(String method) { this.method = method; }

	public String getActionCode() { return this.actionCode; }

	public void setActionCode(String actionCode) { this.actionCode = actionCode; }

	public String getActionName() { return this.actionName; }

	public void setActionName(String actionName) { this.actionName = actionName; }

}
