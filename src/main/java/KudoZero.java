import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.nimbus.State;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.security.Guard;
import java.sql.*;

public class KudoZero {
    private JLabel titulo;
    private JTextField cNombre;
    private JTextField cArtista;
    private JTextField cGenero;
    private JTextField cProductora;
    private JButton bGuardar;
    private JPanel toor;
    private JTable tabla;
    private JTextField cFiltro;

    public KudoZero() {
        tabla.setModel(Filtrar(cFiltro.getText()));
        bGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Guardar(cNombre.getText(), cArtista.getText(), cGenero.getText(), cProductora.getText());
                tabla.setModel(Filtrar(cFiltro.getText()));
                cNombre.setText("");
                cArtista.setText("");
                cGenero.setText("");
                cProductora.setText("");
            }
        });
        cFiltro.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                tabla.setModel(Filtrar(cFiltro.getText()));
            }
        });
    }

    public static void Guardar(String v1, String v2, String v3, String v4) {
        try {
            Connection nex = DriverManager.getConnection("jdbc:mysql://localhost/radiolandia", "root", "");
            Statement co = nex.createStatement();
            String sql = "INSERT INTO `canciones` (`nombre`, `artista`, `genero`, `productora`) VALUES ('"+v1+"', '"+v2+"', '"+v3+"', '"+v4+"'); ";
            co.execute(sql);
            JOptionPane.showMessageDialog(null, "🎶 Cancion ingresada correctamente!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ No se ha podido ingresar la cancion!");
        }
    }

    public static DefaultTableModel Filtrar(String v) {
        try {
            Connection nex = DriverManager.getConnection("jdbc:mysql://localhost/radiolandia", "root", "");
            Statement co = nex.createStatement();
            String sql;
            if (v.trim().length() > 1) {
                sql = "SELECT * FROM `canciones` WHERE `nombre` LIKE '%"+v+"%'";
            } else {
                sql = "SELECT * FROM `canciones`";
            }
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Nombre");
            model.addColumn("Artista");
            model.addColumn("Genero");
            model.addColumn("Productora");
            ResultSet data = co.executeQuery(sql);

            while (data.next()) {
                Object[] fila = new Object[4];
                for (int i = 0; i < 4; i++) {
                    fila[i] = data.getObject(i + 1);
                }
                model.addRow(fila);
            }
            return model;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        JFrame root = new JFrame("Radiolandia");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        root.setContentPane(new KudoZero().toor);
        root.setResizable(false);
        root.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        root.pack();
        root.setVisible(true);
    }
}
