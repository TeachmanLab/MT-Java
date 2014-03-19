package edu.virginia.psyc.pi.DAO;

import edu.virginia.psyc.pi.Trial;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/19/14
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TrialDAO {

    public void insert(Trial trial);
    public List<Trial> getTrials();

}
