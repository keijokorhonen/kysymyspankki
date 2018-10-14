
package kysymyspankki.domain;

public class Kysymys {
    Integer id;
    String kurssi;
    String aihe;
    String kysymysteksti;
    
    public Kysymys(Integer id, String kurssi, String aihe, String kysymysteksti) {
        this.id = id;
        this.kurssi = kurssi;
        this.aihe = aihe;
        this.kysymysteksti = kysymysteksti;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getKurssi() {
        return this.kurssi;
    }
    
    public String getAihe() {
        return this.aihe;
    }
    
    public String getKysymysteksti() {
        return this.kysymysteksti;
    }
}
