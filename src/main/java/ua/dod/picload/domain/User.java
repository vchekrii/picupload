package ua.dod.picload.domain;

import static javax.persistence.CascadeType.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
	
	public User(){
		super();
	}
	
	public User(String id, String ip, Boolean banned) {
		setId(id);
		setIp(ip);
		setBanned(banned);
	}
	
	@Id
	@OneToMany(targetEntity=Picture.class, cascade=ALL, mappedBy="user")
	@NotNull
	@Column(name = "id")
	private String id;
	
	@Column(name="ip")
	private String ip;
	
	@Column(name="banned")
	private Boolean banned;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Boolean getBanned() {
		return banned;
	}

	public void setBanned(Boolean banned) {
		this.banned = banned;
	}

	
}