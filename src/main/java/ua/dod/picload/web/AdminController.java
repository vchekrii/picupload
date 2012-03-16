package ua.dod.picload.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import ua.dod.picload.domain.Picture;
import ua.dod.picload.domain.User;
import ua.dod.picload.service.PictureService;
import ua.dod.picload.service.UserService;
import ua.dod.picload.utils.ImageOverlay;

@SessionAttributes("url")
@Controller
public class AdminController {
	
	static Set<String> bannedUsers = new HashSet<String>();
	
	@Autowired
	UserService userService;
	
	@Autowired
	PictureService pictureService;
	
	@RequestMapping(value={"/admin"}, method=RequestMethod.GET)
	public String getAdmin(Map<String, Object> map, Model model, HttpServletResponse response) {
	
		//Get free space
		//TODO on deployment: change root directory
		File rootDir = new File("D:");
		Long freeSpace = rootDir.getFreeSpace() /1024 /1024;
		Long usedSpace = (rootDir.getTotalSpace()-rootDir.getFreeSpace()) /1024 /1024;
		model.addAttribute("freeSpace", freeSpace);
		model.addAttribute("usedSpace", usedSpace);
		
		List<Picture> allPictures = pictureService.getAllPictures();
		for (Picture pic:allPictures) {
			pic.setPic(pic.getPic().replace(".jpg", ""));
		}
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		
		map.put("allUsers", userService.getAllUsers());
		map.put("allPictures", allPictures);
		return "admin";
	}
	
    
    @RequestMapping(value={"/imageAdmin/{img}"}, method=RequestMethod.GET)
    public String showImageAdmin(Map<String, Object> map, @PathVariable String img, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Picture> picture = new ArrayList<Picture>();
		String url=request.getRequestURL().toString().replace("/imageAdmin/"+img, "/resources/uploadedImages/");
		img += ".jpg";
		
		picture.add(pictureService.getPicture(img));
		map.put("onePicture", picture);
		 //TODO on deployment: file path slashes
		model.addAttribute("url", url);
		
    	return "imageAdmin";
    }
	
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
    	return "login";
    }
    
    @RequestMapping(value = "/deleteUser/{path}/{id}/{user}")
    public String deleteUser(@PathVariable String user, @PathVariable String path, @PathVariable String id, HttpServletRequest request, Model model) {
    	List<Picture> allPictures = new ArrayList<Picture>();
    	
    	String directory = request.getSession().getServletContext().getRealPath("") + "\\resources\\uploadedImages\\";
    	allPictures=pictureService.getAllForUser(user);
    	for (Picture picture:allPictures) {
    		File delFile = new File(directory, picture.getPic());
    		File delFile2 = new File(directory, picture.getPic106());
    		File delFile3 = new File(directory, picture.getPic640());
    		File delFile4 = new File(directory, picture.getPic800());
    		delFile.delete();
    		delFile2.delete();
    		delFile3.delete();
    		delFile4.delete();
    	}
    	pictureService.deletePicForUser(user);
    	userService.deleteUser(user);
    	
    	System.out.println(id);
    	
    	if ((path+"/"+id).equals(model.asMap().get("url"))){
    		return "redirect:/admin";
    	}
    	if (id.equals("null")){
    		return "redirect:/"+path;
    	}
    	return "redirect:/"+path+"/"+id;
    }
    
    @RequestMapping(value = {"/banUser/{path}/{id}/{userId}"})
    public String banUser(@PathVariable String userId, @PathVariable String path, @PathVariable String id) {
    	User user=userService.getUser(userId);
    	user.setBanned(true);
    	userService.storeUser(user);
    	
    	if (id.equals("null")){
    		return "redirect:/"+path;
    	}
    	return "redirect:/"+path+"/"+id;
    }
    
    @RequestMapping(value = {"/unbanUser/{path}/{id}/{userId}"})
    public String unbanUser(@PathVariable String userId, @PathVariable String path, @PathVariable String id) {
    	User user=userService.getUser(userId);
    	user.setBanned(false);
    	userService.storeUser(user);
    	
    	if (id.equals("null")){
    		return "redirect:/"+path;
    	}
    	return "redirect:/"+path+"/"+id;
    }
    
    @RequestMapping(value = {"/banImage/{path}/{id}/{img}"})
    public String banImageAdmin(@PathVariable String img, @PathVariable String path, @PathVariable String id, HttpServletRequest request) throws IOException {
    	ImageOverlay imageOverlay = new ImageOverlay();
    	
    	System.out.println(img);
    	System.out.println(path);
    	 
    	//TODO on deployment: change directory slashes
    	String dirUnbanned = request.getSession().getServletContext().getRealPath("") + "\\resources\\uploadedImages\\";
    	String dirBanned = dirUnbanned+"banned\\";
    	Picture picture=pictureService.getPicture(img+".jpg");
    	
    	File pic = new File(dirUnbanned, img+".jpg");
    	pic.renameTo(new File(dirBanned, img+".jpg"));
    	
    	File pic106 = new File(dirUnbanned, img+"_x106.jpg");
    	pic106.renameTo(new File(dirBanned, img+"_x106.jpg"));
    	
    	File pic640 = new File(dirUnbanned, img+"_x640.jpg");
    	pic640.renameTo(new File(dirBanned, img+"_x640.jpg"));
    	
    	File pic800 = new File(dirUnbanned, img+"_x800.jpg");
    	pic800.renameTo(new File(dirBanned, img+"_x800.jpg"));
    	
    	imageOverlay.overlay(dirBanned, dirUnbanned, img+"_x106.jpg", "ban.png");
    	
    	picture.setBanned(true);
    	pictureService.storePicture(picture);

    	if (id.equals("null")){
    		return "redirect:/{path}";
    	}
    	return "redirect:/{path}/{id}";
    }
    
    @RequestMapping(value = {"/unbanImage/{path}/{id}/{img}"})
    public String unbanImage(@PathVariable String img, @PathVariable String path, @PathVariable String id, HttpServletRequest request) {
    
    	//TODO on deployment: change directory slashes
    	String dirUnbanned = request.getSession().getServletContext().getRealPath("") + "\\resources\\uploadedImages\\";
    	String dirBanned = dirUnbanned+"\\banned";
    	Picture picture=pictureService.getPicture(img+".jpg");
    	
    	File pic = new File(dirBanned, img+".jpg");
    	pic.renameTo(new File(dirUnbanned, img+".jpg"));
    	
    	File picDel = new File(dirUnbanned, img+"_x106.jpg");
    	picDel.delete();
    	File pic106 = new File(dirBanned, img+"_x106.jpg");
    	pic106.renameTo(new File(dirUnbanned, img+"_x106.jpg"));
    	
    	File pic640 = new File(dirBanned, img+"_x640.jpg");
    	pic640.renameTo(new File(dirUnbanned, img+"_x640.jpg"));
    	
    	File pic800 = new File(dirBanned, img+"_x800.jpg");
    	pic800.renameTo(new File(dirUnbanned, img+"_x800.jpg"));
    	picture.setBanned(false);
    	pictureService.storePicture(picture);
    	
    	if (id.equals("null")){
    		return "redirect:/{path}";
    	}
    	return "redirect:/{path}/{id}";
    }
    
    @RequestMapping(value = "/user/{user}")
    public String getUserInf(@PathVariable String user, HttpServletRequest request, Model model, Map<String, Object> map) {
    	
		//Get free space
		//TODO on deployment: change root directory
		File rootDir = new File("D:");
		Long freeSpace = rootDir.getFreeSpace() /1024 /1024;
		Long usedSpace = (rootDir.getTotalSpace()-rootDir.getFreeSpace()) /1024 /1024;
		model.addAttribute("freeSpace", freeSpace);
		model.addAttribute("usedSpace", usedSpace);
		
		List<Picture> allPictures = pictureService.getAllForUser(user);
		for (Picture pic:allPictures) {
			pic.setPic(pic.getPic().replace(".jpg", ""));
		}
		
		map.put("allUsers", userService.getAllUsers());
		map.put("allPictures", allPictures);
		model.addAttribute("url", "user/"+user);
    
    	return "user";
    }
    
    @RequestMapping(value={"/image/delete/{path}/{id}/{img}"}, method=RequestMethod.GET)
    public String delete(@PathVariable String img, @PathVariable String path, @PathVariable String id, HttpServletRequest request){
    	String directory;
    	
    	pictureService.deletePicture(img+".jpg");
    	 //TODO on deployment: file path slashes
    	directory = request.getSession().getServletContext().getRealPath("") + "\\resources\\uploadedImages\\";
    	File delFile = new File(directory, img+".jpg");
    	File delFile1 = new File(directory, img+"_x106.jpg");
    	File delFile2 = new File(directory, img+"_x640.jpg");
    	File delFile3 = new File(directory, img+"_x800.jpg");
    	delFile.delete();
    	delFile1.delete();
    	delFile2.delete();
    	delFile3.delete();
    	return "redirect:/"+path+"/"+id;
    }

}
