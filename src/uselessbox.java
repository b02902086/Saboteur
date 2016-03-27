import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class uselessbox implements ActionListener{ 
        JFrame demo = new JFrame();
        JCheckBox checkbox = new JCheckBox("JCheckBox");
        JRadioButton radiobutton = new JRadioButton("JRadiobutton");
        JButton button = new JButton("JButton");
        JButton button2 = new JButton(new ImageIcon("test.jpg"));
    public static void main(String[] args) {
        uselessbox box = new uselessbox();
        box.ActionDemo();
    }
    public void ActionDemo(){ 
        demo.setSize(1280, 700);
        demo.setLayout(new GridBagLayout());
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        button2.setBounds(500,100,374,354);
        button2.setActionCommand("foot");
        button2.addActionListener(this);
        GridBagConstraints c0 = new GridBagConstraints();
        JLabel label = new JLabel("JLabel");
        // JTextArea textarea = new JTextArea("JTextArea");
         
        demo.getContentPane().add(checkbox);
        demo.getContentPane().add(radiobutton);
        demo.getContentPane().add(button2,c0);
        demo.getContentPane().add(button);
        // demo.getContentPane().add(BorderLayout.CENTER, textarea);
         
        demo.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == "foot") {
            button.setIcon(new ImageIcon("test.jpg"));

            System.out.println("0.0");
        }
        if(cmd == "JButton"){
            System.out.println("6.6");
        }
    }
}