package com.example.readrssapp.DTO;

public class User {
    private String maUser;
    private String hoTen;
    private String matKhau;

    public User(String maUser, String hoTen, String matKhau) {
        this.maUser = maUser;
        this.hoTen = hoTen;
        this.matKhau = matKhau;
    }

    public User() {
    }

    public String getMaUser() {
        return maUser;
    }

    public void setMaUser(String maUser) {
        this.maUser = maUser;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    @Override
    public String toString() {
        return "User{" +
                "maUser='" + maUser + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", matKhau='" + matKhau + '\'' +
                '}';
    }
}
