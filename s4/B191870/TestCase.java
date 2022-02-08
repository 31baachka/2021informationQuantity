package s4.B191870; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
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
            myObject = new Frequencer();
            myObject.setSpace("ababab".getBytes());
            myObject.setTarget("abab".getBytes());
            freq = myObject.frequency();
            assert freq == 2 : "ababab, abab: "+ freq;
            //case 2
            myObject = new Frequencer();
            myObject.setSpace("".getBytes());
            myObject.setTarget("a".getBytes());
            freq = myObject.frequency();
            assert freq == 0 : "SPACE LENGTH = 0, a: "+ freq;
            //case 3
            myObject = new Frequencer();
            myObject.setSpace("abcdabcd".getBytes());
            myObject.setTarget("".getBytes());
            freq = myObject.frequency();
            assert freq == -1 : "abcdabcd,TARGET LENGTH = 0 : "+ freq;
            //case 4      
            myObject = new Frequencer();
            myObject.setTarget("abc".getBytes());
            freq = myObject.frequency();
            assert freq == 0 : "NO SPACE,  abc : "+ freq;
            //case 5 
            myObject = new Frequencer();
            myObject.setSpace("".getBytes());
            freq = myObject.frequency();
            assert freq == -1 : "abcdabcd, NO TARGET : "+ freq;  

        System.out.println("Frequencer ok!");
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
        assert (value > 3.9999) && (4.0001 >value): "IQ for 00 in 3210321001230123 should be 4.0. But it returns "+value;
        //target length is 0
        myObject.setTarget("".getBytes());
        value = myObject.estimation();
        assert (value > -0.0001) && (0.0001 >value): "IQ for '' in 3210321001230123 should be 0.0. But it returns "+value;
        //target is not set
        myObject = new InformationEstimator();
        myObject.setSpace("3210321001230123".getBytes());
        value = myObject.estimation();
        assert (value > -0.0001) && (0.0001 >value): "IQ for not set target in 3210321001230123 should be 0.0. But it returns "+value;
        //target's freq = 0
        myObject.setTarget("abc".getBytes());
        value = myObject.estimation();
        assert (value == Double.MAX_VALUE): "IQ for abc in 3210321001230123 should be Double.MAX_VALUE. But it returns "+value;
        //space length is 0
        myObject = new InformationEstimator();
        myObject.setSpace("".getBytes());
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        assert (value == Double.MAX_VALUE): "IQ for 0 in '' should be Double.MAX_VALUE But it returns "+value;
        //space is not set
        myObject = new InformationEstimator();
        myObject.setTarget("012".getBytes());
        value = myObject.estimation();
        assert (value == Double.MAX_VALUE): "IQ for 012 when SPACE NOT SET should be Double.MAX_VALUE But it returns "+value;


        System.out.println("InfomationEstimator ok!");

	}
	catch(Exception e) {
	    System.out.println("Exception occurred in InformationEstimator Object");
	    success = false;
	}
        if(success) { System.out.println("TestCase OK"); } 
    }
}	    
	    
