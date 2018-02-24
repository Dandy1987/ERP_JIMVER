package org.me.gj.model.facturacion.procesos;

public class NotesVentaCab extends PedidoVentaCab {

    private String notes_tip;
    private String notes_des;
    private String notes_nro;
    private String sup_des;
    private int notes_sit;
    private int clibolfac;

    public String getNotes_tip() {
        return notes_tip;
    }

    public void setNotes_tip(String notes_tip) {
        this.notes_tip = notes_tip;
    }

    public String getNotes_des() {
        return notes_des;
    }

    public void setNotes_des(String notes_des) {
        this.notes_des = notes_des;
    }

    public String getNotes_nro() {
        return notes_nro;
    }

    public void setNotes_nro(String notes_nro) {
        this.notes_nro = notes_nro;
    }

    public String getSup_des() {
        return sup_des;
    }

    public void setSup_des(String sup_des) {
        this.sup_des = sup_des;
    }

    public int getNotes_sit() {
        return notes_sit;
    }

    public void setNotes_sit(int notes_sit) {
        this.notes_sit = notes_sit;
    }

	public int getClibolfac() {
		return clibolfac;
	}

	public void setClibolfac(int clibolfac) {
		this.clibolfac = clibolfac;
	}
    
    

}
