package com.goumang.sys.api.po;

import com.goumang.core.base.BasePo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table ( name ="sys_dic_type" )
public class SysDicTypePo extends BasePo {

	/* 字典分类ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dityId;

	/* 编码 */
	private String code;

	/* 名称 */
	private String name;

	@Override
	public Object getPk() { return this.dityId; }

	@Override
	public void setPk(Object pk) { this.dityId = pk == null || pk.toString().length() == 0 ? null : Long.parseLong(pk.toString()); }

	public Long getDityId() { return this.dityId; }

	public void setDityId(Long dityId) { this.dityId = dityId; }

	public String getCode() { return this.code; }

	public void setCode(String code) { this.code = code; }

	public String getName() { return this.name; }

	public void setName(String name) { this.name = name; }

}
