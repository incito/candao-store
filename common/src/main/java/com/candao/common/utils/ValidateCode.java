package com.candao.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ValidateCode {
	/** 验证码图片的宽度 */
	private int width;

	/** 验证码图片的高度 */
	private int height;

	/** 验证码字符个数 */
	private int codeCount;

	private int x;

	/** 字体高度 */
	private int fontHeight;

	private int codeY;

	/** 验证码 */
	private String codeStr;

	/** 验证图片 */
	private BufferedImage buffImg;

	private char[] codeSequence = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};
	/**
	 * 生成验证码图片的构造函数
	 * @param width
	 * @param height
	 * @param codeCount
	 */
	public ValidateCode(int width, int height, int codeCount) {
		this.width = width;
		this.height = height;
		this.codeCount = codeCount;
		x = width / (codeCount + 1);
		fontHeight = height - 3;
		codeY = height - 3;
		initCode();
	}
	/**
	 * 只生成验证码，不生成图片的构造函数
	 * @param codeCount
	 */
	public ValidateCode(int codeCount) {
		this.codeCount = codeCount;
		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		// 创建一个随机数生成器类
		Random random = new Random();
		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			// String strRand =
			// String.valueOf(codeSequence[random.nextInt(36)]);
			String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			// 将产生的四个随机数组合在一起。
			randomCode.append(strRand);
		}
		codeStr = randomCode.toString();
	}

	/**
	 * 生成验证图片和验证码
	 */
	private void initCode() {

		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();

		// 创建一个随机数生成器类
		Random random = new Random();

		// 将图像填充为白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		// 设置字体。
		g.setFont(font);

		// 画边框。
		g.setColor(new Color(0xc3, 0xc5, 0xc2));
		g.drawRect(0, 0, width - 1, height - 1);

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			// String strRand =
			// String.valueOf(codeSequence[random.nextInt(36)]);
			String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			red = random.nextInt(200);
			green = random.nextInt(200);
			blue = random.nextInt(200);

			// 用随机产生的颜色将验证码绘制到图像中。
			g.setColor(new Color(red, green, blue));
			g.drawString(strRand, (i) * x + 7, codeY);
			// 将产生的四个随机数组合在一起。
			randomCode.append(strRand);
		}
		codeStr = randomCode.toString();

	}

	/**
	 * 获得验证码图片
	 * 
	 * @return 验证码图片
	 */
	public BufferedImage getImage() {
		return buffImg;
	}

	/**
	 * 获得验证码
	 * 
	 * @return 验证码
	 */
	public String getCode() {
		return codeStr;
	}
	
	public static void main(String[] args) {
		ValidateCode validateCode=new ValidateCode(5,5,4);
		String str=validateCode.getCode();
	}

}
