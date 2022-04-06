package us.conian;

import java.util.*;
import java.util.regex.*;

public class ProgramState {
	
	private static final Set<ProgramState> ALL_STATES = new HashSet<>();
	
	private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_!]+$");
	
	public static final ProgramState UNCATEGORIZED = of("uncategorized");
	
	private final String name;
	
	private ProgramState(String name) throws IllegalArgumentException {
		if (name == null)
			throw new NullPointerException();
		if (name.isBlank())
			throw new IllegalArgumentException("The name cannot be blank");
		if (!NAME_PATTERN.matcher(name).matches())
			throw new IllegalArgumentException("The given name is invalid "
					+ "(must only contain letters, numbers, underscores, and exclamation points)");
		this.name = name.strip().toLowerCase();
	}
	
	public String name() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof ProgramState p && p.name.equals(name);
	}
	
	public static ProgramState of(String name) throws IllegalArgumentException {
		ProgramState p = new ProgramState(name);
		if (!ALL_STATES.contains(p))
			synchronized(ALL_STATES) {
				ALL_STATES.add(p);
			}
		return p;
	}
	
	public static Set<ProgramState> all() {
		return Set.copyOf(ALL_STATES);
	}

}
