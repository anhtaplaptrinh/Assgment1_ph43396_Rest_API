package com.example.assgment_ph43396_rest_api_and;

public class SanphamModel {
    private  String _id;
    private  String ten;
    private String anh;
    private  double gia;
    private  String  soluong;

    public SanphamModel(String anh, double gia, String soluong) {
        this.anh = anh;
        this.gia = gia;
        this.soluong = soluong;
    }

    public SanphamModel(String ten, String anh, double gia, String soluong) {
        this.ten = ten;
        this.anh = anh;
        this.gia = gia;
        this.soluong = soluong;
    }

    public SanphamModel(String _id, String ten, String anh, double gia, String soluong) {
        this._id = _id;
        this.ten = ten;
        this.anh = anh;
        this.gia = gia;
        this.soluong = soluong;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }
}
