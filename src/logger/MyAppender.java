package logger;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Mordr on 14.02.2017.
 */
public class MyAppender extends AppenderSkeleton {
    private String username;
    private String password;
    private String spamVictim;
    private String logFileName;
    private String fileEncoding;
    private String zipFileName;

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpamVictim() {
        return spamVictim;
    }

    public void setSpamVictim(String spamVictim) {
        this.spamVictim = spamVictim;
    }

    public String getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public String getZipFileName() {
        return zipFileName;
    }

    public void setZipFileName(String zipFileName) {
        this.zipFileName = zipFileName;
    }

    private void sendEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(spamVictim));
            Date now = new Date();
            message.setSubject("Events log from " + now);
            message.setText("Event log file is 1MB size");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            DataSource source = new FileDataSource(zipFileName);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(zipFileName);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            Transport.send(message);
            System.out.println("email sent");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    private void compressToZip() {
        byte[] buffer = new byte[1024];
        ZipEntry ze= new ZipEntry(logFileName);
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream in = new FileInputStream(logFileName)
        ) {
            zos.putNextEntry(ze);
            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private boolean checkFile(String fileName, String mess) {
        File file = new File(fileName);
        Long bSize = file.length();
        Long kbSize = bSize/1024;
        Long mbSize = kbSize/1024;
        if(mbSize >= 1) {
            compressToZip();
            sendEmail();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        String mess = layout.format(loggingEvent);
        boolean fileSended = checkFile(logFileName, mess);
        try (
                FileWriter fileWriter = new FileWriter(logFileName, !fileSended);
                PrintWriter printWriter = new PrintWriter(fileWriter)
        ) {
            printWriter.println(mess);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
