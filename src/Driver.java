/** The Driver class for CompactPrefixTree */
public class Driver {
        public static void main(String[] args) {

                Dictionary dict = new CompactPrefixTree();

//            dict.add("cat");
//            dict.add("cart");
//            dict.add("carts");
//            dict.add("case");
//            dict.add("doge");
//            dict.add("doghouse");
//            dict.add("wrist");
//            dict.add("wrath");
//            dict.add("wristle");
//            dict.print();


                CompactPrefixTree cpt = new CompactPrefixTree();

                cpt.add("fatted");
                cpt.add("fatten");
                cpt.add("fattened");
                cpt.add("fattener");
            cpt.add("cats");
            cpt.add("demon");
            cpt.add("demons");
            cpt.add("dogs");
                cpt.print();

//          cpt.checkPrefix("ap");
//            System.out.println("Checking word: " + cpt.check("dogs"));

                String[] array = new String[100];
//                array = cpt.suggest("fatte", 100);



//                for(int i = 0; i < array.length; i++) {
//                        System.out.println(array[i]);
//                }
//            cpt.printTree();
//              System.out.println("Checking prefix: " + cpt.checkPrefix("ap"));
//            System.out.println(cpt.check("sandwich"));
////            System.out.println();

        }
}
