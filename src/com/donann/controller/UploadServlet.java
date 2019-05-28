package com.donann.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.donann.domain.Member;
import com.donann.server.MemberServer;
import com.donann.server.impl.MemberServerImpl;
import com.donann.util.UUIDUtils;

/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends BaseServlet {
	private MemberServer memberServer=new MemberServerImpl();
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void upload(HttpServletRequest request, HttpServletResponse response) throws 

ServletException, IOException {
		// TODO Auto-generated method stub

		
		//现在1.创建DiskFileItemFactory核心对象
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//2.创建上传处理器
		ServletFileUpload handler = new ServletFileUpload(factory);
		//3.调用处理器的方法，来解析HTTP请求
		List<Member> mem =null;
		try {
			//items就是解析到的每部分内容
			List<FileItem> items = handler.parseRequest(request);
			for(FileItem item : items) {
				//每一个item就是解析出来的内容，要获取你关心的内容，肯定是要调用FileItem

对象的方法
				if(item.isFormField()) {//isFormField方法用来判断此项是否是一个普通的表

单域
					//关心表单域的name属性
					//关心对应的value值
//					System.out.println(item.getFieldName()+":"+item.getString());
					//如果表单域的值包含中文，且出现了中文乱码
					System.out.println(item.getFieldName()+":"+item.getString

("utf-8"));
				
				}else {
					//说明这是包含文件信息的上传域
					//getString方法获取的是文本形式的文件内容（不可取）
//					System.out.println(item.getFieldName()+":"+item.getString

("gbk"));	
					//getName方法返回的才是上传的文件的名称
					System.out.println("--"+item.getFieldName()+":"+item.getName

());
					//文件的内容，通过getInputStream流的方式来读取
					InputStream is = item.getInputStream();
//					web.xml配置文件路径
//					String path =getServletContext().getInitParameter("upload-

dir");
	
					
					//实现按年月日分类文件
					Date now=new Date();
					DateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
					String dateString=df.format(now);
					String year=dateString.substring(0, dateString.indexOf('-'));
					String month=dateString.substring(dateString.indexOf

('-')+1,dateString.lastIndexOf('-'));
					System.out.println(year+" "+month);
					
					String path=getServletContext().getRealPath("/images/avatar");
//					
					int pathLength=path.length();
					path=path+"/"+year+"/"+month+"/";
					System.out.println("==============");
					System.out.println(path);
					File uploadDir = new File(path);
					
					String email=request.getParameter("email");
					System.out.println(request.getParameter("email"));
					Member member=new Member(email);
					
					
					if(!uploadDir.exists()) {
						new File(path).mkdirs();
					}
					/*
					 * 	利用日期的长整型数转换为字符串存入文件名中
					 * Calendar time=Calendar.getInstance();
					 * long timefilename=time.getTimeInMillis();
					 * String filename = null; 
					 * String ss=filename.valueOf(timefilename); 
					 * 
					 */
					//使用UUID的唯一性转换为字符串存入文件名中
					String oldName=item.getName();
					String newName=UUIDUtils.getuuid();
					//测试输出.jpg
					System.out.println(oldName.substring(oldName.lastIndexOf

('.')));
					File img=new File(path,newName+oldName.substring

(oldName.lastIndexOf('.')));
					FileOutputStream fos = new FileOutputStream(img);
					String avatar=img.getCanonicalPath();
					avatar=avatar.substring(pathLength+1);
					System.out.println(avatar);
					member.setAvatar(avatar);
					
					memberServer.uploadAvatar(member);
					
					byte[] buffer = new byte[1024];
					int len = -1;
					while((len=is.read(buffer))!=-1) {
						fos.write(buffer, 0, len);
					}
					mem= memberServer.getMessage(member);
					System.out.println("GG--"+mem.get(0).getLevel());
					//关闭流
					fos.close();
					is.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("mem", mem.get(0));
		request.getRequestDispatcher("/member.jsp").forward(request, response);
	}

}
