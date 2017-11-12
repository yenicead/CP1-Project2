package com.example.hcoder.hcoder;

public class Tree {

        public Tree left, right;
        public double value;
        public String character;

        // Creating leaf.
        public Tree(double value, String character)
        {
            this.value = value;
            this.character = character;
            left = null;
            right = null;
        }

        // Creating other nodes.
        public Tree(Tree left, Tree right)
        {
            this.value = left.value + right.value;
            this.character = left.character + right.character;

            if (left.value < right.value)
            {
                this.right = right;
                this.left = left;
            }
            else
            {
                this.right = left;
                this.left = right;
            }
        }

}
