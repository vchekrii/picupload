package ua.dod.picload.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pictures")
public class Picture {
	
	public Picture(){
		super();
	}
	
	public Picture(User user, String pic, String picHash, String pic106, String pic640, String pic800, Boolean banned) {
		this.setUser(user);
		this.pic = pic;
		this.picHash = picHash;
		this.pic106= pic106;
		this.pic640=pic640;
		this.pic800=pic800;
		this.banned=banned;
	}
	
	@ManyToOne
	@JoinColumn(name="id")
	@NotNull
	private User user;
	
	@Id
	@NotNull
	@Column(name = "pic")
	private String pic;
	
	@NotNull
	@Column(name = "picHash")
	private String picHash;
	
	@Column(name = "pic106")
	private String pic106;
	
	@Column(name = "pic640")
	private String pic640;

	@Column(name = "pic800")
	private String pic800;
	
	@Column(name = "banned")
	private Boolean banned;

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPic106() {
		return pic106;
	}

	public void setPic106(String pic106) {
		this.pic106 = pic106;
	}

	public String getPic640() {
		return pic640;
	}

	public void setPic640(String pic640) {
		this.pic640 = pic640;
	}

	public String getPic800() {
		return pic800;
	}

	public void setPic800(String pic800) {
		this.pic800 = pic800;
	}


	public String getPicHash() {
		return picHash;
	}

	public void setPicHash(String picHash) {
		this.picHash = picHash;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getBanned() {
		return banned;
	}

	public void setBanned(Boolean banned) {
		this.banned = banned;
	}
	
}