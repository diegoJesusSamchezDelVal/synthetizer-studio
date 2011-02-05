package implement;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JFrame;

/**
 * La fenêtre principale de l'application.
 * */
public class MainWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ResourceBundle res = ResourceBundle.getBundle("lang");
	
	public MainWindow() {
		super();
		setTitle(res.getString("app_name"));
		setLayout(new BorderLayout());
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

}
