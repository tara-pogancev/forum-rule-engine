package forum.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class User {	
	
	private String username;
	private String name;
	private List<UserLabelEnum> userLabels = new ArrayList<>();
	
	public User(String username, String name) {
		super();
		this.username = username;
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public List<UserLabelEnum> getUserLabels() {
		return userLabels;
	}

	public void setUserLabels(List<UserLabelEnum> userLabels) {
		this.userLabels = userLabels;
	}	
	
	
	public void removeLabel(UserLabelEnum label) {
		this.userLabels.remove(label);
	}
	
	public void addLabel(UserLabelEnum label) {
		if (!this.userLabels.contains(label)) {
			this.userLabels.add(label);
		}
	}
	
	
}

