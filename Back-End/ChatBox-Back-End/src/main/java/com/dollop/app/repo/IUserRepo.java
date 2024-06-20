package com.dollop.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dollop.app.data.User;

public interface IUserRepo extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.userName=:userName AND u.password=:password")
	User finduser(String userName, String password);

	@Query("SELECT u FROM User u WHERE u.userName=:userName")
	User findUserByUsername(String userName);

	@Query("SELECT u FROM User u WHERE u.userName !=:userName")
	List<User> findUserByUsernameExcept(String userName);

	@Query("SELECT u FROM User u WHERE u.userName !=:userName AND  u.userName LIKE %:key% ORDER BY u.userName ASC")
	List<User> searchReceiver(@Param("key") String key , @Param("userName") String userName);

	User findUserByEmail(String email);
}
