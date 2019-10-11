package com.mr.draw;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.BorderLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JColorChooser;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import com.mr.util.FrameGetShape;//这是明日科技自己封装的包中的类
import com.mr.util.ShapeWindow;
import com.mr.util.Shapes;
import com.mr.util.DrawImageUtil;
import java.awt.AlphaComposite;
import java.awt.Font;
import javax.swing.JOptionPane;

public class DrawPictureFrame extends JFrame implements FrameGetShape {
	//创建一个8位BGR颜色分量的图像；
	BufferedImage image=new BufferedImage(570,390,BufferedImage.TYPE_INT_BGR);
	Graphics gs=image.getGraphics();//获得图像的绘图对象；
	Graphics2D g=(Graphics2D) gs;//将绘图对象转换为Graphics2D类型；
	DrawPictureCanvas canvas=new DrawPictureCanvas();//创建画布对象；
	Color foreColor=Color.BLACK;//定义前景色;0
	Color backgroundColor=Color.WHITE;//定义背景色；
	int x=-1;
	int y=-1;
	boolean rubber=false;//橡皮擦;
	private JToolBar toolBar;//工具栏;
	private JButton eraserButton;//橡皮按钮；
	private JToggleButton strokeButton1;//细线按钮；
	private JToggleButton strokeButton2;//粗线按钮；
	private JToggleButton strokeButton3;//较粗按钮
	private JButton backgroundButton;//背景色按钮；
	private JButton foregroundButton;//前景色按钮；
	private JButton clearButton;//清除按钮；
	private JButton saveButton;//保存按钮；
	private JButton shapeButton;//图形按钮；
	boolean drawShape=false;
	Shapes shape;
	private JMenuItem strokeMenuItem1;//细线菜单
	private JMenuItem strokeMenuItem2;//粗线菜单
	private JMenuItem strokeMenuItem3;//较粗菜单
	private JMenuItem clearMenuItem;//细线菜单
	private JMenuItem foregroundMenuItem;//细线菜单
	private JMenuItem backgroundMenuItem;//细线菜单
	private JMenuItem eraserMenuItem;//细线菜单
	private JMenuItem exitMenuItem;//细线菜单
	private JMenuItem saveMenuItem;//细线菜单
	private JMenuItem shuiyinMenuItem;//水印菜单
	private String shuiyin="";
	
	public DrawPictureFrame() {
		setResizable(false); // 窗体不能改变大小
		setTitle("画图程序(水印内容:["+shuiyin+"])");//设置标题，添加水印内容提示
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 窗体关闭则停止程序
		setBounds(500, 100, 574, 460);// 设置窗口位置和宽高；
		init();//组件初始化;
		addListener();
	}
	/*
	 * 组件初始化
	 */
	private void init() {
		g.setColor(backgroundColor);//用背景色设置绘图对象的颜色；
		g.fillRect(0,0,570,390);//用背景色填充整个画布；
		g.setColor(foreColor);//用前景色设置绘图对象的颜色；
		canvas.setImage(image);//设置画布的图像；
		getContentPane().add(canvas);//将画布添加到窗体容器默认布局的中部位置；
		toolBar=new JToolBar();//初始化工具栏；
		getContentPane().add(toolBar,BorderLayout.NORTH);//工具栏添加到窗体最北的位置；
		saveButton=new JButton("保存");
		toolBar.add(saveButton);//工具栏添加按钮;
		toolBar.addSeparator();//添加分割条；
		//初始化按钮对象，并添加文本内容
		strokeButton1=new JToggleButton("细线");
		strokeButton1.setSelected(true);//细线按钮处于被选中状态
		toolBar.add(strokeButton1);
		strokeButton2=new JToggleButton("粗线");
		toolBar.add(strokeButton2);
		strokeButton3=new JToggleButton("较粗");
		toolBar.add(strokeButton3);
		//画笔粗细按钮组，保证只有一个按钮被选中
		ButtonGroup strokeGroup=new ButtonGroup();
		strokeGroup.add(strokeButton1);
		strokeGroup.add(strokeButton2);
		strokeGroup.add(strokeButton3);
		toolBar.addSeparator();
		backgroundButton=new JButton("背景颜色");
		toolBar.add(backgroundButton);
		foregroundButton=new JButton("前景颜色");
		toolBar.add(foregroundButton);
		toolBar.addSeparator();
		shapeButton=new JButton("图形");
		toolBar.add(shapeButton);
		clearButton=new JButton("消除");
		toolBar.add(clearButton);
		eraserButton=new JButton("橡皮");
		toolBar.add(eraserButton);
		JMenuBar menuBar=new JMenuBar();//创建菜单栏
		setJMenuBar(menuBar);//窗体载入菜单栏
		JMenu systemMenu=new JMenu("系统");//初始化菜单对象，并添加文本内容
		menuBar.add(systemMenu);//菜单栏添加菜单对象
		shuiyinMenuItem=new JMenuItem("设置水印");//初始化菜单对象，并添加文本内容
		systemMenu.add(shuiyinMenuItem);//菜单添加菜单项
		saveMenuItem=new JMenuItem("保存");//初始化菜单项对象，并添加文本内容
		systemMenu.add(saveMenuItem);//菜单添加菜单项
		systemMenu.addSeparator();//添加分割条
		exitMenuItem=new JMenuItem("退出");//初始化菜单项对象，并添加文本内容
		systemMenu.add(exitMenuItem);//菜单添加菜单项
		JMenu strokeMenu=new JMenu("线形");
		menuBar.add(strokeMenu);
		strokeMenuItem1=new JMenuItem("细线");
		strokeMenu.add(strokeMenuItem1);
		strokeMenuItem2=new JMenuItem("粗线");
		strokeMenu.add(strokeMenuItem2);
		strokeMenuItem3=new JMenuItem("较粗");
		strokeMenu.add(strokeMenuItem3);
		
		JMenu colorMenu=new JMenu("颜色");
		menuBar.add(colorMenu);
		foregroundMenuItem=new JMenuItem("前景颜色");
		colorMenu.add(foregroundMenuItem);
		backgroundMenuItem=new JMenuItem("背景颜色");
		colorMenu.add(backgroundMenuItem);
		
		JMenu editMenu=new JMenu("编辑");
		menuBar.add(editMenu);
		clearMenuItem=new JMenuItem("清除");
		editMenu.add(clearMenuItem);
		eraserMenuItem=new JMenuItem("清除");
		editMenu.add(eraserMenuItem);
		
		}
	/*
	 * 为组件添加动作监听
	 */
	public void addListener() {
		//画板添加鼠标移动事件监听
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(final MouseEvent e) {  //当鼠标拖曳时；
				if(x>0&&y>0) {                              //如果x和y存在鼠标记录；
					if(rubber) {                            //如果橡皮标识为true，表示使用橡皮；
						g.setColor(backgroundColor);        //绘图工具使用背景色；
						g.fillRect(x, y, 10, 10);           //在鼠标滑过的位置画填充的正方形
					}else {                                 //如果橡皮标识为false，表示画笔画图
						g.drawLine(x, y, e.getX(), e.getY());//在鼠标滑过的位置画直线
					}
				}
				x=e.getX();//上一次鼠标绘制点的横坐标；
				y=e.getY();//上一次鼠标绘制点的纵坐标；
				canvas.repaint();//更新画布
			}
		});
		canvas.addMouseListener(new MouseAdapter() {//画板添加鼠标单击事件监听
			public void mouseReleased(final MouseEvent arg0) {//当按键抬起时
				x=-1;                                         //将记录上一次鼠标绘制点的横坐标恢复成-1
				y=-1;                                         //将记录上一次鼠标绘制点的纵坐标恢复成-1
			}
			public void mousePressed(MouseEvent e){//当按下鼠标时
				if(drawShape) {//如果此时鼠标画的是图形
					switch (shape.getType()) {//判断图形的种类
					case Shapes.YUAN://如果是圆形
						//计算坐标，让鼠标处于图形的中心位置
						int yuanX=e.getX()-shape.getWidth()/2;
						int yuanY=e.getY()-shape.getHeigth()/2;
						//创建圆形图形，并指定坐标和宽高
						Ellipse2D yuan=new Ellipse2D.Double(yuanX,yuanY,shape.getWidth(),shape.getHeigth());
						g.draw(yuan);//画图工具画此圆形
						break;//结束switch语句
					case Shapes.FANG://如果是方形
						//计算坐标，让鼠标处于图形的中心位置
						int fangX=e.getX()-shape.getWidth()/2;
						int fangY=e.getY()-shape.getHeigth()/2;
						//创建方形图像，并指定坐标和宽高
						Rectangle2D fang=new Rectangle2D.Double(fangX, fangY, shape.getWidth(), shape.getHeigth());
						g.draw(fang);//绘制方形
						break;
					}
					canvas.repaint();//更新画布
					//画图形标识变量为false，说明现在鼠标画的是图形，而不是线条
					drawShape=false;
				}
			}
		});
		
	
		strokeButton1.addActionListener(new ActionListener() {//细线按钮添加动作监听
			public void actionPerformed(final ActionEvent arg0) {//单击时
				//声明画笔的属性，粗细为1像素，线条末端无修饰，折线处呈尖角
				BasicStroke bs=new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);//画图工具使用此画笔
			}
		});
		strokeButton2.addActionListener(new ActionListener() {//细线按钮添加动作监听
			public void actionPerformed(final ActionEvent arg0) {//单击时
				//声明画笔的属性，粗细为1像素，线条末端无修饰，折线处呈尖角
				BasicStroke bs=new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);//画图工具使用此画笔
			}
		});
		strokeButton3.addActionListener(new ActionListener() {//细线按钮添加动作监听
			public void actionPerformed(final ActionEvent arg0) {//单击时
				//声明画笔的属性，粗细为1像素，线条末端无修饰，折线处呈尖角
				BasicStroke bs=new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);//画图工具使用此画笔
			}
		});
		backgroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				//打开颜色对话框，参数依次为：父窗体、标题、默认选中的颜色
				Color bgColor=JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框",Color.CYAN);
				if (bgColor!=null) {
					backgroundColor=bgColor;//如果选中的颜色不是空的，将选中的颜色赋给背景色变量
				}
				//背景色按钮也更换为这种背景颜色
				backgroundButton.setBackground(backgroundColor);
				g.setColor(backgroundColor);//绘图工具也使用背景色
				g.fillRect(0, 0, 570, 390);//画一个背景色的方形填满整个画布
				g.setColor(foreColor);//绘图工具使用前景色
				canvas.repaint();//更新画布
			}
		});
		
		foregroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				//打开颜色对话框，参数依次为：父窗体、标题、默认选中的颜色
				Color fColor=JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框",Color.CYAN);
				if (fColor!=null) {
					foreColor=fColor;//如果选中的颜色不是空的，将选中的颜色赋给背景色变量
				}
				//背景色按钮也更换为这种背景
				foregroundButton.setForeground(foreColor);
				g.setColor(foreColor);//绘图工具也使用背景色
			}
		});
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				g.setColor(backgroundColor);
				g.fillRect(0, 0, 570, 390);//画一个背景颜色的方形填充满整个画布
				g.setColor(foreColor);
				canvas.repaint();
			}
		});
		eraserButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {//单击工具栏上的橡皮擦，使用橡皮
				if(eraserButton.getText().equals("橡皮")) {
					rubber=true;
					eraserButton.setText("画图");//改变按钮上显示的文本为画图
				}else {
					rubber=false;
					eraserButton.setText("橡皮");
					g.setColor(foreColor);
				}
			}
		});
		
		shapeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//单击时
				//创建图形选择组件
				ShapeWindow shapeWindow=new ShapeWindow(DrawPictureFrame.this);
				int shapeButtonWidth=shapeButton.getWidth();//获取图像按钮宽度
				int shapeWindowWidth=shapeWindow.getWidth();//获取图形按钮宽度
				int shapeButtonX=shapeButton.getX();//获取图形按钮横坐标
				int shapeButtonY=shapeButton.getY();//获取图形按钮纵坐标
				//计算图形组件横坐标，让组件与图形按钮居中对齐
				int shapeWindowX=getX()+shapeButtonX-(shapeWindowWidth-shapeButtonWidth)/2;
				//计算图形组件纵坐标，让其显示在图形按钮下方
				int shapeWindowY=getY()+shapeButtonY+80;
				//设置图形组件坐标位置
				shapeWindow.setLocation(shapeWindowX,shapeWindowY);
				shapeWindow.setVisible(true);//图形组件可见
			}
		});	
		
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {//单击时
				addWatermark();
				DrawImageUtil.saveImage(DrawPictureFrame.this, image);//打印图片
			}
		});
		
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				System.exit(0);//程序关闭
			}
		});
		eraserMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if(eraserMenuItem.getText().equals("橡皮")) {
					rubber=true;
					eraserMenuItem.setText("画图");
					eraserButton.setText("画图");
				}else {
					rubber=false;
					eraserMenuItem.setText("橡皮");
					eraserButton.setText("橡皮");
					g.setColor(foreColor);
				}
			}
		});
		clearMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				g.setColor(backgroundColor);
				g.fillRect(0, 0, 570, 390);
				g.setColor(foreColor);
				canvas.repaint();
			}
		});
		strokeMenuItem1.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//声明画笔的属性，粗细为1像素，线条末端无修饰，折线处呈尖角
				BasicStroke bs=new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);
				strokeButton1.setSelected(true);
			}
		});
		strokeMenuItem2.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//声明画笔的属性，粗细为1像素，线条末端无修饰，折线处呈尖角
				BasicStroke bs=new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);
				strokeButton1.setSelected(true);
			}
		});
		strokeMenuItem3.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//声明画笔的属性，粗细为1像素，线条末端无修饰，折线处呈尖角
				BasicStroke bs=new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);
				strokeButton1.setSelected(true);
			}
		});
		foregroundMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//打开选择颜色对话框，参数为：父窗体、对话框标题、默认选中颜色
				Color fColor=JColorChooser.showDialog(DrawPictureFrame.this,"选择颜色对话框",Color.CYAN);
				if(fColor!=null) {
					foreColor=fColor;
				}
				foregroundButton.setForeground(foreColor);//前景色文字按钮也要更换为这种颜色
				g.setColor(foreColor);
			}
		});
		backgroundMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//打开选择颜色对话框，参数为：父窗体、对话框标题、默认选中颜色
				Color bgColor=JColorChooser.showDialog(DrawPictureFrame.this,"选择颜色对话框",Color.CYAN);
				if(bgColor!=null) {
					backgroundColor=bgColor;
				}
				foregroundButton.setBackground(backgroundColor);//前景色文字按钮也要更换为这种颜色
				g.setColor(backgroundColor);
				g.fillRect(0, 0, 570, 390);
				g.setColor(foreColor);
				canvas.repaint();
			}
		});
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				addWatermark();
				DrawImageUtil.saveImage(DrawPictureFrame.this, image);//打印图片
			}
		});
		shuiyinMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				shuiyin=JOptionPane.showInputDialog(DrawPictureFrame.this,"你想输入什么水印？");
				if(null==shuiyin) {
					shuiyin="";
				}else {
					setTitle("画图程序(水印内容:["+shuiyin+"])");
				}
			}
		});		
	}
	/*
	 * FrameGetShape接口实现类，用于获得图形控件返回的被选中的图形
	 */
	
/**
 * 程序运行主方法
 * args为运行时参数
 */
	private void addWatermark() {
		if(!"".equals(shuiyin.trim())) {//如果水印字段不是空字符串
			g.rotate(Math.toRadians(-30));//将图片旋转-30度
			Font font=new Font("楷体",Font.BOLD,72);
			g.setFont(font);
			g.setColor(Color.GRAY);//使用灰色
			AlphaComposite Alpha=AlphaComposite.SrcOver.derive(0.4f);//设置透明效果
			g.setComposite(Alpha);//使用透明效果
			g.drawString(shuiyin, 150, 500);//绘制文字
			canvas.repaint();
			g.rotate(Math.toRadians(30));//将旋转的图片再旋转回来
			Alpha=AlphaComposite.SrcOver.derive(1f);//不透明效果
			g.setComposite(Alpha);//使用不透明效果
			g.setColor(foreColor);//画笔回复原来颜色
		}
	}
	
	
	public static void main(String[] args) {
		DrawPictureFrame frame = new DrawPictureFrame();
		frame.setVisible(true);// 让窗体可见；
	}
	@Override
	public void getShape(Shapes shape) {
		// TODO Auto-generated method stub
		this.shape=shape;
		drawShape=true;
	}
}

