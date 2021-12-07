package catering.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Collection;


public interface UserRepository extends CrudRepository<User, Long> {
	@Override
	Streamable<User> findAll();

	Streamable<User> getUserByPositionIn(Collection<Position> positions);
}
