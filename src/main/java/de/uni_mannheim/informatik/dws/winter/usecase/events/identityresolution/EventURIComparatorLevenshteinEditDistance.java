package de.uni_mannheim.informatik.dws.winter.usecase.events.identityresolution;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinEditDistance;
import de.uni_mannheim.informatik.dws.winter.usecase.events.model.Event;
import de.uni_mannheim.informatik.dws.winter.usecase.events.utils.BestListSimilarity;

/**
 * {@link Comparator} for {@link Event}s based on the {@link Event#getUris()} ()}
 * value and their {@link LevenshteinEditDistance} value.
 * Based on the MovieTitleComparatorLevenshtein class
 *
 * @author Daniel Ringler
 *
 */
public class EventURIComparatorLevenshteinEditDistance implements Comparator<Event, Attribute> {
    private static final long serialVersionUID = 1L;

    private BestListSimilarity bestListSimilarity = new BestListSimilarity();
    private LevenshteinEditDistance sim = new LevenshteinEditDistance();
    private double threshold;

    public EventURIComparatorLevenshteinEditDistance(double t) {
        threshold = t;
    }

    @Override
    public double compare(
            Event record1,
            Event record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {
        return bestListSimilarity.getBestEditDistanceStripedLowercase(sim, record1.getUris(), record2.getUris(), threshold);
    }
}





