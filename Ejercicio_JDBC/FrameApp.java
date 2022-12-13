package Ejercicio_JDBC;

import javax.swing.*;

public class FrameApp extends JFrame {
    public FrameApp(){
        setTitle("QueryApp");
        setBounds(50,50,600,500);
        add(new PanelApp());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
