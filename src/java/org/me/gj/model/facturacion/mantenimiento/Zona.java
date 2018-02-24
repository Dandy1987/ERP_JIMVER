package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;

public class Zona {

	private long zon_key;
	private String zon_id;
	private int suc_id;
	private int emp_id;
	private String zon_des;
	private int zon_est;
	private String zon_idven;
	private int zon_dvis;
	private String zon_dvisdes;
	private String zon_rutid;
	private int zon_ord;
	private int zon_canid;
	private int zon_mesid;
	private String zon_ubiid;
	private String zon_paisid;
	private String zon_nomrep;
	private String zon_usuadd;
	private Date zon_fecadd;
	private String zon_usumod;
	private Date zon_fecmod;
	private boolean valor;
	private String zon_apenom;
	private String zon_candes;
	private String zon_ubides;
	private String zon_paisdes;
	private int zon_postid;
	private String zon_postdes;

	public Zona() {
	}

	public Zona(long zon_key, String zon_id, int suc_id, int emp_id,
			String zon_des, int zon_est, String zon_idven, int zon_dvis,
			String zon_rutid, int zon_ord, int zon_canid, int zon_mesid,
			String zon_ubiid, String zon_paisid, String zon_nomrep,
			String zon_usuadd, String zon_usumod) {
		this.zon_key = zon_key;
		this.zon_id = zon_id;
		this.suc_id = suc_id;
		this.emp_id = emp_id;
		this.zon_des = zon_des;
		this.zon_est = zon_est;
		this.zon_idven = zon_idven;
		this.zon_dvis = zon_dvis;
		this.zon_rutid = zon_rutid;
		this.zon_ord = zon_ord;
		this.zon_canid = zon_canid;
		this.zon_mesid = zon_mesid;
		this.zon_ubiid = zon_ubiid;
		this.zon_paisid = zon_paisid;
		this.zon_nomrep = zon_nomrep;
		this.zon_usuadd = zon_usuadd;
		this.zon_usumod = zon_usumod;
	}

	public long getZon_key() {
		return zon_key;
	}

	public void setZon_key(long zon_key) {
		this.zon_key = zon_key;
	}

	public String getZon_id() {
		return zon_id;
	}

	public void setZon_id(String zon_id) {
		this.zon_id = zon_id;
	}

	public int getSuc_id() {
		return suc_id;
	}

	public void setSuc_id(int suc_id) {
		this.suc_id = suc_id;
	}

	public int getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

	public String getZon_des() {
		return zon_des;
	}

	public void setZon_des(String zon_des) {
		this.zon_des = zon_des;
	}

	public int getZon_est() {
		return zon_est;
	}

	public void setZon_est(int zon_est) {
		this.zon_est = zon_est;
	}

	public String getZon_idven() {
		return zon_idven;
	}

	public void setZon_idven(String zon_idven) {
		this.zon_idven = zon_idven;
	}

	public int getZon_dvis() {
		return zon_dvis;
	}

	public void setZon_dvis(int zon_dvis) {
		this.zon_dvis = zon_dvis;
	}

	public String getZon_rutid() {
		return zon_rutid;
	}

	public void setZon_rutid(String zon_rutid) {
		this.zon_rutid = zon_rutid;
	}

	public int getZon_ord() {
		return zon_ord;
	}

	public void setZon_ord(int zon_ord) {
		this.zon_ord = zon_ord;
	}

	public int getZon_canid() {
		return zon_canid;
	}

	public void setZon_canid(int zon_canid) {
		this.zon_canid = zon_canid;
	}

	public int getZon_mesid() {
		return zon_mesid;
	}

	public void setZon_mesid(int zon_mesid) {
		this.zon_mesid = zon_mesid;
	}

	public String getZon_ubiid() {
		return zon_ubiid;
	}

	public void setZon_ubiid(String zon_ubiid) {
		this.zon_ubiid = zon_ubiid;
	}

	public String getZon_paisid() {
		return zon_paisid;
	}

	public void setZon_paisid(String zon_paisid) {
		this.zon_paisid = zon_paisid;
	}

	public String getZon_nomrep() {
		return zon_nomrep;
	}

	public void setZon_nomrep(String zon_nomrep) {
		this.zon_nomrep = zon_nomrep;
	}

	public String getZon_usuadd() {
		return zon_usuadd;
	}

	public void setZon_usuadd(String zon_usuadd) {
		this.zon_usuadd = zon_usuadd;
	}

	public Date getZon_fecadd() {
		return zon_fecadd;
	}

	public void setZon_fecadd(Date zon_fecadd) {
		this.zon_fecadd = zon_fecadd;
	}

	public String getZon_usumod() {
		return zon_usumod;
	}

	public void setZon_usumod(String zon_usumod) {
		this.zon_usumod = zon_usumod;
	}

	public Date getZon_fecmod() {
		return zon_fecmod;
	}

	public void setZon_fecmod(Date zon_fecmod) {
		this.zon_fecmod = zon_fecmod;
	}

	public boolean isValor() {
		if (zon_est == 1) {
			valor = true;
		} else {
			valor = false;
		}
		return valor;
	}

	public void setValor(boolean valor) {
		this.valor = valor;
	}

	public String getZon_apenom() {
		return zon_apenom;
	}

	public void setZon_apenom(String zon_apenom) {
		this.zon_apenom = zon_apenom;
	}

	public String getZon_candes() {
		return zon_candes;
	}

	public void setZon_candes(String zon_candes) {
		this.zon_candes = zon_candes;
	}

	public String getZon_ubides() {
		return zon_ubides;
	}

	public void setZon_ubides(String zon_ubides) {
		this.zon_ubides = zon_ubides;
	}

	public String getZon_paisdes() {
		return zon_paisdes;
	}

	public void setZon_paisdes(String zon_paisdes) {
		this.zon_paisdes = zon_paisdes;
	}

	public int getZon_postid() {
		return zon_postid;
	}

	public void setZon_postid(int zon_postid) {
		this.zon_postid = zon_postid;
	}

	public String getZon_postdes() {
		return zon_postdes;
	}

	public void setZon_postdes(String zon_postdes) {
		this.zon_postdes = zon_postdes;
	}

	public String getZon_dvisdes() {
		return zon_dvisdes;
	}

	public void setZon_dvisdes(String zon_dvisdes) {
		this.zon_dvisdes = zon_dvisdes;
	}

}
