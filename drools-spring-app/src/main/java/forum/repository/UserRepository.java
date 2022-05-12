package forum.repository;

import java.util.ArrayList;
import java.util.List;

import forum.model.User;

public class UserRepository {
	
	private List<User> users = new ArrayList<>();	

	private UserRepository() {
		User user1 = new User("Genesis", "Genesis Rhapsodos");
		User user2 = new User("Angeal", "Angeal Hewley");
		User user3 = new User("Sephiroth", "Sephiroth");
		User user4 = new User("Zack", "Zack Fair");
		User user5 = new User("Cloud", "Cloud Strife");
		
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		users.add(user5);
	}	
	
	private static UserRepository instance = new UserRepository();	
	
	public static UserRepository getInstance() {
		return instance;
	}
	
	public List<User> getAllUsers() {
		return users;
	}
	
	public User getUserById(String id) {
		for (User user: users) {
			if (user.username.toLowerCase().equals(id.toLowerCase())) {
				return user;
			}
		}
		
		return null;
	}
	
	public User updateUser(User user) {
		for (User userFromList: users) {
			if (userFromList.username.toLowerCase().equals(user.username.toLowerCase())) {
				int index = users.indexOf(userFromList);
				users.set(index, user);
				return user;
			}
		}
		
		return null;
	}
		
}
