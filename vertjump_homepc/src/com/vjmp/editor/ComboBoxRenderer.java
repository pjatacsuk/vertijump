package com.vjmp.editor;

import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Az editorban felhasznált comboBoxhoz a texturákat megjelenitõ custom renderer.
 * @author User
 *
 */
public class ComboBoxRenderer extends JLabel implements ListCellRenderer {
	 
	
	private static final long serialVersionUID = 1L;
	private List<ImageIcon> images;
	
	
	/**
	 * Konstruktor
	 */
	 public ComboBoxRenderer() {
	        setOpaque(true);
	        setHorizontalAlignment(CENTER);
	        setVerticalAlignment(CENTER);
	        images = new ArrayList<ImageIcon>();
	        //Minden textúrához tartozik egy kis kép
	        for(String path : Editor.textureList){
	        	images.add(createImageIcon(path));
				images.get(images.size()-1).setDescription("");	
			}
	        
	 }
	 
	 /**
	  * Létrehozza a megfelelõ icon-t.
	  * @param path : {@link String} - a texture elérési útja
	  * @return icon - {@link ImageIcon} - a kapott méretezett (45*45)-ös Icon
	  */
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
	
	/**
	 * A felülirt cell renderer függvény.
	 * 
	 */
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

          //a standard render függvény átirtuk kép rendelelésre
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
