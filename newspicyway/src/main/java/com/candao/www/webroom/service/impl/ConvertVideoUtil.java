package com.candao.www.webroom.service.impl;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.candao.common.utils.StreamGobbler;

public class ConvertVideoUtil  extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(ConvertVideoUtil.class);
	
	
	private String mencoder_home ;//mencoder.exe所放的路径
	private String ffmpeg_home ;//ffmpeg.exe所放的路径
		
	
	public String inputFile_home ;//需转换的文件的位置
	public String outputFile_home ;//转换后的flv文件所放的文件夹位置
	private String tempFile_home;//存放rm,rmvb等无法使用ffmpeg直接转换为flv文件先转成的avi文件
	private String fileUrlpath;//上传文件后文件服务器返回的 file id
	 
	 public ConvertVideoUtil(String mencoder_home,String ffmpeg_home,String inputFile_home, String outputFile_home,String tempFile_home){  
		 this.mencoder_home=mencoder_home;
		 this.ffmpeg_home=ffmpeg_home;
		 this.inputFile_home=inputFile_home;
		 this.outputFile_home=outputFile_home;
         this.tempFile_home = tempFile_home;  
     }  
	 
//		@Override
		public void run() {
//			try {
//				Thread.sleep(4000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		 this.convert(inputFile_home, outputFile_home);
//		 if(b){
//			 this.stop();
//		 }
		}    
     
//      /**
//       * 调线程转换
//       */
//	 public void doConvert(){
//		 new ConvertVideoThread().start();
//	 }
//	 
//	 public class  ConvertVideoThread extends Thread{
//		 
//
//		@Override
//		public void run() {
//			new ConvertVideoUtil(mencoder_home, ffmpeg_home, inputFile_home, outputFile_home, tempFile_home).convert(inputFile_home, outputFile_home);
//		}
//	 } 
//	 
        /**  
         *  功能函数  
         * @param inputFile 待处理视频，需带路径  
         * @param outputFile 处理后视频，需带路径  
         * @return  
         */    
        public  boolean convert(String inputFile, String outputFile)    
        {    
            if (!checkfile(inputFile)) {    
                System.out.println(inputFile + " is not file");    
                return false;    
            }    
            if (process(inputFile,outputFile)) {    
                System.out.println("covert is ok"); 
                if(!inputFile.equals(outputFile)){
	                File file=new File(inputFile);
	                if(file.exists()){
	                	file.delete();
	                }
                }
                return true;    
            }    
            return false;    
        }    
        //检查文件是否存在    
        private  boolean checkfile(String path) {    
            File file = new File(path);    
            if (!file.isFile()) {    
                return false;    
            }    
            return true;    
        }    
        /**  
         * 转换过程 ：先检查文件类型，在决定调用 processFlv还是processAVI  
         * @param inputFile  
         * @param outputFile  
         * @return  
         */    
        private  boolean process(String inputFile,String outputFile) {    
            int type = checkContentType( inputFile);    
            boolean status = false;    
            if (type == 0) {    
                status = processFLV(inputFile,outputFile);// 直接将文件转为flv文件    
            } else if (type == 1) {    
                String avifilepath = processAVI(type,inputFile);    
                if (avifilepath == null)    
                    return false;// avi文件没有得到    
                status = processFLV(avifilepath,outputFile);// 将avi转为flv    
            }    
            return status;    
        }    
        /**  
         * 检查视频类型  
         * @param inputFile  
         * @return ffmpeg 能解析返回0，不能解析返回1  
         */    
        private  int checkContentType(String inputFile) {    
            String type = inputFile.substring(inputFile.lastIndexOf(".") + 1,inputFile.length()).toLowerCase();    
            // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）    
            if (type.equals("avi")) {    
                return 0;    
            } else if (type.equals("mpg")) {    
                return 0;    
            } else if (type.equals("wmv")) {    
                return 0;    
            } else if (type.equals("3gp")) {    
                return 0;    
            } else if (type.equals("mov")) {    
                return 0;    
            } else if (type.equals("mp4")) {    
                return 0;    
            } else if (type.equals("asf")) {    
                return 0;    
            } else if (type.equals("asx")) {    
                return 0;    
            } else if (type.equals("flv")) {    
                return 0;    
            }    
            // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),    
            // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.    
            else if (type.equals("wmv9")) {    
                return 1;    
            } else if (type.equals("rm")) {    
                return 1;    
            } else if (type.equals("rmvb")) {    
                return 1;    
            }    
            return 9;    
        }    
        /**  
         *  ffmepg: 能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）  
         * @param inputFile  
         * @param outputFile  
         * @return  
         */    
        private  boolean processFLV(String inputFile,String outputFile) {    
            if (!checkfile(inputFile)) {    
                System.out.println(inputFile + " is not file");    
                return false;    
            }   
            File file = new File(outputFile);  
            if(file.exists()){  
                System.out.println("MP4文件已经存在！无需转换");  
                return true;  
            } else {  
                System.out.println("正在转换成MP4文件……");  
                try {   
//                	String e="D:\\workspace\\project\\kaiying\\src\\main\\webapp\\tools\\ffmpeg.exe  -i D:\\workspace\\project\\kaiying\\src\\main\\webapp\\upload\\20140516\\37391400230343536.avi -b 1500k -vcodec libx264 -g 30 D:\\workspace\\project\\kaiying\\src\\main\\webapp\\upload\\20140516\\37391400230343536.mp4";
//                	String  ffmpegPath="D:\\workspace\\project\\kaiying\\src\\main\\webapp\\tools\\ffmpeg.exe  -i ";
//                	String str=ffmpeg_home+"-i "+inputFile+" -b 1500k -vcodec libx264 -g 30 "+outputFile;
                  	String str=ffmpeg_home+"-i "+inputFile+" -strict experimental -acodec aac -ac 2 -ab 160k -b 1500k -vcodec libx264 -g 30 "+outputFile;
//                	String str=ffmpeg_home+" -i "+inputFile+" -b 1500k -vcodec libvpx -acodec libvorbis -f webm "+outputFile;
//                	String str=ffmpeg_home+" -i "+inputFile+" -acodec libfaac -ab 128k -ac 2 -vcodec libx264 -vpre  -crf 22 -threads 0 "+outputFile;
                	Process p; 
                    // 执行命令   
                    p = Runtime.getRuntime().exec(str);   
                    
                    StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "Error");  
                    StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "Output");  
                    errorGobbler.start();  
                    outputGobbler.start();  
                    try {
						p.waitFor();
					} catch (InterruptedException e) {
						logger.error("-->",e);
						e.printStackTrace();
					} 
                    // 取得命令结果的输出流   
                    InputStream fis = p.getInputStream();   
                    // 用一个读输出流类去读   
                    InputStreamReader isr = new InputStreamReader(fis);   
                    // 用缓冲器读行   
                    BufferedReader br = new BufferedReader(isr);   
                    String line = null;   
                    // 直到读完为止   
                    while ((line = br.readLine()) != null) {   
                    	logger.debug(line,"");   
                    }   
                    return true;  
                } catch (IOException e) {
                	logger.error("-->",e);
                    e.printStackTrace();   
                    return false;
                }   
            }  
             
        }    
        /**  
         * Mencoder:  
         * 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.  
         * @param type  
         * @param inputFile  
         * @return  
         */    
        private  String processAVI(int type,String inputFile) {    
            File file =new File(tempFile_home);    
            if(file.exists()){  
                System.out.println("avi文件已经存在！无需转换");  
                return tempFile_home;  
            }    
            List<String> commend = new java.util.ArrayList<String>();    
            commend.add(mencoder_home);    
            commend.add(inputFile);    
            commend.add("-oac");    
            commend.add("mp3lame");    
            commend.add("-lameopts");    
            commend.add("preset=64");    
            commend.add("-ovc");    
            commend.add("xvid");    
            commend.add("-xvidencopts");    
            commend.add("bitrate=600");    
            commend.add("-of");    
            commend.add("avi");    
            commend.add("-o");    
            commend.add(tempFile_home);    
            StringBuffer test=new StringBuffer();    
            for(int i=0;i<commend.size();i++)    
            test.append(commend.get(i)+" ");    
            System.out.println(test);    
            try     
            {    
            	String  str=mencoder_home+inputFile+" -oac mp3lame -lameopts preset=64 -ovc xvid -xvidencopts bitrate=600 -of avi -o "+tempFile_home;
            	Process p; 
                // 执行命令   
                p = Runtime.getRuntime().exec(str);   
                StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "Error");  
                StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "Output");  
                errorGobbler.start();  
                outputGobbler.start();  
                try {
					p.waitFor();
				} catch (InterruptedException e) {
					logger.error("-->",e);
					e.printStackTrace();
				} 
                // 取得命令结果的输出流   
                InputStream fis = p.getInputStream();   
                // 用一个读输出流类去读   
                InputStreamReader isr = new InputStreamReader(fis);   
                // 用缓冲器读行   
                BufferedReader br = new BufferedReader(isr);   
                String line = null;   
                // 直到读完为止   
                while ((line = br.readLine()) != null) {   
                    System.out.println(line);   
                }   
                System.out.println("who cares");    
                return tempFile_home; 
            } catch (IOException e) {
            	logger.error("-->",e);
                e.printStackTrace();  
                return null;  
            }   
            	
            	
            	
            	
            	
            	
            	
            	
            	
//                ProcessBuilder builder = new ProcessBuilder();    
//                builder.command(commend);    
//                Process p=builder.start();    
//                /**  
//                 * 清空Mencoder进程 的输出流和错误流  
//                 * 因为有些本机平台仅针对标准输入和输出流提供有限的缓冲区大小，  
//                 * 如果读写子进程的输出流或输入流迅速出现失败，则可能导致子进程阻塞，甚至产生死锁。   
//                 */    
//                final InputStream is1 = p.getInputStream();    
//                final InputStream is2 = p.getErrorStream();    
//                new Thread() {    
//                    public void run() {    
//                        BufferedReader br = new BufferedReader(new InputStreamReader(is1));    
//                        try {    
//                            String lineB = null;    
//                            while ((lineB = br.readLine()) != null ){    
//                                if(lineB != null)System.out.println(lineB);    
//                            }    
//                        } catch (IOException e) {    
//                            e.printStackTrace();    
//                        }    
//                    }    
//                }.start();     
//                new Thread() {    
//                    public void run() {    
//                        BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));    
//                        try {    
//                            String lineC = null;    
//                            while ( (lineC = br2.readLine()) != null){    
//                                if(lineC != null)System.out.println(lineC);    
//                            }    
//                        } catch (IOException e) {    
//                            e.printStackTrace();    
//                        }    
//                    }    
//                }.start();     
//                    
//                //等Mencoder进程转换结束，再调用ffmpeg进程    
//                p.waitFor();    
//                 System.out.println("who cares");    
//                return tempFile_home;    
//            }catch (Exception e){     
//                System.err.println(e);     
//                return null;    
//            }     
        }
    
       
        public static void main(String[] args) {
//            File source = new File("D://a.avi");
//            File target = new File("D://18861399971531269.mp4");
//            
//            AudioAttributes audioAttributes = new AudioAttributes();
//            audioAttributes.setCodec("libfaac");
//            audioAttributes.setBitRate(new Integer(128000));
//            audioAttributes.setSamplingRate(new Integer(44100));
//            audioAttributes.setChannels(new Integer(2));
//
//            VideoAttributes videoAttributes = new VideoAttributes();
//            videoAttributes.setCodec("copy");
//            videoAttributes.setTag("h264");
//            videoAttributes.setBitRate(new Integer(5000));
//            videoAttributes.setFrameRate(new Integer(15));
//            videoAttributes.setSize(new VideoSize(512, 384));
//            EncodingAttributes encodingAttributes = new EncodingAttributes();
//            encodingAttributes.setFormat("h264");
//            encodingAttributes.setAudioAttributes(audioAttributes);
//            encodingAttributes.setVideoAttributes(videoAttributes);
//            Encoder encoder = new Encoder();
//            try {
//				encoder.encode(source, target, encodingAttributes);
//				System.out.println("ok");
//			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InputFormatException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (EncoderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
        	
	}  
