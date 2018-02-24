package org.me.gj.model.distribucion.mantenimiento;

import java.util.Date;

public class ProgramacionRutas {

	private int progruta_key;
	private String progruta_id;
	private String progruta_dvisid;
	private String progruta_dvisdes;
	private String progruta_hentid;
	private String progruta_hentdes;
	private String progruta_transid;
	private String progruta_transdes;
	private String progruta_zonid;
	private String progruta_zondes;
	private String progruta_glosa;
	private int progruta_estado;
	private String progruta_usuadd;
	private Date progruta_fecadd;
	private String progruta_usumod;
	private Date progruta_fecmod;
	private boolean valor;

	// Auxiliares
	private String pcambios; // valida si realizo un cambio al actualizar: "
								// Cambio (C) - No Cambio(NC)

	public ProgramacionRutas() {
	}

	public ProgramacionRutas(int progruta_key, String progruta_id,
			String progruta_dvisid, String progruta_hentid,
			String progruta_transid, String progruta_zonid,
			String progruta_glosa, int progruta_estado, String progruta_usuadd,
			String progruta_usumod, String pcambios) {
		super();
		this.progruta_key = progruta_key;
		this.progruta_id = progruta_id;
		this.progruta_dvisid = progruta_dvisid;
		this.progruta_hentid = progruta_hentid;
		this.progruta_transid = progruta_transid;
		this.progruta_zonid = progruta_zonid;
		this.progruta_glosa = progruta_glosa;
		this.progruta_estado = progruta_estado;
		this.progruta_usuadd = progruta_usuadd;
		this.progruta_usumod = progruta_usumod;
		this.pcambios = pcambios;
	}

	public int getProgruta_key() {
		return progruta_key;
	}

	public void setProgruta_key(int progruta_key) {
		this.progruta_key = progruta_key;
	}

	public String getProgruta_id() {
		return progruta_id;
	}

	public void setProgruta_id(String progruta_id) {
		this.progruta_id = progruta_id;
	}

	public String getProgruta_dvisid() {
		return progruta_dvisid;
	}

	public void setProgruta_dvisid(String progruta_dvisid) {
		this.progruta_dvisid = progruta_dvisid;
	}

	public String getProgruta_dvisdes() {
		return progruta_dvisdes;
	}

	public void setProgruta_dvisdes(String progruta_dvisdes) {
		this.progruta_dvisdes = progruta_dvisdes;
	}

	public String getProgruta_hentid() {
		return progruta_hentid;
	}

	public void setProgruta_hentid(String progruta_hentid) {
		this.progruta_hentid = progruta_hentid;
	}

	public String getProgruta_hentdes() {
		return progruta_hentdes;
	}

	public void setProgruta_hentdes(String progruta_hentdes) {
		this.progruta_hentdes = progruta_hentdes;
	}

	public String getProgruta_transid() {
		return progruta_transid;
	}

	public void setProgruta_transid(String progruta_transid) {
		this.progruta_transid = progruta_transid;
	}

	public String getProgruta_transdes() {
		return progruta_transdes;
	}

	public void setProgruta_transdes(String progruta_transdes) {
		this.progruta_transdes = progruta_transdes;
	}

	public String getProgruta_zonid() {
		return progruta_zonid;
	}

	public void setProgruta_zonid(String progruta_zonid) {
		this.progruta_zonid = progruta_zonid;
	}

	public String getProgruta_zondes() {
		return progruta_zondes;
	}

	public void setProgruta_zondes(String progruta_zondes) {
		this.progruta_zondes = progruta_zondes;
	}

	public String getProgruta_glosa() {
		return progruta_glosa;
	}

	public void setProgruta_glosa(String progruta_glosa) {
		this.progruta_glosa = progruta_glosa;
	}

	public int getProgruta_estado() {
		return progruta_estado;
	}

	public void setProgruta_estado(int progruta_estado) {
		this.progruta_estado = progruta_estado;
	}

	public String getProgruta_usuadd() {
		return progruta_usuadd;
	}

	public void setProgruta_usuadd(String progruta_usuadd) {
		this.progruta_usuadd = progruta_usuadd;
	}

	public Date getProgruta_fecadd() {
		return progruta_fecadd;
	}

	public void setProgruta_fecadd(Date progruta_fecadd) {
		this.progruta_fecadd = progruta_fecadd;
	}

	public String getProgruta_usumod() {
		return progruta_usumod;
	}

	public void setProgruta_usumod(String progruta_usumod) {
		this.progruta_usumod = progruta_usumod;
	}

	public Date getProgruta_fecmod() {
		return progruta_fecmod;
	}

	public void setProgruta_fecmod(Date progruta_fecmod) {
		this.progruta_fecmod = progruta_fecmod;
	}

	public boolean isValor() {
		valor = progruta_estado == 1 ? true : false;
		return valor;
	}

	public void setValor(boolean valor) {
		this.valor = valor;
	}

	public String getPcambios() {
		return pcambios;
	}

	public void setPcambios(String pcambios) {
		this.pcambios = pcambios;
	}

}