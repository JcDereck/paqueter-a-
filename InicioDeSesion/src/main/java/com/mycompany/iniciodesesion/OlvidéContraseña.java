package com.mycompany.iniciodesesion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OlvidéContraseña extends JFrame{
    private JTextField correoField;
    private JButton enviarButton;
    private JButton volverButton;

    public OlvidéContraseña() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Restablecer Contraseña - Empresa de Paquetería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(280, 100);
        setLocationRelativeTo(null);

        correoField = new JTextField(20);
        enviarButton = new JButton("Enviar");
        volverButton = new JButton("Volver al Inicio de Sesión");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Correo:"));
        panel.add(correoField);
        panel.add(enviarButton);
        panel.add(volverButton);

        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarRestablecimientoContraseña();
            }
        });
        
        /*enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarRestablecimiento();
            }
        });*/

        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new InicioSesion().setVisible(true);
            }
        });

        add(panel);
    }
    private void enviarRestablecimientoContraseña() {
        String correo = correoField.getText();
        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo de correo no puede estar vacío.");
            return;
        }

        boolean encontrado = EmpleadoService.verificarCorreo(correo);
        if (encontrado) {
            JOptionPane.showMessageDialog(this, "Correo verificado. Proceda a cambiar la contraseña.");
            new CambiarContraseña(correo).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un empleado con ese correo.");
        }
    }
    
    /*private void enviarRestablecimiento() {
        String correo = correoField.getText();
        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo de correo no puede estar vacío.");
            return;
        }

        boolean enviado = EmpleadoService.enviarCorreoRestablecimiento(correo);
        if (enviado) {
            JOptionPane.showMessageDialog(this, "Correo de restablecimiento enviado.");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un empleado con ese correo.");
        }
    }*/
}
