package forum.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class User {	
	
	public String username;
	public String name;
	public List<UserLabelEnum> userLabels = new ArrayList<>();
	
	public User(String username, String name) {
		super();
		this.username = username;
		this.name = name;
	}	
	
}

