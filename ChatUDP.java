import javax.sound.midi.Receiver;
import javax.swing.*;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// Наследование от JFrame'а говорит о том, что приложение оконное
public class ChatUDP extends JFrame {
    // 6. Рисуем, собственно
    private JTextArea taMain;
    private JTextField tfMsg;
    private final String FRM_TITLE = "Parlons";
    private final int FRM_LOC_X = 100;
    private final int FRM_LOC_Y = 100;
    private final int FRM_WIDTH = 400;
    private final int FRM_HEIGHT = 400;

    // IP и порт чата
    private final int PORT = 9876;
    private final String IP_BROADCAST = "192.168.43.201";
    // InetAddress.getLocalHost(); - чтобы чат запускался на любом компе

    private class theReceiver extends Thread{
        // 1. Наследуем от всех возможный потоков

        // 4. Переписываем метод start.
        @Override
        public void start(){
            super.start();
            System.out.println("Hello from thread");
        }
    }

    // открываем сокет, формируем пакет, отправляем его широко вещаться
    private void btnSend_Handler() throws Exception{
        // создаем сокет
        // предлагает выбор - на каком сокете открываться, нам не важно (отсылающий сокет)
        DatagramSocket sendSocket = new DatagramSocket();
        // зададим наш адрес
        InetAddress IPAddress = InetAddress.getByName(IP_BROADCAST);
        // массив из байтов, который будет приниматься нашим data пакетом
        byte[] sendData;
        // строка, которая отвечает за наш ввод - сразу при объявлении будте принимать в себя то, что написано в нашем текстовом поле - поле сообщения
        String sentence = tfMsg.getText();
        // очистим поле ввода (чтобы не флудить нажимая на кнопку
        tfMsg.setText("");
        // кроссплатформенное приложение, которое может запуститься а маке, линуксе и будет вести себя одинакого, поэтому стандартная кодировка
        sendData = sentence.getBytes("UTF-8");
        // наш пакет
        // передадим наши данные sebdData, нашу длину пакета, айпи адрес и порт получателя
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
        // отправим наш пакет в наш сокет
        sendSocket.send(sendPacket);
    }


    private void frameDraw(JFrame frame){
        // Понадобится текстовое поле и кнопка для отправки
        tfMsg = new JTextField();
        taMain = new JTextArea(FRM_HEIGHT/19,50);

        JScrollPane spMain = new JScrollPane(taMain); // колесо прокрутки
        spMain.setLocation(0,0);
        // чтобы не пришлось крутить колесо горизонтально при длинном сообщении пользователя, обернем строчку переносом
        taMain.setLineWrap(true);
        //
        taMain.setEditable(false);
        // кнопка
        JButton btnSend = new JButton();
        btnSend.setText("Send");
        btnSend.setToolTipText("Broadcast a message");
        // описываем что будет происходить когда мы нажмем на кнопку
        btnSend.addActionListener(e ->{
            try {
                btnSend_Handler(); // запустить, но если что-то пойдет не так, лови исключение ex, выбрось исключение, но не ломай программу
            }catch (Exception ex){
                ex.printStackTrace();
            }
            taMain.append("it works");
        });
        // фрейм должен открываться и закрываться, когда жмем на крестик
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // заголовок фрейма
        frame.setTitle(FRM_TITLE);
        frame.setLocation(FRM_LOC_X,FRM_LOC_Y);
        frame.setSize(FRM_WIDTH,FRM_HEIGHT);
        // пользователь не может менять размер экрана
        frame.setResizable(false);
        // менеджер размещения - колесики вверху
        frame.getContentPane().add(BorderLayout.NORTH, spMain);
        // чтобы смотрелся красиво
        frame.getContentPane().add(BorderLayout.CENTER, tfMsg);
        // кнопка
        frame.getContentPane().add(BorderLayout.EAST, btnSend);
        // делаем эту форму видимой
        frame.setVisible(true);

        // цвета
        // пока никакие коды не работают
        // image icon
        ImageIcon img = new ImageIcon("LogoChat.png");
        frame.setIconImage(img.getImage());
    }

    // 3. Поток создаем вне статического окружения
    private void antistatic(){
        // 5. Рисуем приложение
        frameDraw(new ChatUDP());

    }

    public static void main(String[] args) {
        // new Receiver().start();
        // 2. Не сработает, т.к. 2.

        new ChatUDP().antistatic();
    }
}

// Поток будет выполнять действия, не связанные с тем, что выполняет основная программа.
// Далее пишем пользовательсткий интерфейс