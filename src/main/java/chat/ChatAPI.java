package chat;

/**
 * Created by spatium on 18.07.17.
 */
public class ChatAPI {

    public static MultiPagedMessage.Builder createMultiPagedMessge() {
        return new MultiPagedMessage.Builder();
    }


    public static MultiPagedComponentMessage.Builder createMultiPagedComponnentMessage() {
        return new MultiPagedComponentMessage.Builder();
    }
}
