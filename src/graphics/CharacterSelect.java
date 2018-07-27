package graphics;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import characters.Character;
import characters.*;
import program.AIController;
import program.CharacterController;
import program.PlayerController;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class CharacterSelect implements Page
{
	private JPanel m_panel = new JPanel();
	private final JFileChooser m_fileChooser = new JFileChooser();
	
	private CharacterController m_p1;
	private CharacterController m_p2;
	
	public static Character newCharacter(String p_name)
	{
		/*switch(p_name)
		{
			case "Jack":
				return new Jack();
				break;
				//etc.
		}*/
		return new GeorgeTheGlassCutter();
	}
	
	public CharacterSelect(GUI p_gui)
	{
		setUpPanel(p_gui);
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	private void setUpPanel(GUI p_gui)
	{
		GridBagLayout gbl_m_panel = new GridBagLayout();
		gbl_m_panel.columnWidths = new int[]{0, 89, 200, 89, 0, 0};
		gbl_m_panel.rowHeights = new int[]{40, 0, 14, 0, 23, 14, 20, 65, 0};
		gbl_m_panel.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_m_panel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		m_panel.setLayout(gbl_m_panel);
		
		JButton ai1ScriptLoad = new JButton("Load Script");
		GridBagConstraints gbc_ai1ScriptLoad = new GridBagConstraints();
		gbc_ai1ScriptLoad.anchor = GridBagConstraints.NORTH;
		gbc_ai1ScriptLoad.fill = GridBagConstraints.HORIZONTAL;
		gbc_ai1ScriptLoad.insets = new Insets(0, 0, 5, 5);
		gbc_ai1ScriptLoad.gridx = 1;
		gbc_ai1ScriptLoad.gridy = 4;
		ai1ScriptLoad.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				AIController tempController = new AIController();
				
				int returnVal = m_fileChooser.showOpenDialog(p_gui.getWindow());

		        if (returnVal == JFileChooser.APPROVE_OPTION)
		        {
		            File file = m_fileChooser.getSelectedFile();
		            String filePath = file.getPath();
		            System.out.println("Attempting to load " + filePath + " as player 1");
		            
		            //i feel like there may be a better way...
		            try
					{
						tempController.openFile(filePath);
					} catch (FileNotFoundException e1)
					{
						e1.printStackTrace();
						return;
					} catch (IOException e1)
					{
						e1.printStackTrace();
						return;
					}
		            m_p1 = tempController;
		            System.out.println("Player 1 AI script loaded at " + filePath);
		        }
			}
		});
		ai1ScriptLoad.setEnabled(false);
		
		JLabel lblPlayer = new JLabel("Player 1");
		lblPlayer.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblPlayer = new GridBagConstraints();
		gbc_lblPlayer.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblPlayer.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlayer.gridx = 1;
		gbc_lblPlayer.gridy = 1;
		m_panel.add(lblPlayer, gbc_lblPlayer);
		
		JLabel lblPlayer2 = new JLabel("Player 2");
		lblPlayer2.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblPlayer_1 = new GridBagConstraints();
		gbc_lblPlayer_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlayer_1.gridx = 3;
		gbc_lblPlayer_1.gridy = 1;
		m_panel.add(lblPlayer2, gbc_lblPlayer_1);
		
		
		
		JButton ai2ScriptLoad = new JButton("Load Script");
		GridBagConstraints gbc_ai2ScriptLoad = new GridBagConstraints();
		gbc_ai2ScriptLoad.anchor = GridBagConstraints.NORTH;
		gbc_ai2ScriptLoad.insets = new Insets(0, 0, 5, 5);
		gbc_ai2ScriptLoad.gridx = 3;
		gbc_ai2ScriptLoad.gridy = 4;
		ai2ScriptLoad.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				AIController tempController = new AIController();
				
				int returnVal = m_fileChooser.showOpenDialog(p_gui.getWindow());

		        if (returnVal == JFileChooser.APPROVE_OPTION)
		        {
		            File file = m_fileChooser.getSelectedFile();
		            String filePath = file.getPath();
		            System.out.println("Attempting to load " + filePath + " as player 2");
		            
		            //i feel like there may be a better way...
		            try
					{
						tempController.openFile(filePath);
					} catch (FileNotFoundException e1)
					{
						e1.printStackTrace();
						return;
					} catch (IOException e1)
					{
						e1.printStackTrace();
						return;
					}
		            m_p2 = tempController;
		            System.out.print("Player 2 AI script loaded at " + filePath);
		        }
			}
		});
		ai2ScriptLoad.setEnabled(false);
		m_panel.add(ai2ScriptLoad, gbc_ai2ScriptLoad);
		
		JComboBox<String> characterSelector1 = new JComboBox<String>(Character.characterNames);
		characterSelector1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JComboBox<String> bawks = (JComboBox<String>)e.getSource();
		        String characterName = (String)bawks.getSelectedItem();
		        //m_p1.setCharacter(newCharacter(characterName));
		        System.out.println("Player 1 character set to " + characterName);
			}
		});
		//characterSelector1.setSelectedIndex(0);
		GridBagConstraints gbc_characterSelector1 = new GridBagConstraints();
		gbc_characterSelector1.fill = GridBagConstraints.HORIZONTAL;
		gbc_characterSelector1.insets = new Insets(0, 0, 5, 5);
		gbc_characterSelector1.gridx = 1;
		gbc_characterSelector1.gridy = 6;
		m_panel.add(characterSelector1, gbc_characterSelector1);
		
		JComboBox<String> characterSelector2 = new JComboBox<String>(Character.characterNames);
		characterSelector2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JComboBox<String> bawks = (JComboBox<String>)e.getSource();
		        String characterName = (String)bawks.getSelectedItem();
		        //m_p2.setCharacter(newCharacter(characterName));
		        System.out.println("Player 2 character set to " + characterName);
			}
		});
		//characterSelector2.setSelectedIndex(0);
		GridBagConstraints gbc_characterSelector2 = new GridBagConstraints();
		gbc_characterSelector2.insets = new Insets(0, 0, 5, 5);
		gbc_characterSelector2.fill = GridBagConstraints.HORIZONTAL;
		gbc_characterSelector2.gridx = 3;
		gbc_characterSelector2.gridy = 6;
		m_panel.add(characterSelector2, gbc_characterSelector2);
		
		
		JCheckBox p1AiCheckBox = new JCheckBox("Is AI");
		p1AiCheckBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.DESELECTED)
				{
					ai1ScriptLoad.setEnabled(false);
					characterSelector1.setEnabled(true);
					m_p1 = new PlayerController();
					System.out.println("Player 1 set to human");
				} 
				else if(e.getStateChange() == ItemEvent.SELECTED)
				{
					ai1ScriptLoad.setEnabled(true);
					characterSelector1.setEnabled(false);
					m_p1 = new PlayerController();
					System.out.println("Player 1 set to AI");
				}
			}
		});
		GridBagConstraints gbc_p1AiCheckBox = new GridBagConstraints();
		gbc_p1AiCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_p1AiCheckBox.gridx = 1;
		gbc_p1AiCheckBox.gridy = 2;
		m_panel.add(p1AiCheckBox, gbc_p1AiCheckBox);
		
		JCheckBox p2AiCheckBox = new JCheckBox("Is AI");
		p2AiCheckBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.DESELECTED)
				{
					ai2ScriptLoad.setEnabled(false);
					characterSelector2.setEnabled(true);
					m_p2 = new PlayerController();
					System.out.println("Player 2 set to human");
				} 
				else if(e.getStateChange() == ItemEvent.SELECTED)
				{
					ai2ScriptLoad.setEnabled(true);
					characterSelector2.setEnabled(false);
					m_p2 = new PlayerController();
					System.out.println("Player 2 set to AI");
				}
			}
		});
		GridBagConstraints gbc_p2AiCheckBox = new GridBagConstraints();
		gbc_p2AiCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_p2AiCheckBox.gridx = 3;
		gbc_p2AiCheckBox.gridy = 2;
		m_panel.add(p2AiCheckBox, gbc_p2AiCheckBox);
		m_panel.add(ai1ScriptLoad, gbc_ai1ScriptLoad);
		
		
		
		JButton btnStartFight = new JButton("Start Fight");
		GridBagConstraints gbc_btnStartFight = new GridBagConstraints();
		gbc_btnStartFight.insets = new Insets(0, 0, 0, 5);
		gbc_btnStartFight.gridx = 2;
		gbc_btnStartFight.gridy = 7;
		btnStartFight.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						p_gui.setPage(p_gui.getRenderer());
					}
				});
		m_panel.add(btnStartFight, gbc_btnStartFight);
		
		
	}

	@Override
	public JComponent getComponent()
	{
		return m_panel;
	}

}
