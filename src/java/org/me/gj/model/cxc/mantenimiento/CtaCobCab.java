package org.me.gj.model.cxc.mantenimiento;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.me.gj.util.Utilitarios;

public class CtaCobCab {

	private String ctacob_key;
	private String ctacob_periodo;
	private int emp_id;
	private int suc_id;
	private String ctacob_tipdoc;
	private String ctacob_tipdocdes;
	private String ctacob_tipdocdesdes;
	private String ctacob_nrodoc;
	private int ctacob_estado;
	private String ctacob_estadodes;
	private String ctacob_tipmov;
	private String ctacob_tipmovdes;
	private String cli_id;
	private String cli_des;
	private int clidir_id;
	private String clidir_des;
	private Date ctacob_fecemi;
	private Date ctacob_fecent;
	private Date ctacob_fecrec;
	private Date ctacob_fecumov;
	private Date ctacob_fecanu;
	private Date ctacob_fecven;
	private int ctacob_diapla;
	private String idcanal;
	private String idmesa;
	private String idruta;
	private String zon_id;
	private String zon_des;
	private String sup_id;
	private String ven_id;
	private String ven_des;
	private String trans_id;
	private String trans_des;
	private String ctacob_simmon;
	private String ctacob_mon;
	private String ctacob_mondes;
	private double ctacob_tipcam;
	private String con_id;
	private String con_des;
	private String ctacob_glosa;
	private double ctacob_total;
	private double ctacob_saldo;
	private int ctacob_sit;
	private String ctacob_sitdes;
	private String ctacob_pen;
	private String ctacob_pendes;
	private int ctacob_inc;
	private String ctacob_incdes;
	private String ctacob_percom;
	private String ctacob_usuadd;
	private Date ctacob_fecadd;
	private String ctacob_usumod;
	private Date ctacob_fecmod;
	private String doc_emi;
	private double saldo_total;
	private String pcab_nroped;
	private String cli_ruc;
	private String nescab_id;
	private String dociden;
	private String nroiden;
	private String lp_id;
	private String lp_des;
	private String sup_des;
	private String tipvenid;
	private String tipvendes;
	private String diavisdes;
	private String chof_id;
	private String chof_des;
	private String trans_placa;
	private double limcred;
	private String limdoc;
	private String hora_id;
	private String hora_des;
	private String nroreg;
	private String tip_dev;
	private String mot_devid;
	private String mot_devdes;
	private boolean valSelec;

	
	// Auxiliares
	private String s_fecemi;
	private String s_fecumov;
	private String ctacob_stotal;
	private String ctacob_ssaldo;
	private String ssaldo_total;
	private long difdias;
	
	DecimalFormatSymbols dfs;
	DecimalFormat dfc;

	public CtaCobCab() {
		dfs = new DecimalFormatSymbols(Locale.US);
		dfc = new DecimalFormat("#,###,##0.00", dfs);
	}

	public String getCtacob_key() {
		return ctacob_key;
	}

	public void setCtacob_key(String ctacob_key) {
		this.ctacob_key = ctacob_key;
	}

	public String getCtacob_periodo() {
		return ctacob_periodo;
	}

	public void setCtacob_periodo(String ctacob_periodo) {
		this.ctacob_periodo = ctacob_periodo;
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

	public String getCtacob_tipdoc() {
		return ctacob_tipdoc;
	}

	public void setCtacob_tipdoc(String ctacob_tipdoc) {
		this.ctacob_tipdoc = ctacob_tipdoc;
	}

	public String getCtacob_tipdocdes() {
		return ctacob_tipdocdes;
	}

	public void setCtacob_tipdocdes(String ctacob_tipdocdes) {
		this.ctacob_tipdocdes = ctacob_tipdocdes;
	}

	public String getCtacob_nrodoc() {
		return ctacob_nrodoc;
	}

	public void setCtacob_nrodoc(String ctacob_nrodoc) {
		this.ctacob_nrodoc = ctacob_nrodoc;
	}

	public int getCtacob_estado() {
		return ctacob_estado;
	}

	public void setCtacob_estado(int ctacob_estado) {
		this.ctacob_estado = ctacob_estado;
	}

	public String getCtacob_estadodes() {
		return ctacob_estadodes;
	}

	public void setCtacob_estadodes(String ctacob_estadodes) {
		this.ctacob_estadodes = ctacob_estadodes;
	}

	public String getCtacob_tipmov() {
		return ctacob_tipmov;
	}

	public void setCtacob_tipmov(String ctacob_tipmov) {
		this.ctacob_tipmov = ctacob_tipmov;
	}

	public String getCtacob_tipmovdes() {
		return ctacob_tipmovdes;
	}

	public void setCtacob_tipmovdes(String ctacob_tipmovdes) {
		this.ctacob_tipmovdes = ctacob_tipmovdes;
	}

	public String getCli_id() {
		return cli_id;
	}

	public void setCli_id(String cli_id) {
		this.cli_id = cli_id;
	}

	public String getCli_des() {
		return cli_des;
	}

	public void setCli_des(String cli_des) {
		this.cli_des = cli_des;
	}

	public int getClidir_id() {
		return clidir_id;
	}

	public void setClidir_id(int clidir_id) {
		this.clidir_id = clidir_id;
	}

	public String getClidir_des() {
		return clidir_des;
	}

	public void setClidir_des(String clidir_des) {
		this.clidir_des = clidir_des;
	}

	public Date getCtacob_fecemi() {
		return ctacob_fecemi;
	}

	public void setCtacob_fecemi(Date ctacob_fecemi) {
		this.ctacob_fecemi = ctacob_fecemi;
	}

	public Date getCtacob_fecent() {
		return ctacob_fecent;
	}

	public void setCtacob_fecent(Date ctacob_fecent) {
		this.ctacob_fecent = ctacob_fecent;
	}

	public Date getCtacob_fecrec() {
		return ctacob_fecrec;
	}

	public void setCtacob_fecrec(Date ctacob_fecrec) {
		this.ctacob_fecrec = ctacob_fecrec;
	}

	public Date getCtacob_fecven() {
		return ctacob_fecven;
	}

	public void setCtacob_fecven(Date ctacob_fecven) {
		this.ctacob_fecven = ctacob_fecven;
	}

	public Date getCtacob_fecumov() {
		return ctacob_fecumov;
	}

	public void setCtacob_fecumov(Date ctacob_fecumov) {
		this.ctacob_fecumov = ctacob_fecumov;
	}

	public Date getCtacob_fecanu() {
		return ctacob_fecanu;
	}

	public void setCtacob_fecanu(Date ctacob_fecanu) {
		this.ctacob_fecanu = ctacob_fecanu;
	}

	public int getCtacob_diapla() {
		return ctacob_diapla;
	}

	public void setCtacob_diapla(int ctacob_diapla) {
		this.ctacob_diapla = ctacob_diapla;
	}

	public String getIdcanal() {
		return idcanal;
	}

	public void setIdcanal(String idcanal) {
		this.idcanal = idcanal;
	}

	public String getIdmesa() {
		return idmesa;
	}

	public void setIdmesa(String idmesa) {
		this.idmesa = idmesa;
	}

	public String getIdruta() {
		return idruta;
	}

	public void setIdruta(String idruta) {
		this.idruta = idruta;
	}

	public String getZon_id() {
		return zon_id;
	}

	public void setZon_id(String zon_id) {
		this.zon_id = zon_id;
	}

	public String getZon_des() {
		return zon_des;
	}

	public void setZon_des(String zon_des) {
		this.zon_des = zon_des;
	}

	public String getSup_id() {
		return sup_id;
	}

	public void setSup_id(String sup_id) {
		this.sup_id = sup_id;
	}

	public String getVen_id() {
		return ven_id;
	}

	public void setVen_id(String ven_id) {
		this.ven_id = ven_id;
	}

	public String getTrans_id() {
		return trans_id;
	}

	public void setTrans_id(String trans_id) {
		this.trans_id = Utilitarios.lpad(trans_id,4,"0");
	}

	public String getTrans_des() {
		return trans_des;
	}

	public void setTrans_des(String trans_des) {
		this.trans_des = trans_des;
	}

	public String getCtacob_mon() {
		return ctacob_mon;
	}

	public void setCtacob_mon(String ctacob_mon) {
		this.ctacob_mon = ctacob_mon;
	}

	public String getCtacob_mondes() {
		return ctacob_mondes;
	}

	public void setCtacob_mondes(String ctacob_mondes) {
		this.ctacob_mondes = ctacob_mondes;
	}

	public double getCtacob_tipcam() {
		return ctacob_tipcam;
	}

	public void setCtacob_tipcam(double ctacob_tipcam) {
		this.ctacob_tipcam = ctacob_tipcam;
	}

	public String getCon_id() {
		return con_id;
	}

	public void setCon_id(String con_id) {
		this.con_id = con_id;
	}

	public String getCon_des() {
		return con_des;
	}

	public void setCon_des(String con_des) {
		this.con_des = con_des;
	}

	public String getCtacob_glosa() {
		return ctacob_glosa;
	}

	public void setCtacob_glosa(String ctacob_glosa) {
		this.ctacob_glosa = ctacob_glosa;
	}

	public double getCtacob_total() {
		return ctacob_total;
	}

	public void setCtacob_total(double ctacob_total) {
		this.ctacob_total = ctacob_total;
	}

	public double getCtacob_saldo() {
		return ctacob_saldo;
	}

	public void setCtacob_saldo(double ctacob_saldo) {
		this.ctacob_saldo = ctacob_saldo;
	}

	public int getCtacob_sit() {
		return ctacob_sit;
	}

	public void setCtacob_sit(int ctacob_sit) {
		this.ctacob_sit = ctacob_sit;
	}

	public String getCtacob_sitdes() {
		return ctacob_sitdes;
	}

	public void setCtacob_sitdes(String ctacob_sitdes) {
		this.ctacob_sitdes = ctacob_sitdes;
	}

	public String getCtacob_pen() {
		return ctacob_pen;
	}

	public void setCtacob_pen(String ctacob_pen) {
		this.ctacob_pen = ctacob_pen;
	}

	public String getCtacob_pendes() {
		return ctacob_pendes;
	}

	public void setCtacob_pendes(String ctacob_pendes) {
		this.ctacob_pendes = ctacob_pendes;
	}

	public int getCtacob_inc() {
		return ctacob_inc;
	}

	public void setCtacob_inc(int ctacob_inc) {
		this.ctacob_inc = ctacob_inc;
	}

	public String getCtacob_incdes() {
		return ctacob_incdes;
	}

	public void setCtacob_incdes(String ctacob_incdes) {
		this.ctacob_incdes = ctacob_incdes;
	}

	public String getCtacob_percom() {
		return ctacob_percom;
	}

	public void setCtacob_percom(String ctacob_percom) {
		this.ctacob_percom = ctacob_percom;
	}

	public String getCtacob_usuadd() {
		return ctacob_usuadd;
	}

	public void setCtacob_usuadd(String ctacob_usuadd) {
		this.ctacob_usuadd = ctacob_usuadd;
	}

	public Date getCtacob_fecadd() {
		return ctacob_fecadd;
	}

	public void setCtacob_fecadd(Date ctacob_fecadd) {
		this.ctacob_fecadd = ctacob_fecadd;
	}

	public String getCtacob_usumod() {
		return ctacob_usumod;
	}

	public void setCtacob_usumod(String ctacob_usumod) {
		this.ctacob_usumod = ctacob_usumod;
	}

	public Date getCtacob_fecmod() {
		return ctacob_fecmod;
	}

	public void setCtacob_fecmod(Date ctacob_fecmod) {
		this.ctacob_fecmod = ctacob_fecmod;
	}

	public String getS_fecemi() {
		s_fecemi = new SimpleDateFormat("dd/MM/yyyy").format(ctacob_fecemi);
		return s_fecemi;
	}

	public void setS_fecemi(String s_fecemi) {
		this.s_fecemi = s_fecemi;
	}

	public String getS_fecumov() {
		if(ctacob_fecumov != null){
			s_fecumov = new SimpleDateFormat("dd/MM/yyyy").format(ctacob_fecumov);
		}
		return s_fecumov;
	}

	public void setS_fecumov(String s_fecumov) {
		this.s_fecumov = s_fecumov;
	}

	public String getCtacob_simmon() {
		return ctacob_simmon;
	}

	public void setCtacob_simmon(String ctacob_simmon) {
		this.ctacob_simmon = ctacob_simmon;
	}

	
	public String getDoc_emi() {
		return doc_emi;
	}

	public void setDoc_emi(String doc_emi) {
		this.doc_emi = doc_emi;
	}

	public double getSaldo_total() {
		return saldo_total;
	}

	public void setSaldo_total(double saldo_total) {
		this.saldo_total = saldo_total;
	}

	public String getSsaldo_total() {
		ssaldo_total = dfc.format(saldo_total);
		return ssaldo_total;
	}

	public void setSsaldo_total(String ssaldo_total) {
		this.ssaldo_total = ssaldo_total;
	}

	public String getCtacob_stotal() {
		ctacob_stotal = dfc.format(ctacob_total);
		return ctacob_stotal;
	}

	public void setCtacob_stotal(String ctacob_stotal) {
		this.ctacob_stotal = ctacob_stotal;
	}

	public String getCtacob_ssaldo() {
		ctacob_ssaldo = dfc.format(ctacob_saldo);
		return ctacob_ssaldo;
	}

	public void setCtacob_ssaldo(String ctacob_ssaldo) {
		this.ctacob_ssaldo = ctacob_ssaldo;
	}

	public long getDifdias() {
	    if(ctacob_fecent != null && ctacob_fecumov != null ){
			Calendar c_femi = Calendar.getInstance(); 		
		    Calendar c_fumov = Calendar.getInstance();
			String f_emi = new SimpleDateFormat("yyyyMMdd").format(ctacob_fecent);
			String f_umov = new SimpleDateFormat("yyyyMMdd").format(ctacob_fecumov);
			    
		    c_femi.set(Integer.parseInt(f_emi.substring(0, 4)), Integer.parseInt(f_emi.substring(4, f_emi.length() - 2)), Integer.parseInt(f_emi.substring(6, 8)));
		    c_fumov.set(Integer.parseInt(f_umov.substring(0, 4)), Integer.parseInt(f_umov.substring(4, f_umov.length() - 2)), Integer.parseInt(f_umov.substring(6, 8))); 
		   
		    long l_femi = c_femi.getTimeInMillis();
		    long l_fumov = c_fumov.getTimeInMillis();
		    long restafecha = l_fumov - l_femi;   
		    	    
		     difdias = restafecha / (24 * 60 * 60 * 1000);		
	     }else {
	    	 difdias = 0;
	     }
	     
		return difdias;
	}

	public void setDifdias(long difdias) {
		this.difdias = difdias;
	}

	public String getPcab_nroped() {
		return pcab_nroped;
	}

	public void setPcab_nroped(String pcab_nroped) {
		this.pcab_nroped = pcab_nroped;
	}

	public String getVen_des() {
		return ven_des;
	}

	public void setVen_des(String ven_des) {
		this.ven_des = ven_des;
	}

	public String getCli_ruc() {
		return cli_ruc;
	}

	public void setCli_ruc(String cli_ruc) {
		this.cli_ruc = cli_ruc;
	}

	public String getNescab_id() {
		return nescab_id;
	}

	public void setNescab_id(String nescab_id) {
		this.nescab_id = nescab_id;
	}

	public String getDociden() {
		return dociden;
	}

	public void setDociden(String dociden) {
		this.dociden = dociden;
	}

	public String getNroiden() {
		return nroiden;
	}

	public void setNroiden(String nroiden) {
		this.nroiden = nroiden;
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

	public String getSup_des() {
		return sup_des;
	}

	public void setSup_des(String sup_des) {
		this.sup_des = sup_des;
	}

	public String getCtacob_tipdocdesdes() {
		return ctacob_tipdocdesdes;
	}

	public void setCtacob_tipdocdesdes(String ctacob_tipdocdesdes) {
		this.ctacob_tipdocdesdes = ctacob_tipdocdesdes;
	}

	public String getTipvenid() {
		return tipvenid;
	}

	public void setTipvenid(String tipvenid) {
		this.tipvenid = tipvenid;
	}

	public String getTipvendes() {
		return tipvendes;
	}

	public void setTipvendes(String tipvendes) {
		this.tipvendes = tipvendes;
	}

	public String getDiavisdes() {
		return diavisdes;
	}

	public void setDiavisdes(String diavisdes) {
		this.diavisdes = diavisdes;
	}

	public String getChof_id() {
		return chof_id;
	}

	public void setChof_id(String chof_id) {
		this.chof_id = chof_id;
	}

	public String getChof_des() {
		return chof_des;
	}

	public void setChof_des(String chof_des) {
		this.chof_des = chof_des;
	}

	public String getTrans_placa() {
		return trans_placa;
	}

	public void setTrans_placa(String trans_placa) {
		this.trans_placa = trans_placa;
	}

	public double getLimcred() {
		return limcred;
	}

	public void setLimcred(double limcred) {
		this.limcred = limcred;
	}

	public String getLimdoc() {
		return limdoc;
	}

	public void setLimdoc(String limdoc) {
		this.limdoc = limdoc;
	}

	public String getHora_id() {
		return hora_id;
	}

	public void setHora_id(String hora_id) {
		this.hora_id = hora_id;
	}

	public String getHora_des() {
		return hora_des;
	}

	public void setHora_des(String hora_des) {
		this.hora_des = hora_des;
	}

	public String getNroreg() {
		return nroreg;
	}

	public void setNroreg(String nroreg) {
		this.nroreg = nroreg;
	}

	public String getTip_dev() {
		return tip_dev;
	}

	public void setTip_dev(String tip_dev) {
		this.tip_dev = tip_dev;
	}

	public String getMot_devid() {
		return mot_devid;
	}

	public void setMot_devid(String mot_devid) {
		this.mot_devid = mot_devid;
	}

	public String getMot_devdes() {
		return mot_devdes;
	}

	public void setMot_devdes(String mot_devdes) {
		this.mot_devdes = mot_devdes;
	}

	public boolean isValSelec() {
		return valSelec;
	}

	public void setValSelec(boolean valSelec) {
		this.valSelec = valSelec;
	}




	


	
}
