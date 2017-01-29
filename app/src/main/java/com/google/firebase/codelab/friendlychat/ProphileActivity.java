package com.google.firebase.codelab.friendlychat;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProphileActivity extends AppCompatActivity {
    private final int CROPPING_IMAGE_FROM_GALERY_REQUEST_CODE = 1;
    private final int CAPTURE_IMAGE_CAMERA_REQUEST_CODE = 2;
    private FirebaseStorage firebaseStorage;
    private ImageButton cameraButton0;
    private ImageButton cameraButton1;
    private ImageButton cameraButton2;
    private ImageButton cameraButton3;
    private ImageButton cameraButton4;
    private ImageButton cameraButton5;
    private CircleImageView mImageViewAvatar0;
    private CircleImageView mImageViewAvatar1;
    private CircleImageView mImageViewAvatar2;
    private CircleImageView mImageViewAvatar3;
    private CircleImageView mImageViewAvatar4;
    private CircleImageView mImageViewAvatar5;
    private DatabaseReference mFirebaseDatabaseReference;
    private Button sendAboutButton;
    private TextView aboutMeTextView;
    private EditText aboutMeEditText;
    private  ArrayList<CircleImageView> imageViewArrayList;
    private  ArrayList<ImageButton> imageButtonArrayList;
    private int lastButtonPressedPosition;
    private  Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prophile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        sendAboutButton = (Button)findViewById(R.id.sendAboutButton);
        aboutMeTextView = (TextView) findViewById(R.id.aboutMeTextView);
        aboutMeEditText = (EditText) findViewById(R.id.aboutMeEditText);
        cameraButton0 = (ImageButton)findViewById(R.id.imageButtonDrawerIntentPhoto0);
        cameraButton1 = (ImageButton)findViewById(R.id.imageButtonDrawerIntentPhoto1);
        cameraButton2 = (ImageButton)findViewById(R.id.imageButtonDrawerIntentPhoto2);
        cameraButton3 = (ImageButton)findViewById(R.id.imageButtonDrawerIntentPhoto3);
        cameraButton4 = (ImageButton)findViewById(R.id.imageButtonDrawerIntentPhoto4);
        cameraButton5 = (ImageButton)findViewById(R.id.imageButtonDrawerIntentPhoto5);

        mImageViewAvatar0 = (CircleImageView)findViewById(R.id.imageViewDrawerAvatar0);
        mImageViewAvatar1 = (CircleImageView)findViewById(R.id.imageViewDrawerAvatar1);
        mImageViewAvatar2 = (CircleImageView)findViewById(R.id.imageViewDrawerAvatar2);
        mImageViewAvatar3 = (CircleImageView)findViewById(R.id.imageViewDrawerAvatar3);
        mImageViewAvatar4 = (CircleImageView)findViewById(R.id.imageViewDrawerAvatar4);
        mImageViewAvatar5 = (CircleImageView)findViewById(R.id.imageViewDrawerAvatar5);

        imageViewArrayList = new ArrayList<>();
        imageButtonArrayList = new ArrayList<>();
        imageButtonArrayList.add(cameraButton0);
        imageButtonArrayList.add(cameraButton1);
        imageButtonArrayList.add(cameraButton2);
        imageButtonArrayList.add(cameraButton3);
        imageButtonArrayList.add(cameraButton4);
        imageButtonArrayList.add(cameraButton5);
        imageViewArrayList.add(mImageViewAvatar0);
        imageViewArrayList.add(mImageViewAvatar1);
        imageViewArrayList.add(mImageViewAvatar2);
        imageViewArrayList.add(mImageViewAvatar3);
        imageViewArrayList.add(mImageViewAvatar4);
        imageViewArrayList.add(mImageViewAvatar5);


        mFirebaseDatabaseReference.child("usersNew").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profilePic").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Glide.with(ProphileActivity.this)
                            .load(Uri.parse(postSnapshot.getValue(String.class)))
                            .into(imageViewArrayList.get(Integer.parseInt(postSnapshot.getKey())));
                    ++i;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFirebaseDatabaseReference.child("usersNew").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mAboutMe").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String aboutMe = snapshot.getValue(String.class);
                aboutMeTextView.setText(aboutMe);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        for(final ImageButton imageButton:imageButtonArrayList){
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastButtonPressedPosition = imageButtonArrayList.indexOf(imageButton);
                    onAvatarButtonClicked();
                }
            });
        }


        sendAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aboutString = aboutMeEditText.getText().toString();
                aboutMeTextView.setText(aboutString);
                aboutMeEditText.setText("");
                mFirebaseDatabaseReference.child("usersNew").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mAboutMe")
                        .setValue(aboutString);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case CROPPING_IMAGE_FROM_GALERY_REQUEST_CODE:

                    if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                        Uri filePath = data.getData();
                        Uri filePathCompressed = null;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                            filePathCompressed = getImageUri(this,bitmap);

                            //filePathCompressed = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), filePath.toString(), "Title", null));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                        Glide.with(ProphileActivity.this)
                                .load(filePathCompressed != null?filePathCompressed:filePath)
                                .into(imageViewArrayList.get(lastButtonPressedPosition));
                        uploadFile(lastButtonPressedPosition+"",filePathCompressed != null?filePathCompressed:filePath);
                    }
                    break;
                case CAPTURE_IMAGE_CAMERA_REQUEST_CODE:
//                    Picasso.with(getApplicationContext()).load(mCurrentPhotoPath).transform(new CircleTransform()).into(mImageViewAvatar);
//                    ImageUtils.getBitmapFromUri(getApplicationContext(), target, Uri.parse(mCurrentPhotoPath));
                    if ( resultCode == Activity.RESULT_OK) {
                        // Check if the result includes a thumbnail Bitmap

                       if(data != null && data.getData() != null){
                           Uri filePath = data.getData();
                           Uri filePathCompressed = null;
                           try {
                               Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                               filePathCompressed = getImageUri(this,bitmap);
                           } catch (IOException e) {
                               e.printStackTrace();
                           }



                           Glide.with(ProphileActivity.this)
                                   .load(filePathCompressed != null?filePathCompressed:filePath)
                                   .into(imageViewArrayList.get(lastButtonPressedPosition));
                           uploadFile(lastButtonPressedPosition+"",filePathCompressed != null?filePathCompressed:filePath);
                       }

                        if (data == null) {
                            // TODO Do something with the full image stored
                            // in outputFileUri. Perhaps copying it to the app folder
                            Uri filePath =  outputFileUri;
                            Uri filePathCompressed = null;
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                                filePathCompressed = getImageUri(this,bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Glide.with(ProphileActivity.this)
                                    .load(filePathCompressed != null?filePathCompressed:filePath)
                                    .into(imageViewArrayList.get(lastButtonPressedPosition));
                            uploadFile(lastButtonPressedPosition+"",filePathCompressed != null?filePathCompressed:filePath);
                        }
                    }


                    break;
            }
        }
    }




    public static Uri getImageUri(Context aContext, Bitmap aBitmap) {
       // ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        OutputStream output=null;
//        output=new FileOutputStream(file);
//
//
//        try {
//
//            // Compress into png format image from 0% - 100%
//            aBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
//            output.flush();
//            output.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String path = MediaStore.Images.Media.insertImage(aContext.getContentResolver(), aBitmap, "temp.jpg", null);
        OutputStream imagefile = null;
        try {
            imagefile = new FileOutputStream("temp.jpg");
            aBitmap.compress(Bitmap.CompressFormat.JPEG, 50, imagefile);
            imagefile.flush();
            imagefile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
// Write 'bitmap' to file using JPEG and 80% quality hint for JPEG:
//        aBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        return Uri.parse(path);
    }




    //this method will upload the file
    private void uploadFile(String fileName, Uri filePath) {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();


            StorageReference riversRef = firebaseStorage.getReference().child("images/profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + fileName );
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            Uri profilePicUrl =  taskSnapshot.getDownloadUrl();
                            mFirebaseDatabaseReference.child("usersNew").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profilePic").child(lastButtonPressedPosition + "").setValue(profilePicUrl.toString());
                            progressDialog.dismiss();
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    private void saveFullImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Calendar cal = Calendar.getInstance();
        File file = new File(Environment.getExternalStorageDirectory(), cal.getTimeInMillis() +".jpg");
        outputFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_CAMERA_REQUEST_CODE);
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CROPPING_IMAGE_FROM_GALERY_REQUEST_CODE);
    }


    private void onAvatarButtonClicked() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"Take a picture",
                "Choose form existing"};
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                String[] storagePermission = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (which == 0) {
                    saveFullImage();
                  //  openCameraIntent();
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//
//F
//                    } else if (checkSelfPermission(storagePermission[0])
//                            != PackageManager.PERMISSION_GRANTED) {
//                        requestPermissions(storagePermission, PERMISSION_STORAGE_REQUEST_CODE);
//                    } else {
//                        openCameraIntent();
//                    }
                }

                if (which == 1) {
                    showFileChooser();
                }

            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.crash_menu:
//                FirebaseCrash.logcat(Log.ERROR, TAG, "crash caused");
//                causeCrash();
//                return true;
//            case R.id.invite_menu:
//                sendInvitation();
//                return true;
//            case R.id.fresh_config_menu:
//                fetchConfig();
//                return true;
            case R.id.matches_screen_menu:
                startActivity(new Intent(this, MatchesActivity.class));
                return true;
            case R.id.profile_page_menu:
                startActivity(new Intent(this, ProphileActivity.class));
                return true;
            case R.id.sign_out_menu:
//                mFirebaseAuth.signOut();
//                mUsername = ANONYMOUS;
//                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
