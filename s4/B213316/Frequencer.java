package s4.B213316; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {  // This interface provides the design for frequency counter.
    void setTarget(byte[] target);  // set the data to search.
    void setSpace(byte[] space);  // set the data to be searched target from.
    int frequency(); // It return -1, when TARGET is not set or TARGET's length is zero
                     // Otherwise, it return 0, when SPACE is not set or Space's length is zero
                     // Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e. target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}
*/


public class Frequencer implements FrequencerInterface {
    // Code to Test, *warning: This code contains intentional problem*
    static boolean debugMode = false;
    byte[] myTarget;
    byte[] mySpace;

    @Override
    public void setTarget(byte[] target) {
        myTarget = target;
    }
    @Override
    public void setSpace(byte[] space) {
        mySpace = space;
    }

    private void showVariables() {
	try {
	    for(int i=0; i< mySpace.length; i++) { System.out.write(mySpace[i]); }
	}
	catch (NullPointerException npe) {
	    System.out.print("Space is not set");
	}
	System.out.write(' ');
	try {
	    for(int i=0; i< myTarget.length; i++) { System.out.write(myTarget[i]); }
	}
	catch (NullPointerException npe) {
	    System.out.print("Target is not set");
	}
	System.out.write(' ');
    }

    @Override
    public int frequency() {
	int count = 0;
	int targetLength, spaceLength;
	
	if (myTarget == null) {
	    targetLength = 0;
	}
	else {
            targetLength = myTarget.length;
	}

	if (mySpace == null) {
	    spaceLength = 0;
	}
	else {
            spaceLength = mySpace.length;
	}

	if(debugMode) { showVariables(); }
	if (targetLength > 0) {
            for(int start = 0; start<spaceLength; start++) {
                boolean abort = false;
                for(int i = 0; i<targetLength; i++) {
                    if(myTarget[i] != mySpace[start+i]) { abort = true; break; }
                }
                if(abort == false) { count++; }
            }
	}
	else {
	    count = -1;
	}
	if(debugMode) { System.out.printf("%10d\n", count); }
        return count;
    }

    @Override
    public int subByteFrequency(int start, int end) {
	int count = 0;
	int targetLength;

	if (myTarget == null) {
	    targetLength = 0;
	}
	else {
            targetLength = myTarget.length;
	}

	if (debugMode) { showVariables(); }
	if (targetLength > 0) {
	    for (int i = start; i < end; i++) {
		boolean abort = false;
		for (int j = 0; j < targetLength; j++) {
		    if (myTarget[j] != mySpace[i+j]) { abort = true; break; }
		}
		if (abort == false) { count++; }
	    }
	}

	if (targetLength == 0) {
	    count = -1;
	}
	if (debugMode) { System.out.printf("%10d\n", count); }
        return count;
    }

    public static void main(String[] args) {
        Frequencer myObject;
        int freq;
	// White box test, here.
	debugMode = true;
        try {
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            myObject.setTarget("H".getBytes());
            freq = myObject.frequency();
        }
        catch(Exception e) {
            System.out.println("Exception occurred: STOP");
        }
    }
}
