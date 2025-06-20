package db_connection;

public class Mahasiswa {
    private String nim;
    private String nama;
    private String email;
    private String password;
    private int semester;
    private double ipk;
    private int maxSks;

    // Constructor
    public Mahasiswa(String nim, String nama, String email, String password, int semester, double ipk) {
        this.nim = nim;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.semester = semester;
        this.ipk = ipk;
        this.maxSks = calculateMaxSks(ipk);
    }

    private int calculateMaxSks(double ipk) {
        if (ipk >= 3.5) return 24;
        else if (ipk >= 3.0) return 21;
        else if (ipk >= 2.5) return 18;
        else return 15;
    }

    // Getters and Setters
    public String getNim() { return nim; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public int getSemester() { return semester; }
    public double getIpk() { return ipk; }
    public int getMaxSks() { return maxSks; }

    public void setNama(String nama) { this.nama = nama; }
    public void setEmail(String email) { this.email = email; }
    public void setSemester(int semester) { this.semester = semester; }
    public void setIpk(double ipk) {
        this.ipk = ipk;
        this.maxSks = calculateMaxSks(ipk);
    }
}