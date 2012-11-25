package com.vjmp.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.vjmp.editor.MapListEditor.MyTableModel.MapInfo;


/**
 * A pálya listát kezelõ ablak létrehozásáért és kezeléséért felelõs függvény.
 * Implementálja az ActionListenert.
 *
 */
public class MapListEditor implements ActionListener{
	private JTextField map_name_textField = new JTextField(10);
	private JTextField map_path_textField = new JTextField(10);
	private JButton button = new JButton("Add");
	private JButton delete_button = new JButton("Delete");
	private File activeFile = null;
	private JTable table = null;
	private MyTableModel tableData = null;
	public MapListEditor(JFrame frame) {
		JDialog dialog = new JDialog(frame,"HEllo");
		dialog.setVisible(true);
		dialog.setMinimumSize(new Dimension(600,250));
		dialog.setLocation(new Point(frame.getLocation().x + 100,frame.getLocation().y + 100));
		dialog.setResizable(false);
		

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
		
		dialog.setJMenuBar(menuBar);
		
		tableData = new MyTableModel();
		
		table = new JTable(tableData);
		
		dialog.add(table);
		
		JPanel panel = new JPanel();
	
		panel.add(new JLabel("MapName: "));
		
		map_name_textField.setName("map_name_textField");
		panel.add(map_name_textField);
		
		panel.add(new JLabel("MapPath: "));
		
		map_path_textField.setName("map_path_textField");
		panel.add(map_path_textField);
		
		
		button.setName("Add");
		button.addActionListener(tableData);
		panel.add(button);
		
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JSeparator(SwingConstants.VERTICAL));
		panel.add(Box.createHorizontalStrut(5));
		
		delete_button.setName("Delete");
		delete_button.addActionListener(tableData);
		panel.add(delete_button);
		
		dialog.add(panel,BorderLayout.SOUTH);
	}
		

	
	/**
	 * A megjelenitett {@link JTable} saját modellje.
	 * Extendeli az {@link AbstractTableModel}-t, implementálja az {@link ActionListener}-t, illetve
	 * az {@link Iterable} interface-t (MapInfo specifikusan).
	 *
	 *
	 */
	public class MyTableModel extends AbstractTableModel implements ActionListener,Iterable<MapInfo>{
			/**
			 * Belsõ osztály amely tárolja egy mapról az információkat.
			 *
			 */
			public class MapInfo {
				public String number = null;
				public String map_path = null;
				public String map_name = null;
				public boolean is_new = true;
				
				/**
				 * Konstruktor
				 * @param number : {@link String} - a map sorszáma listán
				 * @param map_name : {@link String} - a map neve
				 * @param map_path : {@link String} - a map elérési útvonala
				 */
				public MapInfo(String number,String map_name,String map_path) {
					this.number = number;
					this.map_name = map_name;
					this.map_path = map_path;
				}
			}
			private static final long serialVersionUID = 1L;
			public AbstractTableModel atm = null;
			public String[] columNames = {"Number","Map Name","Map Path"};
			public List<MapInfo> datas = null;
			
			/**
			 * Konstruktor
			 * A map list alapján létrehozza a listát, amit késõbb tudunk módositani
			 * 
			 */
			public MyTableModel() {
				
				List<String> array = new ArrayList<String>();
				datas =new ArrayList<MapInfo>();
				try {
					BufferedReader br = new BufferedReader(new FileReader("./res/map_list.txt"));
					String line = br.readLine();
					while(line != null){
						array.add(line);
						line = br.readLine();
					}
					for(int i=0;i<array.size();i++){
						String[] attributes = array.get(i).split("\"");
						MapInfo tmp = new MapInfo(String.valueOf(i),attributes[0],attributes[1]);
					    datas.add(tmp);
					}
					atm = new DefaultTableModel(columNames,datas.size());
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				/**
				 * 
				 * @return column_size : int - az oszlopok száma
				 */
				@Override
				public int getColumnCount() {
					return columNames.length;
				}

				/**
				 * 
				 * @return row_size : int - a sorok száma
				 */
				@Override
				public int getRowCount() {
					return datas.size();
				}

				/**
				 * Az érték lekérdezése
				 * @param row : int - a sor indexe
				 * @param col : int - az oszlop indexe
				 * @return ret : {@link Object} - a sor,oszlop által meghatározott Object
				 */
				@Override
				public Object getValueAt(int row, int col) {
					switch(col) {
					case 0: return datas.get(row).number;
					case 1: return datas.get(row).map_name;
					default: return datas.get(row).map_path;
					
					}
					
				}
				
				/**
				 * 
				 * @param col : int - oszlop index
				 * @return name : String - oszlop neve
				 */
				public String getColumName(int col){
					return columNames[col];
				}
				
				/**
				 * 
				 * @param row : int - sor indexe
				 * @param col : int - oszlop indexe
				 * @return true - ha editálható, false ha nem
				 */
				public boolean isCellEditable(int row,int col){
					return true;
				}
				
				/**
				 * 
				 * @param value : {@link Object} - a beállitandó érték
				 * @param row : int - a sor indexe
				 * @param col : int  - az oszlop indexe
				 */
				 public void setValueAt(Object value, int row, int col) {
					 switch(col) {
						case 0: datas.get(row).number = (String)value;
						break;
						case 1: datas.get(row).map_name = (String) value;
						break;
						case 2:datas.get(row).map_path = (String) value;
						break;
						}
					 
				        fireTableCellUpdated(row, col);
				        orderTable();
				 }
				 
				 /**
				  * sorba rendezi a táblázatot
				  */
				 public void orderTable(){
					 Collections.sort(datas,new Comparator<MapInfo>(){

							@Override
							public int compare(MapInfo arg0, MapInfo arg1) {
								if(Integer.valueOf(arg0.number) == Integer.valueOf(arg1.number)) {
									if(arg0.is_new) {
										arg0.is_new = false;
										return -1;
									} else {
										return 1;
									}
								}
								return Integer.valueOf(arg0.number).compareTo(Integer.valueOf(arg1.number));
							}
							
						});
					 
					 for(int i=0;i<datas.size();i++){
						 datas.get(i).number = String.valueOf(i);
					 }
					 fireTableStructureChanged();
				 }
				 /**
				  * Kezeli a táblázat megváltozását
				  * @param e  : {@link TableModelEvent}
				  */
				 public void tableChanged(TableModelEvent e) {
				        int row = e.getFirstRow();
				        int column = e.getColumn();
				        
				        
				       
				        if(column == e.ALL_COLUMNS){
				        	for(int i=0;i<getColumnCount();i++){
				        		
				        		fireTableCellUpdated(row,i);
				        		
				        	
				        	}
				        } else {
				        	String columnName = getColumnName(column);
				        }
				        orderTable();
				 }
		
			
				 /**
				  * Kezeli a keletkezõ action-öket
				  * @param e : {@link ActionEvent}
				  */
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton)e.getSource();
					if(button.getName().equals("Add")){
						String map_name = map_name_textField.getText();
						String map_path = map_path_textField.getText();
						if(!map_name.equals("") && !map_path.equals("")){
							datas.add(new MapInfo(String.valueOf(datas.size()),map_name,map_path));
							fireTableRowsInserted(datas.size()-1,datas.size()-1);
						}
						
						
					} else if(button.getName().equals("Delete")){
						
						
						int selected_row = table.getSelectedRow();
						if(selected_row != -1) {
							datas.remove(table.getSelectedRow());
							fireTableRowsDeleted(selected_row,selected_row);
						}
					}
					
					orderTable();
				}
				/**
				 * 
				 * @return iterator : {@link Iterator}
				 */
				@Override
				public Iterator<MapInfo> iterator() {
					return datas.iterator();
				}
				
				/**
				 * Betölti a map listát a megadott activeFile-ról
				 * @param activeFile : {@link File} - az active file
				 */
				public void load(File activeFile) {
					List<String> array = new ArrayList<String>();
					datas =new ArrayList<MapInfo>();
					try {
						BufferedReader br = new BufferedReader(new FileReader(activeFile));
						String line = br.readLine();
						while(line != null){
							array.add(line);
							line = br.readLine();
						}
						for(int i=0;i<array.size();i++){
							String[] attributes = array.get(i).split("\"");
							MapInfo tmp = new MapInfo(String.valueOf(i),attributes[0],attributes[1]);
						    datas.add(tmp);
						}
						atm = new DefaultTableModel(columNames,datas.size());
						fireTableDataChanged();
						br.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				/**
				 * Elmenti a map listát a megadott fileba.
				 * @param activeFile : {@link File} - a megadott file
				 */
				public void save(File activeFile) {
					try {
						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(activeFile)));
						for(MapInfo mapInfo : datas){
							pw.println(mapInfo.map_name +"\""+mapInfo.map_path );
						}
						pw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			
		}



	/**
	 * Kezeli a keletkezett action-öket
	 * @param e : {@link ActionEvent}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem menuItem = (JMenuItem)e.getSource();
		if(menuItem.getName().equals("Open")) {
			JFileChooser fc = new JFileChooser("./");
			int returnVal = fc.showOpenDialog(fc);
			activeFile = fc.getSelectedFile();

				if(activeFile != null){
					tableData.load(activeFile);
				}
		} else if(menuItem.getName().equals("Save as")) {
			
				JFileChooser fc = new JFileChooser("./");
				int returnVal = fc.showOpenDialog(fc);
				activeFile = fc.getSelectedFile();
				if(activeFile != null){
					tableData.save(activeFile);
				}
			 
		}  else if(menuItem.getName().equals("Save")){
		
			
				if(activeFile != null) {
					tableData.save(activeFile);
				} else {
					JFileChooser fc = new JFileChooser("./");
					int returnVal = fc.showOpenDialog(fc);
					activeFile = fc.getSelectedFile();
					tableData.save(activeFile);
				}
		}

	}


	

}
		
