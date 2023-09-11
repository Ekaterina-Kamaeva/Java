import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import javax.swing.*;

public class Example
{
    public static void main(String [] args)
    {
        try
        {
            TestDialog dlg = new TestDialog();
            try
            {
                dlg.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                dlg.setModal(true);
                dlg.setVisible(true);
                if (dlg.isFinished())
                {
                    return;
                }
            }
            finally
            {
                dlg.dispose();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    static class TestDialog extends JDialog
    {
        private JTextField m_textControl;
        private Random m_random;
        private JProgressBar m_progress;
        private JButton m_generateButton;
        private JButton m_cancelButton;

        private boolean m_bIsFinished;

        TestDialog()
        {
            m_random = new Random();

            m_bIsFinished = false;

            setTitle("Генератор случайных чисел");
            setLayout(new BorderLayout());
            JPanel basePanel = new JPanel(new BorderLayout());
            add(basePanel, BorderLayout.CENTER);
            basePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

            JPanel controls = new JPanel();
            controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
            panel.add(new JLabel("Случайное число:"), BorderLayout.NORTH);
            m_textControl = new JTextField("");
            m_textControl.setEditable(false);
            panel.add(m_textControl, BorderLayout.SOUTH);
            controls.add(panel);

            panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            panel.add(new JLabel("Прогресс:"), BorderLayout.NORTH);
            m_progress = new JProgressBar(0, 100);
            panel.add(m_progress, BorderLayout.SOUTH);
            controls.add(panel);

            basePanel.add(controls, BorderLayout.NORTH);

            panel = new JPanel(new BorderLayout());
            m_generateButton = new JButton("Генерировать");
            m_generateButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    onOkPressed();
                }
            });
            panel.add(m_generateButton, BorderLayout.WEST);

            m_cancelButton = new JButton("Отменить");
            m_cancelButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    onCancelPressed();
                }
            });
            panel.add(m_cancelButton, BorderLayout.EAST);

            basePanel.add(panel, BorderLayout.SOUTH);

            Dimension screenDim = getToolkit().getScreenSize();
            int iWidth = 400;
            int iHeight = 250;
            setSize(iWidth, iHeight);
            setLocation((screenDim.width - iWidth) / 2, (screenDim.height - iHeight) / 2);

            addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent arg0)
                {
                    onCancelPressed();
                }
            });
        }

        void onOkPressed()
        {
            m_bIsFinished = false;
            m_progress.setValue(0);
            m_progress.setStringPainted(true);
            // Блокируем кнопку, чтобы невозможно было нажать на неё много раз
            m_generateButton.setEnabled(false);
            // Вместо того чтобы блокировать всё приложение, создаётся отдельный поток,
            // в котором происходит изменение Полосы загрузки.
            // При этом приложение, во время заполнения полосы загрузки реагирует на другие события (закрытие окна, нажатие кнопок)
            new Thread(new Runnable() {
                public void run() {
                    m_textControl.setText(getRandomNumber());
                    m_generateButton.setEnabled(true);
                }
            }).start();
        }

        String getRandomNumber()
        {
            for (int iIdx = 0; iIdx < 100; iIdx++)
            {
                setControlState(iIdx);
                delay(100);
            }
            return String.valueOf(m_random.nextInt(1000000));
        }

        private void setControlState(int progressState) {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        m_progress.setValue(progressState);
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        void onCancelPressed()
        {
            m_bIsFinished = true;
            setVisible(false);
        }

        public boolean isFinished()
        {
            return m_bIsFinished;
        }

        private static void delay(long lMillis)
        {
            try
            {
                Thread.sleep(lMillis);
            }
            catch (InterruptedException ex)
            {
            }
        }
    }

}