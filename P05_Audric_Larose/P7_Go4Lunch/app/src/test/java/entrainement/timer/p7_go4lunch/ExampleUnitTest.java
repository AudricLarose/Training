package entrainement.timer.p7_go4lunch;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import entrainement.timer.p7_go4lunch.model.Location;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.model.Results;
import entrainement.timer.p7_go4lunch.utils.Other;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class ExampleUnitTest {
//    private List<Results> liste_de_place = new ArrayList<>();
//    private Map<String,Object> updateMap = new HashMap();
//    private ExtendedServicePlace servicePlace = DI.getServicePlace();
////    private ExtendedServiceCollegue serviceCollegue= DI.getService();
//
//    @Mock
//    private Task<Void> mockVoidTask;

//    @Mock
//    private DocumentReference documentReference;
//
//    @Mock
//    private DocumentReference emptyDocumentReference;
//
//    @Mock
//    private CollectionReference collectionReference;
//
//    @Mock
//    private CollectionReference emptyCollectionReference;
//
//    @Mock
//    private Query queryReference;
//
//    @Mock
//    private Query emptyQueryReference;
//
//    @Mock
//    private DocumentSnapshot documentSnapshot;
//
//    @Mock
//    private DocumentSnapshot emptyDocumentSnapshot;
//
//    @Mock
//    private QuerySnapshot querySnapshot;
//    @Mock
//    private QuerySnapshot emptyQuerySnapshot;
//
//    @Mock
//    private Task<DocumentSnapshot> emptyDocumentSnapshotTask;
//
//    @Mock
//    private Task<DocumentSnapshot> documentSnapshotTask;
//
//    @Mock
//    private Task<DocumentReference> documentRefTask;
//
//    @Mock
//    private Task<QuerySnapshot> queryResultTask;
//
//    @Mock
//    private Task<QuerySnapshot> emptyQueryResultTask;
//
//    @Mock
//    private ListenerRegistration registration;
    @Before
    public void setup() {
//        liste_de_place= servicePlace.generateListPlaceAPI();
//        MockitoAnnotations.initMocks(this);
//        when(documentReference.get()).thenReturn(documentSnapshotTask);
//
//        when(documentReference.get()).thenReturn(documentSnapshotTask);
//        when(emptyDocumentReference.get()).thenReturn(emptyDocumentSnapshotTask);
//        when(collectionReference.get()).thenReturn(queryResultTask);
//        when(emptyCollectionReference.get()).thenReturn(emptyQueryResultTask);
//        when(queryReference.get()).thenReturn(queryResultTask);
//        when(emptyQueryReference.get()).thenReturn(emptyQueryResultTask);
//        when(documentReference.delete()).thenReturn(mockVoidTask);
//        when(documentReference.update(updateMap)).thenReturn(mockVoidTask);
//        when(documentSnapshot.exists()).thenReturn(true); //This snapshots exist
//        when(documentSnapshot.exists()).thenReturn(true); //This snapshots exist
//        when(emptyDocumentSnapshot.exists()).thenReturn(false); //This snapshots exist
//        when(querySnapshot.isEmpty()).thenReturn(false);
//        when(querySnapshot.iterator()).thenAnswer(new Answer<Iterator<DocumentSnapshot>>() {
//            @Override
//            public Iterator<DocumentSnapshot> answer(InvocationOnMock invocation) {
//                return Collections.singletonList(documentSnapshot).iterator();
//            }
//        });
//        when(emptyQuerySnapshot.isEmpty()).thenReturn(true);
    }
//    void methode() {
//        List mockedList = mock(List.class);
//        when(mockedList.get(0)).thenReturn("first");
//// using mock object - it does not throw any "unexpected interaction" exception
//        mockedList.clear();
//
//        // selective, explicit, highly readable verification
//        verify(mockedList).add("one");
//        verify(mockedList).clear();
//    }

//    @Mock
//    List<String> mockedList;
//
//    @Test
//    public void whenUseMockAnnotation_thenMockIsInjected() {
//        mockedList.add("one");
//        Mockito.verify(mockedList).add("one");
//        assertEquals(0, mockedList.size());
//
//        Mockito.when(mockedList.size()).thenReturn(100);
//        assertEquals(100, mockedList.size());
//    }
//
//    public void getlistcollegue(){
//        Query querymocked=mock(Query.class);
//        Task<QuerySnapshot> task = mock(Task.class);
//        when(querymocked.get()).thenReturn(task);
//
//    }
//    @Test
//    public void test_distance() {
//        Double lat=2.4538914;
//        Double longi= 48.7795441;
//        Me.setMy_longitude(48.7786815);
//        Me.setMy_latitude(2.4687938);
//        Location location= new Location();
//        int distance=Other.getDistance(lat,longi);
//        assertSame(distance,0);
//    }



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
        List<Results> newOne=Other.updatemylisteRest("1",oldOne,1);
        assertSame(1,Integer.valueOf(newOne.get(0).getWhocome()));
    }

    @Test
    public void test_updatelistLike() {
        List<Results> oldOne= new ArrayList<>();
        oldOne.add(new Results("1","0","0"));
        List<Results> newOne=Other.updatemylisteLike("1",oldOne,1);
        assertSame(0,Integer.valueOf(newOne.get(0).getLike()));
    }
    @Test
    public void test_updatelistMinus() {
        List<Results> oldOne= new ArrayList<>();
        oldOne.add(new Results("1","1","0"));
        List<Results> newOne=Other.updatemylisteRest("1",oldOne,-1);
        assertSame(0,Integer.valueOf(newOne.get(0).getWhocome()));
    }

    @Test
    public void test_updatelistLikeMinus() {
        List<Results> oldOne= new ArrayList<>();
        oldOne.add(new Results("1","0","1"));
        List<Results> newOne=Other.updatemylisteLike("1",oldOne,-1);
        assertSame(0,Integer.valueOf(newOne.get(0).getWhocome()));
    }
    }
