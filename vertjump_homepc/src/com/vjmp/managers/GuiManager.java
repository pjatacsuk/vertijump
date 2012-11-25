package com.vjmp.managers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import com.vjmp.editor.ComboBoxRenderer;
import com.vjmp.editor.Editor;
import com.vjmp.editor.MapEditor;
import com.vjmp.editor.TextureList;
import com.vjmp.entities.Entity;
import com.vjmp.entities.Entity.EntityType;
import com.vjmp.entities.drawable.TriggerEntity.TriggerType;
import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.Sprite.Dir;

/**
 * A {@link MapEditor} és {@link Editor}  által megalkotott Swing GUI elemeket manageli. 
 * @author User
 *
 */
public class GuiManager {
	public static enum GuiStates {NONE,MESSAGEBOX,TRIGGER,BLOCK,SPIKE, FINISH_LINE};
	
	private	Component[] components = null;
	private	JFrame frame = null;
	private static GuiStates guiStates = GuiStates.NONE;
	private static boolean guiStatesChanged = true;
	
	
	/**
	 * Konstruktor
	 * @param frame : {@link JFrame} - a megadott ablak
	 */
	public GuiManager(JFrame frame) {
		this.frame = frame;
		components = frame.getContentPane().getComponents();
		guiStatesChanged = true;
		guiStates = GuiStates.BLOCK;
	}

	/**
	 * Visszatér a "MessageBoxField" nevû {@link Component}-el.
	 * @return messageBoxField : {@link Component} - MessageBoxField nevû komponens
	 */
	public Component getMessageBoxField() {
		for (int i = 0; i < components.length; i++) {
			if (components[i].getName().equals("MessageBoxField")) {
				return components[i];
				
			}
		}
		return null;
	}
	
	/**
	 * Beállitja az Direction CheckBox-ok visibility-jét.
	 * @param isVisible
	 */
	public void setSpriteDirectionsVisibility(boolean isVisible) {
		for(int i= 0;i < components.length;i++) {
			if( components[i].getName().equals("North") ||
				components[i].getName().equals("South") ||
				components[i].getName().equals("West") ||
				components[i].getName().equals("East")) {
				components[i].setVisible(isVisible);
			}
				
			
		}
	}
	
	/**
	 * Visszatér a "Textures" nevû {@link Component}-el.
	 * @return textures : {@link Component} - "Textures" nevû komponenes
	 */
	public Component getTexturesBox() {
		for (int i = 0; i < components.length; i++) {
			if (components[i].getName().equals("Textures")) {
				return components[i];
			}
		}
		return null;
	}
	
	/**
	 * Visszatér az "EntityType" nevû {@link Component}-el
	 * @return entityType : {@link Component} - "EntityType" nevû komponens
	 */
	public Component getEntityBox() {
		for (int i = 0; i < components.length; i++) {
			if (components[i].getName().equals("EntityType")) {
				return components[i];
			}
		}
		return null;
	}
	
	/**
	 * Visszatér a "TriggerType" nevû {@link Component}-el
	 * @return triggerType : {@link Component} - "TriggerType" nevû komponens
	 */
	public Component getTriggerTypeBox() {
		for (int i = 0; i < components.length; i++) {
			if (components[i].getName().equals("TriggerType")) {
				return components[i];
			}
		}
		return null;
	}

	/**
	 * Visszatér a falakat jellemzõ checkbox-ok értékével, amiket egy boolean tömbként ad vissza.
	 * @return walls : boolean[] - a falakat jellemzõ boolean tömb, true - ha van fal, false - ha nincs
	 */
	public boolean[] getWalls(){
		boolean[] walls = new boolean[4];
		for(int i= 0;i < components.length;i++) {
			if(components[i].getName().equals("North")) {
				JCheckBox cb = (JCheckBox)components[i];
				walls[Sprite.getDirIndex(Dir.NORTH)] = cb.isSelected();
			}
			if(components[i].getName().equals("East")) {
				JCheckBox cb = (JCheckBox)components[i];
				walls[Sprite.getDirIndex(Dir.EAST)] = cb.isSelected();
			}
			if(components[i].getName().equals("West")) {
				JCheckBox cb = (JCheckBox)components[i];
				walls[Sprite.getDirIndex(Dir.WEST)] = cb.isSelected();
			}
			if(components[i].getName().equals("South")) {
				JCheckBox cb = (JCheckBox)components[i];
				walls[Sprite.getDirIndex(Dir.SOUTH)] = cb.isSelected();
			}
			
		}
		
		
		
		return walls;
	}
	
	/**
	 * Kigenerálja a texture-okat tartalmazó componenst
	 * @param textureList_size : a {@link TextureList} mérete
	 * @param listener : a komponenshez tartozó {@link Editor}
	 * @return list : {@link JComboBox} - a kigenerált ComboBox
	 */
	public static JComboBox GenerateTextureListComponent(int textureList_size,
			Editor listener) {
		String textures_temp = "";
		for (int i = 0; i < textureList_size; i++) {
			textures_temp = textures_temp + i + " ";
		}
		String[] textures = textures_temp.split(" ");

		JComboBox list = new JComboBox(textures);
		list.setName("Textures");
		ComboBoxRenderer renderer = new ComboBoxRenderer();
		list.setRenderer(renderer);
		
		
		list.setPreferredSize(new Dimension(100, list.getMinimumSize().height));
	
		list.addActionListener(listener);
		return list;
	}

	/**
	 * Kigenerálja az {@link EntityType}-okat összefoglaló {@link JComboBox}-t.
	 * @param listener : {@link Editor} - az actionöket kezelõ {@link Editor}
	 * @return list : {@link JComboBox} - a kigenerált {@link JComboBox}
	 */
	public static JComboBox GenerateEntityTypeComponent(Editor listener) {
		String types_tmp = "";
		types_tmp = EntityType.BLOCK.toString() + " ";
		types_tmp += EntityType.TRIGGER.toString();
		String[] types = types_tmp.split(" ");

		JComboBox list = new JComboBox(types);
		list.setName("EntityType");

		list.setPreferredSize(new Dimension(100, list.getMinimumSize().height));
		list.addActionListener(listener);
		return list;
	}
	
	/**
	 * Kigenerálja a {@link TriggerType}-okat összefoglaló {@link JComboBox}-t
	 * @param listener - a kigenerált JComboBox actiönjeit feldolgozó {@link Editor}.
	 * @return list : {@link JComboBox} - a kigenerált {@link JComboBox}
	 */
	public Component GenerateTriggerListComponent(Editor listener) {
		TriggerType[] t = TriggerType.values();
		String trigger_type = "";
		for(int i=0;i<t.length;i++) {
			trigger_type += t[i] + " ";
		}
		String[] types = trigger_type.split(" ");
		JComboBox list = new JComboBox(types);
		list.setName("TriggerType");

		list.setPreferredSize(new Dimension(100, list.getMinimumSize().height));
		list.addActionListener(listener);
		return list;
	}

	/**
	 * Frissiti az összes {@link Component}-t, az adott {@link GuiStates} alapján.
	 */
	public void update() {
		if(guiStatesChanged) {
			switch(guiStates) {
			case BLOCK:
				getMessageBoxField().setVisible(false);
				setSpriteDirectionsVisibility(true);
				getTriggerTypeBox().setVisible(false);
				break;
			case MESSAGEBOX:
				getMessageBoxField().setVisible(true);
				setSpriteDirectionsVisibility(false);
				getTriggerTypeBox().setVisible(true);
				break;
			case TRIGGER:
				getTriggerTypeBox().setVisible(true);
				
				break;
			case SPIKE:
				getMessageBoxField().setVisible(false);
				setSpriteDirectionsVisibility(true);
				getTriggerTypeBox().setVisible(true);
				break;
				
			case FINISH_LINE:
				getTriggerTypeBox().setVisible(true);
				setSpriteDirectionsVisibility(false);
				getMessageBoxField().setVisible(false);
				break;
			}
			
			guiStatesChanged = false;
		}
	}

	/**
	 * Frissiti a {@link Component}-tek változása által generált {@link ActionEvent} alapján a
	 * a {@link GuiStates}-et.
	 * @param arg0 : {@link ActionEvent} - a generált {@link ActionEvent}.
	 */
	public void updateGuiState(ActionEvent arg0) {
		JComboBox c = (JComboBox)arg0.getSource();
		if(c.getName().equals("EntityType")) {
			String type = (String)c.getSelectedItem();
			if(type.equals("BLOCK")) {
				guiStates = GuiStates.BLOCK;
				guiStatesChanged = true;
			} else if(type.equals("TRIGGER")) {
				guiStates = GuiStates.TRIGGER;
				guiStatesChanged = true;
			}
		} else if(c.getName().equals("TriggerType")) {
			String type = (String)c.getSelectedItem();
			if(type.equals("MESSAGE_BOX")){
				guiStates = GuiStates.MESSAGEBOX;
				guiStatesChanged = true;
			} else if(type.equals("SPIKE")){
				guiStates = GuiStates.SPIKE;
				guiStatesChanged = true;
			} else if(type.equals("FINISH_LINE")) {
				guiStates = GuiStates.FINISH_LINE;
				guiStatesChanged = true;
			}
		}
		
	}

	/**
	 * Visszatér a {@link JComboBox}oBox által meghatározott {@link TriggerType}-al.
	 * @param c : a paraméterül adott {@link JComboBox}
	 * @return ret :  {@link TriggerType} - a kiválasztott {@link TriggerType}.
	 */
	public TriggerType getTriggerType(JComboBox c) {
		return TriggerType.valueOf((String) c.getSelectedItem());
	} 

	

}
