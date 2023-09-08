import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

// Наследуемся от JFrame для того, чтобы внутри класса сразу работать с компонентами формы
public class MainForm extends JFrame {
    // Компоненты формы создаются автоматически и мы можем к ним обращаться внутри класса
    private JButton okButton;
    private JTextArea textArea;
    // Важно!!! Необходимо дать имя корневому элементу!!!
    private JPanel mainPanel;

    public MainForm() {
        // Устанавливаем корневой элемент
        setContentPane(mainPanel);
        setSize(400, 200);
        setVisible(true);
        // Устанавливаем операции закрытия приложения по умолчанию
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Обработка событий
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
