package org.me.gj.model.cxc.mantenimiento;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

public class VtasDet {

	private String vtas_key;
	private String vtas_periodo;
	private int emp_id;
	private int suc_id;
	private String pro_id;
	private String pro_des;
	private String pro_clas;
	private int pro_peso;
	private int vtas_estado;
	private String vtas_estadodes;
	private int vtas_sit;
	private String vtas_sitdes;
	private String prov_id;
	private String prov_des;
	private String alm_id;
	private String alm_des;
	private String ubic_id;
	private String ubic_des;
	private String lp_id;
	private String lp_des;
	private int vtas_unipre;
	private int vtas_canped;
	private int vtas_canent;
	private double vtas_punit;
	private double vtas_vventa;
	private double vtas_dscto1;
	private double vtas_dscto2;
	private double vtas_dscto3;
	private double vtas_dscto4;
	private double vtas_dsctot;
	private double vtas_sdsctot;
	private int imp_id;
	private String imp_des;
	private double vtas_impto;
	private double vtas_vimpto;
	private double vtas_pventa;
	private double vtas_impper;
	private String vtas_usuadd;
	private Date vtas_fecadd;
	private String vtas_usumod;
	private Date vtas_fecmod;
	
	//auxiliares
	private String spunit;
	private String svventa;
	private String sdsctot;
	private String ssdsctot;
	private String simpto;
	private String svimpto;
	private String spventa;
	private String simpper;
	private int cantent;
	private int	cantfrac; 
	
	
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df1 = new DecimalFormat("###,##0.0", dfs);
    DecimalFormat df2 = new DecimalFormat("###,##0.00", dfs);
    DecimalFormat df4 = new DecimalFormat("###,##0.0000", dfs);
	
	
	public String getVtas_key() {
		return vtas_key;
	}
	public void setVtas_key(String vtas_key) {
		this.vtas_key = vtas_key;
	}
	public String getVtas_periodo() {
		return vtas_periodo;
	}
	public void setVtas_periodo(String vtas_periodo) {
		this.vtas_periodo = vtas_periodo;
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
	public String getPro_id() {
		return pro_id;
	}
	public void setPro_id(String pro_id) {
		this.pro_id = pro_id;
	}
	public String getPro_des() {
		return pro_des;
	}
	public void setPro_des(String pro_des) {
		this.pro_des = pro_des;
	}
	public String getPro_clas() {
		return pro_clas;
	}
	public void setPro_clas(String pro_clas) {
		this.pro_clas = pro_clas;
	}
	public int getPro_peso() {
		return pro_peso;
	}
	public void setPro_peso(int pro_peso) {
		this.pro_peso = pro_peso;
	}
	public int getVtas_estado() {
		return vtas_estado;
	}
	public void setVtas_estado(int vtas_estado) {
		this.vtas_estado = vtas_estado;
	}
	public String getVtas_estadodes() {
		return vtas_estadodes;
	}
	public void setVtas_estadodes(String vtas_estadodes) {
		this.vtas_estadodes = vtas_estadodes;
	}
	public int getVtas_sit() {
		return vtas_sit;
	}
	public void setVtas_sit(int vtas_sit) {
		this.vtas_sit = vtas_sit;
	}
	public String getVtas_sitdes() {
		return vtas_sitdes;
	}
	public void setVtas_sitdes(String vtas_sitdes) {
		this.vtas_sitdes = vtas_sitdes;
	}
	public String getProv_id() {
		return prov_id;
	}
	public void setProv_id(String prov_id) {
		this.prov_id = prov_id;
	}
	public String getProv_des() {
		return prov_des;
	}
	public void setProv_des(String prov_des) {
		this.prov_des = prov_des;
	}
	public String getAlm_id() {
		return alm_id;
	}
	public void setAlm_id(String alm_id) {
		this.alm_id = alm_id;
	}
	public String getAlm_des() {
		return alm_des;
	}
	public void setAlm_des(String alm_des) {
		this.alm_des = alm_des;
	}
	public String getUbic_id() {
		return ubic_id;
	}
	public void setUbic_id(String ubic_id) {
		this.ubic_id = ubic_id;
	}
	public String getUbic_des() {
		return ubic_des;
	}
	public void setUbic_des(String ubic_des) {
		this.ubic_des = ubic_des;
	}
	public String getLp_id() {
		return lp_id;
	}
	public void setLp_id(String lp_id) {
		this.lp_id = lp_id;
	}
	public String getLp_des() {
		return lp_des;
	}
	public void setLp_des(String lp_des) {
		this.lp_des = lp_des;
	}
	public int getVtas_unipre() {
		return vtas_unipre;
	}
	public void setVtas_unipre(int vtas_unipre) {
		this.vtas_unipre = vtas_unipre;
	}
	public int getVtas_canped() {
		return vtas_canped;
	}
	public void setVtas_canped(int vtas_canped) {
		this.vtas_canped = vtas_canped;
	}
	public int getVtas_canent() {
		return vtas_canent;
	}
	public void setVtas_canent(int vtas_canent) {
		this.vtas_canent = vtas_canent;
	}
	public double getVtas_punit() {
		return vtas_punit;
	}
	public void setVtas_punit(double vtas_punit) {
		this.vtas_punit = vtas_punit;
	}
	public double getVtas_vventa() {
		return vtas_vventa;
	}
	public void setVtas_vventa(double vtas_vventa) {
		this.vtas_vventa = vtas_vventa;
	}
	public double getVtas_dscto1() {
		return vtas_dscto1;
	}
	public void setVtas_dscto1(double vtas_dscto1) {
		this.vtas_dscto1 = vtas_dscto1;
	}
	public double getVtas_dscto2() {
		return vtas_dscto2;
	}
	public void setVtas_dscto2(double vtas_dscto2) {
		this.vtas_dscto2 = vtas_dscto2;
	}
	public double getVtas_dscto3() {
		return vtas_dscto3;
	}
	public void setVtas_dscto3(double vtas_dscto3) {
		this.vtas_dscto3 = vtas_dscto3;
	}
	public double getVtas_dscto4() {
		return vtas_dscto4;
	}
	public void setVtas_dscto4(double vtas_dscto4) {
		this.vtas_dscto4 = vtas_dscto4;
	}
	public double getVtas_dsctot() {
		return vtas_dsctot;
	}
	public void setVtas_dsctot(double vtas_dsctot) {
		this.vtas_dsctot = vtas_dsctot;
	}
	public double getVtas_sdsctot() {
		return vtas_sdsctot;
	}
	public void setVtas_sdsctot(double vtas_sdsctot) {
		this.vtas_sdsctot = vtas_sdsctot;
	}
	public int getImp_id() {
		return imp_id;
	}
	public void setImp_id(int imp_id) {
		this.imp_id = imp_id;
	}
	public String getImp_des() {
		return imp_des;
	}
	public void setImp_des(String imp_des) {
		this.imp_des = imp_des;
	}
	public double getVtas_impto() {
		return vtas_impto;
	}
	public void setVtas_impto(double vtas_impto) {
		this.vtas_impto = vtas_impto;
	}
	public double getVtas_vimpto() {
		return vtas_vimpto;
	}
	public void setVtas_vimpto(double vtas_vimpto) {
		this.vtas_vimpto = vtas_vimpto;
	}
	public double getVtas_pventa() {
		return vtas_pventa;
	}
	public void setVtas_pventa(double vtas_pventa) {
		this.vtas_pventa = vtas_pventa;
	}
	public double getVtas_impper() {
		return vtas_impper;
	}
	public void setVtas_impper(double vtas_impper) {
		this.vtas_impper = vtas_impper;
	}
	public String getVtas_usuadd() {
		return vtas_usuadd;
	}
	public void setVtas_usuadd(String vtas_usuadd) {
		this.vtas_usuadd = vtas_usuadd;
	}
	public Date getVtas_fecadd() {
		return vtas_fecadd;
	}
	public void setVtas_fecadd(Date vtas_fecadd) {
		this.vtas_fecadd = vtas_fecadd;
	}
	public String getVtas_usumod() {
		return vtas_usumod;
	}
	public void setVtas_usumod(String vtas_usumod) {
		this.vtas_usumod = vtas_usumod;
	}
	public Date getVtas_fecmod() {
		return vtas_fecmod;
	}
	public void setVtas_fecmod(Date vtas_fecmod) {
		this.vtas_fecmod = vtas_fecmod;
	}

	
	public int getCantent() {
		cantent = (int) (vtas_canped / vtas_unipre);
		return cantent;
	}
	public void setCantent(int cantent) {
		this.cantent = cantent;
	}
	public int getCantfrac() {
		cantfrac = (int) Math.round(vtas_canped % vtas_unipre);
		return cantfrac;
	}
	public void setCantfrac(int cantfrac) {
		this.cantfrac = cantfrac;
	}
	public String getSpunit() {
		spunit = df4.format(vtas_punit);
		return spunit;
	}

	public void setSpunit(String spunit) {
		this.spunit = spunit;
	}

	public String getSvventa() {
		svventa = df4.format(vtas_vventa);
		return svventa;
	}

	public void setSvventa(String svventa) {
		this.svventa = svventa;
	}

	public String getSdsctot() {
		sdsctot = df1.format(vtas_dsctot);
		return sdsctot;
	}

	public void setSdsctot(String sdsctot) {
		this.sdsctot = sdsctot;
	}

	public String getSsdsctot() {
		ssdsctot = df2.format(vtas_sdsctot);
		return ssdsctot;
	}

	public void setSsdsctot(String ssdsctot) {
		this.ssdsctot = ssdsctot;
	}

	public String getSimpto() {
		simpto = df1.format(vtas_impto);
		return simpto;
	}

	public void setSimpto(String simpto) {
		this.simpto = simpto;
	}

	public String getSvimpto() {
		svimpto = df4.format(vtas_vimpto);
		return svimpto;
	}

	public void setSvimpto(String svimpto) {
		this.svimpto = svimpto;
	}

	public String getSpventa() {
		spventa = df4.format(vtas_pventa);
		return spventa;
	}

	public void setSpventa(String spventa) {
		this.spventa = spventa;
	}

	public String getSimpper() {
		simpper = df4.format(vtas_impper);
		return simpper;
	}

	public void setSimpper(String simpper) {
		this.simpper = simpper;
	}
	
	
	
	
}
