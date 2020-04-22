package entrainement.timer.p7_go4lunch.DI;

import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;

public class DI {
    private static ExtendedServiceCollegue extendedServiceCollegue = new ExtendedServiceCollegue();
    private static ExtendedServicePlace extendedServicePlace= new ExtendedServicePlace();
    public static ExtendedServicePlace getServicePlace(){return extendedServicePlace;}
    public static ExtendedServiceCollegue getService(){ return extendedServiceCollegue;}
}
