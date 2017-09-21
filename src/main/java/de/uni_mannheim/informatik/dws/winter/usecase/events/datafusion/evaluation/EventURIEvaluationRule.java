package de.uni_mannheim.informatik.dws.winter.usecase.events.datafusion.evaluation;

import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.winter.usecase.events.model.Event;


/**
 * {@link EvaluationRule} for the URIs of {@link Event}s. The rule simply
 * compares the uris of two {@link Event}s and returns true, in case their
 * similarity based on {@link LevenshteinSimilarity} is 1.0 for at least on label.
 * Based on TitleEvaluationRule. Created on 2017-01-06
 * @author Daniel Ringler
 *
 */
public class EventURIEvaluationRule extends EvaluationRule<Event, Attribute> {

    SimilarityMeasure<String> sim = new LevenshteinSimilarity();

    @Override
    public boolean isEqual(Event record1, Event record2, Attribute attribute) {
        //compare all labels
        for (String r1URI : record1.getUris()) {
            for (String r2URI : record2.getUris()) {
                double uriResult = sim.calculate(r1URI, r2URI);
                // return true if both labels match
                if (uriResult == 1.0)
                    return true;
            }
        }
        //return false if none of the labels matches
        return false;
    }

    /* (non-Javadoc)
     * @see de.uni_mannheim.informatik.wdi.datafusion.EvaluationRule#isEqual(java.lang.Object, java.lang.Object, de.uni_mannheim.informatik.wdi.model.Correspondence)
     */
    @Override
    public boolean isEqual(Event record1, Event record2,
                           Correspondence<Attribute, Matchable> schemaCorrespondence) {
        return isEqual(record1, record2, (Attribute) null);
    }

}
