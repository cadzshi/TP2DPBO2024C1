public class Mahasiswa {
    private String nim;
    private String nama;
    private String jenisKelamin;
    private String peminatan;

    public Mahasiswa(String nim, String nama, String jenisKelamin, String peminatan) {
        this.nim = nim;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.peminatan = peminatan;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
    public void setPeminatan(String peminatan) {
        this.peminatan = peminatan;
    }

    public String getNim() {
        return this.nim;
    }

    public String getNama() {
        return this.nama;
    }

    public String getJenisKelamin() {
        return this.jenisKelamin;
    }
    public String getPeminatan() {
        return this.peminatan;
    }
}