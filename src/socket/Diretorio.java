package socket;

import java.io.Serializable;

public class Diretorio implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -3565591827863528676L;

	private String path;
    
	private Boolean diretorio;

    public Diretorio() {
    }

    public Diretorio(String path, Boolean diretorio) {
        this.path = path;
        this.diretorio = diretorio;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean isDiretorio() {
        return diretorio;
    }

    public void setDiretorio(Boolean diretorio) {
        this.diretorio = diretorio;
    }
}
