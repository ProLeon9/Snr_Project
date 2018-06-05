package reducenoice;

import java.util.Objects;

public class ReduceNoiceFactory{

    public ReduceNoiceToolBox createRedeceNoiceToolBox(String method){
        if(Objects.equals(method, "FFT"))
            return new FFT();
        if(Objects.equals(method, "POC"))
            return new POC();
        if(Objects.equals(method, "¿¨¶ûÂüÂË²¨"))
            return new KalmanFilter();
        if(Objects.equals(method, "SSA"))
            return new SSA();
        if(Objects.equals(method, "ICA"))
            return new ICA();

        return null; //TODO£º Å×³öÒì³£
    }
}
