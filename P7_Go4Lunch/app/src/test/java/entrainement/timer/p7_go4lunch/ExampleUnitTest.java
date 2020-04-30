package entrainement.timer.p7_go4lunch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.model.Results;
import entrainement.timer.p7_go4lunch.utils.Other;

import static org.junit.Assert.assertSame;

public class ExampleUnitTest {

    @Test
        public void test_whocome_comparator() {
        List<Results> oldOne= new ArrayList<>();
        oldOne.add(new Results("1","0","0"));
        oldOne.add(new Results("2","1","1"));
        oldOne.add(new Results("3","2","2"));
        oldOne.add(new Results("4","3","3"));
        List<Results> newOne=Other.sortedByWhoCome(oldOne);
            assertSame(newOne.get(0).getWhocome(),"3");
            assertSame(newOne.get(1).getWhocome(),"2");
            assertSame(newOne.get(2).getWhocome(),"1");
            assertSame(newOne.get(3).getWhocome(),"0");
        }
    @Test
    public void test_like_comparator() {
        List<Results> oldOne= new ArrayList<>();
        oldOne.add(new Results("1","0","0"));
        oldOne.add(new Results("2","1","1"));
        oldOne.add(new Results("3","2","2"));
        oldOne.add(new Results("4","3","3"));
        List<Results> newOne=Other.sortedByLike(oldOne);
        assertSame(newOne.get(0).getLike(),"3");
        assertSame(newOne.get(1).getLike(),"2");
        assertSame(newOne.get(2).getLike(),"1");
        assertSame(newOne.get(3).getLike(),"0");
    }
    @Test
    public void test_updatelist() {
        List<Results> oldOne= new ArrayList<>();
        oldOne.add(new Results("1","0","0"));
        List<Results> newOne=Other.incrementIncomeCollegue("1",oldOne,1);
        assertSame(1,Integer.valueOf(newOne.get(0).getWhocome()));
    }

    @Test
    public void test_updatelistLike() {
        List<Results> oldOne= new ArrayList<>();
        oldOne.add(new Results("1","0","0"));
        List<Results> newOne=Other.incrementNumberOfLike("1",oldOne,1);
        assertSame(1,Integer.valueOf(newOne.get(0).getLike()));
    }
    @Test
    public void test_updatelistMinus() {
        List<Results> oldOne= new ArrayList<>();
        oldOne.add(new Results("1","1","0"));
        List<Results> newOne=Other.incrementIncomeCollegue("1",oldOne,-1);
        assertSame(0,Integer.valueOf(newOne.get(0).getWhocome()));
    }

    @Test
    public void test_updatelistLikeMinus() {
        List<Results> oldOne= new ArrayList<>();
        oldOne.add(new Results("1","0","1"));
        List<Results> newOne=Other.incrementNumberOfLike("1",oldOne,-1);
        assertSame(0,Integer.valueOf(newOne.get(0).getWhocome()));
    }
    }
