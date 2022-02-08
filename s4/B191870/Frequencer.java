package s4.B191870; // ここは、かならず、自分の名前に変えよ。

/*import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;*/
import java.lang.*;
import s4.specification.*;


/*package s4.specification;
  ここは、１回、２回と変更のない外部仕様である。
  public interface FrequencerInterface {     // This interface provides the design for frequency counter.
  void setTarget(byte  target[]); // set the data to search.
  void setSpace(byte  space[]);  // set the data to be searched target from.
  int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
  //Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
  //Otherwise, get the frequency of TAGET in SPACE
  int subByteFrequency(int start, int end);
  // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
  // For the incorrect value of START or END, the behavior is undefined.
  }
*/



public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;

    int []  suffixArray; // Suffix Arrayの実装に使うデータの型をint []とせよ。


    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.                                    
    // Each suffix is expressed by a integer, which is the starting position in mySpace. 
                            
    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.                                                                

    // この関数は、デバッグに使ってもよい。mainから実行するときにも使ってよい。
    // リポジトリにpushするときには、mainメッソド以外からは呼ばれないようにせよ。
    //
    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                int s = suffixArray[i];
                System.out.printf("suffixArray[%2d]=%2d:", i, s);
                for(int j=s;j<mySpace.length;j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }

    private int suffixCompare(int i, int j) {
        // suffixCompareはソートのための比較メソッドである。
        // 次のように定義せよ。
        //
        // comparing two suffixes by dictionary order.
        //String suffix_i ;

        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD",
        

        // suffix_2 is "CD", and sufffix_3 is "D".
        // Each i and j denote suffix_i, and suffix_j.                            
        // Example of dictionary order                                            
        // "i"      <  "o"        : compare by code                              
        // "Hi"     <  "Ho"       ; if head is same, compare the next element    
        // "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big  
        //  
        //The return value of "int suffixCompare" is as follows. 
        // if suffix_i > suffix_j, it returns 1   
        // if suffix_i < suffix_j, it returns -1  
        // if suffix_i = suffix_j, it returns 0;   

        // ここにコードを記述せよ 
        //suffix starting with i will have length of mySpace.length - i , so as suffix j

        //go though all elements in shoter suffix
        for (int k = 0 ; ((k < mySpace.length-i) && (k < mySpace.length-j));k++) {
            if (mySpace[i + k] > mySpace[j + k]) 
                return 1;
            else if (mySpace[i + k] < mySpace[j + k])
                return -1;
            continue;
        }
        //if program can reach here,
        //that means,the shorter suffix is completely same with a part of longer suffix 
        if (i < j) // suffix_i longer than suffix_j
            return 1;
        if (i > j) //          shorter
            return -1;
        return 0; // or two suffix have same length

    }

    public void setSpace(byte []space) { 
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space; if(mySpace.length>0) spaceReady = true;
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.      
        }
        //                                            
        // ここに、int suffixArrayをソートするコードを書け。

        // もし、mySpace が"ABC"ならば、
        // suffixArray = { 0, 1, 2} となること求められる。
        // このとき、printSuffixArrayを実行すると
        //   suffixArray[ 0]= 0:ABC
        //   suffixArray[ 1]= 1:BC
        //   suffixArray[ 2]= 2:C
        // のようになるべきである。
        // もし、mySpace が"CBA"ならば
        // suffixArray = { 2, 1, 0} となることが求めらる。
        // このとき、printSuffixArrayを実行すると
        //   suffixArray[ 0]= 2:A
        //   suffixArray[ 1]= 1:BA
        //   suffixArray[ 2]= 0:CBA
        // のようになるべきである。

        /*int temp;
        for(int x = 0; x < suffixArray.length; x++) {
            for(int y = 0; y < suffixArray.length -x -1; y++) {
                if(suffixCompare(suffixArray[y], suffixArray[y+1]) == 1) {
                    temp = suffixArray[y];
                    suffixArray[y] = suffixArray[y+1];
                    suffixArray[y+1] = temp;
                } else {
                    continue;
                }
            }
        }*/
        //merge sort method is defined below
        merge_sort(suffixArray, 0, space.length - 1); 
    }

    private void merge(int arr[], int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
  
        /* Create temp arrays */
        int L[] = new int[n1];
        int R[] = new int[n2];
  
        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];
  
        /* Merge the temp arrays */
  
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
  
        // Initial index of merged subarray array
        int k = l;
        
        while (i < n1 && j < n2) {
            // use suffixCompare to compare the corresponding suffix
            if (suffixCompare(L[i], R[j]) == -1 || suffixCompare(L[i], R[j]) == 0) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
  
    }
  
    // Main function that sorts arr[l..r] using
    // merge()
    private void merge_sort(int arr[], int l, int r)
    {
        if (l < r) {
            // Find the middle point
            int m =l+ (r-l)/2;
  
            // Sort first and second halves
            merge_sort(arr, l, m);
            merge_sort(arr, m + 1, r);
  
            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    // ここから始まり、指定する範囲までは変更してはならないコードである。

    public void setTarget(byte [] target) {
        myTarget = target; if(myTarget.length>0) targetReady = true;
    }

    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }


    public int subByteFrequency(int start, int end) {
        // start, and end specify a string to search in myTarget,
        // if myTarget is "ABCD", 
        //     start=0, and end=1 means string "A".
        //     start=1, and end=3 means string "BC".
        // This method returns how many the string appears in my Space.
        // 
        /* This method should be work as follows, but much more efficient.
           int spaceLength = mySpace.length;                      
           int count = 0;                                        
           for(int offset = 0; offset< spaceLength - (end - start); offset++) {
            boolean abort = false; 
            for(int i = 0; i< (end - start); i++) {
             if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
            }
            if(abort == false) { count++; }
           }
        */
        // The following the counting method using suffix array.
        // 演習の内容は、適切なsubByteStartIndexとsubByteEndIndexを定義することである。
        int first = subByteStartIndex(start, end);
        int last1 = subByteEndIndex(start, end);
        return last1 - first;
    }
    // 変更してはいけないコードはここまで。

    private int targetCompare(int i, int j, int k) {
        // subByteStartIndexとsubByteEndIndexを定義するときに使う比較関数。
        // 次のように定義せよ。
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD", 
        // suffix_2 is "CD", and sufffix_3 is "D".
        // target_j_k is a string in myTarget start at j-th postion ending k-th position.
        // if myTarget is "ABCD", 
        //     j=0, and k=1 means that target_j_k is "A".
        //     j=1, and k=3 means that target_j_k is "BC".
        // This method compares suffix_i and target_j_k.
        // if the beginning of suffix_i matches target_j_k, it return 0.
        // if suffix_i > target_j_k it return 1; 
        // if suffix_i < target_j_k it return -1;
        // if first part of suffix_i is equal to target_j_k, it returns 0;
        //
        // Example of search 
        // suffix          target
        // "o"       >     "i"
        // "o"       <     "z"
        // "o"       =     "o"
        // "o"       <     "oo"
        // "Ho"      >     "Hi"
        // "Ho"      <     "Hz"
        // "Ho"      =     "Ho"
        // "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
        // "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
        // The behavior is different from suffixCompare on this case.
        // For example,
        //    if suffix_i is "Ho Hi Ho", and target_j_k is "Ho", 
        //            targetCompare should return 0;
        //    if suffix_i is "Ho Hi Ho", and suffix_j is "Ho", 
        //            suffixCompare should return -1.
        //
        // ここに比較のコードを書け 
        /*byte [] suffix_i = new byte[mySpace.length-i];
        byte [] target_j_k = new byte[k-j];

        for ( int x = i; x< mySpace.length; x++){
            suffix_i[x-i] = mySpace[x];
        }

        for (int y = j ; y< k ; y++) {
            target_j_k[y-j] =  myTarget[y];
        }

        int min = Math.min(suffix_i.length,target_j_k.length);

        for (int z = 0 ; z < min ; z++) {
            if (suffix_i[z] < target_j_k[z]) {
                return -1;
            }
            else if (suffix_i[z] > target_j_k[z]) {
                return 1;
            }
            else continue;

        }

        if (suffix_i.length < target_j_k.length) {
            return -1;
        } else return 0; */
        //targer_j_k has length of k-j
        for (int x = 0 ;(x < mySpace.length-i && x < (k-j));x++) {
            if (mySpace[i + x] < myTarget[j + x]) 
                return -1;
       else if (mySpace[i + x] > myTarget[j + x])
                return 1;
            continue;
        }
        //if program can reach here,
        //that means,the shorter suffix is completely same with a part of longer suffix 
        if (mySpace.length-i < (k-j)) // suffix_i shorter than target_k_j
            return -1;
        //else
        else return 0; 
        
    }
    //using binary search to find target's start,end index
    private int Bsearch(int low,int high,int target_start ,int target_end, int mode ) {

        //find lower bound when mode setting is 0
        if (mode == 0){
 
            if (low > high){
                return low;
            }
            int mid = low + (high-low)/2;
            if (targetCompare(suffixArray[mid], target_start, target_end) == -1)
                return Bsearch(mid+1,high,target_start, target_end,0);
            else
                return Bsearch(low,mid-1,target_start,target_end,0);  
        }
        
        //find upper bound when mode setting is 1
        if (mode == 1) {

            if (low > high){
                return low;
            }
            int mid = low + (high-low)/2;
            if (targetCompare(suffixArray[mid], target_start, target_end) == 1)
                return Bsearch(low, mid - 1, target_start, target_end,1);
            else
                return Bsearch(mid + 1, high, target_start, target_end,1);     
        }

        return -1;
    }


    private int subByteStartIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho
           1: Ho
           2: Ho Hi Ho
           3:Hi Ho
           4:Hi Ho Hi Ho
           5:Ho
           6:Ho Hi Ho
           7:i Ho
           8:i Ho Hi Ho
           9:o
          10:o Hi Ho
        */

        // It returns the index of the first suffix 
        // which is equal or greater than target_start_end.                         
	// Suppose target is set "Ho Ho Ho Ho"
        // if start = 0, and end = 2, target_start_end is "Ho".
        // if start = 0, and end = 3, target_start_end is "Ho ".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        // if target_start_end is "Ho", it will return 5.                           
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        // if target_start_end is "Ho ", it will return 6.                
        //                                                                          
        // ここにコードを記述せよ。 
                                                                          
        return Bsearch(0,suffixArray.length-1,start,end,0);       
    }

    private int subByteEndIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho                                    
           1: Ho                                       
           2: Ho Hi Ho                                 
           3:Hi Ho                                     
           4:Hi Ho Hi Ho                              
           5:Ho                                      
           6:Ho Hi Ho                                
           7:i Ho                                    
           8:i Ho Hi Ho                              
           9:o                                       
          10:o Hi Ho                                 
        */
        // It returns the index of the first suffix 
        // which is greater than target_start_end; (and not equal to target_start_end)
	// Suppose target is set "High_and_Low",
        // if start = 0, and end = 2, target_start_end is "Hi".
        // if start = 1, and end = 2, target_start_end is "i".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                   
        // if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".  
        // Assuming the suffix array is created from "Hi Ho Hi Ho",          
        // if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".    
        //                                                                   
        //　ここにコードを記述せよ                                        
                                                                     
        return Bsearch(0,suffixArray.length-1,start,end,1);       
    }


    // Suffix Arrayを使ったプログラムのホワイトテストは、
    // privateなメソッドとフィールドをアクセスすることが必要なので、
    // クラスに属するstatic mainに書く方法もある。
    // static mainがあっても、呼びださなければよい。
    // 以下は、自由に変更して実験すること。
    // 注意：標準出力、エラー出力にメッセージを出すことは、
    // static mainからの実行のときだけに許される。
    // 外部からFrequencerを使うときにメッセージを出力してはならない。
    // 教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
    // 減点の対象である。

    
    /*
    public static String readFileAsString(String filename) { 
        String text = ""; 
    try { 
        text = new String(Files.readAllBytes(Paths.get("/Users/macbook/vanouy/2021informationQuantity/s4/data/"+filename)));
     } 
        catch (IOException e) 
            { e.printStackTrace(); } 
        return text; 
    }
    */
    
        
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try { // テストに使うのに推奨するmySpaceの文字は、"ABC", "CBA", "HHH", "Hi Ho Hi Ho".
            //test case ABC
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("ABC".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("BC".getBytes());
            int start_index = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex of BC is " + start_index + "\n");
            int end_index = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex of BC is " + end_index + "\n");
            System.out.println("Freq="+frequencerObject.frequency());


            //test case ababab
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("ababab".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("abab".getBytes());
            int start_index1 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex of abab is " + start_index1 + "\n");
            int end_index1 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex of abab is " + end_index1 + "\n");
            System.out.println("Freq="+frequencerObject.frequency());

            
            //case "HHH",target is H
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("H".getBytes());
            int start_index2 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex of H is " + start_index2 + "\n");
            int end_index2 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex of H is " + end_index2 + "\n");
            System.out.println("Freq="+frequencerObject.frequency());
            
            
            
            // test on given data txt.file
            /*
            String a = readFileAsString("space_100k.txt");
            String b = readFileAsString("target_1k.txt");
            frequencerObject = new Frequencer();
            frequencerObject.setSpace(a.getBytes());
            frequencerObject.setTarget(b.getBytes());                               
            int start_index3 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex of "+frequencerObject.myTarget+" is " + start_index3 + "\n");
            int end_index3 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex of "+frequencerObject.myTarget+" is " + end_index3 + "\n");
            System.out.println("Freq="+frequencerObject.frequency());
            */

            //frequencerObject.printSuffixArray();
            /* Example from "Hi Ho Hi Ho"    
               0: Hi Ho                      
               1: Ho                         
               2: Ho Hi Ho                   
               3:Hi Ho                       
               4:Hi Ho Hi Ho                 
               5:Ho                          
               6:Ho Hi Ho
               7:i Ho                        
               8:i Ho Hi Ho                  
               9:o                           
              10:o Hi Ho                     
            */
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("H".getBytes());
            int start_index4 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex of H is " + start_index4 + "\n");
            int end_index4 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex of H is " + end_index4 + "\n");
            System.out.println("Freq="+frequencerObject.frequency());
            
            
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

