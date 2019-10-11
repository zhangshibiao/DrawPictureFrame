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
import com.mr.util.FrameGetShape;//�������տƼ��Լ���װ�İ��е���
import com.mr.util.ShapeWindow;
import com.mr.util.Shapes;
import com.mr.util.DrawImageUtil;
import java.awt.AlphaComposite;
import java.awt.Font;
import javax.swing.JOptionPane;

public class DrawPictureFrame extends JFrame implements FrameGetShape {
	//����һ��8λBGR��ɫ������ͼ��
	BufferedImage image=new BufferedImage(570,390,BufferedImage.TYPE_INT_BGR);
	Graphics gs=image.getGraphics();//���ͼ��Ļ�ͼ����
	Graphics2D g=(Graphics2D) gs;//����ͼ����ת��ΪGraphics2D���ͣ�
	DrawPictureCanvas canvas=new DrawPictureCanvas();//������������
	Color foreColor=Color.BLACK;//����ǰ��ɫ;0
	Color backgroundColor=Color.WHITE;//���屳��ɫ��
	int x=-1;
	int y=-1;
	boolean rubber=false;//��Ƥ��;
	private JToolBar toolBar;//������;
	private JButton eraserButton;//��Ƥ��ť��
	private JToggleButton strokeButton1;//ϸ�߰�ť��
	private JToggleButton strokeButton2;//���߰�ť��
	private JToggleButton strokeButton3;//�ϴְ�ť
	private JButton backgroundButton;//����ɫ��ť��
	private JButton foregroundButton;//ǰ��ɫ��ť��
	private JButton clearButton;//�����ť��
	private JButton saveButton;//���水ť��
	private JButton shapeButton;//ͼ�ΰ�ť��
	boolean drawShape=false;
	Shapes shape;
	private JMenuItem strokeMenuItem1;//ϸ�߲˵�
	private JMenuItem strokeMenuItem2;//���߲˵�
	private JMenuItem strokeMenuItem3;//�ϴֲ˵�
	private JMenuItem clearMenuItem;//ϸ�߲˵�
	private JMenuItem foregroundMenuItem;//ϸ�߲˵�
	private JMenuItem backgroundMenuItem;//ϸ�߲˵�
	private JMenuItem eraserMenuItem;//ϸ�߲˵�
	private JMenuItem exitMenuItem;//ϸ�߲˵�
	private JMenuItem saveMenuItem;//ϸ�߲˵�
	private JMenuItem shuiyinMenuItem;//ˮӡ�˵�
	private String shuiyin="";
	
	public DrawPictureFrame() {
		setResizable(false); // ���岻�ܸı��С
		setTitle("��ͼ����(ˮӡ����:["+shuiyin+"])");//���ñ��⣬���ˮӡ������ʾ
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// ����ر���ֹͣ����
		setBounds(500, 100, 574, 460);// ���ô���λ�úͿ�ߣ�
		init();//�����ʼ��;
		addListener();
	}
	/*
	 * �����ʼ��
	 */
	private void init() {
		g.setColor(backgroundColor);//�ñ���ɫ���û�ͼ�������ɫ��
		g.fillRect(0,0,570,390);//�ñ���ɫ�������������
		g.setColor(foreColor);//��ǰ��ɫ���û�ͼ�������ɫ��
		canvas.setImage(image);//���û�����ͼ��
		getContentPane().add(canvas);//��������ӵ���������Ĭ�ϲ��ֵ��в�λ�ã�
		toolBar=new JToolBar();//��ʼ����������
		getContentPane().add(toolBar,BorderLayout.NORTH);//��������ӵ��������λ�ã�
		saveButton=new JButton("����");
		toolBar.add(saveButton);//��������Ӱ�ť;
		toolBar.addSeparator();//��ӷָ�����
		//��ʼ����ť���󣬲�����ı�����
		strokeButton1=new JToggleButton("ϸ��");
		strokeButton1.setSelected(true);//ϸ�߰�ť���ڱ�ѡ��״̬
		toolBar.add(strokeButton1);
		strokeButton2=new JToggleButton("����");
		toolBar.add(strokeButton2);
		strokeButton3=new JToggleButton("�ϴ�");
		toolBar.add(strokeButton3);
		//���ʴ�ϸ��ť�飬��ֻ֤��һ����ť��ѡ��
		ButtonGroup strokeGroup=new ButtonGroup();
		strokeGroup.add(strokeButton1);
		strokeGroup.add(strokeButton2);
		strokeGroup.add(strokeButton3);
		toolBar.addSeparator();
		backgroundButton=new JButton("������ɫ");
		toolBar.add(backgroundButton);
		foregroundButton=new JButton("ǰ����ɫ");
		toolBar.add(foregroundButton);
		toolBar.addSeparator();
		shapeButton=new JButton("ͼ��");
		toolBar.add(shapeButton);
		clearButton=new JButton("����");
		toolBar.add(clearButton);
		eraserButton=new JButton("��Ƥ");
		toolBar.add(eraserButton);
		JMenuBar menuBar=new JMenuBar();//�����˵���
		setJMenuBar(menuBar);//��������˵���
		JMenu systemMenu=new JMenu("ϵͳ");//��ʼ���˵����󣬲�����ı�����
		menuBar.add(systemMenu);//�˵�����Ӳ˵�����
		shuiyinMenuItem=new JMenuItem("����ˮӡ");//��ʼ���˵����󣬲�����ı�����
		systemMenu.add(shuiyinMenuItem);//�˵���Ӳ˵���
		saveMenuItem=new JMenuItem("����");//��ʼ���˵�����󣬲�����ı�����
		systemMenu.add(saveMenuItem);//�˵���Ӳ˵���
		systemMenu.addSeparator();//��ӷָ���
		exitMenuItem=new JMenuItem("�˳�");//��ʼ���˵�����󣬲�����ı�����
		systemMenu.add(exitMenuItem);//�˵���Ӳ˵���
		JMenu strokeMenu=new JMenu("����");
		menuBar.add(strokeMenu);
		strokeMenuItem1=new JMenuItem("ϸ��");
		strokeMenu.add(strokeMenuItem1);
		strokeMenuItem2=new JMenuItem("����");
		strokeMenu.add(strokeMenuItem2);
		strokeMenuItem3=new JMenuItem("�ϴ�");
		strokeMenu.add(strokeMenuItem3);
		
		JMenu colorMenu=new JMenu("��ɫ");
		menuBar.add(colorMenu);
		foregroundMenuItem=new JMenuItem("ǰ����ɫ");
		colorMenu.add(foregroundMenuItem);
		backgroundMenuItem=new JMenuItem("������ɫ");
		colorMenu.add(backgroundMenuItem);
		
		JMenu editMenu=new JMenu("�༭");
		menuBar.add(editMenu);
		clearMenuItem=new JMenuItem("���");
		editMenu.add(clearMenuItem);
		eraserMenuItem=new JMenuItem("���");
		editMenu.add(eraserMenuItem);
		
		}
	/*
	 * Ϊ�����Ӷ�������
	 */
	public void addListener() {
		//�����������ƶ��¼�����
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(final MouseEvent e) {  //�������ҷʱ��
				if(x>0&&y>0) {                              //���x��y��������¼��
					if(rubber) {                            //�����Ƥ��ʶΪtrue����ʾʹ����Ƥ��
						g.setColor(backgroundColor);        //��ͼ����ʹ�ñ���ɫ��
						g.fillRect(x, y, 10, 10);           //����껬����λ�û�����������
					}else {                                 //�����Ƥ��ʶΪfalse����ʾ���ʻ�ͼ
						g.drawLine(x, y, e.getX(), e.getY());//����껬����λ�û�ֱ��
					}
				}
				x=e.getX();//��һ�������Ƶ�ĺ����ꣻ
				y=e.getY();//��һ�������Ƶ�������ꣻ
				canvas.repaint();//���»���
			}
		});
		canvas.addMouseListener(new MouseAdapter() {//���������굥���¼�����
			public void mouseReleased(final MouseEvent arg0) {//������̧��ʱ
				x=-1;                                         //����¼��һ�������Ƶ�ĺ�����ָ���-1
				y=-1;                                         //����¼��һ�������Ƶ��������ָ���-1
			}
			public void mousePressed(MouseEvent e){//���������ʱ
				if(drawShape) {//�����ʱ��껭����ͼ��
					switch (shape.getType()) {//�ж�ͼ�ε�����
					case Shapes.YUAN://�����Բ��
						//�������꣬����괦��ͼ�ε�����λ��
						int yuanX=e.getX()-shape.getWidth()/2;
						int yuanY=e.getY()-shape.getHeigth()/2;
						//����Բ��ͼ�Σ���ָ������Ϳ��
						Ellipse2D yuan=new Ellipse2D.Double(yuanX,yuanY,shape.getWidth(),shape.getHeigth());
						g.draw(yuan);//��ͼ���߻���Բ��
						break;//����switch���
					case Shapes.FANG://����Ƿ���
						//�������꣬����괦��ͼ�ε�����λ��
						int fangX=e.getX()-shape.getWidth()/2;
						int fangY=e.getY()-shape.getHeigth()/2;
						//��������ͼ�񣬲�ָ������Ϳ��
						Rectangle2D fang=new Rectangle2D.Double(fangX, fangY, shape.getWidth(), shape.getHeigth());
						g.draw(fang);//���Ʒ���
						break;
					}
					canvas.repaint();//���»���
					//��ͼ�α�ʶ����Ϊfalse��˵��������껭����ͼ�Σ�����������
					drawShape=false;
				}
			}
		});
		
	
		strokeButton1.addActionListener(new ActionListener() {//ϸ�߰�ť��Ӷ�������
			public void actionPerformed(final ActionEvent arg0) {//����ʱ
				//�������ʵ����ԣ���ϸΪ1���أ�����ĩ�������Σ����ߴ��ʼ��
				BasicStroke bs=new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);//��ͼ����ʹ�ô˻���
			}
		});
		strokeButton2.addActionListener(new ActionListener() {//ϸ�߰�ť��Ӷ�������
			public void actionPerformed(final ActionEvent arg0) {//����ʱ
				//�������ʵ����ԣ���ϸΪ1���أ�����ĩ�������Σ����ߴ��ʼ��
				BasicStroke bs=new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);//��ͼ����ʹ�ô˻���
			}
		});
		strokeButton3.addActionListener(new ActionListener() {//ϸ�߰�ť��Ӷ�������
			public void actionPerformed(final ActionEvent arg0) {//����ʱ
				//�������ʵ����ԣ���ϸΪ1���أ�����ĩ�������Σ����ߴ��ʼ��
				BasicStroke bs=new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);//��ͼ����ʹ�ô˻���
			}
		});
		backgroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				//����ɫ�Ի��򣬲�������Ϊ�������塢���⡢Ĭ��ѡ�е���ɫ
				Color bgColor=JColorChooser.showDialog(DrawPictureFrame.this, "ѡ����ɫ�Ի���",Color.CYAN);
				if (bgColor!=null) {
					backgroundColor=bgColor;//���ѡ�е���ɫ���ǿյģ���ѡ�е���ɫ��������ɫ����
				}
				//����ɫ��ťҲ����Ϊ���ֱ�����ɫ
				backgroundButton.setBackground(backgroundColor);
				g.setColor(backgroundColor);//��ͼ����Ҳʹ�ñ���ɫ
				g.fillRect(0, 0, 570, 390);//��һ������ɫ�ķ���������������
				g.setColor(foreColor);//��ͼ����ʹ��ǰ��ɫ
				canvas.repaint();//���»���
			}
		});
		
		foregroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				//����ɫ�Ի��򣬲�������Ϊ�������塢���⡢Ĭ��ѡ�е���ɫ
				Color fColor=JColorChooser.showDialog(DrawPictureFrame.this, "ѡ����ɫ�Ի���",Color.CYAN);
				if (fColor!=null) {
					foreColor=fColor;//���ѡ�е���ɫ���ǿյģ���ѡ�е���ɫ��������ɫ����
				}
				//����ɫ��ťҲ����Ϊ���ֱ���
				foregroundButton.setForeground(foreColor);
				g.setColor(foreColor);//��ͼ����Ҳʹ�ñ���ɫ
			}
		});
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				g.setColor(backgroundColor);
				g.fillRect(0, 0, 570, 390);//��һ��������ɫ�ķ����������������
				g.setColor(foreColor);
				canvas.repaint();
			}
		});
		eraserButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {//�����������ϵ���Ƥ����ʹ����Ƥ
				if(eraserButton.getText().equals("��Ƥ")) {
					rubber=true;
					eraserButton.setText("��ͼ");//�ı䰴ť����ʾ���ı�Ϊ��ͼ
				}else {
					rubber=false;
					eraserButton.setText("��Ƥ");
					g.setColor(foreColor);
				}
			}
		});
		
		shapeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//����ʱ
				//����ͼ��ѡ�����
				ShapeWindow shapeWindow=new ShapeWindow(DrawPictureFrame.this);
				int shapeButtonWidth=shapeButton.getWidth();//��ȡͼ��ť���
				int shapeWindowWidth=shapeWindow.getWidth();//��ȡͼ�ΰ�ť���
				int shapeButtonX=shapeButton.getX();//��ȡͼ�ΰ�ť������
				int shapeButtonY=shapeButton.getY();//��ȡͼ�ΰ�ť������
				//����ͼ����������꣬�������ͼ�ΰ�ť���ж���
				int shapeWindowX=getX()+shapeButtonX-(shapeWindowWidth-shapeButtonWidth)/2;
				//����ͼ����������꣬������ʾ��ͼ�ΰ�ť�·�
				int shapeWindowY=getY()+shapeButtonY+80;
				//����ͼ���������λ��
				shapeWindow.setLocation(shapeWindowX,shapeWindowY);
				shapeWindow.setVisible(true);//ͼ������ɼ�
			}
		});	
		
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {//����ʱ
				addWatermark();
				DrawImageUtil.saveImage(DrawPictureFrame.this, image);//��ӡͼƬ
			}
		});
		
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				System.exit(0);//����ر�
			}
		});
		eraserMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if(eraserMenuItem.getText().equals("��Ƥ")) {
					rubber=true;
					eraserMenuItem.setText("��ͼ");
					eraserButton.setText("��ͼ");
				}else {
					rubber=false;
					eraserMenuItem.setText("��Ƥ");
					eraserButton.setText("��Ƥ");
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
				//�������ʵ����ԣ���ϸΪ1���أ�����ĩ�������Σ����ߴ��ʼ��
				BasicStroke bs=new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);
				strokeButton1.setSelected(true);
			}
		});
		strokeMenuItem2.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//�������ʵ����ԣ���ϸΪ1���أ�����ĩ�������Σ����ߴ��ʼ��
				BasicStroke bs=new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);
				strokeButton1.setSelected(true);
			}
		});
		strokeMenuItem3.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//�������ʵ����ԣ���ϸΪ1���أ�����ĩ�������Σ����ߴ��ʼ��
				BasicStroke bs=new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				g.setStroke(bs);
				strokeButton1.setSelected(true);
			}
		});
		foregroundMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//��ѡ����ɫ�Ի��򣬲���Ϊ�������塢�Ի�����⡢Ĭ��ѡ����ɫ
				Color fColor=JColorChooser.showDialog(DrawPictureFrame.this,"ѡ����ɫ�Ի���",Color.CYAN);
				if(fColor!=null) {
					foreColor=fColor;
				}
				foregroundButton.setForeground(foreColor);//ǰ��ɫ���ְ�ťҲҪ����Ϊ������ɫ
				g.setColor(foreColor);
			}
		});
		backgroundMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//��ѡ����ɫ�Ի��򣬲���Ϊ�������塢�Ի�����⡢Ĭ��ѡ����ɫ
				Color bgColor=JColorChooser.showDialog(DrawPictureFrame.this,"ѡ����ɫ�Ի���",Color.CYAN);
				if(bgColor!=null) {
					backgroundColor=bgColor;
				}
				foregroundButton.setBackground(backgroundColor);//ǰ��ɫ���ְ�ťҲҪ����Ϊ������ɫ
				g.setColor(backgroundColor);
				g.fillRect(0, 0, 570, 390);
				g.setColor(foreColor);
				canvas.repaint();
			}
		});
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				addWatermark();
				DrawImageUtil.saveImage(DrawPictureFrame.this, image);//��ӡͼƬ
			}
		});
		shuiyinMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				shuiyin=JOptionPane.showInputDialog(DrawPictureFrame.this,"��������ʲôˮӡ��");
				if(null==shuiyin) {
					shuiyin="";
				}else {
					setTitle("��ͼ����(ˮӡ����:["+shuiyin+"])");
				}
			}
		});		
	}
	/*
	 * FrameGetShape�ӿ�ʵ���࣬���ڻ��ͼ�οؼ����صı�ѡ�е�ͼ��
	 */
	
/**
 * ��������������
 * argsΪ����ʱ����
 */
	private void addWatermark() {
		if(!"".equals(shuiyin.trim())) {//���ˮӡ�ֶβ��ǿ��ַ���
			g.rotate(Math.toRadians(-30));//��ͼƬ��ת-30��
			Font font=new Font("����",Font.BOLD,72);
			g.setFont(font);
			g.setColor(Color.GRAY);//ʹ�û�ɫ
			AlphaComposite Alpha=AlphaComposite.SrcOver.derive(0.4f);//����͸��Ч��
			g.setComposite(Alpha);//ʹ��͸��Ч��
			g.drawString(shuiyin, 150, 500);//��������
			canvas.repaint();
			g.rotate(Math.toRadians(30));//����ת��ͼƬ����ת����
			Alpha=AlphaComposite.SrcOver.derive(1f);//��͸��Ч��
			g.setComposite(Alpha);//ʹ�ò�͸��Ч��
			g.setColor(foreColor);//���ʻظ�ԭ����ɫ
		}
	}
	
	
	public static void main(String[] args) {
		DrawPictureFrame frame = new DrawPictureFrame();
		frame.setVisible(true);// �ô���ɼ���
	}
	@Override
	public void getShape(Shapes shape) {
		// TODO Auto-generated method stub
		this.shape=shape;
		drawShape=true;
	}
}

