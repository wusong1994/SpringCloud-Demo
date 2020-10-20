package com.goumang.sys.api.po;

import com.goumang.core.base.BasePo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table ( name ="sys_func" )
public class SysFuncPo extends BasePo {

	/* 功能点ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long funcId;

	/* 代码，唯一 */
	private String funcCode;

	/* 名称 */
	private String funcName;

	/* 值 */
	private String funcValue;

	@Override
	public Object getPk() { return this.funcId; }

	@Override
	public void setPk(Object pk) { this.funcId = pk == null || pk.toString().length() == 0 ? null : Long.parseLong(pk.toString()); }

	public Long getFuncId() { return this.funcId; }

	public void setFuncId(Long funcId) { this.funcId = funcId; }

	public String getFuncCode() { return this.funcCode; }

	public void setFuncCode(String funcCode) { this.funcCode = funcCode; }

	public String getFuncName() { return this.funcName; }

	public void setFuncName(String funcName) { this.funcName = funcName; }

	public String getFuncValue() { return this.funcValue; }

	public void setFuncValue(String funcValue) { this.funcValue = funcValue; }

}
