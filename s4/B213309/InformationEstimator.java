package s4.B213309; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

// import java.util.Arrays;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface {
    void setTarget(byte target[]);  // set the data for computing the information quantities
    void setSpace(byte space[]);  // set data for sample space to computer probability
    double estimation();  // It returns 0.0 when the target is not set or Target's length is zero;
    // It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
    // The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
    // Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
    // Otherwise, estimation of information quantity,
}
*/


public class InformationEstimator implements InformationEstimatorInterface {
    static boolean debugMode = false;
    // Code to test, *warning: This code is slow, and it lacks the required test
    byte[] myTarget = null; // data to compute its information quantity
    byte[] mySpace = null;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

    private void showVariables() {
	for(int i=0; i< mySpace.length; i++) { System.out.write(mySpace[i]); }
	System.out.write(' ');
	for(int i=0; i< myTarget.length; i++) { System.out.write(myTarget[i]); }
	System.out.write(' ');
    }

    byte[] subBytes(byte[] x, int start, int end) {
        // corresponding to substring of String for byte[],
        // It is not implement in class library because internal structure of byte[] requires copy.
        byte[] result = new byte[end - start];
        for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
        return result;
    }

    // IQ: information quantity for a count, -log2(count/sizeof(space))
    double iq(int freq) {
        if(freq == 0)
            return Double.MAX_VALUE;
        else
            return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    @Override
    public void setTarget(byte[] target) {
        myTarget = target;
    }

    @Override
    public void setSpace(byte[] space) {
        myFrequencer = new Frequencer();
        mySpace = space; 
        myFrequencer.setSpace(space);
    }

    @Override
    public double estimation(){
        // 定義的に返すべきケース
        if(myTarget == null)
            return (double) 0.0;
        if(myTarget.length == 0)
            return (double) 0.0;
        if(mySpace == null)
            return Double.MAX_VALUE;
        
        
        double value = Double.MAX_VALUE; // value = mininimum of each "value1".

        int np = 1<<(myTarget.length-1);
	    if(debugMode) { showVariables(); }
        if(debugMode) { System.out.printf("np=%d length=%d ", np, +myTarget.length); }
        
        // DPの実装
        // 例："abcd"
        // esti("a")       = iq("a")                                            -> mySuffixEstimation[0]
        //
        // esti("ab")      = min( iq("ab"),    (esti("a")      + iq("b")));     -> mySuffixEstimation[1]
        //
        // esti("abc")     = min( iq("abc"),   (esti("a")      + iq("bc")),
        //                                     (esti("ab")     + iq("c"))   );  -> mySuffixEstimation[2]
        //
        // esti("abcd")    = min( iq("abcd"),  (esti("a")      + iq("bcd")),
        //                                     (esti("ab")     + iq("cd")),
        //                                     (esti("abc")    + iq("d"))   );  -> mySuffixEstimation[3]
        // ----------------+------------------+----------------+------------
        //                     文字列そのまま  | 既出の計算結果  と　その後ろの文字列

        // 先頭からn文字目の計算結果を格納する
        double [] mySuffixEstimation  = new double[myTarget.length];

        // 先頭1文字だけの情報量計算
        myFrequencer.setTarget(subBytes(myTarget, 0, 1));
        mySuffixEstimation[0] = iq(myFrequencer.frequency());
        
        // 2文字列からn文字列まで順に情報量計算を行う
        for(int len = 1; len < mySuffixEstimation.length; len++){
            double value1;
            // まず、文字列そのまま
            myFrequencer.setTarget(subBytes(myTarget, 0, len + 1));
            value1 = iq(myFrequencer.frequency());

            //　区切りのある文字列パターン
            for(int slash = 1; slash < len + 1; slash++){
                myFrequencer.setTarget(subBytes(myTarget, slash, len + 1)); // 区切りより後ろの文字列
                // 区切りあり文字列の情報量 = 既出の計算結果 + 後続の文字列の情報量
                double value2 = mySuffixEstimation[slash - 1] + iq(myFrequencer.frequency());
                // 情報量がより小さい場合に更新
                if(value2 < value1)
                    value1 = value2;
            }
            // len+1文字列の情報量
            mySuffixEstimation[len] = value1;
        }
        // n文字列の計算結果はn-1番目に格納されている
        double ans = mySuffixEstimation[mySuffixEstimation.length - 1];
        if(ans < value)
                value = ans;

        if(debugMode)
            System.out.printf("%10.5f\n", mySuffixEstimation[mySuffixEstimation.length - 1]);
        
        return value;
    }

    public static void main(String[] args) {
        InformationEstimator myObject;
        double value;
	debugMode = true;
        myObject = new InformationEstimator();
        myObject.setSpace("3210321001230123".getBytes());
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        myObject.setTarget("01".getBytes());
        value = myObject.estimation();
        myObject.setTarget("0123".getBytes());
        value = myObject.estimation();
        myObject.setTarget("00".getBytes());
        value = myObject.estimation();
        // additional case
        myObject.setSpace("1212121221212112121212121221221".getBytes());
        myObject.setTarget("1212121211212222222212111111111121214212111212121221".getBytes());
        value = myObject.estimation();
    }
}

