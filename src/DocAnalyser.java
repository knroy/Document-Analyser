import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Komol on 4/27/2016.
 */
public class DocAnalyser {

    private Map<String, ArrayList<String>> docs;
    private ArrayList<String> arrayList;
    private Map<String, int[]> incidenceMatrix;
    private int docNumber;
    private int itemMaxLen;
    private String tmpMapping;
    private String tmpDocMapping;
    private String[] mapItems;
    private Stack<String> op, queryItem;


    public DocAnalyser(int docNumber) {
        this.docNumber = docNumber;
        docs = new HashMap<String, ArrayList<String>>();
        arrayList = new ArrayList<String>();
        incidenceMatrix = new HashMap<String, int[]>();
        op = new Stack<String>();
        queryItem = new Stack<String>();
    }

    public void addNewDOC(String docName, String doc) {

        String[] docSplits = doc.split(" ");
        ArrayList<String> tmp = new ArrayList<String>();
        for (String item : docSplits) {
            item = item.toLowerCase();
            if (!hasAlready(item)) {
                arrayList.add(item);
            }
            if (!tmp.contains(item)) {
                tmp.add(item);
            }
        }

        docs.put(docName, tmp);
    }

    private boolean hasAlready(String item) {
        if (arrayList.contains(item))
            return true;
        return false;
    }

    private void allItemMaking() {
        String tmp = "| ";
        int ln = arrayList.size();
        for (int i = 0; i < ln; i++) {
            if (i > 0) {
                tmp += " | ";
            }
            tmp += arrayList.get(i);
        }
        tmp += " |";
        tmpMapping = tmp;
    }

    private void generateIncidenceMatrix() {
        int ln = arrayList.size();
        for (int i = 1; i <= docNumber; i++) {
            int[] mat = new int[ln];
            String docName = "DOC" + Integer.toString(i);
            ArrayList<String> docItems = docs.get(docName);
            for (int j = 0; j < ln; j++) {
                String item = arrayList.get(j);
                if (docItems.contains(item)) {
                    mat[j] = 1;
                } else {
                    mat[j] = 0;
                }
            }
            incidenceMatrix.put(docName, mat);
        }
    }

    private void printMarks(String docName) {
        int[] mat = incidenceMatrix.get(docName);
        int cnt = 0;
        int sz = tmpMapping.length();
        for (int j = 0; j < sz; j++) {
            if (tmpMapping.charAt(j) != '|') {
                if (j >= 2) {
                    if (tmpMapping.charAt(j - 2) == '|') {
                        System.out.print(mat[cnt]);
                        cnt++;
                    } else {
                        System.out.print(" ");
                    }
                } else {
                    System.out.print(" ");
                }
            } else {
                System.out.print("|");
            }
        }
        System.out.println();
    }

    public void printIncidenceMatrix() {

        System.out.println("\nIncindece Matrix: \n");

        System.out.println("      " + tmpMapping);
        for (int i = 1; i <= docNumber; i++) {
            String docName = "DOC" + Integer.toString(i);
            System.out.print(" " + docName + " ");
            printMarks(docName);
        }
    }

    private void calculateMaxLengthItem() {
        int ln = -1;
        int arrSz = arrayList.size();
        for (int i = 0; i < arrSz; i++) {
            int tmp = arrayList.get(i).length();
            if (tmp > ln)
                ln = tmp;
        }
        itemMaxLen = ln;
    }

    private void docMapping() {
        String tmp = "| ";
        for (int i = 1; i <= docNumber; i++) {
            if (i > 1)
                tmp += " | ";
            tmp += "DOC" + Integer.toString(i);
        }
        tmp += " |";
        tmpDocMapping = tmp;
    }

    private void printDocMaps() {
        System.out.print(" ");
        for (int i = 0; i < itemMaxLen; i++)
            System.out.print(" ");
        System.out.print(" ");
        System.out.println(tmpDocMapping);
    }

    private void generateInvertIncidenceMatrix() {
        int ln = arrayList.size();
        String[] str = new String[ln];
        for (int i = 0; i < ln; i++)
            str[i] = "";
        for (int i = 1; i <= docNumber; i++) {
            int[] mat = incidenceMatrix.get("DOC" + Integer.toString(i));
            for (int j = 0; j < ln; j++) {
                str[j] += Integer.toString(mat[j]);
            }
        }
        mapItems = str;
    }

    public void printInvertIncidenceMatrix() {
        System.out.println("\nInvert Incindece Matrix: \n");
        printDocMaps();
        int ln = arrayList.size();
        for (int i = 0; i < ln; i++) {
            String str = arrayList.get(i);
            System.out.print(" " + str);
            for (int j = 0; j < itemMaxLen - str.length(); j++)
                System.out.print(" ");
            System.out.print(" ");
            String tmp = mapItems[i];
            int cnt = 0;
            for (int j = 0; j < tmpDocMapping.length(); j++) {
                if (tmpDocMapping.charAt(j) != '|') {
                    if (j >= 2) {
                        if (tmpDocMapping.charAt(j - 2) == '|') {
                            System.out.print(tmp.charAt(cnt));
                            cnt++;
                        } else {
                            System.out.print(" ");
                        }
                    } else {
                        System.out.print(" ");
                    }
                } else {
                    System.out.print("|");
                }
            }
            System.out.println();
        }
    }

    public void doProcessInBackground() {
        allItemMaking();
        generateIncidenceMatrix();
        calculateMaxLengthItem();
        docMapping();
        generateInvertIncidenceMatrix();
    }

    private String doOperation(String op, String left, String right) {
        int ln = left.length();
        String tmp = "";
        if (op.equals("AND")) {
            for (int i = 0; i < ln; i++) {
                if (left.charAt(i) == '1' && right.charAt(i) == '1')
                    tmp += '1';
                else
                    tmp += '0';
            }
        } else if (op.equals("OR")) {
            for (int i = 0; i < ln; i++) {
                if (left.charAt(i) == '0' && right.charAt(i) == '0')
                    tmp += '0';
                else
                    tmp += '1';
            }
        } else if (op.equals("NOT")) {
            for (int i = 0; i < ln; i++) {
                if (left.charAt(i) == '1')
                    tmp += '0';
                else
                    tmp += '1';
            }
        }
        return tmp;
    }

    private String doOperation(String str) {
        if (arrayList.contains(str)) {
            int n = arrayList.indexOf(str);
            return mapItems[n];
        }
        return null;
    }

    public String afterQuery(String str) {
        int ln = str.length();
        String tmp = "";
        String advanceQuery = "";
        for (int i = 0; i < ln; i++) {
            if (str.charAt(i) == ')') {
                int sz = tmp.length() - 1;
                while (tmp.charAt(sz) != '(') {
                    char ch = tmp.charAt(sz);
                    advanceQuery = ch + advanceQuery;
                    tmp = removeCharFromLast(tmp);
                    sz--;
                }
                if (tmp.charAt(sz) == '(') {
                    tmp = removeCharFromLast(tmp);
                    tmp += " " + getAdvancedQuery(advanceQuery);
                }
            } else {
                tmp += str.charAt(i);
            }
        }

        return getAdvancedQuery(tmp);
    }

    private boolean isOp(String op) {
        if (op.equals("AND") || op.equals("OR") || op.equals("NOT"))
            return true;
        return false;
    }

    private String removeCharFromLast(String str) {
        str = str.substring(0, str.length() - 1);
        return str;
    }

    private String getAdvancedQuery(String str) {
        String[] items = str.split(" ");
        int ln = items.length;
        if (ln == 1) {
            return doOperation(items[0]);
        } else {
            for (int i = 0; i < ln; i++) {
                if (isOp(items[i])) {
                    op.push(items[i]);
                } else if (items[i].trim().length() != 0) {
                    queryItem.push(items[i]);
                }
            }
            String finalString = "";
            while (!op.empty()) {
                String opt = op.peek();
                op.pop();
                if (opt.equals("NOT")) {
                    String left = queryItem.peek();
                    queryItem.pop();

                    if (!isBinary(left))
                        left = doOperation(left);
                    finalString = doOperation("NOT", left, null);
                    queryItem.push(finalString);
                } else {
                    String right = queryItem.peek();
                    queryItem.pop();
                    if (!isBinary(right)) {
                        right = doOperation(right);
                    }
                    String left = queryItem.peek();
                    queryItem.pop();

                    if (!isBinary(left))
                        left = doOperation(left);
                    finalString = doOperation(opt, left, right);
                    queryItem.push(finalString);
                }
            }
            queryItem.clear();
            op.clear();
            return finalString;
        }

    }

    private boolean isBinary(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != '0' && str.charAt(i) != '1')
                return false;
        }
        return true;
    }

    public void printQueryResults(String afQ) {
        int ln = afQ.length();
        int flag = 0;
        System.out.print(afQ + "=>");
        int findFlag = 0;
        for (int i = 0; i < ln; i++) {
            if (afQ.charAt(i) == '1') {
                if (flag != 0)
                    System.out.print(",");
                flag = 1;
                findFlag = 1;
                System.out.print("DOC" + Integer.toString(i + 1));
            }
        }
        if (findFlag == 0)
            System.out.print("Query not belongs to any DOC");
    }


}
