package s4.B191868; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

// TODO
import java.util.Arrays;

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
        //                      
        int len_i = mySpace.length - i; // length of suffix_i
        int len_j = mySpace.length - j; // length of suffix_j
        int index = 0; // index to check each character in suffix_i and suffix_j

        while (index < len_i && index < len_j) {
            if (mySpace[i + index] > mySpace[j + index])
                return 1;
            if (mySpace[i + index] < mySpace[j + index])
                return -1;
            index++;
        }
        if (len_i > len_j)
            return 1;
        if (len_i < len_j)
            return -1;
        return 0;
    }

    public void setSpace(byte[] space) { 
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space; 
        // I fix this part to add more checking for the space 
        if (mySpace == null || mySpace.length < 1) {
            spaceReady = false; // in case user do not init new Frequencer
            return;
        } else {
            spaceReady = true;            
        }
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for(int i = 0; i < space.length; i++) {
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
        
        mergeSort(suffixArray, 0, space.length - 1); // use mergeSort to sort suffixArray  
    }

    private void mergeSort(int arr[], int l, int r) {
        // Input: 
        // arr: the original array 
        // int l: left index
        // int r: right index 

        if (l < r) {
            // Find the middle point
            int m = (l + r) / 2;

            // Sort first and second halves
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    private void merge(int arr[], int l, int m, int r) {
        // Input: 
        // arr: the original array 
        // int l: left index
        // int m: middle index
        // int r: right index

        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        int L[] = new int[n1];
        int R[] = new int[n2];

        /* Copy data to temp arrays */
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        /* Merge the temp arrays and store value back to original array*/
        int i = 0, j = 0; // i is for array L, j is for array R
        int k = l; // k is for merged array i.e the original array
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

    // ここから始まり、指定する範囲までは変更してはならないコードである。

    public void setTarget(byte [] target) {
        myTarget = target; 

        // I fix this method to add more checking for the target 
        if (myTarget == null || myTarget.length < 1) {
            targetReady = false; // in case user do not init new Frequencer
        } else {
            targetReady = true;            
        }
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
        //            suffixCompare should return 1. (It was written -1 before 2021/12/21)
        //
        // ここに比較のコードを書け 
        //

        int len_suffix_i = mySpace.length - i; // length of suffix_i
        int len_target = k - j; // length of sub array of myTarget
        int index = 0; // index to check each character in suffix_i and target_j_k

        while (index < len_suffix_i && index < len_target) {
            if (mySpace[i + index] > myTarget[j + index])
                return 1;
            if (mySpace[i + index] < myTarget[j + index])
                return -1;
            index++;
        }

        // if the code reach this line, it means the first part of suffix_i is equal to target_j_k  
        if (len_suffix_i < len_target)
            return -1;
        
        // returns 0 if length of suffix_i >= length of target_j_k   
        return 0;
    }

    // Use binary search for searching the first index that is equal or greater than the target
    private int searchFirst(int low, int high, int start, int end) {
        if (high >= low) {
            int mid = low + (high - low) / 2;
            // if ((mid == 0 || target > arr[mid - 1]) && arr[mid] == target)
            if ((mid == 0 || (targetCompare(suffixArray[mid - 1], start, end) == -1))
                    && (targetCompare(suffixArray[mid], start, end) == 0))
                return mid;
            // else if (target > arr[mid])
            else if (targetCompare(suffixArray[mid], start, end) == -1)
                // search higher half of the arr
                return searchFirst((mid + 1), high, start, end);
            else
                // search lower half of the arr
                return searchFirst(low, (mid - 1), start, end);
        }
        return -1;
    }

    // Use binary search for searching the last index that is greater than the target
    private int searchLast(int low, int high, int start, int end) {
        if (high >= low) {
            int mid = low + (high - low) / 2;
            // if ((mid == n - 1 || target < arr[mid + 1]) && arr[mid] == target)
            if ((mid == suffixArray.length - 1 || (targetCompare(suffixArray[mid + 1], start, end) == 1))
                    && (targetCompare(suffixArray[mid], start, end) == 0))
                return mid;
            // else if (target < arr[mid])
            else if (targetCompare(suffixArray[mid], start, end) == 1)
                // search lower half of the arr
                return searchLast(low, (mid - 1), start, end);
            else
                // search higher half of the arr
                return searchLast((mid + 1), high, start, end);
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
        //                                                                         

        return searchFirst(0, suffixArray.length - 1, start, end);       
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
        //                                                                   
        
        int result = searchLast(0, suffixArray.length - 1, start, end);
        if (result == -1)
            return result;
        return result + 1; // because the finished position is the next position of the last one 
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
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try { // テストに使うのに推奨するmySpaceの文字は、"ABC", "CBA", "HHH", "Hi Ho Hi Ho".
            // Test case 1: ABC
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("ABC".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("B".getBytes());
            
            // **** Print out subByteStartIndex, and subByteEndIndex
            int sub_start1 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start1 + "; ");
            int sub_end1 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end1 + "; ");
            int result1 = frequencerObject.frequency();
            System.out.print("Freq = " + result1 + " ");
            if(1 == result1) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            // Test case 2: CBA
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("CBA".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("CB".getBytes());

            // **** Print out subByteStartIndex, and subByteEndIndex
            int sub_start2 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start2 + "; ");
            int sub_end2 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end2 + "; ");
            int result2 = frequencerObject.frequency();
            System.out.print("Freq = " + result2 + " ");
            if(1 == result2) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            // Test case 3: HHH
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("H".getBytes());

            // **** Print out subByteStartIndex, and subByteEndIndex
            int sub_start3 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start3 + "; ");
            int sub_end3 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end3 + "; ");
            int result3 = frequencerObject.frequency();
            System.out.print("Freq = " + result3 + " ");
            if(3 == result3) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            // Test case 4: Hi Ho Hi Ho
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray();
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

            frequencerObject.setTarget("H".getBytes());
            // ****  Please write code to check subByteStartIndex, and subByteEndIndex
            int sub_start4 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start4 + "; ");
            int sub_end4 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end4 + "; ");
            int result4 = frequencerObject.frequency();
            System.out.print("Freq = " + result4 + " ");
            if(4 == result4) { System.out.println("OK"); } else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("Error occurs: " + e);
        }
    }
}

