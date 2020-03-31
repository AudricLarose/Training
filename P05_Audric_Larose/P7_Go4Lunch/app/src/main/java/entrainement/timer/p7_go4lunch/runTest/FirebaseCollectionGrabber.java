package entrainement.timer.p7_go4lunch.runTest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entrainement.timer.p7_go4lunch.model.Me;

public class FirebaseCollectionGrabber {
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public interface OnFinish<T> {
        void success(List<T> objects);
        void error(Exception e);
    }

    public static <T> void getCollection(String collectionName, Class<T> tClass, OnFinish<T> onFinish, String... order) {
        CollectionReference collection = firestore
                .collection(collectionName).document(Me.getMonId()).collection("Myplace");
        if (order.length != 0) {
            for (int i = 0; i < order.length; i++) {
                Query query = collection.orderBy(order[i], Query.Direction.DESCENDING);
                query.get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                List<T> objects = new ArrayList<>();
                                List<DocumentSnapshot> docs = task.getResult().getDocuments();
                                for (DocumentSnapshot doc : docs) {
                                    T e = doc.toObject(tClass);
                                    objects.add(e);
                                }
                                onFinish.success(objects);
                            } else {
                                onFinish.error(task.getException());
                            }
                        });
            }
        } else {
            collection
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<T> objects = new ArrayList<>();
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for (DocumentSnapshot doc : docs) {
                                T e = doc.toObject(tClass);
                                objects.add(e);
                            }
                            onFinish.success(objects);
                        } else {
                            onFinish.error(task.getException());
                        }
                    });
        }
    }


}
