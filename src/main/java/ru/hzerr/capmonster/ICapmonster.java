package ru.hzerr.capmonster;

import ru.hzerr.capmonster.exception.*;
import ru.hzerr.capmonster.request.ImageToTextRequest;
import ru.hzerr.capmonster.response.Response;
import ru.hzerr.capmonster.response.impl.ImageToTextData;

public interface ICapmonster extends AutoCloseable {

    Response<ImageToTextData> send(ImageToTextRequest request) throws CapmonsterInterruptedOperationException, CapmonsterFailedOperationException, CapmonsterConnectionException, CapmonsterConnectionIOException, CapmonsterInsufficientBalanceException;

    @Override
    void close();
}
