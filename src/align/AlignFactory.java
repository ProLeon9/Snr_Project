package align;

import java.util.Objects;

public class AlignFactory{

    public AlignToolBox createAlignToolBox(String alignMethod){
        if(Objects.equals(alignMethod, "静态对齐")){
            return new StaticAlign();
        }
        else if(Objects.equals(alignMethod, "动态对齐")){
            return new DTW();
        }

        return null; // TODO : 抛出异常
    }
}
