/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.gj.model.planillas.mantenimiento;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ROJAS
 */
public class DerHabiente {
    
    private int pltipdoc;
    private String plnrodoc;
    private int pldh_id;
    private int pldh_estado;
    private int pldh_tipdoc;
    private String pldh_nrodoc;
    private String pldh_apepat;
    private String pldh_apemat;
    private String pldh_nombres;
    private Date pldh_fecnac;
    private int pldh_sexo;
    private String pldh_vinfam;
    private Date pldh_fecalt;
    private String pldh_tipdocvf;
    private String pldh_nrocam;
    private String pldh_situa;
    private String pldh_tipbaj;
    private Date pldh_fecbaj;
    private String pldh_nrd;
    private int pldh_indom;
    private String pldh_munpn;
    private String pldh_nacion;
    private String pldh_dirtipvia;
    private String pldh_dirnomvia;
    private String pldh_dirnumvia;
    private String pldh_dirint;
    private String pldh_dirtipzon;
    private String pldh_dirnomzon;
    private String pldh_dirref;
    private String pldh_dirubigeo;
    private String pldh_usuadd;
    private Date pldh_fecadd;
    private String pldh_usumod;
    private Date pldh_fecmod;
    //agrego jr 16012018
    private int pldh_estudios;
    private int pldh_discapacidad;
    
    //Utilitarios
    private String pldh_tipdoc_des;
    private String s_pldh_fecnac;
    private String pldh_sexo_des;
    private String pldh_vinfam_des;
    private String pldh_nacion_des;
    private String pldh_munpn_des;
    private String pldh_situa_des;
    private String pldh_dirubigeo_des;
    
    private String ind_accion = "Q";
    private boolean valor;
    
    SimpleDateFormat ffecha = new SimpleDateFormat("dd/MM/yyyy");

    public DerHabiente() {
    }
    
    

    public int getPltipdoc() {
        return pltipdoc;
    }

    public void setPltipdoc(int pltipdoc) {
        this.pltipdoc = pltipdoc;
    }

    public String getPlnrodoc() {
        return plnrodoc;
    }

    public void setPlnrodoc(String plnrodoc) {
        this.plnrodoc = plnrodoc;
    }

    public int getPldh_id() {
        return pldh_id;
    }

    public void setPldh_id(int pldh_id) {
        this.pldh_id = pldh_id;
    }

    public int getPldh_estado() {
        return pldh_estado;
    }

    public void setPldh_estado(int pldh_estado) {
        this.pldh_estado = pldh_estado;
    }

    public int getPldh_tipdoc() {
        return pldh_tipdoc;
    }

    public void setPldh_tipdoc(int pldh_tipdoc) {
        this.pldh_tipdoc = pldh_tipdoc;
    }

    public String getPldh_nrodoc() {
        return pldh_nrodoc;
    }

    public void setPldh_nrodoc(String pldh_nrodoc) {
        this.pldh_nrodoc = pldh_nrodoc;
    }

    public String getPldh_apepat() {
        return pldh_apepat;
    }

    public void setPldh_apepat(String pldh_apepat) {
        this.pldh_apepat = pldh_apepat;
    }

    public String getPldh_apemat() {
        return pldh_apemat;
    }

    public void setPldh_apemat(String pldh_apemat) {
        this.pldh_apemat = pldh_apemat;
    }

    public String getPldh_nombres() {
        return pldh_nombres;
    }

    public void setPldh_nombres(String pldh_nombres) {
        this.pldh_nombres = pldh_nombres;
    }

    public Date getPldh_fecnac() {
        return pldh_fecnac;
    }

    public void setPldh_fecnac(Date pldh_fecnac) {
        this.pldh_fecnac = pldh_fecnac;
    }

    public int getPldh_sexo() {
        return pldh_sexo;
    }

    public void setPldh_sexo(int pldh_sexo) {
        this.pldh_sexo = pldh_sexo;
    }

    public String getPldh_vinfam() {
        return pldh_vinfam;
    }

    public void setPldh_vinfam(String pldh_vinfam) {
        this.pldh_vinfam = pldh_vinfam;
    }

    public Date getPldh_fecalt() {
        return pldh_fecalt;
    }

    public void setPldh_fecalt(Date pldh_fecalt) {
        this.pldh_fecalt = pldh_fecalt;
    }

    public String getPldh_tipdocvf() {
        return pldh_tipdocvf;
    }

    public void setPldh_tipdocvf(String pldh_tipdocvf) {
        this.pldh_tipdocvf = pldh_tipdocvf;
    }

    public String getPldh_nrocam() {
        return pldh_nrocam;
    }

    public void setPldh_nrocam(String pldh_nrocam) {
        this.pldh_nrocam = pldh_nrocam;
    }

    public String getPldh_situa() {
        return pldh_situa;
    }

    public void setPldh_situa(String pldh_situa) {
        this.pldh_situa = pldh_situa;
    }

    public String getPldh_tipbaj() {
        return pldh_tipbaj;
    }

    public void setPldh_tipbaj(String pldh_tipbaj) {
        this.pldh_tipbaj = pldh_tipbaj;
    }

    public Date getPldh_fecbaj() {
        return pldh_fecbaj;
    }

    public void setPldh_fecbaj(Date pldh_fecbaj) {
        this.pldh_fecbaj = pldh_fecbaj;
    }

    public String getPldh_nrd() {
        return pldh_nrd;
    }

    public void setPldh_nrd(String pldh_nrd) {
        this.pldh_nrd = pldh_nrd;
    }

    public int getPldh_indom() {
        return pldh_indom;
    }

    public void setPldh_indom(int pldh_indom) {
        this.pldh_indom = pldh_indom;
    }

    public String getPldh_munpn() {
        return pldh_munpn;
    }

    public void setPldh_munpn(String pldh_munpn) {
        this.pldh_munpn = pldh_munpn;
    }

    public String getPldh_nacion() {
        return pldh_nacion;
    }

    public void setPldh_nacion(String pldh_nacion) {
        this.pldh_nacion = pldh_nacion;
    }

    public String getPldh_dirtipvia() {
        return pldh_dirtipvia;
    }

    public void setPldh_dirtipvia(String pldh_dirtipvia) {
        this.pldh_dirtipvia = pldh_dirtipvia;
    }

    public String getPldh_dirnomvia() {
        return pldh_dirnomvia;
    }

    public void setPldh_dirnomvia(String pldh_dirnomvia) {
        this.pldh_dirnomvia = pldh_dirnomvia;
    }

    public String getPldh_dirnumvia() {
        return pldh_dirnumvia;
    }

    public void setPldh_dirnumvia(String pldh_dirnumvia) {
        this.pldh_dirnumvia = pldh_dirnumvia;
    }

    public String getPldh_dirint() {
        return pldh_dirint;
    }

    public void setPldh_dirint(String pldh_dirint) {
        this.pldh_dirint = pldh_dirint;
    }

    public String getPldh_dirtipzon() {
        return pldh_dirtipzon;
    }

    public void setPldh_dirtipzon(String pldh_dirtipzon) {
        this.pldh_dirtipzon = pldh_dirtipzon;
    }

    public String getPldh_dirnomzon() {
        return pldh_dirnomzon;
    }

    public void setPldh_dirnomzon(String pldh_dirnomzon) {
        this.pldh_dirnomzon = pldh_dirnomzon;
    }

    public String getPldh_dirref() {
        return pldh_dirref;
    }

    public void setPldh_dirref(String pldh_dirref) {
        this.pldh_dirref = pldh_dirref;
    }

    public String getPldh_dirubigeo() {
        return pldh_dirubigeo;
    }

    public void setPldh_dirubigeo(String pldh_dirubigeo) {
        this.pldh_dirubigeo = pldh_dirubigeo;
    }

    public String getPldh_usuadd() {
        return pldh_usuadd;
    }

    public void setPldh_usuadd(String pldh_usuadd) {
        this.pldh_usuadd = pldh_usuadd;
    }

    public Date getPldh_fecadd() {
        return pldh_fecadd;
    }

    public void setPldh_fecadd(Date pldh_fecadd) {
        this.pldh_fecadd = pldh_fecadd;
    }

    public String getPldh_usumod() {
        return pldh_usumod;
    }

    public void setPldh_usumod(String pldh_usumod) {
        this.pldh_usumod = pldh_usumod;
    }

    public Date getPldh_fecmod() {
        return pldh_fecmod;
    }

    public void setPldh_fecmod(Date pldh_fecmod) {
        this.pldh_fecmod = pldh_fecmod;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public boolean isValor() {
        valor = pldh_estado == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getS_pldh_fecnac() {
        s_pldh_fecnac = pldh_fecnac == null ? "" : ffecha.format(pldh_fecnac);
        return s_pldh_fecnac;
    }

    public void setS_pldh_fecnac(String s_pldh_fecnac) {
        this.s_pldh_fecnac = s_pldh_fecnac;
    }

    public String getPldh_sexo_des() {
        return pldh_sexo_des;
    }

    public void setPldh_sexo_des(String pldh_sexo_des) {
        this.pldh_sexo_des = pldh_sexo_des;
    }

    public String getPldh_tipdoc_des() {
        return pldh_tipdoc_des;
    }

    public void setPldh_tipdoc_des(String pldh_tipdoc_des) {
        this.pldh_tipdoc_des = pldh_tipdoc_des;
    }

    public String getPldh_nacion_des() {
        return pldh_nacion_des;
    }

    public void setPldh_nacion_des(String pldh_nacion_des) {
        this.pldh_nacion_des = pldh_nacion_des;
    }

    public String getPldh_munpn_des() {
        return pldh_munpn_des;
    }

    public void setPldh_munpn_des(String pldh_munpn_des) {
        this.pldh_munpn_des = pldh_munpn_des;
    }

    public String getPldh_vinfam_des() {
        return pldh_vinfam_des;
    }

    public void setPldh_vinfam_des(String pldh_vinfam_des) {
        this.pldh_vinfam_des = pldh_vinfam_des;
    }

    public String getPldh_situa_des() {
        return pldh_situa_des;
    }

    public void setPldh_situa_des(String pldh_situa_des) {
        this.pldh_situa_des = pldh_situa_des;
    }

    public String getPldh_dirubigeo_des() {
        return pldh_dirubigeo_des;
    }

    public void setPldh_dirubigeo_des(String pldh_dirubigeo_des) {
        this.pldh_dirubigeo_des = pldh_dirubigeo_des;
    }

    public int getPldh_estudios() {
        return pldh_estudios;
    }

    public void setPldh_estudios(int pldh_estudios) {
        this.pldh_estudios = pldh_estudios;
    }

    public int getPldh_discapacidad() {
        return pldh_discapacidad;
    }

    public void setPldh_discapacidad(int pldh_discapacidad) {
        this.pldh_discapacidad = pldh_discapacidad;
    }
    
    
}
