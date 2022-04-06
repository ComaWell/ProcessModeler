package us.conian;

public enum AttachMode {

	EXISTING("attach to an already-running JVM"),
	NEXT_PROCESS("attach to the next JVM that executes on this machine");
	
	private final String description;
	
	private AttachMode(String description) {
		this.description = description;
	}
	
	public String description() {
		return description;
	}
	
}
