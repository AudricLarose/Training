package entrainement.timer.p7_go4lunch;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    void methode() {
        List mockedList = mock(List.class);
        when(mockedList.get(0)).thenReturn("first");
// using mock object - it does not throw any "unexpected interaction" exception
        mockedList.clear();

        // selective, explicit, highly readable verification
        verify(mockedList).add("one");
        verify(mockedList).clear();
    }

    @Test
    public void addition_isCorrect() {
        // you can mock concrete classes, not only interfaces
        LinkedList mockedList = mock(LinkedList.class);

// stubbing appears before the actual execution
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(999)).thenReturn("999");

// the following prints "first"
        System.out.println(mockedList.get(0));

// the following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));

    }
    public void getlistcollegue(){
        Query querymocked=mock(Query.class);
        Task<QuerySnapshot> task = mock(Task.class);
        when(querymocked.get()).thenReturn(task);


    }
}