package my.project;

public class ConstructionComponent {
	
	public String value;
	public String fieldType;//?
	public boolean isField;
	String parentClassName;

	public String getValue() {
		return value;
	}
		
	public boolean getIsField() {
		return isField;
	}
	
	public String getFieldType() {
		return fieldType;
	}
	
	public String getParentClassName() {
		return parentClassName;
	}
	
	/**@value - if it's a field, the value is fieldName, otherwise it's just some text*/
	public ConstructionComponent(String value, String fieldType, boolean isField, String parentClassName) {
		this.value = value;
		this.fieldType = fieldType;
		this.isField = isField;
		this.parentClassName = parentClassName;
	}
}
