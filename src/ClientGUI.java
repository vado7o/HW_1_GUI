import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientGUI extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();

    private final JPanel pannelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JTextField tfLogin = new JTextField();
    private final JPasswordField tfPassword = new JPasswordField("12345");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");
    private final ServerWindow server;
    private boolean isOnline = false;

    ClientGUI(ServerWindow server, String login) {
        this.tfLogin.setText(login);
        this.server = server;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client");

        pannelTop.add(tfIPAddress);
        pannelTop.add(tfPort);
        pannelTop.add(tfLogin);
        pannelTop.add(tfPassword);
        pannelTop.add(btnLogin);
        add(pannelTop, BorderLayout.NORTH);

        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);

        setVisible(true);

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendText(server);
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server.getStatus()) {
                    isOnline = true;
                    log.setText("");
                    log.append("Вы успешно подключились!\n");
                    server.addClient(ClientGUI.this);
                    server.getLog().append("\n" + login + " присоединился к чату!");

                    try {
                        File logFile = new File("log.txt");
                        logFile.createNewFile();
                        log.append(new String(Files.readAllBytes(Paths.get("log.txt"))));
                    } catch (IOException j) {
                        j.printStackTrace();
                    }
                }
                else {
                    log.append("\nСервер пока не активен! Дождитесь запуска сервера!");
                }
            }
        });
    }

    public String getLogin() {
        return this.tfLogin.getText();
    }

    public void setStatus(Boolean bool) {
        isOnline = bool;
    }
    public JTextArea getLog() {
        return log;
    }
    public void sendText(ServerWindow server) {
        if (!server.getStatus()) {
            log.append("\n" + "Сервер не активен!");
        }
        else {
            if (!isOnline) log.append("\nВы не подключены! Нажмите кнопку \"login\"!");
            else {
                log.append("\n" + this.getLogin() + ": " + tfMessage.getText());
                server.getLog().append("\n" + tfLogin.getText() + ": " + tfMessage.getText());

                for (ClientGUI client : server.getClients()) {
                    if (client != ClientGUI.this) {
                        client.log.append("\n" + tfLogin.getText() + ": " + tfMessage.getText());
                    }
                }
                try {
                    String filename = "log.txt";
                    FileWriter fw = new FileWriter(filename, true);
                    fw.write(tfLogin.getText() + ": " + tfMessage.getText() + "\n");
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.append("Ошибка! Не могу сделать запись в файл log.txt!");
                }
            }
        }
        tfMessage.setText("");
    }
}
