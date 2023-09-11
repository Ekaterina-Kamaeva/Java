import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MVCSampleUsingFrameListener extends JFrame
{
    // Вспомогательный класс для сравнения объектов
    public static class Helpers
    {
        public static boolean areEqual(Object o1, Object o2)
        {
            if (o1 == null)
                return o2 == null;
            return o1.equals(o2);
        }
    }

    // Класс для привязки события к кнопке
    public static class ButtonAction extends AbstractAction
    {
        private Runnable m_action;

        public ButtonAction(String actionName, Runnable action)
        {
            super(actionName);
            m_action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            m_action.run();
        }
    }

    // Модель данных
    public static class DataModel
    {
        public static String PROPERTY_NAME = "DataModel.m_text";

        // Объект для наблюдения за изменениями в объекте
        private PropertyChangeSupport propertyChangeDispatcher = new PropertyChangeSupport(this);

        private String m_text;

        public String getText()
        {
            return m_text;
        }

        public void setText(String text)
        {
            if (!Helpers.areEqual(m_text, text))
            {
                String oldValue = m_text;
                m_text = text;
                // Регистрируем изменение объекта
                propertyChangeDispatcher.firePropertyChange(PROPERTY_NAME, oldValue, text);
            }
        }

        // Метод добавления слушателя изменений
        public void addTextChangeListener(PropertyChangeListener listener) {
            propertyChangeDispatcher.addPropertyChangeListener(PROPERTY_NAME, listener);
        }
    }

    // Контроллер малого окна внутри формы
    public static class ModelViewWindow extends JInternalFrame implements PropertyChangeListener
    {
        private DataModel m_model;
        private JTextArea m_textField;
        private JButton m_updateModelButton;

        public ModelViewWindow(DataModel model, int count)
        {
            super("Окно модели #"+count, true, true, true, true);
            m_model = model;
            // Добавление класса который наблюдает за событиями
            m_model.addTextChangeListener(this);

            JPanel panel = new JPanel(new BorderLayout());

            m_textField = new JTextArea();
            m_textField.setPreferredSize(new Dimension(250, 80));
            panel.add(m_textField, BorderLayout.CENTER);

            m_updateModelButton = new JButton(
                    new ButtonAction("запомнить", this::onUpdateModelButton));
//          Различные способы подключения обработчика события
//            m_updateModelButton = new JButton(
//                new ButtonAction("запомнить", () -> onUpdateModelButton()));
//
//            m_updateModelButton = new JButton(
//                new ButtonAction("запомнить", () -> {
//                    System.out.println("Test");
//                    onUpdateModelButton();
//                }));

//            m_updateModelButton = new JButton(
//                new ButtonAction("запомнить",
//                    new Runnable()
//                {
//                    public void run()
//                    {
//                        onUpdateModelButton();
//                    }
//                }));

            JPanel buttonsPanel = new JPanel(new BorderLayout());
            buttonsPanel.add(m_updateModelButton, BorderLayout.EAST);
            panel.add(buttonsPanel, BorderLayout.SOUTH);

            getContentPane().add(panel);
            pack();
        }

        private void onUpdateModelButton()
        {
            // Взаимодействие с моделью
            m_model.setText(m_textField.getText());
        }

        // При событии в модели, меняется текст внутри текстового поля
        @Override
        public void propertyChange(PropertyChangeEvent event)
        {
            if (Helpers.areEqual(m_model, event.getSource()))
            {
                if (Helpers.areEqual(DataModel.PROPERTY_NAME, event.getPropertyName()))
                {
                    onTextChanged();
                }
            }
        }

        private void onTextChanged()
        {
            String text = m_model.getText();
            m_textField.setText(text);
        }
    }

    // Компонент JavaSwing, позволяющий открывать несколько окон внутри себя
    private final JDesktopPane m_desktopPane = new JDesktopPane();
    private DataModel m_model = new DataModel();

    public MVCSampleUsingFrameListener()
    {
        super("MVS sample");
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);

        setContentPane(m_desktopPane);

        // Создание внутри формы трёх окон
        ModelViewWindow window = createModelViewWindow(1);
        addWindow(window);

        ModelViewWindow window1 = createModelViewWindow(2);
        addWindow(window1);

        ModelViewWindow window2 = createModelViewWindow(3);
        addWindow(window2);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected void addWindow(JInternalFrame frame)
    {
        m_desktopPane.add(frame);
        frame.setVisible(true);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
    }

    protected ModelViewWindow createModelViewWindow(int count)
    {
        ModelViewWindow window = new ModelViewWindow(m_model, count);
        window.setLocation(10+200*(count - 1),10+100*(count-1));
        window.setSize(250, 130);
        window.setPreferredSize(window.getSize());
        window.pack();
        return window;
    }

    public static void main(String [] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            SwingUtilities.invokeLater(() -> {
                MVCSampleUsingFrameListener frame = new MVCSampleUsingFrameListener();
                frame.pack();
                frame.setVisible(true);
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}