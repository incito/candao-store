package xml;

import java.util.Arrays;

/**
 * Created by Administrator on 2016-7-29.
 */
public class Row {
	//内容
	private String[] datas;
	//字体
	private String font;
	//位置
	private String[] locations;
	
	public String[] getDatas() {
		return datas;
	}
	public void setDatas(String[] datas) {
		this.datas = datas;
	}
	public String getFont() {
		return font;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public String[] getLocations() {
		return locations;
	}
	public void setLocations(String[] locations) {
		this.locations = locations;
	}
	
	public static void main(String[] args) {
		String[] buffer = new String[0];
		int newLength = 0;
		buffer = Arrays.copyOf(buffer, ++newLength);
		System.out.println(buffer.length);
	}
}
