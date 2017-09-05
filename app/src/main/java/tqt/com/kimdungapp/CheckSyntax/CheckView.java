package tqt.com.kimdungapp.CheckSyntax;

/**
 * Created by sandwraith8 on 21/04/2017.
 */

import java.util.ArrayList;

import tqt.com.kimdungapp.entry.RuleResult;
import tqt.com.kimdungapp.entry.SearchResult;


public interface CheckView {
    void getAllWordInvalidDone(RuleResult results);

    void getAllWordInvalidSuccess(ArrayList<SearchResult> results);

    void updateWordInvalidDone(SearchResult result);
}

