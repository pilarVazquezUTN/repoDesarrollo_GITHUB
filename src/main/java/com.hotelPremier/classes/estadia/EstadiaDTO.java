package Classes.Estadia;

import java.util.Date;

public class EstadiaDTO {
    private Date checkin;
    private Date checkout;

    /**
     * set de checkin
     * @param checkin
     */
    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }

    /**
     * set de checkout
     * @param checkout
     */
    public void setCheckout(Date checkout) {
        this.checkout = checkout;
    }

    /**
     * get de checkin
     * @return
     */

    public Date getCheckIn(){
        return this.checkin;
    }

    /**
     * get de checkout
     * @return
     */
    public Date getCheckOut(){
        return this.checkout;
    }
}
