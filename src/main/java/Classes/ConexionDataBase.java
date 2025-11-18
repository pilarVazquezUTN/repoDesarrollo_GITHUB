package Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class ConexionDataBase {

        // 1. CONFIGURACIÓN DE ACCESO
        //  Reemplaza estos valores con tus datos reales de PostgreSQL

        private static final String DRIVER = "org.postgresql.Driver";
        private static final String URL = "jdbc:postgresql://localhost:5432/ERNE";
        private static final String USER = "pili"; // o tu usuario
        private static final String PASSWORD = "pili123";

        /**
         * Establece y retorna una nueva conexión a la base de datos PostgreSQL.
         * @return Objeto Connection si la conexión es exitosa, o null si falla.
         */
        public static Connection getConnection() {
            Connection connection = null;
            try {
                // Cargar la clase del driver
                Class.forName(DRIVER);

                // 2. ESTABLECER LA CONEXIÓN
                connection = DriverManager.getConnection(URL, USER, PASSWORD);


            } catch (ClassNotFoundException e) {
                System.err.println(" Driver de PostgreSQL no encontrado.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("ERROR al conectar con la base de datos (revisa URL, usuario y contraseña):");
                System.err.println("Mensaje SQL: " + e.getMessage());
                e.printStackTrace();
            }
            return connection;
        }
    }

