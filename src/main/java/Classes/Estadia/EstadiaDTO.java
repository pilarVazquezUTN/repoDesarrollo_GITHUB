package Classes.Estadia;

import java.util.Date;

public class EstadiaDTO {
    private Date checkin;
    private Date checkout;


    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }
    public void setCheckout(Date checkout) {
        this.checkout = checkout;
    }

    public Date getCheckIn(){
        return this.checkin;
    }
    public Date getCheckOut(){
        return this.checkout;
    }
}
