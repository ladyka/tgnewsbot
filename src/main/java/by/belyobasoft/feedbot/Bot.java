package by.belyobasoft.feedbot;

import by.belyobasoft.feedbot.service.BelYobaBotUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
public class Bot extends TelegramLongPollingBot {

	@Autowired
	private BelYobaBotUpdateService belYobaBotUpdateService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${belyobasoft.tg.bot.username}")
	private String username;

	@Value("${belyobasoft.tg.bot.token}")
	private String token;



	@Override
	public void onUpdateReceived(Update update) {
		if (logger.isDebugEnabled()) {
			logger.debug(update.toString());
		}
		if (update.hasMessage()) {
			Message message = update.getMessage();
			SendMessage response = new SendMessage();
			Long chatId = message.getChatId();
			response.setChatId(chatId);
			String text = message.getText();
			response.setText(text);
			try {
				execute(response);
				logger.info("Sent message \"{}\" to {}", text, chatId);
			} catch (TelegramApiException e) {
				logger.error("Failed to send message \"{}\" to {} due to error: {}", text, chatId, e.getMessage());
			}
		}

	}

	@Override
	public String getBotUsername() {
		return username;
	}

	@Override
	public String getBotToken() {
		return token;
	}


	@PostConstruct
	public void start() {
		logger.info("username: {}, token: {}", username, token);
	}
}
