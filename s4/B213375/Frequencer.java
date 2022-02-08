package s4.B213375; // s4.studentID
import java.lang.*;
import java.util.Arrays;

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

    static boolean debug = false;

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
	if(i == j) return 0; 

	int len;
	byte[] suffix_i = new byte[mySpace.length - i];
	byte[] suffix_j = new byte[mySpace.length - j];

	for(int num = 0; num < mySpace.length - i; num++){
		suffix_i[num] = mySpace[i + num];
	}

	for(int num = 0; num < mySpace.length - j; num++){
		suffix_j[num] = mySpace[j + num];
	}

	for(int num = 0; (num<suffix_i.length)&&(num<suffix_j.length); num++){
		if(suffix_i[num] > suffix_j[num]) return 1;
		else if (suffix_i[num] < suffix_j[num]) return -1; 
	}

        if(i > j) return -1;
	else return 1; 
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
	
        // count1s[substringIndex] = (counted1s)
        int[] count1s = new int[mySpace.length];
        for(int i = 0; i<mySpace.length; i++) count1s[i] = 0; //initialize with 0s
        for(int i = 0; i<mySpace.length; i++){//forall substrings...
            for(int j = 0; j<mySpace.length; j++)//... compare against all others:
                if(suffixCompare(i, j)==1)
                    count1s[i] += 1; //and count 1s
        }
        // suffixArray[(counted1s)] := substringIndex
        for(int i = 0; i<mySpace.length; i++)
            suffixArray[count1s[i]] = i;
            //note for the future: make sure that count1s is exactly the set of integers 0,1,...,mySpace.length-1 for any input!!
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
        //
        return 0; // この行は変更しなければならない。
    }


    private int subByteStartIndex(int start, int end)/*works*/{
        /*  SuffixArrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド。
            ""Returns the index of the first suffix which is
            equal or greater than target_start_end.""
            i.e. the smallest i such that strcmp(target.substr(s,e),space.substr(i))>=0
            The meaning of start and end is the same as subByteFrequency.
         */
        //-----The following are examples assuming SPACE:"Hi Ho Hi Ho"
        /* SuffixArray[i] for "Hi Ho Hi Ho": *note leading whitespaces
            [ 0]= 5: Hi Ho
            [ 1]= 8: Ho
            [ 2]= 2: Ho Hi Ho
            [ 3]= 6:Hi Ho
            [ 4]= 0:Hi Ho Hi Ho
            [ 5]= 9:Ho
            [ 6]= 3:Ho Hi Ho
            [ 7]= 7:i Ho
            [ 8]= 1:i Ho Hi Ho
            [ 9]=10:o
            [10]= 4:o Hi Ho
        */
        /*  EXAMPLE 1:  target : "Ho Ho Ho Ho"
                        start = 0, end = 2
                        target_start_end : "Ho"
            returns 5
            Because SuffixArray[5]:"Ho" is the FIRST entry (searching in sorted order)
            from SuffixArray (the dictionary of suffixes) that MATCHES _OR_ FOLLOWS "Ho".
            i.e. the smallest i such that strcmp(target.substr(s,e),space.substr(i))>=0
        */
        /*  EXAMPLE 2:  target : "Ho Ho Ho Ho"
                        start = 0, end = 3
                        target_start_end : "Ho_"
            returns 6
            Because SuffixArray[6]:"Ho_" is the FIRST entry (searching in sorted order)
            from SuffixArray (the dictionary of suffixes) that MATCHES _OR_ FOLLOWS "Ho_".
            i.e. the smallest i such that strcmp(target.substr(s,e),space.substr(i))>=0
            Note that "Ho" DOES NOT MATCH "Ho_" because they are not exactly equal; and
            also that "Ho" DOES NOT FOLLOW "Ho_" because the former is shorter and the
            proper sorted BYTE-Lexicographic order is {...,Hn,...,Ho,Ho_,Ho__,...,Hoa,...}.
        */

        String space = new String(this.mySpace);
        String target = new String(this.myTarget);
        String target_start_end = target.substring(start,end);
        for(int i=0;i<suffixArray.length;i++)
            if(target_start_end.compareTo(space.substring(suffixArray[i]))<=0)
                return i;/*smallest i such that target.substr DOES NOT PRECEDE SuffixArray[i]
                    (but note that SA stores indirectly as: start_index of space.substr)*/
        return suffixArray.length; /*This end-line is reached...
            when target_start_end PRECEDES ALL entries (& particularly the last entry) in
            SuffixArray (the dictionary of suffix substrings of SPACE), also implying that
            there is no match.*/
    }

    private int subByteEndIndex(int start, int end)/*works*/{
        /*  SuffixArrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド。
            ""Returns the index of the first suffix which is
            greater than target_start_end; (and not equal to target_start_end).""
            i.e. actually, returns the smallest i such that
                suffix.startsWith(target_s_e) is false
                but suffix does NOT precede target_s_e ASCII-Lexicographically
            The meaning of start and end is the same as subByteFrequency.
         */
        //-----The following are examples assuming SPACE:"Hi Ho Hi Ho"
        /* SuffixArray[i] for "Hi Ho Hi Ho": *note leading whitespaces
            [ 0]= 5: Hi Ho
            [ 1]= 8: Ho
            [ 2]= 2: Ho Hi Ho
            [ 3]= 6:Hi Ho
            [ 4]= 0:Hi Ho Hi Ho
            [ 5]= 9:Ho
            [ 6]= 3:Ho Hi Ho
            [ 7]= 7:i Ho
            [ 8]= 1:i Ho Hi Ho
            [ 9]=10:o
            [10]= 4:o Hi Ho
        */
        /*  EXAMPLE 1:  target : "HoHoHo"
                        start = 0, end = 2
                        target_start_end : "Ho"
            returns 7
            Because SuffixArray[7]:"Ho" is the FIRST entry (searching in sorted order
            AND IGNORING all entries before the first MATCH)
            from SuffixArray (the dictionary of suffixes) that
            DOESN'T MATCH _OR_ DOESN'T START WITH "Ho".
            Refer to the following sorted list of strings in ASCII-Lexicographic order:
                SA[3]=6:    Hi_Ho       #ignored...
                SA[4]=0:    Hi_Ho_Hi_Ho #ignored...
                target_s_e: Ho          # <-
                SA[5]=9:    Ho          #STARTSWITH(Ho) (and also EXACTMATCH(Ho))
                SA[6]=3:    Ho_Hi_Ho    #STARTSWITH(Ho)
                SA[7]=7:    i_Ho        #fail! (smallest i such that fail : 7)
                SA[8]=1:    i_Ho_Hi_Ho  #fail!
        */
        /*  EXAMPLE 2:  target : "High and Low"
                        start = 1, end = 2
                        target_start_end : "i"
            returns
            Because SuffixArray[]:"Ho" is the FIRST entry (searching in sorted order
            AND IGNORING all entries before the first MATCH)
            from SuffixArray (the dictionary of suffixes) that
            DOESN'T MATCH _OR_ DOESN'T START WITH "Ho".
            Refer to the following sorted list of strings in ASCII-Lexicographic order:
                SA[3]=6:    Hi_Ho       #ignored...
                SA[4]=0:    Hi_Ho_Hi_Ho #ignored...
                SA[5]=9:    Ho          #ignored...
                SA[6]=3:    Ho_Hi_Ho    #ignored...
                target_s_e: i           # <-
                SA[7]=7:    i_Ho        #STARTSWITH(i)
                SA[8]=1:    i_Ho_Hi_Ho  #STARTSWITH(i)
                SA[9]=10:   o           #fail! (smallest i such that fail : 9)
                SA[10]=4:   o_Hi_Ho     #fail!
        */
        /*
        * Note with insight that the relevance of this analysis (as can be seen through
        * the examples) is that the number of cases where
        *   the suffix from SA[x] STARTSWITH(target.substr(start,end))
        *   (including, if it exists, the EXACTMATCH case of SA[x]==target.substr(start,end))
        * is a count of the distinct instances of the substring TARGET.substr(start,end) in
        * the main string SPACE.
        * */

        int index_of_first_match_candidate = subByteStartIndex(start, end);
        if(index_of_first_match_candidate==mySpace.length) return mySpace.length;//0 instances
            //this line is not strictly required because the following for loop would be
            //skipped and the code would return the equal end-line value.

        String space = new String(this.mySpace);
        String target = new String(this.myTarget);
        String target_start_end = target.substring(start,end);
        if(debug)System.out.println("--tse:"+target_start_end);
int count = 0; //suffixes that start with target.substr(s,e)
        for(int i=index_of_first_match_candidate; i<suffixArray.length; i++){
        if(debug)System.out.println("--"+i+":"+space.substring(suffixArray[i])+".sW(tse)? "+space.substring(suffixArray[i]).startsWith(target_start_end));
            if(space.substring(suffixArray[i]).startsWith(target_start_end)) count++;
            else return i;/*smallest i such that SuffixArray[i] DOES NOT START WITH target_s_e
                    but ignoring indices less than index_of_first_match_candidate
                    (note that index_of_first_match_candidate might refer to a string that
                    doesn't start with target_s_e, and it just comes later than target_s_e in
                    ASCII-Lexicographic order)*/
        }
        return suffixArray.length;/*This end-line is reached...
            when method never entered the 'for' loop since
                subByteStartIndex(start,end) < suffixArray.length   == false
                meaning count==0; or
            when all entries in SuffixArray (the dictionary of suffix substrings of SPACE)
                AFTER AND INCLUDING index = subByteStartIndex(start,end)
                UP UNTIL BUT EXCLUDING index = suffixArray.length
                (i.e. i for: first_match_index <= i < SPACE.length)
                all STARTWITH(target_start_end)
                meaning count==(SPACE.length - subByteStartIndex(...)) */
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
            //Test: "ABC"
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("ABC".getBytes());
            System.out.println("SPACE:"+Arrays.toString(frequencerObject.mySpace));
            frequencerObject.printSuffixArray();
            System.out.println("Compare [0]vs[1]: "+frequencerObject.suffixCompare(0,1)+
                               "\t( expects -1 :: substr(0):ABC precedes(-1) substr(1):BC :: A<B )");
            System.out.println("Compare [1]vs[2]: "+frequencerObject.suffixCompare(1,2));
            System.out.println("Compare [2]vs[1]: "+frequencerObject.suffixCompare(2,1));
            System.out.println("Compare [2]vs[2]: "+frequencerObject.suffixCompare(2,2));
            System.out.println();

            //Test: "CBA"
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("CBA".getBytes());
            System.out.println("SPACE:"+Arrays.toString(frequencerObject.mySpace));
            frequencerObject.printSuffixArray();
            System.out.println("Compare [0]vs[1]: "+frequencerObject.suffixCompare(0,1)+
                               "\t( expects  1 :: substr(0):CBA  follows(1)  substr(1):BA :: C>B )");
            System.out.println("Compare [1]vs[2]: "+frequencerObject.suffixCompare(1,2));
            System.out.println("Compare [2]vs[1]: "+frequencerObject.suffixCompare(2,1));
            System.out.println("Compare [2]vs[2]: "+frequencerObject.suffixCompare(2,2));
            System.out.println();

            //Test: "HHH"
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            System.out.println("SPACE:"+Arrays.toString(frequencerObject.mySpace));
            frequencerObject.printSuffixArray();
            System.out.println();

            //Test: "Hi Ho Hi Ho"
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            System.out.println("SPACE:"+Arrays.toString(frequencerObject.mySpace));
            frequencerObject.printSuffixArray();
            System.out.println();
            /* Example for "Hi Ho Hi Ho" (expected result): *note leading whitespaces
                [ 0]= 5: Hi Ho      //first in order (index-0) is:  substring[5,end+1) "_Hi_Ho"
                [ 1]= 8: Ho         //second in order (index-1) is: substring[8,end+1) "_Ho"
                [ 2]= 2: Ho Hi Ho
                [ 3]= 6:Hi Ho
                [ 4]= 0:Hi Ho Hi Ho
                [ 5]= 9:Ho
                [ 6]= 3:Ho Hi Ho
                [ 7]= 7:i Ho
                [ 8]= 1:i Ho Hi Ho
                [ 9]=10:o
                [10]= 4:o Hi Ho
            */

            debug=true;
            //Test substring frequency counter: (space still "Hi Ho Hi Ho")
            frequencerObject.setTarget("H".getBytes());
            System.out.println("TARGET:"+new String(frequencerObject.myTarget));
            int result = frequencerObject.frequency();//How many distinct instances of tgt:"H" in spc:"Hi Ho Hi Ho"?
            System.out.print("Freq = "+result+" ");
            System.out.println( (result==4)? "OK" : "WRONG" );
                //if(4 == result){System.out.println("OK");}else{System.out.println("WRONG");}
            System.out.println();

        /*  SUBTEST 1:  target : "HoHoHo"
                        start = 0, end = 2
                        target_start_end : "Ho"
                SA[3]=6:    Hi_Ho       #ignored...
                SA[4]=0:    Hi_Ho_Hi_Ho #ignored...
                target_s_e: Ho          # <-
                SA[5]=9:    Ho          #STARTSWITH(Ho) (smallest i such that not precedes : 5)
                SA[6]=3:    Ho_Hi_Ho    #STARTSWITH(Ho)
                SA[7]=7:    i_Ho        #fail! (smallest i such that fail : 7)
                SA[8]=1:    i_Ho_Hi_Ho  #fail!
        */
            frequencerObject.setTarget("HoHoHo".getBytes());
            System.out.println("TARGET:"+new String(frequencerObject.myTarget));
            System.out.println("TARGET.substr:"+new String(frequencerObject.myTarget).substring(0,2));
            result = frequencerObject.subByteFrequency(0,2);
            System.out.print("Freq.sub = "+result+" ");
            System.out.println( (result==2)? "OK" : "WRONG" );
            System.out.println();

        /*  SUBTEST 2:  target : "High and Low"
                        start = 1, end = 2
                        target_start_end : "i"
                SA[5]=9:    Ho          #ignored...
                SA[6]=3:    Ho_Hi_Ho    #ignored...
                target_s_e: i           # <-
                SA[7]=7:    i_Ho        #STARTSWITH(i) (smallest i such that not precedes : 7)
                SA[8]=1:    i_Ho_Hi_Ho  #STARTSWITH(i)
                SA[9]=10:   o           #fail! (smallest i such that fail : 9)
                SA[10]=4:   o_Hi_Ho     #fail!
        */
            frequencerObject.setTarget("High and Low".getBytes());
            System.out.println("TARGET:"+new String(frequencerObject.myTarget));
            System.out.println("TARGET.substr:"+new String(frequencerObject.myTarget).substring(1,2));
            result = frequencerObject.subByteFrequency(1,2);
            System.out.print("Freq.sub = "+result+" ");
            System.out.println( (result==2)? "OK" : "WRONG" );
            System.out.println();

            debug=false;
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}
