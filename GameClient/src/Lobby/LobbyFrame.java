package Lobby;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class LobbyFrame extends JFrame {
	private LobbyPanel lobby = new LobbyPanel();
	private MakeRoomDialog makeRoomDialog = new MakeRoomDialog();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LobbyFrame frame = new LobbyFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LobbyFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //JFrame이 정상적으로 종료되게
		setTitle("Lobby");
		setContentPane(lobby);
		Container c = getContentPane();
		c.setLayout(null);
		
		setSize(1000, 750);
		
		setVisible(true);

	}	
	
	class LobbyPanel extends JPanel{
		private ImageIcon backgroundIcon = new ImageIcon("/Users/mihye/Desktop/2022-2 강의/네트워크프로그래밍/프젝/image/대기방.png");
		private Image backgroundImage  = backgroundIcon.getImage(); //이미지 객체
		
		public LobbyPanel() {
			//방만들기 버튼 
			ImageIcon makeRoomImage = new ImageIcon("/Users/mihye/Desktop/2022-2 강의/네트워크프로그래밍/프젝/image/방만들기.png");
			JButton makeRoomBtn = new JButton(makeRoomImage);
			makeRoomBtn.setSize(180,36);
			makeRoomBtn.setLocation(30,30);
			add(makeRoomBtn);
			
			makeRoomBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                if(e.getSource()==makeRoomBtn) {
	                	makeRoomDialog.setVisible(true);
	                	
	                }	
	            }
	        });
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.drawImage(backgroundImage,0,0,getWidth(),getHeight(), this);
		}
	
		
		
	}

}
