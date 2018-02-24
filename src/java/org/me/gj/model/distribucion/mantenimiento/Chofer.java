package org.me.gj.model.distribucion.mantenimiento;

import java.util.Date;

public class Chofer {
	
	private int emp_id;
	private int suc_id;
	private int chof_key;
	private String chof_id;
	private String chof_apepat;
	private String chof_apemat;
	private String chof_nom;
	private String chof_razsoc;
	private String chof_ruc;
	private int chof_ididen;
	private String chof_nroiden;
	private Date chof_fecnac;
	private String chof_brevete;
	private int chof_telef;
	private String chof_direcc;
	private int chof_est;
	private String chof_usuadd;
	private Date chof_fecadd;
	private String chof_usumod;
	private Date chof_fecmod;
	private String s_nomcompleto;
	private boolean valor;
	
	public Chofer() {
	}
	
	public Chofer(int chof_key, String chof_id, String chof_apepat,
			String chof_apemat, String chof_nom, String chof_razsoc,
			String chof_ruc, int chof_ididen, String chof_nroiden,
			Date chof_fecnac, String chof_brevete, int chof_telef,
			String chof_direcc, int chof_est, String chof_usuadd,
			String chof_usumod) {
		super();
		this.chof_key = chof_key;
		this.chof_id = chof_id;
		this.chof_apepat = chof_apepat;
		this.chof_apemat = chof_apemat;
		this.chof_nom = chof_nom;
		this.chof_razsoc = chof_razsoc;
		this.chof_ruc = chof_ruc;
		this.chof_ididen = chof_ididen;
		this.chof_nroiden = chof_nroiden;
		this.chof_fecnac = chof_fecnac;
		this.chof_brevete = chof_brevete;
		this.chof_telef = chof_telef;
		this.chof_direcc = chof_direcc;
		this.chof_est = chof_est;
		this.chof_usuadd = chof_usuadd;
		this.chof_usumod = chof_usumod;
	}
	
	public int getChof_key() {
		return chof_key;
	}
	public void setChof_key(int chof_key) {
		this.chof_key = chof_key;
	}
	public String getChof_id() {
		return chof_id;
	}
	public void setChof_id(String chof_id) {
		this.chof_id = chof_id;
	}
	public String getChof_apepat() {
		return chof_apepat;
	}
	public void setChof_apepat(String chof_apepat) {
		this.chof_apepat = chof_apepat;
	}
	public String getChof_apemat() {
		return chof_apemat;
	}
	public void setChof_apemat(String chof_apemat) {
		this.chof_apemat = chof_apemat;
	}
	public String getChof_nom() {
		return chof_nom;
	}
	public void setChof_nom(String chof_nom) {
		this.chof_nom = chof_nom;
	}
	public String getChof_razsoc() {
		return chof_razsoc;
	}
	public void setChof_razsoc(String chof_razsoc) {
		this.chof_razsoc = chof_razsoc;
	}
	public String getChof_ruc() {
		return chof_ruc;
	}
	public void setChof_ruc(String chof_ruc) {
		this.chof_ruc = chof_ruc;
	}
	public int getChof_ididen() {
		return chof_ididen;
	}
	public void setChof_ididen(int chof_ididen) {
		this.chof_ididen = chof_ididen;
	}
	public String getChof_nroiden() {
		return chof_nroiden;
	}
	public void setChof_nroiden(String chof_nroiden) {
		this.chof_nroiden = chof_nroiden;
	}
	public Date getChof_fecnac() {
		return chof_fecnac;
	}
	public void setChof_fecnac(Date chof_fecnac) {
		this.chof_fecnac = chof_fecnac;
	}
	public String getChof_brevete() {
		return chof_brevete;
	}
	public void setChof_brevete(String chof_brevete) {
		this.chof_brevete = chof_brevete;
	}
	public int getChof_telef() {
		return chof_telef;
	}
	public void setChof_telef(int chof_telef) {
		this.chof_telef = chof_telef;
	}
	public String getChof_direcc() {
		return chof_direcc;
	}
	public void setChof_direcc(String chof_direcc) {
		this.chof_direcc = chof_direcc;
	}
	public int getChof_est() {
		return chof_est;
	}
	public void setChof_est(int chof_est) {
		this.chof_est = chof_est;
	}
	public String getChof_usuadd() {
		return chof_usuadd;
	}
	public void setChof_usuadd(String chof_usuadd) {
		this.chof_usuadd = chof_usuadd;
	}
	public Date getChof_fecadd() {
		return chof_fecadd;
	}
	public void setChof_fecadd(Date chof_fecadd) {
		this.chof_fecadd = chof_fecadd;
	}
	public String getChof_usumod() {
		return chof_usumod;
	}
	public void setChof_usumod(String chof_usumod) {
		this.chof_usumod = chof_usumod;
	}
	public Date getChof_fecmod() {
		return chof_fecmod;
	}
	public void setChof_fecmod(Date chof_fecmod) {
		this.chof_fecmod = chof_fecmod;
	}

	public String getS_nomcompleto() { 
		s_nomcompleto = chof_apepat.concat(" ").concat(chof_apemat).concat(" ").concat(chof_nom);
		return s_nomcompleto;
	}

	public void setS_nomcompleto(String s_nomcompleto) {
		this.s_nomcompleto = s_nomcompleto;
	}

	public boolean isValor() {
		valor = chof_est == 1?true:false;
		return valor;
	}

	public void setValor(boolean valor) {
		this.valor = valor;
	}

	public int getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

	public int getSuc_id() {
		return suc_id;
	}

	public void setSuc_id(int suc_id) {
		this.suc_id = suc_id;
	}

}
