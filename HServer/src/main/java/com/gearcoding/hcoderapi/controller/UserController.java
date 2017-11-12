package com.gearcoding.hcoderapi.controller;

import com.gearcoding.hcoderapi.encoding.Tree;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.DoubleBuffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.script.ScriptEngine.FILENAME;

@RestController
public class UserController {

    @RequestMapping(value = "/sendmessagetouser", method = RequestMethod.GET)
    public String sendmessagetouser(@RequestParam String username, @RequestParam String message) {
        String encoded_text = null, decoded_text = null;
        encoded_text = encode(message, username, true);
        // encoded_text = huffman_encoder(txt_content);

        return encoded_text;
    }

    @RequestMapping(value = "/senduncompressedmessagetouser", method = RequestMethod.GET)
    public String senduncompressedmessagetouser(@RequestParam String username, @RequestParam String message) {

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(username + "_notencoded.txt");
            bw = new BufferedWriter(fw);
            bw.write(message);

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

        return "done";
    }

    @RequestMapping(value = "/readencodedmessagefor", method = RequestMethod.GET)
    public String readmessagefor(@RequestParam String username) {

        Stream<File> files = Arrays.stream(new File(".").listFiles()).filter(f-> f.getName().startsWith(username));

        //dosya isimleri
        List<String> files2= files.map(f -> f.getName()).collect(Collectors.toList());


        if(files2.get(0).contains("_encoded"))
        {
            return getFileContent(files2.get(0));

        }

        else
        {
            return encode(getFileContent(files2.get(0)), username, false);

        }




        /*cleanup_module();

        StringBuilder result = new StringBuilder();
        String path = username + "_encoded.txt";

        BufferedReader br = null;
        FileReader fr = null;


        HashMap<String, Double> table = new HashMap<>();


        boolean isTableDone = false;
        int orginalMessageLength = 0;

        String encodedMessage = null;

        try {

            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(path);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {

                if (sCurrentLine.startsWith("LENGTH=")) {
                    orginalMessageLength = Integer.parseInt(sCurrentLine.substring(7));
                    isTableDone = true;

                    br.readLine();
                    encodedMessage = br.readLine();
                }


                if (!isTableDone) {
                    String[] split = sCurrentLine.split("\\|");
                    table.put(split[0], Double.parseDouble(split[1]));
                }

                System.out.println(sCurrentLine);
                result.append(sCurrentLine + "\n");
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }


        int[] all_characters = new int[128];

        frequency_calculation_already(table, encodedMessage, leaves, orginalMessageLength, all_characters);
        init_tree(leaves);
        branch_tree(leaves.peek(), "");
        return huffman_decoder(encodedMessage);*/
    }

    @RequestMapping(value = "/readdecodedmessagefor", method = RequestMethod.GET)
    public String readdecodedmessagefor(@RequestParam String username) {


        Stream<File> files = Arrays.stream(new File(".").listFiles()).filter(f-> f.getName().startsWith(username));

        //dosya isimleri
        List<String> files2= files.map(f -> f.getName()).collect(Collectors.toList());


        if(files2.get(0).contains("_notencoded"))
        {
            return getFileContent(files2.get(0));

        }

        else
        {


            cleanup_module();

            StringBuilder result = new StringBuilder();
            String path = username + "_encoded.txt";

            BufferedReader br = null;
            FileReader fr = null;


            HashMap<String, Double> table = new HashMap<>();


            boolean isTableDone = false;
            int orginalMessageLength = 0;

            String encodedMessage = null;

            try {

                //br = new BufferedReader(new FileReader(FILENAME));
                fr = new FileReader(path);
                br = new BufferedReader(fr);

                String sCurrentLine;

                while ((sCurrentLine = br.readLine()) != null) {

                    if (sCurrentLine.startsWith("LENGTH=")) {
                        orginalMessageLength = Integer.parseInt(sCurrentLine.substring(7));
                        isTableDone = true;

                        br.readLine();
                        encodedMessage = br.readLine();
                    }


                    if (!isTableDone) {
                        String[] split = sCurrentLine.split("\\|");
                        table.put(split[0], Double.parseDouble(split[1]));
                    }

                    System.out.println(sCurrentLine);
                    result.append(sCurrentLine + "\n");
                }

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                try {

                    if (br != null)
                        br.close();

                    if (fr != null)
                        fr.close();

                } catch (IOException ex) {

                    ex.printStackTrace();

                }
            }


            int[] all_characters = new int[128];

            frequency_calculation_already(table, encodedMessage, leaves, orginalMessageLength, all_characters);
            init_tree(leaves);
            branch_tree(leaves.peek(), "");
            return huffman_decoder(encodedMessage);

        }
    }

    public static String getFileContent(String path){

        StringBuilder sb = new StringBuilder();
        FileReader fr = null;
        BufferedReader br = null;

        try {

            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(path);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                sb.append(sCurrentLine + "\n");
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }

        return sb.toString();
    }

    public static void setFileContent(String path, String content){

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            bw.write(content.toString());

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

    }

    static PriorityQueue<Tree> leaves = new PriorityQueue<>((left, right) -> (left.value < right.value) ? -1 : 1);

    static TreeMap<Character, String> leaf_map = new TreeMap<>();

    private static void cleanup_module() {
        leaves.clear();
        leaf_map.clear();
    }

    private static String huffman_decoder(String encoded_text) {
        String temp_decoder = "";
        Tree node = leaves.peek();

        for (int i = 0; i < encoded_text.length(); ) {
            Tree tmpNode = node;

            while (tmpNode.left != null && tmpNode.right != null && i < encoded_text.length()) {
                if (encoded_text.charAt(i) == '1') {
                    tmpNode = tmpNode.right;
                } else {
                    tmpNode = tmpNode.left;
                }

                i++;
            }

            if (tmpNode != null) {
                if (tmpNode.character.length() == 1) {
                    temp_decoder += tmpNode.character;
                } else {
                    System.out.println("Error while decoding node.");
                }

            }


        }

        return temp_decoder;
    }

    private static String huffman_encoder(String scanner) {
        String temp_encoder = "";
        for (int i = 0; i < scanner.length(); i++) {
            temp_encoder += leaf_map.get(scanner.charAt(i));
        }
        return temp_encoder;
    }

    private static void init_tree(PriorityQueue<Tree> vector) {
        while (vector.size() > 1) {
            vector.add(new Tree(vector.poll(), vector.poll()));
        }
    }

    private static void frequency_calculation(PriorityQueue<Tree> vector, String scanner, int all_characters[]) {
        for (int i = 0; i < scanner.length(); i++) {
            all_characters[scanner.charAt(i)]++;
        }

        for (int i = 0; i < all_characters.length; i++) {
            if (all_characters[i] > 0) {
                vector.add(new Tree(all_characters[i] / (scanner.length() * 1.0), ((char) i) + ""));
            }
        }
    }

    private static void frequency_calculation_already(HashMap<String, Double> table, String encodedMessage, PriorityQueue<Tree> vector, int scannerLenght, int all_characters[]) {
        Iterator it = table.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            all_characters[((String) pair.getKey()).charAt(0)] = ((Double) pair.getValue()).intValue();

            vector.add(new Tree(((Double) pair.getValue()), ((char) ((String) pair.getKey()).charAt(0)) + ""));

        }
    }

    private static void branch_tree(Tree node, String s) {
        if (node != null) {
            if (node.right != null) {
                branch_tree(node.right, s + "1");
            }

            if (node.left != null) {
                branch_tree(node.left, s + "0");
            }

            if (node.left == null && node.right == null) {
                leaf_map.put(node.character.charAt(0), s);
            }
        }
    }

    public static String encode(String txt_content, String username, boolean writeFile) {


        StringBuilder content = new StringBuilder();

        HashMap<String, Double> hm = new HashMap<>();

        cleanup_module();
        int all_characters[] = new int[128];
        frequency_calculation(leaves, txt_content, all_characters);

        leaves.forEach(f -> {
            System.out.format("%s|%f\n", f.character, f.value);
            hm.put(f.character, f.value);

            content.append(f.character + "|" + f.value + "\n");
        });

        init_tree(leaves);


        content.append("LENGTH=" + txt_content.length() + "\n");
        content.append("ENCODED\n");


        branch_tree(leaves.peek(), "");
        String encoded_text = null, decoded_text = null;
        encoded_text = huffman_encoder(txt_content);
        content.append(encoded_text);

        if(writeFile)
            setFileContent(username + "_encoded.txt", content.toString());



        if(writeFile)
            return encoded_text;
        else
            return content.toString();
    }

}
