package com.mycompany.iniciodesesion;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.text.MaskFormatter;
import org.bson.Document;

public class RegistroEmpleado extends JFrame{
    private JTextField nombreField;
    private JTextField cedulaField;
    private JComboBox<String> cargoBox;
    private JTextField fechaNacimientoField;
    private JButton registrarButton;
    private JButton volverButton;

    public RegistroEmpleado() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Registro de Empleado - Empresa de Paquetería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250, 250);
        setLocationRelativeTo(null);

        nombreField = new JTextField(20);
        cedulaField = new JTextField(20);
        
        String[] cargos = {"Administrador", "Conductor", "Recepcionista", "Vendedor"};
        cargoBox = new JComboBox<>(cargos);
        fechaNacimientoField = new JTextField(20);
        registrarButton = new JButton("Registrar");
        volverButton = new JButton("Canselar");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Cédula:"));
        panel.add(cedulaField);
        panel.add(new JLabel("Cargo:"));
        panel.add(cargoBox);
        panel.add(new JLabel("Fecha de Nacimiento:"));
        panel.add(fechaNacimientoField);
        panel.add(registrarButton);
        panel.add(volverButton);

        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarEmpleado();
            }
        });
        
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverInicioSesion();
            }
        });

        add(panel);
    }

    private void registrarEmpleado() {
        // Verificar que todos los campos estén llenos
        if (nombreField.getText().isEmpty() || cedulaField.getText().isEmpty()||
                fechaNacimientoField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son requeridos.");
            return;
        }
        
        // Validar que la cédula tenga exactamente 10 dígitos
        String cedula = cedulaField.getText();
        if (!cedula.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "La cédula debe contener exactamente 10 dígitos.");
            return;
        }
        
        if (EmpleadoService.verificarCedula(cedula)) {
            JOptionPane.showMessageDialog(this, "La cédula ya está registrada.");
            return;
        }
        
        String fechaNacimiento = fechaNacimientoField.getText();
        if (!isValidDate(fechaNacimiento)) {
            JOptionPane.showMessageDialog(this, "La fecha de nacimiento debe tener el formato dd/MM/yyyy y ser válida.");
            return;
        }
        Empleado empleado = new Empleado();
        empleado.setNombre(nombreField.getText());
        empleado.setCedula(cedulaField.getText());
        empleado.setCargo((String) cargoBox.getSelectedItem());
        empleado.setFechaNacimiento(fechaNacimientoField.getText());

        EmpleadoService.agregarEmpleado(empleado);
        mostrarInformacionEmpleado();
        // Limpiar los campos
        nombreField.setText("");
        cedulaField.setText("");
        cargoBox.setSelectedItem("");
        fechaNacimientoField.setText("");
    }
    
    private void mostrarInformacionEmpleado(){
        String cedula = cedulaField.getText();
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> collection = database.getCollection("empleados");

        Document query = new Document("cedula", cedula);
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

            JOptionPane.showMessageDialog(this, 
                    "=========================================\n"
                    +"      Empleado registrado exitosamente"
                    +"\n========================================="
                    +"\n   IdEmpleado   :   "+empleado.getIdEmpleado()
                    +"\n   Usuario       :   "+empleado.getUsuario()
                    +"\n   Correo        :   "+empleado.getCorreo()
                    +"\n   Contraseña   :   "+empleado.getContraseña()
                    +"\n=========================================");
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
        }
    }
    private void volverInicioSesion() {
        // Cerrar la ventana actual y mostrar la ventana de inicio de sesión
        dispose();
        new InicioSesion().setVisible(true);
    }

    private boolean isValidDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
