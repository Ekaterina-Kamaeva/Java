import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MainForm extends JFrame {
    private JButton okButton;
    private JTextArea textArea;
    // TODO: 1. Как сделать метод-обработчик
    //       2. Работа с Layout
    //       3. Связь Дизайнера с кодом
    public MainForm() {
        textArea.setSize(150, 50);
        textArea.setLineWrap(true);
        add(okButton, BorderLayout.SOUTH);
        add(textArea, BorderLayout.NORTH);
        setSize(400, 200);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random rnd = new Random();
                for (int i = 0; i < 100; i++) {
                    textArea.append(String.format("%d ", rnd.nextInt()));
                }
            }
        });
    }
}
