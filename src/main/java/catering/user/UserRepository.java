package catering.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface UserRepository extends CrudRepository<User, Long> {
	//	User findByUsername(String username);
//	@Override
//	Streamable<User> findAll();
}
