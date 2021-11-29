package util;

import model.EmailDetail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {
    final String senderEmailID = "starwhitehikkaduwa@gmail.com";
    final String senderPassword = "hikkaduwa";
    final String emailSMTPserver = "smtp.gmail.com";
    final String emailServerPort = "465";
    String receiverEmailID;


    public SendEmail(String receiverEmailID, EmailDetail detail  ) {

        this.receiverEmailID=receiverEmailID;
        Properties props = new Properties();
        props.put("mail.smtp.user",senderEmailID);
        props.put("mail.smtp.host", emailSMTPserver);
        props.put("mail.smtp.port", emailServerPort);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", emailServerPort);
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        try
        {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);
            msg.setText("Thanks For Your Reservation\n\n\n\n\n\n" +
                    "Your reservation ID : "+detail.getId()
                    +'\n'+ "Name : "+detail.getName()
                    +'\n'+ "Check In Date : "+detail.getCheckIn()
                    +'\n'+ "Check Out Date : " +detail.getCheckOut()
                    +'\n'+ '\n'+"Advance : Rs "+detail.getAdvance()
                    +'\n'+"Full amount : Rs "+detail.getCost());

            msg.setSubject("Booking Confirmation");
            msg.setFrom(new InternetAddress(senderEmailID));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(receiverEmailID));
            Transport.send(msg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class SMTPAuthenticator extends javax.mail.Authenticator
    {
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(senderEmailID, senderPassword);
        }
    }
}
