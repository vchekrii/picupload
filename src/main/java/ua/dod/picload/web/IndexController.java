package ua.dod.picload.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID; 

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ua.dod.picload.domain.Picture;
import ua.dod.picload.domain.UploadItem;
import ua.dod.picload.domain.User;
import ua.dod.picload.utils.DateFormat;
import ua.dod.picload.utils.ImageResize;
import ua.dod.picload.utils.HashCalculator;
import ua.dod.picload.utils.UserAccess;
import ua.dod.picload.utils.UsersStatistics;
import ua.dod.picload.service.PictureService;
import ua.dod.picload.service.UserService;

@Controller
public class IndexController {
	
	@Autowired
	PictureService pictureService;
	
	@Autowired
	UserService userService;
	
	@Autowired  
	private ReCaptcha reCaptcha; 
	
	UsersStatistics usersStatistics = new UsersStatistics();
	
	@RequestMapping(value={"/index", "/"}, method=RequestMethod.GET)
    public String listIndex(Map<String, Object> map, Model model, HttpServletRequest request, HttpServletResponse response) {
    	List<String> pic106 = new ArrayList<String>();
    	List<Picture> allPictures = new ArrayList<Picture>();
    	String requestHash=null;
    	
		//==================================================================
    	//Set cookie
		try{
			Boolean hasCookie = false;
			Cookie[] cookies = request.getCookies();
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("sessionPicUpload")) {
					hasCookie=true;
					requestHash=cookie.getValue();
					allPictures=pictureService.getAllForUser(cookie.getValue());
					for (Picture picture:allPictures){
						pic106.add(picture.getPic106());
					}
					map.put("pic106", pic106);
					if(userService.getUser(cookie.getValue()).equals(null)){
						throw new NullPointerException();
					}

				} 
			}      
			if(!hasCookie) {
				throw new NullPointerException();
			}
			
		} catch (NullPointerException e) {
			UUID cookieValue = UUID.randomUUID();
			Cookie cookie = new Cookie("sessionPicUpload", cookieValue.toString());
			cookie.setMaxAge(10*365*24*60*60);
			response.addCookie(cookie);
			userService.storeUser(new User(cookieValue.toString(), request.getRemoteAddr(), false));
		}
    	
		//==================================================================
    	//Check if CAPTCHA should be shown
		UserAccess userAccessNew = new UserAccess(requestHash);
		UserAccess userAccessLast;
		
    	if(usersStatistics.usersAccess.containsKey(requestHash)){
			  userAccessLast = usersStatistics.usersAccess.get(requestHash);	
		} else {
			  userAccessLast = userAccessNew;
		}
    	Long accessTime = userAccessNew.getTime()-userAccessLast.getTime();
		Integer count = userAccessLast.getCount();
		
		  if (accessTime<2 && count>=3){
			  map.put("reCaptcha", reCaptcha.createRecaptchaHtml(null, null));
		  }else if (accessTime>=2) {
			  userAccessLast.setCount(0);
			  usersStatistics.usersAccess.remove(requestHash);
			  usersStatistics.usersAccess.put(requestHash, userAccessNew);
		  }
    	model.addAttribute(new UploadItem());
        return "index";
    }

	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public ModelAndView create(@Valid UploadItem uploadItem, BindingResult result, HttpServletRequest request, Model model, Map<String, Object> map) throws IOException, InterruptedException {
      DateFormat dateFormat = new DateFormat();	
      ImageResize imageResize = new ImageResize();
      HashCalculator hashCalculator = new HashCalculator();
      
      InputStream inputStreamHash = null;
      InputStream inputStream = null;
      OutputStream outputStream = null;
      Picture picture;
      String requestHash=null;
      List<Picture> allPictures;
      List<Picture> bannedPictures;
      List<String> hashList=null;
      List<String> pic106 = new ArrayList<String>();
      
      Cookie[] cookies = request.getCookies();
		if (cookies!= null){
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("sessionPicUpload")) {
					requestHash=cookie.getValue();
					allPictures=pictureService.getAllForUser(cookie.getValue());
					for (Picture pic:allPictures){
						pic106.add(pic.getPic106());
					}
					map.put("pic106", pic106);
				}
			} 
		}
	  
	  if(uploadItem.getFileData().getSize()>0 && requestHash!=null){
		  MultipartFile file = uploadItem.getFileData();
		  //==================================================================
		  //Check if CAPTCHA should be shown while any error occurs
		  UserAccess userAccessNew = new UserAccess(requestHash);
		  UserAccess  userAccessLast;
		  if(usersStatistics.usersAccess.containsKey(requestHash)){
			  userAccessLast = usersStatistics.usersAccess.get(requestHash);	
		  } else {
			  userAccessLast = userAccessNew;
		  }
		  Long accessTime = userAccessNew.getTime()-userAccessLast.getTime();
		  Integer count = userAccessLast.getCount();
		  if (accessTime<2 && count>=3){
			  map.put("reCaptcha", reCaptcha.createRecaptchaHtml(null, null));
		  } else if (accessTime<2 && count<3){
			  userAccessLast.setCount(count+1);
			  usersStatistics.usersAccess.remove(requestHash);
			  usersStatistics.usersAccess.put(requestHash, userAccessLast);
		  }
		  //==================================================================
		  //Checks allowed file extension
		  String[] types = { "image/jpeg", "image/pjpeg" };
		  List<String> allowedContentTypes = Arrays.asList(types);
		  String contentType = file.getContentType();
		  if (!allowedContentTypes.contains(contentType)) {
			  result.rejectValue("fileData", "label.wrongExtension", new Object[]{}, "");
			  return new ModelAndView("index"); 
		  }
		  //==================================================================
		  //Check if user is banned
		  User user = userService.getUser(requestHash);
		  if (user.getBanned()){
			  result.rejectValue("fileData", "label.banned", new Object[]{}, "");
			  return new ModelAndView("index"); 
		  }
		  //==================================================================
		  //Get hash of uploaded file (to avoid duplicates)
		  hashList=new ArrayList<String>();
			inputStreamHash = file.getInputStream();
			String hash = hashCalculator.calculateSHA(inputStreamHash);
			inputStreamHash.close();
			allPictures = pictureService.getAllForUser(requestHash);
			for (Picture pict:allPictures){
				hashList.add(pict.getPicHash());
			}
			
			if (hashList.contains(hash)){
				result.rejectValue("fileData", "label.fileExists", new Object[]{}, "");
				return new ModelAndView("index"); 
			}
			
			//==================================================================
			//Check file size
			if(file.getSize()>1054000) {
				result.rejectValue("fileData", "label.fileSize", new Object[]{}, "");
				return new ModelAndView("index"); 
			}
			
		  //==================================================================
		  //Check if such file name already exists	
		  //TODO on deployment: file path slashes
		  File directory = new File(request.getSession().getServletContext().getRealPath("") + "\\resources\\uploadedImages\\");
		  String date = dateFormat.format();
		  File newName = new File(directory, "Img"+date+".jpg");
		  Boolean fileExists = newName.exists();
		  while(fileExists) {
			Thread.sleep(1);
			date = dateFormat.format();
			newName = new File(directory, "Img"+date+".jpg");
			fileExists = newName.exists();
		  }
		  newName.createNewFile();
		  
		  //==================================================================
		  //Check if file is banned
		  bannedPictures = pictureService.getBanned();
		  for (Picture pict : bannedPictures) {
			  if (pict.getPicHash().equals(hash)){
				  result.rejectValue("fileData", "label.fileBanned", new Object[]{}, "");
				  return new ModelAndView("index"); 
			  }
		  }
		  
		  //==================================================================
		  //Validate CAPTCHA
		  if (request.getParameter("recaptcha_challenge_field")!=(null)) {
			  ReCaptchaResponse CaptchaResponse = reCaptcha.checkAnswer(request.getRemoteAddr(), request.getParameter("recaptcha_challenge_field"), request.getParameter("recaptcha_response_field"));  
			  if(CaptchaResponse.isValid()){  
				  userAccessLast.setCount(0);
				  usersStatistics.usersAccess.remove(requestHash);
				  usersStatistics.usersAccess.put(requestHash, userAccessLast);
			  }
			  if(!CaptchaResponse.isValid()){  
				  map.put("reCaptcha", reCaptcha.createRecaptchaHtml(null, null));
				  result.rejectValue("fileData", "label.wrongCaptcha", new Object[]{}, "");
				  return new ModelAndView("index");  
			  } 
		  }
		  
		  //==================================================================
		  //Write file to disk
		  inputStream = file.getInputStream();
		  outputStream = new FileOutputStream(newName);
		  int readBytes = 0;
		  byte[] buffer = new byte[10000];
		  while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
		         outputStream.write(buffer, 0, readBytes);
		  }
		  outputStream.close();
		  inputStream.close();
		  
	   	  //==================================================================
		  //Scale uploaded image & String gets value 'null' if the file is not created
		  String imgPic106 = imageResize.scaleImage(106, directory.toString(), newName.toString(), date);
		  String imgPic640 = imageResize.scaleImage(640, directory.toString(), newName.toString(), date);
		  String imgPic800 = imageResize.scaleImage(800, directory.toString(), newName.toString(), date);      
			
		  picture = new Picture(userService.getUser(requestHash), "Img"+date+".jpg", hash, imgPic106, imgPic640, imgPic800, false );
		  pictureService.storePicture(picture);
	  }
	  
	  RedirectView rv = new RedirectView("index");
	  rv.setExposeModelAttributes(false);
	  return new ModelAndView(rv); 
    }
	
    @RequestMapping(value={"/image/{img:.+}"}, method=RequestMethod.GET)
    public String showImage(Map<String, Object> map, @PathVariable String img, HttpServletRequest request, HttpServletResponse response, Model model) {
    	List<Picture> allPictures;
    	List<Picture> onePicture;
    	List<String> pic106 = new ArrayList<String>();
    	String url;
    	
    	Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies){
			if(cookie.getName().equals("sessionPicUpload")) {
				allPictures=pictureService.getAllForUser(cookie.getValue());
				onePicture=pictureService.getPic106(cookie.getValue(), img);
				for (Picture picture:allPictures){
					pic106.add(picture.getPic106());
				}
				map.put("pic106", pic106);
				map.put("onePicture", onePicture);
			} 
		
		}
		
		 //TODO on deployment: file path slashes	
		url=request.getRequestURL().toString().replace("/image/"+img, "/resources/uploadedImages/");
		model.addAttribute("url", url);
		
    	return "image";
    }

    
    @RequestMapping(value={"/image/delete/{path}/{img}"}, method=RequestMethod.GET)
    public String delete(@PathVariable String img, @PathVariable String path, HttpServletRequest request){
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
    	
    	return "redirect:/"+path;
    }
}

