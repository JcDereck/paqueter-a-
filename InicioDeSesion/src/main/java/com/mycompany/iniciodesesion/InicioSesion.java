package com.mycompany.iniciodesesion;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InicioSesion extends JFrame{
    private JTextField usuarioField;
    private JPasswordField contraseñaField;
    private JButton iniciarSesionButton;
    private JButton registroButton;
    private JButton olvidéContraseñaButton; // Nuevo botón para "Olvidé mi contraseña"

    public InicioSesion() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Inicio de Sesión - Empresa de Paquetería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(570, 100);
        setLocationRelativeTo(null);

        usuarioField = new JTextField(20);
        contraseñaField = new JPasswordField(20);
        iniciarSesionButton = new JButton("Iniciar Sesión");
        registroButton = new JButton("Registrarse");
        olvidéContraseñaButton = new JButton("Olvidé mi contraseña"); // Nuevo botón

        JPanel panel = new JPanel();
        panel.add(new JLabel("Usuario:"));
        panel.add(usuarioField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(contraseñaField);
        panel.add(iniciarSesionButton);
        panel.add(registroButton);
        panel.add(olvidéContraseñaButton); // Agregamos el nuevo botón al panel

        iniciarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para iniciar sesión
                autenticarUsuario();
            }
        });

        registroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegistroEmpleado().setVisible(true);
            }
        });

        olvidéContraseñaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new OlvidéContraseña().setVisible(true);
            }
        });

        add(panel);
    }

    private void autenticarUsuario() {
        String usuario = usuarioField.getText();
        String contraseña = new String(contraseñaField.getPassword());

        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> collection = database.getCollection("empleados");

        Document query = new Document("usuario", usuario).append("contraseña", contraseña);
        Document empleadoDoc = collection.find(query).first();

        if (empleadoDoc != null) {
            Empleado empleado = new Empleado();
            empleado.setIdEmpleado(empleadoDoc.getString("idEmpleado"));
            empleado.setUsuario(empleadoDoc.getString("usuario"));
            empleado.setNombre(empleadoDoc.getString("nombre"));
            empleado.setCedula(empleadoDoc.getString("cedula"));
            empleado.setCargo(empleadoDoc.getString("cargo"));
            empleado.setFechaNacimiento(empleadoDoc.getString("fechaNacimiento"));
            empleado.setCorreo(empleadoDoc.getString("correo"));
            empleado.setContraseña(empleadoDoc.getString("contraseña"));

            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso. Bienvenido " + empleado.getNombre());
            // Aquí puedes redirigir al empleado a la siguiente ventana o funcionalidad de la aplicación
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
        }
    }

    private void abrirRegistroEmpleado() {
        new RegistroEmpleado().setVisible(true);
        dispose();
    }
}
