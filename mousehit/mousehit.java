import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import sun.audio.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.applet.AudioClip;
import javax.sound.sampled.Clip;
import java.awt.image.ImageProducer;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

public class mousehit extends JFrame implements ActionListener
{
	private Cursor cusRemondRelease;
	Image RemondImgRelease = new ImageIcon(getClass().getResource("槌子.png")).getImage();
	Image []icon = new Image[4];
	Timer timer;
	Timer show_frequency;
	Timer refresh_timer;
	JButton Start_btn;
	JLabel time_label;
	JLabel score_label;
	double last_time;
	int score;
	int location_x[] = new int[6];
	int location_y[] = new int[6];
	int x, y, height, hole, pic;
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	ImagePanel img[] = new ImagePanel[4];

	public static void main(String[] args) {
		mousehit frame = new mousehit();
	}
	
   class ImagePanel extends JPanel {
      Image image;
      public ImagePanel(Image Img) { this.image = Img; }
      public void paintComponent(Graphics g) {
         super.paintComponent(g);
		 int imgW = image.getWidth(this); // 計算圖片尺寸
         int imgH = image.getHeight(this);
		 ImageProducer imageproducer = image.getSource();
		 CropImageFilter cropimagefilter = new CropImageFilter(0, 0, imgW, height);
		 ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(imageproducer, cropimagefilter)));		 
		 g.drawImage(icon.getImage(), x, y - height + 200, mousehit.this);
      }
   }	

	public mousehit() {
		super("mousehit");
		score_label = new JLabel("score:0");
		score_label.setFont(new Font("Arial", Font.BOLD, 16));
		score_label.setForeground(Color.red);
		score_label.setSize(150, 28);
		cusRemondRelease = getContentPane().getToolkit().createCustomCursor(RemondImgRelease, new Point(16, 16), "Tand");
		setCursor(cusRemondRelease);

		Start_btn = new JButton("");
		Start_btn.setIcon(new ImageIcon(getClass().getResource("start.png")));
		Start_btn.addActionListener(this);
		Start_btn.setBorder(null);
		Start_btn.setContentAreaFilled(false);

		time_label = new JLabel("last:  60.0  seconds");
		time_label.setFont(new Font("Arial", Font.BOLD, 16));
		time_label.setForeground(Color.red);
		time_label.setSize(400, 50);
		
		icon[0] = toolkit.getImage("地鼠1.png");
		icon[1] = toolkit.getImage("地鼠2.png");
		icon[2] = toolkit.getImage("Bomb.png");
		icon[3] = toolkit.getImage("時鐘.png");
		
		timer = new Timer(100, this);
		timer.setInitialDelay(0);
		show_frequency = new Timer(600, this);
		show_frequency.setInitialDelay(0);
		refresh_timer = new Timer(1, this);
		refresh_timer.setInitialDelay(0);
		JPanel list = new JPanel();
		list.setLayout(new GridLayout(1, 3));
		list.add(score_label);
		list.add(Start_btn);
		list.add(time_label);
		list.setOpaque(false);
		for(int i = 0; i < 4; ++i)img[i] = new ImagePanel(icon[i]);
		for(int i = 0; i < 6; ++i){location_x[i] = 120 + (i % 3) * 325; location_y[i] = i > 2 ? 295 : 20;}

		setBg();
		add(list, BorderLayout.NORTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1080, 720);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e){
				try
				{
					// Open an audio input stream.
					URL url1 = this.getClass().getClassLoader().getResource("hit1.wav");
					AudioInputStream audioIn1 = AudioSystem.getAudioInputStream(url1);
					// Get a sound clip resource.
					clip1 = AudioSystem.getClip();
					// Open audio clip and load samples from the audio input stream.
					clip1.open(audioIn1);
					clip1.start();
					//clip.stop();
				}
				catch (UnsupportedAudioFileException aee)
				{
					aee.printStackTrace();
				}
				catch (IOException aee)
				{
					aee.printStackTrace();
				}
				catch (LineUnavailableException aee)
				{
					aee.printStackTrace();
				}
				if(e.getX() <= x + 220 && e.getX() >= x + 20
				&& e.getY() <= y + 320 && e.getY() >= y + 120
				&& !Start_btn.isEnabled()){
					show_frequency.stop();
						switch(pic){
							case 0:
							score += 10;
							break;
							case 1:
							score += 30;
							break;
							case 2:
							try
							{
								// Open an audio input stream.
								URL url11 = this.getClass().getClassLoader().getResource("bomb.wav");
								AudioInputStream audioIn11 = AudioSystem.getAudioInputStream(url11);
								// Get a sound clip resource.
								clip2 = AudioSystem.getClip();
								// Open audio clip and load samples from the audio input stream.
								clip2.open(audioIn11);
								clip2.start();
								//clip.stop();
							}
							catch (UnsupportedAudioFileException aeee)
							{
								aeee.printStackTrace();
							}
							catch (IOException aeee)
							{
								aeee.printStackTrace();
							}
							catch (LineUnavailableException aeee)
							{
								aeee.printStackTrace();
							}
							score -= 50;
							last_time -= 50;
							break;
							case 3:
							last_time+=10;
							score += 5;
							break;
						}
						score_label.setText("score:" + score);
						show_frequency.start();
					}
			}
			public void mouseReleased(MouseEvent e){
				cusRemondRelease = mousehit.this.getToolkit().createCustomCursor(RemondImgRelease, new Point(16, 16), "Tand");
				mousehit.this.setCursor(cusRemondRelease);
			}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
		});
		setVisible(true);
		setResizable(false);
	}

	public void setBg(){
		((JPanel)this.getContentPane()).setOpaque(false);
		ImageIcon img = new ImageIcon("hole.png");
		JLabel background = new JLabel(img);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
	}
	public Clip clip, clip1, clip2, clip3;
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == Start_btn)
		{
			last_time = 600;
			score_label.setText("score:0");
			height = 0;
			score = 0;
			try
			{
				// Open an audio input stream.
				URL url = this.getClass().getClassLoader().getResource("musicy.wav");
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
				// Get a sound clip resource.
				clip = AudioSystem.getClip();
				// Open audio clip and load samples from the audio input stream.
				clip.open(audioIn);
				clip.start();
				clip.loop(2);
				//clip.stop();
			}
			catch (UnsupportedAudioFileException ae)
			{
				ae.printStackTrace();
			}
			catch (IOException ae)
			{
				ae.printStackTrace();
			}
			catch (LineUnavailableException ae)
			{
				ae.printStackTrace();
			}
			timer.start();
			show_frequency.start();
			Start_btn.setEnabled(false);
		}
		if (e.getSource() == timer)
		{
			if (last_time > 0){
				last_time--;
				time_label.setText("last:  " + last_time / 10 + "  seconds");
			}

			else
			{
				clip.stop();
				timer.stop();
				show_frequency.stop();
				refresh_timer.stop();
				Start_btn.setEnabled(true);
				time_label.setText("last:  0  seconds");
				for(int i = 0; i < 4; ++i){remove(img[i]);img[i] = new ImagePanel(icon[i]);}
				repaint();
				try
				{
					// Open an audio input stream.
					URL url111 = this.getClass().getClassLoader().getResource("timeover.wav");
					AudioInputStream audioIn111 = AudioSystem.getAudioInputStream(url111);
					// Get a sound clip resource.
					clip3 = AudioSystem.getClip();
					// Open audio clip and load samples from the audio input stream.
					clip3.open(audioIn111);
					clip3.start();
					//clip.stop();
				}
				catch (UnsupportedAudioFileException aeeee)
				{
					aeeee.printStackTrace();
				}
				catch (IOException aeeee)
				{
					aeeee.printStackTrace();
				}
				catch (LineUnavailableException aeeee)
				{
					aeeee.printStackTrace();
				}
			}
		}
		if(e.getSource() == show_frequency){
			for(int i = 0; i < 4; ++i){remove(img[i]);img[i] = new ImagePanel(icon[i]);}
			if (last_time > 0)
			{
				refresh_timer.stop();
				height = 0;
				hole = (int)(Math.random() * 6);
				pic = (int)(Math.random() * 4);
				x = location_x[hole]; y = location_y[hole];
				add(img[pic]);
				img[pic].setOpaque(false);
				img[pic].repaint();
				refresh_timer.start();
			}
		}
		if (e.getSource() == refresh_timer)
		{
			if (height < 200)
			{
				height++;
				img[pic].repaint();
			}
		}
	}
}