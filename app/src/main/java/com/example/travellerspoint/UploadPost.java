package com.example.travellerspoint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.travellerspoint.data.MyDbHandler;
import com.example.travellerspoint.data.SharedPreference;
import com.example.travellerspoint.model.Post;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UploadPost extends AppCompatActivity {
    private View include;
    private Toolbar toolbar;
    private ImageView cancel, done;
    private ImageView crop, layer, image;
    private CardView gallery, camera;
    private Button btnDone;
    private EditText description;

    private String picturePath;
    private File photoFile;
    private AlertDialog dialog;
    private AlertDialog.Builder dBuilder;
    private byte[] imageBytes = null;
    private final int GALLERY_REQUEST_CODE = 1000;
    private final int CAMERA_REQUEST_CODE = 100;
    MyDbHandler db = new MyDbHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        include = (View) findViewById(R.id.navigation_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar_new_post);
        cancel = findViewById(R.id.cancle);
        done = findViewById(R.id.done);
        gallery = findViewById(R.id.gallery);
        image = findViewById(R.id.new_post_image);
        camera = findViewById(R.id.camera);
        description = findViewById(R.id.description);
        btnDone = findViewById(R.id.btn_done);

        if (ContextCompat.checkSelfPermission(UploadPost.this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UploadPost.this, new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_REQUEST_CODE);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogue(UploadPost.this);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UploadPost.this, "Done", Toast.LENGTH_SHORT).show();

                if (imageBytes == null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    // Create a Matrix object to apply rotation
                    Matrix rotationMatrix = new Matrix();
                    // Set the rotation angle
                    rotationMatrix.postRotate(90);
                    // Apply the transformation to the Bitmap object
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotationMatrix, true);
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imageBytes = stream.toByteArray();
                }
                //Get description from text view
                String postDescription = description.getText().toString();

                //Get current username
                String UserName = SharedPreference.getDefaults("USERNAME", getApplicationContext());

                // Insert the image into the database
                Post post = new Post();
                post.setProfileUrl(imageBytes);
                post.setUser_name(UserName);
                post.setImageUrl(imageBytes);
                post.setLikes(null);
                post.setAuthor(UserName);
                post.setDescription(postDescription);
                post.setCom_count(null);
                post.setDate(getDateTime());
                Boolean insert = db.addPost(post);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQUEST_CODE);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UploadPost.this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(UploadPost.this, new String[]{
                            Manifest.permission.CAMERA
                    }, CAMERA_REQUEST_CODE);
                }
//                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(iCamera, CAMERA_REQUEST_CODE);

                // Create a new file to store the captured image
                photoFile = null;
                try {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e("roshanTest", "Error: " + ex.getMessage());
                }

// If the file was created successfully, launch the camera app to capture the image
                if (photoFile != null) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoURI = FileProvider.getUriForFile(UploadPost.this, "com.example.android.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            if (requestCode == GALLERY_REQUEST_CODE) {
//                assert data != null;
//                image.setImageURI(data.getData());
//                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            } else {
//                image.setImageResource(R.color.black);
//            }

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected image
            image.setImageURI(data.getData());
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Uri uri = data.getData();

            // Convert the selected image to a byte array
            imageBytes = getBytesFromUri(uri);

        } else {
                image.setImageResource(R.color.black);
        }

//            if(requestCode == CAMERA_REQUEST_CODE) {
//                try {
//                    Bitmap img = (Bitmap)(data.getExtras().get("data"));
//                    image.setImageBitmap(img);
//                } catch (Exception e){
//                    Log.e("roshanTest", "onActivityResult: " + e.getMessage());
//                }
//            }
//        } catch (Exception e) {
//            Log.e("roshanTest", "onActivityResult: " + e.getMessage());
//        }

        //New Code here (Testing)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Load the captured image into the ImageView
            Glide.with(this)
                    .load(photoFile)
                    .into(image);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    private String getDateTime(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    //To convert uri to byte code
    private byte[] getBytesFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showAlertDialogue(Context context){
        dBuilder = new AlertDialog.Builder(context);
        dBuilder.setTitle("Confirm Post");
        dBuilder.setMessage("Upload Post?");
        dBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (imageBytes == null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    // Create a Matrix object to apply rotation
                    Matrix rotationMatrix = new Matrix();
                    // Set the rotation angle
                    rotationMatrix.postRotate(90);
                    // Apply the transformation to the Bitmap object
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotationMatrix, true);
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imageBytes = stream.toByteArray();
                }
                //Get description from text view
                String postDescription = description.getText().toString();

                //Get current username
                String UserName = SharedPreference.getDefaults("USERNAME", getApplicationContext());

                // Insert the image into the database
                Post post = new Post();
                post.setProfileUrl(imageBytes);
                post.setUser_name(UserName);
                post.setImageUrl(imageBytes);
                post.setLikes(null);
                post.setAuthor(UserName);
                post.setDescription(postDescription);
                post.setCom_count(null);
                post.setDate(getDateTime());
                Boolean insert = db.addPost(post);
                Toast.makeText(UploadPost.this, "Post Uploaded Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        dialog = dBuilder.create();
        dialog.show();
    }
}