import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.*;
import java.io.File;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class JPad extends JFrame implements ActionListener, KeyListener, Printable  
{
	//Variables for window items.
	JFrame frame;
	
	//custom icon setup
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	URL url = getClass().getResource("/resources/imgs/JPad_icon.png");
	Image imageIcon = toolkit.createImage(url);
	
	String OS = System.getProperty("os.name");

	//variables for the MenuBar.
	JMenuBar menuBar;
	JMenu file, edit, help; 
	//Items under file.
	JMenuItem New, open, save, saveAs, print, exit;
	//Items under edit.
	JMenuItem copy, paste, cut, delete, selectAll;
	//Items under Help.
	JMenuItem viewHelp, about;
	
	//Variables for the tabs.
	JTabbedPane tabs;
	JScrollPane scrollPane;
	int index, num = 0;
	String tempText;
	String[] tabName = new String[100];
	JTextArea[] textArea = new JTextArea[100];
	Image image;
	ImageIcon img;
	
	//Variables for tab pop up.
	JPopupMenu tabPopup;
	JMenuItem popupClose, popupCloseAll, popupCloseKeep, popupSave, popupSaveAs, popupRename;
	
	//Variables for edit pop up.
	JPopupMenu editPopup;
	JMenuItem popupCopy, popupPaste, popupCut, popupDelete, popupSelectAll;
	
	//Variables for key presses.
	boolean ctrlPressed, altPressed, f1Pressed, f12Pressed = false;
	boolean nPressed, oPressed, sPressed, pPressed = false;
	boolean xPressed, cPressed, vPressed, delPressed, aPressed = false;
	boolean fileWasSaved[] = new boolean[100];
	
	
	//Variables for the JFileChooser.
	JFileChooser fileChoose = new JFileChooser();
	JFileChooser saveDialog = new JFileChooser();
	File currentFile = null;
	File currentDirectory = null;
	String fileString = "", getFileName;
	String textToSave;
	int start, end, fileLength, selectedTab, check, saveCheck;
	
	//Buttons for unsaved changes
	JFrame checkFrame, checkTabFrame;
	JButton quitButton, cancelQuitButton, closeButton, saveButton, cancelCloseButton;
	boolean closeOne, closeAll, closeAllExcept = false;
	
	//variable for printing
	int pageBreaks[];
	
	//Constructor that sets up properties of the JFrame.
	JPad()
	{
		//Set up frame for main window.
		frame = new JFrame("JPad");
		frame.setIconImage(imageIcon); 
		setLayout(new BorderLayout());
		frame.setSize(500,500);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		createMenuBar();
		
		frame.setVisible(true);
	}
	
	public static void main(String[] arg)
	{
		new JPad();
	}
	
	//Creates the JMenuBar and JPopupMenu for JPad.
	public JMenuBar createMenuBar()
	{
		//Menu Bar options.
		menuBar = new JMenuBar();
		file = new JMenu("File");
		edit = new JMenu("Edit");
		help = new JMenu("Help");
		
		//Initiate Tabs.
		tabs = new JTabbedPane();
		
		//Tab Pop up options.
		tabPopup = new JPopupMenu();
		popupClose = new JMenuItem("Close"); 
		popupCloseAll = new JMenuItem("Close All");  
		popupCloseKeep = new JMenuItem("Close All Except Current Tab"); 
		popupSave = new JMenuItem("Save");  
		popupSaveAs = new JMenuItem("Save As");  
		
		//Edit Pop up options.
		editPopup = new JPopupMenu();
		popupCopy = new JMenuItem("Copy"); 
		popupPaste = new JMenuItem("Paste");  
		popupCut = new JMenuItem("Cut"); 
		popupDelete = new JMenuItem("Delete");  
		popupSelectAll = new JMenuItem("Select All");  
		 
		//Under file.
		New = new JMenuItem("New                     |  Ctrl + N");
		open = new JMenuItem("Open                   |  Ctrl + O");
		save = new JMenuItem("Save                    |  Ctrl + S");
		saveAs = new JMenuItem("Save As     |  Ctrl + Alt + S");
		print = new JMenuItem("Print                     |  Ctrl + P");
		exit = new JMenuItem("Exit                             |  Esc");
		
		//Under Edit.
		cut       = new JMenuItem("Cut                   |  Ctrl + X");
		copy      = new JMenuItem("Copy                |  Ctrl + C");
		paste     = new JMenuItem("Paste              |  Ctrl + V");
		delete    = new JMenuItem("Delete              |  Delete");
		selectAll = new JMenuItem("Select All       |  Ctrl + A");
		 
		//Under Help.
		viewHelp = new JMenuItem("View Help      |   Ctrl + F1");
		about = new JMenuItem("About                         |   F1");
		
		//Add Menu options
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(help);
		
		//Add sub options for file.
		file.add(New);
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.add(print);
		file.add(exit);
		//Add sub options for edit.
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		edit.add(delete);
		edit.add(selectAll);
		//Add sub options for help.
		help.add(viewHelp);
		help.add(about);
		
		//Add options for Tab Pop up menu.
		tabPopup.add(popupClose);
		tabPopup.add(popupCloseAll);
		tabPopup.add(popupCloseKeep);
		tabPopup.add(popupSave);
		tabPopup.add(popupSaveAs);
		
		//Add options for edit Pop up menu.
		editPopup.add(popupCopy);
		editPopup.add(popupPaste);
		editPopup.add(popupCut);
		editPopup.add(popupDelete);
		editPopup.add(popupSelectAll);
		
		//Add Menu Bar to frame.
		frame.add(menuBar);
		frame.setJMenuBar(menuBar);
		
		//Variables to add the tab to the window.
		frame.add(tabs);
		addTab();
		
		
		
		//Add ActionListners for file menu.
		New.addActionListener(this);
		open.addActionListener(this);
		save.addActionListener(this);
		saveAs.addActionListener(this);
		print.addActionListener(this);
		exit.addActionListener(this);

		//Add ActionListners for edit menu.
		cut.addActionListener(this);
		copy.addActionListener(this);
		paste.addActionListener(this);
		delete.addActionListener(this);
		selectAll.addActionListener(this);
		
		//Add ActionListners for help menu.
		viewHelp.addActionListener(this);
		about.addActionListener(this);
		
		//Add ActionListners for tab pop up menu.
		popupClose.addActionListener(this);
		popupCloseAll.addActionListener(this);
		popupCloseKeep.addActionListener(this);
		popupSave.addActionListener(this);
		popupSaveAs.addActionListener(this);
		
		//Add ActionListners for edit pop up menu.
		popupCopy.addActionListener(this);
		popupPaste.addActionListener(this);
		popupCut.addActionListener(this);
		popupDelete.addActionListener(this);
		popupSelectAll.addActionListener(this);
			
		//Set up Key Listener for key presses.
		frame.setFocusable(true);
		frame.addKeyListener(this);
		textArea[num].addKeyListener(this);
		
		//Register the event handler for the tab pop up menu.
		tabs.addMouseListener(new MouseAdapter() 
		{
		public void mouseReleased(MouseEvent event)
		{
			if(event.getButton() == MouseEvent.BUTTON3)
			{
				num = tabs.indexAtLocation(event.getX(), event.getY());
				if(index != 0)
				{
					tabPopup.show(event.getComponent(), event.getX(), event.getY());
					
					System.out.println("num: " + num);
					System.out.println("selectedTab: " + selectedTab);
				}
			}
		}
		});
		
		// register the event handler for the edit pop up menu.
		textArea[num].addMouseListener(new MouseAdapter() 
		{
		public void mouseReleased(MouseEvent event)
		{
			if(event.getButton() == MouseEvent.BUTTON3)
			{
				num = tabs.indexAtLocation(event.getX(), event.getY());
				editPopup.show(event.getComponent(), event.getX(), event.getY());
			}
		}
		});
		
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() 
		{
		    @Override
		    public void windowClosing(WindowEvent windowEvent) 
		    {
		    	System.out.println("exit x was pressed");
		    	selectedTab = tabs.getSelectedIndex();
		    	if(selectedTab < 0)
		    	{
		    		System.exit(0);
		    	}
		    	if(fileWasSaved[selectedTab] == false)
		    	{
		    		checkUnsaved();
		    	}
		    	else
		    	{
		    		System.exit(0);
		    	}
		    }
		});
		
		
		System.out.println("OS: " + OS);
			
		return menuBar;
	}
	
	public void addTab()
	{
		textArea[index] = new JTextArea();
		textArea[index].setLineWrap(true);
		fileWasSaved[index] = true;
		scrollPane = new JScrollPane(textArea[index]);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		
		tabName[index] = "New Tab";
		tabs.insertTab(tabName[index], img, scrollPane, "*Right click for options.*", index);
		textArea[index].addKeyListener(this);
		tabs.setSelectedIndex(index);
		++index;
	}
	
	public void checkUnsaved()
	{
		checkFrame = new JFrame("Unsaved Changes");
		JPanel checkPanel = new JPanel();
		checkFrame.setLayout(new BorderLayout());
		checkFrame.setIconImage(imageIcon); 
		checkFrame.setSize(400,100);
		checkFrame.setResizable(false);
		
		String quitQ = "There are unsaved changes. Are you sure you want to exit.";
		JLabel qLabel= new JLabel(quitQ);
		quitButton = new JButton("Quit");
		cancelQuitButton = new JButton("Cancel");
		
		checkPanel.add(qLabel);
		checkPanel.add(quitButton);
		checkPanel.add(cancelQuitButton);
		
		checkFrame.add(checkPanel);
		
		quitButton.addActionListener(this);
		cancelQuitButton.addActionListener(this);
		
		checkFrame.setVisible(true);
	}
	
	public void closeTab()
	{
		checkTabFrame = new JFrame("Unsaved Changes");
		JPanel checkTabPanel = new JPanel();
		checkTabFrame.setLayout(new BorderLayout());
		checkTabFrame.setIconImage(imageIcon); 
		checkTabFrame.setSize(400,100);
		checkTabFrame.setResizable(false);
		
		String quitQ = "There are unsaved changes. Are you sure you want to exit.";
		JLabel qLabel= new JLabel(quitQ);
		closeButton = new JButton("Close");
		saveButton = new JButton("Save");
		cancelCloseButton = new JButton("Cancel");
		
		checkTabPanel.add(qLabel);
		checkTabPanel.add(closeButton);
		checkTabPanel.add(saveButton);
		checkTabPanel.add(cancelCloseButton);
		
		checkTabFrame.add(checkTabPanel);
		
		closeButton.addActionListener(this);
		saveButton.addActionListener(this);
		cancelCloseButton.addActionListener(this);
		
		checkTabFrame.setVisible(true);
	}
	
	public void closeTabOptions()
	{
		if(closeOne == true)
		{
			if(num > -1)
			{
				tabs.remove(num);
				index--;
			}
			closeOne = false;
		}
		else if(closeAll == true)
		{
			tabs.removeAll();
			index = 0;
			boolean[] fileWasSaved;
			closeAll = false;
			addTab(); //needed because removeAll sets JTabbedPane index to -1.
		}
		else if(closeAllExcept == true)
		{
			int keep;
			String tempName;
			keep = tabs.getSelectedIndex();
			tempText = textArea[keep].getText();
			tempName = tabName[keep];
			textArea[0] = new JTextArea(tempText);
			
			if(tempName == null)
			{
				tempName = "New Tab";
			}
			tabs.removeAll();
			index = 0;
			boolean[] fileWasSaved;
			
			tabs.insertTab(tempName, img, textArea[0], "*Right click for options.*", 0);
			index++;
			closeAllExcept = false;
		}
	}
	
	public void showViewHelp()
	{
		try 
		{
			//Go to help WebSite. Currently to github readme.
			String urlHelp = "https://github.com/jrkinch/jpad-text-editor";
			Desktop.getDesktop().browse(URI.create(urlHelp));
		}
		catch (java.io.IOException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void showAbout()
	{
		JFrame aboutFrame = new JFrame("About");
		JPanel aboutPanel = new JPanel();
		aboutFrame.setLayout(new BorderLayout());
		aboutFrame.setIconImage(imageIcon); 
		aboutFrame.setSize(400,300);
		aboutFrame.setResizable(false);
	
		JLabel[] aboutText = new JLabel[8];
		String[] aboutTextString = new String[8];
		
		aboutTextString[0] = "                                                                  ";
		aboutTextString[1] = "                            BETA                                  ";
		aboutTextString[2] = "                            JPad                                  ";	
		aboutTextString[3] = "                       Version: 1.0.0                             ";
		aboutTextString[4] = "                  Operating System:  " + OS + "                   ";
		aboutTextString[5] = "             This Program was developed using Java.               ";
		aboutTextString[6] = "                    Author: jrkinch                           ";
		
		for(int i = 0; i < 7; i++)
		{
			aboutText[i] = new JLabel(aboutTextString[i],JLabel.CENTER);
			
			aboutPanel.add(aboutText[i]);
		}
		aboutFrame.add(aboutPanel);
		aboutFrame.setVisible(true);
	}
	
	public void openFile()
	{
		JFileChooser fileChoose = new JFileChooser(currentDirectory){
			@Override
			public void approveSelection(){
				//logic if extension is not added to name, defaults to '.txt'
				currentFile = getSelectedFile();
				fileString = currentFile.toString();
				getFileName = currentFile.getName();
				
				if(!fileString.contains("."))
				{
					fileString += ".txt";
					getFileName += ".txt";
				}
				File nf = new File(fileString);
				currentDirectory = getCurrentDirectory();
				
				if(!nf.exists() && getDialogType() == OPEN_DIALOG){
					int result = JOptionPane.showConfirmDialog(this,"That File does not exists. Input new file name?","No File Found",JOptionPane.YES_OPTION);
					switch(result){
						case JOptionPane.YES_OPTION:
							fileString = "";
							setSelectedFile(new File(fileString));
							return;
						case JOptionPane.NO_OPTION:
							cancelSelection();
							return;
						case JOptionPane.CLOSED_OPTION:
							return;
					}
				}
				super.approveSelection();
			}
		};
		
		int check = fileChoose.showOpenDialog(null);
		if(check == fileChoose.APPROVE_OPTION)
		{
			readTextFile();
		}
		else if (check == JFileChooser.CANCEL_OPTION) 
		{
			System.out.println("User canceled action");
		}	
	}
	
	public void saveTextFile()
	{
		try 
		{
			FileWriter fstream = new FileWriter(fileString, false);//append is false
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(textArea[selectedTab].getText());
			
			//setting tab info
			tabName[selectedTab] = getFileName;
			tabs.remove(selectedTab);
			tabs.insertTab(tabName[selectedTab], img, textArea[selectedTab], "*Right click for options.*", selectedTab);
			tabs.setSelectedIndex(selectedTab);
			fileWasSaved[selectedTab] = true;

			out.flush();
			out.close();
		}
		catch (Exception evt) 
		{
			System.out.println("Saving: " + evt.getMessage());
		}
	}
	
	public void readTextFile()
	{
		try 
		{
			tabName[index] = getFileName;
			textArea[index] = new JTextArea();
			
			File myObj = new File(fileString);
			Scanner myReader = new Scanner(myObj);
			
			while(myReader.hasNextLine())
			{
				String data = myReader.nextLine();
				textArea[index].append(data);
				textArea[index].append("\n");
			}
			
			//setting tab info
			tabs.insertTab(tabName[index], img, textArea[index], "*Right click for options.*", index);
			tabs.setSelectedIndex(index);
			fileWasSaved[index] = true;
			++index;
		
			myReader.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void saveFile()
	{
		selectedTab = tabs.getSelectedIndex();
		String currentTabPath = currentDirectory + "\\" + tabName[selectedTab];
		File tabPath = new File(currentTabPath);
		if(tabPath.exists())
		{
			System.out.println("File already exists: " + tabPath);
			saveTextFile();
		}
		else
		{
			saveAsFile();
		}
	}
	
	public void saveAsFile()
	{
		JFileChooser saveDialog = new JFileChooser(currentDirectory){
			@Override
			public void approveSelection(){
				//logic if extension is not added to name, defaults to '.txt'
				currentFile = getSelectedFile();
				fileString = currentFile.toString();
				getFileName = currentFile.getName();
				
				if(!fileString.contains("."))
				{
					fileString += ".txt";
					getFileName += ".txt";
				}
				File nf = new File(fileString);
				currentDirectory = getCurrentDirectory();
				
				if(nf.exists() && getDialogType() == SAVE_DIALOG){
					int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
					switch(result){
						case JOptionPane.YES_OPTION:
							super.approveSelection();
							return;
						case JOptionPane.NO_OPTION:
							return;
						case JOptionPane.CLOSED_OPTION:
							return;
						case JOptionPane.CANCEL_OPTION:
							cancelSelection();
							return;
					}
				}
				super.approveSelection();
			}
		};

		int saveCheck = saveDialog.showSaveDialog(null);
		if (saveCheck == JFileChooser.APPROVE_OPTION) 
		{
			saveTextFile();
		}
		else if (saveCheck == JFileChooser.CANCEL_OPTION) 
		{
			System.out.println("User canceled action");
		}
	}
	
	public void printFile()
	{
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this);
		  if(printJob.printDialog()) 
		  {
			  try 
			  {
				  printJob.print();
			  }
			  catch (PrinterException exc) 
			  {
				  System.out.println(exc);
				  printJob.cancel();
			  }
		  }	
	}
	
	public void multiplePressed()
	{
		selectedTab = tabs.getSelectedIndex();
		if(ctrlPressed == true && f12Pressed == true)
		{
			showAbout();
			ctrlPressed = false;
			f12Pressed = false;
		}
		if(ctrlPressed == true && f1Pressed == true)
		{
			showViewHelp();
			ctrlPressed = false;
			f1Pressed = false;
		}
		if(ctrlPressed == true && nPressed == true)
		{
			addTab();
			ctrlPressed = false;
			nPressed = false;
		}
		if(ctrlPressed == true && oPressed == true)
		{
			openFile();
			ctrlPressed = false;
			oPressed = false;
		}
		if(ctrlPressed == true && sPressed == true)
		{
			saveFile();
			ctrlPressed = false;
			sPressed = false;
		}
		if((ctrlPressed == true && altPressed) && sPressed == true)
		{
			saveAsFile();
			ctrlPressed = false;
			altPressed = false;
			sPressed = false;
		}
		if(ctrlPressed == true && pPressed == true)
		{
			printFile();
			ctrlPressed = false;
			pPressed = false;
		}
		
		if(!OS.contains("Windows"))
		{
			if(ctrlPressed == true && xPressed == true)
			{
				textArea[selectedTab].cut();
				ctrlPressed = false;
				xPressed = false;
			}
			if(ctrlPressed == true && cPressed == true)
			{
				textArea[num].copy();
				ctrlPressed = false;
				cPressed = false;
			}
			if(ctrlPressed == true && vPressed == true)
			{
				textArea[num].paste();
				ctrlPressed = false;
				vPressed = false;
			}
			if(delPressed == true)
			{
				textArea[num].getSelectedText();
				textArea[num].replaceSelection("");
				delPressed = false;
			}
			if(ctrlPressed == true && aPressed == true)
			{
				textArea[num].selectAll();
				ctrlPressed = false;
				aPressed = false;
			}
		}
	}

	//Checks to see if a certain key listener was selected.
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		//Event Listeners for file options.
		if(event.getSource() == New)
		{
			addTab();
		}
		else if(event.getSource() == open)
		{
			openFile();
		}
		else if(event.getSource() == save)
		{
			saveFile();
		}
		else if(event.getSource() == saveAs)
		{
			saveAsFile();
		}
		else if(event.getSource() == print)
		{
			printFile();
		}
		else if(event.getSource() == exit)
		{
			System.exit(0);
		}
		
		//Event Listeners for edit options.
		else if(event.getSource() == cut)
		{
			textArea[num].cut();
		}
		else if(event.getSource() == copy)
		{
			textArea[num].copy();
		}
		else if(event.getSource() == paste)
		{
			textArea[num].paste();
		}
		else if(event.getSource() == delete)
		{
			textArea[num].getSelectedText();
			textArea[num].replaceSelection("");
		}
		else if(event.getSource() == selectAll)
		{
			textArea[num].selectAll();
		}
		
		//Event Listeners for Help options.
		else if(event.getSource() == viewHelp)
		{
			showViewHelp();
		}
		else if(event.getSource() == about)
		{
			showAbout();
		}
		
		//Event Listeners for tab pop up options.
		else if(event.getSource() == popupClose)
		{
			closeOne = true;
	    	selectedTab = tabs.getSelectedIndex();
	    	if(fileWasSaved[selectedTab] == false)
	    	{
	    		closeTab();
	    	}
	    	else
	    	{
	    		closeTabOptions();
	    	}
		}
		else if(event.getSource() == popupCloseAll)
		{
			closeAll = true;
	    	selectedTab = tabs.getSelectedIndex();
	    	if(fileWasSaved[selectedTab] == false)
	    	{
	    		closeTab();
	    	}
	    	else
	    	{
	    		closeTabOptions();
	    	}
		}
		else if(event.getSource() == popupCloseKeep)
		{
			closeAllExcept = true;
	    	selectedTab = tabs.getSelectedIndex();
	    	if(fileWasSaved[selectedTab] == false)
	    	{
	    		closeTab();
	    	}	
	    	else
	    	{
	    		closeTabOptions();
	    	}
		}
		else if(event.getSource() == popupSave)
		{
			saveFile();
		}
		else if(event.getSource() == popupSaveAs)
		{
			saveAsFile();
		}
		
		//Event Listeners for the edit Pop up menu.
		else if(event.getSource() == popupCut)
		{
			textArea[selectedTab].cut();
		}
		else if(event.getSource() == popupCopy)
		{
			textArea[selectedTab].copy();
		}
		else if(event.getSource() == popupPaste)
		{
			textArea[selectedTab].paste();
		}
		else if(event.getSource() == popupDelete)
		{
			textArea[selectedTab].getSelectedText();
			textArea[selectedTab].replaceSelection("");
		}
		else if(event.getSource() == popupSelectAll)
		{
			textArea[selectedTab].selectAll();
		}
		
		//Event Listener for unsaved changes prompt.
		else if(event.getSource() == quitButton)
		{
			System.exit(0);
		}
		else if(event.getSource() == cancelQuitButton)
		{
			checkFrame.dispose();
		}
		else if(event.getSource() == closeButton)
		{
			checkTabFrame.dispose();
			closeTabOptions();
		}
		else if(event.getSource() == saveButton)
		{
			checkTabFrame.dispose();
			saveFile();
		}
		else if(event.getSource() == cancelCloseButton)
		{
			checkTabFrame.dispose();
		}
	}

	@Override
	public void keyPressed(KeyEvent event) 
	{
		//Checks for key presses and if multiple keys are pressed.
		int keyCode = event.getKeyCode();
		selectedTab = tabs.getSelectedIndex();
		switch(keyCode)
		{
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			case KeyEvent.VK_F1:
				f1Pressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_F12:
				f12Pressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_ALT:
				altPressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_CONTROL:
				ctrlPressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_N:
				nPressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_O:
				oPressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_S:
				sPressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_P:
				pPressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_X:
				xPressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_C:
				cPressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_V:
				vPressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_DELETE:
				delPressed = true;
				multiplePressed();
				break;
			case KeyEvent.VK_A:
				aPressed = true;
				multiplePressed();
				break;
		}
		if(fileWasSaved[selectedTab] == true)
		{
			fileWasSaved[selectedTab] = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		int keyCode = event.getKeyCode();
		switch(keyCode)
		{
			case KeyEvent.VK_F1:
				f1Pressed = false;	
				break;
			case KeyEvent.VK_F12:
				f12Pressed = false;
				break;
			case KeyEvent.VK_ALT:
				altPressed = false;
				break;
			case KeyEvent.VK_CONTROL:
				ctrlPressed = false;
				break;
			case KeyEvent.VK_N:
				nPressed = false;
				break;
			case KeyEvent.VK_O:
				oPressed = false;
				break;
			case KeyEvent.VK_S:
				sPressed = false;
				break;
			case KeyEvent.VK_P:
				pPressed = false;
				break;
			case KeyEvent.VK_X:
				xPressed = false;
				break;
			case KeyEvent.VK_C:
				cPressed = false;
				break;
			case KeyEvent.VK_V:
				vPressed = false;
				break;
			case KeyEvent.VK_DELETE:
				delPressed = false;
				break;
			case KeyEvent.VK_A:
				aPressed = false;
				break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent event) 
	{
		// TODO Auto-generated method stub
		//Was auto generated with key listener method.
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int page) throws PrinterException 
	{
		tabs.getSelectedComponent();
		selectedTab = tabs.getSelectedIndex();
		fileLength = textArea[selectedTab].getLineCount();
		
		Font font = new Font("New Times Roman", Font.PLAIN, 10);         
        FontMetrics fontMetrics = g.getFontMetrics(font);         
        int lineHeight = fontMetrics.getHeight(); 
        	
		if (pageBreaks == null) 
		{
            int linesPerPage = (int)(pageFormat.getImageableHeight()/lineHeight);
            int numBreaks = (fileLength-1)/linesPerPage;
            pageBreaks = new int[numBreaks];
            for (int b = 0; b < numBreaks; b++) {
                pageBreaks[b] = (b+1)*linesPerPage; 
            }
        }
		
		if(page > pageBreaks.length)
		{
			return NO_SUCH_PAGE;
		}
		
        Graphics2D g2d = (Graphics2D)g; 
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        
        double pageHeight = pageFormat.getHeight();
        double pageLength = pageFormat.getWidth();
        String textLine[] = new String[fileLength];
       
        
        textToSave = textArea[selectedTab].getText();
        
        //Set text to number of lines per page.
        for(int textToLines = 0; textToLines < fileLength; textToLines++)
        {
			try
			{
			   start = textArea[selectedTab].getLineStartOffset(textToLines);
			   end = textArea[selectedTab].getLineEndOffset(textToLines);
			} 
			catch (BadLocationException printEvent) 
			{
			   printEvent.printStackTrace();
			}
	   
			textLine[textToLines] = textToSave.substring(start, end);
        }
        
        //Prints text to the amount of lines per page.
        int y = 25;
		int margin = 25;
        int start = (page == 0) ? 0 : pageBreaks[page-1];
        int end   = (page == pageBreaks.length)
                         ? fileLength : pageBreaks[page];
        for (int line = start; line < end; line++) 
        {
            y += lineHeight;
            g.drawString(textLine[line], margin, y);
        }
       
      System.out.println("pageBreak:" + pageBreaks.length +", page: " + page + ", page height: " + pageHeight + ", page length: " + pageLength);
        
      return 0;
	}
}
