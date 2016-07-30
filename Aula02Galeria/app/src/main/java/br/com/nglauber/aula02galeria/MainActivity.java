package br.com.nglauber.aula02galeria;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_GALLERY_REQUEST = 0;
    private static final int IMAGE_CAMERA_REQUEST = 1;

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showImage(mBitmap);
    }

    public void galeriaClick(View view) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_GALLERY_REQUEST);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                    IMAGE_GALLERY_REQUEST);
        }        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK){
            new LoadImageTask(this).execute(data.getData());
        }
    }

    public void showImage(Bitmap bitmap) {
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        if (imageView != null){
            imageView.setImageBitmap(bitmap);
        }
    }

    class LoadImageTask extends AsyncTask<Uri, Void, Bitmap> {

        WeakReference<MainActivity> mActivity;

        public LoadImageTask(MainActivity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        public MainActivity getActivity() {
            return mActivity.get();
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {
            if (getActivity() != null) {
                try {
                    return BitmapFactory.decodeStream(
                            getActivity().getContentResolver().openInputStream(params[0]));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (getActivity() != null){
                mBitmap = bitmap;
                getActivity().showImage(bitmap);
            }
        }
    }
}
