package exception;

import javax.servlet.ServletException;

public class NullValueException extends ServletException { 
    public NullValueException(String s) { 
        super(s); 
    } 
}

