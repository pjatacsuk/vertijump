package com.vjmp.editor;

import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ImageIcon> images;
	 public ComboBoxRenderer() {
	        setOpaque(true);
	        setHorizontalAlignment(CENTER);
	        setVerticalAlignment(CENTER);
	        images = new ArrayList<ImageIcon>();
	        for(String path : Editor.textureList){
	        	images.add(createImageIcon(path));
	        	String[] desc= path.split("/");
	        	//images.get(images.size()-1).setDescription(desc[desc.length-1]);
				images.get(images.size()-1).setDescription("");	
			}
	        
	        
	        
	       /* try {
				BufferedReader br = new BufferedReader(new FileReader("./res/textures.txt"));
				String path = br.readLine();
				while(path != null) {
					path = "./res/" +  path;
					images.add(createImageIcon(path));
					
					String[] desc = path.split("/");
					images.get(images.size()-1).setDescription(desc[desc.length-1]);
					path = br.readLine();
					
				}
				br.close();
	        } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	        
	        
	 }
	private ImageIcon createImageIcon(String path) {
			
	        if (path != null) {
	        
	            ImageIcon icon = new ImageIcon(path);
	            Image correct_size = icon.getImage().getScaledInstance(45, 45,0);
	            icon.setImage(correct_size);
	            return icon;
	        } else {
	            System.err.println("Couldn't find file: " + path);
	                return null;
	        }
	}
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		  int selectedIndex = Integer.valueOf((String)value);
		  
          if (isSelected) {
              setBackground(list.getSelectionBackground());
              setForeground(list.getSelectionForeground());
          } else {
              setBackground(list.getBackground());
              setForeground(list.getForeground());
          }

          //Set the icon and text.  If icon was null, say so.
          ImageIcon icon = images.get(selectedIndex);
          String desc = images.get(selectedIndex).getDescription();
          setIcon(icon);
          if (icon != null) {
              setText(desc);
              setFont(list.getFont());
          } else {
              
          }

          return this;
	}

}
