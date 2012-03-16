package ua.dod.picload.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ua.dod.picload.domain.Picture;

@Repository
public class PictureDAOImpl implements PictureDAO{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void storePicture(Picture picture) {
		sessionFactory.getCurrentSession().saveOrUpdate(picture);
	}

	@Override
	public void deletePicture(String id) {
		Picture picture = getPicture(id);
		if (null != picture) {
			sessionFactory.getCurrentSession().delete(picture);
		}
	}
	
	@Override
	public void deletePicForUser(String user) {
		System.out.println("picture");
		List<Picture> pics = getAllForUser(user);
		for (Picture pic:pics){
			sessionFactory.getCurrentSession().delete(pic);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Picture> getPic106(String id, String pic106) {
		return sessionFactory.getCurrentSession().createSQLQuery("SELECT * FROM pictures WHERE id='"+id+"' AND pic106='"+pic106+"'").addEntity(Picture.class).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Picture> getAllForUser(String id) {
		return sessionFactory.getCurrentSession().createSQLQuery("SELECT * FROM pictures WHERE id='"+id+"' ORDER BY pic").addEntity(Picture.class).list();
	}

	@Override
	public Picture getPicture(String id) {
		return (Picture) sessionFactory.getCurrentSession().get(Picture.class, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Picture> getAllPictures() {
		return sessionFactory.getCurrentSession().createSQLQuery("SELECT * FROM pictures ORDER BY pic").addEntity(Picture.class).list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Picture> getBanned() {
		return sessionFactory.getCurrentSession().createSQLQuery("SELECT * FROM pictures WHERE banned=1").addEntity(Picture.class).list();
	}
}
