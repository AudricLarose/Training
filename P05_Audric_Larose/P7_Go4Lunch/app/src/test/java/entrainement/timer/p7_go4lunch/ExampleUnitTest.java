package entrainement.timer.p7_go4lunch;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import entrainement.timer.p7_go4lunch.Collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.Restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.Restaurant.Place;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private List<Place> liste_de_place = new ArrayList<>();
    private Map<String,Object> updateMap = new HashMap();
    private ExtendedServicePlace servicePlace = DI.getServicePlace();
    private ExtendedServiceCollegue serviceCollegue= DI.getService();

    @Mock
    private Task<Void> mockVoidTask;

    @Mock
    private DocumentReference documentReference;

    @Mock
    private DocumentReference emptyDocumentReference;

    @Mock
    private CollectionReference collectionReference;

    @Mock
    private CollectionReference emptyCollectionReference;

    @Mock
    private Query queryReference;

    @Mock
    private Query emptyQueryReference;

    @Mock
    private DocumentSnapshot documentSnapshot;

    @Mock
    private DocumentSnapshot emptyDocumentSnapshot;

    @Mock
    private QuerySnapshot querySnapshot;
    @Mock
    private QuerySnapshot emptyQuerySnapshot;

    @Mock
    private Task<DocumentSnapshot> emptyDocumentSnapshotTask;

    @Mock
    private Task<DocumentSnapshot> documentSnapshotTask;

    @Mock
    private Task<DocumentReference> documentRefTask;

    @Mock
    private Task<QuerySnapshot> queryResultTask;

    @Mock
    private Task<QuerySnapshot> emptyQueryResultTask;

    @Mock
    private ListenerRegistration registration;
    @Before
    public void setup() {
        liste_de_place= servicePlace.generateListPlace();
        MockitoAnnotations.initMocks(this);
        when(documentReference.get()).thenReturn(documentSnapshotTask);

        when(documentReference.get()).thenReturn(documentSnapshotTask);
        when(emptyDocumentReference.get()).thenReturn(emptyDocumentSnapshotTask);
        when(collectionReference.get()).thenReturn(queryResultTask);
        when(emptyCollectionReference.get()).thenReturn(emptyQueryResultTask);
        when(queryReference.get()).thenReturn(queryResultTask);
        when(emptyQueryReference.get()).thenReturn(emptyQueryResultTask);
        when(documentReference.delete()).thenReturn(mockVoidTask);
        when(documentReference.update(updateMap)).thenReturn(mockVoidTask);
        when(documentSnapshot.exists()).thenReturn(true); //This snapshots exist
        when(documentSnapshot.exists()).thenReturn(true); //This snapshots exist
        when(emptyDocumentSnapshot.exists()).thenReturn(false); //This snapshots exist
        when(querySnapshot.isEmpty()).thenReturn(false);
        when(querySnapshot.iterator()).thenAnswer(new Answer<Iterator<DocumentSnapshot>>() {
            @Override
            public Iterator<DocumentSnapshot> answer(InvocationOnMock invocation) {
                return Collections.singletonList(documentSnapshot).iterator();
            }
        });
        when(emptyQuerySnapshot.isEmpty()).thenReturn(true);
    }
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