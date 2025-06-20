package db_connection;

import java.util.*;

public class Reservasi {
    private int id;
    private String nim;
    private String kodeMk;
    private Date tanggalReservasi;
    private String status;

    public Reservasi(String nim, String kodeMk) {
        this.nim = nim;
        this.kodeMk = kodeMk;
        this.tanggalReservasi = new Date();
        this.status = "AKTIF";
    }

    // Getters
    public int getId() { return id; }
    public String getNim() { return nim; }
    public String getKodeMk() { return kodeMk; }
    public Date getTanggalReservasi() { return tanggalReservasi; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
}