package ru.hzerr.capmonster;

import ru.hzerr.capmonster.request.ImageToTextRequest;
import ru.hzerr.capmonster.response.Response;
import ru.hzerr.capmonster.response.impl.ImageToTextData;

public interface ICapmonster {

    Response<ImageToTextData> send(ImageToTextRequest request);
}
