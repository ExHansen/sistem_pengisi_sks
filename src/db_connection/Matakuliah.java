package db_connection;

public class Matakuliah {
    private String kodeMk;
    private String namaMk;
    private int sks;
    private int semester;
    private String dosen;
    private String hari;
    private String waktu;
    private int kuota;
    private int terisi;

    public Matakuliah(String kodeMk, String namaMk, int sks, int semester,
                      String dosen, String hari, String waktu, int kuota) {
        this.kodeMk = kodeMk;
        this.namaMk = namaMk;
        this.sks = sks;
        this.semester = semester;
        this.dosen = dosen;
        this.hari = hari;
        this.waktu = waktu;
        this.kuota = kuota;
        this.terisi = 0;
    }

    // Getters
    public String getKodeMk() { return kodeMk; }
    public String getNamaMk() { return namaMk; }
    public int getSks() { return sks; }
    public int getSemester() { return semester; }
    public String getDosen() { return dosen; }
    public String getHari() { return hari; }
    public String getWaktu() { return waktu; }
    public int getKuota() { return kuota; }
    public int getTerisi() { return terisi; }

    public void setTerisi(int terisi) { this.terisi = terisi; }
}