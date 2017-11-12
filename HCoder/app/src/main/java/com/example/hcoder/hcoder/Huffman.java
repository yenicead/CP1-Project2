package com.example.hcoder.hcoder;

import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class Huffman {
    static PriorityQueue<Tree> leaves = new PriorityQueue<>((left, right) -> (left.value < right.value) ? -1 : 1);
    static TreeMap<Character, String> leaf_map = new TreeMap<>();


    private static void cleanup_module()
    {
        leaves.clear();
        leaf_map.clear();
    }


    public static String huffman_decoder(String encoded_text)
    {
        String temp_decoder = "";
        Tree node = leaves.peek();

        for (int i = 0; i < encoded_text.length(); )
        {
            Tree tmpNode = node;

            while (tmpNode.left != null && tmpNode.right != null && i < encoded_text.length())
            {
                if (encoded_text.charAt(i) == '1')
                {
                    tmpNode = tmpNode.right;
                }

                else
                {
                    tmpNode = tmpNode.left;
                }

                i++;
            }

            if (tmpNode != null)
            {
                if (tmpNode.character.length() == 1)
                {
                    temp_decoder += tmpNode.character;
                }
                else
                {
                    System.out.println("Error while decoding node.");
                }

            }


        }

        return temp_decoder;
    }


    private static String huffman_encoder(String scanner)
    {
        String temp_encoder = "";
        for (int i = 0; i < scanner.length(); i++)
        {
            temp_encoder += leaf_map.get(scanner.charAt(i));
        }
        return temp_encoder;
    }


    private static void init_tree(PriorityQueue<Tree> vector)
    {
        while (vector.size() > 1)
        {
            vector.add(new Tree(vector.poll(), vector.poll()));
        }
    }


    private static void frequency_calculation(PriorityQueue<Tree> vector, String scanner, int all_characters[])
    {
        for (int i = 0; i < scanner.length(); i++)
        {
            all_characters[scanner.charAt(i)]++;
        }

        for (int i = 0; i < all_characters.length; i++)
        {
            if (all_characters[i] > 0)
            {
                vector.add(new Tree(all_characters[i] / (scanner.length() * 1.0), ((char) i) + ""));
            }
        }
    }


    private static void branch_tree(Tree node, String s)
    {
        if (node != null)
        {
            if (node.right != null)
            {
                branch_tree(node.right, s + "1");
            }

            if (node.left != null)
            {
                branch_tree(node.left, s + "0");
            }

            if (node.left == null && node.right == null)
            {
                leaf_map.put(node.character.charAt(0), s);
            }
        }
    }

    public static String encode(String txt_content){
        cleanup_module();
        int all_characters[] = new int[128];
        frequency_calculation(leaves, txt_content, all_characters);

        init_tree(leaves);
        branch_tree(leaves.peek(), "");

        String encoded_text = null, decoded_text = null;
        encoded_text = huffman_encoder(txt_content);
        return encoded_text;
    }

    // Main function
    public static String Huffman_main (String txt_content)throws FileNotFoundException
    {
        // @SuppressWarnings("resource")
        // String txt_content = new Scanner(new File("input.txt")).useDelimiter("\\Z").next();

        /*
        cleanup_module();
        System.out.println("Text : " + txt_content);

        int all_characters[] = new int[128];
        frequency_calculation(leaves, txt_content, all_characters);
        init_tree(leaves);
        branch_tree(leaves.peek(), "");
        */
        String encoded_text = null, decoded_text = null;
        encoded_text = encode(txt_content);
        // encoded_text = huffman_encoder(txt_content);
        System.out.println("Encoded-Text : " + encoded_text);

        decoded_text = huffman_decoder(encoded_text);
        System.out.println("Decoded-Text : " + decoded_text);
        cleanup_module();
        return decoded_text;
    }

}
