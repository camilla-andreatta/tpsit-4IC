import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class Rubrica extends JFrame implements ActionListener {

    List<Contatto> contacts = new ArrayList<>();

    DefaultListModel<Contatto> modello = new DefaultListModel<Contatto>();
    JList<Contatto> rubrica = new JList<>(modello);
    JScrollPane scroll = new JScrollPane(rubrica);


    JButton aggiungi = new JButton("+");
    JLabel rubricaLabel = new JLabel("Rubrica dei contatti");
    JButton modifica = new JButton("Modifica");
    JButton elimina = new JButton("Elimina");



    Rubrica(){
        setTitle("Rubrica");
        setBounds(20,30,500,700);

        setMinimumSize(new Dimension(500, 700));
        setLayout(null);

        aggiungi.setActionCommand("Aggiungi");
        aggiungi.setBounds(20,40,55,55);
        aggiungi.addActionListener(this);
        add(aggiungi);

        modifica.setBounds(20, 110, 120, 40);
        modifica.setActionCommand("Modifica");
        modifica.addActionListener(this);
        add(modifica);

        elimina.setBounds(20, 170, 120, 40);
        elimina.setActionCommand("Elimina");
        elimina.addActionListener(this);
        add(elimina);

        int paddingRight = 50;
        int paddingBottom = 50;

        int x = 160; // posizione X iniziale
        int y = 50;  // posizione Y iniziale
        int width = getWidth() - x - paddingRight;   // larghezza = finestra - X - paddingRight
        int height = getHeight() - y - paddingBottom; // altezza = finestra - Y - paddingBottom
        scroll.setBounds(x, y, width, height);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {

                int width = getWidth() - x - paddingRight;
                int height = getHeight() - y - paddingBottom;

                scroll.setBounds(x, y, width, height);
            }
        });

        add(scroll);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.rubrica.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Contatto c = (Contatto) value;
                label.setText(c.getNome() + " " + c.getCognome() + " - " + c.getNumero());
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.GRAY));

                label.setFont(new java.awt.Font("Arial", Font.PLAIN, 16));
                return label;
            }
        });

        this.rubrica.setFixedCellHeight(30);
    }


    public void aggiungi(){
        Contatto nuovo = contattoConUtente(null);
        if (nuovo != null) {
            contacts.add(nuovo);
            contacts.sort((o1, o2) -> o1.getNome().compareTo(o2.getNome()));
            modello.clear();
            modello.addAll(contacts);

            // Messaggio di conferma
            JOptionPane.showMessageDialog(null,
                    "Contatto aggiunto correttamente!",
                    "Conferma",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void modifica(){
        int indexContact = this.rubrica.getSelectedIndex();
        if (indexContact != -1) {
           Contatto contact =  this.contacts.get(indexContact);
            contact = this.contattoConUtente(contact);
            contacts.set(indexContact, contact);
            contacts.sort((o1, o2) -> o1.getNome().compareToIgnoreCase(o2.getNome()));

            modello.clear();
            modello.addAll(contacts);

            JOptionPane.showMessageDialog(null,
                    "Contatto modificato correttamente!",
                    "Conferma",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void elimina(){
        int indexContact = this.rubrica.getSelectedIndex();
        if (indexContact != -1) {
            this.contacts.remove(indexContact);
            modello.clear();
            modello.addAll(contacts);

        }
    }

    public Contatto contattoConUtente(Contatto contact){
        //per creare un nuovo contatto se necessario
        //usiamo un array per permettere la modifica del valore all'interno del listener
        final Contatto[] nuovoContatto=new Contatto[1];

        JTextField nomeField = new JTextField();
        JTextField cognomeField = new JTextField();
        JTextField numeroField = new JTextField();

        JLabel nomeLabel = new JLabel("Nome:");
        JLabel cognomeLabel = new JLabel("Cognome:");
        JLabel numeroLabel = new JLabel("Numero:");

        nomeLabel.setBounds(20, 60, 80, 25);
        cognomeLabel.setBounds(20, 100, 80, 25);
        numeroLabel.setBounds(20, 140, 80, 25);

        nomeField.setBounds(120, 60, 250, 25);
        cognomeField.setBounds(120, 100, 250, 25);
        numeroField.setBounds(120, 140, 250, 25);

        //se stiamo modificando un contatto esistente i campi saranno già compilati
        if (contact != null){
            nomeField.setText(contact.getNome());
            cognomeField.setText(contact.getCognome());
            numeroField.setText(contact.getNumero());
        }

        JDialog dialog = new JDialog(this, true);
        dialog.setTitle(contact == null ? "Nuovo Contatto" : "Modifica Contatto");
        dialog.setBounds(100, 100, 420, 260);

        JPanel pannelloInserimento = new JPanel();
        pannelloInserimento.setLayout(null);
        dialog.add(pannelloInserimento);

        JLabel titolo = new JLabel("Inserisci dati contatto");
        titolo.setBounds(120, 10, 200, 30);
        pannelloInserimento.add(titolo);

        JButton okBtn = new JButton("OK");
        JButton annullaBtn = new JButton("Annulla");

        okBtn.setBounds(120, 180, 100, 30);
        annullaBtn.setBounds(230, 180, 100, 30);

        pannelloInserimento.add(okBtn);
        pannelloInserimento.add(annullaBtn);

        pannelloInserimento.add(nomeLabel);
        pannelloInserimento.add(nomeField);
        pannelloInserimento.add(cognomeLabel);
        pannelloInserimento.add(cognomeField);
        pannelloInserimento.add(numeroLabel);
        pannelloInserimento.add(numeroField);


        okBtn.addActionListener(e -> {
            if (contact == null) {
                // Creo un nuovo contatto
                nuovoContatto[0] = new Contatto(
                        nomeField.getText(),
                        cognomeField.getText(),
                        numeroField.getText()
                );
            } else {
                // Modifico il contatto esistente
                contact.setNome(nomeField.getText());
                contact.setCognome(cognomeField.getText());
                contact.setNumero(numeroField.getText());
            }
            dialog.dispose();
        });

        // Pulsante Annulla
        annullaBtn.addActionListener(e -> {
            nuovoContatto[0] = null; // in caso di nuovo contatto
            dialog.dispose();
        });

        dialog.setVisible(true);

        return contact == null ? nuovoContatto[0] : contact;
        // se stiamo modificando, ritorniamo comunque il contatto esistente già aggiornato

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch(command){
            case "Aggiungi":
                aggiungi();
                break;
            case "Modifica":
                modifica();
                break;
            case "Elimina":
                int result = JOptionPane.showConfirmDialog(
                        this,
                        "Sei sicuro di voler eliminare il contatto?",
                        "Conferma",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (result ==JOptionPane.YES_OPTION ) {
                    elimina();
                    JOptionPane.showMessageDialog(null,
                            "Contatto eliminato correttamente!",
                            "Conferma",
                            JOptionPane.INFORMATION_MESSAGE);
                }


                break;
        }
    }


}

