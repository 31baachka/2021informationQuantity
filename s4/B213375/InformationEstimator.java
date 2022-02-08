package s4.B213375;
import java.lang.*;
import java.util.Arrays;
import s4.specification.*;

/* Used content imported from s4.specification:
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]);  // set the data for computing the information quantities
    void setSpace(byte space[]);  // set data for sample space to computer probability
    double estimation();  // It returns 0.0 when the target is not set or Target's length is zero;
        // It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
        // The behavior is undefined, if the true value is finite but larger than Double.MAX_VALUE.
        // Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
        // Otherwise, estimation of information quantity.
}
*/

public class InformationEstimator implements InformationEstimatorInterface{
    static boolean debugMode = false;
    static boolean fullCalculations = false; //prevents skipping of some unnecessary calculations
    // Code to test, *warning: This code is slow, and it lacks the required test
    byte[] myTarget; // data to compute its information quantity
    byte[] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

    private void showVariables(){
        System.out.print("spc:");
        for(int i=0; i<mySpace.length; i++) System.out.write(mySpace[i]);
        System.out.write((mySpace.length>15)? '\n' : ' ');

        System.out.print("tgt:");
        for(int i=0; i<myTarget.length; i++) System.out.write(myTarget[i]);
        System.out.write(' ');
    }

    byte[] subBytes(byte[] x, int start, int end){
        // similar to String.substring(start,end)
        byte[] result=new byte[end-start];
        for(int i=0; i<end-start; i++) result[i]=x[start+i];
        return result;
    }

    // IQ: Information Quantity for a count := -log2(count/|space|)
    double iq(int freq){return -Math.log10((double)freq/mySpace.length)/Math.log10(2.0);}

    @Override
    public void setTarget(byte[] target){myFrequencer.setTarget((myTarget=target));}
    @Override
    public void setSpace(byte[] space){
        myFrequencer = new Frequencer();
        myFrequencer.setSpace((mySpace=space));
    }

    @Override
    public double estimation(){//estimation of IQ
        // 0.0 : when no Target;
        // Double.MAX_VALUE : when space not set (technically infinite)
        // WARN: Undefined behaviour if true value is finite but larger than Double.MAX_VALUE.
        if(myTarget.length==0) return 0;
        if(mySpace.length==0) return Double.MAX_VALUE;

        /*  This implementation breaks down the problem into multiple calculations,
            out of which the minimum value out of the candidates is the true result.
            The candidates are each a {
                summation of {
                    the Information Quantity of every substring from {
                        a partition of the original Space
                    } with the same original Target
                }
            }, considering all the possible permutations to subdivide Space.*/
        double output = Double.MAX_VALUE; // init @ worst case, then reduce if possible
        boolean[] partition = new boolean[myTarget.length+1]; // used to define a permutation
        int np = 1<<(myTarget.length-1); // number of ways to partition myTarget : 2^(n-1)

        if(debugMode)showVariables();
        if(debugMode)System.out.printf("np=%d length=%d\n", np, +myTarget.length);

        //vvvvv HERE NEW vvvvv //Dynamic Programming attempt; I think it works; can be further optimized
        int t_len = this.myTarget.length;
        double[] DP2 = new double[t_len+1]; //suffixEstimation
        //DP2[t_len]=0.0;//by default
        for(int i=t_len -1; i>=0; i--){
            //Find DP2[i] as min of the following candidates:
            double[] candidates = new double[t_len-i];
            for(int j=0; j<candidates.length; j++){
                double selfref = DP2[i+(j+1)];
                double iq_reducedtarget = iq(myFrequencer.subByteFrequency(i,i+(j+1)));
                candidates[j] = selfref + iq_reducedtarget;
                if(debugMode)System.out.printf(
                        "----DP["+i+"] candidate "+j+": "+
                        "DP["+(i+(j+1))+"] + IQ(\""+new String(myTarget).substring(i,i+(j+1))+"\")"+
                        " \t= %.1f + %.1f\n",selfref,iq_reducedtarget);
            }/* POSSIBLE OPTIMIZATION POINT: if the loops for all the j's are done simultaneously,
            then we can use information used to compute
                iq(TARGET.substring(i, i+[[j=0]]+1))
            and use it to reduce the computations required to find next loop's
                iq(TARGET.substring(i, i+[[j=1]]+1))
            and similarly we can use info from case[[j=1]] in finding case[[j=2]], and so on and so forth.
            Namely, in finding the InformationQuantity of TARGET.substring(s,e), we need to
            find the frequency (count of instances) of TARGET.substring(s,e) within SPACE,
            but then, when finding TARGET.substring(s,e+1), we ONLY NEED TO
            find the frequency (count of instances) of TARGET.charAt(e) immediately after TARGET.substring(s,e).
            This follows from the simple idea that, for example, freq("HAM")>=freq("HAMBURGER"),
            because "HAMBURGER" can only appear on places that already start with "HAM".
            And this provides a recursive avenue to reduce the cost of finding a String by
            downgrading the problem to individual characters because
                freq('H') >= f("HA") >= f("HAM") >= f("HAMBURGER") ; and
                instancesOf('H') ⊇ iO("HA") ⊇ iO("HAM") ⊇ iO("HAMBURGER")
            */
            double min = Double.MAX_VALUE;
            for(int index=0; index<candidates.length; index++)if(candidates[index]<min) min=candidates[index];
            DP2[i]=min;
            if(debugMode)System.out.printf("DP["+i+"] := %.1f\n",DP2[i]);
        }
        if(true) return DP2[0];
        //^^^^^ HERE NEW ^^^^^

        // subestimations[start,end] := IQ(myTarget.substring(start,end) in mySpace), computed as needed and cached
        double[][] subestimations = new double[myTarget.length+1][myTarget.length+1];
        for(double[] arr:subestimations) Arrays.fill(arr, -1); // init as -1 to signify "not yet calculated"

        for(int p=0; p<np; p++){
            // boolean array canonical form of the p-th partition permutation
            /* e.g. partition permutation {"ab" "cde" "fg"}
                a b c d e f g   : myTarget[]
                T F T F F T F T : partition[] (TRUE iff mT[i] starts a substring)
            */
            partition[0] = true;
            for(int i=0; i<myTarget.length-1; i++)
                partition[i+1] = (0!=((1<<i)&p)); // binary uniquely maps a unique p-th permutation
            partition[myTarget.length] = true;

            // compute Information Quantity for this partition candidate as "value1"
            /* e.g. {"ab" "cde" "fg"} -> value1 := IQ("ab")+IQ("cde")+IQ("fg") */
            double value1 = 0.0;
            int end = 0, start = 0;
            while(start<myTarget.length){
                if(debugMode)System.out.write(myTarget[end]);
                end++;
                while(partition[end]==false){
                    if(debugMode)System.out.write(myTarget[end]);
                    end++;
                }
//                myFrequencer.setTarget(subBytes(myTarget, start, end));
                if(debugMode)System.out.print("["+start+"~"+end+")");
                if(subestimations[start][end]==-1){
                    subestimations[start][end]=this.iq(myFrequencer.subByteFrequency(start, end));
                    if(debugMode)System.out.print("{="+subestimations[start][end]+"}");
                }
                if(debugMode)System.out.print(' ');
                value1 += subestimations[start][end];//iq(myFrequencer.frequency()); was old default implementation without caches.
                if(!fullCalculations && value1>=output) break; // early stop, already more than current min
                start=end;
            }
            if(debugMode)
                if(start==myTarget.length) System.out.println("\tval_"+p+" := "+ value1);
                else System.out.println("\tval_"+p+" >= minCandidate"); // early stop, already more than current min

            // retain the minimal value
            if(value1<output) output=value1;
        }
        if(debugMode)System.out.printf("min_p = %10.5f\n", output);
        return output;
    }

    public static void main(String[] args){
        InformationEstimator myObject;
        InformationEstimator.debugMode = true;
        InformationEstimator.fullCalculations = false;
        myObject=new InformationEstimator();

        //////////////////////////////////////////////////
        myObject.setSpace("3210321001230123".getBytes());

        myObject.setTarget("0".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", 2.0);

        myObject.setTarget("01".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", 3.0);

        myObject.setTarget("0123".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", 3.0);

        myObject.setTarget("00".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", 4.0);

        //////////////////////////////////////////////////
        String bin32 =
                "0101010101"+
                "1000000001"+
                "0101111101"+
                "11";
        /*
            "100" : 1 time  : but!! IQ = 2.66 != 5 = -log2(1/32)    //I think
            "001" : 1 time  : but!! IQ = 2.50 != 5 = -log2(1/32)    //I think
            "010" : 8 times : 2 = -log2(1/4) = -log2(8/32)
         */
        myObject.setSpace(bin32.getBytes());
        myObject.setTarget("100".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", 3.0);

        myObject.setTarget("001".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", 1-Math.log10(9.0/32)/Math.log10(2));

        myObject.setTarget("101".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", -Math.log10(7.0/32)/Math.log10(2));

        //////////////////////////////////////////////////
        String lorem_256 =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut "+
                "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris "+
                "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in";
        /*
            "or"  : 7 times : -log2(7/256) = 5.19
            "dol" : 3 times : -log2(3/256) = 6.415
            " do" : 4 times : -log2(4/256) = -log2(1/64) = 6.0
        */
        myObject.setSpace(lorem_256.getBytes());
        myObject.setTarget("or".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", -Math.log10(7.0/256)/Math.log10(2));

        myObject.setTarget("dol".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", -Math.log10(3.0/256)/Math.log10(2));

        myObject.setTarget(" do".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", 6.0);

        //////////////////////////////////////////////////
        String lorem_2048 =
                "LOREM IPSUM DOLOR SIT AMET, CONSECTETUR ADIPISCING ELIT. QUAE EST QUAERENDI AC DISSERENDI, QUAE " +
                "LOGIKH DICITUR, ISTE VESTER PLANE, UT MIHI QUIDEM VIDETUR, INERMIS AC NUDUS EST. UTRUM IGITUR TIBI " +
                "LITTERAM VIDEOR AN TOTAS PAGINAS COMMOVERE? QUAE EST QUAERENDI AC DISSERENDI, QUAE LOGIKH DICITUR, " +
                "ISTE VESTER PLANE, UT MIHI QUIDEM VIDETUR, INERMIS AC NUDUS EST. QUOCUMQUE ENIM MODO SUMMUM BONUM " +
                "SIC EXPONITUR, UT ID VACET HONESTATE, NEC OFFICIA NEC VIRTUTES IN EA RATIONE NEC AMICITIAE CONSTARE " +
                "POSSUNT. IN PRIMO ENIM ORTU INEST TENERITAS AC MOLLITIA QUAEDAM, UT NEC RES VIDERE OPTIMAS NEC " +
                "AGERE POSSINT. DUO REGES: CONSTRUCTIO INTERRETE. SEMPER ENIM ITA ADSUMIT ALIQUID, UT EA, QUAE PRIMA " +
                "DEDERIT, NON DESERAT. TRIA GENERA BONORUM; QUI POTEST IGITUR HABITARE IN BEATA VITA SUMMI MALI " +
                "METUS? SED ISTI IPSI, QUI VOLUPTATE ET DOLORE OMNIA METIUNTUR, NONNE CLAMANT SAPIENTI PLUS SEMPER " +
                "ADESSE QUOD VELIT QUAM QUOD NOLIT? TERRAM, MIHI CREDE, EA LANX ET MARIA DEPRIMET. EORUM ENIM EST " +
                "HAEC QUERELA, QUI SIBI CARI SUNT SESEQUE DILIGUNT. QUAE IN CONTROVERSIAM VENIUNT, DE IIS, SI PLACET" +
                ", DISSERAMUS. ATQUE HOC LOCO SIMILITUDINES EAS, QUIBUS ILLI UTI SOLENT, DISSIMILLIMAS PROFEREBAS. " +
                "SEPTEM AUTEM ILLI NON SUO, SED POPULORUM SUFFRAGIO OMNIUM NOMINATI SUNT. NEGABAT IGITUR ULLAM ESSE " +
                "ARTEM, QUAE IPSA A SE PROFICISCERETUR; HIC, QUI UTRUMQUE PROBAT, AMBOBUS DEBUIT UTI, SICUT FACIT RE" +
                ", NEQUE TAMEN DIVIDIT VERBIS. VIDEMUS IN QUODAM VOLUCRIUM GENERE NON NULLA INDICIA PIETATIS, " +
                "COGNITIONEM, MEMORIAM, IN MULTIS ETIAM DESIDERIA VIDEMUS. SIN EA NON NEGLEGEMUS NEQUE TAMEN AD " +
                "FINEM SUMMI BONI REFEREMUS, NON MULTUM AB ERILLI LEVITATE ABERRABIMUS. CUM SCIRET CONFESTIM ESSE " +
                "MORIENDUM EAMQUE MORTEM ARDENTIORE STUDIO PETERET, QUAM EPICURUS VOLUPTATEM PETENDAM PUTAT. FACILE " +
                "PATEREMUR, QUI ETIAM NUNC AGENDI ALIQUID DISCENDIQUE CAUSA PROPE CONTRA NATURAM VIGILLAS SUSCIPERE " +
                "SOLEAMUS. NUNC RELIQUA VIDEAMUS, NISI AUT AD HAEC, CATO, DICERE ALIQUID VIS AUT NOS IAM LONGIORES " +
                "SUMUS. NEMO IGITUR ESSE BEATUS POTEST. NUNC RELIQUA VIDEAMUS, NISI AUT AD HAEC, CATO, ALIQUIDA.";
        /*
            "UM"   : 19 times : -log2(19/2048) = 6.415
            "ERE"  : 16 times : -log2(16/2048) = -log2(1/128) = 7.0
            "EMUS" :  4 times : -log2( 4/2048) = -log2(1/512) = 9.0
        */
        myObject.setSpace(lorem_2048.getBytes());
        myObject.setTarget("UM".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", -Math.log10(19.0/2048)/Math.log10(2));

        myObject.setTarget("ERE".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", 7.0);

        myObject.setTarget("EMUS".getBytes());
        if(!debugMode) myObject.showVariables();
        System.out.printf("%10.5f", myObject.estimation());
        System.out.printf(" (expected %10.5f)\n\n", 9.0);

    }
}
