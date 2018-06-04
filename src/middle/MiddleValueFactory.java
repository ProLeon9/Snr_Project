package middle;

import java.util.Objects;

public class MiddleValueFactory{

    public MiddleValue createMiddleValue(String algorithm){

        if(Objects.equals(algorithm, "AES"))
            return new AES();

        return null;
    }
}
