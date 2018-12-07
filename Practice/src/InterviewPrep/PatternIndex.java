package InterviewPrep;


// Introduction to Software Testing
// Authors: Paul Ammann & Jeff Offutt
// Chapters 6, 7, and 9; pages ??, ??, and ??
// Can be run from command line
// See PatternIndexTest.java, DataDrivenPatternIndexTest.java  for JUnit tests

public class PatternIndex
{

   public static void main (String[] argv)
   {
     String subject = "string"; 
     String pattern = "ng";  
     System.out.println(patternIndex(subject, pattern));
   }
   
  /**
    * Find index of pattern in subject string
    * 
    * @param subject String to search
    * @param pattern String to find
    * @return index (zero-based) of first occurrence of pattern in subject; -1 if not found
    * @throws NullPointerException if subject or pattern is null
    */
   public static int patternIndex (String subject, String pattern)
   {
      final int NOTFOUND = -1;
      int  iSub = 0, rtnIndex = NOTFOUND;
      boolean isPat  = false;
      int subjectLen = subject.length();
      int patternLen = pattern.length();
   
//      while (isPat == false && isub + patternLen – 1 < subjectLen) // Original
//    	  While (isPat == false && isub + patternLen – 0 < subjectLen) // Mutant A
//    	  isPat = false; 
      while (isPat == false && iSub + patternLen - 1 < subjectLen)
//      while (isPat == false && iSub + patternLen - 0 < subjectLen)
      {
         if (subject.charAt(iSub) == pattern.charAt(0))
         {
            rtnIndex = iSub; // Starting at zero
            isPat = true;
            for (int iPat = 1; iPat < patternLen; iPat ++)
            {
               if (subject.charAt(iSub + iPat) != pattern.charAt(iPat))
               {
                  rtnIndex = NOTFOUND;
                  isPat = false;
                  break;  // out of for loop
               }
            }
         }
         iSub ++;
      }
      return (rtnIndex);
   }
}

