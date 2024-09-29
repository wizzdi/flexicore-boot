package plugins;

import java.util.List;

public class TestEntityCreate {

	private String name;
	private String description;

	public static TestEntityCreate builder(String name,String description){
		return new TestEntityCreate()
				.setName(name)
				.setDescription(description);
	}
	public static TestEntityCreate children1(String name, String description, List<TestDependent>children1){
		return new TestEntityCreate()
				.setName(name)
				.setDescription(description);
	}
	public static TestEntityCreate children2(List<TestDependent> children2){
		return new TestEntityCreate();
	}
	public String getDescription() {
		return description;
	}

	public <T extends TestEntityCreate> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}

	public String getName() {
		return name;
	}

	public <T extends TestEntityCreate> T setName(String name) {
		this.name = name;
		return (T) this;
	}
}
