package com.iot.iot;

import java.util.Calendar;

public class kondisiLampu {
    private boolean statusLampu;
    private int stampTahun, stampBulan, stampTanggal, stampJam, stampMenit, stampDetik;

    public kondisiLampu(){

    }

    public kondisiLampu(boolean statusLampu, int stampTahun, int stampBulan, int stampTanggal, int stampJam, int stampMenit, int stampDetik) {
        this.statusLampu = statusLampu;
        this.stampTahun = stampTahun;
        this.stampBulan = stampBulan;
        this.stampTanggal = stampTanggal;
        this.stampJam = stampJam;
        this.stampMenit = stampMenit;
        this.stampDetik = stampDetik;
    }

    public boolean isStatusLampu() {
        return statusLampu;
    }

    public void setStatusLampu(boolean statusLampu) {
        this.statusLampu = statusLampu;
    }

    public int getStampTahun() {
        return stampTahun;
    }

    public void setStampTahun(int stampTahun) {
        this.stampTahun = stampTahun;
    }

    public int getStampBulan() {
        return stampBulan;
    }

    public void setStampBulan(int stampBulan) {
        this.stampBulan = stampBulan;
    }

    public int getStampTanggal() {
        return stampTanggal;
    }

    public void setStampTanggal(int stampTanggal) {
        this.stampTanggal = stampTanggal;
    }

    public int getStampJam() {
        return stampJam;
    }

    public void setStampJam(int stampJam) {
        this.stampJam = stampJam;
    }

    public int getStampMenit() {
        return stampMenit;
    }

    public void setStampMenit(int stampMenit) {
        this.stampMenit = stampMenit;
    }

    public int getStampDetik() {
        return stampDetik;
    }

    public void setStampDetik(int stampDetik) {
        this.stampDetik = stampDetik;
    }
}
