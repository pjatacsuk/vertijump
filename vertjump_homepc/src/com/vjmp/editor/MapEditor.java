package com.vjmp.editor;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.vjmp.InputHandler;
import com.vjmp.gfx.Camera;
import com.vjmp.managers.GuiManager;

/**
 * Az map szerkesztõ fõosztálya.
 * Felelõs a futtatásért, illetve a Swing gui megalkotásáért.
 * Extendeli a {@link Canvas}-t, implementálja a {@link Runnable}, {@link ActionListener} interfaceket.
 * 
 *
 */
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
	
	/**
	 * Konstruktor
	 * Megalkotja frame-t, illetve a swing gui-t.
	 */
	public MapEditor() {
		setMinimumSize(new Dimension(WIDTH * (SCALE),HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH * (SCALE),HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH * (SCALE),HEIGHT*SCALE));
	
		frame = new JFrame(NAME);
	
		init();
		editor = new Editor(inputHandler,WIDTH*SCALE,HEIGHT*SCALE);
		editor.setComponentManager(new GuiManager(frame));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
	
		
		//CANVAS
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1.0;
		c.gridheight = 8;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.VERTICAL;
		frame.add(this,c);
	
		//TEXTURE LISTA
		c.gridx=1;
		c.gridy=0;
		c.gridheight = 1;
		c.weighty = 0.0;
		
		frame.getContentPane().add(editor.GenerateTextureListComponent(),c);
		
		//ENTITÁS LISTA
		c.gridx=1;
		c.gridy=1;
		c.gridheight = 1;
		c.weighty = 0.0;
		
		frame.add(editor.GenerateEntityTypeComponent(),c);
		
		
		//TRIGGER LISTA
		c.gridy = 2;
		frame.getContentPane().add(editor.GenerateTriggerListComponent(),c);
		
		//MESSAGEBOX FIELD
		c.gridx=1;
		c.gridy=3;
		c.gridheight=1;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JTextField t = new JTextField(10);
		t.setName("MessageBoxField");
		t.addActionListener(editor);
		
		frame.getContentPane().add(t,c);
		
		
		//CHECKBOX-ok
		JCheckBox c1 = new JCheckBox("North",true);
		c1.setName("North");
		
		JCheckBox c2 = new JCheckBox("West",true);
		c2.setName("West");
		
		JCheckBox c3 = new JCheckBox("South",true);
		c3.setName("South");
		
		JCheckBox c4 = new JCheckBox("East",true);
		c4.setName("East");
		
		frame.getContentPane().add(c1,c);
		c.gridy=4;
		frame.getContentPane().add(c2,c);
		c.gridy=5;
		frame.getContentPane().add(c3,c);
		c.gridy=6;
		frame.getContentPane().add(c4,c);
		
		//A guiManager kezeli a component-eket
		editor.setComponentManager(new GuiManager(frame));
		
		//MENU
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File"); 
		menuBar.add(menu);
		
		JMenuItem open = new JMenuItem("Open");
		open.setName("Open");
		open.addActionListener(this);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		menu.add(open);
		
		
		JMenuItem save = new JMenuItem("Save");
		save.setName("Save"); 
		save.addActionListener(this);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		menu.add(save);
		
		JMenuItem save_as = new JMenuItem("Save as");
		save_as.setName("Save as"); 
		save_as.addActionListener(this);
		save_as.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK|ActionEvent.ALT_MASK));
		menu.add(save_as); 
		
		JMenu edit_menu = new JMenu("Edit");
		menuBar.add(edit_menu);
		
		JMenuItem edit_maplist = new JMenuItem("Edit MapList");
		edit_maplist.setName("Edit MapList");
		edit_maplist.addActionListener(this);
		edit_maplist.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.CTRL_MASK));
		edit_menu.add(edit_maplist);
		
		
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
	
	/**
	 * Elinditja a mapeditor szálat.
	 */
	public synchronized void start() {
		running = true;
		init();
		
		new Thread(this).start();
	}
	
	/**
	 * Inicializálja a változókat
	 */
	public void init() {
		
	
		inputHandler  = new InputHandler(this);
	
		camera		  = new Camera(WIDTH * SCALE,HEIGHT * SCALE);
		
		load_resource();
		
	
	
	}
	
	/**
	 * Betölti az  erõforrásokat.
	 */
	public void load_resource() {
		bg = null;
		try {
			bg = ImageIO.read(new File("./res/background_editor.png"));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
	
	}
	
	/**
	 * Updateli a {@link Camera}-t, illetve az {@link Editor}-t. Kezeli az inputokat.
	 */
	public void tick() {
		camera.update(inputHandler);
		editor.update(camera);
		handleOwnInput();
	
	}
	
	/**
	 * Az inputokat kezeli.
	 */
	private void handleOwnInput() {
		try {
			if(inputHandler.O.isPressedAndReleased()) {
				//O-val mentünk
				if(activeFile == null) activeFile = new File("./res/tmp_name.txt");
				save(activeFile);
			} else if(inputHandler.P.isPressedAndReleased()) {
				//P-vel töltünk
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

	/**
	 * Elmenti a pályát a megadott útvonalra.
	 * @param path : {@link String} - megadott útvonal
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	void save(String path) throws FileNotFoundException, IOException {
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
		 oos.writeObject(editor);
		 oos.close();
		 JOptionPane.showMessageDialog(frame,"Saved map to \"./res/" + path +"\"");
	 }
	
	/**
	 * Elmenti a pályát a megadott filera.
	 * @param file : {@link File} - a megadott file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	void save(File file) throws FileNotFoundException, IOException {
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		 oos.writeObject(editor);
		 oos.close();
		 JOptionPane.showMessageDialog(frame,"Saved map to \"./res/" + file.getName() + "\"");
	 }
	
	/**
	 * Betölti a pályát a megadott útvonalról
	 * @param path : {@link String} - megadott útvonal
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	 void load(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		 editor = (Editor)ois.readObject();
		 ois.close();
		 editor.setInputHandler(inputHandler);
		 editor.setComponentManager(new GuiManager(frame));
		 JOptionPane.showMessageDialog(frame,"Loaded map from \"./res/" + path +"\"");
		 frame.setTitle(NAME + " - " + path);
		
	 }
	 
	 /**
	  * Betölti a pályát a megadott fileból
	  * @param file : {@link File} - a megadott file
	  * @throws FileNotFoundException
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 void load(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		 editor = (Editor)ois.readObject();
		 ois.close();
		 editor.setInputHandler(inputHandler);
		 editor.setComponentManager(new GuiManager(frame));
		 JOptionPane.showMessageDialog(frame,"Loaded map from \"./res/" + file.getName() + "\"");
		 frame.setTitle(NAME + " - " + file.getName());
	 }
	

	 /**
	  * A renderelést végzõ függvény. 
	  */
	public void render() {
		BufferStrategy strat = this.getBufferStrategy();
		if(strat == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics2D g = (Graphics2D) strat.getDrawGraphics();
		//kitöltjük a hátteret,kirajzoljuk a háttérképet
		g.fillRect(0, 0, getWidth(), getHeight());
		DrawBackGround(g);
	
		//transzformálunk
		g.setTransform(camera.GetTransform(g));
	
		editor.draw(g);
	
		g.setTransform(camera.GetOldTransform());
		g.dispose();
		
		strat.show();
	}
	
	/**
	 * Kirajzolja a hátteret.
	 * @param g : {@link Graphics}
	 */
	private void DrawBackGround(Graphics g) {
		double width_draw_count = (double)getWidth() / (double)bg.getWidth();
		double height_draw_count = (double)getHeight() / (double)bg.getHeight();
		for(int i=0;i<width_draw_count+1;i++) 
			for(int j=0;j<height_draw_count+1;j++) {
				g.drawImage(bg, i*bg.getWidth(), j*bg.getHeight(),null);
			}
		
	}
	
	
	
	/**
	 * A felülirt run() függvény. A fix 60fps framerate-ért felelõs.
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000.0 / 60.0;
		
		
		int ticks = 0;
		int frames = 0;
		
		//FPS számitáshoz kell
		//long lastTimer = System.currentTimeMillis();
		
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

	/**
	 * Az override-olt actionPerformed függvény. A gui kezelését végzi.
	 * @param arg0 : {@link ActionEvent}
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JMenuItem menuItem = (JMenuItem)arg0.getSource();
		if(menuItem.getName().equals("Open")) {
			//megnyitunk egy file választót
			JFileChooser fc = new JFileChooser("./");
			fc.showOpenDialog(fc);
			activeFile = fc.getSelectedFile();
			try {
				if(activeFile != null){
				load(activeFile);
				}
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
		
		} else if(menuItem.getName().equals("Save as")) {
			//beragada's ellen
			inputHandler.S.setPressed(false);
			try {
				//megnyitunk egy file választót
				JFileChooser fc = new JFileChooser("./");
				fc.showOpenDialog(fc);
				activeFile = fc.getSelectedFile();
				if(activeFile != null){
					save(activeFile);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(menuItem.getName().equals("Edit MapList")) {
			MapListEditor maplistEditor = new MapListEditor(frame);
		} else if(menuItem.getName().equals("Save")){
			//beragada's ellen
			inputHandler.S.setPressed(false);
			try {
				if(activeFile != null) {
					save(activeFile);
				} else {
					//megnyitunk egy file választót
					JFileChooser fc = new JFileChooser("./");
					fc.showOpenDialog(fc);
					activeFile = fc.getSelectedFile();
					if(activeFile != null) {
					save(activeFile);
					}
				}
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
