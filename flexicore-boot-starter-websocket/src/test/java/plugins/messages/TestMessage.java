package plugins.messages;

public class TestMessage implements WSEvent{

	private String test;

	public String getTest() {
		return test;
	}

	public <T extends TestMessage> T setTest(String test) {
		this.test = test;
		return (T) this;
	}
}
