package org.jjcouple.drug.etc;
import com.google.firebase.database.PropertyName;

public class Drug {

    public String side;
    public String drug1;
    public String drug2;


    public Drug(){}

    @PropertyName("detail")
    public String getSide() {
        return side;
    }
    @PropertyName("detail")
    public void setSide(String side) {
        this.side = side;
    }

    @PropertyName("drugA")
    public String getDrug1() {
        return drug1;
    }
    @PropertyName("drugA")
    public void setDrug1(String drug1) {
        this.drug1 = drug1;
    }

    @PropertyName("drugB")
    public String getDrug2() {
        return drug2;
    }
    @PropertyName("drugB")
    public void setDrug2(String drug2) {
        this.drug2 = drug2;
    }

}
