import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame extends JFrame {
private static final double WIDTH=0.6,HEIGHT=0.7;
private GraphPanel graphPanel=new GraphPanel();

	public Frame() {
	
		setTitle("SNMP");
		
		confFrame();
		setLayout(new BorderLayout());
		left();
		
		add(graphPanel,BorderLayout.CENTER);
		setVisible(true);
		
	}
	
	public void confFrame() {		
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				
			}
		setSize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void setSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int appWidth = (int)(WIDTH * screenSize.width);
		int appHeight = (int)(HEIGHT * screenSize.height);
		setPreferredSize(new Dimension(appWidth, appHeight));
		pack();
	}
	
	public void left() {
		final JPanel panel=new JPanel();
		final DefaultListModel<String> l1 = new DefaultListModel<>();  
        l1.addElement("R1 FastEthernet1/0");  
        l1.addElement("R1 FastEthernet2/0");
        l1.addElement("R1 FastEthernet0/0");
        l1.addElement("R1 FastEthernet0/1");
        l1.addElement("R1 Null0");
        l1.addElement("R1 Loopback0");
        
        l1.addElement("R2 FastEthernet1/0");  
        l1.addElement("R2 FastEthernet2/0");
        l1.addElement("R2 FastEthernet3/0");
        l1.addElement("R2 FastEthernet4/0");
        l1.addElement("R2 FastEthernet0/0");
        l1.addElement("R2 Serial0/0");
        l1.addElement("R2 FastEthernet0/1");
        l1.addElement("R2 Serial0/1");
        l1.addElement("R2 Serial0/2");
        l1.addElement("R2 Serial0/3");
        l1.addElement("R2 Null0");
        l1.addElement("R2 Loopback0");
        
        l1.addElement("R3 FastEthernet1/0");  
        l1.addElement("R3 FastEthernet2/0");
        l1.addElement("R3 FastEthernet3/0");
        l1.addElement("R3 FastEthernet4/0");
        l1.addElement("R3 FastEthernet0/0");
        l1.addElement("R3 Serial0/0");
        l1.addElement("R3 FastEthernet0/1");
        l1.addElement("R3 Serial0/1");
        l1.addElement("R3 Serial0/2");
        l1.addElement("R3 Serial0/3");
        l1.addElement("R3 Null0");
        l1.addElement("R3 Loopback0");
		
		JList list = new JList(l1);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		
		
		JScrollPane scrollPane = new JScrollPane(list);
		
		JButton choose=new JButton("Choose");
		choose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s =(String) list.getSelectedValue();
				String router=s.substring(0, 2);
				String port=s.substring(3);
				graphPanel.change(router,port);
			}
			
		});
		panel.setLayout(new BorderLayout());
		panel.add(scrollPane,BorderLayout.CENTER);
		panel.add(choose,BorderLayout.SOUTH);
		add(panel,BorderLayout.WEST);
	}
}
