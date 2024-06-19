package com.mycompany.iniciodesesion;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Random;

public class EmpleadoService {
    private static MongoDatabase database;

    static {
        database = MongoDBConnection.getDatabase();
    }
    
    public static void agregarEmpleado(Empleado empleado) {
        String idEmpleado = generarIdEmpleado(empleado.getCedula());
        String usuario = generarUsuario(empleado.getNombre());
        String correo = usuario + "@expressJet.com";
        String contrasenia =  generarContraseña();

        empleado.setIdEmpleado(idEmpleado);
        empleado.setUsuario(usuario);
        empleado.setCorreo(correo);
        empleado.setContraseña(contrasenia);
        
        MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("empleados");
        Document doc = new Document("idEmpleado", empleado.getIdEmpleado())
              .append("usuario", empleado.getUsuario())
              .append("nombre", empleado.getNombre())
              .append("cedula", empleado.getCedula())
              .append("cargo", empleado.getCargo())
              .append("fechaNacimiento", empleado.getFechaNacimiento())
              .append("correo", empleado.getCorreo())
              .append("contraseña", empleado.getContraseña());

        collection.insertOne(doc);
    }

    private static String generarIdEmpleado(String cedula) {
        return cedula.substring(cedula.length() - 4);
    }

    private static String generarUsuario(String nombre) {
        String baseUsuario = nombre.toLowerCase().replaceAll("\\s", "");
        Random rand = new Random();
        int numero = rand.nextInt(100); // Genera un número entre 0 y 99
        return baseUsuario + numero;
    }
    
    public static boolean verificarCorreo(String correo) {
        MongoCollection<Document> collection = database.getCollection("empleados");
        Document empleadoDoc = collection.find(Filters.eq("correo", correo)).first();
        return empleadoDoc != null;
    }

    public static boolean cambiarContraseña(String correo, String nuevaContraseña) {
        MongoCollection<Document> collection = database.getCollection("empleados");
        Document empleadoDoc = collection.find(Filters.eq("correo", correo)).first();

        if (empleadoDoc != null) {
            collection.updateOne(Filters.eq("correo", correo), new Document("$set", new Document("contraseña", nuevaContraseña)));
            return true;
        } else {
            return false;
        }
    }
    
    /*public static boolean enviarCorreoRestablecimiento(String correo) {
        MongoCollection<Document> collection = database.getCollection("empleados");
        Document empleadoDoc = collection.find(Filters.eq("correo", correo)).first();

        if (empleadoDoc != null) {
            String nuevaContraseña = generarNuevaContraseña();
            collection.updateOne(Filters.eq("correo", correo), new Document("$set", new Document("contraseña", nuevaContraseña)));

            // Aquí deberías enviar un correo electrónico al usuario con la nueva contraseña.
            // Este ejemplo simplemente muestra un cuadro de diálogo.
            JOptionPane.showMessageDialog(null, "Nueva contraseña: " + nuevaContraseña);

            return true;
        } else {
            return false;
        }
    }*/
    public static boolean verificarCedula(String cedula) {
        MongoCollection<Document> collection = database.getCollection("empleados");
        Document empleadoDoc = collection.find(Filters.eq("cedula", cedula)).first();
        return empleadoDoc != null;
    }
    
    private static String generarContraseña() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder contraseña = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            contraseña.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return contraseña.toString();
    }
}
