package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;

public class ProvPresupuesto {

	private int provpresu_key;
	private String provpresu_id;
	private String provpresu_des;
	private String provpresu_desabre;
	private int provpresu_est;
	private int provpresu_provkey;
	private String provpresu_provid;

	private String provpresu_provdes;
	private boolean valor;
	private String provpresu_usuadd;
	private Date provpresu_fecadd;
	private String provpresu_usumod;
	private Date provpresu_fecmod;

	public ProvPresupuesto() {
	}

	public ProvPresupuesto(int provpresu_key,  String provpresu_des,
			String provpresu_desabre, int provpresu_provkey, int provpresu_est,
			String provpresu_usuadd, String provpresu_usumod) {
		this.provpresu_key = provpresu_key;
		this.provpresu_des = provpresu_des;
		this.provpresu_desabre = provpresu_desabre;
		this.provpresu_provkey = provpresu_provkey;
		
		this.provpresu_est = provpresu_est;
		this.provpresu_usuadd = provpresu_usuadd;
		this.provpresu_usumod = provpresu_usumod;

	}

	public int getProvpresu_key() {
		return provpresu_key;
	}

	public void setProvpresu_key(int provpresu_key) {
		this.provpresu_key = provpresu_key;
	}

	public String getProvpresu_id() {
		return provpresu_id;
	}

	public void setProvpresu_id(String provpresu_id) {
		this.provpresu_id = provpresu_id;
	}

	public String getProvpresu_des() {
		return provpresu_des;
	}

	public void setProvpresu_des(String provpresu_des) {
		this.provpresu_des = provpresu_des;
	}

	public String getProvpresu_desabre() {
		return provpresu_desabre;
	}

	public void setProvpresu_desabre(String provpresu_desabre) {
		this.provpresu_desabre = provpresu_desabre;
	}

	public int getProvpresu_est() {
		return provpresu_est;
	}

	public void setProvpresu_est(int provpresu_est) {
		this.provpresu_est = provpresu_est;
	}

	public int getProvpresu_provkey() {
		return provpresu_provkey;
	}

	public void setProvpresu_provkey(int provpresu_provkey) {
		this.provpresu_provkey = provpresu_provkey;
	}

	public String getProvpresu_provid() {
		return provpresu_provid;
	}

	public void setProvpresu_provid(String provpresu_provid) {
		this.provpresu_provid = provpresu_provid;
	}

	public String getProvpresu_provdes() {
		return provpresu_provdes;
	}

	public void setProvpresu_provdes(String provpresu_provdes) {
		this.provpresu_provdes = provpresu_provdes;
	}

	public String getProvpresu_usuadd() {
		return provpresu_usuadd;
	}

	public void setProvpresu_usuadd(String provpresu_usuadd) {
		this.provpresu_usuadd = provpresu_usuadd;
	}

	public Date getProvpresu_fecadd() { 
		return provpresu_fecadd;
	}

	public void setProvpresu_fecadd(Date provpresu_fecadd) {
		this.provpresu_fecadd = provpresu_fecadd;
	}

	public String getProvpresu_usumod() {
		return provpresu_usumod;
	}

	public void setProvpresu_usumod(String provpresu_usumod) {
		this.provpresu_usumod = provpresu_usumod;
	}

	public Date getProvpresu_fecmod() {
		return provpresu_fecmod;
	}

	public void setProvpresu_fecmod(Date provpresu_fecmod) {
		this.provpresu_fecmod = provpresu_fecmod;
	}

	public boolean isValor() {
		if (provpresu_est == 1) {
			valor = true;
		} else {
			valor = false;
		}
		return valor;
	}

	public void setValor(boolean valor) {
		this.valor = valor;
	}

}
