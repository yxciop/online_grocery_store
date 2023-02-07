package cn.letao.online_grocery_store.pojo;

public class Product_Type {
	private Integer id;
	private String  stype;
	private Integer parentId;
	private Integer soncode;
	private String  parentType;
	private Integer idety;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getSoncode() {
		return soncode;
	}

	public void setSoncode(Integer soncode) {
		this.soncode = soncode;
	}

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	public Integer getIdety() {
		return idety;
	}

	public void setIdety(Integer idety) {
		this.idety = idety;
	}
}
