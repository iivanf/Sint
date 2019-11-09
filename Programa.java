public class Programa implements Comparable<Programa>{

    private String edadminima;
    private String langs;
    private String NombrePrograma;
    private String Categoria;
    private String HoraInicio;
    private String HoraFin;
    private String Duracion;
    private String Resumen;



	@Override
	public int compareTo(Programa c){
		//Este e o contrario do Disco
		if(this.getNombrePrograma().compareTo(c.getNombrePrograma()) <0 ) return 1;
		else if(this.getNombrePrograma().compareTo(c.getNombrePrograma())  >0   ) return -1;
		return 0;
	}


	public Programa() {
        this.edadminima="";
		this.langs="";
		this.NombrePrograma="";
		this.Categoria="";
        this.HoraInicio="";
        this.HoraFin="";
        this.Duracion="";
        this.Resumen="";
        
	}

	public Programa(String edadminima,String langs,String NombrePrograma, String Categoria, String HoraInicio, String HoraFin,String Duracion , String Resumen) {
		this.edadminima=edadminima;
		this.langs=langs;
		this.NombrePrograma=NombrePrograma;
		this.Categoria=Categoria;
        this.HoraInicio=HoraInicio;
        this.HoraFin=HoraFin;
        this.Duracion=Duracion;
        this.Resumen=Resumen;
	}

    public String getedadminima() {
		return this.edadminima;
    }
    public String getlangs() {
		return this.langs;
	}
	public String getNombrePrograma() {
		return this.NombrePrograma;
	}
	
	public String getCategoria() {
		return this.Categoria;
	}
	public String getHoraInicio() {
		return this.HoraInicio;
    }
    public String getHoraFin() {
		return this.HoraFin;
    }
    public String getDuracion() {
		return this.Duracion;
    }
    public String getResumen() {
		return this.Resumen;
	}

    public void setedadminima(String edadminima) {
		this.edadminima=edadminima;
    }
    public void setlangs(String langs) {
		this.langs=langs;
	}
	public void setNombrePrograma(String NombrePrograma){
		this.NombrePrograma=NombrePrograma;
	}
	public void setlCategoria(String Categoria) {
		this.Categoria=Categoria;
	}
	public void seetHoraInicio(String HoraInicio) {
		this.HoraInicio=HoraInicio;
	}
	public void setHoraFin(String HoraFin) {
		this.HoraFin=HoraFin;
    }
    public void setDuracion(String Duracion) {
		this.Duracion=Duracion;
    }
    public void setResumen(String Resumen) {
		this.Resumen=Resumen;
	}


}