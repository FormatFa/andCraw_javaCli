package formatfa.craw.javaclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import formatfa.craw.client.AndClient;
import formatfa.craw.client.Connection;
import formatfa.craw.protocol.Request;
import formatfa.craw.protocol.Response;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	
    	new App().run();

       
    }
    
    
    
//    手机画布
    Connection connection;
    ViewPanel vp ;
    private void run() throws IOException {
    	 JFrame frame = new JFrame();
         JMenuBar menuBar = new JMenuBar();
         
         JMenu scale = new JMenu("缩放"); 
         JMenu font = new JMenu("字体");
         JMenuItem scale_add = new JMenuItem("+");
         JMenuItem scale_sub = new JMenuItem("-");
         scale.add(scale_add);
         scale.add(scale_sub);
         
         scale_add.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				vp.setScale(vp.getScale()+0.1f);
				
				
			}});
         scale_sub.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				vp.setScale(vp.getScale()-0.1f);
				
				
				
			}});
         
           
         JMenuItem font_add = new JMenuItem("+");
         JMenuItem font_sub = new JMenuItem("-");
         font.add(font_add);
         font.add(font_sub);
         
         font_add.addActionListener(new ActionListener() {

 			public void actionPerformed(ActionEvent e) {
 				vp.setFontSize(vp.getFontSize()+1);
 				
 			}});
         font_sub.addActionListener(new ActionListener() {

 			public void actionPerformed(ActionEvent e) {
 				// TODO Auto-generated method stub
 				vp.setFontSize(vp.getFontSize()-1);
 				
 				
 			}});
          
         	
         menuBar.add(font);
         menuBar.add(scale);
         
         
         frame.setJMenuBar(menuBar);
         
         
         
         frame.setSize(540	, 960);
//         
     	 connection  = AndClient.connect("192.168.0.123", 2333);
         vp = new ViewPanel(connection);
   
      
         frame.add(vp);
         frame.setVisible(true);
         
		
	}



	static class ViewPanel extends JPanel
    {

		private float scale=0.3f;
		
public float getScale() {
			return scale;
		}

		public void setScale(float scale) {
			this.scale = scale;
		}
		

		//    	循环绘制
    	private void drawRecursize(Graphics g,JSONObject node)
    	{
    		

    		g.setColor(Color.red);
    		int left = (int) (node.getIntValue("left")*scale);
    		int right = (int) (node.getIntValue("right")*scale);
    		int top = (int) (node.getIntValue("top")*scale);
    		int bottom = (int) (node.getIntValue("bottom")*scale);
    		System.out.println(String.format("left=%d,right=%d,top=%d,bottom=%d", left,right,top,bottom));
    		g.drawRect(left,top,right-left,bottom-top);
    		
    		//绘制文字
    		if(node.containsKey("text"))
    		{
    			g.drawString(node.getString("text"), left, top+fontSize);
    		}
    		
    		JSONArray children = node.getJSONArray("children");
    		for(int i=0;i<children.size();i+=1)
    		{
    			drawRecursize(g,children.getJSONObject(i));
    		}
    		
    	}
    	private Connection connection;
    	private Request layout_req;
    	private Font viewFont;
    	private int fontSize = 15;
    	
		public int getFontSize() {
			return fontSize;
		}

		public void setFontSize(int fontSize) {
			this.fontSize = fontSize;
		 viewFont = 	viewFont.deriveFont(fontSize);
		}

		public ViewPanel(Connection connection) {
			super();
			this.connection = connection;
			layout_req = new Request("get",null);
			viewFont = new Font(Font.MONOSPACED,Font.BOLD,fontSize);
		}

		@Override
		public void paint(Graphics g) {
			// TODO Auto-generated method stub
//			super.paint(g);
//			清屏
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(Color.RED);
			g.setFont(viewFont);
			g.drawString("测试", 2, fontSize);
			//获取页面布局
//			JSONObject layouts = 
			try {
				Response response =  connection.request(layout_req);
				JSONObject layouts = JSONObject.parseObject(response.getBody());
				drawRecursize(g,layouts);
				System.out.println("layout:"+layouts);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			
			g.drawRect(0, 0, 100, 300);
//			g.fillRect(0, 0, 200, 200);
			
			
//			刷新
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			repaint();
		}
    
    	
    	
    }
    
    
    
}
