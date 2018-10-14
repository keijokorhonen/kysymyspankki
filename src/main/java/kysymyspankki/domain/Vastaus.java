
package kysymyspankki.domain;

public class Vastaus {
    Integer id;
    Kysymys kysymys;
    String vastausteksti;
    Boolean oikein;
    
    public Vastaus(Integer id, Kysymys kysymys, String vastausteksti, Boolean oikein) {
        this.id = id;
        this.kysymys = kysymys;
        this.vastausteksti = vastausteksti;
        this.oikein = oikein;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Kysymys getKysymys() {
        return this.kysymys;
    }
    
    public String getVastausteksti() {
        return this.vastausteksti;
    }
    
    public Boolean getOikein() {
        return this.oikein;
    }
}
