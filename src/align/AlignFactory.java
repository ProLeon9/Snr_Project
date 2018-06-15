package align;

import java.util.Objects;

public class AlignFactory{

    public AlignToolBox createAlignToolBox(String alignMethod){
        if(Objects.equals(alignMethod, "��̬����")){
            return new StaticAlign();
        }
        else if(Objects.equals(alignMethod, "��̬����")){
            return new DTW();
        }

        return null; // TODO : �׳��쳣
    }
}
