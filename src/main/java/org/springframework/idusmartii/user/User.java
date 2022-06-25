package org.springframework.idusmartii.user;

import java.util.Set;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Getter
@Setter
@Entity
@Audited
@Table(name = "users")
public class User{

	@Id
    @Column(unique = true)
	String username;

	String password;

	boolean enabled;

    @NotAudited
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Authorities> authorities;
}
