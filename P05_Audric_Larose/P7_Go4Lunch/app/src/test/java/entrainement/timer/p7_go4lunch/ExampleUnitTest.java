package entrainement.timer.p7_go4lunch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.model.Results;
import entrainement.timer.p7_go4lunch.utils.Other;

import static org.junit.Assert.assertSame;


public class ExampleUnitTest {
//    private List<Place> liste_de_place = new ArrayList<>();
//    private Map<String,Object> updateMap = new HashMap();
//    private ExtendedServicePlace servicePlace = DI.getServicePlace();
//    private ExtendedServiceCollegue serviceCollegue= DI.getService();
//
//    @Mock
//    private Task<Void> mockVoidTask;
//
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
//    @Before
//    public void setup() {
//        liste_de_place= servicePlace.generateListPlace();
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
//    }
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
//
//    @Test
//    public void addition_isCorrect() {
//        // you can mock concrete classes, not only interfaces
//        LinkedList mockedList = mock(LinkedList.class);
//
//// stubbing appears before the actual execution
//        when(mockedList.get(0)).thenReturn("first");
//        when(mockedList.get(999)).thenReturn("999");
//
//// the following prints "first"
//        System.out.println(mockedList.get(0));
//
//// the following prints "null" because get(999) was not stubbed
//        System.out.println(mockedList.get(999));
//
//    }
//
//    public void getlistcollegue(){
//        Query querymocked=mock(Query.class);
//        Task<QuerySnapshot> task = mock(Task.class);
//        when(querymocked.get()).thenReturn(task);
//
//    }

    @Test
        public void test_whocome_comparator() {
        ExtendedServicePlace servicePlace=DI.getServicePlace();
        List<Results> oldOne=servicePlace.generateListPlaceAPI();
        List<Results> listesortedA=new ArrayList<>();
        oldOne.add(new Results("1","0","0"));
        oldOne.add(new Results("2","1","1"));
        oldOne.add(new Results("3","2","2"));
        oldOne.add(new Results("4","3","3"));
        Other.SortedByWhoCome();
            assertSame(oldOne.get(0).getWhocome(),"0");
            assertSame(oldOne.get(1).getWhocome(),"1");
            assertSame(oldOne.get(2).getWhocome(),"2");
        }
    @Test
    public void test_like_comparator() {
        ExtendedServicePlace servicePlace=DI.getServicePlace();
        List<Results> oldOne=servicePlace.generateListPlaceAPI();
        List<Results> listesortedA=new ArrayList<>();
        oldOne.add(new Results("1","0","0"));
        oldOne.add(new Results("2","1","1"));
        oldOne.add(new Results("3","2","2"));
        oldOne.add(new Results("4","3","3"));
        Other.SortedByLike();
        assertSame(oldOne.get(0).getLike(),"0");
        assertSame(oldOne.get(1).getLike(),"1");
        assertSame(oldOne.get(2).getLike(),"2");
    }

    }
