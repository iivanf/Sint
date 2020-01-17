package p2;
public class Canal implements Comparable<Canal>{

    private String lang;
    private String idCanal;
    private String NombreCanal;
	private String Grupo;

	@Override
	public int compareTo(Canal c){
		//por orden alfabetico do nome
		if(this.getNombreCanal().compareTo(c.getNombreCanal()) >0 ) return 1;
		else if(this.getNombreCanal().compareTo(c.getNombreCanal())  <0   ) return -1;
		return 0;
	}


	public Canal() {
		this.lang="";
		this.idCanal="";
		this.NombreCanal="";
		this.Grupo="";
	}

	public Canal(String lang,String idCanal,String NombreCanal, String Grupo) {
		this.lang=lang;
		this.idCanal=idCanal;
		this.NombreCanal=NombreCanal;
		this.Grupo=Grupo;
	}


	public String getNombreCanal() {
		return this.NombreCanal;
	}
	public String getlang() {
		return this.lang;
	}
	public String getidCanal() {
		return this.idCanal;
	}
	public String getGrupo() {
		return this.Grupo;
	}

	public void setNombreCanal(String NombreCanal){
		this.NombreCanal=NombreCanal;
	}
	public void setlang(String lang) {
		this.lang=lang;
	}
	public void setidCanal(String idCanal) {
		this.idCanal=idCanal;
	}
	public void setGrupo(String Grupo) {
		this.Grupo=Grupo;
	}

}