package com.goumang.sys.api.po;


import com.goumang.core.base.BasePo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table ( name ="sys_dic" )
public class SysDicPo extends BasePo {

	/* ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dicId;

	/* 类型编码 */
	private String type;

	/* 编码 */
	private String code;

	/* 名称 */
	private String name;

	/* 序号 */
	private Long sort;

	@Override
	public Object getPk() { return this.dicId; }

	@Override
	public void setPk(Object pk) { this.dicId = pk==null?null:Long.parseLong(pk.toString()); }

	public Long getDicId() { return this.dicId; }

	public void setDicId(Long dicId) { this.dicId = dicId; }

	public String getType() { return this.type; }

	public void setType(String type) { this.type = type; }

	public String getCode() { return this.code; }

	public void setCode(String code) { this.code = code; }

	public String getName() { return this.name; }

	public void setName(String name) { this.name = name; }

	public Long getSort() { return this.sort; }

	public void setSort(Long sort) { this.sort = sort; }

}
