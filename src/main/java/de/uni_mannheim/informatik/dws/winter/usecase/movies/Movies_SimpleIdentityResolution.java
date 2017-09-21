/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.winter.usecase.movies;

import java.io.File;

import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.aggregators.VotingAggregator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.InstanceBasedRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.MatchableValue;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.CSVRecordReader;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Record;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.blocking.DefaultRecordValueGenerator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

/**
 * @author Oliver Lehmberg (oli@dwslab.de)
 *
 */
public class Movies_SimpleIdentityResolution {

	public static void main(String[] args) throws Exception {
		// load data
		DataSet<Record, Attribute> data1 = new HashedDataSet<>();
		new CSVRecordReader(0).loadFromCSV(new File("usecase/movie/input/scifi1.csv"), data1);
		DataSet<Record, Attribute> data2 = new HashedDataSet<>();
		new CSVRecordReader(0).loadFromCSV(new File("usecase/movie/input/scifi2.csv"), data2);
	
		// Initialize Matching Engine
		MatchingEngine<Record, Attribute> engine = new MatchingEngine<>();

		// define a blocker that uses the record values to generate pairs
		InstanceBasedRecordBlocker<Record, Attribute> blocker = new InstanceBasedRecordBlocker<>(
				new DefaultRecordValueGenerator(data1.getSchema()), 
				new DefaultRecordValueGenerator(data2.getSchema()));
		
		// to calculate the similarity score, aggregate the pairs by counting and normalise with the number of attributes in the smaller schema (= the maximum number of attributes that can match)
		VotingAggregator<Record, MatchableValue> aggregator = new VotingAggregator<>(true, Math.min(data1.getSchema().size(), data2.getSchema().size()), 0.3);

		Processable<Correspondence<Record, MatchableValue>> correspondences = engine.runSimpleIdentityResolution(data1, data2, blocker, aggregator);
		
		// print results
		for(Correspondence<Record, MatchableValue> cor : correspondences.get()) {
			System.out.println(String.format("'%s' <-> '%s' (%.4f)", cor.getFirstRecord().getIdentifier(), cor.getSecondRecord().getIdentifier(), cor.getSimilarityScore()));
			for(Correspondence<MatchableValue, Matchable> cause : cor.getCausalCorrespondences().get()) {
				System.out.print(String.format("%s (%.0f), ", cause.getFirstRecord().getValue(), cause.getSimilarityScore()));
			}
			System.out.println();
		}
	}

	
}
