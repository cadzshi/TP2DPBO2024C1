import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;

public class Menu extends JFrame{
    public static void main(String[] args) {
        // buat object window
        Menu window = new Menu();

        // atur ukuran window
        window.setSize(480, 560);
        // letakkan window di tengah layar
        window.setLocationRelativeTo(null);
        // isi window
        window.setContentPane(window.mainPanel);
        // ubah warna background
        window.getContentPane().setBackground(Color.white);
        // tampilkan window
        window.setVisible(true);
        // agar program ikut berhenti saat window diclose
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;
    private Database database;

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox jenisKelaminComboBox;
    private JComboBox peminatanComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JLabel peminatanLabel;
    private JComboBox comboBox1;

    // constructor
    public Menu() {
        // inisialisasi listMahasiswa
        listMahasiswa = new ArrayList<>();

        // buat objek database
        database = new Database();

        // isi tabel mahasiswa
        mahasiswaTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] jenisKelaminData = {"", "Laki-laki", "Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel(jenisKelaminData));

        String[] peminatanData = {"", "RPL", "SI", "Data", "Multimedia", "Jaringan"};
        peminatanComboBox.setModel(new DefaultComboBoxModel(peminatanData));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex >= 0) {
                    deleteData();
                }
            }
        });
        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // saat tombol
                clearForm();
            }
        });
        // saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();

                // simpan value textfield dan combo box
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex, 3).toString();
                String selectedPeminatan = mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString();

                // ubah isi textfield dan combo box
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);
                peminatanComboBox.setSelectedItem(selectedPeminatan);

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");
                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] column = {"No", "NIM", "Nama", "Jenis Kelamin", "Peminatan"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, column);

        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM mahasiswa");

            int i = 0;
            while (resultSet.next()) {
                Object[] row = new Object[5];

                row[0] = i + 1;
                row[1] = resultSet.getString("nim");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("jenis_kelamin");
                row[4] = resultSet.getString("peminatan");

                temp.addRow(row);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return temp;
        }
        public void insertData () {
            // ambil value dari textfield dan combobox
            String nim = nimField.getText();
            String nama = namaField.getText();
            String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
            String peminatan = peminatanComboBox.getSelectedItem().toString();

//            String checkQuery = "SELECT * FROM mahasiswa WHERE nim = '" + nim + "'";
//            database.insertUpdateDeleteQuery(checkQuery);


            if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty() || peminatan.isEmpty()){
                JOptionPane.showMessageDialog(null, "Data tidak boleh kosong!");
            } else {
                //tambah data ke database
                String sql = "INSERT INTO mahasiswa VALUES (null, '" + nim + "', '" + nama + "', '" + jenisKelamin + "', '" + peminatan + "');";
                database.insertUpdateDeleteQuery(sql);

                // update tabel
                mahasiswaTable.setModel(setTable());

                // bersihkan form
                clearForm();

                // feedback
                System.out.println("Insert berhasil!");
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
            }
        }

        public void updateData () {
            // Ambil data dari form input
            String nim = nimField.getText();
            String nama = namaField.getText();
            String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
            String peminatan = peminatanComboBox.getSelectedItem().toString();

            if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty() || peminatan.isEmpty()){
                JOptionPane.showMessageDialog(null, "Data tidak boleh kosong!");
            } else {
                // Buat string SQL UPDATE
                String sql = "UPDATE mahasiswa SET nim = '" + nim + "', nama = '" + nama + "', jenis_kelamin = '" + jenisKelamin + "', peminatan = '" + peminatan + "' WHERE nim = '" + nim + "'";

                database.insertUpdateDeleteQuery(sql);
                // update tabel
                mahasiswaTable.setModel(setTable());

                // bersihkan form
                clearForm();

                // feedback
                System.out.println("Update Berhasil!");
                JOptionPane.showMessageDialog(null, "Data berhaasil diubah!");
            }
        }

        public void deleteData () {
            // Tampilkan dialog konfirmasi
            int option = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            // Periksa pilihan pengguna
            if (option == JOptionPane.YES_OPTION) {
                /// Ambil nim dari baris yang dipilih
                String nim = mahasiswaTable.getValueAt(selectedIndex, 1).toString();

                // Buat string SQL DELETE
                String sql = "DELETE FROM mahasiswa WHERE nim = '" + nim + "'";
                database.insertUpdateDeleteQuery(sql);

                // update tabel
                mahasiswaTable.setModel(setTable());

                // Bersihkan form
                clearForm();

                // Feedback
                System.out.println("Delete berhasil!");
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
            }
        }

        public void clearForm () {
            // kosongkan semua texfield dan combo box
            nimField.setText("");
            namaField.setText("");
            jenisKelaminComboBox.setSelectedItem("");
            peminatanComboBox.setSelectedItem("");

            // ubah button "Update" menjadi "Add"
            addUpdateButton.setText("Add");
            // sembunyikan button delete
            deleteButton.setVisible(false);
            // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
            selectedIndex = -1;
        }


}
