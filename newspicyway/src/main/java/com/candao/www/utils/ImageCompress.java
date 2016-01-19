/**
 *  缩略图实现，将图片(jpg、bmp、png、gif等等)真实的变成想要的大小
 */
package com.candao.www.utils;
/**
 * A:  770*1094       B:770*540   
 * C:  378*540        D:378*263
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/*******************************************************************************
 * 缩略图类（通用） 本java类能将jpg、bmp、png、gif图片文件，进行等比或非等比的大小转换。 具体使用方法
 * compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
 */
 public class ImageCompress { 
	 private File file = null; // 文件对象 
	 private String inputDir; // 输入图路径
	 private String outputDir; // 输出图路径
	 private String inputFileName; // 输入图文件名
	 private String outputFileName; // 输出图文件名
	 private int outputWidth = 500; // 默认输出图片宽
	 private int outputHeight = 700; // 默认输出图片高
	 private boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)
	 public ImageCompress() { // 初始化变量
		 inputDir = ""; 
		 outputDir = ""; 
		 inputFileName = ""; 
		 outputFileName = ""; 
		 outputWidth = 500; 
		 outputHeight = 700; 
	 } 
	 public void setInputDir(String inputDir) { 
		 this.inputDir = inputDir; 
	 } 
	 public void setOutputDir(String outputDir) { 
		 this.outputDir = outputDir; 
	 } 
	 public void setInputFileName(String inputFileName) { 
		 this.inputFileName = inputFileName;
	 } 
	 public void setOutputFileName(String outputFileName) { 
		 this.outputFileName = outputFileName; 
	 } 
	 public void setOutputWidth(int outputWidth) {
		 this.outputWidth = outputWidth; 
	 } 
	 public void setOutputHeight(int outputHeight) { 
		 this.outputHeight = outputHeight; 
	 } 
	 public void setWidthAndHeight(int width, int height) { 
		 this.outputWidth = width;
		 this.outputHeight = height; 
	 } 
	 
	 /* 
	  * 获得图片大小 
	  * 传入参数 String path ：图片路径 
	  */ 
	 public long getPicSize(String path) { 
		 file = new File(path); 
		 return file.length(); 
	 }
	 
	 // 图片处理 
	 @SuppressWarnings("restriction")
	public String compressPic() { 
		 try { 
			 //获得源文件 
			 file = new File(inputDir +File.separator+ inputFileName); 
			 if (!file.exists()) { 
				 return ""; 
			 } 
			 Image img = ImageIO.read(file); 
			 // 判断图片格式是否正确 
			 if (img.getWidth(null) == -1) {
				 System.out.println(" can't read,retry!" + "<BR>"); 
				 return ""; 
			 } else { 
				 int newWidth; int newHeight; 
				 // 判断是否是等比缩放 
				 if (this.proportion == true) { 
					 // 为等比缩放计算输出的图片宽度及高度 
					 double rate1 = ((double) img.getWidth(null)) / (double) outputWidth + 0.1; 
					 double rate2 = ((double) img.getHeight(null)) / (double) outputHeight + 0.1; 
					 // 根据缩放比率大的进行缩放控制 
					 double rate = rate1 > rate2 ? rate1 : rate2; 
					 newWidth = (int) (((double) img.getWidth(null)) / rate); 
					 newHeight = (int) (((double) img.getHeight(null)) / rate); 
				 } else { 
					 newWidth = outputWidth; // 输出的图片宽度 
					 newHeight = outputHeight; // 输出的图片高度 
				 } 
			 	BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB); 
			 	
			 	/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的
				 * 优先级比速度高 生成的图片质量比较好 但速度慢
				 */ 
			 	tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
			 	FileOutputStream out = new FileOutputStream(outputDir +File.separator +outputFileName);
			 	// JPEGImageEncoder可适用于其他图片类型的转换 
			 	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
			 	JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);  
                /* 压缩质量 */  
                jep.setQuality(0.6f, true);  
                encoder.encode(tag, jep);  
			 	out.close(); 
			 } 
		 } catch (IOException ex) { 
			 ex.printStackTrace(); 
		 } 
		 return outputFileName; 
	} 
 	public String compressPic (String inputDir, String outputDir, String inputFileName, String outputFileName) { 
 		// 输入图路径 
 		this.inputDir = inputDir; 
 		// 输出图路径 
 		this.outputDir = outputDir; 
 		// 输入图文件名 
 		this.inputFileName = inputFileName; 
 		// 输出图文件名
 		this.outputFileName = outputFileName; 
 		return compressPic(); 
 	} 
 	public String compressPic(String inputDir, String outputDir, String inputFileName, String outputFileName, int width, int height, boolean gp) { 
 		// 输入图路径 
 		this.inputDir = inputDir; 
 		// 输出图路径 
 		this.outputDir = outputDir; 
 		// 输入图文件名 
 		this.inputFileName = inputFileName; 
 		// 输出图文件名 
 		this.outputFileName = outputFileName; 
 		// 设置图片长宽
 		setWidthAndHeight(width, height); 
 		// 是否是等比缩放 标记 
 		this.proportion = gp; 
 		return compressPic(); 
 	} 
 	
 	// main测试 
 	// compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
	public static void main(String[] arg) throws IOException { 
 		ImageCompress mypic = new ImageCompress(); 
// 		System.out.println("输入的图片大小：" + mypic.getPicSize("http://192.168.102.50/image01/M00/00/00/wKhmLlVms8iAMbquAASv6gozvyY065.jpg")/1024 + "KB"); 
//		mypic.compressPic("d:\\", "d:\\", "1.png", "r1.png", 770, 1094, false); 
//		System.out.println("输出的图片大小：" + mypic.getPicSize("d:\\r1.png")/1024 + "KB"); 
//		System.out.println("输入的图片大小：" + mypic.getPicSize("d:\\1.jpg")/1024 + "KB"); 
//		mypic.compressPic("d:\\", "d:\\", "1.jpg", "r2.jpg", 770, 1094, false); 
//		System.out.println("输出的图片大小：" + mypic.getPicSize("d:\\r2.jpg")/1024 + "KB"); 
//		System.out.println("输入的图片大小：" + mypic.getPicSize("d:\\1.png")/1024 + "KB"); 
//		mypic.imgCut("d:/1.png", 0, 0, 700, 1000); 
//		System.out.println("输出的图片大小：" + mypic.getPicSize("d:\\r2.jpg")/1024 + "KB"); 
 		mypic.saveToFile("http://192.168.102.50/image01/M00/00/00/wKhmLlVms8iAMbquAASv6gozvyY065.jpg",
 				"D:/workspace/baseline-parent/newspicyway/src/main/webapp/upload/","test.jpg");
 	} 
 	/**
     * 截取图片
     * @param srcImageFile  原图片地址
     * @param x    截取时的x坐标
     * @param y    截取时的y坐标
     * @param desWidth   截取的宽度
     * @param desHeight   截取的高度
     */
    @SuppressWarnings("restriction")
	public  String imgCut(String srcImageFile,String filename, int x, int y, int desWidth,int desHeight) {
    	Date time = new Date();
    	String imagesrc="";
    	String dirTime = String.valueOf(time.getTime());
		String extName = filename.substring(filename.lastIndexOf("."));
        try {
        	
			imagesrc=srcImageFile+File.separator+dirTime+extName;
            Image img;
            ImageFilter cropFilter;
            BufferedImage bi = ImageIO.read(new File(srcImageFile+File.separator+filename));
            int srcWidth = bi.getWidth();
            int srcHeight = bi.getHeight();
            if (srcWidth >= desWidth && srcHeight >= desHeight) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,Image.SCALE_DEFAULT);
                cropFilter = new CropImageFilter(x, y, desWidth, desHeight);
                img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(desWidth, desHeight,BufferedImage.TYPE_INT_RGB);
                tag.getGraphics().drawImage(img, 0, 0, null);
                FileOutputStream out = new FileOutputStream(imagesrc);
			 	// JPEGImageEncoder可适用于其他图片类型的转换 
			 	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
			 	JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);  
                /* 压缩质量 */  
                jep.setQuality(0.6f, true);  
                encoder.encode(tag, jep);  
			 	out.close(); 
                
                
//                Graphics g = tag.getGraphics();
//                g.drawImage(img, 0, 0, null);
//                g.dispose();
//                //输出文件
//                ImageIO.write(tag,extName.substring(1), new File(imagesrc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirTime+extName;
    }
    @SuppressWarnings("rawtypes")
	public  void cutImage(String src,String dest,int x,int y,int w,int h) throws IOException{ 
    	String extName = src.substring(src.lastIndexOf(".")+1);
        Iterator iterator = ImageIO.getImageReadersByFormatName(extName);   
        ImageReader reader = (ImageReader)iterator.next();   
        InputStream in=new FileInputStream(src);  
        ImageInputStream iis = ImageIO.createImageInputStream(in);   
        reader.setInput(iis, true);   
        ImageReadParam param = reader.getDefaultReadParam();   
        Rectangle rect = new Rectangle(x, y, w,h);    
        param.setSourceRegion(rect);   
        BufferedImage bi = reader.read(0,param);     
        ImageIO.write(bi, extName, new File(dest));             
 } 
    /**
     * 图像切割(按指定起点坐标和宽高切割)
     * @param srcImageFile 源图像地址
     * @param result 切片后的图像地址
     * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
     * @param height 目标切片高度
     */
    public  void cut(String srcImageFile, String result,
            int x, int y, int width, int height) {
    	String extName = srcImageFile.substring(srcImageFile.lastIndexOf(".")+1);
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(),
                                cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, extName.substring(1), new File(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------------------------------------------
    //将网络图片保存到本地
    //-------------------------------------------------------------------------------------------------------------
	public String saveToFile(String destUrl,String outDir,String filename) {
		createDir(outDir);
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		try {
			url = new URL(destUrl);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			fos = new FileOutputStream(outDir+File.separator+filename);
			while ((size = bis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}
			fos.flush();
		} catch (IOException e) {
		} catch (ClassCastException e) {
		} finally {
			try {
				fos.close();
				bis.close();
				httpUrl.disconnect();
			} catch (IOException e) {
			} catch (NullPointerException e) {
			}
		}
		return filename;
	}
	public void createDir(String path){
		File file =new File(path);    
		//如果文件夹不存在则创建    
		if  (!file .exists()  && !file .isDirectory())      
		{       
		    file .mkdirs();    
		}
	}
    
 }
