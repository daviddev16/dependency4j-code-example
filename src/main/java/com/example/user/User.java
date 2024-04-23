package com.example.user;

import javax.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(
	name = "users",
	uniqueConstraints = 
		@UniqueConstraint(columnNames = "login", name = "uq_users_login")
)
@NamedQueries({
	@NamedQuery(
			name = "User_FindByLogin", 
			query = "FROM User u WHERE u.login = :paramLogin")
})
public class User {

	@Id
	@Column(
		name = "user_id",
		nullable = false
	)
	@GeneratedValue(
		strategy = GenerationType.IDENTITY
	)
	private Long id;
	
	@Column(
		name = "login",
		nullable = false
	)
	private String login;
	
	@Column(
		name = "password",
		nullable = false
	)
	private String password;
	

}
