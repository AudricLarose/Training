package entrainement.timer.p7_go4lunch;

import entrainement.timer.p7_go4lunch.Collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.Restaurant.ExtendedServicePlace;

public class DI {
    private static ExtendedServiceCollegue extendedServiceCollegue = new ExtendedServiceCollegue();
    private static ExtendedServicePlace extendedServicePlace= new ExtendedServicePlace();
    public static ExtendedServicePlace getServicePlace(){return extendedServicePlace;}
    public static ExtendedServiceCollegue getService(){ return extendedServiceCollegue;}
}
