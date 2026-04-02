package com.moneyboard.Style;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

public class Style
{
	
	public static JButton newStyledButton(String text, Color colorForeground, Color backGr)
	{
		
		JButton button = new JButton(text);
		
		 button.setFont(new Font("Arial", Font.BOLD, 16));
		 button.setBackground(backGr);
		 button.setForeground(colorForeground);
		 
		 
		 return button; 
	}
	
	public static JButton newStyledButton(String text, Color colorForeground, Color backGr, int dimText)
	{
		JButton button = new JButton(text);
		
		 button.setFont(new Font("Arial", Font.BOLD, dimText));
		 button.setBackground(backGr);
		 button.setForeground(colorForeground);
		 
		 
		 return button; 
	}

	

}
