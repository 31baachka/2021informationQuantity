package s4.B213357;  // ここは、かならず、自分の名前に変えよ。
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

    byte [] myTarget; /* 検索する文字列[バイト] */
    byte [] mySpace;  /* 検索対象の文字列[バイト] */
    boolean targetReady = false;
    boolean spaceReady  = false;

    int []  suffixArray; // Suffix Arrayの実装に使うデータの型をint []とせよ。


    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.                                    
    // Each suffix is expressed by a integer, which is the starting position in mySpace. 
                            
    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.                                                                

    // この関数は、デバッグに使ってもよい。mainから実行するときにも使ってよい。
    // リポジトリにpushするとき、main以外からは呼ばれないようにせよ。
    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i < mySpace.length; i++) {
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
        // suffixCompareはソートのための比較メソッド
        //
        // comparing two suffixes by dictionary order.
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD", 
        // suffix_2 is "CD", and sufffix_3 is "D".
        // Each i and j denote suffix_i, and suffix_j.   
        //                         
        // Example of dictionary order                                            
        // "i"      <  "o"        : compare by code                              
        // "Hi"     <  "Ho"       ; if head is same, compare the next element    
        // "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big  
        //  
        // The return value of "int suffixCompare" is as follows. 
        //   if suffix_i > suffix_j, it returns 1   
        //   if suffix_i < suffix_j, it returns -1  
        //   if suffix_i = suffix_j, it returns 0;   

        // ここにコードを記述せよ 	
        int posI = suffixArray[i];
        int posJ = suffixArray[j];

        //文字列を先頭から順番に比較
        while(true) {

            //文字の大小が決まったら、比較結果を格納してループを脱出
            int tmp = Byte.compare(mySpace[posI], mySpace[posJ]);
            if(tmp > 0) return 1;
            if(tmp < 0) return -1;

            //次に比較する位置をずらす
            ++posI;
            ++posJ;
        
            // どちらかの文字列が終端まで見終わった場合
            // 終端まで行った文字列を小さいとしてループを脱出
            if(posI == mySpace.length || posJ == mySpace.length) {
                if(posI > posJ) return -1;
                if(posI < posJ) return 1;
                
                return 0;
            }		
        }
    }


    public void setSpace(byte []space) { 

        // suffixArrayの前処理は、setSpaceで定義
        // space
        mySpace = space;

        if(mySpace == null || mySpace.length == 0) {
            spaceReady = false;
            return;

        }  else {
            spaceReady = true;
        }

        // First, create unsorted suffix array.
        suffixArray = new int[space.length];

        // put all suffixes in suffixArray.
        for(int i = 0; i < space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.      
        }
                                         
        // int[] suffixArrayをソートするコードを書け。
        //  もし、mySpace が "ABC"ならば、
        //  suffixArray = { 0, 1, 2} となること求められる。
        //
        //  このとき、printSuffixArrayを実行すると
        //   suffixArray[0] = 0:ABC
        //   suffixArray[1] = 1:BC
        //   suffixArray[2] = 2:C
        //
        //  もし、mySpace が"CBA"ならば
        //  suffixArray = { 2, 1, 0} となることが求めらる。
        // 
        //  このとき、printSuffixArrayを実行すると
        //   suffixArray[0] = 2:A
        //   suffixArray[1] = 1:BA
        //   suffixArray[2] = 0:CBA


        // マージソート (参考: https://qiita.com/ta7uw/items/fbe35038436bbffea9d6)
        // 平均計算量： O(n log n)
        class MergeSort {

            public void sort(int[] array, int low, int high){
                // マージソート(破壊的)

                if(low < high){
                    int middle = (low + high) >>> 1;

                    // 整列されていないリストを2つのサブリストに分割
                    // サブリストを整列
                    sort(array, low , middle);
                    sort(array, middle+1, high);

                    // サブリストをマージしてひとつの整列済みリストに
                    merge(array, low, middle, high);
                }
            }

            public void merge(int[] array, int low, int middle, int high){
                int[] helper = new int[array.length];

                for (int i = low; i <= high; i++){
                    helper[i] = array[i];
                }
                int helperLeft  = low;
                int helperRight = middle + 1;
                int current     = low;

                while (helperLeft <= middle && helperRight <= high){

                    // suffixCompareを用いて比較
                    /* suffixCompareのテスト 
                    int s = helper[helperLeft];　for(int j=s;j<mySpace.length;j++) {　System.out.print((new String(mySpace)).charAt(j));　}　System.out.write('\n');
                    s = helper[helperRight];　　for(int j=s;j<mySpace.length;j++) {　System.out.print((new String(mySpace)).charAt(j));　}　System.out.write('\n');
                    System.out.println(suffixCompare(helper[helperLeft], helper[helperRight]));
                    */

                    if (suffixCompare(helper[helperLeft], helper[helperRight]) < 0){
                        array[current] = helper[helperLeft];
                        helperLeft ++;

                    } else {
                        array[current] = helper[helperRight];
                        helperRight ++;
                    }
                    current ++;
                }

                int remaining = middle - helperLeft;
                for (int i = 0; i <= remaining; i++){
                    array[current + i] = helper[helperLeft + i];
                }
            }
        }

        // suffixArrayをマージソート
        MergeSort mergeSort = new MergeSort();
        int[] c = suffixArray.clone();
        mergeSort.sort(c, 0, suffixArray.length - 1);
        suffixArray = c;
    }

    // ここから始まり、指定する範囲までは変更してはならないコードである。

    public void setTarget(byte [] target) {
        myTarget = target;
        if(myTarget == null || myTarget.length == 0) targetReady = false;
        else { targetReady = true;}
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
        //
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // When mySpace is "ABCD",
        //  suffix_0  is "ABCD",
        //  suffix_1  is "BCD", 
        //  suffix_2  is "CD", 
        //  sufffix_3 is "D".
        //
        // target_j_k is a string in myTarget start at j-th postion ending k-th position.
        // if myTarget is "ABCD", 
        //   j=0, and k=1 means that target_j_k is "A".
        //   j=1, and k=3 means that target_j_k is "BC".
        //
        // This method compares suffix_i and target_j_k.
        //   if the beginning of suffix_i matches target_j_k, it return 0.
        //   if suffix_i > target_j_k it return 1; 
        //   if suffix_i < target_j_k it return -1;
        //   if first part of suffix_i is equal to target_j_k, it returns 0;
        //
        // Example of search 
        //   suffix          target
        //   "o"       >     "i"
        //   "o"       <     "z"
        //   "o"       =     "o"
        //   "o"       <     "oo"
        //   "Ho"      >     "Hi"
        //   "Ho"      <     "Hz"
        //   "Ho"      =     "Ho"
        //   "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
        //   "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
        //
        // The behavior is different from suffixCompare on this case.
        // For example,
        //    if suffix_i is "Ho Hi Ho", and target_j_k is "Ho", 
        //          targetCompare should return 0;
        //    if suffix_i is "Ho Hi Ho", and suffix_j is "Ho", 
        //          suffixCompare should return -1.
        
        // ここに比較のコードを書け 
        int posI = suffixArray[i];
        int posJ = j;
        int posK = k;

        // 検索文字列が検索対象文字列より長い場合
        if (mySpace.length < posK) {
            return -1;
        }

        // 最初に含んでいるか判定
        boolean isContain = true;
        for (int d = 0; d < (posK - posJ); d++) {
            if(posI + d < mySpace.length) {
                int tmp = Byte.compare(mySpace[posI + d], myTarget[posJ + d]);
                if(tmp != 0) isContain = false;

            } else {
                isContain = false;
            }
        }
        if (isContain) return 0;

        // 大小の判定
        // 文字列を先頭から順番に比較
        // int s = posI; for(int m=s;m<mySpace.length;m++) System.out.print((new String(mySpace)).charAt(m)); System.out.println(); for(int m=posJ;m<posK;m++) System.out.print((new String(myTarget)).charAt(m)); System.out.println();
        
        while(true) {
            // 文字の大小が決まったら、比較結果を格納してループを脱出
            // System.out.print((new String(mySpace)).charAt(posI) + " : "); System.out.print((new String(myTarget)).charAt(posJ) + " : "); System.out.println(Byte.compare(mySpace[posI], myTarget[posJ]));
            
            int result = Byte.compare(mySpace[posI], myTarget[posJ]);
            if(result > 0) return 1;
            if(result < 0) return -1;
        
            //次の文字列の確認位置をずらす
            ++posI;
            ++posJ;

            // どちらかの文字列が終端まで見終わった場合
            // 終端まで行った文字列を小さいとしてループを脱出
            if (posI == mySpace.length) return -1;
            if (posJ == posK - 1) return 1;
        }
    }


    private int binarySearch(int start, int end, boolean isFirst) {
        // バイナリーサーチ

        int lowerBound = 0;
        int upperBound = suffixArray.length - 1; 

        while (lowerBound <= upperBound) {
            int i = (lowerBound + upperBound) / 2;
            int result = targetCompare(i, start, end);

            // トレース用: System.out.println(lowerBound + " " + upperBound + " " + i + " : " + result);
            switch (result) {
                case -1:
                    lowerBound = i + 1;
                    break;

                case 0:
                    // 複数見つかる時
                    if (isFirst) {
                        if (i == 0) {
                            return i;

                        } else if (targetCompare(i-1, start, end) != 0) {
                            return i;

                        } else {
                            upperBound = i - 1;
                            break;    
                        }

                    } else {
                        if (i == suffixArray.length - 1) {
                            return i+1;

                        } else if (targetCompare(i+1, start, end) != 0) {
                            return i+1;

                        } else {
                            lowerBound = i + 1;
                            break;
                        } 
                    }
                
                case 1:
                    upperBound = i - 1;
                    break;     
            }
        }
        return -1;
    }


    private int subByteStartIndex(int start, int end) {
        // suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
 
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
        //  if start = 0, and end = 2, target_start_end is "Ho".
        //  if start = 0, and end = 3, target_start_end is "Ho ".

        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        //  if target_start_end is "Ho", it will return 5.                           
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        //  if target_start_end is "Ho ", it will return 6.                
        //                                                                          
        // ここにコードを記述せよ。    
        /*for (int i = 0; i < suffixArray.length; i++) {
            if (targetCompare(i, start, end) == 0) return i;
        }
        return -1;*/

        return binarySearch(start, end, true); 
    }


    private int subByteEndIndex(int start, int end) {
        // suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド

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
        //   if start = 0, and end = 2, target_start_end is "Hi".
        //   if start = 1, and end = 2, target_start_end is "i".

        // Assuming the suffix array is created from "Hi Ho Hi Ho",                   
        //  if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".  
        // Assuming the suffix array is created from "Hi Ho Hi Ho",          
        //  if target_start_end is "i", it will return 9 for "Hi Ho Hi Ho".    
        //                                                                   
        //　ここにコードを記述せよ
        /*
        for (int i = (suffixArray.length - 1); i >= 0; i--) {
            if (targetCompare(i, start, end) == 0) return (i + 1);
        }
        return -1;
        */

        return binarySearch(start, end, false);    
    }


    // Suffix Arrayを使ったプログラムのホワイトテストは、
    // privateなメソッドとフィールドをアクセスすることが必要なので、
    // クラスに属するstatic mainに書く方法もある。
    // static mainがあっても、呼びださなければよい。
    //
    // 以下は、自由に変更して実験すること。
    // 注意：標準出力、エラー出力にメッセージを出すことは、
    // static mainからの実行のときだけに許される。
    // 外部からFrequencerを使うときにメッセージを出力してはならない。
    // 教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
    // 減点の対象である。
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try { 
            // suffixArrayの生成の確認テスト
            // テストで推奨する mySpaceの文字
            // "ABC", "CBA", "HHH", "Hi Ho Hi Ho".
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("ABC".getBytes());
            frequencerObject.printSuffixArray();

            frequencerObject = new Frequencer();
            frequencerObject.setSpace("CBA".getBytes());
            frequencerObject.printSuffixArray();

            frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            frequencerObject.printSuffixArray();

            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray();

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

            //                                         
            // ****  Please write code to check subByteStartIndex, and subByteEndIndex
            //
            // subByteStartIndexのテスト
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());

            frequencerObject.setTarget("Hi Ho Hi Ho".getBytes());

            int result = frequencerObject.subByteStartIndex(0, 2);
            System.out.print("Start = "+ result+" ");
            if (3 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            result = frequencerObject.subByteStartIndex(3, 5);
            System.out.print("Start = "+ result+" ");
            if (5 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            result = frequencerObject.subByteStartIndex(0, 11);
            System.out.print("Start = "+ result+" ");
            if (4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            result = frequencerObject.subByteStartIndex(0, 0);
            System.out.print("Start = "+ result+" ");
            if (0 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            result = frequencerObject.subByteStartIndex(3, 7);
            System.out.print("Start = "+ result+" ");
            if (6 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            // subByteEndIndexのテスト
            frequencerObject.setTarget("Hi Ho Hi Ho".getBytes());

            result = frequencerObject.subByteEndIndex(0, 2);
            System.out.print("End = "+ result+" ");
            if (5 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            result = frequencerObject.subByteEndIndex(3, 5);
            System.out.print("End = "+ result+" ");
            if (7 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            result = frequencerObject.subByteEndIndex(0, 11);
            System.out.print("End = "+ result+" ");
            if (5 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            result = frequencerObject.subByteEndIndex(0, 0);
            System.out.print("End = "+ result+" ");
            if (11 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            // frequencyのテスト
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());

            frequencerObject.setTarget("H".getBytes());
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            frequencerObject.setTarget("Hi".getBytes());
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(2 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            frequencerObject.setTarget("Hi Ho Hi Ho ".getBytes());
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(0 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            frequencerObject.setTarget("Hi Ho Hi Ho".getBytes());
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(1 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            frequencerObject.setTarget(" ".getBytes());
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(3 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            frequencerObject.setTarget("".getBytes());
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(-1 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            frequencerObject.setTarget(null);
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(-1 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            frequencerObject.setSpace("".getBytes());
            frequencerObject.setTarget("Hi".getBytes());
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(0 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            frequencerObject.setSpace(null);
            frequencerObject.setTarget("Hi".getBytes());
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(0 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

