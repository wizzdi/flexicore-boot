package plugins;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TestDependent {
    @Id
    private String id;
    @ManyToOne(targetEntity = TestEntity.class)
    private TestEntity testEntity;

    public String getId() {
        return id;
    }

    public <T extends TestDependent> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public TestEntity getTestEntity() {
        return testEntity;
    }

    public <T extends TestDependent> T setTestEntity(TestEntity testEntity) {
        this.testEntity = testEntity;
        return (T) this;
    }
}
