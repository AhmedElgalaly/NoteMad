import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;


public class TextEditor extends JFrame {
	private int fontSize;
	private String fontName;
	private Color textColor;
	private JTextArea textArea;
	private JFileChooser fileChooser;
	private String filePath;
	private int words;
	private int characters;
	JLabel WCounter;
	JLabel CCounter;
	final String regex = "(\\d\\w+|[0-9])";
	
	//private Font textFont;
	
	
	TextEditor(){
		this.setSize(500,550);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(new FlowLayout());
		this.setTitle("My Text Editor");
		this.setLocationRelativeTo(null);
		
		addComponents();
		
		this.setVisible(true);
	}
	
	void addComponents() {
		
		
		textArea = addTextArea();
		
		
		
		this.setJMenuBar(addMenu());
		this.add(addTopPanel());
		this.add(addTextAreaToScroll());
		this.add(addFooterPanel());
		
	}
	
	JPanel addTopPanel() {
		JPanel boxes = new JPanel();
		boxes.setLayout(new GridLayout(1,3,30, 10));
		boxes.add(addSpinnerToPanel());
		boxes.add(addColorChooser());
		boxes.add(addFontComboBox());
		
		return boxes;
	}
	
	JMenuBar addMenu() {
		fileChooser = new JFileChooser();
		filePath = new String();
		
		JMenuBar menu = new JMenuBar();
		
		JMenu file_menu = new JMenu("File");
		
		JMenuItem save_item = new JMenuItem("Save");
		save_item.addActionListener(e -> {
			if(filePath.isEmpty())
			{
				int r = fileChooser.showSaveDialog(null);
				
				
				if(r == JFileChooser.APPROVE_OPTION) {
					filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".txt";
					try {
						FileWriter obj  = new FileWriter(filePath);
						obj.write(textArea.getText());
						obj.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Error With Saving"
								+ "File","Fetal Error", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
			}else {
				try {
					FileWriter obj  = new FileWriter(filePath);
					obj.write(textArea.getText());
					obj.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Error With Saving"
							+ "File","Fetal Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		
		JMenuItem saveAs_item = new JMenuItem("Save As");
		saveAs_item.addActionListener(e -> {
			int r = fileChooser.showSaveDialog(null);
			
			
			if(r == JFileChooser.APPROVE_OPTION) {
				filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".txt";
				try {
					FileWriter obj  = new FileWriter(filePath);
					obj.write(textArea.getText());
					obj.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Error With Saving "
							+ "File","Fetal Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		
		JMenuItem open_item = new JMenuItem("Open");
		open_item.addActionListener(e -> {
			String extractedText = new String();
			int r = fileChooser.showOpenDialog(null);
			
			if (r == JFileChooser.APPROVE_OPTION) {
				filePath = fileChooser.getSelectedFile().getAbsolutePath();
				try {
					File obj = new File(filePath);
					if(!fileChooser.getSelectedFile().canRead() ||
							!filePath
							.substring(filePath.length()-4)
							.equals(".txt")) {
						filePath = "";
						throw new IOException();
					}
					
					Scanner reader = new Scanner(obj);
					while(reader.hasNextLine()) {
						extractedText += reader.nextLine();
					}
					reader.close();
					textArea.setText(extractedText);
					
					refreshFooter();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Error With Opening "
							+ "File","Fetal Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
				
			}
			
		});
		
		JMenuItem exit_item = new JMenuItem("Exit");
		exit_item.addActionListener(e -> this.dispose());
		
		file_menu.add(save_item);
		file_menu.add(new JSeparator (SwingConstants.HORIZONTAL));
		
		file_menu.add(saveAs_item);
		file_menu.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		file_menu.add(open_item);
		file_menu.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		file_menu.add(exit_item);
		
		menu.add(file_menu);
		
		return menu;
	}
	
	JComboBox<String> addFontComboBox() {
		String[] fontNames = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();
		
		JComboBox<String> FontNamesComboBox = new JComboBox<>(fontNames);
		FontNamesComboBox.setPreferredSize(new Dimension(125,30));
		FontNamesComboBox.setFocusable(false);
		FontNamesComboBox.addActionListener(e->{
			fontName = (String) FontNamesComboBox.getSelectedItem();
			refreshtextArea();
		});
		
		return FontNamesComboBox;
		
	}
	
	JButton addColorChooser() {
		textColor = new Color(0, 0, 0);
		
		JButton colorChooseActivateBtn = new JButton("Color");
		colorChooseActivateBtn.setPreferredSize(new Dimension(125,30));
		colorChooseActivateBtn.setFocusable(false);
		colorChooseActivateBtn.addActionListener(e -> {
			textColor = JColorChooser
					.showDialog(null, "pick a color", Color.black);
			refreshtextArea();
		});
		
		return colorChooseActivateBtn;
	}
	
	JSpinner addFontSizeSpinner() {
		JSpinner fontSizeSpinner = new JSpinner();
		fontSizeSpinner.setPreferredSize(new Dimension(75,25));
		fontSizeSpinner.setFocusable(false);
		fontSizeSpinner.addChangeListener(e -> {
			fontSize = (int) fontSizeSpinner.getValue();
			refreshtextArea();
		});
		
		return fontSizeSpinner;
	}
	
	JTextArea addTextArea() {
		JTextArea UserEnteredTextBox = new JTextArea();
		
		UserEnteredTextBox.setBackground(Color.WHITE);
		UserEnteredTextBox.setLineWrap(true);
		UserEnteredTextBox.setWrapStyleWord(true);

		UserEnteredTextBox.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
		
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
				refreshFooter();
			}
		
			@Override
			public void keyPressed(KeyEvent arg0) {
					
		
			}
		});
		
		return UserEnteredTextBox;
	}
	
	JScrollPane addTextAreaToScroll() {
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(475,400));
		
		return scrollPane;
	}
	
	JPanel addSpinnerToPanel() {
		JPanel fontSizePanel = new JPanel();
		fontSizePanel.setPreferredSize(new Dimension(125,30));
		
		JLabel fontSLabel = new JLabel("Font:");
		
		fontSizePanel.add(fontSLabel);
		fontSizePanel.add(addFontSizeSpinner());
		
		return fontSizePanel;
	}

	JPanel addFooterPanel(){
		JPanel footer = new JPanel();
		WCounter = new JLabel("Words: 0");
		CCounter = new JLabel("Characters: 0");

		footer.setPreferredSize(new Dimension(450,35));
		//footer.setBackground(Color.white);

		words = 0;
		characters = 0;

		footer.setLayout(new GridLayout(1,2));
		footer.add(WCounter);
		footer.add(new JSeparator(SwingConstants.VERTICAL));
		footer.add(CCounter);

		return footer;
	}
	
	void refreshtextArea() {
		textArea.setFont(new Font(fontName, Font.PLAIN, fontSize));
		textArea.setForeground(textColor);
	}

	void refreshFooter(){
		String x = textArea.getText();
		int spaces =0;
		characters = 0;
		words =0;
        x = x.trim();
		for(char i:x.toCharArray()){
			if(i==' ')
				spaces++;
		}

		characters = x.length()- spaces;
		words = characters !=0 ? spaces+1:0;
		Pattern r = Pattern.compile(regex);
		Matcher m = r.matcher(WCounter.getText());

		if(m.find())
		WCounter.setText(WCounter.getText().replace(m.group(1), Integer.toString(words)));
		
		m = r.matcher(CCounter.getText());
		
		if(m.find())
		CCounter.setText(CCounter.getText().replace(m.group(1), Integer.toString(characters)));
	}
}
