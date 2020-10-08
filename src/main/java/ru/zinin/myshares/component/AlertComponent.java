package ru.zinin.myshares.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.zinin.myshares.model.Alert;
import ru.zinin.myshares.repo.AlertRepo;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class AlertComponent {

    @Value("${ameritrade.key}")
    private String key;

    private final EmailServiceImpl emailService;
    private final JavaMailSender mailSender;
    private final AlertRepo alertRepo;

    public AlertComponent(EmailServiceImpl emailService, JavaMailSender mailSender, AlertRepo alertRepo) {
        this.emailService = emailService;
        this.mailSender = mailSender;
        this.alertRepo = alertRepo;
    }

//        @Scheduled(cron = "0 0/10 16-23 * * MON-FRI", zone = "Europe/Moscow")
//        @Scheduled(cron = "0 0/15 * * * ?", zone = "Europe/Moscow") // для тестов
    @Scheduled(cron = "0 */2 * * * ?", zone = "Europe/Moscow") // для тестов
    public void alertCtrl() throws IOException, MessagingException {

        // находим все алерты в базе
        List<Alert> alerts = alertRepo.findAll();

        if (alerts == null || alerts.size() == 0) return;

        // получаем сет уникальных тикеров
        Set<String> tickers = alerts.stream().map(alert -> alert.getTicker()).collect(Collectors.toSet());

        // создаем строку из тикеров
        StringJoiner joiner = new StringJoiner(",");
        tickers.forEach(t -> joiner.add(t));
        String tickersString = joiner.toString();

        // URL для запроса
        String url = "https://api.tdameritrade.com/v1/marketdata/quotes?apikey=" + key + "&symbol=" + tickersString;

        ObjectMapper objectMapper = new ObjectMapper();

        // получаем мар с инфо по тикерам
        Map map = objectMapper.readValue(new URL(url), Map.class);

        for (Alert alert : alerts) {
            if (alert.getAction().equals("intersectsValueUp")) {
                double lastPrice = (double) ((Map) map.get(alert.getTicker())).get("lastPrice");
                if (lastPrice > alert.getValue()) {
                    MimeMessage message = mailSender.createMimeMessage();
                    boolean multipart = true;
                    MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
                    String htmlMsg =
                            "<!DOCTYPE html>" +
                                    "<html lang=\"en\">" +
                                    "<head>" +
                                    "  <meta charset=\"UTF-8\">\n" +
                                    "  <title>Title</title>\n" +
                                    "</head>" +
                                    "<body>" +
                                    "<div  style=\"border: red 2px solid; margin: 30px\">" +
                                    "  <h1 align=\"center\" style=\"margin: 10px\">Внимание!</h1>" +
                                    "  <p style=\"margin: 10px; font-size: 1.3rem\">" +
                                    "    Тикер <b>"+alert.getTicker()+"</b> пересек значение " +
                                    "<b>"+alert.getValue()+"</b> <span style=\"color: green\"><b>вверх</b></span>" +
                                    "  </p>" +
                                    "</div>" +
                                    "</body>" +
                                    "</html>";
                    message.setContent(htmlMsg, "text/html; charset=UTF-8");
                    helper.setTo(alert.getEmail());
                    helper.setSubject("Alert!");
                    helper.setFrom("alert@vzinin.ru");
                    mailSender.send(message);

                    alertRepo.delete(alert);
                }
            }
            if (alert.getAction().equals("intersectsValueDown")) {
                double lastPrice = (double) ((Map) map.get(alert.getTicker())).get("lastPrice");
                if (lastPrice < alert.getValue()) {
                    MimeMessage message = mailSender.createMimeMessage();
                    boolean multipart = true;
                    MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
                    String htmlMsg =
                            "<!DOCTYPE html>" +
                                    "<html lang=\"en\">" +
                                    "<head>" +
                                    "  <meta charset=\"UTF-8\">\n" +
                                    "  <title>Title</title>\n" +
                                    "</head>" +
                                    "<body>" +
                                    "<div  style=\"border: red 2px solid; margin: 30px\">" +
                                    "  <h1 align=\"center\" style=\"margin: 10px\">Внимание!</h1>" +
                                    "  <p style=\"margin: 10px; font-size: 1.3rem\">" +
                                    "    Тикер <b>"+alert.getTicker()+"</b> пересек значение " +
                                    "<b>"+alert.getValue()+"</b> <span style=\"color: red\"><b>вниз</b></span>" +
                                    "  </p>" +
                                    "</div>" +
                                    "</body>" +
                                    "</html>";
                    message.setContent(htmlMsg, "text/html; charset=UTF-8");
                    helper.setTo(alert.getEmail());
                    helper.setSubject("Alert!");
                    helper.setFrom("alert@vzinin.ru");
                    mailSender.send(message);

                    alertRepo.delete(alert);
                }
            }
            if (alert.getAction().equals("growth(percent)")) {
                double lastPrice = (double) ((Map) map.get(alert.getTicker())).get("lastPrice");
                if (lastPrice < alert.getCtrlValue()) {
                    alert.setCtrlValue(lastPrice);
                    alertRepo.save(alert);
                } else if (lastPrice > alert.getCtrlValue()) {
                    double percents = (lastPrice - alert.getCtrlValue()) / alert.getCtrlValue() * 100;
                    if (percents > alert.getValue()) {
                        MimeMessage message = mailSender.createMimeMessage();
                        boolean multipart = true;
                        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
                        String htmlMsg =
                                "<div  style=\"border: red 2px solid; margin: 30px auto; max-width: 600px\">" +
                                        "  <h1 style=\"margin: 10px\" align=\"center\">Внимание!</h1>" +
                                        "  <p style=\"margin: 10px; font-size: 1.3rem\">" +
                                        "    Тикер <b>"+alert.getTicker()+"</b> вырос на <b>"+String.format("%.2f",percents)+"%</b>" +
                                        "  </p>" +
                                        "</div>";
                        message.setContent(htmlMsg, "text/html; charset=UTF-8");
                        helper.setTo(alert.getEmail());
                        helper.setSubject("Alert!");
                        helper.setFrom("alert@vzinin.ru");
                        mailSender.send(message);

                        alertRepo.delete(alert);
                    }
                }
            }
            if (alert.getAction().equals("drop(percent)")) {
                double lastPrice = (double) ((Map) map.get(alert.getTicker())).get("lastPrice");
                if (lastPrice > alert.getCtrlValue()) {
                    alert.setCtrlValue(lastPrice);
                    alertRepo.save(alert);
                } else if (lastPrice < alert.getCtrlValue()) {
                    double percents = (lastPrice - alert.getCtrlValue()) / alert.getCtrlValue() * 100;
                    if (Math.abs(percents) > alert.getValue()) {
                        MimeMessage message = mailSender.createMimeMessage();
                        boolean multipart = true;
                        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
                        String htmlMsg =
                                "<div  style=\"border: red 2px solid; margin: 30px auto; max-width: 600px\">" +
                                        "  <h1 style=\"margin: 10px\" align=\"center\">Внимание!</h1>" +
                                        "  <p style=\"margin: 10px; font-size: 1.3rem\">" +
                                        "    Тикер <b>"+alert.getTicker()+"</b> упал на <b>"+String.format("%.2f",  Math.abs(percents))+"%</b>" +
                                        "  </p>" +
                                        "</div>";
                        message.setContent(htmlMsg, "text/html; charset=UTF-8");
                        helper.setTo(alert.getEmail());
                        helper.setSubject("Alert!");
                        helper.setFrom("alert@vzinin.ru");
                        mailSender.send(message);

                        alertRepo.delete(alert);
                    }
                }
            }
        }
    }
}
