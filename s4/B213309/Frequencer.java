package s4.B213309;  // ここは、かならず、自分の名前に変えよ。
import java.lang.*;
import s4.specification.*;

import java.io.*;

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
    public void printSuffixArray() {
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
        int suffix_i = i;
	    int suffix_j = j;

        // suffixの文字を一つずつ比較する
	    while(suffix_i < mySpace.length && suffix_j < mySpace.length){
            int diff = mySpace[suffix_i] - mySpace[suffix_j];
            if(diff != 0){
	            return diff / Math.abs(diff); // 絶対値を1にして返す(符号はそのまま)
	        }
	        suffix_i++;
	        suffix_j++;
	    }
        // 先頭から同じsuffixを持つとき、文字列の長さで決定する
        if(i < j){ // suffix_iの方が長い
            return 1;
        } 
        else if(i > j){ // suffix_jの方が長い
            return -1;
        }
        else return 0;  // 同じ
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
        // のようになるべきである
        
        sortMerge(0, suffixArray.length);
        /*　old method
        for(int i = space.length - 1; i > 0; i--){
	        for(int j = 0; j < i; j++){
		        if(suffixCompare(suffixArray[j], suffixArray[j + 1]) > 0){
		            int swap = suffixArray[j];
		            suffixArray[j] = suffixArray[j + 1];
		            suffixArray[j + 1] = swap;
	    	    }
	        }
	    }
        */

    }

    // suffixArryを昇順にソートする関数
    // [start] ~ [end-1]番目の範囲でソートする
    private void sortMerge(int start, int end){
        // 1以下なら返す
        if (end - start <= 1){
            return;
        }

        // 真ん中で分けて1個になるまで分解(再帰)
        int size = end - start;
        int mid = (start + end) / 2;
        sortMerge(start, mid);
        sortMerge(mid, end);

        // [1, 2, 3, 4][5, 6, 7, 8]をソートするなら、
        // 下の配列を作って両端から比較する
        // [1, 2, 3, 4, 8, 7, 6, 5] (suffixcomp)
        // i->                  <-j
        int [] suffixcomp = new int [size];
        for(int i = 0; i < size / 2; i++){
            suffixcomp[i] = suffixArray[start + i];
            suffixcomp[size - 1 - i] = suffixArray[mid + i];
        }
        if(size % 2 == 1)
            suffixcomp[size/2] = suffixArray[mid + size / 2];

        int i = 0;  // iは前から後ろへ
        int j = size - 1;    // jは後ろから前へ
        int k = 0;
        
        // 昇順にマージ
        while(i != j){
            suffixArray[start + k++] = suffixCompare(suffixcomp[i], suffixcomp[j]) > 0 ? suffixcomp[j--] : suffixcomp[i++];
        }
        suffixArray[start + k] = suffixcomp[i];
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
        //            suffixCompare should return 1.
        //
        // ここに比較のコードを書け 
        int suffix_i = i;
        int target_j = j;

        // suffix_iかtarget_j_kの文字列分調べる
        while(suffix_i < mySpace.length && target_j < k){
            int diff = mySpace[suffix_i] - myTarget[target_j];
            // 異なる文字ならば
            if(diff != 0){
	            return diff / Math.abs(diff); // 絶対値を1にして返す(符号はそのまま)
	        }
	        suffix_i++;
	        target_j++;
	    }
        // ここまで両文字列が先頭から同じ文字列
        // target_j_kを全部調べているとき、suffix_iにtarget_j_kは含まれている
        if(!(target_j < k)){
            return 0;
        } // そうでなければtargetの方が長いため-1
        else{
            return -1;
        }
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

        // suffixArrayの順に見ていく
        for (int i = 0; i < suffixArray.length; i++){
            if(targetCompare(suffixArray[i], start, end) == 0){
                return i;
            }
        }                                                                        
        return suffixArray.length; // suffixArray.lengthの値
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
        int target_start_index = subByteStartIndex(start, end);
        // suffixArrayの順に見ていく
        for (int i = target_start_index + 1; i < suffixArray.length; i++){
            if(targetCompare(suffixArray[i], start, end) != 0){
                return i;
            }
        }                                                                        
        return suffixArray.length; // suffixArray.lengthの値      
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
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("ABC".getBytes());
            System.out.println("=============");
            frequencerObject.printSuffixArray();
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("CBA".getBytes());
            System.out.println("=============");
            frequencerObject.printSuffixArray();
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            System.out.println("=============");
            frequencerObject.printSuffixArray();
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            System.out.println("=============");
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
            frequencerObject.setTarget("Ho ".getBytes());
            System.out.println("Ho, start_suffix = " + frequencerObject.subByteStartIndex(0,2));
            System.out.println("Ho, end_suffix = " + frequencerObject.subByteEndIndex(0,2));
            //System.out.println("'Ho' 'Ho'  = " + frequencerObject.targetCompare(9, 0, 2));
            //System.out.println("'Ho' 'Ho ' = " + frequencerObject.targetCompare(1, 0, 3));
            //                                         
            // ****  Please write code to check subByteStartIndex, and subByteEndIndex
            //


            // BenchMarkTest
            // 実行方法
            // $ make Fre
            // $ awk 1 ../data/rand_100k.txt ../data/rand_1k.txt | make runFre
            /*
            if (System.in.available() != 0) {
                InputStreamReader isr = new InputStreamReader(System.in, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String space, target;
                space = br.readLine();
                target = br.readLine();

                long start = System.currentTimeMillis();
                Frequencer myObject;
                myObject = new Frequencer();
                myObject.setSpace(space.getBytes());
	            myObject.setTarget(target.getBytes());
	            myObject.frequency();
                long end = System.currentTimeMillis();

                System.out.println((end - start)  + "ms");
            }
            */
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

