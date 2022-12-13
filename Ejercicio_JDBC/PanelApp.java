package Ejercicio_JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PanelApp extends JPanel {
    private Connection miConn;
    private JComboBox secciones, paises;
    private JButton consultar;
    private JTextArea consola;
    private JScrollPane consolaScroll;

    public PanelApp(){
        //LAYOUT PRINCIPAL PARA EL PANEL
        setLayout(new BorderLayout());
        //--------------------------------------------------------------------------------------------
        //LOS JCOMBOBOX VAN A IR EN UN PANEL INTERNO PARA DARLES OTRA DISPOSICION Y QUE NO SE SUPERPONGAN
        JPanel panelJComboBox = new JPanel();
        panelJComboBox.setLayout(new FlowLayout());

        //les coloco la opcion por defecto el resto vendran después.
        secciones=new JComboBox<>();
        paises = new JComboBox<>();
        secciones.addItem("TODOS");
        paises.addItem("TODOS");

        panelJComboBox.add(secciones);
        panelJComboBox.add(paises);
//------------------------------------------------------------------------------------------------
        //COLOCAMOS EL RESTO DE ELEMENTOS  Y LOS AÑADIMOS AL PANEL PRINCIPAL CON SU LAYOUT PRINCIPAL
        consola = new JTextArea();
        consola.setEditable(false);
        consolaScroll = new JScrollPane(consola);

        consultar = new JButton("Consultar");

        add(panelJComboBox,BorderLayout.NORTH);
        add(consolaScroll, BorderLayout.CENTER);
        add(consultar, BorderLayout.SOUTH);

        //-------------COMPLETAMOS LOS ITEM DE LOS JCOMBOBOX------------------------------
        try{
            miConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/productos","root","root1234");

            Statement statement = miConn.createStatement();



            ResultSet tablaSecciones = statement.executeQuery("SELECT DISTINCTROW SECCION FROM productos");

            while (tablaSecciones.next()) secciones.addItem(tablaSecciones.getString(1));



            ResultSet tablaPaises = statement.executeQuery("SELECT DISTINCTROW PAIS_DE_ORIGEN FROM productos");

            while (tablaPaises.next()) paises.addItem(tablaPaises.getString(1));


            //--------------------Creamos el evento para el boton consultar con un action listener inerno y anonimo---------------------

            consultar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    consola.setText("");
                    ResultSet tabla = null;
                    try {
                        if(secciones.getSelectedItem().toString()=="TODOS" && paises.getSelectedItem().toString()=="TODOS"){

                                tabla =  statement.executeQuery("SELECT * FROM productos");


                        }
                         else if(secciones.getSelectedItem().toString()=="TODOS" && paises.getSelectedItem().toString()!="TODOS"){

                             tabla = statement.executeQuery("SELECT * FROM productos WHERE PAIS_DE_ORIGEN = '"+paises.getSelectedItem().toString()+"'");

                        }
                        else if(secciones.getSelectedItem().toString()!="TODOS" && paises.getSelectedItem().toString()=="TODOS") {

                              tabla = statement.executeQuery("SELECT * FROM productos WHERE SECCION = '" + secciones.getSelectedItem().toString()+"'");

                        }
                        else if(secciones.getSelectedItem().toString()!="TODOS" && paises.getSelectedItem().toString()!="TODOS"){
                                 tabla = statement.executeQuery("SELECT * FROM productos " +
                                        "WHERE SECCION = '"+secciones.getSelectedItem().toString()+"' AND PAIS_DE_ORIGEN = '"
                                         +paises.getSelectedItem().toString()+"'");

                                }
                        while (tabla.next()) {
                            consola.append(
                                    tabla.getString("CODIGO_ARTICULO")
                                            + "--" + tabla.getString("SECCION")
                                            + "--" + tabla.getString("NOMBRE_ARTICULO")
                                            + "--" + tabla.getString("DESCRIPCION")
                                            + "--" + tabla.getString("PRECIO")
                                            + "--" + tabla.getString("FECHA")
                                            + "--" + tabla.getString("IMPORTADO")
                                            + "--" + tabla.getString("PAIS_DE_ORIGEN") + "\n"
                            );
                        }
                        tabla.close();

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        System.out.println("Error en codigo del boton consultar "+ex.getMessage());;
                    }
                }
            });

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}
