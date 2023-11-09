import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ServerWindow extends JFrame {
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JTextArea log = new JTextArea();
    private final JPanel pannelTop = new JPanel(new BorderLayout());
    private final JPanel panelBottom = new JPanel(new GridLayout(1, 2));
    private boolean isServerWorking;
    private ArrayList<ClientGUI> clients = new ArrayList<>();

    public static void main(String[] args) {
        new ServerWindow();
    }

    public boolean getStatus() {
        return this.isServerWorking;
    }
    public JTextArea getLog() {
        return log;
    }
    public void addClient(ClientGUI client) {
        clients.add(client);
    }
    public void removeClient(ClientGUI client) {
        clients.remove(client);
    }
    public ArrayList<ClientGUI> getClients() {
        return clients;
    }

    public ServerWindow() {
        isServerWorking = false;
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isServerWorking) log.append("\n" + "Сервер уже остановлен ранее!");
                else log.append("\n" + "Сервер остановлен!");
                isServerWorking = false;
                System.out.println("Server stopped " + isServerWorking + "\n");

                for (ClientGUI client : clients) {
                    client.getLog().append("\nСервер разорвал соединение! Попробуйет соединиться снова!");
                    client.setStatus(false);
                }
                clients.clear();
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isServerWorking) log.append("\n" + "Сервер уже запущен ранее!");
                else log.append("\n" + "Сервер запущен!");
                isServerWorking = true;
                System.out.println("Server started " + isServerWorking + "\n");

            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);
        panelBottom.add(btnStart);
        panelBottom.add(btnStop);
        add(panelBottom, BorderLayout.SOUTH);
        add(pannelTop, BorderLayout.NORTH);

        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);

        setVisible(true);
    }
}
