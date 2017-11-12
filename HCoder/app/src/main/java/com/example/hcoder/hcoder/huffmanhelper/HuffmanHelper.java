package com.example.hcoder.hcoder.huffmanhelper;

import com.example.hcoder.hcoder.Tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class HuffmanHelper {
    public static String sendmessagetouser(String username, String message) {
        String encoded_text = null, decoded_text = null;
        encoded_text = encode(message, username);
        // encoded_text = huffman_encoder(txt_content);

        return encoded_text;
    }

    public static String readmessagefor(String username) {

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

    public static String encode(String txt_content, String username) {


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

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(username + "_encoded.txt");
            bw = new BufferedWriter(fw);
            bw.write(content.toString());


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

        return encoded_text;
    }

    // Main function
    public static String Huffman_main(String txt_content) throws FileNotFoundException {

//            String encoded_text = null, decoded_text = null;
//            encoded_text = encode(txt_content);
//            // encoded_text = huffman_encoder(txt_content);
//            System.out.println("Encoded-Text : " + encoded_text);
//
//            decoded_text = huffman_decoder(encoded_text);
//            System.out.println("Decoded-Text : " + decoded_text);
//            cleanup_module();
//            return decoded_text;

        return "";
    }
}
