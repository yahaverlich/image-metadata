// package com.thebuzzmedia.exiftool;

package com.drew.metadata;
f
import javax.swing.*;
import java.awt.dnd.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import com.drew.imaging.*;
import com.drew.lang.*;
import com.drew.metadata.*;
import com.drew.tools.*;
import com.drew.imaging.ImageProcessingException;

// import com.thebuzzmedia.exiftool.ExifTool.Feature;
// import com.thebuzzmedia.exiftool.ExifTool.Format;
// import com.thebuzzmedia.exiftool.ExifTool.Tag;






public class Renamer extends JFrame implements ActionListener {

    private JTextField jt;
    private JButton jb;
    private JPanel mainPanel;
    private JPanel textPanel;
    private JTextField jt2;
    private JPanel buttonPanel;
    private File[] files;
    private String namingTemplate;
    private int epStartNum;

    // Initialize the application. Set the UI
    public Renamer() {

        setTitle("Rename your files");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try{
        	runProgram();
        }catch(IOException | ImageProcessingException e){
        	System.out.println(e.getMessage());
        }
    }

    // Calls all the methods that runs the program
    private void runProgram() throws IOException, ImageProcessingException{
        chooseFile();
        setUI();

        // print the file names that were selected
        System.out.println("You chose to open this files: ");
        for(File f : files){
            System.out.println(f.getName());
            System.out.println(f.getAbsolutePath());
            //InputStream targetStream;
            Metadata metadata = null;
            //targetStream = new ByteArrayInputStream(f.getAbsolutePath().getBytes());
            //if(targetStream == null)
            //	throw new IOException("target stream is null");

            //metadata = ImageMetadataReader.readMetadata(targetStream);
            File file = new File("hi.png");

            try {
                metadata = ImageMetadataReader.readMetadata(file);

                System.out.println(metadata + " Using ImageMetadataReader");
            } catch (ImageProcessingException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }

            //if(metadata == null)
            //  	throw new ImageProcessingException("metadata is null");


            if(metadata != null){
                for (Directory directory : metadata.getDirectories()) {
        			for (Tag tag : directory.getTags()) {
            			System.out.println(tag);
        			}
    			}
            }


  // ExifTool tool = new ExifTool();
  //
  // Map<Tag, String> valueMap =
  // 	tool.getImageMeta(f, Tag.GPS_LATITUDE, Tag.GPS_LONGITUDE);
  //
  // System.out.println("Lat: " + valueMap.get(Tag.GPS_LATITUDE) +
  // 					 ", Long: " + valueMap.get(Tag.GPS_LONGITUDE));

        }

    }

    // Rename the file
    private void rename(String name, int startNum){
        int numFiles = files.length;
        System.out.println("num files:" + numFiles);

        File newFilename;
        String strStartNum = "";
        int length = name.length();
        String prefix = name.substring(0,length - 4);
        String postfix = name.substring(length - 4, length);
        // System.out.println(name);
        // System.out.println(prefix);
        // System.out.println(postfix);


        String epName;

            // FIX: Make it not print S02E09 twice --- fix spaces for mac using "\"
        for(File f : files){
            if(startNum < 10){
                strStartNum = Integer.toString(startNum);
                strStartNum = "0" + strStartNum;
            }else{
                strStartNum = Integer.toString(startNum);
            }

            //System.out.println("path is: " + f.getAbsolutePath());
            String path = removeFilenameFromPath(f.getAbsolutePath());
            epName = path + prefix + strStartNum + postfix;
            System.out.println("new Filenames: " + epName);

            newFilename = new File(epName);

            //boolean success = f.renameTo(newFilename);

            // if(!success){
//                 System.err.println("Failed to rename:" + newFilename);
//             }
            startNum++;
        }
    }

    private String removeFilenameFromPath(String text){
        StringCharacterIterator it = new StringCharacterIterator(text);
        int end = it.last();
        while(true){
            if(it.current() != '/'){
                text = text.substring(0, it.getIndex());
                it.previous();
                continue;
            }else{
                text = text.substring(0, it.getIndex() + 1);
                // while(it.getIndex() != it.getBeginIndex()){
                //     if(it.current() == ' '){
                //         text = text.substring(0, it.getIndex()) + "\\" + text.substring(it.getIndex());
                //     }
                //     it.previous();
                // }
                break;
            }
        }

        //System.out.println("My cut string:" + text);
        return text;

    }

    // spawns the file chooser and gets all the file names into the instance variable 'files'
    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setDragEnabled(true);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            files = chooser.getSelectedFiles();
        }else{
            System.exit(0);
        }
    }

    // adds all the panels and buttons and aciton listeners for the UI
    private void setUI(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);

        textPanel = new JPanel();
        jt = new JTextField(20);
        textPanel.add(jt);
        jt2 = new JTextField(5);
        textPanel.add(jt2);

        buttonPanel = new JPanel();
        jb = new JButton("Rename");
        jb.addActionListener(this);
        buttonPanel.add(jb);


        mainPanel.add(textPanel);
        mainPanel.add(buttonPanel);
        pack();
    }

    // called when button is pressed, so that the renaming occurs
    @Override
    public void actionPerformed(ActionEvent e) {
        namingTemplate = jt.getText();
        epStartNum = Integer.parseInt(jt2.getText());
    //    System.out.println(namingTemplate);

        if(namingTemplate != null){
            rename(namingTemplate, epStartNum);
        }else{
            System.out.println("File rename is empty");
        }
    }


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Renamer ex = new Renamer();
            ex.setVisible(true);
        });
    }
}
