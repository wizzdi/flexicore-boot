package plugins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.VirtualField;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@VirtualField.List({
		@VirtualField(name = "children",type = TestDependent.class,mappedBy = "testEntity"),
		@VirtualField(name = "children2",type = TestDependent.class,mappedBy = "testEntity")
})
public class TestEntity {

	@Id
	private String id;
	private String name;
	private String description;


	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public <T extends TestEntity> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public <T extends TestEntity> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}

	public String getId() {
		return id;
	}

	public <T extends TestEntity> T setId(String id) {
		this.id = id;
		return (T) this;
	}

}
