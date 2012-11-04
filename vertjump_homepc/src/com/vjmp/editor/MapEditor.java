package com.vjmp.editor;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.vjmp.InputHandler;
import com.vjmp.gfx.Camera;
import com.vjmp.managers.GuiManager;

public class MapEditor extends Canvas implements Runnable,ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 180;
	public static final int HEIGHT = (WIDTH)*4/3;
	public static final int SCALE = 3;
	public static final String NAME = "VertiJump - The Editor";
	public static 		boolean running = true;
	
	private static BufferedImage bg;
	
	private static InputHandler	inputHandler;
	
	private static Camera		camera;
	private static Editor		editor;
	private File activeFile = null;
	
	
	
	
	private JFrame frame;
	
	public MapEditor() {
		setMinimumSize(new Dimension(WIDTH * (SCALE),HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH * (SCALE),HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH * (SCALE),HEIGHT*SCALE));
	
		frame = new JFrame(NAME);
	
		init();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
	
		
		//list.setBounds(WIDTH-100, HEIGHT-50, 100, 50);
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1.0;
		c.gridheight = 8;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.VERTICAL;
		frame.add(this,c);
	
		c.gridx=1;
		c.gridy=0;
		c.gridheight = 1;
		c.weighty = 0.0;
		
		frame.getContentPane().add(editor.GenerateTextureListComponent(),c);
		
		c.gridx=1;
		c.gridy=1;
		c.gridheight = 1;
		c.weighty = 0.0;
		
		
		frame.add(editor.GenerateEntityTypeComponent(),c);
		
		
		
		c.gridy = 2;
		frame.getContentPane().add(editor.GenerateTriggerListComponent(),c);
		
		c.gridx=1;
		c.gridy=3;
		c.gridheight=1;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		
		JTextField t = new JTextField(10);
		t.setName("MessageBoxField");
		t.addActionListener(editor);
		
		frame.getContentPane().add(t,c);
		
		
		JCheckBox c1 = new JCheckBox("North");
		c1.setName("North");
		JCheckBox c2 = new JCheckBox("West");
		c2.setName("West");
		JCheckBox c3 = new JCheckBox("South");
		c3.setName("South");
		JCheckBox c4 = new JCheckBox("East");
		c4.setName("East");
		frame.getContentPane().add(c1,c);
		c.gridy=4;
		frame.getContentPane().add(c2,c);
		c.gridy=5;
		frame.getContentPane().add(c3,c);
		c.gridy=6;
		frame.getContentPane().add(c4,c);
		
		
		editor.setComponentManager(new GuiManager(frame));
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File"); 
		menuBar.add(menu);
		
		JMenuItem open = new JMenuItem("Open");
		open.setName("Open");
		open.addActionListener(this);
		menu.add(open);
		
		
		JMenuItem save = new JMenuItem("Save");
		save.setName("Save"); 
		save.addActionListener(this);
		menu.add(save); 
		
		
		
		frame.setJMenuBar(menuBar);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		int x = dim.width - frame.getWidth() - 70;
		int y = (dim.height - HEIGHT*SCALE)/2;
		frame.setLocation(x, y);
		
	
	}
	
	public synchronized void start() {
		running = true;
		init();
		
		new Thread(this).start();
	}
	
	public void init() {
		
	
		inputHandler  = new InputHandler(this);
	
		camera		  = new Camera(WIDTH * SCALE,HEIGHT * SCALE);
		try {
			activeFile = new File("./res/map.txt");
			load(activeFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		load_resource();
		
	
	
	}
	public void load_resource() {
		bg = null;
		try {
			bg = ImageIO.read(new File("./res/background.png"));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
	
	}
	
	public void tick() {
		camera.update(inputHandler);
		editor.update(camera);
		handleOwnInput();
	
	}
	
	private void handleOwnInput() {
		try {
			if(inputHandler.O.isPressedAndReleased()) {
				save(activeFile);
			} else if(inputHandler.P.isPressedAndReleased()) {
				load(activeFile);
				
			}
			if(inputHandler.ESC.isPressed()) {
				running = false;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	void save(String path) throws FileNotFoundException, IOException {
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
		 oos.writeObject(editor);
		 oos.close();
		 JOptionPane.showMessageDialog(frame,"Saved map to \"./res/" + path +"\"");
	 }
	void save(File file) throws FileNotFoundException, IOException {
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		 oos.writeObject(editor);
		 oos.close();
		 JOptionPane.showMessageDialog(frame,"Saved map to \"./res/" + file.getName() + "\"");
	 }
	 void load(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		 editor = (Editor)ois.readObject();
		 ois.close();
		 editor.setInputHandler(inputHandler);
		 editor.setComponentManager(new GuiManager(frame));
		 JOptionPane.showMessageDialog(frame,"Loaded map from \"./res/" + path +"\"");
		
	 }
	 void load(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		 editor = (Editor)ois.readObject();
		 ois.close();
		 editor.setInputHandler(inputHandler);
		 editor.setComponentManager(new GuiManager(frame));
		 JOptionPane.showMessageDialog(frame,"Loaded map from \"./res/" + file.getName() + "\"");
	 }
	


	public void render() {
		BufferStrategy strat = this.getBufferStrategy();
		if(strat == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics2D g = (Graphics2D) strat.getDrawGraphics();
		AffineTransform old = g.getTransform();
		
		g.fillRect(0, 0, getWidth(), getHeight());
		DrawBackGround(g);
		g.translate(camera.pos_x,camera.pos_y);
		
		//g.translate(0,0);
		g.setTransform(camera.GetTransform(g));
	
		editor.draw(g);
		g.setTransform(camera.GetOldTransform());
		g.dispose();
		
		strat.show();
	}
	
	private void DrawBackGround(Graphics g) {
		double width_draw_count = (double)getWidth() / (double)bg.getWidth();
		double height_draw_count = (double)getHeight() / (double)bg.getHeight();
		for(int i=0;i<width_draw_count+1;i++) 
			for(int j=0;j<height_draw_count+1;j++) {
				g.drawImage(bg, i*bg.getWidth(), j*bg.getHeight(),null);
			}
		
	}
	
	
	
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000.0 / 60.0;
		
		int ticks = 0;
		int frames = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender  = false;
			while(delta > 0) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
		
			if(shouldRender) {
				frames++;
				render();
			}
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		/*	if(System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println(frames + " " + ticks);
				frames = 0;
				ticks = 0;
			}*/
			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JMenuItem menuItem = (JMenuItem)arg0.getSource();
		if(menuItem.getName().equals("Open")) {
			JFileChooser fc = new JFileChooser("./");
			int returnVal = fc.showOpenDialog(fc);
			activeFile = fc.getSelectedFile();
			try {
				load(activeFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} else if(menuItem.getName().equals("Save")) {
			try {
				JFileChooser fc = new JFileChooser("./");
				int returnVal = fc.showOpenDialog(fc);
				activeFile = fc.getSelectedFile();
				save(activeFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	
	
}
