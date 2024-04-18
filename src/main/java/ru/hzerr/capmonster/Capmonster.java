package ru.hzerr.capmonster;

import ru.hzerr.capmonster.request.ImageToTextRequest;
import ru.hzerr.capmonster.response.Response;
import ru.hzerr.capmonster.response.impl.ImageToTextData;

public class Capmonster extends AbstractCapmonster {

    public Capmonster(String clientKey) {
        super(clientKey);
    }

    @Override
    public Response<ImageToTextData> send(ImageToTextRequest imageToTextRequest) {
        return null;
    }

    public static Capmonster from(String clientKey) {
        return new Capmonster(clientKey);
    }
}
