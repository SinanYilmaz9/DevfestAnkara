package app.sinan.yilmaz.firebase.devfestankara.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyTopPostsFragment extends PostListFragment {

    public MyTopPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        String myUserId = getUid();
        Query myTopPostsQuery = databaseReference.child("user-posts").child(myUserId)
                .orderByChild("starCount");

        //orderByChild metodu ile belirtilen key tarafından sıralanmıs
        // yeni bir Query nesnesi olusturulur

        return myTopPostsQuery;
    }
}
