package com.Microservices.Csv2RdfConverter;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.Microservices.Csv2RdfConverter.domain.model.ConvertInfos;
import com.Microservices.Csv2RdfConverter.service.Csv2RdfService;

public class GUI implements ActionListener {

    JFrame frame;
    JPanel panel;
    JButton create;
    JButton loadBtn;
    JLabel label;

    public static File csv;

    private JLabel lblNewLabel, lblPrefix, lblSuperclass, lblDelimiter;
    private JTextField baseField, prefixField, delimiterField, superclassField;
    static JTextArea textArea;
    static JScrollPane scroll;

    Csv2RdfService conv = new Csv2RdfService();

    // create interface
    public GUI() {
        frame = new JFrame();
        panel = new JPanel();

        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        frame.getContentPane().add(panel, BorderLayout.EAST);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 89, 89, 89, 89, 89 };
        gbl_panel.rowHeights = new int[] { 23, 23, 23, 23, 23, 23, 23 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0, 0.0 };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);
        loadBtn = new JButton("Load File");
        loadBtn.setVerticalAlignment(SwingConstants.TOP);

        // action click on Load button
        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser csvFile = new JFileChooser();
                    FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("csv files (*.csv)", "csv");
                    csvFile.addChoosableFileFilter(csvFilter);
                    csvFile.setFileFilter(csvFilter);
                    if (csvFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        csv = csvFile.getSelectedFile();
                    } else {
                        System.out.println("File read failed!");
                        GUI.textArea.append("File read failed!");
                    }
                    label.setText(csv.getName());
                    textArea.setText(null);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        GridBagConstraints gbc_loadBtn = new GridBagConstraints();
        gbc_loadBtn.fill = GridBagConstraints.BOTH;
        gbc_loadBtn.insets = new Insets(0, 0, 5, 5);
        gbc_loadBtn.gridx = 0;
        gbc_loadBtn.gridy = 0;
        panel.add(loadBtn, gbc_loadBtn);
        label = new JLabel();
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.gridwidth = 2;
        gbc_label.fill = GridBagConstraints.BOTH;
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.gridx = 1;
        gbc_label.gridy = 0;
        panel.add(label, gbc_label);

        lblNewLabel = new JLabel("Base URI:");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 1;
        panel.add(lblNewLabel, gbc_lblNewLabel);

        baseField = new JTextField();
        baseField.setText("https://terrain.dd-bim.org/sachdaten/");
        baseField.setToolTipText("Base URI for the RDF file");
        prefixField = new JTextField();
        prefixField.setText("sd");
        prefixField.setToolTipText("Prefix for base URI in Turtle");
        superclassField = new JTextField();
        superclassField.setText("Sachdaten");
        superclassField.setToolTipText("Superclass for all data");
        delimiterField = new JTextField();
        delimiterField.setText(";");
        delimiterField.setToolTipText("Delimiter of csv data");

        GridBagConstraints gbc_baseField = new GridBagConstraints();
        gbc_baseField.gridwidth = 4;
        gbc_baseField.insets = new Insets(0, 0, 5, 5);
        gbc_baseField.fill = GridBagConstraints.HORIZONTAL;
        gbc_baseField.gridx = 1;
        gbc_baseField.gridy = 1;
        panel.add(baseField, gbc_baseField);
        baseField.setColumns(10);

        lblPrefix = new JLabel("Prefix:");
        GridBagConstraints gbc_lblPrefix = new GridBagConstraints();
        gbc_lblPrefix.anchor = GridBagConstraints.EAST;
        gbc_lblPrefix.insets = new Insets(0, 0, 5, 5);
        gbc_lblPrefix.gridx = 0;
        gbc_lblPrefix.gridy = 2;
        panel.add(lblPrefix, gbc_lblPrefix);

        GridBagConstraints gbc_prefixField = new GridBagConstraints();
        gbc_prefixField.gridwidth = 4;
        gbc_prefixField.insets = new Insets(0, 0, 5, 5);
        gbc_prefixField.fill = GridBagConstraints.HORIZONTAL;
        gbc_prefixField.gridx = 1;
        gbc_prefixField.gridy = 2;
        panel.add(prefixField, gbc_prefixField);
        prefixField.setColumns(10);

        lblSuperclass = new JLabel("Superclass:");
        GridBagConstraints gbc_lblSuperclass = new GridBagConstraints();
        gbc_lblSuperclass.anchor = GridBagConstraints.EAST;
        gbc_lblSuperclass.insets = new Insets(0, 0, 5, 5);
        gbc_lblSuperclass.gridx = 0;
        gbc_lblSuperclass.gridy = 3;
        panel.add(lblSuperclass, gbc_lblSuperclass);

        GridBagConstraints gbc_superclassField = new GridBagConstraints();
        gbc_superclassField.gridwidth = 4;
        gbc_superclassField.insets = new Insets(0, 0, 5, 5);
        gbc_superclassField.fill = GridBagConstraints.HORIZONTAL;
        gbc_superclassField.gridx = 1;
        gbc_superclassField.gridy = 3;
        panel.add(superclassField, gbc_superclassField);
        superclassField.setColumns(10);

        lblDelimiter = new JLabel("Delimiter:");
        GridBagConstraints gbc_lblDelimiter = new GridBagConstraints();
        gbc_lblDelimiter.anchor = GridBagConstraints.EAST;
        gbc_lblDelimiter.insets = new Insets(0, 0, 5, 5);
        gbc_lblDelimiter.gridx = 0;
        gbc_lblDelimiter.gridy = 4;
        panel.add(lblDelimiter, gbc_lblDelimiter);

        GridBagConstraints gbc_delimiterField = new GridBagConstraints();
        gbc_delimiterField.gridwidth = 4;
        gbc_delimiterField.insets = new Insets(0, 0, 5, 5);
        gbc_delimiterField.fill = GridBagConstraints.HORIZONTAL;
        gbc_delimiterField.gridx = 1;
        gbc_delimiterField.gridy = 4;
        panel.add(delimiterField, gbc_delimiterField);
        delimiterField.setColumns(10);

        create = new JButton("Create RDF");
        create.addActionListener(this);
        GridBagConstraints gbc_create = new GridBagConstraints();
        gbc_create.fill = GridBagConstraints.BOTH;
        gbc_create.insets = new Insets(0, 0, 5, 5);
        gbc_create.gridx = 0;
        gbc_create.gridy = 5;
        panel.add(create, gbc_create);

        textArea = new JTextArea(5, 4);
        textArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
        GridBagConstraints gbc_textArea = new GridBagConstraints();
        gbc_textArea.insets = new Insets(0, 0, 5, 5);
        gbc_textArea.fill = GridBagConstraints.BOTH;
        gbc_textArea.gridx = 0;
        gbc_textArea.gridy = 6;
        panel.add(textArea, gbc_textArea);

        scroll = new JScrollPane(textArea);
        GridBagConstraints gbc_scroll = new GridBagConstraints();
        gbc_scroll.gridheight = 4;
        gbc_scroll.gridwidth = 5;
        gbc_scroll.fill = GridBagConstraints.BOTH;
        gbc_scroll.insets = new Insets(0, 0, 5, 5);
        gbc_scroll.gridx = 0;
        gbc_scroll.gridy = 6;
        panel.add(scroll, gbc_scroll);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("CSV2RDF");
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        new GUI(); // open GUI
    }

    // action click on Create button
    @Override
    public void actionPerformed(ActionEvent arg0) {

        // get input from window
        String base = baseField.getText();
        String prefix = prefixField.getText();
        String superclass = superclassField.getText();
        String delimiter = delimiterField.getText();

        if (base.isEmpty() || base.equals("")) {
            textArea.append("Please give a Base URI\n");
            return;
        } else if (prefix.isEmpty() || prefix.equals("")) {
            textArea.append("Please give a prefix\n");
            return;
        } else if (superclass.isEmpty() || superclass.equals("")) {
            textArea.append("Please give a superclass\n");
            return;
        } else if (delimiter.isEmpty() || delimiter.equals("")) {
            textArea.append("Please give a delimiter\n");
            return;
        } else if (csv == null) {
            textArea.append("Choose a file!\n");
            return;
        } else {
            textArea.setText("");

            // convert file
            conv.convert(new ConvertInfos(csv, base, prefix, superclass, delimiter));
            textArea.append("File converted.");
        }
    }
}
