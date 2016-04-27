import java.util.Scanner;
import java.util.Stack;

/**
 * Created by Komol on 4/27/2016.
 */
public class Main {

    private static int n;
    private static Scanner IN = new Scanner(System.in);
    private static DocAnalyser docAnalyser;

    private static Stack<String> op, args;

    public static void main(String[] args) {

        System.out.print("Enter the total DOC numbers: ");
        n = IN.nextInt();
        IN.nextLine();

        docAnalyser = new DocAnalyser(n);

        for (int i = 1; i <= n; i++) {
            String docName = "DOC" + Integer.toString(i);
            //System.out.print("Enter DOC" + Integer.toString(i) + " ");
            String doc = IN.nextLine();
            docAnalyser.addNewDOC(docName, doc);
        }

        docAnalyser.doProcessInBackground();

        docAnalyser.printIncidenceMatrix();
        docAnalyser.printInvertIncidenceMatrix();

        System.out.println("\nEnter Queries: \n");

        String query = IN.nextLine();
        while (!query.equals("#")) {
            String afQ = docAnalyser.afterQuery(query);
            docAnalyser.printQueryResults(afQ);
            System.out.println();
            query = IN.nextLine();
        }
    }
}

        /*

Breakthrough drug for schizophrenia
new schizophrenia
new approach for treatment of schizophrenia
new hopes for schizophrenia patients
drug for prevent schizophrenia
         */