package qiu.li.gao.event;

public class xxEvent {

    public final Object[] message;

    public xxEvent(Object... message) {
        this.message = message;
    }

    public Object[] getMessage() {
        return message;
    }
}
