package com.mycompany.iniciodesesion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CambiarContraseña extends JFrame{
    private JPasswordField nuevaContraseñaField;
    private JPasswordField confirmarContraseñaField;
    private JButton cambiarButton;
    private JButton volverButton;
    private String correo;

    public CambiarContraseña(String correo) {
        this.correo = correo;
        initComponents();
    }

    private void initComponents() {
        setTitle("Cambiar Contraseña - Empresa de Paquetería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 130);
        setLocationRelativeTo(null);

        nuevaContraseñaField = new JPasswordField(20);
        confirmarContraseñaField = new JPasswordField(20);
        cambiarButton = new JButton("Cambiar Contraseña");
        volverButton = new JButton("Volver al Inicio de Sesión");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Nueva Contraseña:"));
        panel.add(nuevaContraseñaField);
        panel.add(new JLabel("Confirmar Contraseña:"));
        panel.add(confirmarContraseñaField);
        panel.add(cambiarButton);
        panel.add(volverButton);

        cambiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarContraseña();
            }
        });

        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new InicioSesion().setVisible(true);
            }
        });

        add(panel);
    }

    private void cambiarContraseña() {
        String nuevaContraseña = new String(nuevaContraseñaField.getPassword());
        String confirmarContraseña = new String(confirmarContraseñaField.getPassword());

        if (nuevaContraseña.isEmpty() || confirmarContraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son requeridos.");
            return;
        }

        if (!nuevaContraseña.equals(confirmarContraseña)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
            return;
        }

        boolean cambiada = EmpleadoService.cambiarContraseña(correo, nuevaContraseña);
        if (cambiada) {
            JOptionPane.showMessageDialog(this, "Contraseña cambiada exitosamente.");
            dispose();
            new InicioSesion().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Error al cambiar la contraseña.");
        }
    }
}
