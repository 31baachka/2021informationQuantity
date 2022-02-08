package s4.B213365;  // ここは、かならず、自分の名前に変えよ。
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
        int str_length = this.mySpace.length;
        // 短い方の配列の長さを計算
        if(i < j) str_length -= j;
        else str_length -= i;

        for(int n = 0; n < str_length; n++)
        {
            if(this.mySpace[i+n] > this.mySpace[j+n]) return 1;
            else if(this.mySpace[i+n] < this.mySpace[j+n]) return -1;
        }

        if(i < j) return 1;
        else if(i > j) return -1;
        else return 0;
        //return 0; // この行は変更しなければいけない。
    }

    private void sort(int[] array, int low, int high){
        //マージソート
        if(low < high){
            int middle = (low + high) >>> 1;
            sort(array, low , middle);
            sort(array, middle+1, high);
            merge(array, low, middle, high);
        }

    }
    private void merge(int[] array, int low, int middle, int high){
        //マージソートのマージ部分
        int[] helper = new int[array.length];

        for (int i = low; i <= high; i++){
            helper[i] = array[i];
        }
        int helperLeft = low;
        int helperRight = middle + 1;
        int current = low;

        while (helperLeft <= middle && helperRight <= high){
            if (suffixCompare(helper[helperLeft],helper[helperRight])<0){
                array[current] = helper[helperLeft];
                helperLeft ++;
            }
            else {
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
    public void setSpace(byte []space) {
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space; if(mySpace.length>0) spaceReady = true;
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.      
        }


        sort(suffixArray,0,suffixArray.length-1);
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
        int first = subByteStartIndex(start, end);
        int last1 = subByteEndIndex(start, end);
        return last1 - first;
    }
    // 変更してはいけないコードはここまで。

    private int targetCompare(int i, int j, int k) {
        // subByteStartIndexとsubByteEndIndexを定義するときに使う比較関数。

        int targetjkLength=k-j;//the length of a part of target string from start to end
        for(int loop=0;loop<targetjkLength;loop++){
            if(mySpace.length-1<suffixArray[i]+loop)return 2;//suffix_i < target_j_k
            if(mySpace[suffixArray[i]+loop] < myTarget[j+loop])return 1;//suffix_i < target_j_k
            else if(mySpace[suffixArray[i]+loop] > myTarget[j+loop])return -1;//suffix_i > target_j_k
        }
        return 0; // j~kの文字列が一致し，さらにtargetjkLengthよりsuffixarray文字列の方が大きい
    }


    private int binarySearch(int start , int end,boolean getfirst){
        //suffixarrayから「start,endで表されるtargetの部分文字列を先頭とするindex」を二分探索法で取り出す．
        //条件に合うindexは複数存在することがあるが，どのindexを返戻するかはここでは考えない
        int left = 0;
        int right = suffixArray.length-1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int compResult=targetCompare(mid,start,end);//temporarily store the comparison result.
            if (compResult==0) {
                //System.out.println(left+","+mid+","+right);
                if(getfirst){//startindexを取得
                    if(mid==0)return mid;
                    if(targetCompare(mid-1,start,end)!=0)return mid;
                    else right=mid-1;
                }else{//endindexを取得
                    if(mid==suffixArray.length-1)return mid+1;
                    if(targetCompare(mid+1,start,end)!=0)return mid+1;
                    else left=mid+1;
                }

                //return mid;
            } else if (compResult<0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;//if no word is matched or an error occured, return -1.

    }


    private int subByteStartIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        int index=binarySearch(start,end,true);
        return index; //このコードは変更しなければならない。
    }

    private int subByteEndIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        int index=binarySearch(start,end,false);


        return index;
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
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("lllll ".getBytes());
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
            //                                         
            // ****  Please write code to check subByteStartIndex, and subByteEndIndex
            //

            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

