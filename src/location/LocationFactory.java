package location;

import java.util.Objects;

public class LocationFactory{

    public LocationToolBox createLocationToolBox(String method){
        if(Objects.equals(method, "NICV"))
            return new NICV();
        else if(Objects.equals(method, "SOSD"))
            return new SOSD();
        else if(Objects.equals(method, "SOST"))
            return new SOST();

        return null;
    }
}
