package s4.B213316; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
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
	    FrequencerInterface  myObject;
	    int freq;
	    System.out.println("checking Frequencer");

	    // This is smoke test
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    assert freq == 4: "Hi Ho Hi Ho, H: " + freq;

	    // Write your testCase here
	    // Test for Frequencer.frequency()
	    // Space and Target are NULL
	    myObject = new Frequencer();
            freq = myObject.frequency();
            assert freq == -1: "Space and Target are NULL: " + freq;
            // Target is NULL
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            freq = myObject.frequency();
            assert freq == -1: "Target is NULL: " + freq;
            // Space is NULL
	    myObject = new Frequencer();
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    assert freq == 0: "Space is NULL: " + freq;
            // Target length is 0
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("".getBytes());
	    freq = myObject.frequency();
	    assert freq == -1: "Hi Ho Hi Ho, : " + freq;
            // Space length is 0
	    myObject = new Frequencer();
	    myObject.setSpace("".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    assert freq == 0: ", H: " + freq;

	    // Test for Frequencer.subByteFrequency()
	    // Smoke test
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.subByteFrequency(4, 9);	// search "H" from "o Hi "
	    assert freq == 1: "o Hi , H: " + freq;
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.subByteFrequency(7, 9);	// search "H" from "i "
	    assert freq == 0: "o Hi , H: " + freq;
	    // Space and Target are NULL
            // Space = NULL, [start, end) = [4, 9)
            // startとendが正しく与えられないときの動作は未定義であるためテストを行わない
	    //myObject = new Frequencer();
            //freq = myObject.subByteFrequency(4, 9);
            //assert freq == -1: "Space and Target are NULL: " + freq;
            // Target is NULL
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            freq = myObject.subByteFrequency(4, 9);
            assert freq == -1: "Target is NULL: " + freq;
            // Space is NULL
            // Space = NULL, [start, end) = [4, 9)
            // startとendが正しく与えられないときの動作は未定義であるためテストを行わない
	    //myObject = new Frequencer();
	    //myObject.setTarget("H".getBytes());
	    //freq = myObject.subByteFrequency(4, 9);
	    //assert freq == 0: "Space is NULL: " + freq;
            // Target length is 0
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("".getBytes());
	    freq = myObject.subByteFrequency(4, 9);
	    assert freq == -1: "Hi Ho Hi Ho, : " + freq;
            // Space length is 0
            // Space = "", [start, end) = [4, 9)
            // startとendが正しく与えられないときの動作は未定義であるためテストを行わない
	    //myObject = new Frequencer();
	    //myObject.setSpace("".getBytes());
	    //myObject.setTarget("H".getBytes());
	    //freq = myObject.subByteFrequency(4, 9);
	    //assert freq == 0: ", H: " + freq;



	}
	catch(Exception e) {
	    System.out.println("Exception occurred in Frequencer Object");
	    success = false;
	}

	try {
	    InformationEstimatorInterface myObject;
	    double value;
	    System.out.println("checking InformationEstimator");
	    myObject = new InformationEstimator();
	    myObject.setSpace("3210321001230123".getBytes());
	    myObject.setTarget("0".getBytes());
	    value = myObject.estimation();
	    assert (value > 1.9999) && (2.0001 >value): "IQ for 0 in 3210321001230123 should be 2.0. But it returns "+value;
	    myObject.setTarget("01".getBytes());
	    value = myObject.estimation();
	    assert (value > 2.9999) && (3.0001 >value): "IQ for 01 in 3210321001230123 should be 3.0. But it returns "+value;
	    myObject.setTarget("0123".getBytes());
	    value = myObject.estimation();
	    assert (value > 2.9999) && (3.0001 >value): "IQ for 0123 in 3210321001230123 should be 3.0. But it returns "+value;
	    myObject.setTarget("00".getBytes());
	    value = myObject.estimation();
	    assert (value > 3.9999) && (4.0001 >value): "IQ for 00 in 3210321001230123 should be 3.0. But it returns "+value;
	}
	catch(Exception e) {
	    System.out.println("Exception occurred in InformationEstimator Object");
	    success = false;
	}
        if(success) { System.out.println("TestCase OK"); } 
    }
}	    
	    
