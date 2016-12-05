package app.sinan.yilmaz.firebase.devfestankara.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecentPostsFragment extends PostListFragment {

    public RecentPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        Query recentPostsQuery = databaseReference.child("posts")
                .limitToFirst(100);

        //limitToFirst kullanarak son 100 postu cagırıyoruz

        return recentPostsQuery;
    }
}
