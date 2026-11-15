/*package Starter_App.Starter_ApUnitTesting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
//------------------------
//------Unit-Testing------
//------------------------

import Starter_App.Starter_Ap.tablesearch;

public class TableSearch_UnitTesting {
    @Test
	    void trimsLeadingAndTrailingWhitespace() {
	        assertEquals("mouse", tablesearch.normalize("  mouse  "));
	    }

	    @Test
	    void lowerCasesInput() {
	        assertEquals("wireless mouse", tablesearch.normalize("Wireless MOUSE"));
	    }

	    @Test
	    void returnsEmptyStringForNull() {
	        assertEquals("", tablesearch.normalize(null));
	    }

	    @Test
	    void emptyQueryMeansEveryRowMatches() {
	        // Mirrors the predicate logic inside connectSearch: query.isEmpty() -> true
	        String query = tablesearch.normalize("   ");
	        assertTrue(query.isEmpty());
	    }
	}

}*/
