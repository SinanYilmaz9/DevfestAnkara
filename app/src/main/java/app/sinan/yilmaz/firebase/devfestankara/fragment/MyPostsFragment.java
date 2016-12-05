package app.sinan.yilmaz.firebase.devfestankara.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyPostsFragment extends PostListFragment {

    public MyPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        //giris yapan user ın tüm postları alınır
        return databaseReference.child("user-posts")
                .child(getUid());
    }
}
