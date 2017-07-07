package com.mwf.fuzzyquery.fakesearchview;

/**
 * Interface to make any model searchable by the {@link FakeSearchAdapter}
 *
 * @author Leonardo Rossetto
 */
public interface SearchItem {

    /**
     * This provide to {@link FakeSearchAdapter} the method to
     * check if that model match the constraint search
     *
     * @param constraint used by the adapter to search
     * @return true if the model match false otherwise
     */
    boolean match(CharSequence constraint);

}
