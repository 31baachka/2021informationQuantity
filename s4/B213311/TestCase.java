package s4.B213311; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 

import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte[]  target); // set the data to search.
    void setSpace(byte[]  space);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or Space's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}
*/

/*
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class TestCase {
	static boolean success = true;

	public static void main(String[] args) {
		try {
			FrequencerInterface myObject;
			// Frequencer.debugMode = true;
			int freq;
			System.out.println("checking Frequencer");

			// This is smoke test
			myObject = new Frequencer();
			myObject.setSpace("Hi Ho Hi Ho".getBytes());
			myObject.setTarget("H".getBytes());
			freq = myObject.frequency();
			assert freq == 4 : "Hi Ho Hi Ho, H: " + freq;
			// Write your testCase here

			// My testCase 1
			myObject = new Frequencer();
			myObject.setSpace("Hi Ho Hi Ho".getBytes());
			myObject.setTarget("Hi".getBytes());
			freq = myObject.frequency();
			assert freq == 2 : "Hi Ho Hi Ho, Hi: " + freq;

			// My testCase 2
			myObject = new Frequencer();
			myObject.setSpace("Hi Ho Hi Ho Ho Ho Ho".getBytes());
			myObject.setTarget("Ho".getBytes());
			freq = myObject.frequency();
			assert freq == 5 : "Hi Ho Hi Ho, Ho: " + freq;

			// My testCase 3
			myObject = new Frequencer();
			myObject.setSpace("HHHHH".getBytes());
			myObject.setTarget("H".getBytes());
			freq = myObject.frequency();
			assert freq == 5 : "HHHHH, H: " + freq;

			// My testCase 4
			myObject = new Frequencer();
			myObject.setSpace("HHHHH".getBytes());
			myObject.setTarget("HH".getBytes());
			freq = myObject.frequency();
			assert freq == 4 : "HHHHH, HH: " + freq;

			// My testCase 5
			myObject = new Frequencer();
			myObject.setSpace("HHHHH".getBytes());
			myObject.setTarget("HHH".getBytes());
			freq = myObject.frequency();
			assert freq == 3 : "HHHHH, HHH: " + freq;

			// My testCase 6
			myObject = new Frequencer();
			myObject.setSpace("HHHHH".getBytes());
			myObject.setTarget("HHHH".getBytes());
			freq = myObject.frequency();
			assert freq == 2 : "HHHHH, HHHH: " + freq;

			// My testCase 7
			myObject = new Frequencer();
			myObject.setSpace("HHHHH".getBytes());
			myObject.setTarget("HHHHH".getBytes());
			freq = myObject.frequency();
			assert freq == 1 : "HHHHH, HHHHH: " + freq;

			// My testCase 8
			myObject = new Frequencer();
			myObject.setSpace("HHHHH".getBytes());
			myObject.setTarget("HHHHHH".getBytes());
			freq = myObject.frequency();
			assert freq == 0 : "HHHHH, HHHHHH: " + freq;

			// My testCase 9
			myObject = new Frequencer();
			myObject.setSpace("".getBytes());
			myObject.setTarget("".getBytes());
			freq = myObject.frequency();
			assert freq == -1 : "Space and Target null: " + freq;

			// My testCase 10
			myObject = new Frequencer();
			freq = myObject.frequency();
			assert freq == -1 : "not setSpace and not setTarget: " + freq;

			// My testCase 11
			myObject = new Frequencer();
			myObject.setSpace("".getBytes());
			freq = myObject.frequency();
			assert freq == -1 : "setSpace null and not setTarget: " + freq;

			// My testCase 12
			myObject = new Frequencer();
			myObject.setTarget("".getBytes());
			freq = myObject.frequency();
			assert freq == -1 : "not setSpace and setTarget null: " + freq;

			// My testCase 13
			myObject = new Frequencer();
			myObject.setSpace("".getBytes());
			myObject.setTarget("A".getBytes());
			freq = myObject.frequency();
			assert freq == 0 : "null, A: " + freq;

			// My testCase 14
			myObject = new Frequencer();
			myObject.setSpace("AAA".getBytes());
			myObject.setTarget("".getBytes());
			freq = myObject.frequency();
			assert freq == -1 : "AAA, null: " + freq;

			// My testCase 15 subByteFrequency
			myObject = new Frequencer();
			myObject.setSpace("HHHHHHHi".getBytes());
			myObject.setTarget("HHHHi".getBytes());
			freq = myObject.subByteFrequency(0, 5);
			assert freq == 1 : "HHHHHHHi, HHHHi[0:5]=HHHHi: " + freq;

			// My testCase 16 subByteFrequency
			myObject = new Frequencer();
			myObject.setSpace("HHHHHHHi".getBytes());
			myObject.setTarget("HHHHi".getBytes());
			freq = myObject.subByteFrequency(1, 2);
			assert freq == 7 : "HHHHHHHi, HHHHi[1:2]=H: " + freq;

			// My testCase 17 subByteFrequency
			myObject = new Frequencer();
			myObject.setSpace("HHHHHHHi".getBytes());
			myObject.setTarget("HHHHi".getBytes());
			freq = myObject.subByteFrequency(3, 5);
			assert freq == 1 : "HHHHHHHi, HHHHi[3:5]=Hi: " + freq;

			// My testCase 18 subByteFrequency
			myObject = new Frequencer();
			myObject.setSpace("HHHHHHHi".getBytes());
			myObject.setTarget("HHHHi".getBytes());
			freq = myObject.subByteFrequency(3, 5);
			assert freq == 1 : "HHHHHHHi, HHHHi[4:5]=i: " + freq;

			// My testCase 19
			myObject = new Frequencer();
			myObject.setSpace("ccddxxa".getBytes());
			myObject.setTarget("ddxxa".getBytes());
			freq = myObject.frequency();
			assert freq == 1 : "ccddxxa, ddxxa: " + freq;

		} catch (Exception e) {
			System.out.println("Exception occurred in Frequencer Object");
			success = false;
		}

		try {
			InformationEstimatorInterface myObject;
			double value;
			System.out.println("checking InformationEstimator");
			myObject = new InformationEstimator();
			myObject.setSpace("3210321001230123".getBytes());

			// Test Case 1 Target is not set
			myObject = new InformationEstimator();
			myObject.setSpace("3210321001230123".getBytes());
			value = myObject.estimation();
			assert value == 0.0 : "Target is not set. It should returns 0.0. But it returns " + value;

			// Test Case 2 Space is not set
			myObject = new InformationEstimator();
			myObject.setTarget("0".getBytes());
			value = myObject.estimation();
			assert value == Double.MAX_VALUE
					: "Space is not set. It should returns Double.MAX_VALUE. But it returns " + value;

			// Test Case 3 Target's length is zero
			myObject = new InformationEstimator();
			myObject.setSpace("3210321001230123".getBytes());
			myObject.setTarget("".getBytes());
			value = myObject.estimation();
			assert value == 0.0 : "Target's length is zero. It should returns 0.0. But it returns " + value;

			// Test Case 4 Space's length is zero
			myObject = new InformationEstimator();
			myObject.setTarget("0".getBytes());
			myObject.setSpace("".getBytes());
			value = myObject.estimation();
			assert value == Double.MAX_VALUE
					: "Target is not set. It should returns Double.MAX_VALUE. But it returns " + value;

			// Test Case 5

			myObject.setSpace("3210321001230123".getBytes());
			myObject.setTarget("0".getBytes());
			value = myObject.estimation();
			assert (value > 1.9999) && (2.0001 > value)
					: "IQ for 0 in 3210321001230123 should be 2.0. But it returns " + value;
			myObject.setTarget("01".getBytes());
			value = myObject.estimation();
			assert (value > 2.9999) && (3.0001 > value)
					: "IQ for 01 in 3210321001230123 should be 3.0. But it returns " + value;
			myObject.setTarget("0123".getBytes());
			value = myObject.estimation();
			assert (value > 2.9999) && (3.0001 > value)
					: "IQ for 0123 in 3210321001230123 should be 3.0. But it returns " + value;
			myObject.setTarget("00".getBytes());
			value = myObject.estimation();
			assert (value > 3.9999) && (4.0001 > value)
					: "IQ for 00 in 3210321001230123 should be 3.0. But it returns " + value;
		} catch (Exception e) {
			System.out.println("Exception occurred in InformationEstimator Object");
			success = false;
		}
		if (success) {
			System.out.println("TestCase OK");
		}
	}
}
