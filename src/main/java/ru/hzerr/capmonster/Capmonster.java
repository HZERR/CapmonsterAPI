package ru.hzerr.capmonster;

import ru.hzerr.capmonster.exception.*;
import ru.hzerr.capmonster.request.ImageToTextRequest;
import ru.hzerr.capmonster.response.Response;
import ru.hzerr.capmonster.response.Status;
import ru.hzerr.capmonster.response.impl.ImageToTextData;
import ru.hzerr.fx.engine.core.annotation.Include;
import ru.hzerr.fx.engine.core.annotation.RegisteredPrototype;
import ru.hzerr.fx.engine.core.annotation.metadata.EngineLogProvider;
import ru.hzerr.fx.engine.core.interfaces.logging.ILogProvider;

import java.math.BigDecimal;

// TODO REALIZE ENGLISH/RUSSIAN LANGUAGE
@RegisteredPrototype
public class Capmonster extends AbstractCapmonster {

    private ILogProvider logProvider;
    private final BigDecimal IMAGE_TO_TEXT_CAPTCHA_PRICE = new BigDecimal("0.0276");

    public Capmonster(String clientKey) {
        super(clientKey);
    }

    @Override
    public Response<ImageToTextData> send(ImageToTextRequest imageToTextRequest) throws CapmonsterInterruptedOperationException, CapmonsterFailedOperationException, CapmonsterConnectionException, CapmonsterConnectionIOException, CapmonsterInsufficientBalanceException {
        if (logProvider != null)
            logProvider.getLogger().debug("Проверяем баланс...");

        BigDecimal balance = getBalance();
        if (logProvider != null)
            logProvider.getLogger().debug("Текущий баланс аккаунта: " + balance.toString());

        if (balance.compareTo(IMAGE_TO_TEXT_CAPTCHA_PRICE) < 0) {
            throw new CapmonsterInsufficientBalanceException("Недостаточный баланс на аккаунте " + balance);
        }

        int taskId = createTask(imageToTextRequest);

        Response<ImageToTextData> response = getTaskResult(ImageToTextData.class, taskId);
        for (int counter = 0; response.getStatus().equals(Status.PROCESSING) || counter <= 60; response = getTaskResult(ImageToTextData.class, taskId)) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new CapmonsterInterruptedOperationException(e.getMessage());
            }

            counter++;
        }

        if (logProvider != null) {
            if (response.getStatus().equals(Status.FAILURE)) {
                logProvider.getLogger().error("Капча не была распознана сервисом. Код ошибки: " + response.getErrorData().getCode());
            } else
                logProvider.getLogger().debug("Капча была успешно распознана сервисом. Ответ: " + response.getData().getText());
        }

        return response;
    }

    @Include(required = false)
    public void setLogProvider(@EngineLogProvider ILogProvider logProvider) {
        this.logProvider = logProvider;
    }

    public static Capmonster from(String clientKey) {
        return new Capmonster(clientKey);
    }
}
