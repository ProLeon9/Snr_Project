package reducedimension;

import java.util.Objects;

public class ReduceDimensionFactory{

    public ReduceDimensionToolBox createReduceDimensionToolBox(String method){
        if(Objects.equals(method, "PCA"))
            return new PCA();
        else if(Objects.equals(method, "LLE"))
            return new LLE();
        else if(Objects.equals(method, "KPCA"))
            return new KPCA();
        return null;  //TODO: Å×³öÒì³£
    }
}
