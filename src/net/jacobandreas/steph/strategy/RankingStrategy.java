package net.jacobandreas.steph.strategy;

import java.util.*;
import net.jacobandreas.steph.tournament.*;

/**
 * Interface specifying the behavior of a strategy for ranking teams at the
 * end of a tournament.
 *
 * @author jacob
 */
public interface RankingStrategy {

    /**
     * Stub to rank the teams.
     * @param teams The teams to rank.
     * @return The list of teams ordered by final ranking.
     */
    public List<Team> rank(Collection<Team> teams);

}
