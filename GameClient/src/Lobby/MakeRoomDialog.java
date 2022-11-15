package Lobby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class MakeRoomDialog extends JDialog {

	private MakeRoomPanel makeRoomPanel = new MakeRoomPanel();
	private ImageIcon backgroundIcon = new ImageIcon("/Users/mihye/Desktop/2022-2 강의/네트워크프로그래밍/프젝/image/makeRoom.png");
	private Image backgroundImage  = backgroundIcon.getImage(); //이미지 객체
	private JPasswordField roomPassword;
	private JTextField roomTitle;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MakeRoomDialog dialog = new MakeRoomDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MakeRoomDialog() {
		setBounds(100, 100, 450, 350);
		getContentPane().setLayout(new BorderLayout());
		makeRoomPanel.setLayout(null);
		makeRoomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(makeRoomPanel);
		
		
	}
	
	class MakeRoomPanel extends JPanel{
		private ImageIcon backgroundIcon = new ImageIcon("/Users/mihye/Desktop/2022-2 강의/네트워크프로그래밍/프젝/image/makeRoom.png");
		private Image backgroundImage  = backgroundIcon.getImage(); //이미지 객체
		
		public MakeRoomPanel() {
			
			//방 제목 입력 
			roomTitle = new JTextField();
			roomTitle.setBounds(149, 100, 282, 20);
			add(roomTitle);
			
			//방 비밀번호 설정 체크박스
			JCheckBox check = new JCheckBox("");
			check.setBounds(150, 149, 282, 20);
			add(check);
			
			Color enableColor =new Color(225,225,225); 
			
			check.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                if(e.getSource()==check) {
	                	if(roomPassword.getBackground()==enableColor){
		                	roomPassword.setEnabled(true);
		        			roomPassword.setBackground(Color.white);
						}else {
							roomPassword.setEnabled(false);
		        			roomPassword.setBackground(enableColor);
						}
	                	
	                }	
	            }
	        });
			
			//방 비밀번호 입력 
			roomPassword = new JPasswordField(10);
			roomPassword.setBounds(149, 180, 200, 30);
			roomPassword.setEnabled(false);
			roomPassword.setBackground(enableColor);
			roomPassword.setEchoChar('*');
			add(roomPassword);
			
			//확인 버튼 
			ImageIcon okBtnImage = new ImageIcon("/Users/mihye/Desktop/2022-2 강의/네트워크프로그래밍/프젝/image/확인.png");
			JButton okBtn = new JButton(okBtnImage);
			okBtn.setBounds(95, 260, 110, 40);
			add(okBtn);
			
			okBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                if(e.getSource()==okBtn) {
	                	System.out.println("Title: "+roomTitle.getText()+", Password: "+roomPassword.getText());
	                	MakeRoomDialog.this.dispose();
	                }	
	            }
	        });
			
			//취소 버튼 
			ImageIcon cancelBtnImage = new ImageIcon("/Users/mihye/Desktop/2022-2 강의/네트워크프로그래밍/프젝/image/취소.png");
			JButton cancelBtn = new JButton(cancelBtnImage);
			cancelBtn.setBounds(235, 260, 110, 40);
			add(cancelBtn);
			
			cancelBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                if(e.getSource()==cancelBtn) {
	                	MakeRoomDialog.this.dispose();
	                	
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
