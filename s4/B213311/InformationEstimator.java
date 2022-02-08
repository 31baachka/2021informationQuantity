package s4.B213311; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 

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
    byte[] mySpace; // Sample space to compute the probability
    FrequencerInterface myFrequencer; // Object for counting frequency

    private void showVariables() {
        for (int i = 0; i < mySpace.length; i++) {
            System.out.write(mySpace[i]);
        }
        System.out.write(' ');
        for (int i = 0; i < myTarget.length; i++) {
            System.out.write(myTarget[i]);
        }
        System.out.write(' ');
    }

    byte[] subBytes(byte[] x, int start, int end) {
        // corresponding to substring of String for byte[],
        // It is not implement in class library because internal structure of byte[]
        // requires copy.
        byte[] result = new byte[end - start];
        for (int i = 0; i < end - start; i++) {
            result[i] = x[start + i];
        }
        ;
        return result;
    }

    // IQ: information quantity for a count, -log2(count/sizeof(space))
    double iq(int freq) {
        if (freq == 0)
            return Double.MAX_VALUE;
        return -Math.log10((double) freq / (double) mySpace.length) / Math.log10((double) 2.0);
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
    public double estimation() {
        // Targetがnullまたは0の場合は-1を返す
        if (myTarget == null || myTarget.length == 0) {
            return 0.0;
        }
        // Spaceがnullまたは0の場合は0を返す
        if (mySpace == null || mySpace.length == 0) {
            return Double.MAX_VALUE;
        }

        double value = Double.MAX_VALUE; // value = minimum of each "value1"

        if (debugMode) {
            showVariables();
        }

        // DP
        // 情報量を記憶しておく配列
        double[] suffixEstimation = new double[myTarget.length];

        // 最初の1文字の情報量計算
        myFrequencer.setTarget(subBytes(myTarget, 0, 1));
        suffixEstimation[0] = iq(myFrequencer.frequency());

        for (int i = 1; i < suffixEstimation.length; i++) {
            double value1;
            // 文字列すべて
            myFrequencer.setTarget(subBytes(myTarget, 0, i + 1));
            value1 = iq(myFrequencer.frequency());

            // 区切り文字に対して探索
            for (int j = 1; j < i + 1; j++) {
                double value2;
                myFrequencer.setTarget(subBytes(myTarget, j, i + 1));
                value2 = suffixEstimation[j - 1] + iq(myFrequencer.frequency());
                if (value1 > value2)
                    value1 = value2;
            }
            suffixEstimation[i] = value1;
        }

        if (suffixEstimation[suffixEstimation.length - 1] < value)
            value = suffixEstimation[suffixEstimation.length - 1];

        if (debugMode) {
            System.out.printf("%10.5f\n", value);
        }
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
