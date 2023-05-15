package io.playdata.security.exception;

public class UsernameLengthException extends Exception{
    public UsernameLengthException(String msg) {
        super(msg);
    }

    public UsernameLengthException(int length) {
        super("Username은" + length + "자 이상이어야 합니다.");
    }
}
