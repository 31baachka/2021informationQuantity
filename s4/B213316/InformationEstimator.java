package s4.B213316; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

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

    private int freqCnt = 0;    // frequency, subByteFrequencyの呼び出し回数をカウントする．性能検証用

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
        return  - Math.log10((double) freq / (double) mySpace.length) / Math.log10((double) 2.0);
    }

    @Override
    public void setTarget(byte[] target) {
        myTarget = target;
    }

    @Override
    public void setSpace(byte[] space) {
        myFrequencer = new Frequencer();
        mySpace = space; myFrequencer.setSpace(space);
    }

    @Override
    public double estimation(){
        // targetがnullもしくは長さ0のときは0.0を返す
        if (myTarget == null || myTarget.length == 0) {
            if (debugMode) { System.out.println("myTarget is not set, or its length is zero!"); }
            return 0.0;
        }
        // spaceがnullもしくは長さ0のときはDouble.MAX_VALUEを返す
        if (mySpace == null || mySpace.length == 0) {
            if (debugMode) { System.out.println("mySpace is not set, or its length is zero!"); }
            return Double.MAX_VALUE;
        }

        freqCnt = 0;

        double value = Double.MAX_VALUE; // value = mininimum of each "value1".
        
        double[] prefixEstimation = new double[myTarget.length+1];  // 接頭辞の情報量

        if(debugMode) { showVariables(); }

        myFrequencer.setTarget(myTarget);

        for (int i = 0; i < myTarget.length; i++) {
            // 文字列Sを接頭辞pとそれ以外tに分けたとき
            // Sの情報量Iq(S)は min(Iq(p)-log2(frequency(t)/len(S)))
            // len(S)はSの長さ
            freqCnt++;
            int freq = myFrequencer.subByteFrequency(i, myTarget.length);
            if (freq == 0) {
                if (debugMode) { System.out.println("True value is infinite!"); }
                return Double.MAX_VALUE; // -log2(0) -> infinite
            }
            if (!prefixIQ(prefixEstimation, i)) {
                if (debugMode) { System.out.println("True value is infinite!"); }
                return Double.MAX_VALUE; // -log2(0) -> infinite
            }
            double value1 = iq(freq);
            value1 = value1 + prefixEstimation[i];
            if (value1 < value) value = value1;
        }


        //boolean[] partition = new boolean[myTarget.length+1];
        //int np = 1<<(myTarget.length-1);
        //for (int p = 0; p < np; p++) {
        //    partition[0] = true;
        //    for (int i = 0; i < myTarget.length-1; i++) {
        //        partition[i+1] = (0 != ((1<<i) & p));
        //    }
        //    partition[myTarget.length] = true;

        //    double value1 = (double)0.0;
        //    int end = 0;
        //    int start = end;
        //    while (start < myTarget.length) {
        //        end++;
        //        while (partition[end] == false) {
        //            end++;
        //        }
        //        myFrequencer.setTarget(subBytes(myTarget, start, end));
        //        value1 = value1 + iq(myFrequencer.frequency());
        //        freqCnt++;
        //        start = end;
        //    }

        //    if (value1 < value) value = value1;
        //}

        if(debugMode) { System.out.printf("%10.5f\n", value); }
        if(debugMode) { System.out.println("Total of frequency call: "+freqCnt); }

        if (debugMode) {
            prefixEstimation[myTarget.length] = value;
            for (int i = 0; i <= myTarget.length; i++) { System.out.print(""+prefixEstimation[i]+" "); }
            System.out.println();
        }

        return value;
    }

    // 接頭辞の情報量を求める
    private boolean prefixIQ(double[] prefixEstimation, int endIdx) {
        if (endIdx == 0) {
            prefixEstimation[endIdx] = 0.0;
            return true;
        }
        else if (endIdx == 1) {
            freqCnt++;
            int freq = myFrequencer.subByteFrequency(0, endIdx);
            if (freq == 0) return false;
            prefixEstimation[endIdx] = iq(freq);
            return true;
        }
        else {
            double value = Double.MAX_VALUE;
            for (int i = 0; i < endIdx; i++) {
                int freq = myFrequencer.subByteFrequency(i, endIdx);
                if (freq == 0) return false; // -log2(0) -> infinite. 情報量測定失敗
                double value1 = iq(freq);
                freqCnt++;
                value1 = value1 + prefixEstimation[i];
                if (value1 < value) value = value1;
            }
            prefixEstimation[endIdx] = value;
            return true;
        }
    }

    public static void main(String[] args) {
        InformationEstimator myObject;
        double value;
        debugMode = true;
        /* myTarget and mySpace are not set. value must be 0.0. */
        myObject = new InformationEstimator();
        value = myObject.estimation();
        System.out.printf("%10.5f\n", value);
        /* myTarget are not set. value must be 0.0. */
        myObject = new InformationEstimator();
        myObject.setSpace("3210321001230123".getBytes());
        value = myObject.estimation();
        System.out.printf("%10.5f\n", value);
        /* length of myTarget is zero. value must be 0.0. */
        myObject = new InformationEstimator();
        myObject.setSpace("3210321001230123".getBytes());
        myObject.setTarget("".getBytes());
        value = myObject.estimation();
        System.out.printf("%10.5f\n", value);
        /* mySpace is not set. value must be Double.MAX_VALUE. */
        myObject = new InformationEstimator();
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        System.out.printf("%10.5f\n", value);
        /* length of mySpace is zero (true value is infinite). value must be Double.MAX_VALUE. */
        myObject = new InformationEstimator();
        myObject.setSpace("".getBytes());
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        System.out.printf("%10.5f\n", value);
        /* mySpace and myTarget are set */
        myObject = new InformationEstimator();
        myObject.setSpace("3210321001230123".getBytes());
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        System.out.printf("%10.5f\n", value);

        myObject.setTarget("01".getBytes());
        value = myObject.estimation();
        System.out.printf("%10.5f\n", value);

        myObject.setTarget("30123".getBytes());
        value = myObject.estimation();
        System.out.printf("%10.5f\n", value);

        myObject.setTarget("00".getBytes());
        value = myObject.estimation();
        System.out.printf("%10.5f\n", value);

        myObject.setTarget("456".getBytes());
        value = myObject.estimation();
        System.out.printf("%10.5f\n", value);
    }
}

