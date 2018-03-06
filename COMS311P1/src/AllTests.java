import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.Timeout;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.lang.StringBuffer;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;



/*
NOTES

/*


/*
Tests are at the bottom.  They look like this:

	@Test(timeout=60000) public void distinctSizeNoDups()
	{
		p = 9;

		assertTrue("distinctSize() returns wrong value for tree with no duplicates", bst0.distinctSize() == bstRef0.distinctSize());
	}

	The test fails iff the last argument to the assert is false.

	JUnit is not meant for tracking point totals, so we are forced to use crude methods.  We use p to track the point value of the current
	question; in the TestWatcher instance, we reset p to 9000 after each question so we can detect if we forget to set p in the next question.

	We track both lost points and earned points so when we output we can check whether the totals are correct.

*/


public class AllTests {

    // general functionality
    static int totalPoints = 300; //disincluding efficiency, which will be added later

    static int p = 9001;
    static int partial = 0;
    static int totalEarned = 0;
    static int totalLost = 0;
    static StringBuilder builder = new StringBuilder();

    // test-specific data
    static HashTable table17;
    static HashTable table37;
    static HashTable tableDups;
    static HashTable tableRemoved;

    // static ref.HashTable table17WU;
    // static ref.HashTable table37WU;
    // static ref.HashTable tableDupsWU;
    // static ref.HashTable tableRemovedWU;

    static int bfsEfficiencyIterations = 5;
    static int bfsStringLength = 10000;
    static int bfsShingleLength = 4;

    static int hcsEfficiencyIterations = 10;
    static int hcsStringLength = 20000;
    static int hcsShingleLength = 10000;

    static int hssEfficiencyIterations = 10;
    static int hssStringLength = 20000;
    static int hssShingleLength = 10000;

    static final int HashTableEfficiencyIterations = 65000;

    static String bfsString0;
    static String bfsString1;
    static String hssString0;
    static String hssString1;
    static String hcsString0;
    static String hcsString1;

    // correctness data for efficiency calculations
    // static int htAddCorrectness = 0;
    static int htSearchIntCorrectness = 0;
    static int htSearchTupleCorrectness = 0;
    static int htRemoveCorrectness = 0;

    static int bfsCorrectness = 0;
    static int hssCorrectness = 0;
    static int hcsCorrectness = 0;

    static int htSearchIntCorrectnessTot = 14;
    static int htSearchTupleCorrectnessTot = 24;
    static int htRemoveCorrectnessTot = 10;

    static int bfsCorrectnessTot = 70;
    static int hssCorrectnessTot = 70;
    static int hcsCorrectnessTot = 70;

    // timing data
    static long hashTableAddDelta = Long.MAX_VALUE;
    static long hashTableSearchIntDelta = Long.MAX_VALUE;
    static long hashTableSearchTupleDelta = Long.MAX_VALUE;
    static long hashTableRemoveDelta = Long.MAX_VALUE;

    static long hashTableAddDeltaRef = Long.MAX_VALUE;
    static long hashTableSearchIntDeltaRef = Long.MAX_VALUE;
    static long hashTableSearchTupleDeltaRef = Long.MAX_VALUE;
    static long hashTableRemoveDeltaRef = Long.MAX_VALUE;

    // static long bfsConstructorDelta = Long.MAX_VALUE;
    // static long bfsVectorLengthDelta = Long.MAX_VALUE;
    // static long bfsSimilarityDelta = Long.MAX_VALUE;

    // static long hssConstructorDelta = Long.MAX_VALUE;
    // static long hssVectorLengthDelta = Long.MAX_VALUE;
    // static long hssSimilarityDelta = Long.MAX_VALUE;

    // static long hcsConstructorDelta = Long.MAX_VALUE;
    // static long hcsVectorLengthDelta = Long.MAX_VALUE;
    // static long hcsSimilarityDelta = Long.MAX_VALUE;

    // static long bfsConstructorDeltaRef = Long.MAX_VALUE;
    // static long bfsVectorLengthDeltaRef = Long.MAX_VALUE;
    // static long bfsSimilarityDeltaRef = Long.MAX_VALUE;

    // static long hssConstructorDeltaRef = Long.MAX_VALUE;
    // static long hssVectorLengthDeltaRef = Long.MAX_VALUE;
    // static long hssSimilarityDeltaRef = Long.MAX_VALUE;

    // static long hcsConstructorDeltaRef = Long.MAX_VALUE;
    // static long hcsVectorLengthDeltaRef = Long.MAX_VALUE;
    // static long hcsSimilarityDeltaRef = Long.MAX_VALUE;

    static long bfsDelta = Long.MAX_VALUE;
    static long bfsDeltaRef = Long.MAX_VALUE;
    static long hssDelta = Long.MAX_VALUE;
    static long hssDeltaRef = Long.MAX_VALUE;
    static long hcsDelta = Long.MAX_VALUE;
    static long hcsDeltaRef = Long.MAX_VALUE;

    // @Rule
    // public Timeout timeout = Timeout.seconds(60);

    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {

            if (p > totalPoints) {
                System.out.println("ERROR: POINT VALUES NOT RESET!"); // this ensures that we don't miss resetting point values
            }

            totalEarned += p;
            p = 9000;
        }

        @Override
        protected void failed(Throwable e, Description description) {

            if (p > totalPoints) {
                System.out.println("ERROR: POINT VALUES NOT RESET!"); // this ensures that we don't miss resetting point values
            }

            if (partial > 0) {
                builder.append("test " + description + " partially failed with exception " + e + " (-" + (p - partial) + ");\n");

                totalLost += p - partial;
                totalEarned += partial;
                partial = 0;
            } else {
                builder.append("test " + description + " failed with exception " + e + " (-" + p + "); \n");
                totalLost += p;
            }

            p = 9000;
        }
    };

    @AfterClass
    public static void printResults() {
        if (totalEarned + totalLost != totalPoints) {
            System.out.println("\n\nERROR!  Earned " + totalEarned + " but lost " + totalLost + "; should add to " + totalPoints);
            System.out.println("missing " + (totalPoints - totalEarned - totalLost));
        }

        // System.out.println("hashTableAddDelta: " + (float) hashTableAddDelta / (float) 1000000000);
        // System.out.println("hashTableSearchIntDelta: " + (float) hashTableSearchIntDelta / (float) 1000000000);
        // System.out.println("hashTableSearchTupleDelta: " + (float) hashTableSearchTupleDelta / (float) 1000000000);
        // System.out.println("hashTableRemoveDelta: " + (float) hashTableRemoveDelta / (float) 1000000000 + "\n");

        // System.out.println("bfsConstructorDelta: " + (float) bfsConstructorDelta / (float) 1000000000);
        // System.out.println("bfsVectorLengthDelta: " + (float) bfsVectorLengthDelta / (float) 1000000000);
        // System.out.println("bfsSimilarityDelta: " + (float) bfsSimilarityDelta / (float) 1000000000 + "\n");

        // System.out.println("hssConstructorDelta: " + (float) hssConstructorDelta / (float) 1000000000);
        // System.out.println("hssVectorLengthDelta: " + (float) hssVectorLengthDelta / (float) 1000000000);
        // System.out.println("hssSimilarityDelta: " + (float) hssSimilarityDelta / (float) 1000000000 + "\n");

        // System.out.println("hcsConstructorDelta: " + (float) hcsConstructorDelta / (float) 1000000000);
        // System.out.println("hcsVectorLengthDelta: " + (float) hcsVectorLengthDelta / (float) 1000000000);
        // System.out.println("hcsSimilarityDelta: " + (float) hcsSimilarityDelta / (float) 1000000000 + "\n");

        // System.out.println("htSearchTupleCorrectness fraction: " + (float) htSearchTupleCorrectness / (float) htSearchTupleCorrectnessTot);
        // System.out.println("htSearchIntCorrectness fraction: " + (float) htSearchIntCorrectness / (float) htSearchIntCorrectnessTot);
        // System.out.println("htRemoveCorrectness fraction: " + (float) htRemoveCorrectness / (float) htRemoveCorrectnessTot);
        // System.out.println("bfsCorrectness fraction: " + (float) bfsCorrectness / (float) bfsCorrectnessTot);
        // System.out.println("hssCorrectness fraction: " + (float) hssCorrectness / (float) hssCorrectnessTot);
        // System.out.println("hcsCorrectness fraction: " + (float) hcsCorrectness / (float) hcsCorrectnessTot);

        // float bfsDelta = bfsConstructorDelta + bfsVectorLengthDelta + bfsSimilarityDelta;
        // float hssDelta = hssConstructorDelta + hssVectorLengthDelta + hssSimilarityDelta;
        // float hcsDelta = hcsConstructorDelta + hcsVectorLengthDelta + hcsSimilarityDelta;

        // float bfsDeltaRef = bfsConstructorDeltaRef + bfsVectorLengthDeltaRef + bfsSimilarityDeltaRef;
        // float hssDeltaRef = hssConstructorDeltaRef + hssVectorLengthDeltaRef + hssSimilarityDeltaRef;
        // float hcsDeltaRef = hcsConstructorDeltaRef + hcsVectorLengthDeltaRef + hcsSimilarityDeltaRef;

        float htAddRatio = (float) (htSearchTupleCorrectness + htSearchIntCorrectness) / (float) (htSearchTupleCorrectnessTot + htSearchIntCorrectnessTot);
        float htSearchTupleRatio = (float) htSearchTupleCorrectness / (float) htSearchTupleCorrectnessTot;
        float htSearchIntRatio = (float) htSearchIntCorrectness / (float) htSearchIntCorrectnessTot;
        float htRemoveRatio = (float) htRemoveCorrectness / (float) htRemoveCorrectnessTot;
        float bfsRatio = (float) bfsCorrectness / (float) bfsCorrectnessTot;
        float hssRatio = (float) hssCorrectness / (float) hssCorrectnessTot;
        float hcsRatio = (float) hcsCorrectness / (float) hcsCorrectnessTot;

        float htAddEff = 10f * htAddRatio * Math.min(1f, 1.5f * (float) hashTableAddDeltaRef / (float) hashTableAddDelta);
        float htSearchTupleEff = 4f * htSearchTupleRatio * Math.min(1f, 1.5f * (float) hashTableSearchTupleDeltaRef / (float) hashTableSearchTupleDelta);
        float htSearchIntEff = 4f * htSearchIntRatio * Math.min(1f, 1.5f * (float) hashTableSearchIntDeltaRef / (float) hashTableSearchIntDelta);
        float htRemoveEff = 7f * htRemoveRatio * Math.min(1f, 1.5f * (float) hashTableRemoveDeltaRef / (float) hashTableRemoveDelta);
        float bfsEff = 15f * bfsRatio * Math.min(1f, 1.5f * (float) bfsDeltaRef / (float) bfsDelta);
        float hssEff = 30f * hssRatio * Math.min(1f, 1.5f * (float) hssDeltaRef / (float) hssDelta);
        float hcsEff = 30f * hcsRatio * Math.min(1f, 1.5f * (float) hcsDeltaRef / (float) hcsDelta);

        float pointsEff = htAddEff + htSearchTupleEff + htSearchIntEff + htRemoveEff + bfsEff + hssEff + hcsEff;
        float pointsCorr = totalEarned;

        int total = (int) Math.ceil(250f / 300f * pointsCorr + 150f / 100f * pointsEff);


        System.out.println("================================================================");
        System.out.println("points before report: " + Math.min(total, 400) + "; comments below (v2)");
        System.out.println("================================================================");
        System.out.println("[total points: " + Math.min(total, 400) + "/400]");
        System.out.println("");
        System.out.println("[We rescaled correctness/efficiency; your total score is calculated as");
        System.out.println("ceiling(250/300 * correctness + 150/100 * efficiency)]");
        System.out.println("");
        System.out.println("[correctness: " + pointsCorr + "/300; correctness-scaled efficiency: " + pointsEff + "/100]");
        System.out.println("[correctness deductions:");
        System.out.println(builder.toString());
        System.out.println("]");
        System.out.println("");
        System.out.println("[We calculated several efficiency categories; for each, we scaled by correctness and compared student runtime to a reference implementation:");
        System.out.println("efficiencyPoints = pointsPossible * correctnessPoints / correctnessPointsPossible * min(1, 1.5 * referenceTime / studentTime).");
        System.out.println("(Units are nanoseconds; HashTable add was scaled by search correctness)]");
        System.out.println("[HashTable add (" + htAddEff + "/10): " + hashTableAddDelta + " student, " + hashTableAddDeltaRef + " ref]");
        System.out.println("[HashTable search(int k) (" + htSearchIntEff + "/4): " + hashTableSearchIntDelta + " student, " + hashTableSearchIntDeltaRef + " ref]");
        System.out.println("[HashTable search(Tuple t) (" + htSearchTupleEff + "/4): " + hashTableSearchTupleDelta + " student, " + hashTableSearchTupleDeltaRef + " ref]");
        System.out.println("[HashTable remove (" + htRemoveEff + "/7): " + hashTableRemoveDelta + " student, " + hashTableRemoveDeltaRef + " ref]");
        System.out.println("[BruteForceSimilarity (" + bfsEff + "/15): " + bfsDelta + " student, " + bfsDeltaRef + " ref]");
        System.out.println("[HashStringSimilarity (" + hssEff + "/30): " + hssDelta + " student, " + hssDeltaRef + " ref]");
        System.out.println("[HashCodeSimilarity (" + hcsEff + "/30): " + hcsDelta + " student, " + hcsDeltaRef + " ref]");
        System.out.println("================================================================");

    }

    // ================================================================
    // SETUP
    // ================================================================
    @BeforeClass
    public static void setUp() throws Exception {
        boolean success = true;

        try {
            // we can't choose JUnit execution order, so if we want to test add and
            //   removal, we have to set up a separate case for each stage of add and remove
            table17 = new HashTable(17);
            table37 = new HashTable(17);
            tableDups = new HashTable(17);
            tableRemoved = new HashTable(17);
        } catch (Exception e) {
            System.out.println("SETUP ERROR: couldn't call HashTable constructors");
            success = false;
        }

        try {
            // setup initial hashtables
            ArrayList<Tuple> tuplesToAdd = new ArrayList<Tuple>();

            for (int i = 0; i < 9; i++) {
                tuplesToAdd.add(new Tuple(11 * i * i + 5 * i, "string" + i));

            }

            for (Tuple t : tuplesToAdd) {
                table17.add(t);
                table37.add(t);
                tableDups.add(t);
                tableRemoved.add(t);
//        	 		table17.add(new Tuple(t.getKey(), t.getValue()));
//                table37.add(new Tuple(t.getKey(), t.getValue()));
//                tableDups.add(new Tuple(t.getKey(), t.getValue()));;
//                tableRemoved.add(new Tuple(t.getKey(), t.getValue()));	
            }

            // force hashtables to double
            ArrayList<Tuple> tuplesToAdd1 = new ArrayList<Tuple>();

            for (int i = 9; i < 16; i++) {
                tuplesToAdd1.add(new Tuple(11 * i * i + 5 * i, "string" + i));
            }

            for (Tuple t : tuplesToAdd1) {
                table37.add(t);
                tableDups.add(t);
                tableRemoved.add(t);
            }
        } catch (Exception e) {
            System.out.println("SETUP ERROR: couldn't add to HashTables");
            success = false;
        }

        try {
            // add duplicate elements
            tableDups.add(new Tuple(0, "string0"));
            tableDups.add(new Tuple(16, "string1"));
            tableDups.add(new Tuple(54, "string2"));

            tableRemoved.add(new Tuple(0, "string0"));
            tableRemoved.add(new Tuple(16, "string1"));
            tableRemoved.add(new Tuple(54, "string2"));
        } catch (Exception e) {
            System.out.println("SETUP ERROR: couldn't add duplicates to HashTables");
            success = false;
        }

        try {
            // remove elements
            tableRemoved.remove(new Tuple(0, "string0"));
            tableRemoved.remove(new Tuple(0, "string0"));
            tableRemoved.remove(new Tuple(114, "string3"));
            tableRemoved.remove(new Tuple(196, "string4"));
        } catch (Exception e) {
            System.out.println("SETUP ERROR: couldn't remove from HashTables");
            success = false;
        }

        // setup efficiency test strings
        try {
            Random r = new Random(0l); // always initialized to the same seed for consistency

            // BFS
            StringBuilder sbBFS0 = new StringBuilder("");
            StringBuilder sbBFS1 = new StringBuilder("");
            for (int i = 0; i < bfsStringLength; i++) {
                sbBFS0.append((char) (97 + r.nextInt(26)));
                sbBFS1.append((char) (97 + r.nextInt(26)));
            }

            bfsString0 = sbBFS0.toString();
            bfsString1 = sbBFS1.toString();

            // HSS
            StringBuilder sbHSS0 = new StringBuilder("");
            StringBuilder sbHSS1 = new StringBuilder("");
            for (int i = 0; i < hssStringLength; i++) {
                sbHSS0.append((char) (97 + r.nextInt(26)));
                sbHSS1.append((char) (97 + r.nextInt(26)));
            }

            hssString0 = sbHSS0.toString();
            hssString1 = sbHSS1.toString();

            // HCS
            StringBuilder sbHCS0 = new StringBuilder("");
            StringBuilder sbHCS1 = new StringBuilder("");
            for (int i = 0; i < hcsStringLength; i++) {
                sbHCS0.append((char) (97 + r.nextInt(26)));
                sbHCS1.append((char) (97 + r.nextInt(26)));
            }

            hcsString0 = sbHCS0.toString();
            hcsString1 = sbHCS1.toString();


        } catch (Exception e) {
            System.out.println("SETUP ERROR: couldn't setup strings for similarity efficiency tests");
            success = false;
        }

    }

 

    // tuple equality
    boolean eq(Tuple a, Tuple b) {
        return a.getKey() == b.getKey()
                && a.getValue().compareTo(b.getValue()) == 0;
    }

    boolean ArrayListEquality(ArrayList<Tuple> a, ArrayList<Tuple> b) {
        if (a.size() != b.size()) return false;

        // of all the ways to do this, this is probably the dumbest
        for (Tuple t : a) {
            // count occurrences in a
            int counta = 0;
            int countb = 0;
            for (int i = 0; i < a.size(); i++) {
                if (eq(t, a.get(i))) counta++;
                if (eq(t, b.get(i))) countb++;
            }
            if (counta != countb) return false;
        }

        for (Tuple t : b) {
            // count occurrences in a
            int counta = 0;
            int countb = 0;
            for (int i = 0; i < a.size(); i++) {
                if (eq(t, a.get(i))) counta++;
                if (eq(t, b.get(i))) countb++;
            }
            if (counta != countb) return false;
        }
        return true;
    }

    // ================================================================
    // TEST HASHTABLE
    // ================================================================

    // before forcing the double
    @Test(timeout = 60000)
    public void maxLoad() throws Exception {
        p = 7;
        assertTrue("hashtable has incorrect max load", table17.maxLoad() == 2);
    }

    @Test(timeout = 60000)
    public void avgLoad() throws Exception {
        p = 7;
        float al = table17.averageLoad();
        assertTrue("hashtable has incorrect average load",
                table17.averageLoad() > 1.12f && table17.averageLoad() < 1.13f);
    }

    @Test(timeout = 60000)
    public void loadFactor() throws Exception {
        p = 7;
        float lf = table17.loadFactor();
        assertTrue("hashtable has incorrect load factor",
                table17.loadFactor() > 0.52f && table17.loadFactor() < 0.53f);
    }

    @Test(timeout = 60000)
    public void searchTuplePresent() throws Exception {
        p = 4;
        assertTrue("tuple frequency incorrect",
                table17.search(new Tuple(0, "string0")) == 1);
        htSearchTupleCorrectness += p;
    }

    @Test(timeout = 60000)
    public void searchTupleAbsent() throws Exception {
        p = 3;
        assertTrue("tuple frequency incorrect",
                table17.search(new Tuple(0, "not added")) == 0);
        htSearchTupleCorrectness += p;
    }

    @Test(timeout = 60000)
    public void searchIntPresent() throws Exception {
        p = 4;
        ArrayList<Tuple> correct = new ArrayList<Tuple>();
        correct.add(new Tuple(0, "string0"));
        assertTrue("search returns wrong list",
                ArrayListEquality(table17.search(0), correct));
        htSearchIntCorrectness += p;
    }

    @Test(timeout = 60000)
    public void searchIntAbsent() throws Exception {
        p = 3;
        ArrayList<Tuple> correct = new ArrayList<Tuple>();
        assertTrue("search returns wrong list",
                ArrayListEquality(table17.search(-1), correct));
        htSearchIntCorrectness += p;
    }


    // after forcing the double
    @Test(timeout = 60000)
    public void maxLoadDoubled() throws Exception {
        p = 7;
        assertTrue("hashtable has incorrect max load", table37.maxLoad() == 2);
    }

    @Test(timeout = 60000)
    public void avgLoadDoubled() throws Exception {
        p = 7;
        assertTrue("hashtable has incorrect average load",
                table37.averageLoad() > 1.77f && table37.averageLoad() < 1.78f);
    }

    @Test(timeout = 60000)
    public void loadFactorDoubled() throws Exception {
        p = 7;
        float x = table37.loadFactor();
        assertTrue("hashtable has incorrect load factor",
                table37.loadFactor() > 0.43f && table37.loadFactor() < 0.44f);
    }

    @Test(timeout = 60000)
    public void searchTuplePresentDoubled() throws Exception {
        p = 4;
        assertTrue("tuple frequency incorrect",
                table37.search(new Tuple(2550, "string15")) == 1);
        htSearchTupleCorrectness += p;
    }

    @Test(timeout = 60000)
    public void searchTupleAbsentDoubled() throws Exception {
        p = 3;
        assertTrue("tuple frequency incorrect",
                table37.search(new Tuple(0, "not added")) == 0);
        htSearchTupleCorrectness += p;
    }

    @Test(timeout = 60000)
    public void searchIntPresentDoubled() throws Exception {
        p = 4;
        ArrayList<Tuple> correct = new ArrayList<Tuple>();
        correct.add(new Tuple(2550, "string15"));
        assertTrue("search returns wrong list",
                ArrayListEquality(table37.search(2550), correct));
        htSearchIntCorrectness += p;
    }

    @Test(timeout = 60000)
    public void searchIntAbsentDoubled() throws Exception {
        p = 3;
        ArrayList<Tuple> correct = new ArrayList<Tuple>();
        assertTrue("search returns wrong list",
                ArrayListEquality(table37.search(-1), correct));
        htSearchIntCorrectness += p;
    }

    // after adding duplicates
    @Test(timeout = 60000)
    public void searchTupleDups0() throws Exception {
        p = 5;
        assertTrue("tuple frequency incorrect",
                tableDups.search(new Tuple(0, "string0")) == 2);
        htSearchTupleCorrectness += p;
    }

    @Test(timeout = 60000)
    public void searchTupleDups1() throws Exception {
        p = 5;
        assertTrue("tuple frequency incorrect",
                tableDups.search(new Tuple(2550, "string15")) == 1);
        htSearchTupleCorrectness += p;
    }

    // after removing a duplicate
    @Test(timeout = 60000)
    public void searchTupleRemoved0() throws Exception {
        p = 5;
        assertTrue("tuple frequency incorrect",
                tableRemoved.search(new Tuple(0, "string0")) == 0);
        htRemoveCorrectness += p;
    }

    @Test(timeout = 60000)
    public void searchTupleRemoved1() throws Exception {
        p = 5;
        assertTrue("tuple frequency incorrect",
                tableRemoved.search(new Tuple(196, "string4")) == 0);
        htRemoveCorrectness += p;
    }


    // ================================================================
    // TEST BRUTE FORCE SIMILARITY
    // ================================================================

    // vector length
    public void testVectorLengthBFS(String s, int k, float target) throws Exception {
        p = 7;

        BruteForceSimilarity bfs = new BruteForceSimilarity(s, s, k);
        float vl = bfs.lengthOfS1();
        assertTrue("incorrect brute force vector length",
                target - 0.01f < vl && vl < target + 0.01f);
        bfsCorrectness += p;
    }

    @Test(timeout = 60000)
    public void vlengthS1BFS0() throws Exception {
        testVectorLengthBFS("abcdefghijklmnopqrstuvwxyz", 2, 5.0f);
    }

    @Test(timeout = 60000)
    public void vlengthS1BFS1() throws Exception {
        testVectorLengthBFS("abcdefghijklmnopqrstuvwxyz", 8, 4.35889894354f);
    }

    @Test(timeout = 60000)
    public void vlengthS1BFS2() throws Exception {
        testVectorLengthBFS("inaholeinthegroundtherelivedahobbit", 2, 6.63324958071f);
    }

    @Test(timeout = 60000)
    public void vlengthS1BFS3() throws Exception {
        testVectorLengthBFS("marswasemptybeforewecamethatsnottosaythatnothinghadeverhappened", 4, 7.87400787401f);
    }

    @Test(timeout = 60000)
    public void vlengthS1BFS4() throws Exception {
        testVectorLengthBFS("butallofthathappenedinmineralunconsciousnessandunobserved", 3, 7.54983443527f);
    }


    // similarity
    public void testSimilarityBFS(String s, String t, int k, float target) throws Exception {
        p = 7;

        BruteForceSimilarity bfs = new BruteForceSimilarity(s, t, k);
        float sim = bfs.similarity();
        assertTrue("incorrect brute force similarity",
                target - 0.01f < sim && sim < target + 0.01f);
        bfsCorrectness += p;
    }

    @Test(timeout = 60000)
    public void similarityBFS0() throws Exception {
        testSimilarityBFS(
                "abcdefghijklmnopqrstuvwxyz",
                "abcdefghijklmnopqrstuvwxyz",
                2, 1.0f
        );
    }

    @Test(timeout = 60000)
    public void similarityBFS1() throws Exception {
        testSimilarityBFS(
                "abcdefghijklmnopqrstuvwxyz",
                "qrstuvwxyzabcdefghijklmnop",
                8, 0.631578947368f
        );
    }

    @Test(timeout = 60000)
    public void similarityBFS2() throws Exception {
        testSimilarityBFS(
                "inaholeinthegroundtherelivedahobbit",
                "ithadaperfectlyrounddoorlikeaportholepaintedgreen",
                2, 0.451335466924f
        );
    }

    @Test(timeout = 60000)
    public void similarityBFS3() throws Exception {
        testSimilarityBFS(
                "marswasemptybeforewecamethatsnottosaythatnothinghadeverhappened",
                "theplanethadaccretedmeltedroiledandcooledleavingasurfacescarredbyenormousgeolocicalfeaturescraterscanyonsvolcanoes",
                4, 0.0120543186004f
        );
    }

    @Test(timeout = 60000)
    public void similarityBFS4() throws Exception {
        testSimilarityBFS(
                "butallofthathappenedinmineralunconsciousnessandunobserved",
                "therewerenowitnesses",
                3, 0.0592348877759f
        );
    }

    // ================================================================
    // TEST HASH STRING SIMILARITY
    // ================================================================

    // vector length
    public void testVectorLengthHSS(String s, int k, float target) throws Exception {
        p = 7;

        HashStringSimilarity hss = new HashStringSimilarity(s, s, k);
        float vl = hss.lengthOfS1();
        assertTrue("incorrect hash string vector length",
                target - 0.01f < vl && vl < target + 0.01f);
        hssCorrectness += p;
    }

    @Test(timeout = 60000)
    public void vlengthS1HSS0() throws Exception {
        testVectorLengthHSS("abcdefghijklmnopqrstuvwxyz", 2, 5.0f);
    }

    @Test(timeout = 60000)
    public void vlengthS1HSS1() throws Exception {
        testVectorLengthHSS("abcdefghijklmnopqrstuvwxyz", 8, 4.35889894354f);
    }

    @Test(timeout = 60000)
    public void vlengthS1HSS2() throws Exception {
        testVectorLengthHSS("inaholeinthegroundtherelivedahobbit", 2, 6.63324958071f);
    }

    @Test(timeout = 60000)
    public void vlengthS1HSS3() throws Exception {
        testVectorLengthHSS("marswasemptybeforewecamethatsnottosaythatnothinghadeverhappened", 4, 7.87400787401f);
    }

    @Test(timeout = 60000)
    public void vlengthS1HSS4() throws Exception {
        testVectorLengthHSS("butallofthathappenedinmineralunconsciousnessandunobserved", 3, 7.54983443527f);
    }

    // similarity
    public void testSimilarityHSS(String s, String t, int k, float target) throws Exception {
        p = 7;

        HashStringSimilarity hss = new HashStringSimilarity(s, t, k);
        float sim = hss.similarity();
        assertTrue("incorrect hash string similarity",
                target - 0.01f < sim && sim < target + 0.01f);
        hssCorrectness += p;
    }

    @Test(timeout = 60000)
    public void similarityHSS0() throws Exception {
        testSimilarityHSS(
                "abcdefghijklmnopqrstuvwxyz",
                "abcdefghijklmnopqrstuvwxyz",
                2, 1.0f
        );
    }

    @Test(timeout = 60000)
    public void similarityHSS1() throws Exception {
        testSimilarityHSS(
                "abcdefghijklmnopqrstuvwxyz",
                "qrstuvwxyzabcdefghijklmnop",
                8, 0.631578947368f
        );
    }

    @Test(timeout = 60000)
    public void similarityHSS2() throws Exception {
        testSimilarityHSS(
                "inaholeinthegroundtherelivedahobbit",
                "ithadaperfectlyrounddoorlikeaportholepaintedgreen",
                2, 0.451335466924f
        );
    }

    @Test(timeout = 60000)
    public void similarityHSS3() throws Exception {
        testSimilarityHSS(
                "marswasemptybeforewecamethatsnottosaythatnothinghadeverhappened",
                "theplanethadaccretedmeltedroiledandcooledleavingasurfacescarredbyenormousgeolocicalfeaturescraterscanyonsvolcanoes",
                4, 0.0120543186004f
        );
    }

    @Test(timeout = 60000)
    public void similarityHSS4() throws Exception {
        testSimilarityHSS(
                "butallofthathappenedinmineralunconsciousnessandunobserved",
                "therewerenowitnesses",
                3, 0.0592348877759f
        );
    }

    // ================================================================
    // TEST HASH CODE SIMILARITY
    // ================================================================

    // vector length
    public void testVectorLengthHCS(String s, int k, float target) throws Exception {
        p = 7;

        HashCodeSimilarity hcs = new HashCodeSimilarity(s, s, k);
        float vl = hcs.lengthOfS1();
        assertTrue("incorrect hash code vector length",
                target - 0.01f < vl && vl < target + 0.01f);
        hcsCorrectness += p;
    }

    @Test(timeout = 60000)
    public void vlengthS1HCS0() throws Exception {
        testVectorLengthHCS("abcdefghijklmnopqrstuvwxyz", 2, 5.0f);
    }

    @Test(timeout = 60000)
    public void vlengthS1HCS1() throws Exception {
        testVectorLengthHCS("abcdefghijklmnopqrstuvwxyz", 8, 4.35889894354f);
    }

    @Test(timeout = 60000)
    public void vlengthS1HCS2() throws Exception {
        testVectorLengthHCS("inaholeinthegroundtherelivedahobbit", 2, 6.63324958071f);
    }

    @Test(timeout = 60000)
    public void vlengthS1HCS3() throws Exception {
        testVectorLengthHCS("marswasemptybeforewecamethatsnottosaythatnothinghadeverhappened", 4, 7.87400787401f);
    }

    @Test(timeout = 60000)
    public void vlengthS1HCS4() throws Exception {
        testVectorLengthHCS("butallofthathappenedinmineralunconsciousnessandunobserved", 3, 7.54983443527f);
    }

    // similarity
    public void testSimilarityHCS(String s, String t, int k, float target) throws Exception {
        p = 7;

        HashCodeSimilarity hcs = new HashCodeSimilarity(s, t, k);
        float sim = hcs.similarity();
        assertTrue("incorrect hash code similarity",
                target - 0.01f < sim && sim < target + 0.01f);
        hcsCorrectness += p;
    }

    @Test(timeout = 60000)
    public void similarityHCS0() throws Exception {
        testSimilarityHCS(
                "abcdefghijklmnopqrstuvwxyz",
                "abcdefghijklmnopqrstuvwxyz",
                2, 1.0f
        );
    }

    @Test(timeout = 60000)
    public void similarityHCS1() throws Exception {
        testSimilarityHCS(
                "abcdefghijklmnopqrstuvwxyz",
                "qrstuvwxyzabcdefghijklmnop",
                8, 0.631578947368f
        );
    }

    @Test(timeout = 60000)
    public void similarityHCS2() throws Exception {
        testSimilarityHCS(
                "inaholeinthegroundtherelivedahobbit",
                "ithadaperfectlyrounddoorlikeaportholepaintedgreen",
                2, 0.451335466924f
        );
    }

    @Test(timeout = 60000)
    public void similarityHCS3() throws Exception {
        testSimilarityHCS(
                "marswasemptybeforewecamethatsnottosaythatnothinghadeverhappened",
                "theplanethadaccretedmeltedroiledandcooledleavingasurfacescarredbyenormousgeolocicalfeaturescraterscanyonsvolcanoes",
                4, 0.0120543186004f
        );
    }

    @Test(timeout = 60000)
    public void similarityHCS4() throws Exception {
        testSimilarityHCS(
                "butallofthathappenedinmineralunconsciousnessandunobserved",
                "therewerenowitnesses",
                3, 0.0592348877759f
        );
    }

    // // ================================================================
    // // TEST HASHTABLE EFFICIENCY
    // // ================================================================

    // @Test(timeout = 300000)
    // public void testHashTableEfficiency() {
    //     p = 0;

    //     HashTable h = new HashTable(17);

    //     long startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.add(new Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableAddDelta = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.search((i + i * i));
    //     }
    //     hashTableSearchIntDelta = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.search(new Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableSearchTupleDelta = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.remove(new Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableRemoveDelta = System.nanoTime() - startTime;
    // }

    // @Test(timeout = 300000)
    // public void testHashTableEfficiencyRef() {
    //     p = 0;

    //     ref.HashTable h = new ref.HashTable(17);

    //     long startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.add(new ref.Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableAddDeltaRef = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.search((i + i * i));
    //     }
    //     hashTableSearchIntDeltaRef = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.search(new ref.Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableSearchTupleDeltaRef = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.remove(new ref.Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableRemoveDeltaRef = System.nanoTime() - startTime;
    // }

    //     @Test(timeout = 300000)
    // public void testHashTableEfficiency2() {
    //     p = 0;

    //     HashTable h = new HashTable(17);

    //     long startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.add(new Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableAddDelta = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.search((i + i * i));
    //     }
    //     hashTableSearchIntDelta = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.search(new Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableSearchTupleDelta = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.remove(new Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableRemoveDelta = System.nanoTime() - startTime;
    // }

    // @Test(timeout = 300000)
    // public void testHashTableEfficiencyRef2() {
    //     p = 0;

    //     ref.HashTable h = new ref.HashTable(17);

    //     long startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.add(new ref.Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableAddDeltaRef = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.search((i + i * i));
    //     }
    //     hashTableSearchIntDeltaRef = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.search(new ref.Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableSearchTupleDeltaRef = System.nanoTime() - startTime;

    //     startTime = System.nanoTime();
    //     for (int i = 0; i < HashTableEfficiencyIterations; i++) {
    //         h.remove(new ref.Tuple((i + i * i), "string" + i));
    //     }
    //     hashTableRemoveDeltaRef = System.nanoTime() - startTime;
    // }

    // // ================================================================
    // // TEST BRUTEFORCESIMILARITY EFFICIENCY
    // // ================================================================

    // @Test(timeout = 300000)
    // public void testBFSEfficiency() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < bfsEfficiencyIterations; i++)
    //     {
    //         BruteForceSimilarity bfs = new BruteForceSimilarity(bfsString0, bfsString1, bfsShingleLength);
    //         float vl = bfs.lengthOfS1();
    //         float s = bfs.similarity();
    //     }
    //     bfsDelta = System.nanoTime() - startTime;
    // }

    // @Test(timeout = 300000)
    // public void testBFSEfficiencyRef() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < bfsEfficiencyIterations; i++)
    //     {
    //         ref.BruteForceSimilarity bfs = new ref.BruteForceSimilarity(bfsString0, bfsString1, bfsShingleLength);
    //         float vl = bfs.lengthOfS1();
    //         float s = bfs.similarity();
    //     }
    //     bfsDeltaRef = System.nanoTime() - startTime;
    // }

    // @Test(timeout = 300000)
    // public void testBFSEfficiency2() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < bfsEfficiencyIterations; i++)
    //     {
    //         BruteForceSimilarity bfs = new BruteForceSimilarity(bfsString0, bfsString1, bfsShingleLength);
    //         float vl = bfs.lengthOfS1();
    //         float s = bfs.similarity();
    //     }
    //     bfsDelta = System.nanoTime() - startTime;
    // }

    // @Test(timeout = 300000)
    // public void testBFSEfficiencyRef2() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < bfsEfficiencyIterations; i++)
    //     {
    //         ref.BruteForceSimilarity bfs = new ref.BruteForceSimilarity(bfsString0, bfsString1, bfsShingleLength);
    //         float vl = bfs.lengthOfS1();
    //         float s = bfs.similarity();
    //     }
    //     bfsDeltaRef = System.nanoTime() - startTime;
    // }

    // // ================================================================
    // // TEST HASHSTRINGSIMILARITY EFFICIENCY
    // // ================================================================

    // @Test(timeout = 300000)
    // public void testHSSEfficiency() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < hssEfficiencyIterations; i++)
    //     {
    //         HashStringSimilarity hss = new HashStringSimilarity(hssString0, hssString1, hssShingleLength);
    //         float vl = hss.lengthOfS1();
    //         float s = hss.similarity();
    //     }
    //     hssDelta = System.nanoTime() - startTime;
    // }

    // @Test(timeout = 300000)
    // public void testHSSEfficiencyRef() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < hssEfficiencyIterations; i++)
    //     {
    //         ref.HashStringSimilarity hss = new ref.HashStringSimilarity(hssString0, hssString1, hssShingleLength);
    //         float vl = hss.lengthOfS1();
    //         float s = hss.similarity();
    //     }
    //     hssDeltaRef = System.nanoTime() - startTime;
    // }

    // @Test(timeout = 300000)
    // public void testHSSEfficiency2() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < hssEfficiencyIterations; i++)
    //     {
    //         HashStringSimilarity hss = new HashStringSimilarity(hssString0, hssString1, hssShingleLength);
    //         float vl = hss.lengthOfS1();
    //         float s = hss.similarity();
    //     }
    //     hssDelta = System.nanoTime() - startTime;
    // }

    // @Test(timeout = 300000)
    // public void testHSSEfficiencyRef2() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < hssEfficiencyIterations; i++)
    //     {
    //         ref.HashStringSimilarity hss = new ref.HashStringSimilarity(hssString0, hssString1, hssShingleLength);
    //         float vl = hss.lengthOfS1();
    //         float s = hss.similarity();
    //     }
    //     hssDeltaRef = System.nanoTime() - startTime;
    // }

    // // ================================================================
    // // TEST HASHCODESIMILARITY EFFICIENCY
    // // ================================================================

    // @Test(timeout = 300000)
    // public void testHCSEfficiency() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < hcsEfficiencyIterations; i++)
    //     {
    //         HashCodeSimilarity hcs = new HashCodeSimilarity(hcsString0, hcsString1, hcsShingleLength);
    //         float vl = hcs.lengthOfS1();
    //         float s = hcs.similarity();
    //     }
    //     hcsDelta = System.nanoTime() - startTime;
    // }

    // @Test(timeout = 300000)
    // public void testHCSEfficiencyRef() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < hcsEfficiencyIterations; i++)
    //     {
    //         ref.HashCodeSimilarity hcs = new ref.HashCodeSimilarity(hcsString0, hcsString1, hcsShingleLength);
    //         float vl = hcs.lengthOfS1();
    //         float s = hcs.similarity();
    //     }
    //     hcsDeltaRef = System.nanoTime() - startTime;
    // }

    // @Test(timeout = 300000)
    // public void testHCSEfficiency2() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < hcsEfficiencyIterations; i++)
    //     {
    //         HashCodeSimilarity hcs = new HashCodeSimilarity(hcsString0, hcsString1, hcsShingleLength);
    //         float vl = hcs.lengthOfS1();
    //         float s = hcs.similarity();
    //     }
    //     hcsDelta = System.nanoTime() - startTime;
    // }

    // @Test(timeout = 300000)
    // public void testHCSEfficiencyRef2() {
    //     p = 0;

    //     long startTime = System.nanoTime();
    //     for(int i = 0; i < hcsEfficiencyIterations; i++)
    //     {
    //         ref.HashCodeSimilarity hcs = new ref.HashCodeSimilarity(hcsString0, hcsString1, hcsShingleLength);
    //         float vl = hcs.lengthOfS1();
    //         float s = hcs.similarity();
    //     }
    //     hcsDeltaRef = System.nanoTime() - startTime;
    // }

}
