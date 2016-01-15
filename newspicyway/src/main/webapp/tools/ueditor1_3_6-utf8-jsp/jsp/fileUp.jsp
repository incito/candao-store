    <%@page import="org.apache.commons.lang.StringUtils"%>
		<%@page import="java.io.File"%>
        <%@page import="java.util.Properties"%>
        <%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
        <%@ page import="com.candao.www.webroom.service.impl.Uploader" %>
        <%@ page import="com.candao.www.webroom.service.impl.ConvertVideoUtil" %>
        <%@ page import="java.io.FileInputStream" %>


            <%
    request.setCharacterEncoding( Uploader.ENCODEING );
    response.setCharacterEncoding( Uploader.ENCODEING );
    
    String currentPath = request.getRequestURI().replace( request.getContextPath(), "" );

    File currentFile = new File( currentPath );

    currentPath = currentFile.getParent() + File.separator;

    //加载配置文件
    String propertiesPath = request.getSession().getServletContext().getRealPath( currentPath + "config.properties" );
    Properties properties = new Properties();
    try {
        properties.load( new FileInputStream( propertiesPath ) );
    } catch ( Exception e ) {
        //加载失败的处理
        e.printStackTrace();
    }
    
    Uploader up = new Uploader(request);
    
    up.setSavePath("upload"); //保存路径
    String[] fileType = {".rar" , ".doc" , ".docx" , ".zip" , ".pdf" , ".txt" , ".swf", ".wmv", ".flv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg", ".ogg", ".mov", ".wmv", ".mp4"};  //允许的文件类型
    up.setAllowFiles(fileType);
    up.setMaxSize(50000 * 1024);        //允许的文件最大尺寸，单位KB
    up.upload();
    String imagePath= request.getContextPath()+"/"+up.getUrl();
    String imageRealPath=request.getRealPath("/")+File.separator+up.getUrl();
    String outputRealPath=imageRealPath.substring(0,imageRealPath.lastIndexOf("."))+".mp4";
    String tempRealPath=imageRealPath.substring(0,imageRealPath.lastIndexOf("."))+".avi";
    String url=imagePath.substring(0,imagePath.lastIndexOf("."))+".mp4";
    String mencoderPath=request.getRealPath("/")+File.separator+"tools"+File.separator+"mencoder.exe ";
    String ffmpegPath=request.getRealPath("/")+File.separator+"tools"+File.separator+"ffmpeg.exe ";
    ConvertVideoUtil converVideo =new ConvertVideoUtil(mencoderPath,ffmpegPath,imageRealPath,outputRealPath,tempRealPath);
    converVideo.start();
// converVideo.convert(imageRealPath,outputRealPath);
// response.getWriter().print("{'url':'"+up.getUrl()+"','fileType':'"+up.getType()+"','state':'"+up.getState()+"','original':'"+up.getOriginalName()+"'}");
    response.getWriter().print("{'url':'"+url+"','fileType':'video/mp4','state':'"+up.getState()+"','original':'"+up.getOriginalName()+"'}");
%>
