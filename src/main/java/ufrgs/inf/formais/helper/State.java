package ufrgs.inf.formais.helper;

public class State {
	
	private String name;
	
	public State(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof State) ? this.name.equals(((State)o).getName()) : false ;
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	public String toString() {
		return this.name;
	}

}
