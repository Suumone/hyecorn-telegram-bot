package com.professional.telegram_hyecorn_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
public class TelegramHyecornBotApplication {

	public static void main(String[] args) throws TelegramApiException {


		SpringApplication.run(TelegramHyecornBotApplication.class, args);

		//telegramBotsApi.registerBot();

		/*MainBot bot = new MainBot(new DefaultBotOptions());
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		telegramBotsApi.registerBot(bot);*/
	}

}
