package com.example.royalapp;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Mail {

    private final static Session SESSION;
    private final static String EMAIL = "royalfinanca@gmail.com";
    private final static String SENHA = "vhdniwxvadivxtsu";
    private final static InternetAddress INTERNET_ADDRESS;

    static {

        try {
            INTERNET_ADDRESS = new InternetAddress(EMAIL);
        } catch (AddressException ex) {
            throw new RuntimeException(ex);
        }

        Properties props = new Properties();
        /**
         * Parâmetros de conexão com servidor Gmail
         */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        SESSION = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL, SENHA);
                    }
                });
    }

    private Mail() {
    }

    @SuppressWarnings("unchecked")
    public static Future<Void> enviar(String titulo, String mensagem, String destinatario) {
        return (Future<Void>) Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Message message = new MimeMessage(SESSION);
                message.setFrom(INTERNET_ADDRESS);


                message.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
                message.setSubject(titulo);//Assunto
                message.setText(mensagem);

                Transport.send(message);
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
