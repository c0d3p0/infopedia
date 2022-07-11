package com.iorix2k2.infopedia.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iorix2k2.infopedia.user.model.User;


public interface UserRepository extends JpaRepository<User, Long>
{
	@Query("SELECT new User(u.id, u.username, age, gender, country) " +
				"FROM User u WHERE u.username LIKE LOWER(CONCAT('%', :username,'%'))")
	List<User> findPartialByUsernameContainingIgnoreCase(String username);
	
	@Query(value="SELECT id, username, age, gender, country FROM infopedia_user " +
			"ORDER BY RANDOM() FETCH FIRST :amount ROWS ONLY", nativeQuery=true)
	List<Object[]> findPartialTopAmountOrderByRandom(Long amount);
	
	@Query("SELECT new User(u.id, u.username, age, gender, country) " +
			"FROM User u WHERE u.id=:id")
	Optional<User> findPartialById(Long id);
	
	@Query(value = "SELECT new User(u.id, u.fullName, u.age, u.gender, " +
			"u.country, u.email, u.username) FROM User u WHERE u.id=:id")
	Optional<User> findDataById(Long id);
	
	@Query(value="SELECT new User(u.id, u.username, u.email) FROM User u " +
			"WHERE u.id<>:id AND (u.username=:username OR u.email=:email)")
	List<User> findByIdNotAndUsernameOrEmail(Long id, String username, String email);

	Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

	List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);
}
