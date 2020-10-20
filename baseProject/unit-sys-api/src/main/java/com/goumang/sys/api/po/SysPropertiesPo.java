package com.goumang.sys.api.po;

import com.goumang.core.base.BasePo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table ( name ="sys_properties" )
public class SysPropertiesPo extends BasePo {

	/* 属性ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long propId;

	/* 键 */
	private String propKey;

	/* 名称 */
	private String propName;

	/* 值 */
	private String propValue;

	@Override
	public Object getPk() { return this.propId; }

	@Override
	public void setPk(Object pk) { this.propId = pk == null || pk.toString().length() == 0 ? null : Long.parseLong(pk.toString()); }

	public Long getPropId() { return this.propId; }

	public void setPropId(Long propId) { this.propId = propId; }

	public String getPropKey() { return this.propKey; }

	public void setPropKey(String propKey) { this.propKey = propKey; }

	public String getPropName() { return this.propName; }

	public void setPropName(String propName) { this.propName = propName; }

	public String getPropValue() { return this.propValue; }

	public void setPropValue(String propValue) { this.propValue = propValue; }

}
