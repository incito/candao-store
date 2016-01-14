package com.candao.common.utils;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;


public class test {
 
    public void runCmd(String command) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
 
            while ( (line = br.readLine()) != null)
                System.out.println(line);
 
            int exitVal = proc.waitFor();
            System.out.println("Process exitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
 
    public boolean transfer(String infile,String outfile) {
        String avitoflv = "ffmpeg -i "+infile+" -ar 22050 -ab 56 -f flv -y -s 320x240 "+outfile;
        String flvto3gp = "ffmpeg -i " + infile + " -ar 8000 -ac 1 -acodec amr_nb -vcodec h263 -s 176x144 -r 12 -b 30 -ab 12 " + outfile;
        String avito3gp = "ffmpeg -i " + infile + " -ar 8000 -ac 1 -acodec amr_nb -vcodec h263 -s 176x144 -r 12 -b 30 -ab 12 " + outfile;
        String avitojpg = "ffmpeg -i " + infile + " -y -f image2 -ss 00:00:10 -t 00:00:01 -s 350x240 " + outfile;
 
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(avitoflv);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
 
            while ( (line = br.readLine()) != null)
                System.out.println(line);
 
            int exitVal = proc.waitFor();
            System.out.println("Process exitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        return true;
    }
 
    public static String readFile(String fileName,int id) {
        String dataStr = "";
        FileInputStream fis = null;
 
        try {
            FileReader file = new FileReader(fileName);//建立FileReader对象，并实例化为fr
            BufferedReader br=new BufferedReader(file);//建立BufferedReader对象，并实例化为br
            String Line=br.readLine();//从文件读取一行字符串
            dataStr=Line;
            br.close();//关闭BufferedReader对象
        } catch(Exception e) {
 
        } finally {
            try {
                if(fis!=null)
                    fis.close();
            } catch(Exception e) {}
        }
        return dataStr;
    }
 
    public String readtime(String file) {
        String str="/root/Desktop/info.txt";
        String timelen = "";
        String cmd = "timelen "+file;
 
        runCmd(cmd);
        timelen=readFile(str,1);
 
        return timelen;
    }

	public static void main(String[] args) {
		try {
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
			Date beginDate= format.parse("2007-12-24");
			java.util.Date endDate= format.parse("2007-12-25");
			long day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
			System.out.println("相隔的天数="+day);
		} catch (ParseException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

	}
}