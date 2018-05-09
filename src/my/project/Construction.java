package my.project;

import java.util.List;

public class Construction {

	public String name;
	public List<ConstructionComponent> constructionComponents;
	
	public List<ConstructionComponent> getConstructionComponents(){
		return constructionComponents;
	}
	
	public void setConstructionComponents(List<ConstructionComponent> constructionComponents){
		this.constructionComponents = constructionComponents;
	}
	
	public String getName() {
		return name;
	}
	
	public Construction(String constructionName) {
		this.name = constructionName;
	}
}
