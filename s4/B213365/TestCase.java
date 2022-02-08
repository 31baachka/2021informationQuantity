package s4.B213365; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
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


    public static boolean tester(FrequencerInterface myObject, String space,String target,int answer){
    	try{
			myObject.setSpace(space.getBytes());
			myObject.setTarget(target.getBytes());
			int freq = myObject.frequency();
			assert freq == answer: space+","+target+": " + freq;
		}	catch(Exception e) {
			return false;
		}
    	return true;

	}

    public static void main(String[] args) {
	try {
	    FrequencerInterface  myObject;
	    int freq;
	    int answer;
	    String target;
	    String space;

	    System.out.println("checking Frequencer");
		System.out.println("foo");

	    // This is smoke test
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    assert freq == 4: "Hi Ho Hi Ho, H: " + freq;
	    // Write your testCase here

		myObject.setTarget("Ho ".getBytes());
		freq = myObject.frequency();
		assert freq == 1: "Hi Ho Hi Ho, Ho : " + freq;


		space="ABC";
		target="ABCABA";
		answer=0;
		myObject.setSpace(space.getBytes());
		myObject.setTarget(target.getBytes());
		freq = myObject.frequency();
		assert freq == answer: space+","+target+": " + freq;

		//basic
		success=success && TestCase.tester(myObject,"ABCABA","ABCABA",1);

		//duplication
		success=success && TestCase.tester(myObject,"ABABAB","ABAB",2);

		//target.length<space.length
		success=success && TestCase.tester(myObject,"ABA","ABCABA",0);


		//space.length==0
		success=success && TestCase.tester(myObject,"","ABCABA",0);

		//target.length==0
		//success=success && TestCase.tester(myObject,"aaaa","",-1);


		//ASCII外の文字列の挙動
		success=success && TestCase.tester(myObject,"見ろ！人が塵の様だ！","人",1);


		//long

		space="  Alice was beginning to get very tired of sitting by her sister\n" +
				"on the bank, and of having nothing to do:  once or twice she had\n" +
				"peeped into the book her sister was reading, but it had no\n" +
				"pictures or conversations in it, `and what is the use of a book,'\n" +
				"thought Alice `without pictures or conversation?'\n" +
				"\n" +
				"  So she was considering in her own mind (as well as she could,\n" +
				"for the hot day made her feel very sleepy and stupid), whether\n" +
				"the pleasure of making a daisy-chain would be worth the trouble\n" +
				"of getting up and picking the daisies, when suddenly a White\n" +
				"Rabbit with pink eyes ran close by her.\n" +
				"\n" +
				"  There was nothing so VERY remarkable in that; nor did Alice\n" +
				"think it so VERY much out of the way to hear the Rabbit say to\n" +
				"itself, `Oh dear!  Oh dear!  I shall be late!'  (when she thought\n" +
				"it over afterwards, it occurred to her that she ought to have\n" +
				"wondered at this, but at the time it all seemed quite natural);\n" +
				"but when the Rabbit actually TOOK A WATCH OUT OF ITS WAISTCOAT-\n" +
				"POCKET, and looked at it, and then hurried on, Alice started to\n" +
				"her feet, for it flashed across her mind that she had never\n" +
				"before seen a rabbit with either a waistcoat-pocket, or a watch to\n" +
				"take out of it, and burning with curiosity, she ran across the\n" +
				"field after it, and fortunately was just in time to see it pop\n" +
				"down a large rabbit-hole under the hedge.\n" +
				"\n" +
				"  In another moment down went Alice after it, never once\n" +
				"considering how in the world she was to get out again.\n" +
				"\n" +
				"  The rabbit-hole went straight on like a tunnel for some way,\n" +
				"and then dipped suddenly down, so suddenly that Alice had not a\n" +
				"moment to think about stopping herself before she found herself\n" +
				"falling down a very deep well.\n" +
				"\n" +
				"  Either the well was very deep, or she fell very slowly, for she\n" +
				"had plenty of time as she went down to look about her and to\n" +
				"wonder what was going to happen next.  First, she tried to look\n" +
				"down and make out what she was coming to, but it was too dark to\n" +
				"see anything; then she looked at the sides of the well, and\n" +
				"noticed that they were filled with cupboards and book-shelves;\n" +
				"here and there she saw maps and pictures hung upon pegs.  She\n" +
				"took down a jar from one of the shelves as she passed; it was\n" +
				"labelled `ORANGE MARMALADE', but to her great disappointment it\n" +
				"was empty:  she did not like to drop the jar for fear of killing\n" +
				"somebody, so managed to put it into one of the cupboards as she\n" +
				"fell past it.";
		success=success && TestCase.tester(myObject,space,"ing",18);
		success=success && TestCase.tester(myObject,space," ",433);
		success=success && TestCase.tester(myObject,space,"so",6);
		success=success && TestCase.tester(myObject,space,"ll ",6);
		//success=success && TestCase.tester(myObject," lllll","ll",4);
		//success=success && TestCase.tester(myObject,"lllll ","ll",4);
		success=success && TestCase.tester(myObject,"lllll","ll",4);




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
	    
