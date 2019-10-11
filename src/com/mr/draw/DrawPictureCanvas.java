package com.mr.draw;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
public class DrawPictureCanvas extends Canvas {
	private Image image=null;//创建画板中展示的图片对象；
	/*
	 * 设置化般中的图片
	 * image-画板中展示的图片对象
	 */
	public void setImage(Image image) {
		this.image=image;
	}
	/*
	 *重写paint方法
	 */
	public void paint(Graphics g) {
		g.drawImage(image,0,0,null);//在画布上绘制图像；
	}
	/*
	 * 重写update方法，这样可解决屏幕闪烁问题
	 */
	public void update(Graphics g) {
		paint(g);//调用paint方法
	}

}
