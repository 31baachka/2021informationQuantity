package s4.B213304;  // ここは、かならず、自分の名前に変えよ。
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

    // B191870の学生とペアプログラミングを行ったので、この関数はその学生と同じになる。
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
	byte [] suffix_i = new byte[mySpace.length-i];                                        
	byte [] suffix_j = new byte[mySpace.length-j];

	//System.out.println("¥n");
	for (int x = i; x < mySpace.length; x++) {
		suffix_i[x-i] = mySpace[x];
		//System.out.write(suffix_i[x-i]);
	}                                        
	
	//System.out.println("¥n");
	for (int y = j; y < mySpace.length; y++) {
		suffix_j[y-j] = mySpace[y];
		//System.out.write(suffix_j[y-j]);
	}                                     

	//System.out.println("¥n");
	int min = Math.min(suffix_i.length,suffix_j.length);

        for (int z = 0 ; z < min ; z++) {
            if (suffix_i[z] < suffix_j[z]) {
                return -1;
            }
            else if (suffix_i[z] > suffix_j[z]) {
                return 1;
            }
            else continue;

        }

        if (suffix_i.length < suffix_j.length) {
            return -1;
        } else if (suffix_i.length == suffix_j.length) {
            return 0;
        } else return 1;
	   
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
    
	int temp;
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

    // B191870の学生とペアプログラミングを行ったので、この関数はその学生と同じになる。
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
        //
    	
	byte [] suffix_i = new byte[mySpace.length-i];
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
        } else return 0; 
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
        
	byte [] target_start_end = new byte[end-start];
	boolean isFound = false;

	for(int k = 0; k < target_start_end.length; k++) {
		target_start_end[k] = myTarget[k+start];
	}

	for(int i=0; i< mySpace.length; i++) {
                int s = suffixArray[i];
		int j = 0;
		if(mySpace.length - s >= target_start_end.length) {
		   while(j < target_start_end.length) {
                        if(mySpace[s+j] == target_start_end[j]) {
                                j++;
                                isFound = true;
                        } else {
                                isFound = false;
                                break;
                        }
                   }  
		}
                if(isFound) return i;
        }

   	return suffixArray.length; 
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
        // ここにコードを記述せよ                                           
        //                                                                   
	byte [] target_start_end = new byte[end-start];
        boolean isFound = true;

	for(int k = 0; k < target_start_end.length; k++) {
		target_start_end[k] = myTarget[k+start];
	}
	
	int i;
        for(i=subByteStartIndex(start, end); i< mySpace.length; i++) {
                int s = suffixArray[i];
                int j = 0;
                if(mySpace.length - s >= target_start_end.length) {
                   while(j < target_start_end.length) {
                        if(mySpace[s+j] == target_start_end[j]) {
                                j++;
                                isFound = true;
                        } else {
                                isFound = false;
                                break;
                        }
                   }
                   if(!isFound) return i;
                } else return i;
        }

	return i;	
	//return suffixArray.length; // この行は変更しなければならない、       
    }

    public void printTarget() {
    	if(spaceReady) {
		System.out.printf("Target : ");
		for(int i=0; i<myTarget.length; i++) {
			System.out.write(myTarget[i]);
		}
		System.out.printf("\n");
	}
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
            // 以下のテストの動作はB191870の学生のコードを参考にし、改良したものである。

	    // TestCase is "ABC"
	    frequencerObject = new Frequencer();
            frequencerObject.setSpace("ABC".getBytes());
            frequencerObject.printSuffixArray();
	    frequencerObject.setTarget("B".getBytes());
	    frequencerObject.printTarget();
	    int start_index = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
	    System.out.print("subByteStartIndex of the target is " + start_index + "\n");
            int end_index = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex of the target is " + end_index + "\n");            
            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+"\n");

	    // TestCase is "CBA"
	    frequencerObject = new Frequencer();
            frequencerObject.setSpace("CBA".getBytes());
            frequencerObject.printSuffixArray();
	    frequencerObject.setTarget("BA".getBytes());
	    frequencerObject.printTarget();
	    start_index = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
	    System.out.print("subByteStartIndex of the target is " + start_index + "\n");
            end_index = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex of the target is " + end_index + "\n");            
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+"\n");
            
	    // TestCase is "HHH"
	    frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            frequencerObject.printSuffixArray();
	    frequencerObject.setTarget("HHH".getBytes());
	    frequencerObject.printTarget();
	    start_index = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
	    System.out.print("subByteStartIndex of the target is " + start_index + "\n");
            end_index = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex of the target is " + end_index + "\n");            
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+"\n");
            
	    // TestCase is "HHH"
	    frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            frequencerObject.printSuffixArray();
	    frequencerObject.setTarget("BB".getBytes());
	    frequencerObject.printTarget();
	    start_index = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
	    System.out.print("subByteStartIndex of the target is " + start_index + "\n");
            end_index = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex of the target is " + end_index + "\n");            
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+"\n");

	    // TestCase is "Hi Ho Hi Ho"
	    frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray();
	    frequencerObject.setTarget("Hi".getBytes());
	    frequencerObject.printTarget();
	    start_index = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
	    System.out.print("subByteStartIndex of the target is " + start_index + "\n");
            end_index = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex of the target is " + end_index + "\n");            
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+"\n");
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
 
            //                                         
            // ****  Please write code to check subByteStartIndex, and subByteEndIndex
            //

	    frequencerObject.setTarget("H".getBytes());
	    frequencerObject.printTarget();
            result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
	}
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

