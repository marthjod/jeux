package de.fhb.jeux.config;

/* POJO equivalent for a single bonus points distribution config file (JSON) entry.
 * A bonuspointsdistribution.json file looks like this (3-entry list here):

 [
 {
 "setsWonByWinner":1,
 "totalSets":1,
 "bonusPointsWinner":1,
 "bonusPointsLoser":0
 },
 {
 "setsWonByWinner":2,
 "totalSets":2,
 "bonusPointsWinner":4,
 "bonusPointsLoser":0
 },
 {
 "setsWonByWinner":2,
 "totalSets":3,
 "bonusPointsWinner":3,
 "bonusPointsLoser":1
 }
 ]

 */
public class BonusPointsRule {
	private int setsWonByWinner;
	private int totalSets;
	private int bonusPointsWinner;
	private int bonusPointsLoser;

	public BonusPointsRule() {
	}

	public int getSetsWonByWinner() {
		return setsWonByWinner;
	}

	public int getTotalSets() {
		return totalSets;
	}

	public int getBonusPointsWinner() {
		return bonusPointsWinner;
	}

	public int getBonusPointsLoser() {
		return bonusPointsLoser;
	}
}
