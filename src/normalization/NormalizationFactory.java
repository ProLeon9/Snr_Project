package normalization;

import java.util.Objects;

public class NormalizationFactory{

    public NormalizationToolBox createNormalizationToolBox(String method){
        if(Objects.equals(method, "Z-Score"))
            return new ZScore();
        return null; //TODO: Å×³öÒì³£
    }
}
