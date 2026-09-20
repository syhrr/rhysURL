package exception;

public class ShortUrlNotFoundException extends RuntimeException {
    private final String code;

    public ShortUrlNotFoundException(String code){
        super("No URL found for code: "+ code);
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}


