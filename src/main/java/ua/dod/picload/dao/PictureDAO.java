package ua.dod.picload.dao;

import java.util.List;

import ua.dod.picload.domain.Picture;

public interface PictureDAO {
	
	public void storePicture(Picture picture);
	public void deletePicture(String id);
	public List<Picture> getPic106(String hash, String pic106);
	public Picture getPicture(String id);
	public List<Picture> getAllForUser(String hash);
	public List<Picture> getAllPictures();
	public void deletePicForUser(String user);
	public List<Picture> getBanned();
}