package app.sinan.yilmaz.firebase.devfestankara;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import app.sinan.yilmaz.firebase.devfestankara.R;

import app.sinan.yilmaz.firebase.devfestankara.models.Post;
import app.sinan.yilmaz.firebase.devfestankara.models.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends BaseActivity {

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";


    private DatabaseReference mDatabase;
    private StorageReference storageRef;

    String imageUrl;
    Uri downloadUrl;
    private Uri image;

    private EditText mTitleField;
    private EditText mBodyField;
    private ImageButton mImageButton;
    private FloatingActionButton mSubmitButton;

    private static final int GALLERY_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();


        mTitleField = (EditText) findViewById(R.id.field_title);
        mBodyField = (EditText) findViewById(R.id.field_body);
        mImageButton = (ImageButton) findViewById(R.id.postImage);
        mSubmitButton = (FloatingActionButton) findViewById(R.id.fab_submit_post);



     mImageButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
             gallery.setType("image/*");
             startActivityForResult(gallery,GALLERY_REQUEST);

         }
     });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {
        final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();


        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return;
        }


        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }




        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();


        final String userId = getUid();

       StorageReference filePath = storageRef.child("photos").child(image.getLastPathSegment());

        filePath.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                downloadUrl = taskSnapshot.getDownloadUrl();
                imageUrl = downloadUrl.toString();

                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                User user = dataSnapshot.getValue(User.class);


                                if (user == null) {
                                    //
                                    Log.e(TAG, "User " + userId + " is unexpectedly null");
                                    Toast.makeText(NewPostActivity.this,
                                            "Error: could not fetch user.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // Yeni bir post yaz
                                    writeNewPost(userId, user.username, title, body,imageUrl);
                                }


                                setEditingEnabled(true);
                                finish();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());

                                setEditingEnabled(true);

                            }
                        });
            }
        });



   }

    private void setEditingEnabled(boolean enabled) {
        mTitleField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

        image = data.getData();

            mImageButton.setImageURI(image);

    }}
    private void writeNewPost(String userId, String username, String title, String body, String imageUrl) {
        // Yeni bir post olustur  /user-posts/$userid/$postid ve
        // /posts/$postid aynı anda her iki yolda
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body, imageUrl);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }}
