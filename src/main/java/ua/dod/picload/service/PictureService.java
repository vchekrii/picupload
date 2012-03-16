package ua.dod.picload.service;

import java.util.List;

import ua.dod.picload.domain.Picture;

public interface PictureService {
	
	public void storePicture(Picture picture);
	public void deletePicture(String id);
	public List<Picture> getPic106(String id, String pic106);
	public Picture getPicture(String id);
	public List<Picture> getAllForUser(String id);
	public List<Picture> getAllPictures();
	public void deletePicForUser(String user);
	public List<Picture> getBanned();
}