package ua.dod.picload.service;

import java.util.List;

import ua.dod.picload.domain.Picture;
import ua.dod.picload.dao.PictureDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PictureServiceImpl implements PictureService{

	@Autowired
	PictureDAO pictureDao;
	
	@Transactional
	@Override
	public void storePicture(Picture picture) {
		pictureDao.storePicture(picture);
	}
	
	@Transactional
	@Override
	public void deletePicture(String id) {
		pictureDao.deletePicture(id);
	}
	
	

	@Transactional
	@Override
	public List<Picture> getPic106(String id, String pic106) {
		return pictureDao.getPic106(id,pic106);
	}

	@Transactional
	@Override
	public Picture getPicture(String id) {
		return pictureDao.getPicture(id);
	}
	
	@Transactional
	@Override
	public List<Picture> getAllForUser(String id) {
		return pictureDao.getAllForUser(id);
	}

	@Transactional
	@Override
	public List<Picture> getAllPictures() {
		return pictureDao.getAllPictures();
	}

	@Transactional
	@Override
	public void deletePicForUser(String user) {
		pictureDao.deletePicForUser(user);
		
	}
	
	@Transactional
	@Override
	public List<Picture> getBanned() {
		return pictureDao.getBanned();
		
	}
}
