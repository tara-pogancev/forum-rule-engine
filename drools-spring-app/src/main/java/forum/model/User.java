package forum.model;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.definition.type.Modifies;

import forum.repository.UserRepository;
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
	

	@Modifies({ "userLabels" })
	public void removeLabel(UserLabelEnum label) {
		this.userLabels.remove(label);
	}
	

	@Modifies({ "userLabels" })
	public void addLabel(UserLabelEnum label) {
		if (!this.userLabels.contains(label)) {
			this.userLabels.add(label);
		}
	}
	
	public void update() {
		UserRepository.getInstance().updateUser(this);
	}
	
}

