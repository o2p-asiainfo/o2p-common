package com.ailk.eaap.o2p.util.file.filters;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author 颖勤
 *
 */
public abstract class AbstractFileListFilter<F> {
    /**
     * {@inheritDoc}
     */
    public final List<F> filterFiles(F[] files) {
        List<F> accepted = new ArrayList<F>();
        if (files != null) {
            for (F file : files) {
                if (this.accept(file)) {
                    accepted.add(file);
                }
            }
        }
        return accepted;
    }

    /**
     * Subclasses must implement this method.
     */
    protected abstract boolean accept(F file);
}
