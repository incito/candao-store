package kaiying;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Test7 {

	public static void main(String[] args) throws IOException{
		dishSql();
	}
	
	public static void dishSql() throws IOException{
		//000000000000616,辣道鸳鸯锅,0101001,000000000000064,38.00
		
		String sqlString = "update t_dish set printer ='";
         FileReader reader = new FileReader("d:/t_dish.csv");
         BufferedReader br = new BufferedReader(reader);
         String str = null;
         int n = 0 ;
         while((str = br.readLine()) != null) {
        	 
        	 n++;
        	 StringBuffer sb= new StringBuffer(sqlString);
        	 if(n % 2 == 0){
        		 sb.append("厨打2'   ");
        	 }else {
        		 sb.append("厨打1'  ");
			}
        	 sb.append("  where dishid ='"+str.trim() +"'; \n");
        	 System.out.println(sb.toString());
         }
        
         br.close();
         reader.close();
//         FileWriter writer = new FileWriter("D:/新辣道所有资料/sqlserverdata/caipin.txt");
//         BufferedWriter bw = new BufferedWriter(writer);
//         bw.write(sb.toString());
//         bw.close();
//         writer.close();
	}
	
	
	public static void dish() throws IOException{
		//000000000000616,辣道鸳鸯锅,0101001,000000000000064,38.00
		String sqlString = "insert into  t_dish(dishid,title,dishNo,dishtype,price,userId ,createTime) values (";
		 StringBuffer sb= new StringBuffer("");
         FileReader reader = new FileReader("d:/t_dish.csv");
         BufferedReader br = new BufferedReader(reader);
         String str = null;
         int n = 0 ;
         while((str = br.readLine()) != null) {
        	 
        	 String[] strs = str.split(",");
        	 sb.append(sqlString + " '" +getStr(strs[0])+"','"+getStr(strs[1])+"','"+getStr(strs[2])+"','"+getStr(strs[3])+"','"+getStr(strs[4])+"','"+"bc1022e6-c7f2-46c1-97fc-29413ff07ff3"+"',"+" now()"+"   ); \n");
        	 n++;
        	 if(n % 100 == 0){
        		 sb.append("commit; \n");
        	 }
        	 
         }
         br.close();
         reader.close();
         FileWriter writer = new FileWriter("D:/新辣道所有资料/sqlserverdata/caipin.txt");
         BufferedWriter bw = new BufferedWriter(writer);
         bw.write(sb.toString());
         bw.close();
         writer.close();
	}
	
	
	public static void dishType() throws IOException{
//		000000000000064,锅底类,0101,GDL,000000000000063,2,000000000000007
		String sqlString = "insert into  t_basicdata(id,itemid,itemDesc,itemSort,status,fid ,depthnum,"
				+ "itemType,create_time,update_time,remark) values (";
		
		 StringBuffer sb= new StringBuffer("");
         FileReader reader = new FileReader("D:/新辣道所有资料/sqlserverdata/caipinfenlei.csv");
         BufferedReader br = new BufferedReader(reader);
         String str = null;
         int n = 0 ;
         while((str = br.readLine()) != null) {
        	 String[] strs = str.split(",");
        	 sb.append(sqlString + " '" + strs[0]+"','"+strs[2]+"','"+strs[1]+"','"+"0'"+",'"+" 1"+"','"+strs[4]+"','"+(Integer.parseInt(strs[5]) - 1)+"','"+" DISH "+"',"+" NOW() "+","+ "Now()"+",'"+" "+"'   ); \n");
        	  
        	 n++;
        	 if(n % 100 == 0){
        		 sb.append("commit; \n");
        	 }
        	 
         }
         br.close();
         reader.close();
         FileWriter writer = new FileWriter("D:/新辣道所有资料/sqlserverdata/feilei.txt");
         BufferedWriter bw = new BufferedWriter(writer);
         bw.write(sb.toString());
         bw.close();
         writer.close();
	}
	
	public static String getStr(String str){
		if(!str.contains("'")){
			return str;
		}
		return str.replaceAll("'", "''");
	}
}
