package s4.B213362; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

// B191865 の方のコードを参考にしました

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
    byte[] myTarget; // data to compute its information quantity
    byte[] mySpace;  // Sample space to compute the probability
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
    double iq(int start, int end) { // estiation の変更に伴い変更
        // 文字列の呼び出しをこちらで行う
        myFrequencer.setTarget(subBytes(myTarget, start, end));
        int freq = myFrequencer.frequency();
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
        // buggy による修正 
        if(myTarget == null || myTarget.length == 0) return (double) 0.0;
        if(mySpace == null) return Double.MAX_VALUE;
        
        int np = 1<<(myTarget.length-1);
        
	    if(debugMode) { showVariables(); }
        if(debugMode) { System.out.printf("np=%d length=%d ", np, +myTarget.length); }

        // 計算結果を保存する配列
        double[] suffixEstimation = new double[this.myTarget.length];
        // 分割統治法でボトムアップ的に DP を行う
        suffixEstimation[0] = iq(0, 1); // １文字のときの結果
        for(int n=1; n < this.myTarget.length; n++)
        { // 文字数をターゲットの文字まで増やしながら計算
            double min = iq(0, n+1); // 最小の値を格納する変数
            for(int i=0; i<n; i++)
            { // 現在の文字までの計算結果の中から最小の値を格納
                double temp = suffixEstimation[i] + iq(i+1, n+1); // 計算結果
                if(min > temp) min = temp;
            }
            suffixEstimation[n] = min; // 最終的な答えを配列にコピー
        }
    
    // 最終的な値を格納 & 出力
    double value = suffixEstimation[this.myTarget.length-1];
	if(debugMode) { System.out.printf("%10.5f\n", value); }
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
    }
}

